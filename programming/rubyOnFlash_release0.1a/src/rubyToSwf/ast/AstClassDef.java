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

import java.util.Vector;

public class AstClassDef implements IAstExpression {
	private String className;
	private AstStatements stmts;
	private IAstExpression base;
	private int line;
	
	public AstClassDef(String className,IAstExpression base, AstStatements stmts, int line){
		this.base = base;
		this.className = className;
		this.stmts = stmts;
		this.line = line;
	}
	public int getLine() {
		return line;
	}

	public int getType() {
		return IAstNode.CLASS_DEF;
	}

	public String getClassName(){
		return className;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public IAstExpression getBase(){
		return base;
	}
	
	public void setBase(IAstExpression base){
		this.base = base;
	}
	
	public AstStatements getStmts(){
		return this.stmts;
	}
	
	public void setStmts(AstStatements stmts){
		this.stmts = stmts;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		
		short regNo = ((Integer)st.get("RegisterNo")).shortValue();
		st.set("RegisterNo",new Integer(regNo+1));
		
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
		Push pushClassName = new Push();
		Push.StackValue classNameVal = new Push.StackValue();
		classNameVal.setString(className);
		pushClassName.addValue(classNameVal);
		actionTag.addAction(pushClassName);
		actionTag.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		actionTag.addAction(pushNull);
		actionTag.addAction(new Equals2());
		
		
		ActionBlock classExistsBlock = new ActionBlock();
		classExistsBlock.addAction(pushEnv);
		classExistsBlock.addAction(new GetVariable());
		classExistsBlock.addAction(pushContext);
		classExistsBlock.addAction(new GetMember());
		classExistsBlock.addAction(pushClassName);
		classExistsBlock.addAction(new GetMember());
		
		if(base!=null){
			classExistsBlock.addAction(new PushDuplicate());

			Push pushProto = new Push();
			Push.StackValue protoVal = new Push.StackValue();
			protoVal.setString("__proto__");
			pushProto.addValue(protoVal);
			classExistsBlock.addAction(pushProto);
			classExistsBlock.addAction(new GetMember());
			DoAction tempAction = new DoAction();
			base.generateCode(tempAction,st);
			rubyToSwf.util.CodeGenUtil.copyTags(classExistsBlock,tempAction.getActions());
			
			classExistsBlock.addAction(new Equals2());
			
			Push pushErr = new Push();
			Push.StackValue errVal = new Push.StackValue();
			errVal.setString("Base name different from original definition.  Line: "+base.getLine());
			pushErr.addValue(errVal);
			
			Throw asThrow = new Throw();
			
			classExistsBlock.addAction(new If((short)(asThrow.getSize()+pushErr.getSize())));
			classExistsBlock.addAction(pushErr);
			classExistsBlock.addAction(asThrow);
		
		}
		classExistsBlock.addAction(new StoreRegister((short)regNo));
		classExistsBlock.addAction(new Pop());
		
		ActionBlock classNotExistsBlock = new ActionBlock();
		classNotExistsBlock.addAction(pushEnv);
		classNotExistsBlock.addAction(new GetVariable());
		classNotExistsBlock.addAction(pushContext);
		classNotExistsBlock.addAction(new GetMember());
		classNotExistsBlock.addAction(pushClassName);
		
		//define the actionscript constructor for this class
		DefineFunction2 asConsFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		asConsFunc.addAction(pushThis);
		asConsFunc.addAction(new GetVariable());
		Push pushConstructorArguments = new Push();
		Push.StackValue consVal = new Push.StackValue();
		consVal.setString("constructor");
		pushConstructorArguments.addValue(consVal);
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushConstructorArguments.addValue(argVal);
		asConsFunc.addAction(pushConstructorArguments);
		asConsFunc.addAction(new GetVariable());
		Push pushCallee = new Push();
		Push.StackValue calleeVal = new Push.StackValue();
		calleeVal.setString("callee");
		pushCallee.addValue(calleeVal);
		asConsFunc.addAction(pushCallee);
		asConsFunc.addAction(new GetMember());
		asConsFunc.addAction(new SetMember());
		
		classNotExistsBlock.addAction(asConsFunc);
		
		classNotExistsBlock.addAction(new PushDuplicate());
		Push pushConstructorClass = new Push();
		pushConstructorClass.addValue(consVal);
		Push.StackValue classVal = new Push.StackValue();
		classVal.setString("Class");
		pushConstructorClass.addValue(classVal);
		classNotExistsBlock.addAction(pushConstructorClass);
		classNotExistsBlock.addAction(new GetVariable());
		classNotExistsBlock.addAction(new SetMember());
		
		classNotExistsBlock.addAction(new PushDuplicate());
		
		//set the "name" property
		classNotExistsBlock.addAction(new PushDuplicate());
		Push pushClassNamePropClassName = new Push();
		Push.StackValue classNamePropVal = new Push.StackValue();
		classNamePropVal.setString("name");
		pushClassNamePropClassName.addValue(classNamePropVal);
		pushClassNamePropClassName.addValue(classNameVal);
		classNotExistsBlock.addAction(pushClassNamePropClassName);
		classNotExistsBlock.addAction(new SetMember());
		
		//sets the inheritance path
		if(base!=null){
			//has a base class
			//classNotExistsBlock.addAction(new PushDuplicate());
			classNotExistsBlock.addAction(new PushDuplicate());
			Push pushPrototype0Obj = new Push();
			Push.StackValue prototypeVal = new Push.StackValue();
			prototypeVal.setString("prototype");
			pushPrototype0Obj.addValue(prototypeVal);
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			pushPrototype0Obj.addValue(zeroVal);
			Push.StackValue objVal = new Push.StackValue();
			objVal.setString("Object");
			pushPrototype0Obj.addValue(objVal);
			classNotExistsBlock.addAction(pushPrototype0Obj);
			classNotExistsBlock.addAction(new NewObject());
			classNotExistsBlock.addAction(new PushDuplicate());
			classNotExistsBlock.addAction(new PushDuplicate());
			Push pushCons = new Push();
			pushCons.addValue(consVal);
			
			classNotExistsBlock.addAction(pushCons);			
			
			DoAction tempAction = new DoAction();
			base.generateCode(tempAction,st);
			rubyToSwf.util.CodeGenUtil.copyTags(classNotExistsBlock,tempAction.getActions());
						
			classNotExistsBlock.addAction(new SetMember());
			Push pushProto = new Push();
			Push.StackValue protoVal = new Push.StackValue();
			protoVal.setString("__proto__");
			pushProto.addValue(protoVal);
			classNotExistsBlock.addAction(pushProto);
			
			rubyToSwf.util.CodeGenUtil.copyTags(classNotExistsBlock,tempAction.getActions());
			
			Push pushPrototype = new Push();
			pushPrototype.addValue(prototypeVal);
			classNotExistsBlock.addAction(pushPrototype);
			classNotExistsBlock.addAction(new GetMember());
			classNotExistsBlock.addAction(new SetMember());
			classNotExistsBlock.addAction(new SetMember());
			classNotExistsBlock.addAction(pushProto);

			rubyToSwf.util.CodeGenUtil.copyTags(classNotExistsBlock,tempAction.getActions());
			
			classNotExistsBlock.addAction(new SetMember());
		}else{
			//no base class
			//classNotExistsBlock.addAction(new PushDuplicate());
			classNotExistsBlock.addAction(new PushDuplicate());
			Push pushPrototype0Obj = new Push();

			Push.StackValue prototypeVal = new Push.StackValue();
			prototypeVal.setString("prototype");
			pushPrototype0Obj.addValue(prototypeVal);
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			pushPrototype0Obj.addValue(zeroVal);
			Push.StackValue objVal = new Push.StackValue();
			objVal.setString("Object");
			pushPrototype0Obj.addValue(objVal);
			classNotExistsBlock.addAction(pushPrototype0Obj);
			classNotExistsBlock.addAction(new NewObject());
			classNotExistsBlock.addAction(new SetMember());
			Push pushProtoObject = new Push();
			Push.StackValue protoVal = new Push.StackValue();
			protoVal.setString("__proto__");
			pushProtoObject.addValue(protoVal);
			Push.StackValue objectVal = new Push.StackValue();
			objectVal.setString("Object");
			pushProtoObject.addValue(objectVal);
			classNotExistsBlock.addAction(pushProtoObject);
			classNotExistsBlock.addAction(new GetVariable());
			classNotExistsBlock.addAction(new SetMember());
		}
		
		classNotExistsBlock.addAction(new StoreRegister(regNo));
		classNotExistsBlock.addAction(new SetMember());
		
		classExistsBlock.addAction(new Jump((short)classNotExistsBlock.getSize()));
		actionTag.addAction(new If((short)classExistsBlock.getSize()));
		
		rubyToSwf.util.CodeGenUtil.copyTags(actionTag,classExistsBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(actionTag,classNotExistsBlock);
		
		//set the current env
		Push pushEnv0Bind = new Push();
		pushEnv0Bind.addValue(envVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushEnv0Bind.addValue(zeroVal);
		Push.StackValue bindVal = new Push.StackValue();
		bindVal.setString("Binding");
		pushEnv0Bind.addValue(bindVal);
		actionTag.addAction(pushEnv0Bind);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		Push pushContextReg = new Push();
		pushContextReg.addValue(contextVal);
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushContextReg.addValue(regNoVal);
		actionTag.addAction(pushContextReg);
		actionTag.addAction(new SetMember());
		Push pushSelfReg = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelfReg.addValue(selfVal);
		pushSelfReg.addValue(regNoVal);
		actionTag.addAction(pushSelfReg);
		actionTag.addAction(new SetMember());
		Push pushPrevEnvEnv = new Push();
		Push.StackValue prevEnvVal = new Push.StackValue();
		prevEnvVal.setString("prev Env");
		pushPrevEnvEnv.addValue(prevEnvVal);
		pushPrevEnvEnv.addValue(envVal);
		actionTag.addAction(pushPrevEnvEnv);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetVariable());
				
		//eval statements

		stmts.generateCode(actionTag,st);
		
		//clean up
		Push pushEnvEnv = new Push();
		pushEnvEnv.addValue(envVal);
		pushEnvEnv.addValue(envVal);
		actionTag.addAction(pushEnvEnv);
		actionTag.addAction(new GetVariable());
		Push pushPrevEnv = new Push();
		pushPrevEnv.addValue(prevEnvVal);
		actionTag.addAction(pushPrevEnv);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetVariable());
		
		//returns the last statement executed as the eval statement
		
		st.set("RegisterNo",new Integer(regNo));
		
	}
}
