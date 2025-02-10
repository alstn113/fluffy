#!/bin/bash

NGINX_CONFIG_PATH="/etc/nginx/sites-available/api.fluffy.run"

BLUE_PORT=8080
GREEN_PORT=8081

BLUE_HEALTH_CHECK_URL="http://localhost:8082/actuator/health"
GREEN_HEALTH_CHECK_URL="http://localhost:8083/actuator/health"

HEALTH_CHECK_ATTEMPTS=10
HEALTH_CHECK_DELAY=5
BEFORE_HEALTH_CHECK_DELAY=30

health_check() {
    local target_url=$1

    echo "Performing health check for $target_url (attempts: $HEALTH_CHECK_ATTEMPTS, delay: $HEALTH_CHECK_DELAY)..."

    for i in $(seq 1 $HEALTH_CHECK_ATTEMPTS); do
        response=$(curl -s -o /dev/null -w "%{http_code}" "$target_url")

        if [ "$response" -eq 200 ]; then
            echo "Health check attempt ($i/$HEALTH_CHECK_ATTEMPTS) passed"
            echo "Health check successful for $target_url"
            return 0
        else 
            echo "Health check attempt ($i/$HEALTH_CHECK_ATTEMPTS) failed"
        fi

        sleep $HEALTH_CHECK_DELAY
    done

    echo "Health check failed for $target_url"
    return 1
}

switch_container() {
    local prev_container=$1
    local prev_port=$2
    local next_container=$3
    local next_port=$4
    local health_check_url=$5

    echo "Starting $next_container (port: $next_port)..."
    docker compose -f compose.yml up "$next_container" -d

    echo "Waiting for $next_container to start..."
    sleep $BEFORE_HEALTH_CHECK_DELAY

    if ! health_check "$health_check_url"; then
        echo "Health check failed, rolling back..."
        docker compose -f compose.yml down "$next_container"
        return
    fi

    echo "Updating Nginx configuration... $prev_container (port: $prev_port) -> $next_container (port: $next_port)"
    sed -i "s/server localhost:$prev_port/server localhost:$next_port/" "$NGINX_CONFIG_PATH"

    echo "Reloading Nginx configuration..."
    if ! sudo nginx -s reload; then
        echo "Failed to reload Nginx: $(sudo nginx -t 2>&1)"
        return
    fi

    echo "$next_container is now live, stopping $prev_container..."
    docker compose -f compose.yml down "$prev_container"
}

IS_GREEN=$(docker container ps | grep app-green)

if [ -z "$IS_GREEN" ]; then
    echo "### BLUE >> GREEN ###"
    switch_container "app-blue" "$BLUE_PORT" "app-green" "$GREEN_PORT" "$GREEN_HEALTH_CHECK_URL"
else
    echo "### GREEN >> BLUE ###"
    switch_container "app-green" "$GREEN_PORT" "app-blue" "$BLUE_PORT" "$BLUE_HEALTH_CHECK_URL"
fi
