package edu.ou.oudb.cloudwebservice.presentationlayer;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.ou.oudb.cloudwebservice.businesslayer.BusinessManager;
import edu.ou.oudb.cloudwebservice.entities.Relation;

@Path("/dbinfo")
public class DBInfoResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/relations")
	public Collection<Relation> getAllRelations()
	{
		
		System.out.println("DBInfoRequiered");
		
		return BusinessManager.getInstance().getAllRelations();
	}
	
}
