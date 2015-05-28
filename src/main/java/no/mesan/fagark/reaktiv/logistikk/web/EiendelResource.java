package no.mesan.fagark.reaktiv.logistikk.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import no.mesan.fagark.reaktiv.logistikk.core.EiendelsForvalter;
import no.mesan.fagark.reaktiv.logistikk.domain.Eiendel;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionCatchable;
import no.mesan.fagark.reaktiv.logistikk.exception.ExceptionLogger;
import no.mesan.fagark.reaktiv.logistikk.web.dto.EiendelDto;

public class EiendelResource {

    private final String eierId;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final static EiendelsForvalter forvalter = new EiendelsForvalter();

    public EiendelResource(final String eierId) {
        super();
        this.eierId = eierId;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public List<EiendelDto> visEiendeler() {
        final List<Eiendel> eiendelsListe = forvalter.listEiendeler(eierId);

        final List<EiendelDto> eiendelerDto = new ArrayList<EiendelDto>();
        eiendelsListe.forEach(e -> eiendelerDto.add(EiendelDto.create(e)));

        return eiendelerDto;
    }

    @GET
    @Path("/{eiendelid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public EiendelDto visEiendel(@PathParam(value = "eiendelid") final int eiendelId) {
        final Optional<Eiendel> funnet = forvalter.finnEiendel(eierId, eiendelId);

        if (!funnet.isPresent()) {
            logger.log(Level.INFO, "Fant ikke eiendel:" + eiendelId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return EiendelDto.create(funnet.get());
    }

    @PUT
    @Path("{eiendelid}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public Response motta(@PathParam(value = "eiendelid") final int eiendelId, final EiendelDto eiendel) {
        eiendel.id = eiendelId;
        eiendel.eierid = eierId;

        forvalter.motta(Eiendel.create(eiendel));
        logger.log(Level.INFO, "Lagret eiendel :" + eiendel);
        return Response.status(Status.CREATED).build();
    }

    @POST
    @Path("{eiendelid}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ExceptionCatchable(catcher = ExceptionLogger.class)
    public EiendelDto utlever(@PathParam(value = "eiendelid") final int eiendelId) {

        final Optional<Eiendel> utlevertEiendel = forvalter.utlever(eierId, eiendelId);
        if (!utlevertEiendel.isPresent()) {
            logger.log(Level.INFO, "Fant ikke eiendel:" + eiendelId);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return EiendelDto.create(utlevertEiendel.get());
    }
}
