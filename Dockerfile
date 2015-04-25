##########################################################
# Dockerfile which builds a base image with oracle-java8.
##########################################################
FROM dockerfile/java:oracle-java8

#ADD ./target/fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz /opt

#WORKDIR /opt/fagark-reaktiv-logistikk-0.1-SNAPSHOT

#FROM maven:3-jdk-8

RUN apt-get update && apt-get install -y maven

ADD . /home

WORKDIR /home

RUN mvn package

RUN cp target/fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz /opt

WORKDIR /opt

RUN tar -zxf fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz && rm fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz

WORKDIR ./fagark-reaktiv-logistikk-0.1-SNAPSHOT
