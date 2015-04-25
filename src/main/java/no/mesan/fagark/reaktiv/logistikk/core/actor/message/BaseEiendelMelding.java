package no.mesan.fagark.reaktiv.logistikk.core.actor.message;

import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;

/**
 * Meldinger brukt mellom aktører.
 * 
 * @author arne
 *
 */
public class BaseEiendelMelding {
    final Eiendel eiendel;

    public BaseEiendelMelding(final Eiendel eiendel) {
        super();
        this.eiendel = eiendel;
    }

    public Eiendel getEiendel() {
        return eiendel;
    }

    public static class EiendelTilKontroll extends BaseEiendelMelding {

        public EiendelTilKontroll(final Eiendel eiendel) {
            super(eiendel);
        }
    }

    public static class EiendelTilEkspedisjon extends BaseEiendelMelding {

        public EiendelTilEkspedisjon(final Eiendel eiendel) {
            super(eiendel);
        }

    }
}
