package edu.ou.oudb.cloudwebservice.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {

	private List<Tuple> tuples;
	private Cost cost;
	
	public Result()
	{
		setTuples(new ArrayList<Tuple>());
		setCost(new Cost());
	}
	
	public Result(List<Tuple> tuples, Cost cost)
	{
		setTuples(tuples);
		setCost(cost);
	}

	public List<Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(List<Tuple> tuples) {
		this.tuples = tuples;
	}

	public Cost getCost() {
		return cost;
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}
	
}
