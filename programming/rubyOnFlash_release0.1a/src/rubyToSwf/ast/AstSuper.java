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
  * Represents a call to super
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstSuper implements IAstExpression{
	private Vector<AstFunctionCall.Argument> args;
	private int line;
	private boolean defaultFlag;
	
	public AstSuper(Vector<AstFunctionCall.Argument> args, int line){
		this.line = line;
		this.args = args;
		this.defaultFlag = false;
	}
	
	public String toString(){
		String res = "super";
		
		if(args!=null){
			for(Enumeration<AstFunctionCall.Argument> e = args.elements();e.hasMoreElements();){
				res += "\n\t"+((AstFunctionCall.Argument)e.nextElement()).toString();
			}
		}		
		return res;
	}
	
	public void addArg(AstFunctionCall.Argument expr){
		if(expr==null){
			throw new RuntimeException("Cannot add null expr to an array");
		}
		
		args.add(expr);
	}
	
	public int getType(){
		return IAstNode.SUPER;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getArgCount(){
		return args.size();
	}

	public Vector<AstFunctionCall.Argument> getArgs(){
		return args;
	}
	
	public void setArgs(Vector<AstFunctionCall.Argument> args){
		this.args = args;
	}

	/**
	  * @return A reference to itself, in contrast to most other primitives that returns a primitive value
	*/
	public Object getValue(){
		return this;
	}
	
	public void setDefaultFlag(){
		this.defaultFlag = true;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(defaultFlag){
			Push pushArg = new Push();
			Push.StackValue argVal = new Push.StackValue();
			argVal.setString("arguments");
			pushArg.addValue(argVal);
			actionTag.addAction(pushArg);
			actionTag.addAction(new GetVariable());
			Push pushThis = new Push();
			Push.StackValue thisVal = new Push.StackValue();
			thisVal.setString("this");
			pushThis.addValue(thisVal);
			actionTag.addAction(pushThis);
			actionTag.addAction(new GetVariable());
			Push push2This = new Push();
			Push.StackValue twoVal = new Push.StackValue();
			twoVal.setInteger(2);
			push2This.addValue(twoVal);
			actionTag.addAction(push2This);
			
			actionTag.addAction(pushArg);
			actionTag.addAction(new GetVariable());
			Push pushCallee = new Push();
			Push.StackValue calleeVal = new Push.StackValue();
			calleeVal.setString("callee");
			pushCallee.addValue(calleeVal);
			actionTag.addAction(pushCallee);
			actionTag.addAction(new GetMember());
			Push pushContext = new Push();
			Push.StackValue contextVal = new Push.StackValue();
			contextVal.setString("class Context");
			pushContext.addValue(contextVal);
			actionTag.addAction(pushContext);
			actionTag.addAction(new GetMember());
			Push pushProto = new Push();
			Push.StackValue protoVal = new Push.StackValue();
			protoVal.setString("__proto__");
			pushProto.addValue(protoVal);
			actionTag.addAction(pushProto);
			actionTag.addAction(new GetMember());
			Push pushPrototype = new Push();
			Push.StackValue prototypeVal = new Push.StackValue();
			prototypeVal.setString("prototype");
			pushPrototype.addValue(prototypeVal);
			actionTag.addAction(pushPrototype);
			actionTag.addAction(new GetMember());
			
			//push function name
			actionTag.addAction(pushArg);
			actionTag.addAction(new GetVariable());
			actionTag.addAction(pushCallee);
			actionTag.addAction(new GetMember());
			Push pushMethodName = new Push();
			Push.StackValue methodNameVal = new Push.StackValue();
			methodNameVal.setString("method Name");
			pushMethodName.addValue(methodNameVal);
			actionTag.addAction(pushMethodName);
			actionTag.addAction(new GetMember());
			
			//get the function object
			actionTag.addAction(new GetMember());
			
			Push pushApply = new Push();
			Push.StackValue applyVal = new Push.StackValue();
			applyVal.setString("apply");
			pushApply.addValue(applyVal);
			actionTag.addAction(pushApply);
			actionTag.addAction(new CallMethod());
		}else{
			//the this obj
			Push pushThis = new Push();
			Push.StackValue thisVal = new Push.StackValue();
			thisVal.setString("this");
			pushThis.addValue(thisVal);
			actionTag.addAction(pushThis);
			actionTag.addAction(new GetVariable());
			
			if(args==null){
				args = new Vector<AstFunctionCall.Argument>();
			}			
			for(int i=0;i<args.size();i++){
				args.elementAt(i).generateCode(actionTag,st);
			}
			
			if(!this.hasProcArg()){
				//push a null as the closure arg
				Push pushNull = new Push();
				Push.StackValue nullVal = new Push.StackValue();
				nullVal.setNull();
				pushNull.addValue(nullVal);
				actionTag.addAction(pushNull);
			}
			
			this.generateArgCount(actionTag, st);
			actionTag.addAction(new Increment());// +1 for this object
			actionTag.addAction(new Increment());// +1 for closure
			actionTag.addAction(new InitArray());
			
			short regNo = ((Integer)st.get("RegisterNo")).shortValue();
			st.set("RegisterNo", new Integer(regNo+1));
			actionTag.addAction(new StoreRegister(regNo));
			actionTag.addAction(new Pop());
			
			/**
			 * var index = 0;
			 * while(index < args.length){
			 * 	push argArray[index];
			 * 	index += 1;
			 * }
			 */
			Push pushIndex0 = new Push();
			Push.StackValue indexVal = new Push.StackValue();
			indexVal.setString("index "+Global.getCounter());
			pushIndex0.addValue(indexVal);
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			pushIndex0.addValue(zeroVal);
			actionTag.addAction(pushIndex0);
			actionTag.addAction(new DefineLocal());
			
			ActionBlock condBlock = new ActionBlock();
			Push pushIndex = new Push();
			pushIndex.addValue(indexVal);
			condBlock.addAction(pushIndex);
			condBlock.addAction(new GetVariable());
			Push pushReg = new Push();
			Push.StackValue regNoVal = new Push.StackValue();
			regNoVal.setRegisterNumber(regNo);
			pushReg.addValue(regNoVal);
			condBlock.addAction(pushReg);
			Push pushLength = new Push();
			Push.StackValue lengthVal = new Push.StackValue();
			lengthVal.setString("length");
			pushLength.addValue(lengthVal);
			condBlock.addAction(pushLength);
			condBlock.addAction(new GetMember());
			condBlock.addAction(new Less2());
			condBlock.addAction(new Not());
			
			ActionBlock bodyBlock = new ActionBlock();
			bodyBlock.addAction(pushReg);
			bodyBlock.addAction(pushIndex);
			bodyBlock.addAction(new GetVariable());
			bodyBlock.addAction(new GetMember());
			Push pushIndexIndex = new Push();
			pushIndexIndex.addValue(indexVal);
			pushIndexIndex.addValue(indexVal);
			bodyBlock.addAction(pushIndexIndex);
			bodyBlock.addAction(new GetVariable());
			bodyBlock.addAction(new Increment());
			bodyBlock.addAction(new SetVariable());
			
			If ifTag = new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize()));
			
			bodyBlock.addAction(new Jump((short)-(bodyBlock.getSize() + condBlock.getSize() + ifTag.getSize() + new Jump((short)0).getSize())));
			
			rubyToSwf.util.CodeGenUtil.copyTags(actionTag, condBlock);
			actionTag.addAction(ifTag);
			rubyToSwf.util.CodeGenUtil.copyTags(actionTag, bodyBlock);
			
			st.set("RegisterNo", new Integer(regNo));
			
			this.generateArgCount(actionTag, st);
			actionTag.addAction(new Increment());
			actionTag.addAction(new Increment());
			
			Push pushArg = new Push();
			Push.StackValue argVal = new Push.StackValue();
			argVal.setString("arguments");
			pushArg.addValue(argVal);
			actionTag.addAction(pushArg);
			actionTag.addAction(new GetVariable());
			Push pushCallee = new Push();
			Push.StackValue calleeVal = new Push.StackValue();
			calleeVal.setString("callee");
			pushCallee.addValue(calleeVal);
			actionTag.addAction(pushCallee);
			actionTag.addAction(new GetMember());
			Push pushContext = new Push();
			Push.StackValue contextVal = new Push.StackValue();
			contextVal.setString("class Context");
			pushContext.addValue(contextVal);
			actionTag.addAction(pushContext);
			actionTag.addAction(new GetMember());
			Push pushProto = new Push();
			Push.StackValue protoVal = new Push.StackValue();
			protoVal.setString("__proto__");
			pushProto.addValue(protoVal);
			actionTag.addAction(pushProto);
			actionTag.addAction(new GetMember());
			Push pushPrototype = new Push();
			Push.StackValue prototypeVal = new Push.StackValue();
			prototypeVal.setString("prototype");
			pushPrototype.addValue(prototypeVal);
			actionTag.addAction(pushPrototype);
			actionTag.addAction(new GetMember());
			
			actionTag.addAction(pushArg);
			actionTag.addAction(new GetVariable());
			
			actionTag.addAction(pushCallee);
			actionTag.addAction(new GetMember());
			Push pushMethodName = new Push();
			Push.StackValue methodNameVal = new Push.StackValue();
			methodNameVal.setString("method Name");
			pushMethodName.addValue(methodNameVal);
			actionTag.addAction(pushMethodName);
			actionTag.addAction(new GetMember());
			
			//get the function object
			actionTag.addAction(new GetMember());
			
			Push pushCall = new Push();
			Push.StackValue callVal = new Push.StackValue();
			callVal.setString("call");
			pushCall.addValue(callVal);
			actionTag.addAction(pushCall);
			actionTag.addAction(new CallMethod());
		}
	}
	
	private boolean hasArrayArg(){
		if(args==null){
			return false;
		}
		
		for(int i=0;i<args.size();i++){
			if(args.get(i).isArray()){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasProcArg(){
		if(args==null){
			return false;
		}
		
		for(int i=0;i<args.size();i++){
			if(args.get(i).isProc()){
				return true;
			}
		}
		return false;
	}
	
	//does not include last proc arg
	private void generateArgCount(DoAction actionTag, SymbolTable st){
		int argCount = 0;
		if(args != null){
			argCount = args.size();
		}
		if(hasProcArg()){
			argCount -= 1;
		}
		Push pushArgCount = new Push();
		Push.StackValue argCountVal = new Push.StackValue();
		argCountVal.setInteger(argCount);
		pushArgCount.addValue(argCountVal);
		actionTag.addAction(pushArgCount);
		
		if(hasArrayArg()){
			if(hasProcArg()){
				//use the 2nd last arg
				args.get(args.size()-2).generateCode(actionTag, st);
				Push pushLength = new Push();
				Push.StackValue lengthVal = new Push.StackValue();
				lengthVal.setString("length");
				pushLength.addValue(lengthVal);
				actionTag.addAction(pushLength);
				actionTag.addAction(new GetMember());
				actionTag.addAction(new Add2());
				actionTag.addAction(new Decrement());
			}else{
				//use the last arg
				args.get(args.size()-1).generateCode(actionTag, st);
				Push pushLength = new Push();
				Push.StackValue lengthVal = new Push.StackValue();
				lengthVal.setString("length");
				pushLength.addValue(lengthVal);
				actionTag.addAction(pushLength);
				actionTag.addAction(new GetMember());
				actionTag.addAction(new Add2());
				actionTag.addAction(new Decrement());
			}
		}
	}
}