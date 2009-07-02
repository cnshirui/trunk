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
import java.util.Vector;
import java.util.Enumeration;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;

import java.io.*;
import com.jswiff.swfrecords.*;

/**
  * Represents an array
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstHash implements IAstLiteral{
	private Vector<IAstExpression> keys;
	private Vector<IAstExpression> values;
	private int line;
	
	public AstHash(int line){
		this.line = line;
		this.keys = new Vector<IAstExpression>();
		this.values = new Vector<IAstExpression>();
	}
	
	/**
	 * Returns the keys vector
	 * @return the keys vector
	 */
	public Object getValue(){
		return keys;
	}
	
	public String toString(){
		String res = "Hash:";
		for(int i=0;i<keys.size();i++){
			res += "\n\tKey: "+keys.elementAt(i).toString()+" Value: "+values.elementAt(i).toString();
		}
		return res;
	}
	
	public void add(IAstExpression key, IAstExpression value){
		if(key==null || value==null){
			throw new RuntimeException("Cannot add null expressions to a hash");
		}
		
		keys.add(key);
		values.add(value);
	}
	
	public void setKeys(Vector<IAstExpression> elements){
		this.keys = elements;
	}
	
	public Vector<IAstExpression> getKeys(){
		return keys;
	}
	
	public void setValues(Vector<IAstExpression> elements){
		this.values = elements;
	}
	
	public Vector<IAstExpression> getValues(){
		return values;
	}
	
	public int getType(){
		return IAstNode.HASH;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getSize(){
		return keys.size();
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push pushHashTemp = new Push();
		Push.StackValue hashTempVal = new Push.StackValue();
		hashTempVal.setString("hashTemp "+Global.getCounter());
		pushHashTemp.addValue(hashTempVal);
		actionTag.addAction(pushHashTemp);
		Push push0Hash = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0Hash.addValue(zeroVal);
		Push.StackValue hashVal = new Push.StackValue();
		hashVal.setString("Hash");
		push0Hash.addValue(hashVal);
		
		actionTag.addAction(push0Hash);
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
		
		actionTag.addAction(new NewObject());
		
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
		
		actionTag.addAction(new DefineLocal());
		
		for(int i=0;i<keys.size();i++){
			keys.elementAt(i).generateCode(actionTag,st);
			values.elementAt(i).generateCode(actionTag,st);
			actionTag.addAction(new StackSwap());
			Push push2 = new Push();
			Push.StackValue twoVal = new Push.StackValue();
			twoVal.setInteger(2);
			push2.addValue(twoVal);
			actionTag.addAction(push2);
			actionTag.addAction(pushHashTemp);
			actionTag.addAction(new GetVariable());
			Push pushIndexerAssign = new Push();
			Push.StackValue indexerAssignVal = new Push.StackValue();
			indexerAssignVal.setString("[]=");
			pushIndexerAssign.addValue(indexerAssignVal);
			actionTag.addAction(pushIndexerAssign);
			
			//push onto closure stack
			actionTag.addAction(pushNull);
			actionTag.addAction(push1ClosureStack);
			actionTag.addAction(new GetVariable());
			actionTag.addAction(pushPush);
			actionTag.addAction(new CallMethod());
			actionTag.addAction(new Pop());
			
			actionTag.addAction(new CallMethod());
			
			//pop from closure stack
			actionTag.addAction(push0ClosureStack);
			actionTag.addAction(new GetVariable());
			actionTag.addAction(pushPop);
			actionTag.addAction(new CallMethod());
			actionTag.addAction(new Pop());
			
			actionTag.addAction(new Pop());
		}
		
		actionTag.addAction(pushHashTemp);
		actionTag.addAction(new GetVariable());
	}
}