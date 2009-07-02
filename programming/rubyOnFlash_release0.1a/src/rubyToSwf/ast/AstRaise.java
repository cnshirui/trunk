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

import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;

/**
 * Represents a raise statement
 * @author Hongjian
 *
 */
public class AstRaise implements IAstStatement {

	private int line;
	private boolean lastFlag;
	private IAstExpression expr;
	
	
	public AstRaise(IAstExpression expr,int line){
		this.expr = expr;
		this.line = line;
		this.lastFlag = false;
	}
	public void setLast() {
		lastFlag = true;
	}

	public void generateCode(DoAction actionTag, SymbolTable st) {
		Push pushStage = new Push();
		Push.StackValue stageVal = new Push.StackValue();
		stageVal.setString("Stage");
		pushStage.addValue(stageVal);
		actionTag.addAction(pushStage);
		actionTag.addAction(new GetVariable());
		Push pushHeight = new Push();
		Push.StackValue heightVal = new Push.StackValue();
		heightVal.setString("height");
		pushHeight.addValue(heightVal);
		actionTag.addAction(pushHeight);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushStage);
		actionTag.addAction(new GetVariable());
		Push pushWidth = new Push();
		Push.StackValue widthVal = new Push.StackValue();
		widthVal.setString("width");
		pushWidth.addValue(widthVal);
		actionTag.addAction(pushWidth);
		actionTag.addAction(new GetMember());
		Push push000Root = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push000Root.addValue(zeroVal);
		push000Root.addValue(zeroVal);
		push000Root.addValue(zeroVal);
		Push.StackValue rootVal = new Push.StackValue();
		rootVal.setString("_root");
		push000Root.addValue(rootVal);
		actionTag.addAction(push000Root);
		actionTag.addAction(new GetVariable());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("getNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		actionTag.addAction(pushGetNextHighestDepth);
		actionTag.addAction(new CallMethod());
		Push pushConsole6Root = new Push();
		Push.StackValue consoleVal = new Push.StackValue();
		consoleVal.setString("console Txt");
		pushConsole6Root.addValue(consoleVal);
		Push.StackValue sixVal = new Push.StackValue();
		sixVal.setInteger(6);
		pushConsole6Root.addValue(sixVal);
		pushConsole6Root.addValue(rootVal);
		actionTag.addAction(pushConsole6Root);
		actionTag.addAction(new GetVariable());
		Push pushCreateTextField = new Push();
		Push.StackValue createTextFieldVal = new Push.StackValue();
		createTextFieldVal.setString("createTextField");
		pushCreateTextField.addValue(createTextFieldVal);
		actionTag.addAction(pushCreateTextField);
		actionTag.addAction(new CallMethod());
		actionTag.addAction(new Pop());
		Push pushConsole = new Push();
		pushConsole.addValue(consoleVal);
		actionTag.addAction(pushConsole);
		actionTag.addAction(new GetVariable());
		Push pushText = new Push();
		Push.StackValue textVal = new Push.StackValue();
		textVal.setString("text");
		pushText.addValue(textVal);
		actionTag.addAction(pushText);
		
		if(expr==null){
			Push pushErrorMsg = new Push();
			Push.StackValue errorMsgVal = new Push.StackValue();
			errorMsgVal.setString("An exception is thrown");
			pushErrorMsg.addValue(errorMsgVal);
			actionTag.addAction(pushErrorMsg);
		}else{
			Push push0 = new Push();
			push0.addValue(zeroVal);
			actionTag.addAction(push0);
			expr.generateCode(actionTag, st);
			Push pushToString = new Push();
			Push.StackValue toStringVal = new Push.StackValue();
			toStringVal.setString("toString");
			pushToString.addValue(toStringVal);
			actionTag.addAction(pushToString);
			actionTag.addAction(new CallMethod());
		}
		
		actionTag.addAction(new SetMember());
		actionTag.addAction(pushConsole);
		actionTag.addAction(new GetVariable());
		Push pushBackgroundTrue = new Push();
		Push.StackValue backgroundVal = new Push.StackValue();
		backgroundVal.setString("background");
		pushBackgroundTrue.addValue(backgroundVal);
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setBoolean(true);
		pushBackgroundTrue.addValue(trueVal);
		actionTag.addAction(pushBackgroundTrue);
		actionTag.addAction(new SetMember());
		
		Push pushEmpty = new Push();
		Push.StackValue emptyVal = new Push.StackValue();
		emptyVal.setString("");
		pushEmpty.addValue(emptyVal);
		actionTag.addAction(pushEmpty);
		actionTag.addAction(new Throw());
	}

	public int getLine() {
		return line;
	}

	public int getType() {
		return IAstNode.RAISE;
	}

}
