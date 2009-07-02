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
import com.jswiff.swfrecords.*;

import java.io.*;
import java.util.Vector;

public class AstYield implements IAstExpression {

	private int line;
	private Vector<AstFunctionCall.Argument>argList;
	
	public AstYield(Vector<AstFunctionCall.Argument>args, int line){
		this.argList = args;
		this.line = line;
	}
	
	public void setArgList(Vector<AstFunctionCall.Argument>args){
		this.argList = args;
	}
	
	public Vector<AstFunctionCall.Argument> getArgList(){
		return this.argList;
	}
	
	public int getType(){
		return IAstNode.YIELD;
	}
	
	/**
	* Returns the line number
	*/
	public int getLine(){
		return this.line;
	}
	
	public void generateCode(DoAction actionTag,SymbolTable st){
		int numArg = 0;
		if(argList!=null){
			numArg = argList.size();
		}
		
		//dummyArray = new Array(...);
		Push pushDummyArrayName = new Push();
		Push.StackValue dummyArrayNameVal = new Push.StackValue();
		dummyArrayNameVal.setString("dummyArray "+Global.getCounter());
		pushDummyArrayName.addValue(dummyArrayNameVal);
		actionTag.addAction(pushDummyArrayName);
		
		//create a dummy array object, then invert
		for(int i=0;i<numArg;i++){
			argList.get(i).generateCode(actionTag,st);
		}
		
		//generate ArgCount
		this.generateArgCount(actionTag, st);
		
		actionTag.addAction(new InitArray());
		
		//dummyArray = new Array(...);
		actionTag.addAction(new DefineLocal());
		
		//push the elements in reverse order
		/**
		 * var index = 0;
		 * while(index < argArray.length){
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
		
		this.generateArgCount(actionTag, st);
		//actionTag.addAction(new Increment());
		
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
		Push pushCall = new Push();
		Push.StackValue callVal = new Push.StackValue();
		callVal.setString("call");
		pushCall.addValue(callVal);
		actionTag.addAction(pushCall);
		
		actionTag.addAction(new CallMethod());
		
		
		//check return value.  If "Block Break" class returned, then exit the entire method
		actionTag.addAction(new PushDuplicate());
		Push pushBlockBreak = new Push();
		Push.StackValue blockBreakVal = new Push.StackValue();
		blockBreakVal.setString("Block Break");
		pushBlockBreak.addValue(blockBreakVal);
		actionTag.addAction(pushBlockBreak);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new Equals2());
		actionTag.addAction(new Not());
		
		//return nil
		Push push0NilClass = new Push();
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		
		NewObject newObjAction = new NewObject();
		
		Return returnAction = new Return();
		
		actionTag.addAction(new If((short)(push0NilClass.getSize()+newObjAction.getSize()+returnAction.getSize())));
		actionTag.addAction(push0NilClass);
		actionTag.addAction(newObjAction);
		actionTag.addAction(returnAction);
		
	}
	
	private void generateArgCount(DoAction actionTag, SymbolTable st){
		if(argList == null){
			Push pushArgCount = new Push();
			Push.StackValue argCountVal = new Push.StackValue();
			argCountVal.setInteger(0);
			pushArgCount.addValue(argCountVal);
			actionTag.addAction(pushArgCount);
		}else{
			Push pushArgCount = new Push();
			Push.StackValue argCountVal = new Push.StackValue();
			argCountVal.setInteger(argList.size());
			pushArgCount.addValue(argCountVal);
			actionTag.addAction(pushArgCount);
			
			if(argList.size()>0 && argList.get(argList.size()-1).isArray()){
				argList.get(argList.size()-1).generateCode(actionTag, st);
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
