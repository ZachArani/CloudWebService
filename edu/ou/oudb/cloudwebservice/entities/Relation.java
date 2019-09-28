package edu.ou.oudb.cloudwebservice.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "relations")
public class Relation {

	private String name;
	private long nbTuples;
	private double avgTupleSize;
	private int maxTupleSize;
	private List<String> attributeNames = null;
	private List<String> attributeTypes = null;
	
	private Map<String, Double> minForAttributes = null;
	
	private Map<String, Double> maxForAttributes = null;
	
	private Map<String, Long> nbDifferentValuesForAttributes = null;
	
	
	public Relation()
	{
		name = "";
		nbTuples = 0;
		setAvgTupleSize(0);
		setMaxTupleSize(0);
		attributeNames = new ArrayList<String>();
		attributeTypes = new ArrayList<String>();
		setMinForAttributes(new HashMap<String, Double>());
		setMaxForAttributes(new HashMap<String, Double>());
		setNbDifferentValuesForAttributes(new HashMap<String, Long>());
	}
	
	public Relation(String name, 
			long nbTuples, 
			double avgTupleSize,
			int maxTupleSize,
			List<String> attributeNames, 
			List<String> attributeTypes,
			Map<String, Double> minForAttributes, 
			Map<String, Double> maxForAttributes ,
			Map<String, Long> nbDifferentValuesForAttributes)
	{
		this.name = name;
		this.nbTuples = nbTuples;
		this.setAvgTupleSize(avgTupleSize);
		this.maxTupleSize = maxTupleSize;
		this.attributeNames = attributeNames;
		this.attributeTypes = attributeTypes;
		this.setMinForAttributes(minForAttributes);
		this.setMaxForAttributes(maxForAttributes);
		this.setNbDifferentValuesForAttributes(nbDifferentValuesForAttributes);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNbTuples() {
		return nbTuples;
	}

	public void setNbTuples(long nbTuples) {
		this.nbTuples = nbTuples;
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(List<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

	public List<String> getAttributeTypes() {
		return attributeTypes;
	}

	public void setAttributeTypes(List<String> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}

	public Map<String, Double> getMinForAttributes() {
		return minForAttributes;
	}

	public void setMinForAttributes(Map<String, Double> minForAttributes) {
		this.minForAttributes = minForAttributes;
	}

	public Map<String, Double> getMaxForAttributes() {
		return maxForAttributes;
	}

	public void setMaxForAttributes(Map<String, Double> maxForAttributes) {
		this.maxForAttributes = maxForAttributes;
	}

	public Map<String, Long> getNbDifferentValuesForAttributes() {
		return nbDifferentValuesForAttributes;
	}

	public void setNbDifferentValuesForAttributes(
			Map<String, Long> nbDifferentValuesForAttributes) {
		this.nbDifferentValuesForAttributes = nbDifferentValuesForAttributes;
	}

	public int getMaxTupleSize() {
		return maxTupleSize;
	}

	public void setMaxTupleSize(int maxTupleSize) {
		this.maxTupleSize = maxTupleSize;
	}

	public double getAvgTupleSize() {
		return avgTupleSize;
	}

	public void setAvgTupleSize(double avgTupleSize) {
		this.avgTupleSize = avgTupleSize;
	}
	
}
