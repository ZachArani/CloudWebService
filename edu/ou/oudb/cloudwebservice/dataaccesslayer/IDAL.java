package edu.ou.oudb.cloudwebservice.dataaccesslayer;

import java.sql.SQLException;
import java.util.List;

import edu.ou.oudb.cloudwebservice.entities.Relation;
import edu.ou.oudb.cloudwebservice.entities.Tuple;

public interface IDAL {

	public List<Tuple> processQuery(String sqlQuery) throws SQLException;

	public List<Relation> getAllRelations() throws SQLException;
}
