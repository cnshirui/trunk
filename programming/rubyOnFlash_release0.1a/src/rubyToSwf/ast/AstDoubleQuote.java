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
  * Represents a double quoted string, does not support expression substitution
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstDoubleQuote implements IAstLiteral{
	private String value;
	private int line;
	
	public AstDoubleQuote(String val, int line){
		this.value = val;
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.SINGLE_QUOTE;
	}
	
	public Object getValue(){
		return value;
	}
	
	public String toString(){
		return value;
	}
	
	public int getLine(){
		return line;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push.StackValue valueVal = new Push.StackValue();
		valueVal.setString(value);
		Push pushValue1Object = new Push();
		pushValue1Object.addValue(valueVal);
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		pushValue1Object.addValue(oneVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushValue1Object.addValue(objectVal);
		actionTag.addAction(pushValue1Object);
		actionTag.addAction(new GetVariable());
		Push pushString = new Push();
		Push.StackValue stringVal = new Push.StackValue();
		stringVal.setString("String");
		pushString.addValue(stringVal);
		actionTag.addAction(pushString);
		actionTag.addAction(new NewMethod());
	}
}