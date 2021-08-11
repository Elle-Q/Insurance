#!/usr/bin/env bash

cd /data/web/api-server


cd ./com.fintech.insurance.micro.biz && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.customer && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.finance && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.system && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.support && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.thirdparty && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd ../com.fintech.insurance.micro.retrieval && mvn clean package docker:build -Dmaven.test.skip=true -DskipDockerTag -DskipDockerPush

cd /data/web/api-server

docker rmi $(docker images | grep "none" | awk '{print $3}')