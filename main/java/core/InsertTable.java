package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Database;
import adt.Response;
import adt.Row;
import adt.Table;
import adt.Schema;
import driver.Driver;


//CHANGES!!!!!
public class InsertTable implements Driver  {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*insert\\s+into\\s*([A-Za-z_0-9]*)\\s*(\\((?:[A-Z][a-z_0-9]*)\\s*(?:(?:(?:\\s*[,]?\\s*)(?:[A-Z][a-z_0-9]*)\\s*)*)\\))?\\s*values\\s*(\\(?(?:(?:\\\"[A-Za-z_0-9]*!?.*\\\")|(?:[+]?[-]?[0-9])|(?:true|false)|(?:null))\\s*(?:(?:(?:\\s*[,]?\\s*)(?:(?:\\\"[A-Za-z_0-9]*!?.*\\\"\\s*)|(?:[+]?[-]?[0-9]\\s*)|(?:true|false)|(?:null))\\s*)*)\\)?)",
				Pattern.CASE_INSENSITIVE
				);
		}
	
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches()) return null;
		
		String tableName = matcher.group(1).trim();
		
		if(!db.containsKey(tableName)) {
			return new Response(false, "Table Not Found", null);
		}
		
		Table table2 = db.get(tableName);
		String columnNames = matcher.group(2);
		Schema previousSchema = new Schema(table2.getSchema());
		
		List<String> columnNList = new ArrayList<>();
		columnNList = (List<String>)previousSchema.get("column_names");
		for(int i =0; i < columnNList.size(); i++) {
			//System.out.println(columnNList.get(i));
		}
		
		String[] orderedNames = new String[columnNList.size()];
		String[] orderedValues = new String[columnNList.size()];
		//System.out.println(columnNames);
		
		String[] columnNameA;
		List<String> columnName = new ArrayList<String>();
		if(((columnNames != null))) {
			columnNames = columnNames.substring(columnNames.indexOf('(')+1, columnNames.indexOf(')')).trim();
			columnNameA = columnNames.split(",");
			columnName =  (List<String>)(Arrays.asList(columnNameA));
		}else {
			for(int i = 0; i < columnNList.size(); i++) {
			String a = columnNList.get(i);
			orderedNames[i] = a;
		}
	}

		String columnValues = matcher.group(3);
		
		//Check to see if there's Values
		//System.out.println(columnValues);
		if(columnValues == null) {
			return new Response(false, "There must be Values Inserted", null);
		//No Parenthesis Test Case
		}else if(!columnValues.contains("(")) {
			return new Response(false, "Parenthesis Error on Value Names", null);
		}else {
			columnValues = columnValues.substring(columnValues.indexOf('(')+1, columnValues.indexOf(')'));
		}
		//System.out.println(columnValues);
		//Checks to see if the table is in the database

		//Checks to see if there is a table Name entered
		if((tableName.isEmpty())){
				return new Response(false, "Table Name not given", null);
		}
		
		//Table Used in the End
		Table table = new Table();
		//New Row
		Row row = new Row();
		
		//Values as a Array then into a Linked LIst
		String[] columnValueA = columnValues.split(",");
		LinkedList<String> columnValue = new LinkedList<String>(Arrays.asList(columnValueA));
		//Assigning correct index
		for(int i =0; i < columnValue.size(); i++) {
			columnValue.set(i, columnValue.get(i).trim());
		}
		if(columnNames == null) {
			if(columnNList.size() != columnValue.size()){
				return new Response(false, "Mismatch Names", null);
			}
		}

		//Index value of Primary  
		int primary_index = (int) previousSchema.get("primary_index");
		
		//Column Types from Scehma is inserted into list
		List<String> columnTList = new ArrayList<>();
		columnTList = (List<String>)previousSchema.get("column_types");
		for(int i =0; i < columnTList.size(); i++) {
			//System.out.println(columnTList.get(i));
		}
		
		//String value of Primary Column NAME (Column Name)
		String primColumn = columnNList.get(primary_index);
		//System.out.println(primColumn);
		if(primColumn == null) {
			return new Response(false, "Invalid", null);
		}
		
		//Insertion of correct Column Name
		//Check for Primary as well
		boolean pI = false;
		//System.out.println(columnNames != null);
		if(columnNames != null) {
		for(int i = 0; i < columnName.size(); i++) {
			columnName.set(i, columnName.get(i).trim());
			if(columnName.contains(primColumn)) {
				pI=true;
			}
		}
		if(pI=false) {
			return new Response(false, "There has to be atleast one Primary", null);
		}
		
		//Check to see if column names don't repeat
		boolean flag = false;
		for(int i = 0; i < columnName.size(); i++) {
			for(int j = i+1; j < columnName.size();j++) {
				if(i!=j && columnName.get(j).trim().equals(columnName.get(i).trim())){
					flag = true;
				}
			}
			if(flag == true) {
				return new Response(false, "Column Names Can't be the same", null);
			}
		}
		//Checks to see if # of names is equal to # of values
		if(columnName.size() != columnValue.size() ) {
			return new Response(false, "Column names and Values Don't Match Up", null);
		}
		//System.out.println("b1");
		//Orders the Lists
		for(int i = 0; i < columnNList.size(); i++) {
			if(!columnName.contains(columnNList.get(i))) {
				orderedNames[i] = null;
				orderedValues[i] = "hello";
			}
			for(int j =0; j < columnName.size(); j++) {
				if(columnNList.get(i).equals(columnName.get(j))) {
					orderedNames[i] = columnName.get(j);
					orderedValues[i] = columnValueA[j];
					//System.out.println("b2");
				}
				if(!columnNList.contains(columnName.get(j))) {
					return new Response(false, "Invalid Name Isn't there", null);
				}
				if(orderedNames.length != orderedValues.length){
					return new Response(false, "Invalid Col Names/Valuess", null);
				}
			}
		}
	}else {
		for(int i = 0; i < columnValue.size(); i++) {
			String a = columnValue.get(i);
			orderedValues[i] = a;
		}
	}
		//System.out.println(orderedValues.length);
		for(int i=0; i < columnNList.size(); i++) {
			//System.out.println("breakpoint1");
			if(!Arrays.asList(orderedNames).contains(columnNList.get(i))){
				row.add(null);
			}else {
				//System.out.println(orderedValues[i]);
				if((orderedValues[i].toLowerCase().trim().matches("true|false|null")) && (columnTList.get(i).equals("boolean"))) {
					//System.out.println("boolean");
					if(orderedValues[i].toLowerCase().trim().equals("null")) {
						row.add(null);
					}else {
				boolean b = Boolean.parseBoolean(orderedValues[i].trim());
				row.add(b);
				}	
			}else if((orderedValues[i].trim().matches("[+]?[-]?[0-9]*|null")) && (columnTList.get(i).equals("integer"))) {
					//System.out.println("integer");
					
					String number = orderedValues[i].trim();
					if(number.trim().equals("null")) {
						row.add(null);
					}else {
					//System.out.println(number);
					int p = Integer.parseInt(number);
					if((number.charAt(0) == '0') && number.length() >= 2) {
						//System.out.println("NO LEADING ZEROS");
						return new Response(false, "No Leading Zeros", null);
					}
					row.add(p);
					}
				}else if(orderedValues[i].toLowerCase().matches("null")) {
					row.add(null);
				}else {
					//System.out.println("string");
					//String s = orderedValues[i].trim().substring(1, orderedValues[i].length()-1)
					if(!columnTList.get(i).equals("string")) {
						return new Response(false, "Invalid Type", null);
					}
					if(!orderedValues[i].contains("\"")) {
						return new Response(false, "Not a proper String literal", null);
					}
					String s = orderedValues[i].trim().replaceAll("\"", "");
					row.add(s);
					//System.out.println("bTT^^");
				}
			}
		}
		//System.out.println(row.get(primary_index));
		if(row.get(primary_index) == null){
			//System.out.println(row.get(primary_index));
			return new Response(false, "Primary Can't be Null", null);
		}
		//Inserts Primary
		List<Object> primColList = new ArrayList<Object>();
		for(Row row3 : table.values()) {
			primColList.add(row3.get(primary_index));
		}
		//Checks of there's more then one Primary
		//System.out.println(row.get(primary_index));
		if(table2.containsKey(row.get(primary_index))) {
			return new Response(false, "Can't be the Same Primary", null);
		}
		

		table2.put(row.get(primary_index), row);
		table.setSchema( previousSchema);
		table.getSchema().put("table_name", null);
		table.put(row.get(primary_index),row);
		
		
//		List<Object> valuesio = new ArrayList<Object>();
//		for(Row tim : table2.values()) {
//			valuesio.add(tim);
//			System.out.println(valuesio);
//		}
//		
//		String[] arr;
//		for(int i =0; i < valuesio.size(); i++) {
//			
			
		
//		for(int i = 0; i < valuesio.size(); i++) {
//			System.out.println(valuesio.get(i));
//		}
//		List<Object> arr = new ArrayList();
//		for(int i = 0; i < valuesio.size(); i++) {
//			Object n = valuesio.get(i);
//		for(int i =0; i < columnNList.size(); i++) {
//			String name = columnNList.get(i);
//			String type = columnTList.get(i);
//			System.out.println("Name: " + name + " Type: " + type);
//		}
	
		return new Response(true, "Inserted Table: " + tableName  , table);
	}
}


		
		
		

		
			
		
	

		
		
		
		
		
	
	


