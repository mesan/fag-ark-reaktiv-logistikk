# fag-ark-reaktiv-logistikk
Faggruppe 2015 arkitektur gruppe 3 - logistikktjenesten


Har deaktivert integrasjonen med dockerhub. For å lage et lokalt image, følg stegene...

mvn package

docker build -t mesanfagark/reaktiv-logistikk .

docker run -i -t -p 9999:9999 --name logistikk mesanfagark/reaktiv-logistikk java -jar bin/fagark-reaktiv-logistikk-0.1-SNAPSHOT.jar
