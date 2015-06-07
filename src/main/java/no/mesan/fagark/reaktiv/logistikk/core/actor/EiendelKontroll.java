package no.mesan.fagark.reaktiv.logistikk.core.actor;

import static no.mesan.fagark.reaktiv.logistikk.EmbeddedDb.database;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import no.mesan.fagark.reaktiv.logistikk.DatabaseCollectionName;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEiendelMelding.EiendelTilKontroll;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class EiendelKontroll extends UntypedActor {
    private static Logger logger = LoggerFactory.getLogger(EiendelKontroll.class);

    @Override
    public void onReceive(final Object message) {
        if (message instanceof EiendelTilKontroll) {
            final Eiendel eiendel = ((EiendelTilKontroll) message).getEiendel();
            logger.trace("Detaljkontroll av eiendel: " + eiendel);

            final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            final Validator validator = factory.getValidator();
            final Set<ConstraintViolation<Eiendel>> errors = validator.validate(eiendel);

            final ConcurrentNavigableMap<String, List<KontrollMelding>> treeMap = database
                    .getTreeMap(DatabaseCollectionName.KONTROLL.toString());

            final List<KontrollMelding> kontrollMeldinger = treeMap.get(eiendel.getEierId());

            if (!errors.isEmpty()) {

                final KontrollMelding kontrollMelding = new KontrollMelding(new Long(System.currentTimeMillis()).intValue(),
                        eiendel.getEierId(), eiendel);

                errors.forEach(e -> kontrollMelding.leggTilConstraintViolation(e));

                kontrollMeldinger.add(kontrollMelding);
                
                treeMap.put(eiendel.getEierId(), kontrollMeldinger);
                database.commit();
            }

            getSender().tell(new EiendelTilEkspedisjon(eiendel), getSelf());

        } else {
            unhandled(message);
        }
    }

}
