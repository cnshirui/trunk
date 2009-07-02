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
import java.util.Vector;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;

import java.io.*;
import com.jswiff.swfrecords.*;

/**
  * Represents a method call operation, . or ::
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstMethodCall implements IAstExpression{

	//private instance fields
	private IAstExpression receiver;
	private AstFunctionCall operation;//can only be of type function call
	
	private int line;
	
	/**
	  * Constructor
	  * @param receiver The receiver
	  * @param op The operation
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstMethodCall(IAstExpression receiver, AstFunctionCall op, int line){		
		this.receiver = receiver;
		this.operation = op;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.METHOD_CALL;
	}
	
	/**
	  * @return The string representation of this node, in the format: receiver.toString() :: operation.toString()
	*/
	public String toString(){
		return "("+receiver.toString() + ")." + operation.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getReceiver(){
		return receiver;
	}
	
	public AstFunctionCall getOperation(){
		return operation;
	}
	
	public void setReceiver(IAstExpression expr){
		this.receiver = expr;
	}
	
	public void setOperation(AstFunctionCall expr){
		this.operation = expr;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){	
		Push pushTemp = new Push();
		Push.StackValue tempVal = new Push.StackValue();
		tempVal.setString("temp "+Global.getCounter());
		pushTemp.addValue(tempVal);
		actionTag.addAction(pushTemp);
		receiver.generateCode(actionTag,st);
		actionTag.addAction(new DefineLocal());
		operation.generateCode(actionTag,st);
		actionTag.addAction(pushTemp);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new StackSwap());
		actionTag.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0ClosureStack.addValue(zeroVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push0ClosureStack.addValue(closureStackVal);
		actionTag.addAction(push0ClosureStack);
		actionTag.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		actionTag.addAction(pushPop);
		actionTag.addAction(new CallMethod());
		actionTag.addAction(new Pop());
		
	}
}