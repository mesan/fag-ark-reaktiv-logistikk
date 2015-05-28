package no.mesan.fagark.reaktiv.logistikk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import no.mesan.fagark.reaktiv.logistikk.domain.constraint.MinstEnEiendel;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;

import org.hibernate.validator.constraints.NotBlank;


public class Eier implements Serializable, Kontrollerbar {

    private static final long serialVersionUID = 3248313757240321024L;

    @NotBlank(message = "Eiendel mangler eierid.")
    private final String id;
    private final Date opprettetDato;
    private Date sistOppdatert;


    @MinstEnEiendel
    private final List<Eiendel> eiendeler = new ArrayList<Eiendel>();

    public Eier(final String id, final List<EiendelDto> eiendelerDto) {
        super();
        this.id = id;
        opprettetDato = new Date(System.currentTimeMillis());
        sistOppdatert = opprettetDato;
        eiendelerDto.forEach(e -> eiendeler.add(Eiendel.create(e)));
    }

    public static Eier create(final EierDto eierDto) {
        if (eierDto instanceof EierDto) {

            final List<Eiendel> createdEiendeler = new ArrayList<Eiendel>();
            eierDto.eiendelerDto.forEach(e -> createdEiendeler.add(Eiendel.create(e)));
            return new Eier(eierDto.id, eierDto.eiendelerDto);
        }

        return null;
    }

    public void oppdater(final Eier eier) {
        sistOppdatert = new Date(System.currentTimeMillis());
        oppdater(eier.getEiendeler());
    }

    public void oppdater(final List<Eiendel> mottatEiendeler) {
        mottatEiendeler.forEach(m -> oppdater(m));
    }

    public void oppdater(final Eiendel mottatEiendel) {

        final Optional<Eiendel> funnet = finn(mottatEiendel.getId());
        if (funnet.isPresent()) {
            final Eiendel eksisterende = funnet.get();
            eksisterende.setBeskrivelse(mottatEiendel.getBeskrivelse());
            eksisterende.setTekniskBeskrivelse(mottatEiendel.getTekniskBeskrivelse());
            eksisterende.setSistOppdatert(new Date(System.currentTimeMillis()));
        } else {
            eiendeler.add(mottatEiendel);
        }
    }

    public void slett(final Eiendel eiendel) {
        eiendeler.remove(eiendel);
    }

    public Optional<Eiendel> finn(final int eiendelId) {
        final Predicate<Eiendel> eiendelTilhorer = (e -> e.getId() == eiendelId);
        return eiendeler.stream().filter(eiendelTilhorer).findFirst();
    }

    @Override
    public String toString() {
        return "Eier [id=" + id + ", eiendeler=" + eiendeler + "]";
    }

    public String getId() {
        return id;
    }

    public Date getOpprettetDato() {
        return opprettetDato;
    }

    public Date getSistOppdatert() {
        return sistOppdatert;
    }

    public void setSistOppdatert(final Date sistOppdatert) {
        this.sistOppdatert = sistOppdatert;
    }

    public List<Eiendel> getEiendeler() {
        return eiendeler;
    }
}
