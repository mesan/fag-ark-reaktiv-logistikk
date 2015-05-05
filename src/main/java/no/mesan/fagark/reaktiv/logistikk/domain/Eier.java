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


public class Eier implements Serializable {

    private static final long serialVersionUID = 3248313757240321024L;

    private final String id;
    private String fornavn;
    private String etternavn;
    private final Date opprettetDato;

    private Date sistOppdatert;

    @MinstEnEiendel
    private final List<Eiendel> eiendeler = new ArrayList<Eiendel>();

    public Eier(final String id, final String fornavn, final String etternavn,
            final List<EiendelDto> eiendelerDto) {
        super();
        this.id = id;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        opprettetDato = new Date(System.currentTimeMillis());
        sistOppdatert = opprettetDato;
        eiendelerDto.forEach(e -> eiendeler.add(Eiendel.create(e)));
    }

    public static Eier create(final EierDto e) {
        if (e instanceof EierDto) {
            return new Eier(e.id, e.fornavn, e.etternavn, e.eiendelerDto);
        }

        return null;
    }
    @Override
    public String toString() {
        return "Eier [id=" + id + ", fornavn=" + fornavn + ", etterNavn=" + etternavn + ", eiendeler=" + eiendeler + "]";
    }

    public String getId() {
        return id;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
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

    public void setFornavn(final String fornavn) {
        this.fornavn = fornavn;
    }

    public void setEtternavn(final String etternavn) {
        this.etternavn = etternavn;
    }

    public List<Eiendel> getEiendeler() {
        return eiendeler;
    }

    public void oppdater(final List<Eiendel> mottatEiendeler) {
        mottatEiendeler.forEach(m -> oppdater(m));
    }

    public void oppdater(final Eiendel mottatEiendel) {
        final Predicate<Eiendel> eiendelTilhorer = (e -> e.getId() == mottatEiendel.getId());
        final Optional<Eiendel> funnet = eiendeler.stream().filter(eiendelTilhorer).findFirst();

        if (funnet.isPresent()) {
            final Eiendel eksisterende = funnet.get();
            eksisterende.setBeskrivelse(mottatEiendel.getBeskrivelse());
            eksisterende.setTekniskBeskrivelse(mottatEiendel.getTekniskBeskrivelse());
            eksisterende.setSistOppdatert(new Date(System.currentTimeMillis()));
        } else {
            eiendeler.add(mottatEiendel);
        }
    }

}
