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
  * Represents an function call
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstFunctionCall implements IAstExpression{
	
	public static class Argument{
		private IAstExpression arg;
		private boolean bArray;
		private boolean bProc;
		private boolean bGenerated;
		//only used for array type arg
		private String arrayName;
		
		public Argument(IAstExpression arg){
			this.arg = arg;
			this.bArray = false;
			this.bProc = false;
			this.bGenerated = false;
		}
		
		public void setArray(){
			this.bArray = true;
		}
		
		public boolean isArray(){
			return this.bArray;
		}
		
		public void setProc(){
			this.bProc = true;
		}
		
		public boolean isProc(){
			return this.bProc;
		}
		
		public boolean isOrdinary(){
			return !(bArray || bProc);
		}
		
		public IAstExpression getArg(){
			return arg;
		}
		
		public String toString(){
			return arg.toString();
		}
		
		public void generateCode(DoAction actionTag, SymbolTable st){
			if(isArray()){
				if(bGenerated){
					Push pushArrayName = new Push();
					Push.StackValue arrayNameVal = new Push.StackValue();
					arrayNameVal.setString(this.arrayName);
					pushArrayName.addValue(arrayNameVal);					
					actionTag.addAction(pushArrayName);
					actionTag.addAction(new GetVariable());
				}else{
					/**
					 * var arrayName = (generateCode)
					 * index = 0;
					 * while(i<arrayName.length){
					 * 	push arrayName[index];
					 * 	index++;
					 * }
					 */
					Push pushArrayName = new Push();
					Push.StackValue arrayNameVal = new Push.StackValue();
					this.arrayName = "array "+Global.getCounter();//set the arrayName field
					arrayNameVal.setString(this.arrayName);
					pushArrayName.addValue(arrayNameVal);					
					actionTag.addAction(pushArrayName);
					arg.generateCode(actionTag, st);
					actionTag.addAction(new DefineLocal());
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
					condBlock.addAction(pushArrayName);
					condBlock.addAction(new GetVariable());
					Push pushLength = new Push();
					Push.StackValue lengthVal = new Push.StackValue();
					lengthVal.setString("length");
					pushLength.addValue(lengthVal);
					condBlock.addAction(pushLength);
					condBlock.addAction(new GetMember());
					condBlock.addAction(new Less2());
					condBlock.addAction(new Not());
					
					ActionBlock bodyBlock = new ActionBlock();
					bodyBlock.addAction(pushArrayName);
					bodyBlock.addAction(new GetVariable());
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
					
					If ifOffset = new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize()));
					
					bodyBlock.addAction(new Jump((short)-(ifOffset.getSize() + condBlock.getSize() + bodyBlock.getSize() + new Jump((short)0).getSize())));
					
					rubyToSwf.util.CodeGenUtil.copyTags(actionTag, condBlock);
					actionTag.addAction(ifOffset);
					rubyToSwf.util.CodeGenUtil.copyTags(actionTag, bodyBlock);
				}
			}else{
				arg.generateCode(actionTag, st);
			}
			bGenerated = true;
		}
	}
	
	private Vector<AstFunctionCall.Argument> args;
	private AstClosure closure;
	private String functionName;
	private int line;
	private boolean methodFlag;
	private boolean nativeFlag;
	
	public AstFunctionCall(String name, Vector<AstFunctionCall.Argument> args, AstClosure closure,int line){
		this.line = line;
		this.functionName = name;
		this.args = args;
		this.closure = closure;
		this.methodFlag = false;
		this.nativeFlag = false;
	}
	
	public String toString(){
		String res = "Function call: "+functionName;
		if(args!=null){
			for(Enumeration<AstFunctionCall.Argument> e = args.elements();e.hasMoreElements();){
				res += "\n\t"+((AstFunctionCall.Argument)e.nextElement()).toString();
			}
		}
		if(closure!=null){
			res += "\n" +closure.toString();
		}
		
		return res;
	}
	
	public void addArg(AstFunctionCall.Argument arg){
		if(arg==null){
			throw new RuntimeException("Cannot add null expr to an array");
		}
		
		args.add(arg);
	}
	
	public int getType(){
		return IAstNode.FUNCTION_CALL;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getArgCount(){
		return args.size();
	}
	
	public void setClosure(AstClosure close){
		closure = close;
	}
	
	public AstClosure getClosure(){
		return closure;
	}
	
	public Vector<AstFunctionCall.Argument> getArgs(){
		return args;
	}
	
	public void setArgs(Vector<AstFunctionCall.Argument> args){
		this.args = args;
	}
	
	public void setFunctionName(String name){
		this.functionName = name;
	}
	
	public String getFunctionName(){
		return functionName;
	}
	
	/**
	  * @return A reference to itself, in contrast to most other primitives that returns a primitive value
	*/
	public Object getValue(){
		return this;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){		
		if(args==null){
			args = new Vector<AstFunctionCall.Argument>();
		}
		
		Push pushDummyArrayName = new Push();
		Push.StackValue dummyArrayNameVal = new Push.StackValue();
		dummyArrayNameVal.setString("dummyArray "+Global.getCounter());
		pushDummyArrayName.addValue(dummyArrayNameVal);
		actionTag.addAction(pushDummyArrayName);
		
		for(int i=0;i<args.size();i++){
			args.elementAt(i).generateCode(actionTag,st);
		}

		if(closure!=null){
			closure.generateCode(actionTag,st);
		}else{
			if(!hasProcArg()){
				Push pushNull = new Push();
				Push.StackValue nullVal = new Push.StackValue();
				nullVal.setNull();
				pushNull.addValue(nullVal);
				actionTag.addAction(pushNull);
			}
		}
		
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
		
		this.generateArgCount(actionTag, st);
		//actionTag.addAction(new Increment());
		actionTag.addAction(new InitArray());
		/*
		short regNo = ((Integer)st.get("RegisterNo")).shortValue();
		st.set("RegisterNo", new Integer(regNo+1));
		actionTag.addAction(new StoreRegister(regNo));
		actionTag.addAction(new Pop());
		*/
		actionTag.addAction(new DefineLocal());
		
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
		/*
		Push pushReg = new Push();
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		condBlock.addAction(pushReg);
		*/
		condBlock.addAction(pushDummyArrayName);
		condBlock.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		condBlock.addAction(pushLength);
		condBlock.addAction(new GetMember());
		condBlock.addAction(new Less2());
		condBlock.addAction(new Not());
		
		ActionBlock bodyBlock = new ActionBlock();
		//bodyBlock.addAction(pushReg);
		bodyBlock.addAction(pushDummyArrayName);
		bodyBlock.addAction(new GetVariable());
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
		
		//st.set("RegisterNo", new Integer(regNo));
		
		this.generateArgCount(actionTag, st);
		//actionTag.addAction(new Increment());
		
		Push pushName = new Push();
		Push.StackValue funcName = new Push.StackValue();
		funcName.setString(functionName);
		
		pushName.addValue(funcName);
		actionTag.addAction(pushName);
		
		if(nativeFlag){
			actionTag.addAction(new CallFunction());
			
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
		}else{
			if(!methodFlag){
				Push pushEnv = new Push();
				Push.StackValue envVal = new Push.StackValue();
				envVal.setString("current Env");
				pushEnv.addValue(envVal);
				actionTag.addAction(pushEnv);
				actionTag.addAction(new GetVariable());
				Push pushSelf = new Push();
				Push.StackValue selfVal = new Push.StackValue();
				selfVal.setString("self");
				pushSelf.addValue(selfVal);
				actionTag.addAction(pushSelf);
				actionTag.addAction(new GetMember());
				actionTag.addAction(new StackSwap());
				actionTag.addAction(new CallMethod());
				
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
			}
		}
	}
	
	public void setMethodFlag(){
		this.methodFlag=true;
	}
	
	public void setNativeFlag(){
		this.nativeFlag = true;
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