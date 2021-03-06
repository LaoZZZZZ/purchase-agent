package com.purchase_agent.webapp.giraffe.resource;

import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.LineItems;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
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
    private final SecurityContextWrapper securityContextWrapper;
    private final Links links;
    private final LineItemDao lineItemDao;

    @Inject
    public LineItemsResource(@RequestTime final Provider<DateTime> requestTime,
                             @Context final SecurityContextWrapper securityContextWrapper,
                             final Links links,
                             final LineItemDao lineItemDao) {
        this.requestTime = requestTime;
        this.securityContextWrapper = securityContextWrapper;
        this.links = links;
        this.lineItemDao = lineItemDao;
    }

    @Path("/single/{lineItemId}")
    public Class<LineItemResource> id() {return LineItemResource.class;}

    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @Path("/search")
    @GET
    public Response searchItems(@QueryParam("category") final com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Category category,
                                @QueryParam("transaction_id") final String transactionId,
                                @QueryParam("brand") final String brand,
                                @QueryParam("status") final com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Status status,
                                @QueryParam("next") final String next,
                                @QueryParam("owner") final String owner,
                                @QueryParam("limit") final Integer limit) {
        if (!this.securityContextWrapper.isUserInRole(Roles.USER) && !this.securityContextWrapper.isUserInRole(Roles.ADMIN)) {
            logger.warning("Unauthorized user to create transaction!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        LineItemDao.Search search = this.lineItemDao.search();
        if (category != null) {
            search = search.category(category);
        }

        if (!Strings.isNullOrEmpty(transactionId)) {
            search = search.transactionId(transactionId);
        }

        if (status != null) {
            search = search.status(status);
        }

        if (!Strings.isNullOrEmpty(next)) {
            search = search.next(next);
        }

        if (limit != null) {
            search = search.limit(limit);
        }

        if (!Strings.isNullOrEmpty(brand)) {
            search = search.brand(brand);
        }

        if (!Strings.isNullOrEmpty(owner)) {
            search = search.owner(owner);
        }
        
        final LineItemDao.Search.Result result = search.execute();
        UserAuthModel userAuthModel = null;
        if (securityContextWrapper.isUserInRole(Roles.USER)) {
            userAuthModel = securityContextWrapper.getUserInfo();
        }
        List<LineItem> lineitemList = new ArrayList<>();
        for (final com.purchase_agent.webapp.giraffe.objectify_entity.LineItem lineItem: result.lineItems) {
            if (userAuthModel == null || lineItem.getOwner().equals(userAuthModel.getUsername())) {
                lineitemList.add(LineItemResource.toMediaType(lineItem));
            }
        }
        LineItems lineItems = new LineItems();
        lineItems.setLineItems(lineitemList);
        Response.ResponseBuilder responseBuilder = Response.ok(lineItems);
        if (!Strings.isNullOrEmpty(result.encodedCursor)) {
            responseBuilder = responseBuilder.location(links.forSearchLineItems(result.encodedCursor));
        }
        return responseBuilder.build();
    }


    @RolesAllowed(Roles.USER)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLineItem(final LineItem lineItem) {
        if (!this.securityContextWrapper.isUserInRole(Roles.USER)) {
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
