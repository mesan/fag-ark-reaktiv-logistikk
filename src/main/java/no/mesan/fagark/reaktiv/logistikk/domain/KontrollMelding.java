package no.mesan.fagark.reaktiv.logistikk.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.ConstraintViolation;

public class KontrollMelding implements Serializable{

    private static final long serialVersionUID = 8092796836406091623L;
    public final int id;
    public final String eierId;
    final StringBuilder meldinger = new StringBuilder();
    public final Date opprettetDato;
    Kontrollerbar kilde;

    public KontrollMelding(final int id, final String eierId, final Kontrollerbar pKilde) {
        super();
        this.id = id;
        this.eierId = eierId;
        kilde = pKilde;
        opprettetDato = new Date(System.currentTimeMillis());
    }

    public void leggTilConstraintViolation(final ConstraintViolation<?> violation) {
        meldinger.append("" + violation.getPropertyPath() + ": " + violation.getMessage() + " ");

    }

    @Override
    public String toString(){

        return "Kilde: " + kilde.toString() + " " + meldinger.toString();
    }
}
