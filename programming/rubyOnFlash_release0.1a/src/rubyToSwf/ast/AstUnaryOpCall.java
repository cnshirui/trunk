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
  * Represents a Unary operator call
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstUnaryOpCall implements IAstExpression{
	//public const
	public static final int OP_UPLUS = 1;
	public static final int OP_UMINUS = 2;
	public static final int OP_NOT = 3;

	//private instance fields
	private IAstExpression operand;
	private int opType;
	
	private int line;
	
	/**
	  * Constructor
	  * @param operand The operand expression tree
	  * @param op It must a valid operator, as defined by the constants
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstUnaryOpCall(IAstExpression operand, int op, int line){		
		if(op>0 && op<4){
			opType = op;
		}else{
			throw new RuntimeException("Invalid Unary operator!!!");
		}
		
		this.operand = operand;
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.UNARY_OP_CALL;
	}
	
	/**
	  * @return The string representation of this node, in the format: opStringoperand.toString(), note that there is no space in between
	*/
	public String toString(){
		String opString="";
		
		switch(opType){
			case AstUnaryOpCall.OP_UPLUS:
				opString ="+";
				break;
			case AstUnaryOpCall.OP_UMINUS:
				opString ="-";
				break;
		}
		
		return opString +operand.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		switch(opType){
		case OP_UPLUS:{
			Push push0 = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0.addValue(zeroVal);
			actionTag.addAction(push0);
			operand.generateCode(actionTag,st);
			Push pushUPlus = new Push();
			Push.StackValue uPlusVal = new Push.StackValue();
			uPlusVal.setString("@+");
			pushUPlus.addValue(uPlusVal);
			actionTag.addAction(pushUPlus);
			
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
			break;
		case OP_UMINUS:{
			Push push0 = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0.addValue(zeroVal);
			actionTag.addAction(push0);
			operand.generateCode(actionTag,st);
			Push pushUMinus = new Push();
			Push.StackValue uMinusVal = new Push.StackValue();
			uMinusVal.setString("@-");
			pushUMinus.addValue(uMinusVal);
			actionTag.addAction(pushUMinus);
			
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
			break;
		case OP_NOT:{
			operand.generateCode(actionTag,st);
			CodeGenUtil.genCheckNullNilFalse(actionTag);
			actionTag.addAction(new Not());
			//create true or false accordingly
			
			ActionBlock trueBlock = new ActionBlock();
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			trueBlock.addAction(pushEnv);
			trueBlock.addAction(new GetVariable());
			Push pushTrue = new Push();
			Push.StackValue trueVal = new Push.StackValue();
			trueVal.setString("true");
			pushTrue.addValue(trueVal);
			trueBlock.addAction(pushTrue);
			trueBlock.addAction(new GetMember());
			
			ActionBlock falseBlock = new ActionBlock();
			falseBlock.addAction(pushEnv);
			falseBlock.addAction(new GetVariable());
			Push pushFalse = new Push();
			Push.StackValue falseVal = new Push.StackValue();
			falseVal.setString("false");
			pushFalse.addValue(falseVal);
			falseBlock.addAction(pushFalse);
			falseBlock.addAction(new GetMember());
			falseBlock.addAction(new Jump((short)trueBlock.getSize()));
			
			actionTag.addAction(new If((short)falseBlock.getSize()));
			
			CodeGenUtil.copyTags(actionTag, falseBlock);
			CodeGenUtil.copyTags(actionTag, trueBlock);
			
			}
			break;
		}
	}
}