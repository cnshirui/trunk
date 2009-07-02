/*
 * Ruby On Flash is a compiler written in Java that compiles Ruby source code directly into Flash applications(.swf files), 
 * and aims to provide a programmer-friendly approach to casual Flash game development.   
 * 
 * Copyright (C) 2006-2007 Lem Hongjian (http://sourceforge.net/projects/rubyonflash)
 * 
 * This file is part of Ruby On Flash.
 *
 * Ruby On Flash is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ruby On Flash is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ruby On Flash.  If not, see <http://www.gnu.org/licenses/>.
 */

package rubyToSwf.codegen;

import rubyToSwf.common.*;
import java.util.Hashtable;
import java.util.Set;

/**
 * A symbol table, points to prev scope via instance var 'prev'
 * Implemented using a hashtable
 * @author Lem Hongjian
 *
 */
public class SymbolTable{
	private SymbolTable prev;
	private Hashtable ht;
	
	public SymbolTable(){
		prev = null;
		ht = new Hashtable();
	}
	
	/**
	 * Takes the outer scope's symbol table
	 * @param prev The symbol table for the previous scope
	 */
	public SymbolTable(SymbolTable prev){
		this.prev = prev;
	}
	
	public SymbolTable getPrev(){
		return this.prev;
	}
	
	public void setPrev(SymbolTable prev){
		this.prev = prev;
	}
	
	/**
	 * Private constructor used by the clone method
	 * @param ht The cloned hashtable
	 */
	private SymbolTable(Hashtable ht){
		this.ht = ht;
	}
	
	/**
	 * Checks whether a key entry exists.  Note that get method returns null if the value assigned to the key is null
	 * @param key The key to search for
	 * @return True if the key exists, false otherwise
	 */
	public boolean exists(String key){
		if(key==null){
			if(Global.verbose){
				System.out.println("Error: SymbolTable.exists: Null key used for search");
			}
			throw new RuntimeException("Error: Null key used for search");
		}
		
		return ht.keySet().contains(key);
	}
	
	/**
	 * Searches the local hashtable for an entry first, then searches up the prev chains
	 * @param key
	 * @return
	 */
	public Object get(String key){
		if(key==null){
			if(Global.verbose){
				System.out.println("Error: SymbolTable.get: Null key used for search");
			}
			throw new RuntimeException("Error: Null key used for search");
		}
		Object value = ht.get(key);
		if(value==null){
			if(prev==null){
				return null;
			}else{
				return prev.get(key);
			}
		}else{
			return value;
		}
	}
	
	public void set(String key, Object value){
		if(key==null){
			if(Global.verbose){
				System.out.println("Error: SymbolTable.set: Null key used");
			}
		}
		
		//ht would throw an exception
		ht.put(key, value);
	}
	
	public SymbolTable clone(){
		return new SymbolTable((Hashtable)this.ht.clone());
	}
}