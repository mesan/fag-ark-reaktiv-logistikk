package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.Date;

import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;

public class KontrollMeldingDto {


    public int id;
    public String eierId;
    public int eiendelId;
    public String melding;
    public Date opprettetDato;

    public KontrollMeldingDto() {
        super();
    }

    public KontrollMeldingDto(final int id, final String eierId, final String melding,
            final Date opprettetDato) {
        super();
        this.id = id;
        this.eierId = eierId;
        eiendelId = eiendelId;
        this.melding = melding;
        this.opprettetDato = opprettetDato;
    }

    public static KontrollMeldingDto create(final KontrollMelding melding) {
        if (melding instanceof KontrollMelding) {
            return new KontrollMeldingDto(melding.id, melding.eierId, melding.toString(),
                    melding.opprettetDato);
        }

        return new KontrollMeldingDto();
    }
}
