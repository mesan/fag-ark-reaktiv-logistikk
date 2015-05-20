package no.mesan.fagark.reaktiv.logistikk.core;

import static no.mesan.fagark.reaktiv.logistikk.EmbeddedDb.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.function.Predicate;

import no.mesan.fagark.reaktiv.logistikk.DatabaseCollectionName;
import no.mesan.fagark.reaktiv.logistikk.core.actor.Mottak;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilMottak;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Klassen håndterer mottak, visning og utlevering av eiendeler.
 * 
 * @author arne
 *
 */
public class EiendelsForvalter {

    final ActorSystem system;

    public EiendelsForvalter() {
        system = ActorSystem.create("EiendelForvalter");
    }

    public void mottak(final EierDto eier, final String callbackUrl) {
        final ActorRef mottak = system.actorOf(Props.create(Mottak.class, eier.eiendelerDto.size()));
        mottak.tell(new TilMottak(Eier.create(eier)), mottak);
    }

    public void motta(final Eiendel mottatEiendel) {
        try {
            final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
            final Eier eierTilOppdatering = treeMap.get(mottatEiendel.getEierId());
            eierTilOppdatering.oppdater(mottatEiendel);

            database.commit();
        } catch (final Exception e) {
            database.rollback();
            throw new RuntimeException(e);
        }
    }

    public Optional<Eier> utlever(final String eierId) {

        try {
            final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
            final Optional<Eier> utlevertEier = Optional.ofNullable(treeMap.remove(eierId));
            database.commit();

            return utlevertEier;
        } catch (final Exception e) {
            database.rollback();
            throw new RuntimeException(e);
        }
    }

    public Optional<Eier> finnEier(final String eierId) {
        final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
        return Optional.ofNullable(treeMap.get(eierId));
    }

    public List<Eier> listEiere() {
        final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
        return new ArrayList<Eier>(treeMap.values());
    }

    public Optional<Eiendel> finnEiendel(final String eierId, final Integer eiendelsId) {
        final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
        final Eier eier = treeMap.get(eierId);

        if (eier == null) {
            return Optional.empty();
        }

        final Predicate<Eiendel> eiendelTilhorer = (e -> e.getId() == eiendelsId);
        return eier.getEiendeler().stream().filter(eiendelTilhorer).findFirst();
    }

    public List<Eiendel> listEiendeler() {
        final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
        final List<Eiendel> alleEiendeler = new ArrayList<Eiendel>();
        treeMap.values().forEach(e -> alleEiendeler.addAll(e.getEiendeler()));

        return alleEiendeler;
    }

    public List<Eiendel> listEiendeler(final String eierId) {
        final ConcurrentNavigableMap<String, Eier> treeMap = database.getTreeMap(DatabaseCollectionName.EIER.toString());
        final Eier eier = treeMap.get(new String(eierId));

        if (eier == null) {
            return new ArrayList<Eiendel>();
        }

        return eier.getEiendeler();
    }

    public List<KontrollMelding> hentKontrollMeldinger(final String eierId) {
        final ConcurrentNavigableMap<String, List<KontrollMelding>> treeMap = database.getTreeMap(DatabaseCollectionName.KONTROLL
                .toString());

        return treeMap.get(eierId);
    }
}
