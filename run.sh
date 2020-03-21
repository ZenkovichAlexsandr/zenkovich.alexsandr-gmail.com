#!/bin/bash

./gradlew clean build
docker-compose -f ./docker/docker-compose.yml up -d