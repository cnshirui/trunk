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
import rubyToSwf.util.*;

import java.util.Vector;
import java.util.Enumeration;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;

import java.io.*;
import com.jswiff.swfrecords.*;

/**
  * Represents a closure
  * @author Lem Hongjian
  * @version 1.0
*/

public class AstClosure implements IAstLiteral{
	
	public static class ClosureFormal{
		private String name;
		private boolean bArray;
		
		public ClosureFormal(String name){
			this.name = name;
			this.bArray = false;
		}
		
		public ClosureFormal(String name, boolean bArray){
			this.name = name;
			this.bArray = bArray;
		}
		
		public String getName(){
			return this.name;
		}
		
		public boolean isArray(){
			return this.bArray;
		}
	}
	private AstStatements statements;
	private Vector<AstClosure.ClosureFormal> formalParams;
	private int line;
	private String redoLabel;
	
	public AstClosure(Vector<AstClosure.ClosureFormal>params, AstStatements statements,int line){
		this.line = line;
		if(params!=null){
			this.formalParams = params;
		}else{
			this.formalParams = new Vector<AstClosure.ClosureFormal>();
		}
		checkUnique();
		
		//check that only last formal can be array type
		for(int i=0;i<formalParams.size()-1;i++){
			if(formalParams.get(i).isArray()){
				if(Global.verbose){
					System.out.println("AstClosure.AstClosure: Only the last formal parameter can be an array type.  Line: "+line);
				}
				throw new RuntimeException("Only the last formal parameter can be an array type.  Line: "+line);
			}
		}
		
		this.statements = statements;
		
		this.redoLabel = "BLOCK_REDO_"+ Global.getCounter();
	}
	
	public String toString(){
		String res = "Closure:\n\tParams:";
		for(Enumeration<AstClosure.ClosureFormal> e = formalParams.elements();e.hasMoreElements();){
			res += "\n\t\t"+((Object)e.nextElement()).toString();
		}
		
		res += "\nBody:\n"+statements.toString();
		return res;
	}
	
	public void setParams(Vector<AstClosure.ClosureFormal>params){
		if(params==null){
			params = new Vector<AstClosure.ClosureFormal>();
		}
		this.formalParams = params;
		
		checkUnique();
	}
	
	public Vector<AstClosure.ClosureFormal> getParams(){
		return this.formalParams;
	}
	
	public void setBody(AstStatements statements){
		this.statements = statements;
	}
	
	public AstStatements getBody(){
		return statements;
	}
	
	public int getType(){
		return IAstNode.CLOSURE;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getSize(){
		return formalParams.size();
	}
	
	public String getRedoLabel(){
		return this.redoLabel;
	}
	protected void checkUnique(){
		java.util.Hashtable<String, Object> ht = new java.util.Hashtable<String,Object>();
		for(int i=0;i<formalParams.size();i++){
			if(ht.get(formalParams.get(i))!=null){
				if(Global.verbose){
					System.out.println("AstClosure.checkUnique: Formal parameter "+formalParams.get(i)+" already exists.  ");
				}
				throw new RuntimeException("ParserError: Formal parameter "+formalParams.get(i)+" already exists.  ");
			}
			ht.put(formalParams.get(i).getName(), new Object());
		}
	}
	
	/**
	  * @return A reference to itself, in contrast to most other primitives that returns a primitive value
	*/
	public Object getValue(){
		return this;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		Push pushEnv = new Push();
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("current Env");
		pushEnv.addValue(envVal);
		actionTag.addAction(pushEnv);
		actionTag.addAction(new GetVariable());
		Push pushEnv0Binding = new Push();
		pushEnv0Binding.addValue(envVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushEnv0Binding.addValue(zeroVal);
		Push.StackValue bindingVal = new Push.StackValue();
		bindingVal.setString("Binding");
		pushEnv0Binding.addValue(bindingVal);
		actionTag.addAction(pushEnv0Binding);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new PushDuplicate());
		Push pushProtoEnv = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoEnv.addValue(protoVal);
		pushProtoEnv.addValue(envVal);
		actionTag.addAction(pushProtoEnv);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetVariable());
		
		Push push1Proc = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1Proc.addValue(oneVal);
		Push.StackValue procVal = new Push.StackValue();
		procVal.setString("Proc");
		push1Proc.addValue(procVal);
		actionTag.addAction(push1Proc);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new PushDuplicate());//@arity
		actionTag.addAction(new PushDuplicate());//call method
		
		Push pushArityNum = new Push();
		Push.StackValue arityVal = new Push.StackValue();
		arityVal.setString("@arity");
		pushArityNum.addValue(arityVal);
		int arity = formalParams.size();
		if(formalParams.size() >0 && formalParams.get(formalParams.size()-1).isArray()){
			arity = arity * -1;
		}
		Push.StackValue numVal = new Push.StackValue();
		numVal.setInteger(arity);
		pushArityNum.addValue(numVal);
		actionTag.addAction(pushArityNum);
		actionTag.addAction(new SetMember());
		
		Push pushCall = new Push();
		Push.StackValue callVal = new Push.StackValue();
		callVal.setString("call");
		pushCall.addValue(callVal);
		actionTag.addAction(pushCall);
		
		//define the method here
		//within the new method body, can use new SymbolTable
		SymbolTable newSt = st.clone();
		DefineFunction2 callFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		callFunc.addAction(pushEnv0Binding);
		
		callFunc.addAction(new NewObject());
		callFunc.addAction(new PushDuplicate());
		
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushProtoThis.addValue(thisVal);
		callFunc.addAction(pushProtoThis);
		callFunc.addAction(new GetVariable());
		Push pushBindingAttr = new Push();
		Push.StackValue bindingAttrVal = new Push.StackValue();
		bindingAttrVal.setString("@binding");
		pushBindingAttr.addValue(bindingAttrVal);
		callFunc.addAction(pushBindingAttr);
		callFunc.addAction(new GetMember());
		
		callFunc.addAction(new SetMember());
		
		callFunc.addAction(new DefineLocal());
		
		//check that if num of args <= current index then dun assign
		for(int i=0;i<formalParams.size()-1;i++){
			Push pushArg = new Push();
			
			callFunc.addAction(pushArg);
			Push.StackValue argVal = new Push.StackValue();
			argVal.setString("arguments");
			pushArg.addValue(argVal);
			callFunc.addAction(new GetVariable());
			Push pushLength = new Push();
			Push.StackValue lengthVal = new Push.StackValue();
			lengthVal.setString("length");
			pushLength.addValue(lengthVal);
			callFunc.addAction(pushLength);
			callFunc.addAction(new GetMember());
			Push pushI = new Push();
			Push.StackValue iVal = new Push.StackValue();
			iVal.setInteger(i);
			pushI.addValue(iVal);
			callFunc.addAction(pushI);
			callFunc.addAction(new Greater());
			callFunc.addAction(new Not());
			
			ActionBlock assignBlock = new ActionBlock();
			assignBlock.addAction(pushEnv);
			assignBlock.addAction(new GetVariable());
			Push pushFormalArg = new Push();
			Push.StackValue formalVal = new Push.StackValue();
			formalVal.setString(formalParams.get(i).getName());
			pushFormalArg.addValue(formalVal);
			pushFormalArg.addValue(argVal);
			assignBlock.addAction(pushFormalArg);
			assignBlock.addAction(new GetVariable());
			assignBlock.addAction(pushI);
			assignBlock.addAction(new GetMember());
			assignBlock.addAction(new SetMember());
			
			callFunc.addAction(new If((short)assignBlock.getSize()));
			
			rubyToSwf.util.CodeGenUtil.copyTags(callFunc.getBody(), assignBlock);
		}
		//check if last arg is array
		if(formalParams.size()>0){
			int i = formalParams.size() - 1;
			if(formalParams.get(formalParams.size()-1).isArray()){
				callFunc.addAction(pushEnv);
				callFunc.addAction(new GetVariable());
				Push pushFormalIndexArg = new Push();
				Push.StackValue formalVal = new Push.StackValue();
				formalVal.setString(formalParams.get(i).getName());
				pushFormalIndexArg.addValue(formalVal);
				Push.StackValue indexVal = new Push.StackValue();
				indexVal.setString("curr Index "+Global.getCounter());
				pushFormalIndexArg.addValue(indexVal);
				Push.StackValue argVal = new Push.StackValue();
				argVal.setString("arguments");
				pushFormalIndexArg.addValue(argVal);
				callFunc.addAction(pushFormalIndexArg);
				callFunc.addAction(new GetVariable());
				Push pushLen = new Push();
				Push.StackValue lenVal = new Push.StackValue();
				lenVal.setString("length");
				pushLen.addValue(lenVal);
				callFunc.addAction(pushLen);
				callFunc.addAction(new GetMember());
				callFunc.addAction(new Decrement());
				callFunc.addAction(new DefineLocal());
				
				ActionBlock exprBlock = new ActionBlock();
				Push pushIndex = new Push();
				pushIndex.addValue(indexVal);
				exprBlock.addAction(pushIndex);
				exprBlock.addAction(new GetVariable());
				Push pushI = new Push();
				Push.StackValue iVal = new Push.StackValue();
				iVal.setInteger(i);
				pushI.addValue(iVal);
				exprBlock.addAction(pushI);
				exprBlock.addAction(new Less2());
				// while index >= i
				
				//loop body statement
				ActionBlock bodyBlock = new ActionBlock();
				Push pushArg = new Push();
				pushArg.addValue(argVal);
				bodyBlock.addAction(pushArg);
				bodyBlock.addAction(new GetVariable());
				bodyBlock.addAction(pushIndex);
				bodyBlock.addAction(new GetVariable());
				bodyBlock.addAction(new GetMember());
				bodyBlock.addAction(pushIndex);
				bodyBlock.addAction(new PushDuplicate());
				bodyBlock.addAction(new GetVariable());
				Push push1 = new Push();
				push1.addValue(oneVal);
				bodyBlock.addAction(push1);
				bodyBlock.addAction(new Subtract());
				bodyBlock.addAction(new SetVariable());
				bodyBlock.addAction(new Jump((short)(-1*(exprBlock.getSize()+(new If((short)0)).getSize()+bodyBlock.getSize()+(new Jump((short)0)).getSize()))));
				
				CodeGenUtil.copyTags(callFunc.getBody(),exprBlock);
				callFunc.addAction(new If((short)bodyBlock.getSize()));
				CodeGenUtil.copyTags(callFunc.getBody(),bodyBlock);
				
				callFunc.addAction(pushArg);
				callFunc.addAction(new GetVariable());
				callFunc.addAction(pushLen);
				callFunc.addAction(new GetMember());
				//callFunc.addAction(push1);
				//callFunc.addAction(new Subtract());
				callFunc.addAction(pushI);
				callFunc.addAction(new Subtract());
				callFunc.addAction(new InitArray());
				callFunc.addAction(new SetMember());				
			}else{
				Push pushArg = new Push();
				
				callFunc.addAction(pushArg);
				Push.StackValue argVal = new Push.StackValue();
				argVal.setString("arguments");
				pushArg.addValue(argVal);
				callFunc.addAction(new GetVariable());
				Push pushLength = new Push();
				Push.StackValue lengthVal = new Push.StackValue();
				lengthVal.setString("length");
				pushLength.addValue(lengthVal);
				callFunc.addAction(pushLength);
				callFunc.addAction(new GetMember());
				//callFunc.addAction(new Decrement());
				Push pushI = new Push();
				Push.StackValue iVal = new Push.StackValue();
				iVal.setInteger(i);
				pushI.addValue(iVal);
				callFunc.addAction(pushI);
				callFunc.addAction(new Greater());
				
				ActionBlock assignBlock = new ActionBlock();
				assignBlock.addAction(pushEnv);
				assignBlock.addAction(new GetVariable());
				Push pushFormalArg = new Push();
				Push.StackValue formalVal = new Push.StackValue();
				formalVal.setString(formalParams.get(i).getName());
				pushFormalArg.addValue(formalVal);
				pushFormalArg.addValue(argVal);
				assignBlock.addAction(pushFormalArg);
				assignBlock.addAction(new GetVariable());
				assignBlock.addAction(pushI);
				assignBlock.addAction(new GetMember());
				assignBlock.addAction(new SetMember());
				
				Jump jump = new Jump((short)assignBlock.getSize());
				
				callFunc.addAction(new If((short)jump.getSize()));
				
				callFunc.addAction(jump);
				rubyToSwf.util.CodeGenUtil.copyTags(callFunc.getBody(), assignBlock);
			}
		}
		
		//generateCode for stmts
		
		//set the enclosingLoop_Closure
		newSt.set("enclosingLoop_Closure",this);
		
		DoAction stmtsBody = new DoAction();
		this.statements.generateCode(stmtsBody,newSt);
		((Action)stmtsBody.getActions().getActions().get(0)).setLabel(getRedoLabel());
		
		CodeGenUtil.copyTags(callFunc.getBody(),stmtsBody.getActions());
		
		callFunc.addAction(new Return());
		
		actionTag.addAction(callFunc);
		actionTag.addAction(new SetMember());
		

	}
}