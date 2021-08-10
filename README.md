## Deployment Tips

### 1. How to package

Due to the production packages are standard docker images, in order to avoid the docker error during the maven build/package phase for non-production environment, please follow the coming build commands:

    - mvn clean install -Dmaven.test.skip=true -DskipDocker  (only build and install, no docker related operations, for local environment)
    - mvn clean package -Dmaven.test.skip-true -DskipDockerTag -DskipDockerPush (build, package and create docker image, for test environment)
    - mvn clean package -Dmaven.test.skip=true (build, package, create docker image and uploding the images to private registry, for production environment)

### 2. Test Environment Deployment

For test environment, if the application does not run with docker containers, you must setup following system level environment variables to config somme connection information, such as mysql database connection, redis connection and eureka discovery center connection.

> MySQL connection information, including db host, db port, db name, db user and password.

- INSURANCE_FINANCE_DB_HOST=119.23.59.161
- INSURANCE_FINANCE_DB_PORT=3306
- INSURANCE_FINANCE_DB_NAME=insurance_finance
- INSURANCE_FINANCE_DB_USER=root
- INSURANCE_FINANCE_DB_PASSWORD=123456

> Redis connection information, including redis db index, redis host and redis port, password setting is not supported for test environment.

- INSURANCE_FINANCE_REDIS_DB=1
- INSURANCE_FINANCE_REDIS_HOST=localhost
- INSURANCE_FINANCE_REDIS_PORT=6379

> Eureka service discovery center connection information, including the discovery host and port. 

- INSURANCE_FINANCE_DISCOVERY_HOST=localhost
- INSURANCE_FINANCE_DISCOVERY_PORT=1111

**After prepare the environment file in linux system, you can use "source" command to activation the environment variables and use command "env" to check the value.**

