#!/bin/bash

NGINX_CONFIG_PATH="/etc/nginx/sites-available/api.fluffy.run"
HOST_HEALTH_CHECK_ENDPOINT="http://localhost:8082/actuator/health"
HEALTH_CHECK_ATTEMPTS=5
HEALTH_CHECK_DELAY=3

health_check() {
    for i in $(seq 1 $HEALTH_CHECK_ATTEMPTS); do
        echo "Health check attempt ($i/$HEALTH_CHECK_ATTEMPTS)"
        response=$(curl -s -o /dev/null -w "%{http_code}" $HOST_HEALTH_CHECK_ENDPOINT)

        if [ $response -eq 200 ]; then
            echo "Health check passed"
            return 0
        fi

        sleep $HEALTH_CHECK_DELAY
    done

    echo "Health check failed"
    return 1
}

switch_container() {
    local prev_container=$1
    local next_container=$2

    docker compose -f compose.yml up $next_container -d

    if ! health_check; then
        echo "Health check failed, rolling back"
        docker compose -f compose.yml down $next_container
        return 1
    fi

    sed -i "s/server $prev_container:8080;/server $next_container:8080;/" "$NGINX_CONFIG_PATH"

    sudo nginx -s reload
}

IS_GREEN=$(docker container ps | grep app-green)

if [ -z "$IS_GREEN" ]; then
    echo "### BLUE >> GREEN ###"
    switch_container "app-blue" "app-green"
else
    echo "### GREEN >> BLUE ###"
    switch_container "app-green" "app-blue"
fi
