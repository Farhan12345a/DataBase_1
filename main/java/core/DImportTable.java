package core;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Database;
import adt.HashMap;
import adt.Response;
import adt.Row;
import adt.Table;
import driver.Driver;
import adt.Schema;
import javax.json.*;
import javax.xml.bind.*;

public class DImportTable {


	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*import\\s*([A-Za-z_0-9]*)\\.(xml|json)\\s*(?:to\\s*([A-Za-z_0-9]*))?", //Pattern
				Pattern.CASE_INSENSITIVE
				);
		}
}
	
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches()) return null;
	
		String fileName = matcher.group(1);
		String type = matcher.group(2);
		if(!type.equals("xml") || !type.equals("json")) {
			return new Response(false, "Wrong Type Entered", null);
		}
		
		String tableName;
		Table table = new Table();
		//"TO" Clause is Given
		if(!matcher.group(3).isEmpty()) {
			tableName = matcher.group(3).substring(2);
			//MIGHT BE WRONG LOGIC
			
		}else {
			//SEE HOW TO GET FILE's SCHEMA PROPETIES
			//tableName = table.getSchema().get("table_name"));
			//tableName = table.getSchema().get("table_name");
		}
		
		//Applys proper suffixes
		if(db.containsKey(tableName)) {
			for(int i = 1; i < 10; i++) {
				tableName = tableName + "_" + i;
				if(!db.containsKey(tableName)) {
					break;
				}
			}
		}
		
	
		//Set <List<String>> set;
		
		//JSON
		if(type.equals("json")) {
			Map<String, Integer> result = null;
	
			try {
				//Check if file Name needs the json extension
				JsonReader reader =Json.createReader(new FileInputStream("data/"+fileName));
				JsonObject obj= reader.readObject();
				reader.close();
				
				Row row;
				int count = 0;
				//LOOK AT HOW TO IMPLEMENT TABLENAME
				
				result = new HashMap<>();
				
				for(String key: obj.keySet()) {
					if(count < 4) {
						if(obj.equals("column_names" )|| obj.equals("column_types")) {
						JsonArrayBuilder build = Json.createArrayBuilder();
//						for(JsonValue v : (JsonArray)key.getValue()) {
//							build.add(v);
//						}
						LinkedList<String> list = new LinkedList<String>();
						for(JsonValue a : build.build()) {
							//Might need to Substring
							list.add(a.toString());
						}
						
						table.getSchema().put(key, list);

				}else {
					table.getSchema().put(key, obj.get(key));
				}
			}else {
				//JsonObject object =; // Get the value
				//row = new Row();
				
				//for(String key2 : object.keySet()) {
					//Sys
					//Find a way to put into Row
					
				//}
				
			}
				//table.put(key2, new Row(row));
			}
				count++;
				db.put(tableName, table);
				
				return new Response(true, "Import " + fileName,table);
			}catch (FileNotFoundException e) {
				
				//Return Statement ????
				e.printStackTrace();
			}
		//XML FILES
		}else {
			try {
				Unmarshaller unmarshaller = JAXBContext.newInstance(Set.class).createUnmarshaller();
				File file = new File("data/" + fileName);
				
				Object obj = unmarshaller.unmarshal(file);
				table = new Table((Table) obj);
				
				return new Response(true, "Import " + fileName, table);
			
				
				//Table Name Specifications
				
				
			} catch (JAXBException e) {
				e.printStackTrace();
				//return new Response(false, "JAXB Exception", null);
			}
			
		}
		
	}
		
	}
	

	
	
	for(Entry<String, JsonValue> e: json_root_object.entrySet() ) {
		if(count<4) {
			if(e.getKey().equals("column_names")) {
				JsonArrayBuilder array = Json.createArrayBuilder();
				for(JsonValue value : (JsonArray)e.getValue()) {
					array.add(value);
				}
				LinkedList<String> list = new LinkedList<String>();
				for(JsonValue value2 : array.build()) {
					list.add(value2.toString());
				}
				t.getSchema().put(e.getKey(), list);
			}
		}
	}
