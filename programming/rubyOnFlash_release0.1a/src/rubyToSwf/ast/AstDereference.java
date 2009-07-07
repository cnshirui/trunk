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
  * Represents a dereference operation, . or ::
  * Orignally, dereference operator was represented by an AstBinaryOpCall instance.  
  * However, as the code generation for a dot expression is significantly different, it might be better to spin off a separate class
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstDereference implements IAstExpression{

	//private instance fields
	private IAstExpression receiver;
	private IAstExpression operation;//can only be of type localVar or constant
	
	private int line;
	
	/**
	  * Constructor
	  * @param receiver The receiver
	  * @param op The operation
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstDereference(IAstExpression receiver, IAstExpression op, int line){		
		this.receiver = receiver;
		this.operation = op;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.DEREFERENCE;
	}
	
	/**
	  * @return The string representation of this node, in the format: receiver.toString() :: operation.toString()
	*/
	public String toString(){
		return receiver.toString() + "::" + operation.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getReceiver(){
		return receiver;
	}
	
	public IAstExpression getOperation(){
		return operation;
	}
	
	public void setReceiver(IAstExpression expr){
		this.receiver = expr;
	}
	
	public void setOperation(IAstExpression expr){
		this.operation = expr;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		receiver.generateCode(actionTag,st);
		operation.generateCode(actionTag,st);
	}
	
	public Action getLhs(DoAction actionTag,SymbolTable st){
		receiver.generateCode(actionTag,st);
		if(operation instanceof AstVariableLocal){
			((AstVariableLocal)operation).getLhs(actionTag,st);
		}else if(operation instanceof AstConstant){
			((AstConstant)operation).getLhs(actionTag,st);
		}
		
		return new SetMember();
	}	
}