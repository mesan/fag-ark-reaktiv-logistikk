package no.mesan.fagark.reaktiv.logistikk.web;

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

import no.mesan.fagark.reaktiv.logistikk.core.EiendelsForvalter;
import no.mesan.fagark.reaktiv.logistikk.domain.Eier;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionCatchable;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionCatcher;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionLogger;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EierDto;

@Path("/eier")
public class EierResource {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Context
    private UriInfo uriInfo;

    private final static EiendelsForvalter forvalter = new EiendelsForvalter();

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public List<EierDto> visEiere() {

        final List<Eier> eiere = forvalter.listEiere();

        final List<EierDto> eiereDto = new ArrayList<EierDto>();
        eiere.forEach(e -> eiereDto.add(EierDto.create(e)));

        return eiereDto;
    }

    @GET
    @Path("/{eierid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public EierDto visEier(@PathParam(value = "eierid") final String eierId) {
        final Optional<Eier> funnet = forvalter.finnEier(eierId);

        if (!funnet.isPresent()) {
            logger.log(Level.INFO, "Fant ikke eier:" + eierId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return EierDto.create(funnet.get());
    }

    @PUT
    @Path("/{eierid}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public Response motta(@HeaderParam("callback_url") final String callbackUrl,
            @PathParam(value = "eierid") final String eierId,
            final EierDto eierDto) {

        eierDto.id = eierId;
        eierDto.eiendelerDto.forEach(e -> e.eierid = eierDto.id);

        forvalter.motta(Eier.create(eierDto), callbackUrl);

        return Response.created(uriInfo.getBaseUri()).build();
    }

    @POST
    @Path("/{eierid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public EierDto uttak(@PathParam(value = "eierid") final String eierId) {
        final Optional<Eier> funnet = forvalter.utlever(eierId);

        if (!funnet.isPresent()) {
            logger.log(Level.INFO, "Fant ikke eier:" + eierId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return EierDto.create(funnet.get());
    }

    /**
     * Metoden redirekter alle /eiendel http-requester til EiendelResource.
     * 
     * @param eierId
     *            eiers id.
     * @return En eiendelsresurs for anngitt eier.
     */
    @Path("/{eierid}/eiendel")
    public EiendelResource eiendel(@PathParam(value = "eierid") final String eierId) {
        return ExceptionCatcher.newInstance(EiendelResource.class, eierId);
    }

    @GET
    @Path("/{eierid}/kontroll")
    public KontrollResource kontroll(@PathParam(value = "eierid") final String eierId) {
        return ExceptionCatcher.newInstance(KontrollResource.class, eierId);
    }
}
