package core;

import adt.Response;
import adt.Row;

import adt.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.stream.*;


/** 
 * This class implements an interactive console
 * for a database server.
 * 
 * Finish implementing the required features
 * but do not modify the protocols.
 */
public class Console { 
	/**
	 * This is the entry point for running the
	 * console as a stand-alone program.
	 */
	public static void main(String[] args) {
		prompt(new Server(), System.in, System.out);
	}
	
	//Module 4 CHANGES!!!
	public static void prompt (Server server, InputStream in_stream, OutputStream out_stream) {
		final Scanner in = new Scanner(in_stream);
		final PrintStream out = new PrintStream(out_stream);
		
		/*
		 * TODO: Use a REPL to prompt the user for inputs,
		 * and send each input to the server for parsing.
		 * No inputs are to be parsed in the console, except
		 * for the case-insensitive sentinel EXIT, which
		 * terminates the console.
		*/
		
		
		while(true) {
			out.println("Enter an input");
			out.print(">> ");
			String text = in.nextLine();
			if(text.equalsIgnoreCase("exit")) {
				//terminate console
				break;
			}else {
				List<Response> responses = server.interpret(text);
				
				
				/*
				 * TODO: This wrongly assumes that there is only
				 * one response from the server. However, there 
				 * may be one or more responses, and each response
				 * should be reported in the order listed.
				 */
				//For loop to print out all the responses
				for(int i =0; i < responses.size(); i++) {
				out.println("Success: " + responses.get(i).get("success"));
				out.println("Message: " + responses.get(i).get("message"));
				out.println("Table:   " + responses.get(i).get("table"));
				
				}
				
				//JUST FIX TABLE NAME ROW!!!
				//Table Creation
				//CHANGES!!!
//			for(int k = 0; k < responses.size(); k++) {
//				
//				Table t = (Table) responses.get(k).get("table");
//				Object tableName = t.getSchema().get("table_name");
//				List<Object> columnNames = (List<Object>) t.getSchema().get("column_names");
//				int primary_index = (int) t.getSchema().get("primary_index");
//				Object primColumn = columnNames.get(primary_index);
//				List<Object> tableValues = new ArrayList<>();
//				
////				for(Row row1 : t.values()) {
////					tableValues.add(row1);
////				}
//				t.values().forEach(row1 -> tableValues.add(row1));
//				
//				
//				List<Object> dbTypes = (List<Object>)t.getSchema().get("column_types");
//			
//				Object col0 = columnNames.get(0);
//				Object col1 = columnNames.get(1);
//				Object col2 = columnNames.get(2);
//				
//				if(col0.equals(primColumn)) {
//					col0 += "*";
//				}else if(col1.equals(primColumn)) {
//					col1 += "*";
//				}else if(col2.equals(primColumn)) {
//					col2 += "*";
//				}
//				
//			int tableLength = 50;
//			
//			if(tableName != null) {
//				System.out.println("Table Name : " +tableName);
////				for(int i = 0; i < 50; i++) {
////					if(i == 0 || i == 49) {
////						System.out.print("+");
////					}else if(i != 45) {
////					System.out.print("-");
////				}
////			}
//				
//				
//				System.out.println();
//				System.out.print(String.format("|%47s|", tableName));
//			}else {
//				System.out.print(" ");
//			}
//				
//				System.out.println();
//				for(int i = 0; i < 49; i++) {
//					if(i == 0|| i==48) {
//						System.out.print("|");
//					}else {
//					System.out.print("=");
//					}
//				}
//			System.out.println();
//			
//			if(col0!=null) {
//				String name = (String)col0;
//				if(name.length() > 14) {
//					name = name.substring(0, 11);
//					col0 = name + "...";
//				}
//			}
//			if(col1!=null) {
//				String name = (String)col1;
//				if(name.length() > 14) {
//					name = name.substring(0, 11);
//					col1 = name + "...";
//				}
//			}
//			if(col2!=null) {
//				String name = (String)col2;
//				if(name.length() > 14) {
//					name = name.substring(0, 11);
//					col2 = name + "...";
//				}
//			}
//			
//			System.out.print(String.format("|%-15s|", col0)+String.format("%-15s|", col1)+String.format("%-15s|", col2));
//			System.out.println();
//			
//			for(int i = 0; i < 49; i++) {
//				if(i == 0|| i==48) {
//					System.out.print("|");
//				}else {
//				System.out.print("-");
//				}
//			}
//			System.out.println();
//			
//			for(int i =0; i<tableValues.size(); i++) {
//				Row r4 = (Row)tableValues.get(i);
//				Row r3 = new Row();
//				for(int j =0; j < r4.size(); j++) {
//					Object a = r4.get(j);
//					if(a == null) {
//						a = String.format("%-15s", " ");
//					}
//					if(dbTypes.indexOf("integer") == j) {
//						//Code to make integers on the right
//						a = String.format("%15s", a);
//					}else if(dbTypes.indexOf("string") == j) {
//						a = "\"" + a + "\"";
//						String b = (String)a;
//						if(b.length() > 14) {
//							b = b.substring(0,11);
//							a = b + "...";
//						}
//						a = String.format("%-15s", a);
//					}
//					if(dbTypes.indexOf("boolean") == j) {
//						a = String.format("%-15s", a);
//					}
//					r3.add(a);
//				}
//				System.out.println("|"+r3.get(0)+"|"+r3.get(1)+"|"+r3.get(2)+"|");
//			}
//			
//			for(int i = 0; i < 50; i++) {
//				if(i == 0 || i == 49) {
//					System.out.print("+");
//				}else if(i != 45) {
//				System.out.print("-");
//			}
//		}
//			
//			System.out.println();
//			}
			}
		}
		in.close();
		// TODO: Support tabular view, in a later module.
	}
}	




