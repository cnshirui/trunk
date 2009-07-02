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
  * Represents an integer
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstInteger implements IAstLiteral{
	private Integer value;
	private int line;
	
	public AstInteger(int val,int line){
		this.value = new Integer(val);
		this.line = line;
	}
	
	public AstInteger(Integer val,int line){
		this.value = val;
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.INTEGER;
	}
	
	public Object getValue(){
		return value;
	}
	
	public String toString(){
		return value.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push.StackValue intVal = new Push.StackValue();
		intVal.setInteger(value.intValue());
		Push push = new Push();
		push.addValue(intVal);
		Push.StackValue numArgVal = new Push.StackValue();
		numArgVal.setInteger(1);
		push.addValue(numArgVal);
		Push.StackValue integerVal = new Push.StackValue();
		integerVal.setString("Integer");
		push.addValue(integerVal);
		actionTag.addAction(push);
		actionTag.addAction(new NewObject());
	}
}