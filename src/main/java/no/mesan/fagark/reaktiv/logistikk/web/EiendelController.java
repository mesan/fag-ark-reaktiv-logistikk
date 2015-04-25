package no.mesan.fagark.reaktiv.logistikk.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
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

import no.mesan.fagark.reaktiv.logistikk.core.EiendelsForvalter;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/eiendel")
public class EiendelController {
    private static Logger logger = LoggerFactory.getLogger(EiendelController.class);

    private final static EiendelsForvalter forvalter = new EiendelsForvalter();

    @Context
    private UriInfo uriInfo;

    private static final Abdera abdera = new Abdera();

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<EiendelDto> listEiendeler() {
        final List<Eiendel> eiendelsListe = forvalter.listEiendeler();

        if (eiendelsListe.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<EiendelDto> eiendelerDto = new ArrayList<EiendelDto>();
        eiendelsListe.forEach(e -> eiendelerDto.add(EiendelDto.create(e)));

        return eiendelerDto;
    }

    @GET
    @Path("/{eiendelid}/eier/{eierid}")
    @Produces(MediaType.APPLICATION_XML)
    public EiendelDto vis(@PathParam(value = "eiendelid") final int eiendelId, @PathParam(value = "eierid") final int eierId) {
        final Optional<Eiendel> funnet = forvalter.finnEiendel(eierId, eiendelId);

        if (funnet.isPresent()) {
            return EiendelDto.create(funnet.get());
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("/eier/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public void mottak(@PathParam(value = "id") final int id, final EierDto eier) {

        try {
            eier.id = id;
            forvalter.mottak(eier);
        } catch (final Exception e) {
            logger.error("Feilet ved registrering av eier med eiendeler.", e);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @POST
    @Path("/eier/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public EierDto uttak(@PathParam(value = "id") final int id) {
        final Optional<Eier> funnet = forvalter.utlever(id);

        if (funnet.isPresent()) {
            return EierDto.create(funnet.get());
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("/eier/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public EierDto vis(@PathParam(value = "id") final int id) {
        final Optional<Eier> funnet = forvalter.finnEier(id);

        if (funnet.isPresent()) {
            return EierDto.create(funnet.get());
        }

        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }


    @GET
    @Path("/eier")
    @Produces(MediaType.APPLICATION_XML)
    public List<EierDto> listeEiere() {
        final List<Eier> eiere = forvalter.listEiere();

        if (eiere.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final List<EierDto> eiereDto = new ArrayList<EierDto>();
        eiere.forEach(e -> eiereDto.add(EierDto.create(e)));

        return eiereDto;
    }



    @GET
    @Path("/eier/{id}/feed")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Feed feed(@PathParam(value = "id") final int id) {
        final Optional<Eier> funnetEier = forvalter.finnEier(id);

        if (!funnetEier.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        final Eier eier = funnetEier.get();

        final Feed feed = abdera.newFeed();
        feed.setId(uriInfo.getPath() + eier.getId());
        feed.setTitle("Eiendeler for " + eier.getFornavn() + " " + eier.getEtternavn());
        feed.setSubtitle("");
        feed.setUpdated(eier.getOpprettetDato());
        feed.addAuthor("Reaktiv - Logistikk ");

        for (final Eiendel eiendel : eier.getEiendeler()) {

            final Entry entry = feed.addEntry();
            entry.setId(uriInfo.getPath() + eiendel.getEierId() + "/eiendel/" + eiendel.getId());
            entry.setTitle("Eiedel: " + eiendel.getNavn());
            entry.setSummary(eiendel.getBeskrivelse());
            entry.setUpdated(eiendel.getOpprettelseDato());
            entry.setPublished(eiendel.getOpprettelseDato());
            entry.addLink(uriInfo.getRequestUri().toString());
        }

        return feed;
    }

}
