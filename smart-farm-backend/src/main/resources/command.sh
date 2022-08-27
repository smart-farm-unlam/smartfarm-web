#!/bin/bash

# turn on bash's job control
set -m

java -jar /usr/app/app.jar &
uvicorn healthPredict:app

fg %1
