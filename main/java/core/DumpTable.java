package core;

import adt.Table;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Row;
import adt.Table;
import driver.Driver;
import adt.Database;
import adt.Response;


public class DumpTable implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
				"\\s*dump\\s*table\\s*([A-Z][a-z_0-9]*)",
				Pattern.CASE_INSENSITIVE
				);
	}
	
	public Response execute(Database db, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches())return null;
		
		String tableName = matcher.group(1);
		
		if(db.containsKey(tableName)) {
			return new Response(true, "Drop table: " + tableName, db.get(tableName));
			
		}else {
			return new Response(false, "Table is not found", null);
		}
		
	}

}
