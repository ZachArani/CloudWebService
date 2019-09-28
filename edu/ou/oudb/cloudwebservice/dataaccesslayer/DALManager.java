package edu.ou.oudb.cloudwebservice.dataaccesslayer;

import java.sql.SQLException;
import java.util.List;

import edu.ou.oudb.cloudwebservice.entities.Relation;
import edu.ou.oudb.cloudwebservice.entities.Tuple;

public class DALManager {

private static DALManager instance = null;
	
	private IDAL dal;
	
	public static DALManager getInstance() {
		if (instance == null)
		{
			instance = new DALManager();
		}
		
		return instance;
	}
	
	/**
	 * Protected constructor for singleton
	 */
	protected DALManager() {}
	
	/**
	 * Method allowing to choose which "database" we want to use
	 * @param provider the type of "database"
	 */
	public void setProvider(DALProvider provider)
	{
		switch (provider) {
		case STUB:
			dal = new StubDAL() ;
			break;
		case HIVE:
			dal = new HiveDAL();
			break;
		}
	}
	
	public IDAL getDal() {
		return dal;
	}

	public void setDal(IDAL dal) {
		this.dal = dal;
	}

	public List<Tuple> processQuery(String sqlQuery) throws SQLException {
		return dal.processQuery(sqlQuery);
	}

	public List<Relation> getAllRelations() throws SQLException {
		return dal.getAllRelations();
	}
	
}
