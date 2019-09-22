package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Database;
import adt.Response;
import adt.Row;
import adt.Table;
import driver.Driver;


public class ShowTables implements Driver{
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*show\\s+tables\\s*",
				Pattern.CASE_INSENSITIVE
				);
		}
	
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
	
		if (!matcher.matches()) return null;
		
		if(db.isEmpty()) {
			return new Response(false, "No Tables in Database", null);
		}
		
		Table table2 = new Table();
		
		//Names (Strings)
		List<String> names = new ArrayList<>();
		names.add("table_name");
		names.add("row_count");
		table2.getSchema().put("column_names", names);
		table2.getSchema().put("table_name", null);
		
		
		//Types (Strings)
		List<String> types = new ArrayList<>();
		types.add("string");
		types.add("integer");
		table2.getSchema().put("column_types", types);
		
		table2.getSchema().put("primary_index", 0);
		
		for(String x : db.keySet()) {{
			Row row = new Row();
			row.add(x);
			row.add(db.get(x).size());
			table2.put(x, row);
			
		}
		
		}
		

		return new Response(true, "Tables: " +db.size(), table2);
	}
}
			
		
			
		
		
		
	
	


		
