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
  * Represents a dereference operation for native actionscript objects, AS. or AS::
  * To call a native function or class or object, that native object must be dereferenced through the AS namespace, i.e. preceded the object with AS. or AS::
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstNative implements IAstExpression{

	//private instance fields
	private IAstExpression operation;//can only be of type function call or localVar or constant
	
	private int line;
	
	/**
	  * Constructor
	  * @param right The operation
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstNative(IAstExpression op, int line){		
		this.operation = op;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.NATIVE;
	}
	
	/**
	  * @return The string representation of this node, in the format: AS:: operation.toString()
	*/
	public String toString(){
		return "AS::" + operation.toString();
	}
	
	public int getLine(){
		return line;
	}

	public IAstExpression getOperation(){
		return operation;
	}
	
	public void setOperation(IAstExpression expr){
		this.operation = expr;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(operation instanceof AstVariableLocal){
			AstVariableLocal temp = (AstVariableLocal)operation;
			temp.setNativeFlag();
			temp.generateCode(actionTag,st);
		}else if(operation instanceof AstConstant){
			AstConstant temp = (AstConstant)operation;
			temp.setNativeFlag();
			temp.generateCode(actionTag,st);
		}else if(operation instanceof AstFunctionCall){
			AstFunctionCall temp = (AstFunctionCall)operation;
			temp.setNativeFlag();
			temp.generateCode(actionTag,st);
		}else{
			if(Global.verbose){
				System.out.println("AstNative.generateCode: Invalid native call type.  Line: "+operation.getLine());
			}
			throw new RuntimeException("Error: Invalid native call type.  Line: "+operation.getLine());
		}
	}
	
	public Action getLhs(DoAction actionTag, SymbolTable st){
		if(operation instanceof AstVariableLocal){
			AstVariableLocal temp = (AstVariableLocal)operation;
			temp.setNativeFlag();
			return temp.getLhs(actionTag,st);
		}else if(operation instanceof AstConstant){
			AstConstant temp = (AstConstant)operation;
			temp.setNativeFlag();
			return temp.getLhs(actionTag,st);
		}else{
			if(Global.verbose){
				System.out.println("AstNative.getLhs: Invalid native call type.  Line: "+operation.getLine());
			}
			throw new RuntimeException("Error: Invalid native call type.  Line: "+operation.getLine());
		}
	}
}