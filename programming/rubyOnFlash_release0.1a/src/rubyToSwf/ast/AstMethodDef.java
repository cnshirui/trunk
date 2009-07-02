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

public class AstMethodDef implements IAstExpression {
	
	public static class FormalParam{
		private String name;
		private IAstExpression expr;
		private int line;
		
		private boolean arrayFlag;
		private boolean procFlag;
		
		public FormalParam(String name,IAstExpression expr, int line){
			this.name = name;
			this.expr = expr;
			this.line = line;
			
			this.arrayFlag=false;
			this.procFlag=false;
		}
		
		public int getLine(){
			return line;
		}
		
		public String getName(){
			return this.name;
		}
		
		public IAstExpression getExpr(){
			return this.expr;
		}
		
		public boolean hasDefault(){
			return (this.expr!=null);
		}
		
		public void setArrayFlag(){
			this.arrayFlag=true;
		}
		
		public void setProcFlag(){
			this.procFlag=true;
		}
		
		public boolean isArray(){
			return this.arrayFlag;
		}
		
		public boolean isProc(){
			return this.procFlag;
		}
		
		public boolean isCommon(){
			return (!this.arrayFlag && !this.procFlag && expr==null);
		}
	}
	
	private int line;
	private IAstExpression receiver;
	private String name;
	private Vector<AstMethodDef.FormalParam> formalsList;
	private AstStatements stmts;
	
	public AstMethodDef(IAstExpression receiver, String name, Vector<AstMethodDef.FormalParam> formalsList,AstStatements stmts, int line){
		if((receiver!=null) && !((receiver instanceof AstVariableLocal) || (receiver instanceof AstVariableClass) || (receiver instanceof AstVariableInstance) || (receiver instanceof AstConstant))){
			if(Global.verbose){
				System.out.println("AstMethodDef::constructor: Invalid receiver type for method definition.  Line: "+receiver.getLine());
			}
			throw new RuntimeException("Error: Invalid receiver type for method definition.  Line: "+receiver.getLine());
		}
		this.receiver = receiver;
		this.name = name;
		if(formalsList==null){
			this.formalsList = new Vector<AstMethodDef.FormalParam>();
		}else{
			this.formalsList = formalsList;
		}
		//check that the format of the formals are correct
		//normal formals before defaults, before *, before &
		int numFormals = this.formalsList.size();
		
		int currIndex = 0;
		int currStage = 0;
		
		for(currIndex=0;currIndex<numFormals;currIndex++){
			FormalParam temp = this.formalsList.get(currIndex);
			if(!temp.isCommon()){
				if(temp.hasDefault()){
					currStage=1;
				}else if(temp.isArray()){
					currIndex++;
					currStage = 3;
				}else{
					currStage = 3;
				}
				break;
			}
		}
		
		if(currStage==1){
			for(currIndex=currIndex;currIndex<numFormals;currIndex++){
				FormalParam temp = this.formalsList.get(currIndex);
				if(!temp.hasDefault()){
					if(temp.isArray()){
						currIndex++;
						currStage = 3;
					}else if(temp.isProc()){
						currStage = 3;
					}else{
						if(Global.verbose){
							System.out.println("AstMethodDef (Constructor): Formals with no default values are not allowed behind formals with default formals.  Line:"+temp.getLine());
						}
						throw new RuntimeException("Parse Error: Formals with no default values are not allowed behind formals with default formals.  Line:"+temp.getLine());
					}
				}
			}
		}
		
		if(currStage==3){
			if(currIndex+1 < numFormals){
				if(Global.verbose){
					System.out.println("AstMethodDef (Constructor): Invalid last formal.  Line: "+formalsList.get(currIndex).getLine());
				}
				throw new RuntimeException("Parser Error: Invalid last formal.  Line: "+formalsList.get(currIndex).getLine());
			}
			if(currIndex+1==numFormals && !this.formalsList.get(currIndex).isProc()){
				if(Global.verbose){
					System.out.println("AstMethodDef (Constructor): The last formal should be a proc.  Line: "+formalsList.get(currIndex).getLine());
				}
				throw new RuntimeException("Parser Error: The last formal should be a proc.  Line: "+formalsList.get(currIndex).getLine());
			}
		}
		
		//check that the formals have unique names
		java.util.Hashtable<String, Object> tempHt = new java.util.Hashtable<String, Object>();
		for(int i=0;i<this.formalsList.size();i++){
			String formalName = this.formalsList.get(i).getName();
			if(tempHt.get(formalName)!=null){
				if(Global.verbose){
					System.out.println("AstMethodDef (constructor): The formal parameter "+formalName+" already exists in method "+name+" Line: "+this.formalsList.get(i).getLine());
				}
				throw new RuntimeException("Error: The formal parameter "+formalName+" already exists in method "+name+" Line: "+this.formalsList.get(i).getLine());
			}
			tempHt.put(name, new Object());
		}
		this.stmts=stmts;
		this.line = line;
	}
	
	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return IAstNode.METHOD_DEF;
	}
	
	public String getName(){
		return name;
	}

	public void generateCode(DoAction actionTag,SymbolTable oldSt){
		int numFormals = formalsList.size();
		
		if(receiver==null){
			//assign to env["class Context"].prototype
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			Push pushContext = new Push();
			Push.StackValue contextVal = new Push.StackValue();
			contextVal.setString("class Context");
			pushContext.addValue(contextVal);
			actionTag.addAction(pushContext);
			actionTag.addAction(new GetMember());
			Push pushPrototype = new Push();
			Push.StackValue prototypeVal = new Push.StackValue();
			prototypeVal.setString("prototype");
			pushPrototype.addValue(prototypeVal);
			actionTag.addAction(pushPrototype);
			actionTag.addAction(new GetMember());
		}else{
			receiver.generateCode(actionTag,oldSt);
		}
		
		Push pushName = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString(this.name);
		pushName.addValue(nameVal);
		actionTag.addAction(pushName);
		
		//define function
		DefineFunction2 func = new DefineFunction2("",(short)4,new RegisterParam[0]);
		//clone new symboltable, and reset the registerNum
		SymbolTable st = oldSt.clone();
		st.set("RegisterNo", new Integer(0));
		
		//create new env
		Push pushEnv0Binding = new Push();
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("current Env");
		pushEnv0Binding.addValue(envVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushEnv0Binding.addValue(zeroVal);
		Push.StackValue bindingVal = new Push.StackValue();
		bindingVal.setString("Binding");
		pushEnv0Binding.addValue(bindingVal);
		func.addAction(pushEnv0Binding);
		func.addAction(new NewObject());
		func.addAction(new DefineLocal());
		
		//assign this to env.self
		Push pushEnv = new Push();
		pushEnv.addValue(envVal);
		func.addAction(pushEnv);
		func.addAction(new GetVariable());
		func.addAction(new PushDuplicate());
		Push pushSelfThis = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelfThis.addValue(selfVal);
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushSelfThis.addValue(thisVal);
		func.addAction(pushSelfThis);
		func.addAction(new GetVariable());
		func.addAction(new SetMember());
			
		//assign env['class Context'] = arguments.callee["class Context"]
		//env obj already duplicated on the stack
		Push pushContextArg = new Push();
		Push.StackValue contextVal = new Push.StackValue();
		contextVal.setString("class Context");
		pushContextArg.addValue(contextVal);
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushContextArg.addValue(argVal);
		func.addAction(pushContextArg);
		func.addAction(new GetVariable());
		
		Push pushCallee = new Push();
		Push.StackValue calleeVal = new Push.StackValue();
		calleeVal.setString("callee");
		pushCallee.addValue(calleeVal);
		func.addAction(pushCallee);
		func.addAction(new GetMember());
		Push pushContext = new Push();
		pushContext.addValue(contextVal);
		func.addAction(pushContext);
		func.addAction(new GetMember());
		
		func.addAction(new SetMember());
		
		int i = 0;
		for(i=0;i<formalsList.size();i++){
			FormalParam temp = formalsList.get(i);
			if(temp.isCommon()){
				Push pushArg = new Push();
				pushArg.addValue(argVal);
				func.addAction(pushArg);
				func.addAction(new GetVariable());
				Push pushLen = new Push();
				Push.StackValue lengthVal = new Push.StackValue();
				lengthVal.setString("length");
				pushLen.addValue(lengthVal);
				func.addAction(pushLen);
				func.addAction(new GetMember());
				//func.addAction(new Decrement());				
				Push pushI = new Push();
				Push.StackValue iVal = new Push.StackValue();
				iVal.setInteger(i);
				pushI.addValue(iVal);
				func.addAction(pushI);
				func.addAction(new Greater());
				func.addAction(new Not());
				
				ActionBlock definedBlock = new ActionBlock();
				definedBlock.addAction(pushEnv);
				definedBlock.addAction(new GetVariable());
				Push pushFormalArg = new Push();
				Push.StackValue formalVal = new Push.StackValue();
				formalVal.setString(formalsList.get(i).getName());
				pushFormalArg.addValue(formalVal);				
				pushFormalArg.addValue(argVal);
				definedBlock.addAction(pushFormalArg);
				definedBlock.addAction(new GetVariable());
				definedBlock.addAction(pushI);
				definedBlock.addAction(new GetMember());
				definedBlock.addAction(new SetMember());
				
				func.addAction(new If((short)definedBlock.getSize()));
				rubyToSwf.util.CodeGenUtil.copyTags(func.getBody(), definedBlock);
			}else{
				break;
			}
		}
		
		for(i=i;i<formalsList.size();i++){
			FormalParam temp = formalsList.get(i);
			if(temp.hasDefault()){
				Push pushArg = new Push();
				pushArg.addValue(argVal);
				func.addAction(pushArg);
				func.addAction(new GetVariable());
				Push pushLen = new Push();
				Push.StackValue lengthVal = new Push.StackValue();
				lengthVal.setString("length");
				pushLen.addValue(lengthVal);
				func.addAction(pushLen);
				func.addAction(new GetMember());

				Push pushI = new Push();
				Push.StackValue iVal = new Push.StackValue();
				iVal.setInteger(i);
				pushI.addValue(iVal);
				func.addAction(pushI);
				func.addAction(new Greater());
				
				DoAction useDefaultBlock = new DoAction();
				useDefaultBlock.addAction(pushEnv);
				useDefaultBlock.addAction(new GetVariable());
				Push pushFormal = new Push();
				Push.StackValue formalVal = new Push.StackValue();
				formalVal.setString(formalsList.get(i).getName());
				pushFormal.addValue(formalVal);
				useDefaultBlock.addAction(pushFormal);
				formalsList.get(i).getExpr().generateCode(useDefaultBlock,oldSt);
				useDefaultBlock.addAction(new SetMember());
				
				//use arguments
				ActionBlock useArgBlock = new ActionBlock();
				useArgBlock.addAction(pushEnv);
				useArgBlock.addAction(new GetVariable());
				Push pushFormalArg = new Push();
				pushFormalArg.addValue(formalVal);
				pushFormalArg.addValue(argVal);
				useArgBlock.addAction(pushFormalArg);
				useArgBlock.addAction(new GetVariable());
				useArgBlock.addAction(pushI);
				useArgBlock.addAction(new GetMember());
				useArgBlock.addAction(new SetMember());
				
				useDefaultBlock.addAction(new Jump((short)useArgBlock.getSize()));
				
				func.addAction(new If((short)useDefaultBlock.getActions().getSize()));
				CodeGenUtil.copyTags(func.getBody(),useDefaultBlock.getActions());
				CodeGenUtil.copyTags(func.getBody(), useArgBlock);
			}else{
				break;
			}
		}
		
		//check for * formal
		if(i<numFormals && formalsList.get(i).isArray()){
			func.addAction(pushEnv);
			func.addAction(new GetVariable());
			Push pushFormalIndexArg = new Push();
			Push.StackValue formalVal = new Push.StackValue();
			formalVal.setString(formalsList.get(i).getName());
			pushFormalIndexArg.addValue(formalVal);
			Push.StackValue indexVal = new Push.StackValue();
			indexVal.setString("curr Index "+Global.getCounter());
			pushFormalIndexArg.addValue(indexVal);
			pushFormalIndexArg.addValue(argVal);
			func.addAction(pushFormalIndexArg);
			func.addAction(new GetVariable());
			Push pushLen = new Push();
			Push.StackValue lenVal = new Push.StackValue();
			lenVal.setString("length");
			pushLen.addValue(lenVal);
			func.addAction(pushLen);
			func.addAction(new GetMember());
			func.addAction(new Decrement());
			func.addAction(new DefineLocal());
			
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
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			push1.addValue(oneVal);
			bodyBlock.addAction(push1);
			bodyBlock.addAction(new Subtract());
			bodyBlock.addAction(new SetVariable());
			bodyBlock.addAction(new Jump((short)(-1*(exprBlock.getSize()+(new If((short)0)).getSize()+bodyBlock.getSize()+(new Jump((short)0)).getSize()))));
			
			CodeGenUtil.copyTags(func.getBody(),exprBlock);
			func.addAction(new If((short)bodyBlock.getSize()));
			CodeGenUtil.copyTags(func.getBody(),bodyBlock);
			
			func.addAction(pushArg);
			func.addAction(new GetVariable());
			func.addAction(pushLen);
			func.addAction(new GetMember());
			func.addAction(pushI);
			func.addAction(new Subtract());
			func.addAction(new InitArray());
			func.addAction(new SetMember());
			i++;
		}
		
		//assign closure to proc formal param
		if(i<numFormals && formalsList.get(i).isProc()){
			func.addAction(pushEnv);
			func.addAction(new GetVariable());
			Push pushFormal = new Push();
			Push.StackValue formalVal = new Push.StackValue();
			formalVal.setString(formalsList.get(i).getName());
			pushFormal.addValue(formalVal);
			func.addAction(pushFormal);
			Push push0ClosureStack = new Push();
			push0ClosureStack.addValue(zeroVal);
			Push.StackValue closureStackVal = new Push.StackValue();
			closureStackVal.setString("closure Stack");
			push0ClosureStack.addValue(closureStackVal);
			func.addAction(push0ClosureStack);
			func.addAction(new GetVariable());
			Push pushPeek = new Push();
			Push.StackValue peekVal = new Push.StackValue();
			peekVal.setString("peek");
			pushPeek.addValue(peekVal);
			func.addAction(pushPeek);
			func.addAction(new CallMethod());
			
			short regNo2 = ((Integer)st.get("RegisterNo")).shortValue();
			st.set("RegisterNo",new Integer(regNo2+1));
			func.addAction(new StoreRegister((short)regNo2));
			func.addAction(new SetMember());
			
			//assign closure to env
			func.addAction(pushEnv);
			func.addAction(new GetVariable());
			Push pushYieldReg = new Push();
			Push.StackValue yieldVal = new Push.StackValue();
			yieldVal.setString("yield");
			pushYieldReg.addValue(yieldVal);
			Push.StackValue regVal = new Push.StackValue();
			regVal.setRegisterNumber((short)regNo2);
			pushYieldReg.addValue(regVal);
			func.addAction(pushYieldReg);
			func.addAction(new SetMember());
			
			st.set("RegisterNo",new Integer(regNo2));
		}else{
			//assign closure to env
			func.addAction(pushEnv);
			func.addAction(new GetVariable());
			Push pushYield = new Push();
			Push.StackValue yieldVal = new Push.StackValue();
			yieldVal.setString("yield");
			pushYield.addValue(yieldVal);
			func.addAction(pushYield);

			Push push0ClosureStack = new Push();
			push0ClosureStack.addValue(zeroVal);
			Push.StackValue closureStackVal = new Push.StackValue();
			closureStackVal.setString("closure Stack");
			push0ClosureStack.addValue(closureStackVal);
			func.addAction(push0ClosureStack);
			func.addAction(new GetVariable());
			Push pushPeek = new Push();
			Push.StackValue peekVal = new Push.StackValue();
			peekVal.setString("peek");
			pushPeek.addValue(peekVal);
			func.addAction(pushPeek);
			func.addAction(new CallMethod());
			
			func.addAction(new SetMember());
		}
		
		
		//generate code from stmts
		if(stmts!=null){
			DoAction stmtsBlock = new DoAction();
			stmts.generateCode(stmtsBlock,st);
			
			java.util.List actions = stmtsBlock.getActions().getActions();
			for(int j=0;j<actions.size();j++){
				func.addAction((Action)actions.get(j));
			}
		}
		
		//generate return
		func.addAction(new Return());
		//assign func to receiver.methodName
		actionTag.addAction(func);
		
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		Push pushContextEnv = new Push();
		pushContextEnv.addValue(contextVal);
		pushContextEnv.addValue(envVal);
		actionTag.addAction(pushContextEnv);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushContext);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//set "method Name" to function object
		Push pushMethodNameName = new Push();
		Push.StackValue methodNameVal = new Push.StackValue();
		methodNameVal.setString("method Name");
		pushMethodNameName.addValue(methodNameVal);
		pushMethodNameName.addValue(nameVal);
		actionTag.addAction(pushMethodNameName);
		actionTag.addAction(new SetMember());
		
		//toString()
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushThis = new Push();
		pushThis.addValue(thisVal);
		toStringFunc.addAction(pushThis);
		toStringFunc.addAction(new GetVariable());
		Push pushMethodName = new Push();
		pushMethodName.addValue(methodNameVal);
		toStringFunc.addAction(pushMethodName);
		toStringFunc.addAction(new GetMember());
		toStringFunc.addAction(new Return());
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		actionTag.addAction(new SetMember());
		
		Push push0NilClass = new Push();
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilVal = new Push.StackValue();
		nilVal.setString("NilClass");
		push0NilClass.addValue(nilVal);
		actionTag.addAction(push0NilClass);
		actionTag.addAction(new NewObject());
	}
}
