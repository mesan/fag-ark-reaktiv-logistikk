package no.mesan.fagark.reaktiv.logistikk.web;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import no.mesan.fagark.reaktiv.logistikk.core.EiendelsForvalter;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.domain.KontrollMelding;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

@Path("/eier")
public class EierResource {

    @Context
    private UriInfo uriInfo;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final static EiendelsForvalter forvalter = new EiendelsForvalter();

    private static final Abdera abdera = new Abdera();

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<EierDto> visEiere() {

        final List<Eier> eiere = forvalter.listEiere();

        if (eiere.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<EierDto> eiereDto = new ArrayList<EierDto>();
        eiere.forEach(e -> eiereDto.add(EierDto.create(e)));

        return eiereDto;
    }

    @GET
    @Path("/{eierid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public EierDto visEier(@PathParam(value = "eierid") final String eierid) {
        final Optional<Eier> funnet = forvalter.finnEier(eierid);

        if (!funnet.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return EierDto.create(funnet.get());
    }

    @PUT
    @Path("/{eierid}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void mottak(@HeaderParam("callback_url") final String callbackUrl, @PathParam(value = "eierid") final String eierId,
            final EierDto eier) {

        try {
            eier.id = eierId;
            eier.eiendelerDto.forEach(e -> e.eierid = eier.id);
            forvalter.mottak(eier, callbackUrl);
        } catch (final Exception e) {
            logger.log(Level.WARNING, "Fei lunder lagring.", e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @POST
    @Path("/{eierid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public EierDto uttak(@PathParam(value = "eierid") final String eierId) {
        final Optional<Eier> funnet = forvalter.utlever(eierId);

        if (!funnet.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return EierDto.create(funnet.get());
    }

    /**
     * Dette redirekter alle /eiendel http-requester til EiendelResource.
     * 
     * @param eierId
     *            eiers id.
     * @return En eiendelsresurs for anngitt eier.
     */
    @Path("/{eierid}/eiendel")
    public EiendelResource eiendel(@PathParam(value = "eierid") final String eierId) {
        return new EiendelResource(eierId);
    }

    @GET
    @Path("/{eierid}/kontroll")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_ATOM_XML)
    public Feed kontroll(@PathParam(value = "eierid") final String eierId) {
        final Optional<Eier> funnetEier = forvalter.finnEier(eierId);

        if (!funnetEier.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(EierDto.class);
        } catch (final JAXBException e1) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
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

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Feed feed() {
        final List<Eier> eiere = forvalter.listEiere();

        if (eiere.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(EierDto.class);
        } catch (final JAXBException e1) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final Feed feed = abdera.newFeed();
        feed.setId(uriInfo.getPath());
        feed.setTitle("Registrerte eiere ");
        feed.setSubtitle("");
        feed.addAuthor("Reaktiv - Logistikk ");

        for (final Eier eier : eiere) {

            final Entry entry = feed.addEntry();
            entry.setId(uriInfo.getPath() + "/" + eier.getId());
            entry.setTitle("Eier");
            entry.setUpdated(eier.getOpprettetDato());
            entry.setPublished(eier.getOpprettetDato());
            entry.addLink(uriInfo.getRequestUri().toString());

            try {

                final StringWriter writer = new StringWriter();
                ctx.createMarshaller().marshal(EierDto.create(eier), writer);
                entry.setContent(writer.toString(), MediaType.APPLICATION_XML);
            } catch (final JAXBException e) {
                logger.log(Level.WARNING, "Feil ved oppretting av Atom Entry.", e);
            }
        }

        return feed;
    }
}
