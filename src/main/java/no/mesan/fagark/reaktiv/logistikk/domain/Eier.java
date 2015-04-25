package no.mesan.fagark.reaktiv.logistikk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;


public class Eier implements Serializable {

    private static final long serialVersionUID = 3248313757240321024L;

    private final int id;
    private final String fornavn;
    private final String etternavn;
    private final Date opprettetDato;

    private final List<Eiendel> eiendeler = new ArrayList<Eiendel>();

    public Eier(final int id, final String fornavn, final String etternavn,
            final List<EiendelDto> eiendelerDto) {
        super();
        this.id = id;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        opprettetDato = new Date(System.currentTimeMillis());
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

    public int getId() {
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

    public List<Eiendel> getEiendeler() {
        return eiendeler;
    }

}
