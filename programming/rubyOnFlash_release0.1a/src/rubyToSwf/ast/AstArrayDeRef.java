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

import java.util.Vector;

/**
  * Represents an array dereference, ie, accessing an array element
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstArrayDeRef implements IAstExpression{

	//private instance fields
	private IAstExpression receiver;
	private Vector<IAstExpression> args;
	
	private int line;
	
	/**
	  * Constructor
	  * @param receiver The receiver
	  * @param op The operation
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstArrayDeRef(IAstExpression receiver, Vector<IAstExpression> args, int line){		
		this.receiver = receiver;
		this.args = args;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.DEREFERENCE;
	}
	
	/**
	  * @return The string representation of this node, in the format: receiver.toString() :: operation.toString()
	*/
	public String toString(){
		return receiver.toString() + "[" + args.toString()+"]";
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getReceiver(){
		return receiver;
	}
	
	public Vector<IAstExpression> getArgs(){
		return args;
	}
	
	public void setReceiver(IAstExpression expr){
		this.receiver = expr;
	}
	
	public void setArgs(Vector<IAstExpression> args){
		this.args = args;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push pushReceiverTemp = new Push();
		Push.StackValue receiverTempVal = new Push.StackValue();
		receiverTempVal.setString("receiverTemp "+Global.getCounter());
		pushReceiverTemp.addValue(receiverTempVal);
		actionTag.addAction(pushReceiverTemp);
		receiver.generateCode(actionTag,st);
		actionTag.addAction(new DefineLocal());

		Push pushDummyArrayName = new Push();
		Push.StackValue dummyArrayNameVal = new Push.StackValue();
		dummyArrayNameVal.setString("dummyArray "+Global.getCounter());
		pushDummyArrayName.addValue(dummyArrayNameVal);
		actionTag.addAction(pushDummyArrayName);
		for(int i=0;i<args.size();i++){
			args.get(i).generateCode(actionTag, st);
		}
		Push pushSize = new Push();
		Push.StackValue sizeVal = new Push.StackValue();
		sizeVal.setInteger(args.size());
		pushSize.addValue(sizeVal);
		actionTag.addAction(pushSize);
		actionTag.addAction(new InitArray());
		actionTag.addAction(new DefineLocal());
		
		for(int i=0;i<args.size();i++){
			actionTag.addAction(pushDummyArrayName);
			actionTag.addAction(new GetVariable());
			Push pushI = new Push();
			Push.StackValue iVal = new Push.StackValue();
			iVal.setInteger(i);
			pushI.addValue(iVal);
			actionTag.addAction(pushI);
			actionTag.addAction(new GetMember());
		}
		
		int n = args.size();
		Push pushN = new Push();
		Push.StackValue nVal = new Push.StackValue();
		nVal.setInteger(n);
		pushN.addValue(nVal);
		actionTag.addAction(pushN);
		actionTag.addAction(pushReceiverTemp);
		actionTag.addAction(new GetVariable());
		Push pushIndexer = new Push();
		Push.StackValue indexerVal = new Push.StackValue();
		indexerVal.setString("[]");
		pushIndexer.addValue(indexerVal);
		actionTag.addAction(pushIndexer);
		
		//push null onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		actionTag.addAction(pushNull);
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		actionTag.addAction(push1ClosureStack);
		actionTag.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		actionTag.addAction(pushPush);
		actionTag.addAction(new CallMethod());
		actionTag.addAction(new Pop());
		
		actionTag.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0ClosureStack.addValue(zeroVal);
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
	
	public void generateAssign(DoAction actionTag,SymbolTable st,IAstExpression rightExpr){
		receiver.generateCode(actionTag,st);
		short regNo = ((Integer)st.get("RegisterNo")).shortValue();
		st.set("RegisterNo",new Integer(regNo+1));
		actionTag.addAction(new StoreRegister((short)regNo));
		actionTag.addAction(new Pop());
		
		//push null onto the closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		actionTag.addAction(pushNull);
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		actionTag.addAction(push1ClosureStack);
		actionTag.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		actionTag.addAction(pushPush);
		actionTag.addAction(new CallMethod());
		actionTag.addAction(new Pop());
		
		Push pushDummyArrayName = new Push();
		Push.StackValue dummyArrayNameVal = new Push.StackValue();
		dummyArrayNameVal.setString("dummyArray "+Global.getCounter());
		pushDummyArrayName.addValue(dummyArrayNameVal);
		actionTag.addAction(pushDummyArrayName);
		for(int i=0;i<args.size();i++){
			args.get(i).generateCode(actionTag, st);
		}
		
		rightExpr.generateCode(actionTag, st);
		
		Push pushSize = new Push();
		Push.StackValue sizeVal = new Push.StackValue();
		sizeVal.setInteger(args.size()+1);
		pushSize.addValue(sizeVal);
		actionTag.addAction(pushSize);
		actionTag.addAction(new InitArray());
		actionTag.addAction(new DefineLocal());
		
		for(int i=0;i<args.size()+1;i++){
			actionTag.addAction(pushDummyArrayName);
			actionTag.addAction(new GetVariable());
			Push pushI = new Push();
			Push.StackValue iVal = new Push.StackValue();
			iVal.setInteger(i);
			pushI.addValue(iVal);
			actionTag.addAction(pushI);
			actionTag.addAction(new GetMember());
		}
		
		int n = args.size() + 1;
		Push pushNRegNoIndexer = new Push();
		Push.StackValue nVal = new Push.StackValue();
		nVal.setInteger(n);
		pushNRegNoIndexer.addValue(nVal);
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushNRegNoIndexer.addValue(regNoVal);
		Push.StackValue indexerVal = new Push.StackValue();
		indexerVal.setString("[]=");
		pushNRegNoIndexer.addValue(indexerVal);
		actionTag.addAction(pushNRegNoIndexer);
		actionTag.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0ClosureStack.addValue(zeroVal);
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
		
		st.set("RegisterNo",new Integer(regNo));
	}	
}