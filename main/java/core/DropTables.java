package core;
import java.util.ArrayList;
//Try using ArrayList for this part

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Row;
import adt.Table;
import driver.Driver;
import adt.Database;
import adt.Response;


public class DropTables implements Driver{
		private static final Pattern pattern;
		static {
			pattern = Pattern.compile(
					"\\s*drop\\s+table\\s+([A-Z][a-z_0-9]*)",
					Pattern.CASE_INSENSITIVE
					);
		}
		
		public Response execute(Database db, String query) {
			Matcher matcher = pattern.matcher(query.trim());
			if(!matcher.matches())
				return null;
			
			String table_name = matcher.group(1);
			int rows = 0;
			Table table = new Table();
			int a = 700;
			//FIGURE OUT HOW TO GET NUMBER OF ROW
			if(db.containsKey(table_name)) {
				table = db.get(table_name);
				db.remove(table_name);
				rows = table.size();
			}else {
				return new Response(false, table_name + " is not found in the database", null);
			}	
			return new Response(true, "Table Dropped: "+ table_name + ", number of rows: " +table.size(), table);
			
		}
}

