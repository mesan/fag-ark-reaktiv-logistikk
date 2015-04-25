package no.mesan.fagark.reaktiv.logistikk.core.actor.message;

import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

/**
 * Meldinger brukt mellom aktører.
 * 
 * @author arne
 *
 */
public class BaseEierMelding {
        private final Eier eier;

        public Eier getEier() {
            return eier;
        }

        public BaseEierMelding(final Eier e) {
            eier = e;
        }

    public static class TilMottak extends BaseEierMelding {

        public TilMottak(final Eier e) {
            super(e);
        }

    }

    public static class TilKontroll extends BaseEierMelding {

        public TilKontroll(final Eier e) {
            super(e);
        }
    }

    public static class TilEkspedisjon extends BaseEierMelding {

        public TilEkspedisjon(final Eier e) {
            super(e);
        }

    }

}
