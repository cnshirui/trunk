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
  * Represents a break statement
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstAlias implements IAstExpression{
	
	private int line;
	private String newName;
	private String oldName;
	
	public AstAlias(String newName, String oldName, int line){
		this.line = line;
		this.newName = newName;
		this.oldName = oldName;
	}
	
	public int getType(){
		return IAstNode.ALIAS;
	}
	
	public String toString(){
		return "alias "+newName+" "+oldName;
	}
	
	public int getLine(){
		return line;
	}
	
	public void setNewName(String newName){
		this.newName = newName;
	}
	
	public String getNewName(){
		return this.newName;
	}
	
	public void setOldName(String oldName){
		this.oldName = oldName;
	}
	
	public String getOldName(){
		return oldName;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
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
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNewName = new Push();
		Push.StackValue newNameVal = new Push.StackValue();
		newNameVal.setString(newName);
		pushNewName.addValue(newNameVal);
		actionTag.addAction(pushNewName);
		
		//get the function obj
		actionTag.addAction(pushEnv);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushContext);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushOldName = new Push();
		Push.StackValue oldNameVal = new Push.StackValue();
		oldNameVal.setString(oldName);
		pushOldName.addValue(oldNameVal);
		actionTag.addAction(pushOldName);
		actionTag.addAction(new GetMember());
		
		//check if method exists
		actionTag.addAction(new PushDuplicate());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		actionTag.addAction(pushNull);
		actionTag.addAction(new Equals2());
		
		//error msg if method not found
		Push pushErr = new Push();
		Push.StackValue errVal = new Push.StackValue();
		errVal.setString("Method does not exists");
		pushErr.addValue(errVal);
		
		Throw asThrow = new Throw();
		
		actionTag.addAction(new Not());
		actionTag.addAction(new If((short)(pushErr.getSize()+asThrow.getSize())));
		actionTag.addAction(pushErr);
		actionTag.addAction(asThrow);
		
		//assign to env["class Context"][newName]
		actionTag.addAction(new SetMember());
		
		//return NilClass instance
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