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
  * Represents a constant, i.e. names that start with an uppercase character
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstConstant implements IAstExpression{
	
	private String constName;
	private int line;
	private boolean propertyFlag;//true if this constant is a property of an object
	private boolean nativeFlag;
	
	public AstConstant(String constN,int line){
		this.constName = constN;
		this.line = line;
		
		this.propertyFlag=false;
		this.nativeFlag=false;
	}
	
	public int getType(){
		return IAstNode.CONSTANT;
	}
	
	public String toString(){
		return constName;
	}
	
	public void setName(String name){
		this.constName = name;
	}
	
	public String getName(){
		return this.constName;
	}
	
	public int getLine(){
		return line;
	}
	
	public void setPropertyFlag(boolean flag){
		this.propertyFlag = flag;
	}
	
	public boolean getPropertyFlag(){
		return propertyFlag;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){	
		if(propertyFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(constName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			actionTag.addAction(new GetMember());
		}else if(nativeFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(constName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			actionTag.addAction(new GetVariable());
		}else{
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			Push pushContext = new Push();
			Push.StackValue contextVal = new Push.StackValue();
			contextVal.setString("class Context");
			pushContext.addValue(contextVal);
			actionTag.addAction(pushContext);
			actionTag.addAction(new GetMember());
			Push pushName = new Push();
			Push.StackValue nameVal = new Push.StackValue();
			nameVal.setString(constName);
			pushName.addValue(nameVal);
			actionTag.addAction(pushName);
			actionTag.addAction(new GetMember());
		}
	}
	
	public Action getLhs(DoAction actionTag,SymbolTable st){
		if(propertyFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(constName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			
			return new SetVariable();
		}else if(nativeFlag){
			Push.StackValue strVal = new Push.StackValue();
			strVal.setString(constName);
			Push push = new Push();
			push.addValue(strVal);
			actionTag.addAction(push);
			
			return new SetVariable();
		}else{
			//constants belong to the scope of the class
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			Push pushContext = new Push();
			Push.StackValue contextVal = new Push.StackValue();
			contextVal.setString("class Context");
			pushContext.addValue(contextVal);
			actionTag.addAction(pushContext);
			actionTag.addAction(new GetMember());
			Push pushName = new Push();
			Push.StackValue nameVal = new Push.StackValue();
			nameVal.setString(constName);
			pushName.addValue(nameVal);
			actionTag.addAction(pushName);
			
			return new SetMember();
		}
	}
	
	public void setNativeFlag(){
		this.nativeFlag = true;
	}
}