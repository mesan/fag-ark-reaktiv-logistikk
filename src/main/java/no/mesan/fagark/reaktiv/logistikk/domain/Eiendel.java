package no.mesan.fagark.reaktiv.logistikk.domain;

import java.io.Serializable;
import java.util.Date;

import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;

public class Eiendel implements Serializable {

    private static final long serialVersionUID = -3338988285265033207L;
    private final int id;
    private final int eierId;

    private final String navn;
    private final String tekniskBeskrivelse;
    private final String beskrivelse;
    private final Date opprettelseDato;


    public Eiendel(final int id, final int eierId, final String navn, final String tekniskBeskrivelse, final String beskrivelse,
            final Date opprettelseDato) {
        super();
        this.id = id;
        this.eierId = eierId;
        this.navn = navn;
        this.tekniskBeskrivelse = tekniskBeskrivelse;
        this.beskrivelse = beskrivelse;
        this.opprettelseDato = opprettelseDato;
    }

    public static Eiendel create(final EiendelDto e) {
        if (e instanceof EiendelDto) {
            return new Eiendel(e.id, e.eierid, e.navn, e.tekniskBeskrivelse, e.beskrivelse, e.opprettelseDato);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public int getEierId() {
        return eierId;
    }
    public String getNavn() {
        return navn;
    }

    public String getTekniskBeskrivelse() {
        return tekniskBeskrivelse;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public Date getOpprettelseDato() {
        return opprettelseDato;
    }

    @Override
    public String toString() {
        return "Eiendel [id=" + id + ", eierId=" + eierId + ", navn=" + navn + "]";
    }
}
