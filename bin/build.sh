#!/usr/bin/env bash

function build {
  echo "Attempting to build app"
  results=$(mvn clean install)
  echo $results
}

build