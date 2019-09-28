package edu.ou.oudb.cloudwebservice.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Tuple {
	private List<String> attributeValues;
	
	public Tuple()
	{
		this.attributeValues = new ArrayList<String>();
	}
	
	public Tuple(List<String> attributeValues)
	{
		this.attributeValues = attributeValues;
	}

	public List<String> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<String> attributeValues) {
		this.attributeValues = attributeValues;
	}
}