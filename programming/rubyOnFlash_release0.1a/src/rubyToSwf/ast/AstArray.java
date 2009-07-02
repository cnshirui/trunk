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

public class AstArray implements IAstLiteral{
	private Vector<IAstExpression> elements;
	private int line;
	
	public AstArray(Vector<IAstExpression> elements,int line){
		this.line = line;
		this.elements = elements;
	}
	
	public Object getValue(){
		return elements;
	}
	
	public String toString(){
		String res = "Array:";
		if(elements==null){
			res += "[]";
		}else{
			for(Enumeration<IAstExpression> e = elements.elements();e.hasMoreElements();){
				res += "\n\t"+((Object)e.nextElement()).toString();
			}
		}
		
		return res;
	}
	
	public void addElement(IAstExpression expr){
		if(expr==null){
			throw new RuntimeException("Cannot add null expr to an array");
		}
		
		elements.add(expr);
	}
	
	public void setElements(Vector<IAstExpression> elements){
		this.elements = elements;
	}
	
	public Vector<IAstExpression> getElements(){
		return elements;
	}
	
	public int getType(){
		return IAstNode.ARRAY;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getSize(){
		return elements.size();
	}
	
	/**
	 * Instead of using Action InitArray, an Array instance is created instead.  
	 * This is because the InitArray action expects array arguments/elements to be on the stack in reverse order, 
	 * which means that the arguments will be evaluated in reverse order as well.  
	 * E.g. 
	 * <code>
	 * 		i = 0;
	 * 		array1 = [i=i+1,i=i+2,i=i+3];
	 * 		array1[0] -> returns 6 instead of 1
	 * </code>
	 */
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push push0Array = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0Array.addValue(zeroVal);
		Push.StackValue arrayVal = new Push.StackValue();
		arrayVal.setString("Array");
		push0Array.addValue(arrayVal);
		actionTag.addAction(push0Array);
		actionTag.addAction(new NewObject());
		
		int numElem = 0;
		
		if(elements!=null){
			numElem = elements.size();
		}
		
		for(int i=0;i<numElem;i++){
			actionTag.addAction(new PushDuplicate());
			Push push0Index1Integer = new Push();
			push0Index1Integer.addValue(zeroVal);
			Push.StackValue indexVal = new Push.StackValue();
			indexVal.setInteger(i);
			push0Index1Integer.addValue(indexVal);
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			push0Index1Integer.addValue(oneVal);
			Push.StackValue integerVal = new Push.StackValue();
			integerVal.setString("Integer");
			push0Index1Integer.addValue(integerVal);
			actionTag.addAction(push0Index1Integer);
			actionTag.addAction(new NewObject());
			Push pushValueOf = new Push();
			Push.StackValue valueOfVal = new Push.StackValue();
			valueOfVal.setString("valueOf");
			pushValueOf.addValue(valueOfVal);
			actionTag.addAction(pushValueOf);
			actionTag.addAction(new CallMethod());
			elements.elementAt(i).generateCode(actionTag, st);
			actionTag.addAction(new SetMember());
		}
	}
}