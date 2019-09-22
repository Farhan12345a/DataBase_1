
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


public class DSelectTable implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*select\\s*((?:\\s*[*]\\s*)|(?:(?:(?:[A-Z][a-z_0-9]*)(?:\\s*(?:as)"
				+ "\\s*(?:[A-Z][a-z_0-9]*))?)(?:(?:(?:\\s*[,]\\s*)(?:(?:[A-Z][a-z_0-9]*)"
				+ "(?:\\s*(?:as)\\s*(?:[A-Z][a-z_0-9]*))?)\\s*)*)))\\s*from\\s*([A-Z][a-z_0-9]*)"
				+ "\\s*(?:(\\s*WHERE\\s*[A-Z][a-z_0-9]*)?\\s*(=|<>|<|>|<=|>=)?\\s*(\\\"[a-z]*\\\"|(?:\\d+)|null|(?:true|false))?)?",
				Pattern.CASE_INSENSITIVE
				);
	}
	
	//CCHHANNNNGEESSSS
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches()) return null;
		
		String columnName = matcher.group(1);
		String tableName = matcher.group(2);
		String sourceName = matcher.group(3);
		//sourceName currently INCLUDES "WHERE"
		String operator = matcher.group(4);
		String sourceType = matcher.group(5);
		
		//Checks to see if table is in the database
		if(!db.containsKey(tableName)) {
			return new Response(false, "Table not found in the Database", null);
		}
		
		//Assigning the table name as a SOURCE TABLE
		Table sourceTN = db.get(tableName);
		
		//Computed Table Used in this Driver
		Table computeTable = new Table();
		
		//Checks the validity of column Names
		if(columnName == null) {
			return new Response(false, "There has to be something for the column name", null);
		}
		
		//Schema from DataBase
		Schema previousSchema = new Schema(sourceTN.getSchema());
		
		//Index of Primary Value
		int primary_index = (int) sourceTN.getSchema().get("primary_index"); //previousSchema.get("primary_index");
		System.out.println("Index of Primary Value: " + primary_index);
		
		//List of column Names from database
		List<String> columnNamesFromDB = new ArrayList<>();
		columnNamesFromDB = (List<String>)previousSchema.get("column_names");
		System.out.println("Column Names From DataBase: " + columnNamesFromDB);
		
		//List of column Types from database
		List<String> columnTypesFromDB = new ArrayList<>();
		columnTypesFromDB = (List<String>)previousSchema.get("column_types");
		System.out.println("Column Types From Database: " + columnTypesFromDB);
		
		//List of Types for Given Column Names
		List<String> columnTypesList = new ArrayList<String>();
		List<String> columnNameList = new ArrayList<String>();
		
		//if * is given, column names default to column names in Schema
		if(columnName.trim().equals("*")) {
			for(int i = 0; i < columnNamesFromDB.size(); i++) {
				columnNameList.add(columnNamesFromDB.get(i).trim());
				computeTable.getSchema().put("column_types",columnTypesFromDB);
				}
			//System.out.println("Column Names (With *): " +columnNameList);
		}else {
		//column Names split and as a Array if normal column Names Are Given
		String[] columnNamesArray = columnName.trim().split(",");
		columnNameList =  (List<String>)(Arrays.asList(columnNamesArray));
		}
		
		System.out.println("Column Names From User Input: " + columnNameList);

		List<String> dupNameList = new ArrayList<String>();
		List<String> originalList = new ArrayList<String>();
		for(int i = 0; i < columnNameList.size(); i++) {
				if(columnNameList.get(i).contains("AS")||columnNameList.get(i).contains("as")) {
					String columnNameA = columnNameList.get(i).trim();
					if(columnNameA.contains("AS")) {
					int asIndex = columnNameA.indexOf("AS");
					String split = columnNameA.trim().substring(asIndex + 2, columnNameA.length());
					dupNameList.add(i, split);
					
					String split2 = columnNameA.trim().substring(0, asIndex);
					originalList.add(i, split2);
					}else {
						int asIndex = columnNameA.indexOf("as");
						String split = columnNameA.trim().substring(asIndex + 2, columnNameA.length());
						dupNameList.add(i, split);
						
						String split2 = columnNameA.trim().substring(0, asIndex);
						originalList.add(i, split2);
					}
				}else {
					dupNameList.add(i, columnNameList.get(i).trim());
					originalList.add(columnNameList.get(i).trim());
				}
		}
		System.out.println("Dup Name List: " + dupNameList);
		System.out.println("ORIGINAL LIST :" + originalList);
		
		//Make dupList into a Row
		Row newRow = new Row();
		for(int i = 0; i < dupNameList.size(); i++) {
			newRow.add(dupNameList.get(i).trim());
		}
	
		for(int i =0; i < originalList.size(); i++) {
			if(originalList.get(i).trim().equals(columnNamesFromDB.get(0))) {
				columnTypesList.add(i,columnTypesFromDB.get(0));
				
			}else if(originalList.get(i).trim().equals(columnNamesFromDB.get(1))) {
				columnTypesList.add(i,columnTypesFromDB.get(1));
				
			}else if(originalList.get(i).trim().equals(columnNamesFromDB.get(2))) {
				columnTypesList.add(i,columnTypesFromDB.get(2));	
				}
			}

			//System.out.println("Column Types List: " + columnTypesList);
			computeTable.getSchema().put("column_types", columnTypesList);
	
		List<Row> valuesFromDB = new ArrayList<Row>();
		for(Row rowWithValues: sourceTN.values()) {
			valuesFromDB.add(rowWithValues);
		
			Row finalRow = new Row();
			//Converts List of Names into Row
			Row nameRow = new Row();
			for(int i = 0; i < columnTypesList.size(); i++) {
				if(columnTypesList.get(i).equals(columnTypesFromDB.get(0))) {
				finalRow.add(rowWithValues.get(0));
				}else if (columnTypesList.get(i).equals(columnTypesFromDB.get(1))) {
				finalRow.add(rowWithValues.get(1));
				}else if(columnTypesList.get(i).equals(columnTypesFromDB.get(2))) {
				finalRow.add(rowWithValues.get(2));
				}
			nameRow.add(originalList.get(i).trim());
			
			//System.out.println("Final Row: " +finalRow);
			computeTable.put(rowWithValues.get(primary_index), finalRow);
			computeTable.getSchema().put("column_names", nameRow);
			
		}
	}
		//System.out.println("VALUES FROM DB: " +valuesFromDB);
		for(int i =0; i < columnTypesList.size(); i++) {
			if(columnTypesList.get(i).equals(columnTypesFromDB.get(0))) {
				primary_index = i;
				break;
			}else {
				continue;
			}
		}
		
		if(!columnTypesList.contains(columnTypesFromDB.get(0))){
			return new Response(false, "FLASE", null);
		}
		
		

		System.out.println("New Primary:  " + primary_index);
		//Gets String Value of Primary
		String primColumn = originalList.get(primary_index);
		System.out.println("Primary Value (String): " + primColumn);
		
	
		
		//Checks for repeating Column Names
		boolean flag3 = false;
		for(int i = 0; i < columnNameList.size(); i++) {
			for(int j = i+1; j < columnNameList.size();j++) {
				if(i!=j && columnNameList.get(j).trim().equals(columnNameList.get(i).trim())){
					flag3 = true;
				}
			}
			if(flag3 == true) {
				return new Response(false, "Column Names Can't be the same", null);
			}
		}
		
		System.out.println("NEW ROW!!!: " + newRow);
		computeTable.getSchema().put("column_names", newRow);
		
		
		//Gets all the Values At 0 index
		String ps = columnNamesFromDB.get(0);
		List<Object> primCol = new ArrayList<Object>();
		for(int i =0; i < valuesFromDB.size(); i++) {
			Object specificCol= valuesFromDB.get(i).get(columnNamesFromDB.indexOf(ps));
			primCol.add(specificCol);
		}
		System.out.println("Values of first column: " + primCol);
	
		
		//Get all values at index 1
		String numero = columnNamesFromDB.get(1);
		List<Object> index1List = new ArrayList<Object>();
		for(int i =0; i < valuesFromDB.size(); i++) {
			Object specificCol= valuesFromDB.get(i).get(columnNamesFromDB.indexOf(numero));
			index1List.add(specificCol);
		}
		System.out.println("Values of second column: " + index1List);
		//System.out.println("List: " + index1List);
		//Get all values at index 2
		String boli = columnNamesFromDB.get(2);
		List<Object> index2List = new ArrayList<Object>();
		for(int i =0; i < valuesFromDB.size(); i++) {
			Object specificCol= valuesFromDB.get(i).get(columnNamesFromDB.indexOf(boli));
			index2List.add(specificCol);
		}
		System.out.println("Values of third column: " + index2List);
		
		System.out.println("Source name: " + sourceName);
		
		//If where clause if given:
		//Selected rows(lhs) are the only rows in the source table for given condition == true
		if(sourceName != null) {
			if(!sourceName.contains("WHERE") ||!sourceName.contains("WHERE")) {
				return new Response(false, "The word Where, is needed", null);
			}
			
			sourceName = sourceName.trim().substring(5, sourceName.length());
			System.out.println("New source Name: " + sourceName);
			
			//Validates source Name
			boolean flag20 = false;
			for(int i = 0; i < columnNamesFromDB.size(); i++) {
			if(sourceName.trim().equals(columnNamesFromDB.get(i))) {
				flag20 = true;
			}
		}
		if(flag20 == false) {
			return new Response(false, "Invalied source name", null);
		}
		
		//Validates the operator
		if(!operator.trim().equals("=")||!operator.trim().equals("<>")||!operator.trim().equals("<")||!operator.trim().equals(">")||!operator.trim().equals("<=")||!operator.trim().equals(">=")) {
			return new Response(false, "Invalid operator", null);
		}
		
		//Validates sourceType
		
		
		
		
	}

		//HOW TO GET THE FIELD VALUES!!!!!!!!
		//If WHERE CLAUSE IS GIVEN:
		//Selected rows are defined to be only the rows in SOURCE TABLE for WHICH EVALUATION IS TRUE
		//LIKELEY PROBLEM: NEED TO GET TYPE OF SOURCENAME!!!
		//Ultimate flag is used at end for WHERE condition
		boolean ultimateFlag = false;
		Row row = new Row();
		int rowCount = 0;
		if(sourceName != null) {
		if(sourceName.contains("WHERE")) {
			//SUBSTRING OF WHERE
			sourceName = sourceName.trim().substring(5, sourceName.length());
			//System.out.println(sourceName);
			
			//Checks to see if the sourceName is Valid COlumn Name DB
			if(!columnNamesFromDB.contains(sourceName)) {
				return new Response(false, "Invalid Column Name, Not in Source Table", null);
			}
			//Might be wrong
			if(!valuesFromDB.contains(sourceType)) {
				return new Response(false, "Not a Valid Type in Source Table", null);
			}
			
			//VALIDATE SOURCE TYPE???
			
			//Operator Operations
			//'=' Test
			boolean condition = false;
			if(operator.trim().equals("=")) {
				//flag2 will check for same types
				boolean flag2 = false;
				for(int i =0; i < columnTypesFromDB.size(); i++) {
					//String Test
					int index = columnNamesFromDB.indexOf(sourceName);
					if(columnTypesFromDB.get(index).equals("string") && sourceType.contains("\"")) {
						condition = true;
						
					}
						
					if((columnTypesFromDB.get(i).equals("string")) && (sourceType.trim().matches("[a-z]*"))) {
						flag2 = true;
						for(int j = 0; j < primCol.size(); j++) {
							if(sourceType.equals(primCol.get(j))){
								ultimateFlag = true;
								//HAS TO BE ROW??
								row.add(valuesFromDB.get(i));
								rowCount++;
							}
						}
					//Integer Test
					}else if((columnTypesFromDB.get(i).equals("integer")) && (sourceType.trim().matches("(?:\\d+)"))) {
						flag2 = true;
						//CHECK FOR PARSING NESSARY
						for(int j = 0; j < index1List.size(); j++) {
							if(!sourceType.equals(index1List.get(j))) {
								ultimateFlag=true;
								row.add(valuesFromDB.get(i));
								rowCount++;
							}
						}
					//Boolean Test
					}else if((columnTypesFromDB.get(i).equals("boolean")) && (sourceType.trim().matches("(?:true|false)"))) {
						flag2 = true;
						for(int j = 0; j < index2List.size(); j++) {
						if(sourceType.equals(index2List.get(j))) {
							ultimateFlag = true;
							row.add(valuesFromDB.get(i));
							rowCount++;
						}
					}
				}
			}
			//CHECK IF THIS IS RIGHT
			if(flag2 == false) {
				return new Response(false, "Column Name and Value Aren't the Same Type", null);
			}
				//NULL CASE FROM SPEC. TABLE
				//Might have to be in for loop to add .get
				if(sourceType == null) {
					//MIGHT CHANGE
					if(columnNamesFromDB.equals(null)) {
						ultimateFlag = true;
						row.add(valuesFromDB);
						rowCount++;
					}else {
						ultimateFlag = false;
				}
			}
		}else if(operator.trim().equals("<>")) {
				boolean flag2 = false;
				for(int i =0; i < columnTypesFromDB.size(); i++) {
					//String Check
					if((columnTypesFromDB.get(i).equals("string") && (sourceType.trim().matches("[a-z]*")))) {
						flag2 = true;
						for(int j = 0; j < primCol.size(); j++) {
						if(!sourceType.equals(primCol.get(j))) {
							ultimateFlag = true;
							row.add(valuesFromDB.get(i));
							rowCount++;
						}
					}
					//Integer Check
					}else if((columnTypesFromDB.get(i).equals("integer")) && (sourceType.trim().matches("(?:\\d+)"))) {
						flag2 = true;
						for(int j = 0; j < index1List.size(); j++) {
							if(!sourceType.equals(index1List.get(j))) {
								ultimateFlag=true;
								row.add(valuesFromDB.get(i));
								rowCount++;
							}
						}
					//Boolean check
					}else if((columnTypesFromDB.get(i).equals("boolean")) && (sourceType.trim().matches("(?:true|false)"))) {
						flag2 = true;
						for(int j = 0; j < index2List.size(); j++) {
							if(!sourceType.equals(index2List.get(j))) {
								ultimateFlag = true;
								row.add(valuesFromDB.get(i));
								rowCount++;
							}
						}
					}
				}
				
			//CHECK PLACEMENT
				if(flag2 == false) {
					return new Response(false, "Column Name and Value Aren't the Same Type", null);
				}
				
			//NULL CASES MIGHT HAVE TO BE IN FORLOOP!!
				if((sourceType == null)||(sourceName == null)) {
					if((sourceType == null) && (sourceName == null)){
						ultimateFlag = false;
					}else if((sourceName == null) && (sourceType != null)) {
						ultimateFlag = true;
					}else {
						ultimateFlag = true;
					}
				}
			//Checks Less then / equal to operator
			}else if((operator.trim().equals("<")) || (operator.trim().equals("<="))) {
				boolean flag2 = false;
				for(int i =0; i < columnTypesFromDB.size(); i++) {
					//Check String!!! LOOK UP HOW TO
					if((columnTypesFromDB.get(i).equals("string")) && (sourceType.trim().matches("[a-z]*"))) {
						flag2 = true;
						
					//Check Integer
					}else if((columnTypesFromDB.get(i).equals("integer")) && (sourceType.trim().matches("(?:\\d+)"))) {
						flag2 = true;
						int number = Integer.parseInt(sourceType);
						int numberG = Integer.parseInt(columnNamesFromDB.get(i));
						if(numberG < number) {
								ultimateFlag = true;
								row.add(valuesFromDB.get(i));
								rowCount++;
							}else if(operator.trim().equals("<=")) {
								if(number == numberG) {
									ultimateFlag = true;
									row.add(valuesFromDB.get(i));
									rowCount++;
							}
						}
					}else if((columnTypesFromDB.get(i).equals("boolean")) && (sourceType.trim().matches("(?:true|false)"))) {
						return new Response(false, "Can't be a Boolean For Operator", null);
					}	
				}
				if(flag2 == false) {
					return new Response(false, "Column Name and Value Aren't the Same Type", null);
				}
			//NULL CASES
			if((sourceName == null) || (sourceType == null)) {
				ultimateFlag = false;
			}	
			//Checks More then / equal to operator
			}else if((operator.trim().equals(">")||operator.trim().equals(">="))) {
				boolean flag2 = false;
				for(int i =0; i < columnTypesFromDB.size(); i++) {
					if((columnTypesFromDB.get(i).equals("string")) && (sourceType.trim().matches("[a-z]*"))) {
						flag2 = true;

					}else if((columnTypesFromDB.get(i).equals("integer")) && (sourceType.trim().matches("(?:\\d+)"))) {
						flag2 = true;
						int number = Integer.parseInt(sourceType);
						int numberG = Integer.parseInt(columnNamesFromDB.get(i));
						if(numberG < number) {
								ultimateFlag = true;
								row.add(valuesFromDB.get(i));
								rowCount++;
							}else if(operator.trim().equals("<=")) {
								if(number == numberG) {
									ultimateFlag = true;
									row.add(valuesFromDB.get(i));
									rowCount++;
							}
						}
					}else if((columnTypesFromDB.get(i).equals("boolean")) && (sourceType.trim().matches("(?:true|false)"))) {
						return new Response(false, "Can't be a Boolean", null);
					}	
				}
				if(flag2 == false) {
					return new Response(false, "Column Name and Value Aren't the Same Type", null);
				}
				if((sourceName == null) || (sourceType == null)) {
					ultimateFlag = false;
				}
			}
		}
	}
		
		computeTable.getSchema().put("table_name", null);
		computeTable.getSchema().put("primary_index", primary_index);
		
		return new Response(true,"Table name: " + tableName + " Number of Rows: " + rowCount, computeTable);

	
	}
}
	

