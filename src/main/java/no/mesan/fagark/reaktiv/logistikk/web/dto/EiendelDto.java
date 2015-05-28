package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;

@XmlRootElement(name = "eiendel")
@XmlAccessorType(XmlAccessType.FIELD)
public class EiendelDto {

    public int id;
    public String eierid;
    public String navn;
    public String tekniskBeskrivelse;
    public String beskrivelse;
    public Date opprettelseDato;
    public Date sistOppdatert;

    public EiendelDto() {
        super();
    }

    public EiendelDto(final int id, final String eierid, final String navn, final String tekniskBeskrivelse,
            final String beskrivelse, final Date opprettelseDato, final Date oppdatertDato) {
        super();
        this.id = id;
        this.eierid = eierid;
        this.navn = navn;
        this.tekniskBeskrivelse = tekniskBeskrivelse;
        this.beskrivelse = beskrivelse;
        this.opprettelseDato = opprettelseDato;
        sistOppdatert = oppdatertDato;
    }

    public static EiendelDto create(final Eiendel e){

        if(e instanceof Eiendel){
            return new EiendelDto(e.getId(), e.getEierId(), e.getNavn(), e.getTekniskBeskrivelse(), e.getBeskrivelse(),
                    e.getOpprettelseDato(), e.getSistOppdatert());
        }

        return new EiendelDto();
    }
}
