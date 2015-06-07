package no.mesan.fagark.reaktiv.logistikk.core.actor;

import static no.mesan.fagark.reaktiv.logistikk.EmbeddedDb.database;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import no.mesan.fagark.reaktiv.logistikk.DatabaseCollectionName;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilKontroll;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilKontroll;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class Kontroll extends UntypedActor {
    final ActorRef kontrollRouter;
    ActorRef mottak;
    Integer antallKontrollert = 0;
    Integer antallEiendeler = 0;
    Eier underBehandling;

    @SuppressWarnings("deprecation")
    public Kontroll(final Integer pAntallEiendeler) {
        kontrollRouter = getContext().actorOf(
                Props.create(EiendelKontroll.class).withRouter(new RoundRobinRouter(pAntallEiendeler)));
    }

    private static Logger logger = LoggerFactory.getLogger(Kontroll.class);

    @Override
    public void onReceive(final Object message) {
        if (message instanceof TilKontroll) {
            final Eier eier = ((TilKontroll) message).getEier();
            logger.trace("Hovedkontroll av eiendeler: " + eier);

            if (underBehandling == null) {
                mottak = getSender();
                antallEiendeler = eier.getEiendeler().size();
                underBehandling = eier;
            }

            hovedKontroll(eier);

            for (final Eiendel eiendel : eier.getEiendeler()) {
                kontrollRouter.tell(new EiendelTilKontroll(eiendel), getSelf());
            }

        } else if (message instanceof EiendelTilEkspedisjon) {
            antallKontrollert++;
            final Eiendel eiendel = ((EiendelTilEkspedisjon) message).getEiendel();
            logger.trace("Eiendel er ferdig kontrollert: " + eiendel);

            if (antallKontrollert == antallEiendeler) {

                mottak.tell(new TilEkspedisjon(underBehandling), getSelf());
                underBehandling = null;
                getContext().stop(getSelf());
            }
        }

        else {

            unhandled(message);
        }
    }

    private void hovedKontroll(final Eier eier) {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<Eier>> errors = validator.validate(eier);

        final ConcurrentNavigableMap<String, List<KontrollMelding>> treeMap = database.getTreeMap(DatabaseCollectionName.KONTROLL
                .toString());

        final CopyOnWriteArrayList<KontrollMelding> kontrollMeldinger = new CopyOnWriteArrayList<>();
        treeMap.put(eier.getId(), kontrollMeldinger);

        if (!errors.isEmpty()) {

            final KontrollMelding kontrollMelding = new KontrollMelding(new Long(System.currentTimeMillis()).intValue(),
                    eier.getId(), eier);

            errors.forEach(e -> kontrollMelding.leggTilConstraintViolation(e));
            
            kontrollMeldinger.add(kontrollMelding);

            database.commit();
        }
    }
}
