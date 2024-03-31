#!/usr/bin/env bash

./mvnw clean package
sudo docker build -t ask-api:latest .
