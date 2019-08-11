#ARG OPENJDK_TAG=8u212
#FROM openjdk:${OPENJDK_TAG} AS builder
#
#ARG SBT_VERSION=1.2.8
#
#RUN \
#  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
#  dpkg -i sbt-$SBT_VERSION.deb && \
#  rm sbt-$SBT_VERSION.deb && \
#  apt-get update && \
#  apt-get install sbt
#
#WORKDIR /app
#ADD . /app
#
#RUN sbt streamConsumerService/avroScalaGenerateSpecific
#RUN sbt assembly
#
#
#FROM openjdk:8-jdk-alpine
#
#LABEL org.label-schema.vendor="Stream Consumer Service" \
#      maintainer="Mikolaj Komisarek <mikolajkomisarek@outlook.com>"
#
#WORKDIR /app/
#COPY --from=builder /app/target/scala-2.12/app-assembly.jar /app.jar
#
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM bde2020/spark-scala-template:2.4.0-hadoop2.7


ENV SPARK_APPLICATION_MAIN_CLASS pl.com.itti.Main