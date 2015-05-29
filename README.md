# fag-ark-reaktiv-logistikk
Faggruppe 2015 arkitektur gruppe 3 - logistikktjenesten

Logistikk tjenesten er tilgjengelig ved 

docker run -i -t -p 9999:9999 --name logistikk mesanfagark/reaktiv-logistikk

Deretter kan den startes med 
docker start logistikk


(Har ikke testet ut denne på Windows)


Logistikk API

URL
/logistikk/eier/1

PUT
Content-Type: application/json
{"eiendel":[{"navn":"Telefon","tekniskBeskrivelse":"Sony","beskrivelse":"Svart telefon med en kjempe sprekk i skjerm."},{"navn":"Caps","tekniskBeskrivelse":"Caps","beskrivelse":"Blå caps"}]}

GET
Accept: application/json

URL
/logistikk/eier/1/eiendel/3

GET
Accept: application/json

PUT
Content-Type: application/json
{"navn":"Telefon","tekniskBeskrivelse":"Sony","beskrivelse":"Svart telefon med en kjempe sprekk i skjerm."}
