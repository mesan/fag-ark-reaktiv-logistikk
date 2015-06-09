#!/bin/sh

/opt/logstash-forwarder/logstash-forwarder -config /etc/logstashConfig/config.json &
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar /opt/fagark-reaktiv-logistikk-0.1-SNAPSHOT/bin/fagark-reaktiv-logistikk-0.1-SNAPSHOT.jar

