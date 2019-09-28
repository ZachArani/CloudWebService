package edu.ou.oudb.cloudwebservice.dataaccesslayer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import edu.ou.oudb.cloudwebservice.entities.Relation;
import edu.ou.oudb.cloudwebservice.entities.Tuple;

public class HiveDAL implements IDAL {

	private final String DB_HIVE_LOGIN = "hduser";
	private final String DB_HIVE_PASSWORD = "";
	private final String META_MYSQL_LOGIN = "hive";
	private final String META_MYSQL_PASSWORD = "banane42";
	
	private final String DB_HIVE_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	private final String DB_HIVE_PORT = "10000";
	private final String DB_HIVE_HOST_NAME = "localhost";
	private final String DB_HIVE_URL = "jdbc:hive2://"+DB_HIVE_HOST_NAME+":"+DB_HIVE_PORT+"/";
	
	private final String META_MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
	//private final String META_MYSQL_PORT = "3306";
	private final String META_MYSQL_HOST_NAME = "localhost";
	private final String META_MYSQL_URL = "jdbc:mysql://" + META_MYSQL_HOST_NAME + "/hive?autoReconnect=true&user="
												+ META_MYSQL_LOGIN
												+"&password="
												+ META_MYSQL_PASSWORD;

	
	private final String COL_NAME = "COLUMN_NAME";
	private final String COL_MIN_LONG = "LONG_LOW_VALUE";
	private final String COL_MIN_DOUBLE = "DOUBLE_LOW_VALUE";
	private final String COL_MAX_LONG = "LONG_HIGH_VALUE";
	private final String COL_MAX_DOUBLE = "DOUBLE_HIGH_VALUE";
	private final String AVG_COL_LENGTH = "AVG_COL_LEN";
	private final String MAX_COL_LENGTH = "MAX_COL_LEN";
	private final String NUM_DISTINCT = "NUM_DISTINCTS";
	private final String TABLE_NAME = "TABLE_NAME";
	
	
	private Connection hiveConnection;
	private Connection mySqlConnection;
	
	public HiveDAL()
	{
		
		try {
			Class.forName(DB_HIVE_DRIVER_NAME);
			Class.forName(META_MYSQL_DRIVER_NAME);
			
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Driver not found");
			cnfe.printStackTrace();
		}
		
		try {
			hiveConnection = DriverManager.getConnection(DB_HIVE_URL,DB_HIVE_LOGIN,DB_HIVE_PASSWORD);
			mySqlConnection = DriverManager.getConnection(META_MYSQL_URL);
		} catch (SQLException sqle) {
			System.err.println("Connection failed!");
			sqle.printStackTrace();
		}
	}
	

	public Connection getHiveConnection() throws SQLException {
		
		if (hiveConnection.isClosed())
			hiveConnection = DriverManager.getConnection(DB_HIVE_URL,DB_HIVE_LOGIN,DB_HIVE_PASSWORD);
		
		return hiveConnection;
	}

	public void setHiveConnection(Connection connection) {
		this.hiveConnection = connection;
	}
	
	public Connection getMySqlConnection() throws SQLException
	{
		if (mySqlConnection.isClosed())
			mySqlConnection = DriverManager.getConnection(META_MYSQL_URL);
		
		return mySqlConnection;
	}
	
	public void setMySqlConnection(Connection connection)
	{
		this.mySqlConnection = connection;
	}
	
	@Override
	public List<Tuple> processQuery(String sqlQuery) throws SQLException {

		List<Tuple> tuples = new ArrayList<Tuple>();
		
		List<String> attributeValues = new ArrayList<String>();

		if (sqlQuery.contains("drop") || sqlQuery.contains("alter"))
		{
			throw new NotImplementedException("drop or alter are not implemented yet");
		}
		else
		{
			Statement stmt = null;
			ResultSet result = null;
			try{
				stmt = hiveConnection.createStatement();
				result = stmt.executeQuery(sqlQuery);
				ResultSetMetaData resultMeta = result.getMetaData();
				
				int nbColumn = resultMeta.getColumnCount();
				
				while(result.next())
				{
					for (int i = 1; i <= nbColumn; ++i)
					{
						attributeValues.add(result.getString(i));
					}
					
					tuples.add(new Tuple(attributeValues));
					
					attributeValues = new ArrayList<String>();
				}
				
			} catch (SQLException e) {
				throw e;
			}
			finally
			{
				if (stmt != null)
				{
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("RETURN TUPLES");
		
		return tuples;
	}


	@Override
	public List<Relation> getAllRelations() throws SQLException {
		
		// bean attributes to fill up
		List<Relation> relations = new ArrayList<Relation>();
		long nbTuples = 0;
		int maxTupleSize =0;
		double avgTupleSize = 0;
		List<String> attributeNames = new ArrayList<String>();
		List<String> attributeTypes = new ArrayList<String>();
		Map<String, Double> minValuesForAttr = new HashMap<String,Double>();
		Map<String, Double> maxValuesForAttr = new HashMap<String,Double>();
		Map<String, Long> nbDiffValuesForAttr = new HashMap<String,Long>();
				
		// metadata variables
		DatabaseMetaData metadata 	= null;
		Statement hiveStmt			= null;
		Statement mySqlStmt			= null;
		String catalog  			= null;
		String schema				= null;
		String databaseName         = null;
		String tableName			= null;
		String columnName		 	= null;
		String[] types				= null;
		
		// ResultSet variables
		ResultSet countTuplesRs;
		ResultSet attributeSetRs;
		ResultSet infoAttributeRs;
		ResultSet tableNamesRs;
	
		long minLong = 0;
		double minDouble = 0;
		double min;
		long maxLong = 0;
		double maxDouble = 0;
		double max;
		String query = null;
		
		metadata = getHiveConnection().getMetaData();
		hiveStmt = getHiveConnection().createStatement();
		mySqlStmt = getMySqlConnection().createStatement();
		// get table nmes
		tableNamesRs = metadata.getTables(catalog, schema, tableName, types);
		while(tableNamesRs.next())
		{
			//get the current table name
			tableName = tableNamesRs.getString(3);
			databaseName = tableNamesRs.getString(2);
		
			// count the lines (the metadata does not seem to work yet)
			query = "SELECT COUNT(*) FROM " + databaseName + "." + tableName;
			System.out.println(query);
			countTuplesRs = hiveStmt.executeQuery(query);
			if (countTuplesRs.next())
			{
				nbTuples = Long.parseLong(countTuplesRs.getString(1));
			}
			
			query = "DESCRIBE " + databaseName + "." +  tableName;
			attributeSetRs = hiveStmt.executeQuery(query);
			while(attributeSetRs.next())
			{
				columnName = attributeSetRs.getString(1);
				attributeNames.add(columnName);
				attributeTypes.add(attributeSetRs.getString(2));
			}
			
			// get the current column names, types, min, max, distinct values, avg column size, max columnSize;
			query = "select * from TAB_COL_STATS where " + TABLE_NAME + "= \"" + tableName + "\";";
			infoAttributeRs = mySqlStmt.executeQuery(query);
			
			System.out.println();
			
			while(infoAttributeRs.next())
			{
				// column name 
				columnName = infoAttributeRs.getString(COL_NAME);
				
				// column avg size
				avgTupleSize += infoAttributeRs.getDouble(AVG_COL_LENGTH);
				// column max size
				maxTupleSize += infoAttributeRs.getInt(MAX_COL_LENGTH);
				// min attribute value
				minLong = infoAttributeRs.getLong(COL_MIN_LONG);
				minDouble = infoAttributeRs.getDouble(COL_MIN_DOUBLE);
				if (minLong != 0)
				{
					min = minLong;
				}
				else if (minDouble != 0)
				{
					min = minDouble;
				}
				else
				{
					min = 0;
				}
				minValuesForAttr.put(columnName, min);
				
				// maximum value for attribute
				maxLong = infoAttributeRs.getLong(COL_MAX_LONG);
				maxDouble = infoAttributeRs.getDouble(COL_MAX_DOUBLE);
				if (maxLong != 0)
				{
					max = maxLong;
				}
				else if (maxDouble != 0)
				{
					max = maxDouble;
				}
				else
				{
					max = 0;
				}
				maxValuesForAttr.put(columnName, max);
				
				// nb distinct values for attribute
				nbDiffValuesForAttr.put(columnName, infoAttributeRs.getLong(NUM_DISTINCT));					
			}

			System.out.println("Table Name = " + tableName);
			System.out.println("Nb Tuples = " + nbTuples);
			System.out.println("max tuple size = " + maxTupleSize);
			System.out.println("avg tuple size = " + avgTupleSize);
			System.out.println("attribute Names: " + attributeNames);
			System.out.println("attribute Types: " + attributeTypes);
			System.out.println("minValForAttr: " + minValuesForAttr);
			System.out.println("maxValForAttr: " + maxValuesForAttr);
			System.out.println("nbDiffValForAttr: " + nbDiffValuesForAttr);

			
			relations.add(new Relation(tableName,nbTuples, avgTupleSize, maxTupleSize, attributeNames, attributeTypes, minValuesForAttr, maxValuesForAttr, nbDiffValuesForAttr));
			
			
			// reinit
			attributeNames = new ArrayList<String>();
			attributeTypes = new ArrayList<String>();
			minValuesForAttr = new HashMap<String,Double>();
			maxValuesForAttr = new HashMap<String,Double>();
			nbDiffValuesForAttr = new HashMap<String,Long>();
			avgTupleSize = 0;
			maxTupleSize = 0;
		}
			
		
		return relations;
	}

}
