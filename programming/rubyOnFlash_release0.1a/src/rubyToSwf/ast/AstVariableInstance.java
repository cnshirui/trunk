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
import rubyToSwf.util.*;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import java.io.*;
import com.jswiff.swfrecords.*;

/**
  * Represents a class variable, \@@
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstVariableInstance implements IAstExpression{
	
	private String varName;
	private int line;
	
	public AstVariableInstance(String var, int line){
		this.varName = var;
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.VARIABLE_INSTANCE;
	}
	
	public String toString(){
		return varName;
	}
	
	public int getLine(){
		return line;
	}
	
	public void setName(String name){
		this.varName = name;
	}
	
	public String getName(){
		return varName;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push pushEnv = new Push();
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("current Env");
		pushEnv.addValue(envVal);
		actionTag.addAction(pushEnv);
		actionTag.addAction(new GetVariable());
		Push pushSelf = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelf.addValue(selfVal);
		actionTag.addAction(pushSelf);
		actionTag.addAction(new GetMember());
		Push pushName = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString(varName);
		pushName.addValue(nameVal);
		actionTag.addAction(pushName);
		actionTag.addAction(new GetMember());
	}
	
	public Action getLhs(DoAction actionTag,SymbolTable st){
		if(varName.equals("true")||varName.equals("false")||varName.equals("nil")){
			throw new RuntimeException("Cannot assign to "+varName);
		}
		Push pushEnv = new Push();
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("current Env");
		pushEnv.addValue(envVal);
		actionTag.addAction(pushEnv);
		actionTag.addAction(new GetVariable());
		Push pushSelf = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelf.addValue(selfVal);
		actionTag.addAction(pushSelf);
		actionTag.addAction(new GetMember());
		Push pushName = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString(varName);
		pushName.addValue(nameVal);
		actionTag.addAction(pushName);
		
		return new SetMember();
	}
}