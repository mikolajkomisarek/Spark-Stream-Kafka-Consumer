ARG OPENJDK_TAG=8u212
FROM openjdk:${OPENJDK_TAG} AS builder

ARG SBT_VERSION=1.2.8

RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt

WORKDIR /app
ADD . /app

RUN sbt streamConsumerService/avroScalaGenerateSpecific
RUN sbt assembly


FROM bde2020/spark-submit:2.1.0-hadoop2.8-hive-java8

RUN mkdir -p /app
COPY --from=builder /app/target/scala-2.12/app-assembly.jar /app/spark-examples.jar
#COPY examples/target/original-spark-examples_2.11-2.3.0-SNAPSHOT.jar /app/spark-examples.jar

ENV SPARK_MASTER_NAME spark-master
ENV SPARK_MASTER_PORT 7077
ENV SPARK_APPLICATION_JAR_LOCATION /app/spark-examples.jar
ENV SPARK_APPLICATION_MAIN_CLASS pl.com.itti.Main
ENV SPARK_APPLICATION_ARGS ""


#FROM openjdk:8-jdk-alpine
#
#LABEL org.label-schema.vendor="Stream Consumer Service" \
#      maintainer="Mikolaj Komisarek <mikolajkomisarek@outlook.com>"
#
#WORKDIR /app/
#COPY --from=builder /app/target/scala-2.12/app-assembly.jar /app.jar
#
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

