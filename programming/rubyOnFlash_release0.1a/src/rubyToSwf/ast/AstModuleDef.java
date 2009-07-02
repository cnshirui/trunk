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
import rubyToSwf.util.*;
import rubyToSwf.codegen.SymbolTable;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import java.io.*;
import com.jswiff.swfrecords.*;

import java.util.Vector;

public class AstModuleDef implements IAstExpression {
	private String moduleName;
	private AstStatements stmts;
	private int line;
	
	public AstModuleDef(String className, AstStatements stmts, int line){
		this.moduleName = className;
		this.stmts = stmts;
		this.line = line;
	}
	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return IAstNode.MODULE_DEF;
	}

	public String getModuleName(){
		return moduleName;
	}
	
	public void setModuleName(String moduleName){
		this.moduleName = moduleName;
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
		Push pushModName = new Push();
		Push.StackValue modNameVal = new Push.StackValue();
		modNameVal.setString(moduleName);
		pushModName.addValue(modNameVal);
		actionTag.addAction(pushModName);
		actionTag.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		actionTag.addAction(pushNull);
		actionTag.addAction(new Equals2());
		
		ActionBlock modNotExistsBlock = new ActionBlock();
		modNotExistsBlock.addAction(pushEnv);
		modNotExistsBlock.addAction(new GetVariable());
		modNotExistsBlock.addAction(pushContext);
		modNotExistsBlock.addAction(new GetMember());
		modNotExistsBlock.addAction(pushModName);
		DefineFunction2 modFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0NilClass = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		modFunc.addAction(push0NilClass);
		modFunc.addAction(new NewObject());
		modNotExistsBlock.addAction(modFunc);
		modNotExistsBlock.addAction(new PushDuplicate());
		modNotExistsBlock.addAction(new PushDuplicate());
		
		//set the name property
		modNotExistsBlock.addAction(new PushDuplicate());
		Push pushModuleNamePropModuleName = new Push();
		Push.StackValue moduleNamePropVal = new Push.StackValue();
		moduleNamePropVal.setString("name");
		pushModuleNamePropModuleName.addValue(moduleNamePropVal);
		pushModuleNamePropModuleName.addValue(modNameVal);
		modNotExistsBlock.addAction(pushModuleNamePropModuleName);
		modNotExistsBlock.addAction(new SetMember());
		
		
		Push pushProtoModule = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoModule.addValue(protoVal);
		Push.StackValue moduleVal = new Push.StackValue();
		moduleVal.setString("Module");
		pushProtoModule.addValue(moduleVal);
		modNotExistsBlock.addAction(pushProtoModule);
		modNotExistsBlock.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		//modNotExistsBlock.addAction(pushPrototype);
		//modNotExistsBlock.addAction(new GetMember());

		modNotExistsBlock.addAction(new SetMember());
		Push pushPrototype0Object = new Push();
		pushPrototype0Object.addValue(prototypeVal);
		pushPrototype0Object.addValue(zeroVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushPrototype0Object.addValue(objectVal);
		modNotExistsBlock.addAction(pushPrototype0Object);
		modNotExistsBlock.addAction(new NewObject());
		modNotExistsBlock.addAction(new PushDuplicate());
		Push pushProtoNull = new Push();
		pushProtoNull.addValue(protoVal);
		pushProtoNull.addValue(nullVal);
		modNotExistsBlock.addAction(pushProtoNull);
		modNotExistsBlock.addAction(new SetMember());
		modNotExistsBlock.addAction(new SetMember());
		modNotExistsBlock.addAction(new StoreRegister((short)regNo));
		modNotExistsBlock.addAction(new SetMember());
		/*
		modNotExistsBlock.addAction(pushEnv);
		modNotExistsBlock.addAction(new GetVariable());
		modNotExistsBlock.addAction(pushContext);
		modNotExistsBlock.addAction(new GetMember());
		modNotExistsBlock.addAction(pushModName);
		modNotExistsBlock.addAction(new GetMember());
		*/
		
		ActionBlock modExistsBlock = new ActionBlock();
		modExistsBlock.addAction(pushEnv);
		modExistsBlock.addAction(new GetVariable());
		modExistsBlock.addAction(pushContext);
		modExistsBlock.addAction(new GetMember());
		modExistsBlock.addAction(pushModName);
		modExistsBlock.addAction(new GetMember());
		modExistsBlock.addAction(new StoreRegister(regNo));
		modExistsBlock.addAction(new Pop());
		modExistsBlock.addAction(new Jump((short)modNotExistsBlock.getSize()));
		
		actionTag.addAction(new If((short)modExistsBlock.getSize()));
		CodeGenUtil.copyTags(actionTag, modExistsBlock);
		CodeGenUtil.copyTags(actionTag, modNotExistsBlock);
		
		Push pushEnv0Binding = new Push();
		pushEnv0Binding.addValue(envVal);
		pushEnv0Binding.addValue(zeroVal);
		Push.StackValue bindingVal = new Push.StackValue();
		bindingVal.setString("Binding");
		pushEnv0Binding.addValue(bindingVal);
		actionTag.addAction(pushEnv0Binding);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		Push pushContextRegNo = new Push();
		pushContextRegNo.addValue(contextVal);
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber((regNo));
		pushContextRegNo.addValue(regNoVal);
		actionTag.addAction(pushContextRegNo);
		actionTag.addAction(new SetMember());
		
		Push pushSelfRegNo = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelfRegNo.addValue(selfVal);
		pushSelfRegNo.addValue(regNoVal);
		actionTag.addAction(pushSelfRegNo);
		actionTag.addAction(new SetMember());
		
		st.set("RegisterNo",new Integer(regNo));
		
		Push pushPrevEnvEnv = new Push();
		Push.StackValue prevEnvVal = new Push.StackValue();
		prevEnvVal.setString("prev Env");
		pushPrevEnvEnv.addValue(prevEnvVal);
		pushPrevEnvEnv.addValue(envVal);
		actionTag.addAction(pushPrevEnvEnv);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetVariable());
		
		stmts.generateCode(actionTag, st);
		
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
	}
}
