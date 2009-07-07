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
import java.util.List;

/**
  * Represents a Range object
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstRange implements IAstExpression{
	//private instance fields
	private IAstExpression leftExpr;
	private IAstExpression rightExpr;
	private boolean bExcludeLast;
	
	private int line;
	
	/**
	  * Constructor
	  * @param left The left expression tree
	  * @param right The right expression tree
	  * @param bIncludeLast if true, this object represents the ".." operator, otherwise "..."
	*/
	public AstRange(IAstExpression left, IAstExpression right, boolean bExcludeLast, int line){		
		leftExpr = left;
		rightExpr = right;
		this.bExcludeLast = bExcludeLast;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.RANGE;
	}
	
	/**
	  * @return The string representation of this node, in the format: leftExpr.toString() opString rightExpr.toString()
	*/
	public String toString(){
		String opString = "";
		if(bExcludeLast){
			opString = "...";
		}else{
			opString = "..";
		}
		return leftExpr.toString() + " " +opString + " " + rightExpr.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getLeftExpr(){
		return leftExpr;
	}
	
	public IAstExpression getRightExpr(){
		return rightExpr;
	}
	
	public void setLeftExpr(IAstExpression expr){
		this.leftExpr = expr;
	}
	
	public void setRightExpr(IAstExpression expr){
		this.rightExpr = expr;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		
		if(bExcludeLast){
			Push push0TrueClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0TrueClass.addValue(zeroVal);
			Push.StackValue trueClassVal = new Push.StackValue();
			trueClassVal.setString("TrueClass");
			push0TrueClass.addValue(trueClassVal);
			actionTag.addAction(push0TrueClass);
			actionTag.addAction(new NewObject());
		}else{
			Push push0FalseClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0FalseClass.addValue(zeroVal);
			Push.StackValue falseClassVal = new Push.StackValue();
			falseClassVal.setString("FalseClass");
			push0FalseClass.addValue(falseClassVal);
			actionTag.addAction(push0FalseClass);
			actionTag.addAction(new NewObject());
		}
		
		leftExpr.generateCode(actionTag,st);
		rightExpr.generateCode(actionTag,st);
		actionTag.addAction(new StackSwap());
		
		Push push3Range = new Push();
		Push.StackValue threeVal = new Push.StackValue();
		threeVal.setInteger(3);
		push3Range.addValue(threeVal);
		
		Push.StackValue rangeVal = new Push.StackValue();
		rangeVal.setString("Range");
		push3Range.addValue(rangeVal);
		
		actionTag.addAction(push3Range);
		
		actionTag.addAction(new GetVariable());
		Push pushNew = new Push();
		Push.StackValue newVal = new Push.StackValue();
		newVal.setString("new");
		pushNew.addValue(newVal);
		actionTag.addAction(pushNew);
		
		//push onto closure stack
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
}