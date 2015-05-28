package no.mesan.fagark.reaktiv.logistikk.core.actor;

import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilKontroll;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilMottak;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Dette er foreldreklassen som styrer kontroll og ekspedering av en eier med
 * eiendeler. Den tar i mot en eier, utfører kontroll på alle eiendeler i
 * parallel og ekspederer eier når all kontroll er utført.
 * 
 * @author arne
 *
 */
@SuppressWarnings("deprecation")
public class Mottak extends UntypedActor {
    private static Logger logger = LoggerFactory.getLogger(Mottak.class);

    final ActorRef ekspedisjon;
    final ActorRef hovedKontroller;


    public Mottak(final Integer pAntallEiendeler) {
        ekspedisjon = getContext().system().actorOf(Props.create(Ekspedisjon.class));
        hovedKontroller = getContext().system().actorOf(Props.create(Kontroll.class, pAntallEiendeler));

    }

    @Override
    public void onReceive(final Object message) {
        if (message instanceof TilMottak) {
            final Eier eier = ((BaseEierMelding) message).getEier();
            logger.trace("Mottak: " + eier);
            opprettEiendelId(eier);

            hovedKontroller.tell(new TilKontroll(eier), getSelf());

        } else if (message instanceof TilEkspedisjon) {

            final Eier eier = ((TilEkspedisjon) message).getEier();
            logger.trace("Kontrollert: " + eier);

            ekspedisjon.tell(new TilEkspedisjon(eier), getSelf());
            getContext().stop(getSelf());

        } else {
            unhandled(message);
        }
    }

    private void opprettEiendelId(final Eier eier) {
        int eiendelId = 1;

        for (final Eiendel e : eier.getEiendeler()) {
            e.setId(eiendelId);
            eiendelId++;
        }
    }

}
