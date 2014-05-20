package com.netflix.genie.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.genie.common.exceptions.CloudServiceException;
import com.netflix.genie.common.messages.ApplicationConfigRequest;
import com.netflix.genie.common.messages.ApplicationConfigResponse;
import com.netflix.genie.common.model.ApplicationConfigElement;
import com.netflix.genie.server.services.ApplicationConfigService;
import com.netflix.genie.server.services.ConfigServiceFactory;
import com.netflix.genie.server.util.JAXBContextResolver;
import com.netflix.genie.server.util.ResponseUtil;

/**
 * Code for ApplicationConfigResource - REST end-point for supporting Application
 * @author amsharma
 * 
 */
@Path("/v1/config/application")
@Produces({ "application/xml", "application/json" })
public class ApplicationConfigResourceV1 {
    
    private ApplicationConfigService acs;
    private static Logger logger = LoggerFactory
            .getLogger(ApplicationConfigResourceV1.class);

    
    /**
     * Custom JAXB context resolver for the cluster config requests/responses.
     *
     * @author amsharma
     */
    @Provider
    public static class ApplicationJAXBContextResolver extends JAXBContextResolver {
        /**
         * Constructor - initialize the resolver for the types that
         * this resource cares about.
         *
         * @throws Exception if there is any error in initialization
         */
        public ApplicationJAXBContextResolver() throws Exception {
            super(new Class[]{ApplicationConfigElement.class,
                    ApplicationConfigRequest.class,
                    ApplicationConfigResponse.class});
        }
    }
    
    /**
     * Default constructor.
     *
     * @throws CloudServiceException
     *             if there is any error
     */
    public ApplicationConfigResourceV1() throws CloudServiceException {
        acs = ConfigServiceFactory.getApplicationConfigImpl();
    }
        
    /**
     * Get Application config for given id.
     *
     * @param id
     *            unique id for application config
     * @return successful response, or one with an HTTP error code
     */
    @GET
    @Path("/{id}")
    public Response getApplicationConfig(@PathParam("id") String id) {
        logger.info("called");
        return getApplicationConfig(id, null);
    }
    
    /**
     * Get Application config based on user params.
     *
     * @param id
     *            unique id for config (optional)
     * @param name
     *            name for config (optional)

     * @return successful response, or one with an HTTP error code
     */
    @GET
    @Path("/")
    public Response getApplicationConfig (@QueryParam("id") String id,
            @QueryParam("name") String name) {
        
        logger.info("called");
        ApplicationConfigResponse acr = acs.getApplicationConfig(id, name);
        return ResponseUtil.createResponse(acr);
    }
    
    /**
     * Create Application configuration.
     *
     * @param request
     *            contains a application config element 
     * @return successful response, or one with an HTTP error code
     */
    @POST
    @Path("/")
    @Consumes({ "application/xml", "application/json" })
    public Response createApplicationConfig(ApplicationConfigRequest request) {
        logger.info("called to create new application");
        ApplicationConfigResponse acr = acs.createApplicationConfig(request);
        return ResponseUtil.createResponse(acr);
    } 
    
    /**
     * Insert/update application config.
     *
     * @param id
     *            unique id for config to upsert
     * @param request
     *            contains the application config element for update
     * @return successful response, or one with an HTTP error code
     */
    @PUT
    @Path("/{id}")
    @Consumes({ "application/xml", "application/json" })
    public Response updateApplicationConfig(@PathParam("id") String id,
            ApplicationConfigRequest request) {
        logger.info("called to create/update application config");
        ApplicationConfigElement applicationConfig = request.getApplicationConfig();
        if (applicationConfig != null) {
            // include "id" in the request
            applicationConfig.setId(id);
        }

        ApplicationConfigResponse acr = acs.updateApplicationConfig(request);
        return ResponseUtil.createResponse(acr);
    }

    /**
     * Delete without an id, returns an error.
     *
     * @return error code, since no id is provided
     */
    @DELETE
    @Path("/")
    public Response deleteApplicationConfig() {
        logger.info("called");
        return deleteApplicationConfig(null);
    }

    /**
     * Delete a application config from database.
     *
     * @param id
     *            unique id for config to delete
     * @return successful response, or one with an HTTP error code
     */
    @DELETE
    @Path("/{id}")
    public Response deleteApplicationConfig(@PathParam("id") String id) {
        logger.info("called");
        ApplicationConfigResponse acr = acs.deleteApplicationConfig(id);
        return ResponseUtil.createResponse(acr);
    }
}
