package no.mesan.fagark.reaktiv.logistikk.core;

import static no.mesan.fagark.reaktiv.logistikk.EmbeddedDb.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.function.Predicate;

import no.mesan.fagark.reaktiv.logistikk.core.actor.Mottak;
import no.mesan.fagark.reaktiv.logistikk.core.actor.message.BaseEierMelding.TilMottak;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
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

    public void mottak(final EierDto eier) {
        final ActorRef mottak = system.actorOf(Props.create(Mottak.class, eier.eiendelerDto.size()));
        mottak.tell(new TilMottak(Eier.create(eier)), mottak);
    }

    public Optional<Eier> finnEier(final Integer id) {
        final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap("eier");
        return Optional.of(treeMap.get(new Integer(id)));

    }

    public Optional<Eiendel> finnEiendel(final Integer eierId, final Integer eiendelsId) {
        final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap("eier");
        final Eier eier = treeMap.get(new Integer(eierId));
        final Predicate<Eiendel> eiendelTilhorer = (e -> e.getId() == eiendelsId);

        return eier.getEiendeler().stream().filter(eiendelTilhorer).findFirst();
    }

    public List<Eier> listEiere() {
        final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap("eier");

        return new ArrayList<Eier>(treeMap.values());
    }

    public List<Eiendel> listEiendeler() {
        final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap("eier");
        final List<Eiendel> alleEiendeler = new ArrayList<Eiendel>();
        treeMap.values().forEach(e -> alleEiendeler.addAll(e.getEiendeler()));

        return alleEiendeler;
    }

    public Optional<Eier> utlever(final int id) {
        final ConcurrentNavigableMap<Integer, Eier> treeMap = database.getTreeMap("eier");
        return Optional.of(treeMap.remove(new Integer(id)));
    }
}
