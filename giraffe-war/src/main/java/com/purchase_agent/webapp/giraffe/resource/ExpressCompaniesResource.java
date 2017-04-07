package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;

import com.purchase_agent.webapp.giraffe.mediatype.ExpressCompany;
import com.purchase_agent.webapp.giraffe.utils.Links;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 4/6/17.
 */
@Path("/express_companies")
@Produces(MediaType.APPLICATION_JSON)
public class ExpressCompaniesResource {
    private static final Logger logger = Logger.getLogger(ExpressCompaniesResource.class.getName());

    private final DateTime requestTime;
    private final SecurityContext securityContext;
    private final Links links;

    @Inject
    public ExpressCompaniesResource(@RequestTime final DateTime requestTime,
                                    @Context final SecurityContext securityContext,
                                    final Links links) {
        this.requestTime = requestTime;
        this.securityContext = securityContext;
        this.links = links;
    }

    @Path("/{companyId")
    Class<ExpressCompanyResource> getCompanyResource() {return ExpressCompanyResource.class;}

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExpressCompany(final ExpressCompany expressCompany) {
        if (!this.securityContext.isUserInRole(Roles.USER) || !this.securityContext.isUserInRole(Roles.ADMIN)) {
            logger.warning("Unauthorized user to create line item!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (expressCompany == null) {
            logger.warning("Invalid request line item body!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        com.purchase_agent.webapp.giraffe.objectify_entity.ExpressCompany persisted =
                new com.purchase_agent.webapp.giraffe.objectify_entity.ExpressCompany();
        persisted.setId(UUID.randomUUID().toString());
        persisted.setCompanyAddress(expressCompany.getCompanyAddress());
        persisted.setCompanyName(expressCompany.getCompanyName());
        persisted.setHours(expressCompany.getHours());
        persisted.setContact(expressCompany.getContact());
        persisted.setTrackingWebSite(expressCompany.getTrackingWebSite());
        persisted.setCreationTime(requestTime);
        ofy().save().entity(persisted).now();

        return Response.created(links.forExpressCompany(persisted.getId())).build();
    }
}
