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
import rubyToSwf.util.CodeGenUtil;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import java.io.*;
import com.jswiff.swfrecords.*;
import java.util.List;

/**
  * Represents an if expression
  * Note that if expr then elsif expr...  else is represented by nesting branches, e.g.
  * if expr1 then stmts1 elsif expr2 then stmts2 else stmts3 end is represented by
  * [stmts1 else [stmts1 else stmts2]]
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstIfExpression implements IAstExpression{

	//private instance fields
	private IAstExpression condExpr;
	private AstStatements thenStmts;
	private AstStatements elseStmts;
	
	private int line;
	
	/**
	  * Constructor
	  * @param cond The condition expression
	  * @param thenPart The statements for the then part
	  * @param elsePart The statements for the else part
	*/
	public AstIfExpression(IAstExpression cond, AstStatements thenPart, AstStatements elsePart, int line){		
		this.condExpr = cond;
		this.thenStmts = thenPart;
		this.elseStmts = elsePart;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.IF_EXPRESSION;
	}
	
	public String toString(){
		return "if "+condExpr.toString() + "then\n\t" + thenStmts.toString()+"\n else\n\t"+elseStmts.toString()+ "\nend";
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getCondExpr(){
		return condExpr;
	}
	
	public AstStatements getThenStmts(){
		return thenStmts;
	}
	
	public AstStatements getElseStmts(){
		return elseStmts;
	}
	
	public void setCondExpr(IAstExpression expr){
		this.condExpr = expr;
	}
	
	public void setThenStmts(AstStatements stmts){
		this.thenStmts = stmts;
	}
	
	public void setElseStmts(AstStatements stmts){
		this.elseStmts = stmts;
	}
	
	/**
	 * The code sequence can be visualized as such:
	 * --------------
	 * | NullBlock  |- checks if object on stack is null, if so, jump to toFalse block
	 * --------------
	 * | NilClass   |- checks if value on stack is an instance of NilClass, if so, jump to toFalse block
	 * --------------
	 * | FalseClass |- checks if value on stack is an instance of FalseClass, if so, jump to the toFalse block
	 * --------------
	 * | toTrue     |- pushes a true value, jumps after toFalse block
	 * --------------
	 * | toFalse    |- pushes a false value
	 * --------------
	 * | If action  |
	 * --------------
	 * | Else Stmts |
	 * --------------
	 * | Then Stmts |
	 * --------------
	 * Note that Null block, NilClass block and FalseClass block must leave a copy of the original value on the stack
	 */
	public void generateCode(DoAction actionTag,SymbolTable st){
		condExpr.generateCode(actionTag,st);
		
		//generates the Null, NilClass, FalseClass, toTrue and toFalse blocks
		CodeGenUtil.genCheckNullNilFalse(actionTag);
		
		DoAction ifTrue = new DoAction();
		thenStmts.generateCode(ifTrue,st);
		DoAction ifFalse = new DoAction();
		if(elseStmts!=null){
			elseStmts.generateCode(ifFalse,st);
		}else{
			//return nil
			Push push0NilClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0NilClass.addValue(zeroVal);
			Push.StackValue nilClassVal = new Push.StackValue();
			nilClassVal.setString("NilClass");
			push0NilClass.addValue(nilClassVal);
			ifFalse.addAction(push0NilClass);
			ifFalse.addAction(new NewObject());
		}
		ifFalse.addAction(new Jump((short)ifTrue.getActions().getSize()));
		
		//append the if tag
		actionTag.addAction(new If((short)ifFalse.getActions().getSize()));
		
		List tempActionsList = ifFalse.getActions().getActions();
		for(int i=0;i<tempActionsList.size();i++){
			Action tempAction = (Action)tempActionsList.get(i);
			actionTag.addAction(tempAction);
		}
				
		tempActionsList = ifTrue.getActions().getActions();
		for(int i=0;i<tempActionsList.size();i++){
			Action tempAction = (Action)tempActionsList.get(i);
			actionTag.addAction(tempAction);
		}
	}
}