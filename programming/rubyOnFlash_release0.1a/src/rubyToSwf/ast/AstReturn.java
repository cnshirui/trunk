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
import rubyToSwf.util.*;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import com.jswiff.swfrecords.*;

import java.io.*;

public class AstReturn implements IAstStatement {

	private int line;
	private IAstExpression expr;
	private boolean bLast;
	
	public AstReturn(IAstExpression expr, int line){
		this.expr = expr;
		this.line = line;
		this.bLast = false;
	}
	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return IAstNode.RETURN;
	}
	
	public String toString(){
		String res = "Return";
		if(expr!=null){
			res += " "+expr.toString();
		}
		return res;
	}

	/**
	 * Not used, as regardless of whether this is a last statement, no pop is necessary, since this statement transfers control
	 */
	public void setLast(){
		this.bLast = true;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(expr==null){
			Push pushNull = new Push();
			Push.StackValue nullVal = new Push.StackValue();
			nullVal.setNull();
			pushNull.addValue(nullVal);
			actionTag.addAction(pushNull);
		}else{
			expr.generateCode(actionTag,st);
		}
		
		actionTag.addAction(new Return());
	}
}
