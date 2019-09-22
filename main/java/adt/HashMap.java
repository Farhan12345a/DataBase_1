package adt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//import java.util.Iterator;
/** 
 * This is currently just an alias for a built-in
 * implementation of a hash-based Map and is therefore
 * non-compliant with the Hash modules specification.
 * 
 * You must replace this code with your own hash-based
 * Map implementation when attempting the Hash modules.
 * However, you can continue to use this non-compliant
 * class for all non-Hash modules.
 */
public class HashMap<K,V>  implements Map<K,V> {			//extends java.util.HashMap<K,V>
	private static final long serialVersionUID = 1L;

//	
	//Default Constructor
//	public HashMap() {
//		super();
//	}
//	
//	public HashMap(Map<? extends K,? extends V> copy) {
//		super(copy);
//	}

	public class LocalEntry<K,V> implements Map.Entry<K, V>{
		private K key;
		private V value;
		private LocalEntry<K,V> nextEntry;
		
		//Constructors
		public LocalEntry() {
			
		}
		
		public LocalEntry(K key) {
			this.key = key;
			this.value = null;
			this.nextEntry = null;
		}
		
		public LocalEntry(K key, V value) {
			this.key = key;
			this.value = value;
			this.nextEntry = null;
		}
		
		public LocalEntry(K key, V value, LocalEntry<K,V> entry) {
			this.key = key;
			this.value = value;
			this.nextEntry = entry;
		}
		
		//Overwritten methods
		@Override 
		public K getKey() {
			return this.key;
		}
		
		@Override
		public V getValue() {
			return this.value;
		}
	
		//LOOK OVER !!!
		@Override
		public boolean equals(Object o) {
			LocalEntry<K,V> e2 = (LocalEntry<K,V>) o;
			return (this.getKey()==null ?
				      e2.getKey()==null : this.getKey().equals(e2.getKey()))  &&
				     (this.getValue()==null ?
				      e2.getValue()==null : this.getValue().equals(e2.getValue()));
		}
	
		@Override
		public int hashCode() {
			return
			 (this.getKey()==null   ? 0 : this.getKey().hashCode()) ^
		     (this.getValue()==null ? 0 : this.getValue().hashCode());
		}

		@Override
		public V setValue(V tempValue) {
			// TODO Auto-generated method stub
			V prevValue = value;
			value = tempValue;
			return prevValue;
		}
		
		//To-String Method
		public String toString() {
			return getClass().getName() + '@' + Integer.toHexString(hashCode());
		}
		
	}
	
	//Variables
	LocalEntry<K,V> [] storedEntries;
	int capacity = 100;
	int size =0;
	int loadFactor = this.size/capacity;
	// might have to change load factor to .75f ? for the HASH to work.
	// Might have to change varibles.
	
	//Default Constructor
	public HashMap() {
		storedEntries = (LocalEntry<K,V>[])new LocalEntry [capacity];
	}
	
	public HashMap(Map<? extends K,? extends V> map) {
		this();
		this.putAll(map);
	}
	
//	private boolean h2Rehash() {
//		return ((size/capacity) > loadFactor);
//	}
	
	private void h2Rehash2() {
		//You can use a iterator
		//Iterator<LocalEntry<K,V>> i = entrySet().iterator();
		
	}
	
	//HashFunction
	private int myHashCode(Object key) {
		//Checks for Integer
		int hashCode=0;
		if(key instanceof Integer) {
			return key.hashCode();
		}
		if(key instanceof String) {
				String tempKey = key.toString();
				int sum =0;
				for(int i =0; i<tempKey.length();i++) {
					int multiply = (int) (tempKey.charAt(i) * tempKey.charAt((tempKey.length()-1) -i))/4;
					sum += multiply;
				}
				//System.out.println("Sum: "+ sum);
				hashCode = sum % storedEntries.length;
				//System.out.println("Hashcode: " + hashCode);
				
			}
			return hashCode;
		}
		

	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(Object key) {
		//Checks if there  are elements in list
		if(isEmpty()==true) {
			return false;
		}
		
		
		LocalEntry<K,V> specificEntry = storedEntries[myHashCode(key)];
		
		if(specificEntry == null) {
			return false;
		}
		
		K testKey = specificEntry.getKey();
		
		//Checks the first key
		if(testKey.equals(key)) {
			return true;
		}
		
		//Goes through all the keys of the list
		LocalEntry<K,V> next = specificEntry.nextEntry;
		
		while(next!=null) {
			if(next!=null) {
				K testKeyNext = next.getKey();
				if(testKeyNext.equals(key)) {
					return true;
			}
				next = next.nextEntry;
		}
	}
		return false;
	}

	//Hash 3********
	@Override
	public boolean containsValue(Object value) {
		if(isEmpty() == true) {
			return false;
		}
		
		Collection c = this.values();
		for(int i = 0; i < c.size(); i++) {
			if(c.contains(value)) 
				return true;
			return false;
		}
		return false;
	}

	//Hash 3 ***
	//Create and returns a set of elements in the HashMap
	//See what TYPE of set is needed
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> set = new HashSet<Entry<K,V>>();
		for(LocalEntry<K,V> entry : storedEntries) {
			if(entry != null) {
				while(entry.nextEntry != null) {
					set.add(entry);
					entry = entry.nextEntry;
				}
				set.add(entry);
			}
			//set.add(entry);
		}
		return set;
	}
//	Set<LocalEntry<K,V>> set = new HashSet<LocalEntry<K,V>>();
//	for(LocalEntry<K,V> entry : storedEntries) {
//		set.add(entry);
//	}
//	return set;

	@Override
	public V get(Object key) {
		//Checks if there  are elements in list
		if(isEmpty() == true) {
			return null;
		}
		
		//Creates a map at index of key's hashCode
		LocalEntry<K,V> specificEntry = (LocalEntry<K,V>) storedEntries[myHashCode(key)];
		
		while(specificEntry!=null) {
			K testKey = specificEntry.getKey();
			if(testKey.equals(key)) {
				V value = (V)specificEntry.getValue();
				return value;
		}
		specificEntry = specificEntry.nextEntry;
	}
		return null;
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(size() == 0) {
			return true;
			}else {
				return false;
	}
}

	//Hash 3 ***
	//Creates and returns a set of the key elements
	@Override
	public Set<K> keySet() {
//		if(isEmpty() == true) {
//			return false;
//		}
		
		//Creates a default set for the keys
		Set<K> keys = new HashSet<K>();
		
		//Goes through all of the elements of the Entry Array
		for(int i = 0; i < storedEntries.length; i++) {
			//Checks to see if there's a item in the index
			LocalEntry<K,V> specific = (LocalEntry<K,V>) storedEntries[i];
			if(specific != null) {
				//Temporary Entry
				LocalEntry<K,V> entry = specific;
				//Checks to see if the next node is filled
				while(entry.nextEntry != null) {
					//adds key for specific node
					K tempKey = entry.getKey();
					keys.add(tempKey);
					//Moves on to next node
					entry = entry.nextEntry;
				}
				//Case if there's only one entry
				K tempKey = entry.getKey();
				keys.add(tempKey);
			}
		}
		return keys;
	}

	@Override
	public V put(K key, V value) {
		
		LocalEntry<K,V> entry;
		V temp;
		
		//Creates a map at index of key's hashCode
		LocalEntry<K,V> specificEntry = (LocalEntry<K,V>) storedEntries[myHashCode(key)];
	
		if(specificEntry!=null) {
			entry = specificEntry;
			K testKey = specificEntry.getKey();
			if(testKey.equals(key)) {
				temp = (V)entry.getValue();
				entry.setValue(value);
				return temp;
			}else {
				//WHILE!!!!
				LocalEntry<K,V> next = entry.nextEntry;
				
				while(entry.nextEntry!=null) {
					K nextKey = entry.nextEntry.getKey();
					if(nextKey.equals(key)) {
						temp= (V)entry.nextEntry.getValue();
						entry.nextEntry.setValue(value);
						return temp;
					}
					entry=entry.nextEntry;
				}
				
				entry.nextEntry = new LocalEntry(key,value);
				size++;
				entry = storedEntries[myHashCode(key)];
				return null;
			}
		}
		
		storedEntries[myHashCode(key)] = new LocalEntry(key,value);
		size++;
		return null;
	}

	//Hash 3 *****
	//Method copies all mappings and puts it into another map
	//*LOOK OVER*
	//Straight from ListMap.java
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e: m.entrySet()) {
			this.put(e.getKey(), e.getValue());
		}
	}
	

	@Override
	public V remove(Object key) {
		//Checks to see if the Map is empty
		if(isEmpty() == true) {
			return null;
		}
		//Get specific Hash value
		int index = myHashCode(key);
		//Value
		V returnValue = null;
		if(storedEntries[index]==null) {
			return null;
		}
		
		LocalEntry<K,V> specificEntry = storedEntries[myHashCode(key)];
		LocalEntry<K,V> nextEntry = specificEntry.nextEntry;
		K testKey = specificEntry.getKey();
		
		
		if(testKey.equals(key)) {
			returnValue = (V)specificEntry.getValue();
			storedEntries[index] =nextEntry;
			size--;
			return returnValue;
		}
		
		while(specificEntry.nextEntry!=null) {
			K nextKey = specificEntry.nextEntry.getKey();
			if(nextKey.equals(key)) {
				returnValue = (V)specificEntry.nextEntry.getValue();
				specificEntry.nextEntry =specificEntry.nextEntry.nextEntry;
				size--;
				return returnValue;
			}
			specificEntry = specificEntry.nextEntry;
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	//Start of Hash 3****
	//Returns collection of all the values in the map
	@Override
	public Collection<V> values() {
		Collection<V> valueCollection = new ArrayList<V>();
		for(int i = 0; i < storedEntries.length; i++) {
			if(storedEntries[i] != null) {
				LocalEntry<K,V> tempEntry = storedEntries[i];
				while(tempEntry.nextEntry != null) {
					V value = tempEntry.getValue();
					valueCollection.add(value);
					tempEntry = tempEntry.nextEntry;
				}
				V value = tempEntry.getValue();
				valueCollection.add(value);
			}
		}
		
		return valueCollection;

	}

}