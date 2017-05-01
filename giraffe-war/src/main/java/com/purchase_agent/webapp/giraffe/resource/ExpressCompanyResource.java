package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.ExpressCompany;
import com.purchase_agent.webapp.giraffe.utils.Links;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 4/6/17.
 */
@Path("/express_companies/{companyId}")
public class ExpressCompanyResource {
    private static final Logger logger = Logger.getLogger(ExpressCompanyResource.class.getName());

    private final String companyId;
    private final DateTime requestTime;
    private final SecurityContextWrapper securityContextWrapper;
    private final Links links;

    @Inject
    public ExpressCompanyResource(@PathParam("companyId") final String companyId,
                                  @RequestTime final DateTime requestTime,
                                  @Context final SecurityContextWrapper securityContextWrapper,
                                  final Links links) {
        this.companyId = companyId;
        this.requestTime = requestTime;
        this.securityContextWrapper = securityContextWrapper;
        this.links = links;
    }

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @GET
    public Response getExpressCompany() {
        if (!this.securityContextWrapper.isUserInRole(Roles.USER) || !this.securityContextWrapper.isUserInRole(Roles.ADMIN)) {
            logger.warning("Unauthorized user to create line item!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        ExpressCompany persisted = ofy().load().type(ExpressCompany.class).id(companyId).now();
        if (persisted == null) {
            logger.warning("could not find line item " + companyId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toWireModel(persisted)).build();
    }

    private com.purchase_agent.webapp.giraffe.mediatype.ExpressCompany toWireModel(final ExpressCompany persisted) {
        com.purchase_agent.webapp.giraffe.mediatype.ExpressCompany toReturn = new
                com.purchase_agent.webapp.giraffe.mediatype.ExpressCompany();
        toReturn.setTrackingWebSite(persisted.getTrackingWebSite());
        toReturn.setContact(persisted.getContact());
        toReturn.setCompanyAddress(persisted.getCompanyAddress());
        toReturn.setCompanyName(persisted.getCompanyName());
        toReturn.setHours(persisted.getHours());
        toReturn.setRating(persisted.getRating());
        return toReturn;
    }
}
