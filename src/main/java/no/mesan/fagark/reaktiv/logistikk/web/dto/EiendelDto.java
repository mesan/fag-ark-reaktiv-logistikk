package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;

@XmlRootElement(name = "eiendel")
public class EiendelDto {

    public int id;
    public String eierid;
    public String navn;
    public String tekniskBeskrivelse;
    public String beskrivelse;
    public Date opprettelseDato;
    public Date sistOppdatert;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    public List<Link> links = new ArrayList<Link>();

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

    public EiendelDto withLink(final Link link) {
        links.add(link);
        return this;
    }

    public EiendelDto withLinks(final List<Link> links) {
        this.links = links;
        return this;
    }

}
