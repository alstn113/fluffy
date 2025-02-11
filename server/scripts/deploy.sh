#!/bin/bash

NGINX_CONFIG_PATH="/etc/nginx/sites-available/api.fluffy.run"

APP1_BLUE="app1-blue"
APP1_GREEN="app1-green"
APP2_BLUE="app2-blue"
APP2_GREEN="app2-green"

APP1_BLUE_PORT=8080
APP1_GREEN_PORT=8081
APP2_BLUE_PORT=8082
APP2_GREEN_PORT=8083

APP1_BLUE_HEALTH_CHECK_URL="http://localhost:8090/actuator/health"
APP1_GREEN_HEALTH_CHECK_URL="http://localhost:8091/actuator/health"
APP2_BLUE_HEALTH_CHECK_URL="http://localhost:8092/actuator/health"
APP2_GREEN_HEALTH_CHECK_URL="http://localhost:8093/actuator/health"

HEALTH_CHECK_ATTEMPTS=10
HEALTH_CHECK_DELAY=5
BEFORE_HEALTH_CHECK_DELAY=60

health_check() {
    local app1_health_check_url=$1
    local app2_health_check_url=$2

    echo "헬스 체크를 시작하기 전에 $BEFORE_HEALTH_CHECK_DELAY 초 대기합니다."
    sleep $BEFORE_HEALTH_CHECK_DELAY

    echo "헬스 체크 시작 (app1: $app1_health_check_url, app2: $app2_health_check_url), (시도: $HEALTH_CHECK_ATTEMPTS, 딜레이: $HEALTH_CHECK_DELAY)"

    for i in $(seq 1 $HEALTH_CHECK_ATTEMPTS); do
        app1_response=$(curl -s -o /dev/null -w "%{http_code}" "$app1_health_check_url")
        app2_response=$(curl -s -o /dev/null -w "%{http_code}" "$app2_health_check_url")

        if [ "$app1_response" -eq 200 ] && [ "$app2_response" -eq 200 ]; then
            echo "헬스 체크 성공 (app1: $app1_response, app2: $app2_response) (시도: $i/$HEALTH_CHECK_ATTEMPTS)"
            return 0
        else
            echo "헬스 체크 실패 (app1: $app1_response, app2: $app2_response) (시도: $i/$HEALTH_CHECK_ATTEMPTS)"
        fi

        sleep $HEALTH_CHECK_DELAY
    done

    echo "모든 헬스 체크 시도가 실패했습니다."
    return 1
}

switch_container() {
    local prev_container=$1

    local prev_app1
    local prev_app2
    local prev_app1_port
    local prev_app2_port

    local next_app1
    local next_app2
    local next_app1_port
    local next_app2_port

    local app1_health_check_url
    local app2_health_check_url

    if [ "$prev_container" = "app-blue" ]; then
        prev_app1="$APP1_BLUE"
        prev_app2="$APP2_BLUE"
        prev_app1_port="$APP1_BLUE_PORT"
        prev_app2_port="$APP2_BLUE_PORT"

        next_app1="$APP1_GREEN"
        next_app2="$APP2_GREEN"
        next_app1_port="$APP1_GREEN_PORT"
        next_app2_port="$APP2_GREEN_PORT"

        app1_health_check_url="$APP1_GREEN_HEALTH_CHECK_URL"
        app2_health_check_url="$APP2_GREEN_HEALTH_CHECK_URL"
    elif [ "$prev_container" = "app-green" ]; then
        prev_app1="$APP1_GREEN"
        prev_app2="$APP2_GREEN"
        prev_app1_port="$APP1_GREEN_PORT"
        prev_app2_port="$APP2_GREEN_PORT"

        next_app1="$APP1_BLUE"
        next_app2="$APP2_BLUE"
        next_app1_port="$APP1_BLUE_PORT"
        next_app2_port="$APP2_BLUE_PORT"

        app1_health_check_url="$APP1_BLUE_HEALTH_CHECK_URL"
        app2_health_check_url="$APP2_BLUE_HEALTH_CHECK_URL"
    fi

    echo "$next_app1, $next_app2 를 시작합니다."
    docker compose -f compose.yml up -d "$next_app1" "$next_app2"

    if ! health_check "$app1_health_check_url" "$app2_health_check_url"; then
        docker compose -f compose.yml down "$next_app1" "$next_app2"
        return
    fi

    # NGINX 에서 server localhost:xxxx 부분을 변경
    echo "NGINX 설정을 변경합니다."
    sed -i "s/server localhost:$prev_app1_port/server localhost:$next_app1_port/" "$NGINX_CONFIG_PATH"
    sed -i "s/server localhost:$prev_app2_port/server localhost:$next_app2_port/" "$NGINX_CONFIG_PATH"

    if ! sudo nginx -s reload; then
        echo "NGINX 를 reload 하는데 실패했습니다. $(sudo nginx -t 2>&1)"
        return
    fi

    echo "$prev_app1, $prev_app2 를 중지합니다."
    docker compose -f compose.yml down "$prev_app1" "$prev_app2"

    echo "컨테이너 상태"
    docker ps -a
}

IS_GREEN=$(docker container ps | grep "$APP1_GREEN")

if [ -z "$IS_GREEN" ]; then
    echo "### BLUE >> GREEN ###"
    switch_container "app-blue"
else
    echo "### GREEN >> BLUE ###"
    switch_container "app-green"
fi
