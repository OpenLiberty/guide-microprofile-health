#!/bin/bash
set -euxo pipefail

##############################################################################
##
##  GH actions CI test script
##
##############################################################################

# LMP 3.0+ goals are listed here: https://github.com/OpenLiberty/ci.maven#goals

sed -i 's/0.9/1.1/' ../finish/src/main/java/io/openliberty/guides/system/SystemLivenessCheck.java
cat ../finish/src/main/java/io/openliberty/guides/system/SystemLivenessCheck.java
sed -i 's/0.95/1.1/' ../finish/src/main/java/io/openliberty/guides/system/SystemStartupCheck.java
cat ../finish/src/main/java/io/openliberty/guides/system/SystemStartupCheck.java
sed -i 's/0.9/1.1/' ../finish/src/main/java/io/openliberty/guides/inventory/InventoryLivenessCheck.java
cat ../finish/src/main/java/io/openliberty/guides/inventory/InventoryLivenessCheck.java
sed -i 's/0.95/1.1/' ../finish/src/main/java/io/openliberty/guides/inventory/InventoryStartupCheck.java
cat ../finish/src/main/java/io/openliberty/guides/inventory/InventoryStartupCheck.java

## Rebuild the application
#       package                   - Take the compiled code and package it in its distributable format.
#       liberty:create            - Create a Liberty server.
#       liberty:install-feature   - Install a feature packaged as a Subsystem Archive (esa) to the Liberty runtime.
#       liberty:deploy            - Copy applications to the Liberty server's dropins or apps directory.
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

## Run the tests
# These commands are separated because if one of the commands fail, the test script will fail and exit.
# e.g if liberty:start fails, then there is no need to run the failsafe commands.
#       liberty:start             - Start a Liberty server in the background.
#       failsafe:integration-test - Runs the integration tests of an application.
#       liberty:stop              - Stop a Liberty server.
#       failsafe:verify           - Verifies that the integration tests of an application passed.
mvn liberty:start

sleep 5

curl http://localhost:9080/health | jq

mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    failsafe:integration-test liberty:stop
mvn failsafe:verify
