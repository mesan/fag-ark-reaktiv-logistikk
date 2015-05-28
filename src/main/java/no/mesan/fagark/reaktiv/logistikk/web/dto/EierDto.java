package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

@XmlRootElement(name = "eier")
public class EierDto {

    public String id;
    public Date opprettetDato;
    public Date sistOppdatert;
    @XmlElement(name = "eiendel")
    public List<EiendelDto> eiendelerDto;

    public EierDto() {
        super();
    }

    @Override
    public String toString() {
        return "Eier [id=" + id + ", eiendeler="
                + eiendelerDto + "]";
    }

    public EierDto(final String id, final Date opprettetDato,
 final Date sistOppdatert,
 final List<EiendelDto> eiendelerDto) {
        super();
        this.id = id;
        this.opprettetDato = opprettetDato;
        this.sistOppdatert = sistOppdatert;
        this.eiendelerDto = eiendelerDto;

    }

    public static EierDto create(final Eier eier) {
        if (eier instanceof Eier) {
            final List<EiendelDto> createdEiendeler = new ArrayList<EiendelDto>();
            eier.getEiendeler().forEach(e -> createdEiendeler.add(EiendelDto.create(e)));
            return new EierDto(eier.getId(), eier.getOpprettetDato(), eier.getSistOppdatert(), createdEiendeler);
        }

        return new EierDto();
    }

}
