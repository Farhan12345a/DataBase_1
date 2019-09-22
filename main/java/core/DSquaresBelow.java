package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.xml.ws.soap.AddressingFeature.Responses;
//Above package was shwing error
import adt.Database;
import adt.Response;
import adt.Row;
import adt.Table;
import driver.Driver;

public class DSquaresBelow implements Driver {
		private static final Pattern pattern;
		static {
			pattern = Pattern.compile(
					"\\s*show\\s*tables\\s",
				Pattern.CASE_INSENSITIVE
			);
		}



public Response execute(Database db, String query) {
	Matcher matcher = pattern.matcher(query.trim());
	if (!matcher.matches()) return null;
	
	String firstN = "x";
	

	int upper = Integer.parseInt(matcher.group(1));
	String name = matcher.group(2) != null ? matcher.group(2) : firstN;
	String name2 = matcher.group(3) != null? matcher.group(3) : name + "_squared";
	if(name.equals(name2)) {
		return new Response(false, "Names are equal", null);
	}
	
	
	Table table = new Table();
	
	table.getSchema().put("table_name", null);
	
	
	List<String> names = new ArrayList<>();
	names.add(name);
	names.add(name2);
	table.getSchema().put("column_names", names);
	
	List<String> types = new ArrayList<>();
	types.add("integer");
	types.add("integer");
	table.getSchema().put("column_types", types);
	
	table.getSchema().put("primary_index", 0);
	
	for (int i = 0; Math.pow(i,2) < upper; i++) {
		Row row = new Row();
		row.add(i);
		row.add((int)Math.pow(i,2));
		table.put(i, row);
	}
	
	return new Response(true, null, table);
}
}
