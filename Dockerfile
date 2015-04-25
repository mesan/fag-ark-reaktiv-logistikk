##########################################################
# Dockerfile which builds a base image with oracle-java8.
##########################################################
FROM dockerfile/java:oracle-java8

ADD ./target/fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz /opt

#WORKDIR /opt



#RUN tar -zxf /opt/fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz

WORKDIR /opt/fagark-reaktiv-logistikk-0.1-SNAPSHOT
