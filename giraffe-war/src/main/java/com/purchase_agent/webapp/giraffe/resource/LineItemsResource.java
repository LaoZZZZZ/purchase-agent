package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.utils.Links;
import com.purchase_agent.webapp.giraffe.mediatype.LineItem;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 3/11/17.
 */
@Path("/line_item")
@Produces(MediaType.APPLICATION_JSON)
public class LineItemsResource {
    private static final Logger logger = Logger.getLogger(LineItemsResource.class.getName());

    private final Provider<DateTime> requestTime;
    private final SecurityContext securityContext;
    private final Links links;

    @Inject
    public LineItemsResource(@RequestTime final Provider<DateTime> requestTime,
                             @Context final SecurityContext securityContext,
                             final Links links) {
        this.requestTime = requestTime;
        this.securityContext = securityContext;
        this.links = links;
    }

    @Path("/{lineItemId}")
    public Class<LineItemResource> id() {return LineItemResource.class;}

    @Path("/search")
    @GET
    public Response searchItems() {
        return Response.ok().build();
    }


    @RolesAllowed(Roles.USER)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLineItem(final LineItem lineItem) {
        if (!this.securityContext.isUserInRole(Roles.USER)) {
            logger.warning("Unauthorized user to create line item!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (lineItem == null) {
            logger.warning("Invalid request line item body!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        com.purchase_agent.webapp.giraffe.objectify_entity.LineItem persisted = new com.purchase_agent.webapp.giraffe.objectify_entity.LineItem();
        persisted.setId(UUID.randomUUID().toString());
        persisted.setStatus(lineItem.getStatus());
        persisted.setCategory(lineItem.getCategory());
        if (lineItem.getOriginalPrice() == null) {
            logger.warning("No original price is provided!");
        }
        persisted.setOriginalPrice(lineItem.getOriginalPrice());
        if (lineItem.getPurchasePrice() == null) {
            logger.warning("No purchase price is provided");
        }
        persisted.setPurchasePrice(lineItem.getPurchasePrice());
        persisted.setBrand(lineItem.getBrand());
        persisted.setPurchaseTime(lineItem.getPurchaseTime());

        ofy().save().entity(persisted).now();
        return Response.created(links.forLineItemCreation(persisted.getId())).build();
    }
}
