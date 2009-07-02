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

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import java.io.*;
import com.jswiff.swfrecords.*;

/**
  * Represents a tempory Ast object, used primarily for keywords such as nil, true, end
  * These keywords are not being used in the final syntax tree.  The source line variable will however, be useful for debugging purposes
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstObject implements IAstExpression{
	
	private int line;
	private Object value;
	
	public AstObject(int line){
		this.line = line;
		this.value = null;
	}
	
	public AstObject(Object val,int line){
		this.line = line;
		this.value = val;
	}
	
	public int getType(){
		return IAstNode.NA;
	}
	
	public int getLine(){
		return line;
	}
	
	public Object getValue(){
		return value;
	}
	
	public String toString(){
		String temp = (value==null)?"null":value.toString();
		return ("Ast generic object,value: "+temp+" ,line: "+line);
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
	}
}