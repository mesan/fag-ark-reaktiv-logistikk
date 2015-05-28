package no.mesan.fagark.reaktiv.logistikk.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

import no.mesan.fagark.reaktiv.logistikk.domain.constraint.MobilTelefon;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;

import org.hibernate.validator.constraints.NotBlank;

@MobilTelefon(message = "Det er ikke lov med mobiltelefon!!!!!!!!!!!!!!!!!!!!!!!")
public class Eiendel implements Serializable, Kontrollerbar {

    private static final long serialVersionUID = -3338988285265033207L;
    
    @Digits(integer = 0, fraction = 0, message = "Eiendel mangler id.")
    private int id;

    @NotBlank(message = "Eiendel mangler eierid.")
    private final String eierId;

    @Max(20)
    @NotBlank(message = "Eiendel mangler navn")
    private final String navn;

    @Max(50)
    @NotBlank(message = "Eiendel mangler teknisk beskrivelse.")
    private String tekniskBeskrivelse;

    @Max(100)
    private String beskrivelse;
    
    private final Date opprettelseDato;
    private Date sistOppdatert;

    public Eiendel(final int id, final String eierId, final String navn, final String tekniskBeskrivelse, final String beskrivelse) {
        super();
        this.id = id;
        this.eierId = eierId;
        this.navn = navn;
        this.tekniskBeskrivelse = tekniskBeskrivelse;
        this.beskrivelse = beskrivelse;
        opprettelseDato = new Date(System.currentTimeMillis());
        sistOppdatert = opprettelseDato;
    }

    public static Eiendel create(final EiendelDto e) {
        if (e instanceof EiendelDto) {
            return new Eiendel(e.id, e.eierid, e.navn, e.tekniskBeskrivelse, e.beskrivelse);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getEierId() {
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

    public void setTekniskBeskrivelse(final String tekniskBeskrivelse) {
        this.tekniskBeskrivelse = tekniskBeskrivelse;
    }

    public void setBeskrivelse(final String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public Date getSistOppdatert() {
        return sistOppdatert;
    }

    public void setSistOppdatert(final Date sistOppdatert) {
        this.sistOppdatert = sistOppdatert;
    }

    @Override
    public String toString() {
        return "Eiendel [id=" + id + ", eierId=" + eierId + ", navn=" + navn + "]";
    }
}
