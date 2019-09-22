package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Database;
import adt.Response;
import adt.Table;
import adt.Row;
import adt.Table;
import driver.Driver;

public class CreateTable implements Driver{
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*CREATE\\s+TABLE\\s+([A-Z][a-z_0-9]*)\\s*\\(((?:\\s*PRIMARY\\s*)?\\s*"
				+ "(?:INTEGER|STRING|BOOLEAN)\\s*(?:[A-Z][a-z_0-9]*)\\s*(?:(?:\\s*[,]?\\s*"
				+ "(?:(?:\\s*PRIMARY\\s*)?(?:INTEGER|STRING|BOOLEAN)\\s*(?:[A-Z][a-z_0-9]*)\\s*)))*)\\)",
				Pattern.CASE_INSENSITIVE
				);
		}
	

public Response execute(Database db, String query) {
	Matcher matcher = pattern.matcher(query.trim());
	if(!matcher.matches()) return null;
	
	String tableName = matcher.group(1);
	
	if(db.containsKey(tableName)) {
		return new Response(false, "Table is already in the database", null);
	}
	
	if(tableName == null) {
		new Response(false, "No Table Name Given", null);
	}
	
	String[] columnDef = matcher.group(2).split(","); 
	String[] columnNames = new String[columnDef.length];	
	String[] columnTypes = new String[columnDef.length];
	
	boolean flag = false;
	int primary_index = 0;
	for(int i =0; i < columnDef.length; i++) {
		String[] splitCol = columnDef[i].trim().split(" ");		
		
		
		if(splitCol.length == 3) {
			if(flag == true) {
				return new Response(false, "There can't be more then one primary column",null);
			}else {
				flag = true;
				columnTypes[i] = splitCol[1].toLowerCase();
				columnNames[i] = splitCol[2];
				primary_index = i;
			}
		}else {
			columnTypes[i] = splitCol[0].toLowerCase();
			columnNames[i] = splitCol[1];
		}
	}
		if(flag==false) {
			return new Response(false, "There must be one primary column", null);
	}
		
	
	//Schema
	
	//Validates Table
	
	
	Table table = new Table();
	
	table.getSchema().put("table_name", tableName);
	
	table.getSchema().put("primary_index", primary_index);
	
	List<String> names = new ArrayList<>();
	for(int i =0; i < columnDef.length; i++) {
		names.add(columnNames[i]);
	}
	table.getSchema().put("column_names", names);					//NAMES MIGHT HAVE TO BE CHANGED IF DOESN'T WORK
	
	
	List<String> types = new ArrayList<>();
	for(int i = 0; i < columnDef.length; i++) {
		types.add(columnTypes[i]);
	}
	table.getSchema().put("column_types", types);
	
	db.put(tableName, table);
	return new Response(true, "Table: " + tableName , table);
	
	
}
	

	

}
