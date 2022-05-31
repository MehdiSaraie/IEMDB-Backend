#!/bin/bash

docker build -t iemdb_image:0.1 .
docker run -p 9009:8080 iemdb_image:0.1