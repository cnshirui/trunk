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

import com.jswiff.swfrecords.tags.DoAction;

/**
 * AstLoad encapsulates a load statement.  Unlike Ruby, where load is a method and is thus able to dynamically load and parse a file, 
 * this implementation treats a load statement as a compile time preprocesser.  This is because, this parser cannot be loaded into 
 * the Swf virtual machine at runtime.  
 * @author Lem Hongjian
 *
 */
public class AstLoad implements IAstStatement {

	private int line;
	private boolean bLast=false;
	private AstStatements stmts;
	
	public AstLoad(AstStatements stmts, int line){
		this.line= line;
		this.stmts = stmts;
	}
	
	public void setLast() {
		this.bLast = true;
	}

	/**
	 * AstLoad::generateCode should not be called.  
	 * AstLoad simply encapsulates an AstStatements, which is eventually appended to the parent program.  
	 * Refer to AstStatement::add
	 */
	public void generateCode(DoAction actionTag, SymbolTable st) {
	}

	public void setStatements(AstStatements stmts){
		this.stmts = stmts;
	}
	
	public AstStatements getStatements(){
		return this.stmts;
	}
	
	public int getLine() {
		return line;
	}

	public int getType() {
		return IAstNode.LOAD;
	}

}
