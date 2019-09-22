package adt;

import adt.HashMap.LocalEntry;

public class TestHash extends HashMap {
	public static void main(String[] args) {
		HashMap a = new HashMap();
		a.put('a', 5);
		a.put("bet", 11);
		a.put("lebron", 12); 
//		System.out.println(a.entrySet());
//		System.out.println(a.values());
//		System.out.println(a.keySet());
		for(int i = 0; i <a.storedEntries.length;i++) {
		//System.out.println(a.storedEntries[i]);
		}
		System.out.println(a.entrySet().toString());
	
		if(a.containsValue(5)) {
			System.out.println("Correct");
		}
	}
	
}
