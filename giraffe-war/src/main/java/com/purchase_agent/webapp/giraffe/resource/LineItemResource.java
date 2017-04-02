package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.LineItem;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by lukez on 3/11/17.
 */
@Path("lineItem/{id}")
public class LineItemResource {
    private static final Logger logger = Logger.getLogger(LineItemResource.class.getName());

    private final DateTime requstTime;
    @Inject
    public LineItemResource(@RequestTime final DateTime requestTime) {
        this.requstTime = requestTime;
    }

    @RolesAllowed({Roles.USER})
    @GET
    public Response getLineItem() {

        return Response.ok().build();
    }

}
