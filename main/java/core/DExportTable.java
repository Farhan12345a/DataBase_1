package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.*;
import javax.xml.*;
import javax.xml.bind.*;

import adt.Database;
import adt.Response;
import adt.Row;
import adt.Table;
import java.util.LinkedList;
import java.util.Map;

public class DExportTable {

	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*export\\s*([A-Za-z_0-9]*)\\s*(to\\s*([A-Za-z_0-9]*)\\.(xml|json)|as\\s*(xml|json))", //Pattern
				Pattern.CASE_INSENSITIVE
				);
		}
	
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches()) return null;
		
		String tableName = matcher.group(1);
		
		//Checks to see if there's a Table Name
		if(tableName.isEmpty()) {
			return new Response(false, "Table Name is Needed", null);
		}
		
		//Checks to see if Table Name is in the Database
		if(!db.containsKey(tableName)) {
			return new Response(false, "Table Name is Not In Database", null);
		}
		
		String fileName, type;
		String export = matcher.group(2);
		
		if(export.contains("to") || export.contains("TO")){
			fileName = matcher.group(3);
			//Checks to See if file Name is in the Database
			if(db.containsKey(fileName)) {
				return new Response(false, "File Name Already Exists", null);
			}
			type = matcher.group(4);
			if(!type.equals("xml") || (!type.equals("json") || !type.equals("XML") || (!type.equals("JSON")))){
				return new Response(false, "Wrong Type", null);
			}
			
		}else if(export.contains("as") || export.contains("AS")) {
			//CHECK TO SEE IF THIS IS RIGHT
			fileName = tableName;
			type = matcher.group(5);
			if(!type.equals("xml") || (!type.equals("json") || !type.equals("XML") || (!type.equals("JSON")))){
				return new Response(false, "Wrong Type", null);
			}
			
		}else {
			return new Response(false, "Wrong Inputted Data For Export", null);
		}
		
		Table table = new Table(db.get(tableName));
		
		//JSON TYPE
		if(type.equals("json") || type.equals("JSON")) {
			Map<String, Integer> result = null;
			try {
				JsonWriter writer = Json.createWriter(new FileOutputStream("data/" + fileName + ".json"));
				JsonObjectBuilder jsonBuild = Json.createObjectBuilder();
				
				JsonReader reader = Json.createReader(new FileInputStream("data/" + fileName + ".json"));
				JsonObject obj = reader.readObject();
				reader.close();
				//Check this
				
				//result = new Hashmap();
				//
				for(String key: obj.keySet()) { 
					if(obj.equals("column_names") || obj.equals("column_types")) {
						JsonArray json_a = obj.getJsonArray(key);
						
//						for(String name : (LinkedList<String>) obj.keySet()) {
//							json_a.add(name);
//						}
						result.put(key, obj.getInt(key));
						//jsonBuild.add(entry.getKey(), arrayJ);
			
						String nullString = "null";
						//obj.add(entry.getKey(), nullString);
					}else {
						//String value = (String) entry.getValue();
						result.put(key, obj.getInt(key));
					}
					
					
					return new Response(true, "Export: " + tableName + " to " + fileName, table);
					
				}
					//CheCk Proper Exception
			}catch (FileNotFoundException e){
				return null;
			}
			//XML
		}else {
			try {
			JAXBContext jax = JAXBContext.newInstance(Table.class);
			Marshaller marsh = jax.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marsh.marshal(table, new File("data/" + fileName + ".xml"));
			
			return new Response(true, "Export: " + tableName + "to " + fileName, table);
			
			//CHECK CORRECT EXCEPTION
		}catch (JAXBException e) {
			return new Response(false, "Export FAILED", null);
		}
			
		}
		return new Response(false, "Hi", null);
	}
	
}
