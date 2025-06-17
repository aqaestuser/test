FROM mcr.microsoft.com/playwright/java:v1.51.0-noble

# Usage:
# 1. build jar:
# mvn clean package -DskipTests
#
# 2. build image:
#   cd ../
#   docker build -t ui-test:latest -f ./docker/Dockerfile .
#
# 3. run container:
#   docker run ui-test:latest
#
#
# required in .env:
# local.baseURL=<>
# local.email=<>
# local.password=<>
# local.headlessMode=true

USER root
WORKDIR /

COPY target/npgw-ui-test-*-jar-with-dependencies.jar /npgw-ui-test-jar-with-dependencies.jar
COPY config/ config/

ENTRYPOINT ["java", "-jar", "npgw-ui-test-jar-with-dependencies.jar"]
