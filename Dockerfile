FROM mcr.microsoft.com/playwright/java:v1.51.0-noble

# Usage:
# 1. build jar:
# mvn clean package -DskipTests
#
# 2. build image:
#   docker build -t ui-test:latest
#
# 3. run container:
#   docker run ui-test:latest
#       -e LOCAL_EMAIL =
#       -e LOCAL_PASSWORD =
#       -e LOCAL_BASE_URL =

USER root
WORKDIR /

ENV DOCKER_RUN=1
ENV APP_OPTIONS="browserType=CHROMIUM;headlessMode=true;slowMoMode=0;viewportWidth=1920;viewportHeight=964;tracingMode=true;videoMode=true;videoWidth=1920;videoHeight=957;closeBrowserIfError=true;artefactDir=target/artefact;defaultTimeout=5000;additionalRetries=0;colorScheme=DARK;DEBUG=''"


COPY target/npgw-ui-test-*-jar-with-dependencies.jar /npgw-ui-test-jar-with-dependencies.jar
COPY testng.xml .

ENTRYPOINT ["java", "-jar", "npgw-ui-test-jar-with-dependencies.jar"]
