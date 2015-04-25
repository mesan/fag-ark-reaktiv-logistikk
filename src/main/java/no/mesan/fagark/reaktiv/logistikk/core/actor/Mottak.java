package no.mesan.fagark.reaktiv.logistikk.core.actor;

import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilKontroll;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilMottak;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

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

    final ActorRef kontrollRouter;
    final ActorRef ekspedisjon;

    Integer antallKontrollert = 0;
    Integer antallEiendeler = 0;
    Eier underBehandling;

    public Mottak(final Integer pAntallEiendeler) {
        ekspedisjon = getContext().system().actorOf(Props.create(Ekspedisjon.class));
        kontrollRouter = getContext().actorOf(Props.create(Kontroll.class).withRouter(new RoundRobinRouter(pAntallEiendeler)));
        antallEiendeler = pAntallEiendeler;
    }

    @Override
    public void onReceive(final Object message) {
        if (message instanceof TilMottak) {

            final Eier eier = ((BaseEierMelding) message).getEier();

            if (underBehandling == null) {
                underBehandling = eier;
            }

            logger.trace("Mottak: " + eier);

            for (final Eiendel e : eier.getEiendeler()) {
                kontrollRouter.tell(new EiendelTilKontroll(e), getSelf());
            }
        } else if (message instanceof EiendelTilEkspedisjon) {
            antallKontrollert++;

            if (antallKontrollert == antallEiendeler) {
                final Eiendel eiendel = ((EiendelTilEkspedisjon) message).getEiendel();
                logger.trace("Kontrollert: " + eiendel);

                ekspedisjon.tell(new TilEkspedisjon(underBehandling), getSelf());
                underBehandling = null;
                getContext().stop(getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
