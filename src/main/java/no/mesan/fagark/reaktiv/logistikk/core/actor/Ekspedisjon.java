package no.mesan.fagark.reaktiv.logistikk.core.actor;

import static no.mesan.fagark.reaktiv.logistikk.EmbeddedDb.database;

import java.util.concurrent.ConcurrentNavigableMap;

import no.mesan.fagark.reaktiv.logistikk.DatabaseCollectionName;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilEkspedisjon;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;


public class Ekspedisjon extends UntypedActor {
    private static Logger logger = LoggerFactory.getLogger(Ekspedisjon.class);

    @Override
    public void onReceive(final Object message) {
        if (message instanceof TilEkspedisjon) {
            final Eier eier = ((BaseEierMelding) message).getEier();
            logger.trace("Ekspederer eier med eiendeler: " + eier);

            try {
                final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());

                final Eier funnet = treeMap.get(eier.getId());

                if (funnet != null) {
                    funnet.oppdater(eier);
                } else {

                    treeMap.put(eier.getId(), eier);
                }

                database.commit();
            } catch (final Exception e) {
                database.rollback();
                throw e;
            }
        } else {
            unhandled(message);
        }
    }

}
