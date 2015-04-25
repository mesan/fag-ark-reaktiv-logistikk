package no.mesan.fagark.reaktiv.logistikk.core.actor;

import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilKontroll;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class Kontroll extends UntypedActor {
    private static Logger logger = LoggerFactory.getLogger(Kontroll.class);

    @Override
    public void onReceive(final Object message) {
        if (message instanceof EiendelTilKontroll) {
            final Eiendel eiendel = ((EiendelTilKontroll) message).getEiendel();
            logger.trace("Kontrollerer: " + eiendel);

            getSender().tell(new EiendelTilEkspedisjon(eiendel), getSelf());
        } else {
            unhandled(message);

        }
    }

}
