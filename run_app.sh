#!/bin/bash

set -e

SPARK_HOME=/opt/spark3.5.3
MAIN_CLASS=app.TestApp
COMPILE_TARGET=target/scala-2.12/Test-application-1.0.0.jar

sbt compile
sbt assembly
${SPARK_HOME}/bin/spark-submit \
    --class ${MAIN_CLASS} \
    --master local \
    ${COMPILE_TARGET}
