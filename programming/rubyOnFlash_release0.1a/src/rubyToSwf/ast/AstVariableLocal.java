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
  * Represents a local variable, i.e. without @ or $ or \@@
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstVariableLocal implements IAstExpression{
	
	private String varName;
	private int line;
	private boolean nativeFlag;
	
	private boolean propertyFlag;// true if this variable is a property of an object
	
	public AstVariableLocal(String var, int line){
		this.varName = var;
		this.line = line;
		
		this.propertyFlag = false;
		this.nativeFlag = false;
	}
	
	public int getType(){
		return IAstNode.VARIABLE_LOCAL;
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
	
	public void setPropertyFlag(boolean flag){
		propertyFlag = flag;
	}
	
	public boolean getPropertyFlag(){
		return propertyFlag;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(propertyFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			actionTag.addAction(new GetMember());
		}else if(nativeFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			actionTag.addAction(new GetVariable());
		}else{
			Push pushEnv = new Push();
			Push.StackValue envName = new Push.StackValue();
			envName.setString("current Env");
			pushEnv.addValue(envName);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			actionTag.addAction(new GetMember());
		}
	}
	
	public Action getLhs(DoAction actionTag,SymbolTable st){
		if(varName.equals("true")||varName.equals("false")||varName.equals("nil")){
			throw new RuntimeException("Cannot assign to "+varName);
		}
		if(propertyFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			
			//if it's a property, then the return value will be ignored anyway..
			return new SetVariable();
		}else if(nativeFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			
			return new SetVariable();
		}else{
			/*
			Push pushEnv = new Push();
			Push.StackValue envName = new Push.StackValue();
			envName.setString("current Env");
			pushEnv.addValue(envName);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			*/
			CodeGenUtil.callFindObj(actionTag,varName);
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(varName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			
			return new SetMember();
		}
	}
	
	public void setNativeFlag(){
		this.nativeFlag = true;
	}
}