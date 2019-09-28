package edu.ou.oudb.cloudwebservice.presentationlayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.ou.oudb.cloudwebservice.businesslayer.BusinessManager;
import edu.ou.oudb.cloudwebservice.entities.Result;

@Path("/result")
public class ResultResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Result selectQuery(@QueryParam("query") String sqlQuery)
	{
		System.out.println("Query processed: " + sqlQuery);
		
		return BusinessManager.getInstance().processQuery(sqlQuery);
	}

	
}