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

import rubyToSwf.codegen.SymbolTable;

import rubyToSwf.common.*;
import rubyToSwf.codegen.*;
import rubyToSwf.util.*;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import com.jswiff.swfrecords.*;

import java.util.Vector;

public class AstCase implements IAstExpression {
	
	public static class When{
		private IAstExpression expr;
		private AstStatements stmts;
		private int line;
		
		public When(IAstExpression expr,AstStatements stmts, int line){
			this.expr = expr;
			this.stmts = stmts;
			this.line = line;
		}
		
		public int getLine(){
			return line;
		}
		
		public AstStatements getStmts(){
			return this.stmts;
		}
		
		public IAstExpression getExpr(){
			return this.expr;
		}
	}
	
	private int line;
	private IAstExpression expr;
	private Vector<AstCase.When> whens;
	private AstStatements elseStmts;
	
	public AstCase(IAstExpression expr, Vector<AstCase.When> whens,AstStatements elseStmts, int line){
		this.expr = expr;
		this.whens = whens;
		this.elseStmts = elseStmts;
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}

	public int getType() {
		return IAstNode.CASE;
	}

	public void generateCode(DoAction actionTag,SymbolTable st){
		expr.generateCode(actionTag, st);
		
		DoAction elseBlock = new DoAction();
		elseBlock.addAction(new Pop());
		if(elseStmts == null){
			//push nil
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			elseBlock.addAction(pushEnv);
			elseBlock.addAction(new GetVariable());
			Push pushNil = new Push();
			Push.StackValue nilVal = new Push.StackValue();
			nilVal.setString("nil");
			pushNil.addValue(nilVal);
			elseBlock.addAction(pushNil);
			elseBlock.addAction(new GetMember());
		}else{
			elseStmts.generateCode(elseBlock, st);
		}
		
		ActionBlock[] blocks = new ActionBlock[whens.size()];
		
		int last = whens.size()-1;		
		int offset = elseBlock.getActions().getSize();
		
		for(int i=last;i>=0;i--){
			blocks[i] = new ActionBlock();
			blocks[i].addAction(new PushDuplicate());			
			//blocks[i].addAction(new StackSwap());
			
			Push push1 = new Push();
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			push1.addValue(oneVal);
			blocks[i].addAction(push1);
			DoAction temp = new DoAction();
			whens.get(i).getExpr().generateCode(temp, st);
			CodeGenUtil.copyTags(blocks[i], temp.getActions());
			Push pushEq3 = new Push();
			Push.StackValue eq3Val = new Push.StackValue();
			eq3Val.setString("===");
			pushEq3.addValue(eq3Val);
			blocks[i].addAction(pushEq3);
			
			//push onto closure stack
			Push pushNull = new Push();
			Push.StackValue nullVal = new Push.StackValue();
			nullVal.setNull();
			pushNull.addValue(nullVal);
			blocks[i].addAction(pushNull);
			
			Push push1ClosureStack = new Push();
			push1ClosureStack.addValue(oneVal);
			Push.StackValue closureStackVal = new Push.StackValue();
			closureStackVal.setString("closure Stack");
			push1ClosureStack.addValue(closureStackVal);
			blocks[i].addAction(push1ClosureStack);
			blocks[i].addAction(new GetVariable());
			Push pushPush = new Push();
			Push.StackValue pushVal = new Push.StackValue();
			pushVal.setString("push");
			pushPush.addValue(pushVal);
			blocks[i].addAction(pushPush);
			blocks[i].addAction(new CallMethod());
			blocks[i].addAction(new Pop());
			
			blocks[i].addAction(new CallMethod());
			
			//pop from closure stack
			Push push0ClosureStack = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0ClosureStack.addValue(zeroVal);
			push0ClosureStack.addValue(closureStackVal);
			blocks[i].addAction(push0ClosureStack);
			blocks[i].addAction(new GetVariable());
			Push pushPop = new Push();
			Push.StackValue popVal = new Push.StackValue();
			popVal.setString("pop");
			pushPop.addValue(popVal);
			blocks[i].addAction(pushPop);
			blocks[i].addAction(new CallMethod());
			blocks[i].addAction(new Pop());
			
			CodeGenUtil.genCheckNullNilFalse(blocks[i]);
			blocks[i].addAction(new Not());
			
			temp = new DoAction();
			temp.addAction(new Pop());
			whens.get(i).getStmts().generateCode(temp, st);
			temp.addAction(new Jump((short)offset));
			
			blocks[i].addAction(new If((short)temp.getActions().getSize()));
			CodeGenUtil.copyTags(blocks[i], temp.getActions());
			
			offset += blocks[i].getSize();
		}

		for(int i=0;i<=last;i++){
			CodeGenUtil.copyTags(actionTag, blocks[i]);
		}
		
		CodeGenUtil.copyTags(actionTag, elseBlock.getActions());
	}
}
