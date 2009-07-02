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
  * Represents an while loop expression
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstWhileLoop implements IAstExpression{

	//private instance fields
	private IAstExpression condExpr;
	private AstStatements stmts;
	private String contLabel;
	private String breakLabel;
	private String redoLabel;
	
	private int line;
	
	/**
	  * Constructor
	  * @param cond The condition expression
	  * @param stmts The statements to loop
	*/
	public AstWhileLoop(IAstExpression cond, AstStatements stmts, int line){		
		this.condExpr = cond;
		this.stmts = stmts;
		
		this.line = line;
		
		int counter = Global.getCounter();
		contLabel = "WHILE_CONT_"+counter;
		breakLabel = "WHILE_BREAK_"+counter;
		redoLabel = "WHILE_REDO_"+counter;
	}
	
	public int getType(){
		return IAstNode.WHILE_LOOP;
	}
	
	public String toString(){
		return "while "+condExpr.toString() + "do\n\t" + stmts.toString()+"\nend";
	}
	
	public int getLine(){
		return line;
	}
	
	public IAstExpression getCondExpr(){
		return condExpr;
	}
	
	public AstStatements getStmts(){
		return stmts;
	}
	
	public void setCondExpr(IAstExpression expr){
		this.condExpr = expr;
	}
	
	public void setStmts(AstStatements stmts){
		this.stmts = stmts;
	}
	
	public String getContLabel(){
		return this.contLabel;
	}
	
	public String getBreakLabel(){
		return this.breakLabel;
	}
	
	public String getRedoLabel(){
		return this.redoLabel;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){		
		DoAction condBlock = new DoAction();
		condExpr.generateCode(condBlock,st);
		((Action)condBlock.getActions().getActions().get(0)).setLabel(getContLabel());
		
		rubyToSwf.util.CodeGenUtil.genCheckNullNilFalse(condBlock);
		
		condBlock.addAction(new Not());
		If dummyIf = new If((short)0);
		
		//set the enclosingLoop_Closure
		SymbolTable newSt = st.clone();
		newSt.set("enclosingLoop_Closure",this);
		
		DoAction ifTrue = new DoAction();
		stmts.generateCode(ifTrue,newSt);
		ifTrue.addAction(new Pop());
		
		//set label for redo
		((Action)ifTrue.getActions().getActions().get(0)).setLabel(getRedoLabel());
		
		Jump dummyJump = new Jump((short)0);
		int stmtsSize = ifTrue.getActions().getSize() + condBlock.getActions().getSize()+dummyIf.getSize()+dummyJump.getSize();
		ifTrue.addAction(new Jump((short)-(stmtsSize)));
		
		condBlock.addAction(new If((short)ifTrue.getActions().getSize()));
		
		List tempActionsList = condBlock.getActions().getActions();
		for(int i=0;i<tempActionsList.size();i++){
			Action tempAction = (Action)tempActionsList.get(i);
			actionTag.addAction(tempAction);
		}
		tempActionsList = ifTrue.getActions().getActions();
		for(int i=0;i<tempActionsList.size();i++){
			Action tempAction = (Action)tempActionsList.get(i);
			actionTag.addAction(tempAction);
		}
		
		Push.StackValue nilVal = new Push.StackValue();
		nilVal.setNull();
		
		Push push = new Push();
		push.addValue(nilVal);
		push.setLabel(getBreakLabel());
		
		actionTag.addAction(push);
	}
}