package edu.ou.oudb.cloudwebservice.dataaccesslayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ou.oudb.cloudwebservice.entities.Relation;
import edu.ou.oudb.cloudwebservice.entities.Tuple;

public class StubDAL implements IDAL{

	@Override
	public List<Tuple> processQuery(String sqlQuery) {
		
		List<Tuple> tupleList = new ArrayList<Tuple>();
		
		tupleList.add(new Tuple(Arrays.asList(new String[]{"1","Paul Dupont","Darren Ben","Checkup…","9/11/2014","9:30am","56"})));
		tupleList.add(new Tuple(Arrays.asList(new String[]{"2","Jack Tristen","Bernard Clayton","Cardiac test…","9/12/2014","10:30am","68"})));
		tupleList.add(new Tuple(Arrays.asList(new String[]{"3","Manny Kingsley","John Smith","Anxiety disorder research…","9/12/2014","1:45pm","80"})));
		tupleList.add(new Tuple(Arrays.asList(new String[]{"4","Meredith Eliot","Bernard Clayton","","9/12/2014","1:35pm","65"})));
		tupleList.add(new Tuple(Arrays.asList(new String[]{"5","Hale Lawson","Cindy Parks","Checkup…","9/12/2014","2:15pm","73"})));
	
		return tupleList;
	}

	@Override
	public List<Relation> getAllRelations() {
		List<Relation> relations = new ArrayList<Relation>();
		
		int sumDataTypeSize = 0;	
		sumDataTypeSize += 22; //NUMBER SIZE
		sumDataTypeSize += 50; //PATIENT NAME SIZE
		sumDataTypeSize += 50; //DOCTOR NAME SIZE
		sumDataTypeSize += 50; //DESCRIPTION VARCHAR2 SIZE
		sumDataTypeSize += 7; // DATE SIZE
		sumDataTypeSize += 7; //DATE SIZE
		sumDataTypeSize += 22; //NUMBER SIZE
		
		
		List<String> attributeTypes = new ArrayList<String>();
		attributeTypes.add("NUMBER");
		attributeTypes.add("VARCHAR2");
		attributeTypes.add("VARCHAR2");
		attributeTypes.add("VARCHAR2");
		attributeTypes.add("DATE");
		attributeTypes.add("DATE");
		attributeTypes.add("NUMBER");
		
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("NoteID");
		attributeNames.add("PatientName");
		attributeNames.add("DoctorName");
		attributeNames.add("Description");
		attributeNames.add("Date");
		attributeNames.add("Time");
		attributeNames.add("HeartRate");
		
		Map<String,Double> minValuesMap = new HashMap<String,Double>();
		minValuesMap.put("NoteID",Double.valueOf(1));
		minValuesMap.put("PatientName", null);
		minValuesMap.put("DoctorName", null);
		minValuesMap.put("Description", null);
		minValuesMap.put("Date", null);
		minValuesMap.put("Time", null);
		minValuesMap.put("HeartRate", Double.valueOf(56));
	
		Map<String,Double> maxValuesMap = new HashMap<String,Double>();
		maxValuesMap.put("NoteID",Double.valueOf(5));
		maxValuesMap.put("PatientName", null);
		maxValuesMap.put("DoctorName", null);
		maxValuesMap.put("Description", null);
		maxValuesMap.put("Date", null);
		maxValuesMap.put("Time", null);
		maxValuesMap.put("HeartRate", Double.valueOf(80));
		
		Map<String,Long> nbDiffValuesMap = new HashMap<String,Long>();
		nbDiffValuesMap.put("NoteID",Long.valueOf(5));
		nbDiffValuesMap.put("PatientName", Long.valueOf(5));
		nbDiffValuesMap.put("DoctorName", Long.valueOf(5));
		nbDiffValuesMap.put("Description", Long.valueOf(4));
		nbDiffValuesMap.put("Date", Long.valueOf(2));
		nbDiffValuesMap.put("Time", Long.valueOf(5));
		nbDiffValuesMap.put("HeartRate", Long.valueOf(5));
		
		relations.add(new Relation("NOTE", 5, sumDataTypeSize, sumDataTypeSize, attributeNames, attributeTypes, minValuesMap, maxValuesMap,nbDiffValuesMap));
		
		return relations;
	}

}
