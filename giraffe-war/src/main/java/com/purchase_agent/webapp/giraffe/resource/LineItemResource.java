package com.purchase_agent.webapp.giraffe.resource;

import com.googlecode.objectify.Work;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 3/11/17.
 */
@Path("/line_item/single/{lineItemId}")
@Produces(MediaType.APPLICATION_JSON)
public class LineItemResource {
    private static final Logger logger = Logger.getLogger(LineItemResource.class.getName());

    private final Provider<DateTime> requestTime;
    private final SecurityContextWrapper securityContextWrapper;
    private final String lineItemId;
    private final LineItemDao lineItemDao;
    @Inject
    public LineItemResource(@RequestTime final Provider<DateTime> requestTime,
                            @Context final SecurityContextWrapper securityContextWrapper,
                            @PathParam("lineItemId") final String lineItemId,
                            final LineItemDao lineItemDao) {
        this.requestTime = requestTime;
        this.securityContextWrapper = securityContextWrapper;
        this.lineItemId = lineItemId;
        this.lineItemDao = lineItemDao;
    }

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @GET
    public Response getLineItem() {
        if (!this.securityContextWrapper.isUserInRole(Roles.USER) && !this.securityContextWrapper.isUserInRole(Roles.ADMIN)) {
            logger.warning("User does not have access to line item " + lineItemId);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        LineItem persisted = ofy().load().type(LineItem.class).id(lineItemId).now();
        if (persisted == null) {
            logger.warning("could not find line item " + lineItemId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toMediaType(persisted)).build();
    }

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLineItem(final com.purchase_agent.webapp.giraffe.mediatype.LineItem lineItem) {
        if (lineItem == null) {
            logger.info("the request body is null!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return ofy().transactNew(3, new Work<Response>() {
            @Override
            public Response run() {
                final LineItem persisted = lineItemDao.get().id(lineItemId);
                if (persisted.getId().equals(lineItem.getId())) {
                    logger.info("Wrong line item");
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                persisted.setStatus(lineItem.getStatus());
                persisted.setPurchasePrice(lineItem.getPurchasePrice());
                persisted.setPurchaseTime(lineItem.getPurchaseTime());
                persisted.setCategory(lineItem.getCategory());
                persisted.setBrand(lineItem.getBrand());
                ofy().save().entity(persisted).now();
                return Response.noContent().build();
            }
        });
    }

    public static com.purchase_agent.webapp.giraffe.mediatype.LineItem toMediaType(final LineItem persisted) {
        com.purchase_agent.webapp.giraffe.mediatype.LineItem toReturn = new com.purchase_agent.webapp.giraffe.mediatype.LineItem();
        toReturn.setPurchaseTime(persisted.getPurchaseTime());
        toReturn.setStatus(persisted.getStatus());
        toReturn.setOriginalPrice(persisted.getOriginalPrice());
        toReturn.setPurchasePrice(persisted.getPurchasePrice());
        toReturn.setCategory(persisted.getCategory());
        toReturn.setId(persisted.getId());
        return toReturn;
    }
}
