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
import rubyToSwf.util.*;
import rubyToSwf.common.*;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import com.jswiff.swfrecords.*;

import java.io.*;

public class AstDefined implements IAstExpression {

	private int line;
	private IAstExpression expr;
	
	public AstDefined(IAstExpression expr, int line){
		this.expr = expr;
		this.line = line;
	}
	public int getLine() {
		// TODO Auto-generated method stub
		return line;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return IAstNode.DEFINED;
	}
	
	public String toString(){
		String res = "Return";
		if(expr!=null){
			res += " "+expr.toString();
		}
		return res;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		if(expr instanceof AstYield){
			Push pushEnv = new Push();
			Push.StackValue envVal = new Push.StackValue();
			envVal.setString("current Env");
			pushEnv.addValue(envVal);
			actionTag.addAction(pushEnv);
			actionTag.addAction(new GetVariable());
			Push pushYield = new Push();
			Push.StackValue yieldVal = new Push.StackValue();
			yieldVal.setString("yield");
			pushYield.addValue(yieldVal);
			actionTag.addAction(pushYield);
			actionTag.addAction(new GetMember());
			
			Push pushNull = new Push();
			Push.StackValue nullVal = new Push.StackValue();
			nullVal.setNull();
			pushNull.addValue(nullVal);
			actionTag.addAction(pushNull);
			actionTag.addAction(new Equals2());
			
			ActionBlock notNullBlock = new ActionBlock();
			Push pushMsg1 = new Push();
			Push.StackValue msgVal = new Push.StackValue();
			msgVal.setString("yield");
			pushMsg1.addValue(msgVal);
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			pushMsg1.addValue(oneVal);
			notNullBlock.addAction(pushMsg1);
			Push pushObject = new Push();
			Push.StackValue objectVal = new Push.StackValue();
			objectVal.setString("Object");
			pushObject.addValue(objectVal);
			notNullBlock.addAction(pushObject);
			notNullBlock.addAction(new GetVariable());
			Push pushString = new Push();
			Push.StackValue stringVal = new Push.StackValue();
			stringVal.setString("String");
			pushString.addValue(stringVal);
			notNullBlock.addAction(pushString);
			notNullBlock.addAction(new GetMember());
			Push pushUndefined = new Push();
			Push.StackValue undefinedVal = new Push.StackValue();
			undefinedVal.setUndefined();
			pushUndefined.addValue(undefinedVal);
			notNullBlock.addAction(pushUndefined);
			notNullBlock.addAction(new NewMethod());
			
			ActionBlock nullBlock = new ActionBlock();
			Push push0NilClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0NilClass.addValue(zeroVal);
			Push.StackValue nilClassVal = new Push.StackValue();
			nilClassVal.setString("NilClass");
			push0NilClass.addValue(nilClassVal);
			nullBlock.addAction(push0NilClass);
			nullBlock.addAction(new NewObject());
			
			notNullBlock.addAction(new Jump((short)nullBlock.getSize()));
			
			actionTag.addAction(new If((short)notNullBlock.getSize()));
			
			CodeGenUtil.copyTags(actionTag,notNullBlock);
			
			CodeGenUtil.copyTags(actionTag,nullBlock);
			
		}else if(expr instanceof AstSuper){
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
			actionTag.addAction(new GetMember());
			
			Push pushNull = new Push();
			Push.StackValue nullVal = new Push.StackValue();
			nullVal.setNull();
			pushNull.addValue(nullVal);
			actionTag.addAction(pushNull);
			actionTag.addAction(new Equals2());
			
			ActionBlock notNullBlock = new ActionBlock();
			Push pushMsg1 = new Push();
			Push.StackValue msgVal = new Push.StackValue();
			msgVal.setString("super");
			pushMsg1.addValue(msgVal);
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			pushMsg1.addValue(oneVal);
			notNullBlock.addAction(pushMsg1);
			Push pushObject = new Push();
			Push.StackValue objectVal = new Push.StackValue();
			objectVal.setString("Object");
			pushObject.addValue(objectVal);
			notNullBlock.addAction(pushObject);
			notNullBlock.addAction(new GetVariable());
			Push pushString = new Push();
			Push.StackValue stringVal = new Push.StackValue();
			stringVal.setString("String");
			pushString.addValue(stringVal);
			notNullBlock.addAction(pushString);
			notNullBlock.addAction(new GetMember());
			Push pushUndefined = new Push();
			Push.StackValue undefinedVal = new Push.StackValue();
			undefinedVal.setUndefined();
			pushUndefined.addValue(undefinedVal);
			notNullBlock.addAction(pushUndefined);
			notNullBlock.addAction(new NewMethod());
			
			ActionBlock nullBlock = new ActionBlock();
			Push push0NilClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0NilClass.addValue(zeroVal);
			Push.StackValue nilClassVal = new Push.StackValue();
			nilClassVal.setString("NilClass");
			push0NilClass.addValue(nilClassVal);
			nullBlock.addAction(push0NilClass);
			nullBlock.addAction(new NewObject());
			
			notNullBlock.addAction(new Jump((short)nullBlock.getSize()));
			
			actionTag.addAction(new If((short)notNullBlock.getSize()));
			
			CodeGenUtil.copyTags(actionTag,notNullBlock);
			
			CodeGenUtil.copyTags(actionTag,nullBlock);
			
		//}else if(expr instanceof AstVariableGlobal || expr instanceof AstVariableInstance || expr instanceof AstVariableLocal || expr instanceof AstVariableClass || expr instanceof AstConstant){
			//expr.generateCode(actionTag,st);
		}else{
			expr.generateCode(actionTag,st);
			
			Push pushNull = new Push();
			Push.StackValue nullVal = new Push.StackValue();
			nullVal.setNull();
			pushNull.addValue(nullVal);
			actionTag.addAction(pushNull);
			actionTag.addAction(new Equals2());
			
			ActionBlock notNullBlock = new ActionBlock();
			Push pushMsg1 = new Push();
			Push.StackValue msgVal = new Push.StackValue();
			
			//set the description according to what type of variable this is
			if(expr instanceof AstVariableGlobal){
				msgVal.setString("global-variable");
			}else if(expr instanceof AstVariableLocal){
				msgVal.setString("local-variable");
			}else if(expr instanceof AstVariableClass){
				msgVal.setString("class-variable");
			}else if(expr instanceof AstVariableInstance){
				msgVal.setString("instance-variable");
			}else if(expr instanceof AstConstant){
				msgVal.setString("constant");
			}else if(expr instanceof AstDereference){
				msgVal.setString("attribute");
			}else{
				msgVal.setString("expression");
			}
			
			pushMsg1.addValue(msgVal);
			Push.StackValue oneVal = new Push.StackValue();
			oneVal.setInteger(1);
			pushMsg1.addValue(oneVal);
			notNullBlock.addAction(pushMsg1);
			Push pushObject = new Push();
			Push.StackValue objectVal = new Push.StackValue();
			objectVal.setString("Object");
			pushObject.addValue(objectVal);
			notNullBlock.addAction(pushObject);
			notNullBlock.addAction(new GetVariable());
			Push pushString = new Push();
			Push.StackValue stringVal = new Push.StackValue();
			stringVal.setString("String");
			pushString.addValue(stringVal);
			notNullBlock.addAction(pushString);
			notNullBlock.addAction(new GetMember());
			Push pushUndefined = new Push();
			Push.StackValue undefinedVal = new Push.StackValue();
			undefinedVal.setUndefined();
			pushUndefined.addValue(undefinedVal);
			notNullBlock.addAction(pushUndefined);
			notNullBlock.addAction(new NewMethod());
			
			ActionBlock nullBlock = new ActionBlock();
			Push push0NilClass = new Push();
			Push.StackValue zeroVal = new Push.StackValue();
			zeroVal.setInteger(0);
			push0NilClass.addValue(zeroVal);
			Push.StackValue nilClassVal = new Push.StackValue();
			nilClassVal.setString("NilClass");
			push0NilClass.addValue(nilClassVal);
			nullBlock.addAction(push0NilClass);
			nullBlock.addAction(new NewObject());
			
			notNullBlock.addAction(new Jump((short)nullBlock.getSize()));
			
			actionTag.addAction(new If((short)notNullBlock.getSize()));
			
			CodeGenUtil.copyTags(actionTag,notNullBlock);
			
			CodeGenUtil.copyTags(actionTag,nullBlock);
			/*
			if(Global.verbose){
				System.out.println("AstDefined::generateCode: Invalid arg to defined? line: "+line);
			}
			throw new RuntimeException("Error: Invalid arg to defined? line: "+line);
			*/
		}
	}
}
