# fag-ark-reaktiv-logistikk
Faggruppe 2015 arkitektur gruppe 3 - logistikktjenesten

Logistikk tjenesten er tilgjengelig ved 

docker run -i -t -p 9999:9999 --name logistikk mesanfagark/reaktiv-logistikk

Deretter kan den startes med 
docker start logistikk


(Har ikke testet ut denne på Windows)


Logistikk API

/logistikk/eier/1

PUT
Content-Type: application/json
{"eiendel":[{"id":1,"navn":"Telefon","tekniskBeskrivelse":"Sony","beskrivelse":"Svart telefon med en kjempe sprekk i skjerm."},{"id":2,"navn":"Caps","tekniskBeskrivelse":"Caps","beskrivelse":"Blå caps"}]}

GET
Accept: application/json




