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
  * The base interface that all Ast classes implements
  * @author Lem Hongjian
  * @version 1.0
*/
public interface IAstNode{

	public final int NA = -1;
	public final int INTEGER = 1;
	public final int FLOAT = 2;
	public final int SINGLE_QUOTE = 3;
	public final int VARIABLE_LOCAL = 4;
	public final int CONSTANT = 5;
	public final int UNARY_OP_CALL = 6;
	public final int BINARY_OP_CALL = 7;
	public final int TERNARY_OP_CALL = 8;
	public final int STATEMENTS = 9;
	public final int ARRAY = 10;
	public final int FUNCTION_CALL = 11;
	public final int CLOSURE = 12;
	public final int DEREFERENCE = 13;
	public final int NATIVE = 14;
	public final int METHOD_CALL = 15;
	public final int IF_EXPRESSION = 16;
	public final int WHILE_LOOP = 17;
	public final int BREAK = 18;
	public final int NEXT = 19;
	public final int HASH = 20;
	public final int METHOD_DEF = 21;
	public final int YIELD = 22;
	public final int CLASS_DEF = 23;
	public final int VARIABLE_CLASS = 24;
	public final int VARIABLE_INSTANCE = 25;
	public final int RETURN = 26;
	public final int VARIABLE_GLOBAL = 27;
	public final int ALIAS = 28;
	public final int UNDEF = 29;
	public final int REDO = 30;
	public final int SUPER = 31;
	public final int RANGE = 32;
	public final int DEFINED = 33;
	public final int CASE = 34;
	public final int MODULE_DEF = 35;
	public final int LOAD = 36;
	public final int RAISE = 37;
	/**
	  * Returns the type as an integer
	*/
	public int getType();
	
	/**
	* Returns the line number
	*/
	public int getLine();
	
	public void generateCode(DoAction actionTag,SymbolTable st);
}