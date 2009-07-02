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
import java.util.List;

/**
  * Represents a Binary operator call
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstBinaryOpCall implements IAstExpression{
	//public const
	public static final int OP_PLUS = 1; //+
	public static final int OP_MINUS = 2; // -
	public static final int OP_MULTIPLY = 3; // *
	public static final int OP_DIVIDE = 4; // /
	public static final int OP_MODULO = 5; // %
	public static final int OP_POW = 6; // **
	public static final int OP_GREAT = 7; // >
	public static final int OP_LESS = 8; // <
	public static final int OP_GEQ = 9; // >=
	public static final int OP_LEQ = 10; // <=
	public static final int OP_EQ = 11; // ==
	public static final int OP_EQQ = 12; // ===
	public static final int OP_NEQ = 13; // !=
	public static final int OP_ASSIGN = 14; // =, however, lhs should be a variable
	public static final int OP_AND = 15; // &&
	public static final int OP_OR = 16; // ||
	public static final int OP_COMPARE = 17; // <=>
	public static final int OP_BIT_AND = 18; // &
	public static final int OP_BIT_OR = 19; // |
	public static final int OP_BIT_XOR = 20; // ^
	public static final int OP_LEFT_SHIFT = 21; // <<
	public static final int OP_RIGHT_SHIFT = 22; // >>

	//private instance fields
	private IAstExpression leftExpr;
	private IAstExpression rightExpr;
	private int opType;
	
	private int line;
	
	/**
	  * Constructor
	  * @param left The left expression tree
	  * @param right The right expression tree
	  * @param op It must a valid operator, as defined by the constants
	  * @throw java.lang.RuntimeException if op is not a valid operator
	*/
	public AstBinaryOpCall(IAstExpression left, IAstExpression right, int op, int line){		
		if(op>0 && op<23){
			opType = op;
		}else{
			throw new RuntimeException("Invalid binary operator!!!");
		}
		leftExpr = left;
		rightExpr = right;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.BINARY_OP_CALL;
	}
	
	/**
	  * @return The string representation of this node, in the format: leftExpr.toString() opString rightExpr.toString()
	*/
	public String toString(){
		String opString = "";
		
		switch(opType){
			case AstBinaryOpCall.OP_PLUS:
				opString ="+";
				break;
			case AstBinaryOpCall.OP_MINUS:
				opString ="-";
				break;
			case AstBinaryOpCall.OP_MULTIPLY:
				opString ="*";
				break;
			case AstBinaryOpCall.OP_DIVIDE:
				opString ="/";
				break;
			case AstBinaryOpCall.OP_MODULO:
				opString ="%";
				break;
			case AstBinaryOpCall.OP_POW:
				opString ="**";
				break;
			case AstBinaryOpCall.OP_GREAT:
				opString = ">";
				break;
			case AstBinaryOpCall.OP_LESS:
				opString = "<";
				break;
			case AstBinaryOpCall.OP_GEQ:
				opString = ">=";
				break;
			case AstBinaryOpCall.OP_LEQ:
				opString = "<=";
				break;
			case AstBinaryOpCall.OP_COMPARE:
				opString = "<=>";
				break;
			case AstBinaryOpCall.OP_EQ:
				opString = "==";
				break;
			case AstBinaryOpCall.OP_EQQ:
				opString = "===";
				break;
			case AstBinaryOpCall.OP_NEQ:
				opString = "!=";
				break;
			case AstBinaryOpCall.OP_ASSIGN:
				opString = "=";
				break;
			case AstBinaryOpCall.OP_AND:
				opString = "&&";
				break;
			case AstBinaryOpCall.OP_OR:
				opString = "||";
				break;
			case AstBinaryOpCall.OP_BIT_AND:
				opString = "&";
				break;
			case AstBinaryOpCall.OP_BIT_OR:
				opString = "|";
				break;
			case AstBinaryOpCall.OP_BIT_XOR:
				opString = "^";
				break;
			case AstBinaryOpCall.OP_LEFT_SHIFT:
				opString = "<<";
				break;
			case AstBinaryOpCall.OP_RIGHT_SHIFT:
				opString = ">>";
				break;
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
		//TODO: assignment is a special case for lhs		
		switch(opType){
			case AstBinaryOpCall.OP_ASSIGN:
				if(leftExpr instanceof AstVariableLocal){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstVariableLocal)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstConstant){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstConstant)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstVariableInstance){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstVariableInstance)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstVariableClass){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstVariableClass)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstVariableGlobal){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstVariableGlobal)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstDereference){
					short regNo = ((Integer)st.get("RegisterNo")).shortValue();
					st.set("RegisterNo",new Integer(regNo+1));
					Action act = ((AstDereference)leftExpr).getLhs(actionTag,st);
					rightExpr.generateCode(actionTag,st);
					actionTag.addAction(new StoreRegister(regNo));
					actionTag.addAction(act);
					Push.StackValue regNoVal = new Push.StackValue();
					regNoVal.setRegisterNumber(regNo);
					Push pushRegNo = new Push();
					pushRegNo.addValue(regNoVal);
					actionTag.addAction(pushRegNo);
					st.set("RegisterNo",new Integer(regNo));
				}else if(leftExpr instanceof AstArrayDeRef){
					((AstArrayDeRef)leftExpr).generateAssign(actionTag,st,rightExpr);
				}
				break;
			case AstBinaryOpCall.OP_AND:{
				
					//short circuit
					leftExpr.generateCode(actionTag,st);
					actionTag.addAction(new PushDuplicate());
					CodeGenUtil.genCheckNullNilFalse(actionTag);
					actionTag.addAction(new PushDuplicate());
					actionTag.addAction(new Not());
					
					//if reached here, it means that left expr is true, so result depends only on right expr
					DoAction rightBlock = new DoAction();
					rightBlock.addAction(new Pop());
					rightBlock.addAction(new Pop());
					rightExpr.generateCode(rightBlock,st);
					rightBlock.addAction(new PushDuplicate());
					CodeGenUtil.genCheckNullNilFalse(rightBlock);
					//rightBlock.addAction(new And());
					
					actionTag.addAction(new If((short)rightBlock.getActions().getSize()));
					
					List rightActionsList = rightBlock.getActions().getActions();
					for(int i=0;i<rightActionsList.size();i++){
						actionTag.addAction((Action)rightActionsList.get(i));
					}
					
					//invariant: by the time the code reached here, there should be 2 values on the stack, true/false and the last evaluated value
					//the If action would remove the true/false value
					//if true, return last evaluated value
					//if false, return false obj
					
					ActionBlock returnFalseBlock = new ActionBlock();
					returnFalseBlock.addAction(new Pop());
					Push pushEnv = new Push();
					Push.StackValue envVal = new Push.StackValue();
					envVal.setString("current Env");
					pushEnv.addValue(envVal);
					returnFalseBlock.addAction(pushEnv);
					returnFalseBlock.addAction(new GetVariable());
					Push pushFalse = new Push();
					Push.StackValue falseVal = new Push.StackValue();
					falseVal.setString("false");
					pushFalse.addValue(falseVal);
					returnFalseBlock.addAction(pushFalse);
					returnFalseBlock.addAction(new GetMember());
					
					actionTag.addAction(new If((short)returnFalseBlock.getSize()));
					
					CodeGenUtil.copyTags(actionTag, returnFalseBlock);
				}
				break;
			case AstBinaryOpCall.OP_OR:{
					//short circuit
					leftExpr.generateCode(actionTag,st);
					actionTag.addAction(new PushDuplicate());
					CodeGenUtil.genCheckNullNilFalse(actionTag);
					actionTag.addAction(new PushDuplicate());
					
					//if reached here, it means that left expr is true, so result depends only on right expr
					DoAction rightBlock = new DoAction();
					rightBlock.addAction(new Pop());
					rightBlock.addAction(new Pop());
					rightExpr.generateCode(rightBlock,st);
					rightBlock.addAction(new PushDuplicate());
					CodeGenUtil.genCheckNullNilFalse(rightBlock);
					
					actionTag.addAction(new If((short)rightBlock.getActions().getSize()));
					
					List rightActionsList = rightBlock.getActions().getActions();
					for(int i=0;i<rightActionsList.size();i++){
						actionTag.addAction((Action)rightActionsList.get(i));
					}
					
					//invariant: by the time the code reached here, there should be 2 values on the stack, true/false and the last evaluated value
					//the If action would remove the true/false value
					//if true, return last evaluated value
					//if false, return false obj
					
					ActionBlock returnFalseBlock = new ActionBlock();
					returnFalseBlock.addAction(new Pop());
					Push pushEnv = new Push();
					Push.StackValue envVal = new Push.StackValue();
					envVal.setString("current Env");
					pushEnv.addValue(envVal);
					returnFalseBlock.addAction(pushEnv);
					returnFalseBlock.addAction(new GetVariable());
					Push pushFalse = new Push();
					Push.StackValue falseVal = new Push.StackValue();
					falseVal.setString("false");
					pushFalse.addValue(falseVal);
					returnFalseBlock.addAction(pushFalse);
					returnFalseBlock.addAction(new GetMember());
					
					actionTag.addAction(new If((short)returnFalseBlock.getSize()));
					
					CodeGenUtil.copyTags(actionTag, returnFalseBlock);

				}
				break;
			default:{				
				leftExpr.generateCode(actionTag,st);
				rightExpr.generateCode(actionTag,st);
				actionTag.addAction(new StackSwap());
				Push push1 = new Push();
				Push.StackValue oneVal = new Push.StackValue();
				oneVal.setInteger(1);
				push1.addValue(oneVal);
				actionTag.addAction(push1);
				actionTag.addAction(new StackSwap());
				
				String opString = "";
				switch(opType){
				case AstBinaryOpCall.OP_PLUS:
					opString = "+";
					break;
				case AstBinaryOpCall.OP_MINUS:
					opString = "-";
					break;
				case AstBinaryOpCall.OP_MULTIPLY:
					opString = "*";
					break;
				case AstBinaryOpCall.OP_DIVIDE:
					opString = "/";
					break;
				case AstBinaryOpCall.OP_MODULO:
					opString = "%";
					break;
				case AstBinaryOpCall.OP_POW:
					opString = "**";
					break;
				case AstBinaryOpCall.OP_GREAT:
					opString = ">";
					break;
				case AstBinaryOpCall.OP_LESS:
					opString = "<";
					break;
				case AstBinaryOpCall.OP_GEQ:
					opString = ">=";
					break;
				case AstBinaryOpCall.OP_LEQ:
					opString = "<=";
					break;
				case AstBinaryOpCall.OP_EQ:
					opString = "==";
					break;
				case AstBinaryOpCall.OP_COMPARE:
					opString = "<=>";
					break;
				case AstBinaryOpCall.OP_EQQ:
					opString = "===";
					break;
				case AstBinaryOpCall.OP_NEQ:
					opString = "!=";
					break;
				case AstBinaryOpCall.OP_BIT_AND:
					opString = "&";
					break;
				case AstBinaryOpCall.OP_BIT_OR:
					opString = "|";
					break;
				case AstBinaryOpCall.OP_BIT_XOR:
					opString = "^";
					break;
				case AstBinaryOpCall.OP_LEFT_SHIFT:
					opString = "<<";
					break;
				case AstBinaryOpCall.OP_RIGHT_SHIFT:
					opString = ">>";
					break;
				default:
					if(Global.verbose){
						System.out.println("AstBinaryOpCall::generateCode: Invalid binary operation.  Line: "+line);
					}
					throw new RuntimeException("Invalid binary operation.  Line: "+line);
				}
				
				Push pushOp = new Push();
				Push.StackValue opVal = new Push.StackValue();
				opVal.setString(opString);
				pushOp.addValue(opVal);
				actionTag.addAction(pushOp);
				
				//push null onto the stack
				Push pushNull = new Push();
				Push.StackValue nullVal = new Push.StackValue();
				nullVal.setNull();
				pushNull.addValue(nullVal);
				actionTag.addAction(pushNull);
				Push push1ClosureStack = new Push();
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
				break;
				
		}
	}
}