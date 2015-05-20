##########################################################
# Dockerfile which builds a base image with oracle-java8.
##########################################################
FROM maven:3-jdk-8

ADD . /home

WORKDIR /home

RUN mvn package

RUN cp target/fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz /opt

WORKDIR /opt

RUN tar -zxf fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz && rm fagark-reaktiv-logistikk-0.1-SNAPSHOT-bin.tar.gz

WORKDIR ./fagark-reaktiv-logistikk-0.1-SNAPSHOT

CMD ["/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java", "-jar", "bin/fagark-reaktiv-logistikk-0.1-SNAPSHOT.jar"]
