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

package rubyToSwf.ast;

import rubyToSwf.common.*;
import rubyToSwf.codegen.SymbolTable;
import java.util.Vector;
import java.util.Enumeration;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import java.io.*;
import com.jswiff.swfrecords.*;

/**
* Represents a list of statements
* @version 1.0
* @author Lem Hongjian
*/
public class AstStatements implements IAstNode{
	private int line;
	private Vector<IAstStatement> statements;
	
	public AstStatements(int line){
		this.line = line;
		statements = new Vector<IAstStatement>();
	}
	
	/**
	* Adds a statement to the list of statements
	* @param stmt The statement to add
	*/
	public void add(IAstStatement stmt){
		if(stmt==null){
			if(Global.verbose){
				System.out.println("Null statement encountered in AstStatements.add()");
			}
			throw new RuntimeException("Null Statement!!! line: "+stmt.getLine());
		}
		if(stmt instanceof AstLoad){
			statements.addAll(((AstLoad)stmt).getStatements().getStatements());
		}else{
			statements.add(stmt);
		}
	}
	
	/**
	* @return The vector containing the list of statements
	*/
	public Vector<IAstStatement> getStatements(){
		return statements;
	}
	
	public int getType(){
		return IAstNode.STATEMENTS;
	}
	
	public int getLine(){
		return this.line;
	}
	
	public String toString(){
		String result = "Statements:";
		for(Enumeration<IAstStatement> e= statements.elements();e.hasMoreElements();){
			result += "\n\t" + ((Object)e.nextElement()).toString();
		}
		return result;
	}
	
	/**
	 * Invariant: A single block of statements would leave 1 and only 1 value after all statements have been executed
	 * Each statement except for the last would not leave any value on the stack
	 * @param actionTag The Action tag to append to
	 */
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(statements != null && statements.size() >0){
			statements.elementAt(statements.size()-1).setLast();
			for(int i=0;i<statements.size();i++){
				statements.elementAt(i).generateCode(actionTag,st);
			}
			
		}else{
			//return NilClass instance  as the result of evaluation (all expressions leave a value on the stack at the end of evaluation)
			Push push0NilClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0NilClass.addValue(zeroVal);
			Push.StackValue nilVal = new Push.StackValue();
			nilVal.setString("NilClass");
			push0NilClass.addValue(nilVal);
			actionTag.addAction(push0NilClass);
			actionTag.addAction(new NewObject());
		}
	}
}