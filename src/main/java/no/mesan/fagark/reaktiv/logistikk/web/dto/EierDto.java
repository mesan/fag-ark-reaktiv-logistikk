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
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

@XmlRootElement(name = "eier")
public class EierDto {

    public String id;
    public Date opprettetDato;
    public Date sistOppdatert;

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    public List<Link> links = new ArrayList<Link>();

    @XmlElement(name = "eiendel")
    public List<EiendelDto> eiendelerDto;

    public EierDto() {
        super();
    }

    @Override
    public String toString() {
        return "Eier [id=" + id + ", eiendeler=" + eiendelerDto + "]";
    }

    public EierDto(final String id, final Date opprettetDato, final Date sistOppdatert, final List<EiendelDto> eiendelerDto) {

        super();
        this.id = id;
        this.opprettetDato = opprettetDato;
        this.sistOppdatert = sistOppdatert;
        this.eiendelerDto = eiendelerDto;
    }

    public static EierDto create(final Eier eier) {
        if (eier instanceof Eier) {
            return new EierDto(eier.getId(), eier.getOpprettetDato(), eier.getSistOppdatert(), new ArrayList<EiendelDto>());
        }

        return new EierDto();
    }

    public EierDto withEiendeler(final List<Eiendel> eiendeler) {
        final List<EiendelDto> createdEiendeler = new ArrayList<EiendelDto>();
        eiendeler.forEach(e -> createdEiendeler.add(EiendelDto.create(e)));
        return this;
    }

    public EierDto withLinks(final List<Link> links) {
        this.links = links;
        return this;
    }

}
