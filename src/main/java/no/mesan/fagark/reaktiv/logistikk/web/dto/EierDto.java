package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

@XmlRootElement(name = "eier")
public class EierDto {

    public String id;
    public String fornavn;
    public String etternavn;
    public Date opprettetDato;
    public Date sistOppdatert;
    @XmlElement(name = "eiendel")
    public final List<EiendelDto> eiendelerDto = new ArrayList<EiendelDto>();

    public EierDto() {
        super();
    }

    @Override
    public String toString() {
        return "Eier [id=" + id + ", fornavn=" + fornavn + ", etterNavn=" + etternavn + ", eiendeler="
                + eiendelerDto + "]";
    }

    public EierDto(final String id, final String fornavn, final String etternavn, final Date opprettetDato,
 final Date sistOppdatert,
            final List<Eiendel> eiendeler) {
        super();
        this.id = id;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.opprettetDato = opprettetDato;
        this.sistOppdatert = sistOppdatert;
        eiendeler.forEach(e -> eiendelerDto.add(EiendelDto.create(e)));

    }

    public static EierDto create(final Eier e) {
        if (e instanceof Eier) {
            return new EierDto(e.getId(), e.getFornavn(), e.getEtternavn(), e.getOpprettetDato(), e.getSistOppdatert(),
                    e.getEiendeler());
        }

        return new EierDto();
    }

}
