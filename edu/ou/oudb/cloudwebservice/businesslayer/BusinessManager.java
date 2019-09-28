package edu.ou.oudb.cloudwebservice.businesslayer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.ou.oudb.cloudwebservice.dataaccesslayer.DALManager;
import edu.ou.oudb.cloudwebservice.dataaccesslayer.DALProvider;
import edu.ou.oudb.cloudwebservice.entities.Cost;
import edu.ou.oudb.cloudwebservice.entities.Relation;
import edu.ou.oudb.cloudwebservice.entities.Result;
import edu.ou.oudb.cloudwebservice.entities.Tuple;

/**
* Class allowing to manage the access and modification of data
* as well as the call of our algorithm items
*
*/
public class BusinessManager {

	private static final int NB_ESTIMATION_ITER = 5;
	
	private final double COST_INSTANCE = 0.053; // cost per instance
	private final double NB_NS_PER_HOUR = 3600000000000L; // nb nano second in one hour
	
	private Map<String, Cost> previousEstimations;
	
	/**
	 * BusinessManager instance
	 */
	private static BusinessManager instance = null;
	
	/**
	 * DalManager instance
	 */
	private DALManager dalManager;
	
	/**
	 * Protected constructor
	 */
	protected BusinessManager() 
	{
		dalManager = DALManager.getInstance();
		dalManager.setProvider(DALProvider.HIVE);
		previousEstimations = loadEstimations();
	}
	
	/**
	 * getInstance for singleton
	 * @return instance of BusinessManager
	 */
	public static BusinessManager getInstance()
	{
		if (instance == null)
		{
			instance = new BusinessManager();
		}
		
		return instance;
	}

	public Result processQuery(String sqlQuery) {

		long startTime = System.nanoTime();
		Result result = null;
		
		try {
			List<Tuple> tuples = dalManager.processQuery(sqlQuery);
			long endTime = System.nanoTime();
			
			long duration = (endTime - startTime);
			
			double moneyCost = getMoneyCost(duration, 10);
			
			result = new Result(tuples, new Cost(duration,moneyCost));
		} catch (SQLException e) {
			//save estimations
			saveEstimations();
		}
		
		return result;
	}
	
	public Cost processEstimation(String sqlQuery)
	{	
		Cost estimation;
		
		System.out.println("Estimation requiered for query: " + sqlQuery);
		
		if (previousEstimations.containsKey(sqlQuery))
		{
			System.out.println("estimation already computed previously");
			estimation = previousEstimations.get(sqlQuery);
		}
		else
		{	
			System.out.println("estimations needs to be computed");
			long duration = 0;
			
			try {
				for (int i = 0; i < NB_ESTIMATION_ITER; i++) {
					long startTime = System.nanoTime();
					dalManager.processQuery(sqlQuery);
					long endTime = System.nanoTime();
					duration += (endTime - startTime);
				}
			} catch (SQLException e) {
				//save estimations
				saveEstimations();
			}
			
			double moneyCost = getMoneyCost(duration/NB_ESTIMATION_ITER, 10);
			
			estimation = new Cost(duration/NB_ESTIMATION_ITER, moneyCost);
			
			previousEstimations.put(sqlQuery, estimation);
		}
		
		return estimation;
	}

	public Collection<Relation> getAllRelations() {
		try {
			return dalManager.getAllRelations();
		} catch (SQLException e) {
			//save estimations
			e.printStackTrace();
			saveEstimations();
		}
		
		return new ArrayList<Relation>();
	}
	
	private double getMoneyCost(long duration, int nbInstances)
	{
		//insert cost model here
		return nbInstances * COST_INSTANCE * duration/NB_NS_PER_HOUR;
	}
	
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String,Cost> loadEstimations()
	{
		ConcurrentHashMap<String,Cost> estimationsMap = new ConcurrentHashMap<String,Cost>();
		
		try {
			ObjectInputStream s = new ObjectInputStream(new FileInputStream("estimations.save"));
			estimationsMap = (ConcurrentHashMap<String, Cost>) s.readObject();
			s.close();
		} catch (IOException e) {
			System.err.println("error while loading saved estimations");
		} catch (ClassNotFoundException e) {
			System.err.println("cast error while deserializing estimations");
		}
		
		return estimationsMap;
	}
	
	private void saveEstimations()
	{
		try {
			ObjectOutputStream oos = new ObjectOutputStream (new FileOutputStream("estimations.save"));
			oos.writeObject(previousEstimations);
			oos.close();
		} catch (IOException e) {
			System.err.println("error while saving estimations");
			e.printStackTrace();
		}
	}

}