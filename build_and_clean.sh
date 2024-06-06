#!/bin/bash

# Start the services and run tests
docker-compose up --build

# Remove the test container after it exits
docker-compose rm -f test
