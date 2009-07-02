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
 * Encapsulates an expression inside a statement
 * @author Hongjian
 *
 */
public class AstExprStatement implements IAstStatement {

	private IAstExpression expr;
	private boolean bLast;
	
	public AstExprStatement(IAstExpression expr){
		this.expr = expr;
		this.bLast = false;
	}
	
	public void setLast() {
		this.bLast = true;
	}

	public int getLine() {
		return expr.getLine();
	}

	public int getType() {
		return expr.getType();
	}

	public String toString(){
		return expr.toString();
	}
	/**
	 * Pops the last item on the stack if it is not the last statement in a block of statements
	 */
	public void generateCode(DoAction actionTag,SymbolTable st){
		expr.generateCode(actionTag,st);
		
		if(!bLast){
			actionTag.addAction(new Pop());
		}
	}
}
