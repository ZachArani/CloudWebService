package edu.ou.oudb.cloudwebservice.presentationlayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.ou.oudb.cloudwebservice.businesslayer.BusinessManager;
import edu.ou.oudb.cloudwebservice.entities.Cost;

@Path("/estimation")
public class EstimationResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Cost selectQuery(@QueryParam("query") String sqlQuery)
	{	
		return BusinessManager.getInstance().processEstimation(sqlQuery);
	}

	
}
