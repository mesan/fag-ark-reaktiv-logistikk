package no.mesan.fagark.reaktiv.logistikk.web;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import no.mesan.fagark.reaktiv.logistikk.core.EiendelsForvalter;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionCatchable;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionLogger;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

public class KontrollMeldingResource {

    private final String eierId;

    @Context
    private UriInfo uriInfo;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final static EiendelsForvalter forvalter = new EiendelsForvalter();

    private static final Abdera abdera = new Abdera();

    public KontrollMeldingResource(final String eierId) {
        super();
        this.eierId = eierId;
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public Feed visKontrollMeldinger() {
        final Optional<Eier> funnetEier = forvalter.finnEier(eierId);

        if (!funnetEier.isPresent()) {
            logger.log(Level.INFO, "Fant ikke eier:{1}", eierId);
        }

        final Eier eier = funnetEier.get();

        final Feed feed = abdera.newFeed();
        feed.setId(uriInfo.getPath());

        feed.setTitle("Kontrollmeldinger for " + eier.getId());
        feed.setSubtitle("");
        feed.addAuthor("Reaktiv - Logistikk ");

        final List<KontrollMelding> hentKontrollMeldinger = forvalter.hentKontrollMeldinger(eierId);

        for (final KontrollMelding m : hentKontrollMeldinger) {
            final Entry entry = feed.addEntry();
            entry.setId(uriInfo.getPath() + "/" + eier.getId());
            entry.setTitle("Eierid: " + m.eierId);
            entry.setContent(m.toString());
            entry.setUpdated(m.opprettetDato);
            entry.setPublished(m.opprettetDato);
            entry.addLink(uriInfo.getRequestUri().toString());

        }

        return feed;

    }
}
