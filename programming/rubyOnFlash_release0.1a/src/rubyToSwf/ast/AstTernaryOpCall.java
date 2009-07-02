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
  * Represents a ternary operator call ? :
  * note: currently there is only 1 ternary operator
  * @author Lem Hongjian
  * @version 1.0
*/
public class AstTernaryOpCall implements IAstExpression{
	//private instance fields
	private IAstExpression condExpr;
	private IAstExpression trueBranch;
	private IAstExpression falseBranch;
	private int line;
	
	/**
	  * Constructor
	  * @param condExpr The conditional part
	  * @param trueBranch The true branch
	  * @param falseBranch The false branch
	*/
	public AstTernaryOpCall(IAstExpression condExpr, IAstExpression trueBranch, IAstExpression falseBranch, int line){
		this.condExpr = condExpr;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
		
		this.line = line;
	}
	
	public int getType(){
		return IAstNode.TERNARY_OP_CALL;
	}
	
	/**
	  * @return The string representation of this node, in the format: condExpr.toString() ? trueBranch.toString() : falseBranch.toString()
	*/
	public String toString(){		
		return condExpr.toString() + " ? " + trueBranch.toString() + " : " + falseBranch.toString();
	}
	
	public int getLine(){
		return line;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
	}
}