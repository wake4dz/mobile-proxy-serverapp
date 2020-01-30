#!/bin/bash

PWD=$(pwd)
echo $PWD
winpty docker run -it --rm --name maven-clean-install -v "$PWD":"/usr/src/app" -w /usr/src/app maven:3.2-jdk-7 mvn clean install
