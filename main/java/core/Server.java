package core;

import driver.*;
import adt.Response;
import adt.Database;

import java.util.List;
import java.util.LinkedList;

/** 
 * This class implements a server with an active
 * connection to a backing database.
 * 
 * Finish implementing the required features
 * but do not modify the protocols.
 */
public class Server {
	@SuppressWarnings("unused")
	private static final String
		STUDENT_NAME  = "Farhan Shahbaz",
		STUDENT_IDNUM = "800248293",
		STUDENT_EMAIL = "fs0022@mix.wvu.edu";
	
	private Database database;
	private List<Driver> drivers;
	
	public Server() {
		this(new Database());
	}
	
	public Server(Database database) {
		this.database = database;
		
		// TODO: Add each new driver as it is implemented.
		drivers = new LinkedList<Driver>();
		drivers.add(new DEcho());
		drivers.add(new DSquaresBelow());
		drivers.add(new DRange());
		drivers.add(new CreateTable());
		drivers.add(new DropTables());
		drivers.add(new ShowTables());
		drivers.add(new DumpTable());
		drivers.add(new InsertTable());
		drivers.add(new DSelectTable());
		//drivers.add(new GSQUARES_BELOW());
	}
	
	public Database database() {
		return database;
	}
	
	public List<Response> interpret(String script) {
		/*
		 * TODO: This wrongly assumes the entire script
		 * is a single query. However, there may be
		 * one or more semicolon-delimited queries in
		 * the script to be split and parsed distinctly.
		 */
		String[] queries = script.split("; ");
		
		
		//replace script with script.split(;);
		
		
		/* TODO: This only checks the first driver for a
		 * response to the first query. Instead, iterate over
		 * all drivers until one of them gives a response
		 * for the first query. Default to a failure response
		 * only if no driver gave a response for a query.
		 * Then iterate again for the next query. Don't
		 * forget to pass a reference to the actual database.
		 */
		//NOT FINISHED YET
		List<Response> responses = new LinkedList<Response>();
		for(int i =0; i < queries.length; i++) {
			for(int j = 0; j < drivers.size(); j++) {
			Response response = drivers.get(j).execute(database, queries[i]);
		//Loops cycle through all queries and all drivers
		if(response!=null) {
			responses.add(response);
		}
	}
		if(responses.isEmpty()) {
			responses.add(new Response(false, "Unrecognized Query", null));
		}
	}
		return responses;
	}
		
	
}


