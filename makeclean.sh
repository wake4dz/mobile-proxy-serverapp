#!/bin/bash

PWD=$(pwd)
echo $PWD
winpty docker run -it --rm --name maven-build -v "/${PWD}":/usr/src/app -w //usr/src/app maven:3.6.2-jdk-8 bash -c "pwd && ls -lah && mvn clean install"
