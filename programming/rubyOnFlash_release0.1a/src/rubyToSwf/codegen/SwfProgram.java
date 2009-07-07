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

package rubyToSwf.codegen;

import rubyToSwf.common.Global;
import rubyToSwf.ast.*;
import rubyToSwf.*;
import rubyToSwf.parser.*;
import java.util.Vector;

import com.jswiff.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;
import com.jswiff.listeners.*;

import java.io.*;
import com.jswiff.swfrecords.*;

public class SwfProgram{
	private String filename;
	private String sourcePath;
	//private IAstNode ast;
	private Vector<IResourceFileItem> resourceFileItems;
	private Vector<String>libraries;
	private int width, height,frameRate,background;
	
	//TODO: provide the ability to alter other parameters such as stage size
	
	public SwfProgram(String filename,String sourcePath,Vector<IResourceFileItem> resourceFileItems,Vector<String>libraries,int width, int height, int frameRate,int background){
		if(filename==null || filename.trim().equals("")){
			if(Global.verbose){
				System.out.println("Error: SwfProgram.Constructor cannot accept a null or blank filename");
			}
			throw new RuntimeException("Error: SwfProgram.Constructor cannot accept a null or blank filename");
		}
		
		/*
		if(ast==null){
			if(Global.verbose){
				System.out.println("Error: SwfProgram.Constructor cannot accept a null AST");
			}
			throw new RuntimeException("Error: SwfProgram.Constructor cannot accept a null AST");
		}
		*/
		this.filename = filename;
		this.sourcePath = sourcePath;
		this.resourceFileItems = resourceFileItems;
		this.libraries = libraries;
		this.width = width;
		this.height = height;
		this.frameRate = frameRate;
		this.background = background;
	}
	
	public void generate(){
		// create a new SWF document
		SWFDocument document = new SWFDocument();
		document.setFrameRate((short)this.frameRate);
		document.setFrameSize(new Rect(0,width*20,0,height*20));
		document.setBackgroundColor(rubyToSwf.util.ImageProcessor.intToRgb(this.background));

	    SymbolTable st = new SymbolTable();
		st.set("RegisterNo",new Integer(0));
	    initSwf(document,st);
	    loadRuby(document);
	    linkLibraries(document);
		DoAction actionTag = new DoAction();		
		
		if(resourceFileItems != null){
			ExportAssets.ExportMapping[] exportMappings = new ExportAssets.ExportMapping[resourceFileItems.size()];
			for(int i=0;i<resourceFileItems.size();i++){
				exportMappings[i] = new ExportAssets.ExportMapping(resourceFileItems.get(i).generateTags(document),resourceFileItems.get(i).getID());
			}
			document.addTag(new ExportAssets(exportMappings));
		}
		
		rubyToSwf.parser.parser p = null;
		if(sourcePath.trim().length()==0){
			p = new rubyToSwf.parser.parser(new rubyToSwf.parser.Scanner());
			System.out.println("Using standard input...");
		}else{
			p = new rubyToSwf.parser.parser(new rubyToSwf.parser.Scanner(sourcePath));
		}
		
		try{
			IAstNode ast = (IAstNode)p.parse().value;
			ast.generateCode(actionTag,st);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		
		//lastly, show frame
		document.addTag(actionTag);
		document.addTag(new ShowFrame()); // show frame
		try{
			SWFWriter writer = new SWFWriter(document, new FileOutputStream(filename));
			writer.write();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void loadRuby(SWFDocument document){
		DoAction actionTag = new DoAction();
		SymbolTable st = new SymbolTable();
		st.set("RegisterNo",new Integer(0));
		try{
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Object.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Class.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/FalseClass.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/TrueClass.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/NilClass.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Numeric.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Integer.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Float.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/String.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Range.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Array.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Hash.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Proc.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Math.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Key.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Mouse.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/Sound.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/MovieClip.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
			((rubyToSwf.ast.AstStatements)(new parser(new Scanner("Ruby/TextField.rb"))).parse().value).generateCode(actionTag, st);
			actionTag.addAction(new Pop());
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		document.addTag(actionTag);
	}
	
	protected void linkLibraries(SWFDocument document){
		for(int i=0;i<libraries.size();i++){
			String filename = libraries.get(i);
			System.out.println(filename);
			try{
				FileInputStream fi = new FileInputStream(filename);
				SWFReader reader = new SWFReader(fi);
				SWFDocumentReader docReader = new SWFDocumentReader();
				reader.addListener(docReader);
				reader.read();
				SWFDocument swfDoc = docReader.getDocument();
				
				/*copy over the following tags:
				 * 	ExportAssets, ImportAssets, ImportAssets2, DoAction, DoInitAction, 
				 * 	DefineShape, DefineShape2, DefineShape3, DefineShape4, DefineBits,
				 * 	DefineBitsJPEG2, DefineBitsJPEG3, DefineBitsLossless, DefineBitsLossless2,
				 * 	DefineMorphShape, DefineMorphShape2, DefineFont, DefineFont2, DefineFont3,
				 *  DefineFontInfo, DefineFontInfo2, DefineFont2, DefineFont3, DefineFontAlignment,
				 *  DefineText, DefineText2, DefineEditText, DefineSound, DefineButton, 
				 *  DefineButton2, DefineButtonCXform, DefineButtonSound, DefineSprite, 
				 *  DefineVideoStream
				 *
				 *Basically copy over definition tags and actionTags, as well as any imports and exports
				*/
				java.util.List tags = swfDoc.getTags();
				for(int j=0;j<tags.size();j++){
					Object tag = tags.get(j);
					if(tag instanceof ExportAssets || tag instanceof ImportAssets || tag instanceof ImportAssets2 || tag instanceof DoAction || tag instanceof DoInitAction || tag instanceof DefineShape || tag instanceof DefineShape2 || tag instanceof DefineShape3 || tag instanceof DefineShape4 || tag instanceof DefineBits || tag instanceof DefineBitsJPEG2 ||tag instanceof DefineBitsJPEG3 || tag instanceof DefineBitsLossless || tag instanceof DefineBitsLossless2 || tag instanceof DefineMorphShape || tag instanceof DefineMorphShape2 || tag instanceof DefineFont || tag instanceof DefineFont2 || tag instanceof DefineFont3 || tag instanceof DefineFontInfo || tag instanceof DefineFontInfo2 || tag instanceof DefineFont2 || tag instanceof DefineFont3 || tag instanceof DefineFontAlignment || tag instanceof DefineText || tag instanceof DefineText2 || tag instanceof DefineEditText || tag instanceof DefineSound || tag instanceof DefineButton || tag instanceof DefineButton2 || tag instanceof DefineButtonCXform || tag instanceof DefineButtonSound || tag instanceof DefineSprite || tag instanceof DefineVideoStream){
						document.addTag((Tag)tag);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	protected void initSwf(SWFDocument document,SymbolTable st){
		DoAction actionTag = new DoAction();	
		
		generateClosureStack(actionTag,st);

		generateInitObj(actionTag, st);
		
		generateObjModClass(actionTag,st);
		generateInitModule(actionTag,st);
		generateInitClass(actionTag,st);
		
		generateTrueClass(actionTag,st);
		generateFalseClass(actionTag,st);
		generateNilClass(actionTag,st);
		
		generateEnv(actionTag,st);
		
		generateNumericClass(actionTag,st);
		generateIntegerClass(actionTag,st);
		generateFloatClass(actionTag,st);
		generateStringClass(actionTag,st);
		generateFunction(actionTag,st);
		
		generateRangeClass(actionTag,st);
		generateArrayClass(actionTag,st);
		generateHashClass(actionTag,st);
		generateProcClass(actionTag,st);
		
		//start of component model
		generateMovieClip(actionTag,st);
		generateSpriteClass(actionTag,st);
		generateImageSpriteClass(actionTag,st);
		generateSwfSpriteClass(actionTag,st);
		
		generateInitKeyMouse(actionTag,st);
		
		generateTextField(actionTag, st);
		generateInitSound(actionTag, st);
		document.addTag(actionTag);
	}
	
	protected void generateClosureStack(DoAction actionTag,SymbolTable st){
		DefineFunction2 closureStackClass = new DefineFunction2("Closure Stack Class",(short)0,new RegisterParam[0]);
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		closureStackClass.addAction(pushThis);
		closureStackClass.addAction(new GetVariable());
		Push pushLength0 = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength0.addValue(lengthVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushLength0.addValue(zeroVal);
		closureStackClass.addAction(pushLength0);
		closureStackClass.addAction(new SetMember());
		
		actionTag.addAction(closureStackClass);
		
		/*
		 * ClosureStackClass.prototype.push = function(value){
		 * 	this[this.length] = value;
		 * 	this.length = this.length + 1;
		 * 	return value;
		 * }
		 * }
		 */
		Push pushClosureStackClass = new Push();
		Push.StackValue closureStackClassVal = new Push.StackValue();
		closureStackClassVal.setString("Closure Stack Class");
		pushClosureStackClass.addValue(closureStackClassVal);
		actionTag.addAction(pushClosureStackClass);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		actionTag.addAction(pushPush);
		DefineFunction2 pushFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"value")});
		pushFunc.addAction(pushThis);
		pushFunc.addAction(new GetVariable());
		pushFunc.addAction(new PushDuplicate());
		Push pushLength = new Push();
		pushLength.addValue(lengthVal);
		pushFunc.addAction(pushLength);
		pushFunc.addAction(new GetMember());
		Push pushValue = new Push();
		Push.StackValue valueVal = new Push.StackValue();
		valueVal.setString("value");
		pushValue.addValue(valueVal);
		pushFunc.addAction(pushValue);
		pushFunc.addAction(new GetVariable());
		pushFunc.addAction(new SetMember());
		pushFunc.addAction(pushThis);
		pushFunc.addAction(new GetVariable());
		Push pushLengthThis = new Push();
		pushLengthThis.addValue(lengthVal);
		pushLengthThis.addValue(thisVal);
		pushFunc.addAction(pushLengthThis);
		pushFunc.addAction(new GetVariable());
		pushFunc.addAction(pushLength);
		pushFunc.addAction(new GetMember());
		pushFunc.addAction(new Increment());
		pushFunc.addAction(new SetMember());
		pushFunc.addAction(pushValue);
		pushFunc.addAction(new GetVariable());
		pushFunc.addAction(new Return());
		actionTag.addAction(pushFunc);
		actionTag.addAction(new SetMember());
		
		/*
		 * ClosureStackClass.prototype.pop = function(){
		 * 	this.length = this.length - 1;
		 * 	temp = this[this.length];
		 * 	this[this.length] = null;
		 * 	return temp;
		 * }
		 */
		actionTag.addAction(pushClosureStackClass);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		actionTag.addAction(pushPop);
		DefineFunction2 popFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		popFunc.addAction(pushThis);
		popFunc.addAction(new GetVariable());
		popFunc.addAction(pushLengthThis);
		popFunc.addAction(new GetVariable());
		popFunc.addAction(pushLength);
		popFunc.addAction(new GetMember());
		popFunc.addAction(new Decrement());
		popFunc.addAction(new SetMember());
		Push pushTempThis = new Push();
		Push.StackValue tempVal = new Push.StackValue();
		tempVal.setString("temp");
		pushTempThis.addValue(tempVal);
		pushTempThis.addValue(thisVal);
		popFunc.addAction(pushTempThis);
		popFunc.addAction(new GetVariable());
		popFunc.addAction(new PushDuplicate());
		popFunc.addAction(pushLength);
		popFunc.addAction(new GetMember());
		popFunc.addAction(new GetMember());
		popFunc.addAction(new DefineLocal());
		popFunc.addAction(pushThis);
		popFunc.addAction(new GetVariable());
		popFunc.addAction(new PushDuplicate());
		popFunc.addAction(pushLength);
		popFunc.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		popFunc.addAction(pushNull);
		popFunc.addAction(new SetMember());
		Push pushTemp = new Push();
		pushTemp.addValue(tempVal);
		popFunc.addAction(pushTemp);
		popFunc.addAction(new GetVariable());
		popFunc.addAction(new Return());
		actionTag.addAction(popFunc);
		actionTag.addAction(new SetMember());
		
		/*
		 * ClosureStackClass.prototype.peek = function(){
		 * 	return this[this.length-1];
		 * }
		 */
		actionTag.addAction(pushClosureStackClass);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushPeek = new Push();
		Push.StackValue peekVal = new Push.StackValue();
		peekVal.setString("peek");
		pushPeek.addValue(peekVal);
		actionTag.addAction(pushPeek);
		DefineFunction2 peekFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		peekFunc.addAction(pushThis);
		peekFunc.addAction(new GetVariable());
		peekFunc.addAction(new PushDuplicate());
		peekFunc.addAction(pushLength);
		peekFunc.addAction(new GetMember());
		peekFunc.addAction(new Decrement());
		peekFunc.addAction(new GetMember());
		peekFunc.addAction(new Return());
		actionTag.addAction(peekFunc);
		actionTag.addAction(new SetMember());
		
		//initialize stack frame
		/* Equivalent to the following Actionscript code
		 * closureStack = new ClosureStackClass();
		 * closureStack.push(null);
		*/
		Push pushClosureStack0ClosureStackClass = new Push();
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		pushClosureStack0ClosureStackClass.addValue(closureStackVal);
		pushClosureStack0ClosureStackClass.addValue(zeroVal);
		pushClosureStack0ClosureStackClass.addValue(closureStackClassVal);
		actionTag.addAction(pushClosureStack0ClosureStackClass);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetVariable());
		Push pushNull1ClosureStack = new Push();
		pushNull1ClosureStack.addValue(nullVal);
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		pushNull1ClosureStack.addValue(oneVal);
		pushNull1ClosureStack.addValue(closureStackVal);
		actionTag.addAction(pushNull1ClosureStack);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPush);
		actionTag.addAction(new CallMethod());
		actionTag.addAction(new Pop());
	}
	
	protected void generateInitObj(DoAction actionTag,SymbolTable st){
		//define to_s
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushToS = new Push();
		Push.StackValue toSVal = new Push.StackValue();
		toSVal.setString("to_s");
		pushToS.addValue(toSVal);
		actionTag.addAction(pushToS);
		DefineFunction2 toSFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		
		//push onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		toSFunc.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		toSFunc.addAction(push1ClosureStack);
		toSFunc.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		toSFunc.addAction(pushPush);
		toSFunc.addAction(new CallMethod());
		toSFunc.addAction(new Pop());
		
		Push push0This = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0This.addValue(zeroVal);
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		push0This.addValue(thisVal);
		toSFunc.addAction(push0This);
		toSFunc.addAction(new GetVariable());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		toSFunc.addAction(pushToString);
		toSFunc.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		toSFunc.addAction(push0ClosureStack);
		toSFunc.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		toSFunc.addAction(pushPop);
		toSFunc.addAction(new CallMethod());
		toSFunc.addAction(new Pop());
		
		Push push1Object = new Push();
		push1Object.addValue(oneVal);
		push1Object.addValue(objVal);
		toSFunc.addAction(push1Object);
		toSFunc.addAction(new GetVariable());
		Push pushString = new Push();
		Push.StackValue stringVal = new Push.StackValue();
		stringVal.setString("String");
		pushString.addValue(stringVal);
		toSFunc.addAction(pushString);
		toSFunc.addAction(new NewMethod());
		toSFunc.addAction(new Return());
		actionTag.addAction(toSFunc);
		actionTag.addAction(new SetMember());
		
		//==
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushEq = new Push();
		Push.StackValue eqVal = new Push.StackValue();
		eqVal.setString("==");
		pushEq.addValue(eqVal);
		actionTag.addAction(pushEq);
		DefineFunction2 eqFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"other")});
		//does a direct comparison, i.e. compares the 2 objects to check if they refer to the same obj
		Push pushThis = new Push();
		pushThis.addValue(thisVal);
		eqFunc.addAction(pushThis);
		eqFunc.addAction(new GetVariable());
		Push pushOther = new Push();
		Push.StackValue otherVal = new Push.StackValue();
		otherVal.setString("other");
		pushOther.addValue(otherVal);
		eqFunc.addAction(pushOther);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(new Equals2());
		ActionBlock falseBlock = new ActionBlock();
		Push push0FalseClass = new Push();
		push0FalseClass.addValue(zeroVal);
		Push.StackValue falseClassVal = new Push.StackValue();
		falseClassVal.setString("FalseClass");
		push0FalseClass.addValue(falseClassVal);
		falseBlock.addAction(push0FalseClass);
		falseBlock.addAction(new NewObject());
		falseBlock.addAction(new Return());
		eqFunc.addAction(new If((short)falseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(), falseBlock);
		Push push0TrueClass = new Push();
		push0TrueClass.addValue(zeroVal);
		Push.StackValue trueClassVal = new Push.StackValue();
		trueClassVal.setString("TrueClass");
		push0TrueClass.addValue(trueClassVal);
		eqFunc.addAction(push0TrueClass);
		eqFunc.addAction(new NewObject());
		eqFunc.addAction(new Return());
		actionTag.addAction(eqFunc);
		actionTag.addAction(new SetMember());
		
		//clone
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushClone = new Push();
		Push.StackValue cloneVal = new Push.StackValue();
		cloneVal.setString("clone");
		pushClone.addValue(cloneVal);
		actionTag.addAction(pushClone);
		DefineFunction2 cloneFunc = new DefineFunction2("",(short)2,new RegisterParam[0]);
		Push push0Object = new Push();
		push0Object.addValue(zeroVal);
		push0Object.addValue(objVal);
		cloneFunc.addAction(push0Object);
		cloneFunc.addAction(new NewObject());
		//we only use these registers
		short regNo0 = (short)0;//the temp object
		short regNo1 = (short)1;//an enumerated key
		cloneFunc.addAction(new StoreRegister(regNo0));
		cloneFunc.addAction(new Pop());
		cloneFunc.addAction(pushThis);
		cloneFunc.addAction(new GetVariable());
		cloneFunc.addAction(new Enumerate2());
		StoreRegister storeRegister1 = new StoreRegister(regNo1);
		cloneFunc.addAction(storeRegister1);
		cloneFunc.addAction(pushNull);
		cloneFunc.addAction(new Equals2());
		
		ActionBlock forBody = new ActionBlock();
		Push pushReg0Reg1This = new Push();
		Push.StackValue reg0Val = new Push.StackValue();
		reg0Val.setRegisterNumber(regNo0);
		pushReg0Reg1This.addValue(reg0Val);
		Push.StackValue reg1Val = new Push.StackValue();
		reg1Val.setRegisterNumber(regNo1);
		pushReg0Reg1This.addValue(reg1Val);
		pushReg0Reg1This.addValue(thisVal);
		forBody.addAction(pushReg0Reg1This);
		forBody.addAction(new GetVariable());
		Push pushReg1 = new Push();
		pushReg1.addValue(reg1Val);
		forBody.addAction(pushReg1);
		forBody.addAction(new GetMember());
		forBody.addAction(new SetMember());
		forBody.addAction(new Jump((short)(-(new Jump((short)0).getSize() + forBody.getSize() + storeRegister1.getSize()+pushNull.getSize() + new Equals2().getSize()+new If((short)-1).getSize()))));
		
		cloneFunc.addAction(new If((short)forBody.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(cloneFunc.getBody(), forBody);
		Push pushReg0 = new Push();
		pushReg0.addValue(reg0Val);
		cloneFunc.addAction(pushReg0);
		cloneFunc.addAction(new Return());
		
		actionTag.addAction(cloneFunc);
		actionTag.addAction(new SetMember());
		
		//!=
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNEq = new Push();
		Push.StackValue neqVal = new Push.StackValue();
		neqVal.setString("!=");
		pushNEq.addValue(neqVal);
		actionTag.addAction(pushNEq);
		DefineFunction2 neqFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"other")});
		
		neqFunc.addAction(pushOther);
		neqFunc.addAction(new GetVariable());
		Push push1This = new Push();
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		neqFunc.addAction(push1This);
		neqFunc.addAction(new GetVariable());
		neqFunc.addAction(pushEq);
		
		neqFunc.addAction(pushNull);	
		neqFunc.addAction(push1ClosureStack);
		neqFunc.addAction(new GetVariable());
		neqFunc.addAction(pushPush);
		neqFunc.addAction(new CallMethod());
		neqFunc.addAction(new Pop());
		
		neqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		neqFunc.addAction(push0ClosureStack);
		neqFunc.addAction(new GetVariable());
		neqFunc.addAction(pushPop);
		neqFunc.addAction(new CallMethod());
		neqFunc.addAction(new Pop());
		
		rubyToSwf.util.CodeGenUtil.genCheckNullNilFalse(neqFunc.getBody());
		neqFunc.addAction(new Not());
		
		neqFunc.addAction(new If((short)falseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(neqFunc.getBody(), falseBlock);
		neqFunc.addAction(push0TrueClass);
		neqFunc.addAction(new NewObject());
		neqFunc.addAction(new Return());
		
		neqFunc.addAction(new Return());
		actionTag.addAction(neqFunc);
		actionTag.addAction(new SetMember());
		
		//instance_of?
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushInstanceOf = new Push();
		Push.StackValue instanceOfVal = new Push.StackValue();
		instanceOfVal.setString("instance_of?");
		pushInstanceOf.addValue(instanceOfVal);
		actionTag.addAction(pushInstanceOf);
		DefineFunction2 kindOfFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"aClass")});
		kindOfFunc.addAction(pushThis);
		kindOfFunc.addAction(new GetVariable());
		Push pushConstructor = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructor.addValue(constructorVal);
		kindOfFunc.addAction(pushConstructor);
		kindOfFunc.addAction(new GetMember());
		Push pushAClass = new Push();
		Push.StackValue aClassVal = new Push.StackValue();
		aClassVal.setString("aClass");
		pushAClass.addValue(aClassVal);
		kindOfFunc.addAction(pushAClass);
		kindOfFunc.addAction(new GetVariable());
		kindOfFunc.addAction(new Equals2());
		kindOfFunc.addAction(new If((short)falseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(kindOfFunc.getBody(), falseBlock);
		kindOfFunc.addAction(push0TrueClass);
		kindOfFunc.addAction(new NewObject());
		kindOfFunc.addAction(new Return());
		actionTag.addAction(kindOfFunc);
		actionTag.addAction(new SetMember());
		
		//equal?
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushEquals = new Push();
		Push.StackValue equalVal = new Push.StackValue();
		equalVal.setString("equal?");
		pushEquals.addValue(equalVal);
		actionTag.addAction(pushEquals);
		DefineFunction2 equalsFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		equalsFunc.addAction(pushThis);
		equalsFunc.addAction(new GetVariable());
		equalsFunc.addAction(pushOther);
		equalsFunc.addAction(new GetVariable());
		equalsFunc.addAction(new Equals2());
		equalsFunc.addAction(new If((short)falseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(equalsFunc.getBody(), falseBlock);
		equalsFunc.addAction(push0TrueClass);
		equalsFunc.addAction(new NewObject());
		equalsFunc.addAction(new Return());
		actionTag.addAction(equalsFunc);
		actionTag.addAction(new SetMember());
		
		/*Object.prototype.initialize = function(){
		 *	return null;
		 *}
		 */
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		actionTag.addAction(pushInitialize);
		DefineFunction2 initFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		initFunc.addAction(pushNull);
		initFunc.addAction(new Return());
		actionTag.addAction(initFunc);
		actionTag.addAction(new SetMember());
		
		//Object.primitiveEqual?
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushPrimitiveEqual = new Push();
		Push.StackValue primitiveEqualVal = new Push.StackValue();
		primitiveEqualVal.setString("primitiveEqual?");
		pushPrimitiveEqual.addValue(primitiveEqualVal);
		actionTag.addAction(pushPrimitiveEqual);
		DefineFunction2 primitiveEqualsFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"val1"),new RegisterParam((short)0,"val2")});
		Push pushVal1 = new Push();
		Push.StackValue val1Val = new Push.StackValue();
		val1Val.setString("val1");
		pushVal1.addValue(val1Val);
		primitiveEqualsFunc.addAction(pushVal1);
		primitiveEqualsFunc.addAction(new GetVariable());
		Push pushVal2 = new Push();
		Push.StackValue val2Val = new Push.StackValue();
		val2Val.setString("val2");
		pushVal2.addValue(val2Val);
		primitiveEqualsFunc.addAction(pushVal2);
		primitiveEqualsFunc.addAction(new GetVariable());
		primitiveEqualsFunc.addAction(new Equals2());
		primitiveEqualsFunc.addAction(new If((short)falseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(primitiveEqualsFunc.getBody(), falseBlock);
		primitiveEqualsFunc.addAction(push0TrueClass);
		primitiveEqualsFunc.addAction(new NewObject());
		primitiveEqualsFunc.addAction(new Return());
		actionTag.addAction(primitiveEqualsFunc);
		actionTag.addAction(new SetMember());
		
		Push pushOldObj0Obj = new Push();
		Push.StackValue oldObjVal = new Push.StackValue();
		oldObjVal.setString("old Obj");
		pushOldObj0Obj.addValue(oldObjVal);
		pushOldObj0Obj.addValue(objVal);
		actionTag.addAction(pushOldObj0Obj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetVariable());
		DefineFunction2 objectClass = new DefineFunction2("Object",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		objectClass.addAction(pushThis);
		objectClass.addAction(new GetVariable());
		Push pushConstructorObject = new Push();
		pushConstructorObject.addValue(constructorVal);
		pushConstructorObject.addValue(objVal);
		objectClass.addAction(pushConstructorObject);
		objectClass.addAction(new GetVariable());
		objectClass.addAction(new SetMember());
		
		actionTag.addAction(objectClass);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushPrototypeOldObj = new Push();
		pushPrototypeOldObj.addValue(prototypeVal);
		pushPrototypeOldObj.addValue(oldObjVal);
		actionTag.addAction(pushPrototypeOldObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushObj);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//define Object.toObject(value)
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushToObject = new Push();
		Push.StackValue toObjectVal = new Push.StackValue();
		toObjectVal.setString("toObject");
		pushToObject.addValue(toObjectVal);
		actionTag.addAction(pushToObject);
		DefineFunction2 toObjectFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"value")});
		Push pushValue = new Push();
		Push.StackValue valueVal = new Push.StackValue();
		valueVal.setString("value");
		pushValue.addValue(valueVal);
		toObjectFunc.addAction(pushValue);
		toObjectFunc.addAction(new GetVariable());
		toObjectFunc.addAction(new PushDuplicate());
		toObjectFunc.addAction(new TypeOf());
		toObjectFunc.addAction(new PushDuplicate());
		Push pushNumeric = new Push();
		Push.StackValue numericVal = new Push.StackValue();
		numericVal.setString("numeric");
		pushNumeric.addValue(numericVal);
		toObjectFunc.addAction(pushNumeric);
		toObjectFunc.addAction(new Equals2());
		toObjectFunc.addAction(new Not());
		
		ActionBlock numericBlock = new ActionBlock();
		numericBlock.addAction(new Pop());
		Push push1Float = new Push();
		push1Float.addValue(oneVal);
		Push.StackValue floatVal = new Push.StackValue();
		floatVal.setString("Float");
		push1Float.addValue(floatVal);
		numericBlock.addAction(push1Float);
		numericBlock.addAction(new NewObject());
		numericBlock.addAction(new Return());
		
		toObjectFunc.addAction(new If((short)numericBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(toObjectFunc.getBody(), numericBlock);
		
		toObjectFunc.addAction(new PushDuplicate());
		toObjectFunc.addAction(pushString);
		toObjectFunc.addAction(new Equals2());
		toObjectFunc.addAction(new Not());
		
		ActionBlock stringBlock = new ActionBlock();
		stringBlock.addAction(new Pop());
		stringBlock.addAction(push1Object);
		stringBlock.addAction(new GetVariable());
		stringBlock.addAction(pushString);
		stringBlock.addAction(new NewMethod());
		stringBlock.addAction(new Return());
		
		toObjectFunc.addAction(new If((short)stringBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(toObjectFunc.getBody(), stringBlock);
		
		toObjectFunc.addAction(new PushDuplicate());
		Push pushNullString = new Push();
		Push.StackValue nullStringVal = new Push.StackValue();
		nullStringVal.setString("null");
		pushNullString.addValue(nullStringVal);
		toObjectFunc.addAction(pushNullString);
		toObjectFunc.addAction(new Equals2());
		toObjectFunc.addAction(new Not());
		
		ActionBlock nullBlock = new ActionBlock();
		nullBlock.addAction(new Pop());
		nullBlock.addAction(new Pop());
		Push push0NilClass = new Push();
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		nullBlock.addAction(push0NilClass);
		nullBlock.addAction(new NewObject());
		nullBlock.addAction(new Return());
		
		toObjectFunc.addAction(new If((short)nullBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(toObjectFunc.getBody(), nullBlock);
		
		
		toObjectFunc.addAction(new PushDuplicate());
		Push pushBoolean = new Push();
		Push.StackValue booleanVal = new Push.StackValue();
		booleanVal.setString("boolean");
		pushBoolean.addValue(booleanVal);
		toObjectFunc.addAction(pushBoolean);
		toObjectFunc.addAction(new Equals2());
		toObjectFunc.addAction(new Not());
		
		ActionBlock booleanBlock = new ActionBlock();
		booleanBlock.addAction(new Pop());
		ActionBlock booleanFalseBlock = new ActionBlock();
		booleanFalseBlock.addAction(push0FalseClass);
		booleanFalseBlock.addAction(new NewObject());
		booleanFalseBlock.addAction(new Return());
		booleanBlock.addAction(new If((short)booleanFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(booleanBlock, booleanFalseBlock);
		booleanBlock.addAction(push0TrueClass);
		booleanBlock.addAction(new NewObject());
		booleanBlock.addAction(new Return());
		
		toObjectFunc.addAction(new If((short)booleanBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(toObjectFunc.getBody(), booleanBlock);
		
		toObjectFunc.addAction(new Pop());
		toObjectFunc.addAction(new Return());
		
		actionTag.addAction(toObjectFunc);
		actionTag.addAction(new SetMember());
	}
	/**
	 * Defines the class Binding and its methods
	 * Creates an instance of Binding, and name it "current Env"
	 * @param actionTag The actionTag to append the actions to
	 */
	protected void generateEnv(DoAction actionTag,SymbolTable st){
		//Define an Binding class, essentially just an normal object, with customized toString, and
		//misc. methods
		DefineFunction2 bindingClass = new DefineFunction2("Binding",(short)0,new RegisterParam[0]);
		
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		bindingClass.addAction(pushThis);
		bindingClass.addAction(new GetVariable());
		Push pushTrue0TrueClass = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setString("true");
		pushTrue0TrueClass.addValue(trueVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushTrue0TrueClass.addValue(zeroVal);
		Push.StackValue trueClassVal = new Push.StackValue();
		trueClassVal.setString("TrueClass");
		pushTrue0TrueClass.addValue(trueClassVal);
		bindingClass.addAction(pushTrue0TrueClass);
		bindingClass.addAction(new NewObject());
		bindingClass.addAction(new SetMember());
		
		bindingClass.addAction(pushThis);
		bindingClass.addAction(new GetVariable());
		Push pushFalse0FalseClass = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setString("false");
		pushFalse0FalseClass.addValue(falseVal);
		pushFalse0FalseClass.addValue(zeroVal);
		Push.StackValue falseClassVal = new Push.StackValue();
		falseClassVal.setString("FalseClass");
		pushFalse0FalseClass.addValue(falseClassVal);
		bindingClass.addAction(pushFalse0FalseClass);
		bindingClass.addAction(new NewObject());
		bindingClass.addAction(new SetMember());
		
		bindingClass.addAction(pushThis);
		bindingClass.addAction(new GetVariable());
		Push pushNil0NilClass = new Push();
		Push.StackValue nilVal = new Push.StackValue();
		nilVal.setString("nil");
		pushNil0NilClass.addValue(nilVal);
		pushNil0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		pushNil0NilClass.addValue(nilClassVal);
		bindingClass.addAction(pushNil0NilClass);
		bindingClass.addAction(new NewObject());
		bindingClass.addAction(new SetMember());
		
		//set constructor attribute
		bindingClass.addAction(pushThis);
		bindingClass.addAction(new GetVariable());
		Push pushConstructorBinding = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorBinding.addValue(constructorVal);
		Push.StackValue bindingVal = new Push.StackValue();
		bindingVal.setString("Binding");
		pushConstructorBinding.addValue(bindingVal);
		bindingClass.addAction(pushConstructorBinding);
		bindingClass.addAction(new GetVariable());
		bindingClass.addAction(new SetMember());
		
		actionTag.addAction(bindingClass);
		
		Push pushBinding = new Push();
		pushBinding.addValue(bindingVal);
		actionTag.addAction(pushBinding);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//Binding.name = "Binding"
		Push pushNameBinding = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameBinding.addValue(nameVal);
		pushNameBinding.addValue(bindingVal);
		actionTag.addAction(pushNameBinding);
		actionTag.addAction(new SetMember());
		
		//push "Binding.prototype"
		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString()
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		toStringFunc.addAction(pushBinding);
		toStringFunc.addAction(new Return());
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//push "Binding.prototype"
		actionTag.addAction(pushBinding);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//define new method "clone"
		Push pushClone = new Push();
		Push.StackValue cloneVal = new Push.StackValue();
		cloneVal.setString("clone");
		pushClone.addValue(cloneVal);
		actionTag.addAction(pushClone);
		DefineFunction2 cloneFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushCopy0Binding = new Push();
		Push.StackValue copyVal = new Push.StackValue();
		copyVal.setString("copy");
		pushCopy0Binding.addValue(copyVal);
		pushCopy0Binding.addValue(zeroVal);
		pushCopy0Binding.addValue(bindingVal);
		cloneFunc.addAction(pushCopy0Binding);
		cloneFunc.addAction(new NewObject());
		cloneFunc.addAction(new DefineLocal());
		cloneFunc.addAction(pushThis);
		cloneFunc.addAction(new GetVariable());
		cloneFunc.addAction(new Enumerate2());
		
		ActionBlock enumBlock = new ActionBlock();
		short regNo = ((Integer)st.get("RegisterNo")).shortValue();
		st.set("RegisterNo",new Integer(regNo+1));
		enumBlock.addAction(new StoreRegister(regNo));
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		enumBlock.addAction(pushNull);
		enumBlock.addAction(new Equals2());
		
		ActionBlock bodyBlock = new ActionBlock();
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		Push pushCopyRegThisReg = new Push();
		pushCopyRegThisReg.addValue(copyVal);
		pushCopyRegThisReg.addValue(regNoVal);
		pushCopyRegThisReg.addValue(thisVal);
		pushCopyRegThisReg.addValue(regNoVal);
		bodyBlock.addAction(pushCopyRegThisReg);
		bodyBlock.addAction(new GetMember());
		bodyBlock.addAction(new SetMember());
		bodyBlock.addAction(new Jump((short)(-1*(bodyBlock.getSize()+enumBlock.getSize()+(new Jump((short)0)).getSize()+(new If((short)0)).getSize()))));
		//set the branch for enumBlock
		
		java.util.List actions = enumBlock.getActions();
		for(int i=0;i<actions.size();i++){
			cloneFunc.addAction((Action)actions.get(i));
		}
		cloneFunc.addAction(new If((short)bodyBlock.getSize()));
		actions = bodyBlock.getActions();
		for(int i=0;i<actions.size();i++){
			cloneFunc.addAction((Action)actions.get(i));
		}
		cloneFunc.addAction(new Return());
		actionTag.addAction(cloneFunc);
		actionTag.addAction(new SetMember());
		
		st.set("RegisterNo",new Integer(regNo));
		
		//create an instance of Binding
		Push push0Binding = new Push();
		Push.StackValue envName = new Push.StackValue();
		envName.setString("current Env");
		push0Binding.addValue(envName);
		push0Binding.addValue(zeroVal);
		push0Binding.addValue(bindingVal);
		actionTag.addAction(push0Binding);
		actionTag.addAction(new NewObject());
		//add env['self'] = this;
		actionTag.addAction(new PushDuplicate());
		Push pushSelfThis = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelfThis.addValue(selfVal);
		pushSelfThis.addValue(thisVal);
		actionTag.addAction(pushSelfThis);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		//set "current Env" <- binding instance
		actionTag.addAction(new SetVariable());
		//set env['class Context'] = Object
		Push pushEnv = new Push();
		pushEnv.addValue(envName);
		actionTag.addAction(pushEnv);
		actionTag.addAction(new GetVariable());
		Push pushContext = new Push();
		Push.StackValue contextVal = new Push.StackValue();
		contextVal.setString("class Context");
		pushContext.addValue(contextVal);
		actionTag.addAction(pushContext);
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//assign Binding.__proto__ = Object
		actionTag.addAction(pushBinding);
		actionTag.addAction(new GetVariable());
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * generates the "global" functions, such as findObj, functions defined here are utility functions
	 * used by the runtime, such as findObj.  Note that these function names have spaces in them, to avoid having name clashes with the
	 * user code.  
	 * @param actionTag the action tag to append to
	 * @param st the symboltable
	 */
	protected void generateFunction(DoAction actionTag, SymbolTable st){
		RegisterParam[] findObjParams = new RegisterParam[2];
		findObjParams[0] = new RegisterParam((short)0,"env");
		findObjParams[1] = new RegisterParam((short)0,"key");
		DefineFunction2 findObjFunc = new DefineFunction2("find Obj",(short)0,findObjParams);
		Push pushEnv = new Push();
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("env");
		pushEnv.addValue(envVal);
		findObjFunc.addAction(pushEnv);
		findObjFunc.addAction(new GetVariable());
		Push pushKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("key");
		pushKey.addValue(keyVal);
		findObjFunc.addAction(pushKey);
		findObjFunc.addAction(new GetVariable());
		findObjFunc.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		findObjFunc.addAction(pushNull);
		findObjFunc.addAction(new Equals2());
		
		ActionBlock ifBlock = new ActionBlock();
		ifBlock.addAction(pushKey);
		ifBlock.addAction(new GetVariable());
		ifBlock.addAction(pushEnv);
		ifBlock.addAction(new GetVariable());
		Push push2Recur = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2Recur.addValue(twoVal);
		Push.StackValue recurVal = new Push.StackValue();
		recurVal.setString("recur findObj");
		push2Recur.addValue(recurVal);
		ifBlock.addAction(push2Recur);
		ifBlock.addAction(new CallFunction());
		ifBlock.addAction(new Return());
		
		findObjFunc.addAction(new If((short)ifBlock.getSize()));
		
		java.util.List actions = ifBlock.getActions();
		for(int i=0;i<actions.size();i++){
			findObjFunc.addAction((Action)actions.get(i));
		}
		
		findObjFunc.addAction(pushEnv);
		findObjFunc.addAction(new GetVariable());
		findObjFunc.addAction(new Return());
		
		actionTag.addAction(findObjFunc);
		
		// function recur_findObj
		DefineFunction2 recurFindObjFunc = new DefineFunction2("recur findObj",(short)0,findObjParams);//same parameters as findObj
		Push pushTemp = new Push();
		Push.StackValue tempVal = new Push.StackValue();
		tempVal.setString("temp");
		pushTemp.addValue(tempVal);
		recurFindObjFunc.addAction(pushTemp);
		recurFindObjFunc.addAction(pushEnv);
		recurFindObjFunc.addAction(new GetVariable());
		Push pushProto = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProto.addValue(protoVal);
		recurFindObjFunc.addAction(pushProto);
		recurFindObjFunc.addAction(new GetMember());
		recurFindObjFunc.addAction(new DefineLocal());
		recurFindObjFunc.addAction(pushEnv);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(pushProto);
		recurFindObjFunc.addAction(pushNull);
		recurFindObjFunc.addAction(new SetMember());
		recurFindObjFunc.addAction(pushEnv);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(pushKey);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(new GetMember());
		recurFindObjFunc.addAction(pushNull);
		recurFindObjFunc.addAction(new Equals2());
		
		ifBlock = new ActionBlock();
		ifBlock.addAction(pushEnv);
		ifBlock.addAction(new GetVariable());
		ifBlock.addAction(pushProto);
		ifBlock.addAction(pushTemp);
		ifBlock.addAction(new GetVariable());
		ifBlock.addAction(new SetMember());
		ifBlock.addAction(pushEnv);
		ifBlock.addAction(new GetVariable());
		ifBlock.addAction(new Return());
		
		recurFindObjFunc.addAction(new If((short)ifBlock.getSize()));
		actions = ifBlock.getActions();
		for(int i=0;i<actions.size();i++){
			recurFindObjFunc.addAction((Action)actions.get(i));
		}
		
		recurFindObjFunc.addAction(pushEnv);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(pushProto);
		recurFindObjFunc.addAction(pushTemp);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(new SetMember());
		recurFindObjFunc.addAction(pushKey);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(pushTemp);
		recurFindObjFunc.addAction(new GetVariable());
		recurFindObjFunc.addAction(push2Recur);
		recurFindObjFunc.addAction(new CallFunction());
		recurFindObjFunc.addAction(new Return());
		
		actionTag.addAction(recurFindObjFunc);		
		
		//define a "Block Break" class, returned by the break statement when within a block
		DefineFunction2 blockBreakFunc = new DefineFunction2("Block Break",(short)0,new RegisterParam[0]);
		actionTag.addAction(blockBreakFunc);
	}
	
	protected void generateObjModClass(DoAction actionTag,SymbolTable st){
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		//Object.name = "Object"
		Push pushNameObject = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameObject.addValue(nameVal);
		pushNameObject.addValue(objVal);
		actionTag.addAction(pushNameObject);
		actionTag.addAction(new SetMember());
		
		Push pushMod = new Push();
		Push.StackValue modVal = new Push.StackValue();
		modVal.setString("Module");
		pushMod.addValue(modVal);
		actionTag.addAction(pushMod);
		DefineFunction2 moduleClass = new DefineFunction2("Module",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		moduleClass.addAction(pushThis);
		moduleClass.addAction(new GetVariable());
		Push pushConstructorModule = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorModule.addValue(constructorVal);
		pushConstructorModule.addValue(modVal);
		moduleClass.addAction(pushConstructorModule);
		moduleClass.addAction(new GetVariable());
		moduleClass.addAction(new SetMember());
		
		actionTag.addAction(moduleClass);
		actionTag.addAction(pushMod);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		//set the module Name property
		actionTag.addAction(new PushDuplicate());
		Push pushModuleNamePropModuleName = new Push();
		Push.StackValue moduleNamePropVal = new Push.StackValue();
		moduleNamePropVal.setString("name");
		pushModuleNamePropModuleName.addValue(moduleNamePropVal);
		pushModuleNamePropModuleName.addValue(modVal);
		actionTag.addAction(pushModuleNamePropModuleName);
		actionTag.addAction(new SetMember());
		
		Push pushPrototype0Obj = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0Obj.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0Obj.addValue(zeroVal);
		pushPrototype0Obj.addValue(objVal);
		actionTag.addAction(pushPrototype0Obj);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());//Module.prototype = new Object()
		Push pushProtoObj = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObj.addValue(protoVal);
		pushProtoObj.addValue(objVal);
		actionTag.addAction(pushProtoObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());//Module.__proto__ = Object
		actionTag.addAction(new SetMember());//Object["Module"] = Module
		
		Push pushClass = new Push();
		Push.StackValue classVal = new Push.StackValue();
		classVal.setString("Class");
		pushClass.addValue(classVal);
		actionTag.addAction(pushClass);
		DefineFunction2 classClass = new DefineFunction2("Class",(short)1,new RegisterParam[0]);//use 1 register
		
		//set constructor attribute
		classClass.addAction(pushThis);
		classClass.addAction(new GetVariable());
		Push pushConstructorClass = new Push();
		pushConstructorClass.addValue(constructorVal);
		pushConstructorClass.addValue(classVal);
		classClass.addAction(pushConstructorClass);
		classClass.addAction(new GetVariable());
		classClass.addAction(new SetMember());
		
		actionTag.addAction(classClass);
		actionTag.addAction(pushClass);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		//set the class Name property
		actionTag.addAction(new PushDuplicate());
		Push pushClassNamePropClassName = new Push();
		Push.StackValue classNamePropVal = new Push.StackValue();
		classNamePropVal.setString("name");
		pushClassNamePropClassName.addValue(classNamePropVal);
		pushClassNamePropClassName.addValue(classVal);
		actionTag.addAction(pushClassNamePropClassName);
		actionTag.addAction(new SetMember());
		
		Push pushPrototype0Mod = new Push();
		pushPrototype0Mod.addValue(prototypeVal);
		pushPrototype0Mod.addValue(zeroVal);
		pushPrototype0Mod.addValue(modVal);
		actionTag.addAction(pushPrototype0Mod);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());//Class.prototype = new Module();
		Push pushProtoMod = new Push();
		pushProtoMod.addValue(protoVal);
		pushProtoMod.addValue(modVal);
		actionTag.addAction(pushProtoMod);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());//Class.__proto__ = Module
		actionTag.addAction(new SetMember());//Object["Class"] = Class
		
		Push pushProto0Class = new Push();
		pushProto0Class.addValue(protoVal);
		pushProto0Class.addValue(zeroVal);
		pushProto0Class.addValue(classVal);
		actionTag.addAction(pushProto0Class);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new PushDuplicate());
		Push pushNew = new Push();
		Push.StackValue newVal = new Push.StackValue();
		newVal.setString("new");
		pushNew.addValue(newVal);
		actionTag.addAction(pushNew);
		
		DefineFunction2 newFunc = new DefineFunction2("",(short)1,new RegisterParam[0]);
		
		Push pushIArg = new Push();
		Push.StackValue iVal = new Push.StackValue();
		iVal.setString("i "+Global.getCounter());
		pushIArg.addValue(iVal);
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushIArg.addValue(argVal);
		newFunc.addAction(pushIArg);
		newFunc.addAction(new GetVariable());
		Push pushLen = new Push();
		Push.StackValue lenVal = new Push.StackValue();
		lenVal.setString("length");
		pushLen.addValue(lenVal);
		newFunc.addAction(pushLen);
		newFunc.addAction(new GetMember());
		newFunc.addAction(new Decrement());
		newFunc.addAction(new DefineLocal());
		
		ActionBlock loopExprBlock = new ActionBlock();
		Push pushI = new Push();
		pushI.addValue(iVal);
		loopExprBlock.addAction(pushI);
		loopExprBlock.addAction(new GetVariable());
		Push push0 = new Push();
		push0.addValue(zeroVal);
		loopExprBlock.addAction(push0);
		loopExprBlock.addAction(new Less2());
		
		ActionBlock loopBodyBlock = new ActionBlock();
		Push pushArg = new Push();
		pushArg.addValue(argVal);
		loopBodyBlock.addAction(pushArg);
		loopBodyBlock.addAction(new GetVariable());
		loopBodyBlock.addAction(pushI);
		loopBodyBlock.addAction(new GetVariable());
		loopBodyBlock.addAction(new GetMember());
		Push pushII = new Push();
		pushII.addValue(iVal);
		pushII.addValue(iVal);
		loopBodyBlock.addAction(pushII);
		loopBodyBlock.addAction(new GetVariable());
		loopBodyBlock.addAction(new Decrement());
		loopBodyBlock.addAction(new SetVariable());
		
		loopExprBlock.addAction(new If((short)(loopBodyBlock.getSize()+(new Jump((short)-1)).getSize())));
		loopBodyBlock.addAction(new Jump((short)(-1*(loopExprBlock.getSize()+loopBodyBlock.getSize()+(new Jump((short)-1)).getSize()))));
		
		rubyToSwf.util.CodeGenUtil.copyTags(newFunc.getBody(),loopExprBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(newFunc.getBody(),loopBodyBlock);
		
		newFunc.addAction(pushArg);
		newFunc.addAction(new GetVariable());
		newFunc.addAction(pushLen);
		newFunc.addAction(new GetMember());
		
		newFunc.addAction(pushThis);
		newFunc.addAction(new GetVariable());
		Push pushEmpty = new Push();
		Push.StackValue emptyVal = new Push.StackValue();
		emptyVal.setUndefined();
		pushEmpty.addValue(emptyVal);
		newFunc.addAction(pushEmpty);
		newFunc.addAction(new NewMethod());
		
		newFunc.addAction(new StoreRegister((short)0));
		
		
		//newObj.initialize.apply(newObj,arguments);
		newFunc.addAction(pushArg);
		newFunc.addAction(new GetVariable());
		Push pushReg0 = new Push();
		Push.StackValue reg0Val = new Push.StackValue();
		reg0Val.setRegisterNumber((short)0);
		pushReg0.addValue(reg0Val);
		newFunc.addAction(pushReg0);
		Push push2 = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2.addValue(twoVal);
		newFunc.addAction(push2);
		newFunc.addAction(pushReg0);
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		newFunc.addAction(pushInitialize);
		newFunc.addAction(new GetMember());
		Push pushApply = new Push();
		Push.StackValue applyVal = new Push.StackValue();
		applyVal.setString("apply");
		pushApply.addValue(applyVal);
		newFunc.addAction(pushApply);
		newFunc.addAction(new CallMethod());
		newFunc.addAction(new Pop());
		
		
		newFunc.addAction(new Return());
		
		actionTag.addAction(newFunc);
		actionTag.addAction(new SetMember());//newClassIns.new = newFunc
		
		actionTag.addAction(new SetMember());//Object.__proto__ = newClassIns
		
		
		//Function.prototype.__proto__ = Object;
		Push pushFunction = new Push();
		Push.StackValue functionVal = new Push.StackValue();
		functionVal.setString("Function");
		pushFunction.addValue(functionVal);
		actionTag.addAction(pushFunction);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushProtoObject = new Push();
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Initializes the class and instance methods of the Module class object
	 * @param actionTag The DoAction tag to attach to
	 * @param st The symbol table
	 */
	protected void generateInitModule(DoAction actionTag, SymbolTable st){
		Push pushModule = new Push();
		Push.StackValue moduleVal = new Push.StackValue();
		moduleVal.setString("Module");
		pushModule.addValue(moduleVal);
		actionTag.addAction(pushModule);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new PushDuplicate());
		
		//defines toString() for class Module.prototype
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		toStringFunc.addAction(pushThis);
		toStringFunc.addAction(new GetVariable());
		Push pushModuleNameProp = new Push();
		Push.StackValue moduleNamePropVal = new Push.StackValue();
		moduleNamePropVal.setString("name");
		pushModuleNameProp.addValue(moduleNamePropVal);
		toStringFunc.addAction(pushModuleNameProp);
		toStringFunc.addAction(new GetMember());
		toStringFunc.addAction(new Return());
		
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//defines Module.prototype.include
		Push pushInclude = new Push();
		Push.StackValue includeVal = new Push.StackValue();
		includeVal.setString("include");
		pushInclude.addValue(includeVal);
		actionTag.addAction(pushInclude);
		RegisterParam[] params = new RegisterParam[1];
		params[0] = new RegisterParam((short)0,"moduleArg");
		DefineFunction2 includeFunc = new DefineFunction2("",(short)4,params);
		
		//set the class proxy, for class methods
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		Push pushProto0Obj = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProto0Obj.addValue(protoVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushProto0Obj.addValue(zeroVal);
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("old Obj");
		pushProto0Obj.addValue(objVal);
		includeFunc.addAction(pushProto0Obj);
		includeFunc.addAction(new GetVariable());
		Push pushUndefined = new Push();
		Push.StackValue undefinedVal = new Push.StackValue();
		undefinedVal.setUndefined();
		pushUndefined.addValue(undefinedVal);
		includeFunc.addAction(pushUndefined);
		includeFunc.addAction(new NewMethod());
		
		includeFunc.addAction(new PushDuplicate());
		
		Push pushProtoNull = new Push();
		pushProtoNull.addValue(protoVal);
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushProtoNull.addValue(nullVal);
		includeFunc.addAction(pushProtoNull);
		includeFunc.addAction(new SetMember());
		
		includeFunc.addAction(new PushDuplicate());
		
		Push pushResolve = new Push();
		Push.StackValue resolveVal = new Push.StackValue();
		resolveVal.setString("__resolve");
		pushResolve.addValue(resolveVal);
		includeFunc.addAction(pushResolve);
		
		//define __resolve method
		RegisterParam[] resolveParams = new RegisterParam[1];
		resolveParams[0] = new RegisterParam((short)0,"funcName");
		DefineFunction2 resolveFunc = new DefineFunction2("",(short)4,resolveParams);
		Push pushArg = new Push();
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushArg.addValue(argVal);
		resolveFunc.addAction(pushArg);
		resolveFunc.addAction(new GetVariable());
		Push pushCallee = new Push();
		Push.StackValue calleeVal = new Push.StackValue();
		calleeVal.setString("callee");
		pushCallee.addValue(calleeVal);
		resolveFunc.addAction(pushCallee);
		resolveFunc.addAction(new GetMember());
		Push pushSelf = new Push();
		Push.StackValue selfVal = new Push.StackValue();
		selfVal.setString("self");
		pushSelf.addValue(selfVal);
		resolveFunc.addAction(pushSelf);
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(new StoreRegister((short)0));
		
		Push pushModuleLink = new Push();
		Push.StackValue moduleLinkVal = new Push.StackValue();
		moduleLinkVal.setString("moduleLink");
		pushModuleLink.addValue(moduleLinkVal);
		resolveFunc.addAction(pushModuleLink);
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(new StoreRegister((short)1));
		resolveFunc.addAction(new PushDuplicate());
		resolveFunc.addAction(new PushDuplicate());
		Push pushProto = new Push();
		pushProto.addValue(protoVal);
		resolveFunc.addAction(pushProto);
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(new StoreRegister((short)2));
		resolveFunc.addAction(new Pop());
		resolveFunc.addAction(pushProtoNull);
		resolveFunc.addAction(new SetMember());
		Push pushFuncName = new Push();
		Push.StackValue funcNameVal = new Push.StackValue();
		funcNameVal.setString("funcName");
		pushFuncName.addValue(funcNameVal);
		resolveFunc.addAction(pushFuncName);
		resolveFunc.addAction(new GetVariable());
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(new StoreRegister((short)3));
		resolveFunc.addAction(new Pop());
		Push pushReg1ProtoReg2 = new Push();
		Push.StackValue reg1Val = new Push.StackValue();
		reg1Val.setRegisterNumber((short)1);
		pushReg1ProtoReg2.addValue(reg1Val);
		pushReg1ProtoReg2.addValue(protoVal);
		Push.StackValue reg2Val = new Push.StackValue();
		reg2Val.setRegisterNumber((short)2);
		pushReg1ProtoReg2.addValue(reg2Val);
		resolveFunc.addAction(pushReg1ProtoReg2);
		resolveFunc.addAction(new SetMember());
		Push pushReg3Null = new Push();
		Push.StackValue reg3Val = new Push.StackValue();
		reg3Val.setRegisterNumber((short)3);
		pushReg3Null.addValue(reg3Val);
		pushReg3Null.addValue(nullVal);
		resolveFunc.addAction(pushReg3Null);
		resolveFunc.addAction(new Equals2());
		
		ActionBlock resolveNotNullBlock = new ActionBlock();
		Push pushReg3 = new Push();
		pushReg3.addValue(reg3Val);
		resolveNotNullBlock.addAction(pushReg3);
		resolveNotNullBlock.addAction(new Return());
		
		resolveFunc.addAction(new If((short)resolveNotNullBlock.getSize()));
		
		rubyToSwf.util.CodeGenUtil.copyTags(resolveFunc.getBody(), resolveNotNullBlock);
		
		Push pushReg0KlassLink = new Push();
		Push.StackValue reg0Val = new Push.StackValue();
		reg0Val.setRegisterNumber((short)0);
		pushReg0KlassLink.addValue(reg0Val);
		Push.StackValue klassLinkVal = new Push.StackValue();
		klassLinkVal.setString("klassLink");
		pushReg0KlassLink.addValue(klassLinkVal);
		resolveFunc.addAction(pushReg0KlassLink);
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(pushFuncName);
		resolveFunc.addAction(new GetVariable());
		resolveFunc.addAction(new GetMember());
		resolveFunc.addAction(new Return());
		
		includeFunc.addAction(resolveFunc);
		
		includeFunc.addAction(new SetMember());
		
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(pushResolve);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new StackSwap());
		includeFunc.addAction(pushSelf);
		includeFunc.addAction(new StackSwap());
		includeFunc.addAction(new SetMember());
		
		
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(new PushDuplicate());
		
		Push pushModuleLinkModuleArg = new Push();
		pushModuleLinkModuleArg.addValue(moduleLinkVal);
		Push.StackValue moduleArgVal = new Push.StackValue();
		moduleArgVal.setString("moduleArg");
		pushModuleLinkModuleArg.addValue(moduleArgVal);
		includeFunc.addAction(pushModuleLinkModuleArg);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(new SetMember());
		
		Push pushKlassLinkThis = new Push();
		pushKlassLinkThis.addValue(klassLinkVal);
		pushKlassLinkThis.addValue(thisVal);
		includeFunc.addAction(pushKlassLinkThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushProto);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new SetMember());
		
		includeFunc.addAction(new SetMember());
		
		
		//prototype proxy class for instance methods
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushProto0Obj);
		includeFunc.addAction(new NewObject());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(pushResolve);
		includeFunc.addAction(resolveFunc);
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(pushResolve);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new StackSwap());
		includeFunc.addAction(pushSelf);
		includeFunc.addAction(new StackSwap());
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(new PushDuplicate());
		includeFunc.addAction(pushProtoNull);
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(pushModuleLinkModuleArg);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(pushKlassLinkThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushProto);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new SetMember());
		
		includeFunc.addAction(new SetMember());
		
		//set the valueOf for both the class and its prototype
		//but first check if they already have a valueOf method
		
		//first, define the valueOf method.  Note that this DefineFunction is not added yet
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)1,new RegisterParam[0]);
		valueOfFunc.addAction(pushThis);
		valueOfFunc.addAction(new GetVariable());
		Push push1Arguments = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1Arguments.addValue(oneVal);
		push1Arguments.addValue(argVal);
		valueOfFunc.addAction(push1Arguments);
		valueOfFunc.addAction(new GetVariable());
		valueOfFunc.addAction(pushCallee);
		valueOfFunc.addAction(new GetMember());
		valueOfFunc.addAction(pushSelf);
		valueOfFunc.addAction(new GetMember());
		valueOfFunc.addAction(pushProto);
		valueOfFunc.addAction(new GetMember());
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		valueOfFunc.addAction(pushValueOf);
		valueOfFunc.addAction(new GetMember());
		Push pushCall = new Push();
		Push.StackValue callVal = new Push.StackValue();
		callVal.setString("call");
		pushCall.addValue(callVal);
		valueOfFunc.addAction(pushCall);
		valueOfFunc.addAction(new CallMethod());
		valueOfFunc.addAction(new Return());
		
		//set the valueOf method for the class object if neccessary
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		pushProtoThis.addValue(thisVal);
		includeFunc.addAction(pushProtoThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushProto);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new StoreRegister((short)0));
		includeFunc.addAction(new Pop());
		Push pushNull = new Push();
		pushNull.addValue(nullVal);
		includeFunc.addAction(pushNull);
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushValueOf);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushNull);
		includeFunc.addAction(new Equals2());
		includeFunc.addAction(new Not());
		
		ActionBlock noValueOfBlock = new ActionBlock();
		noValueOfBlock.addAction(pushThis);
		noValueOfBlock.addAction(new GetVariable());
		noValueOfBlock.addAction(pushValueOf);
		noValueOfBlock.addAction(valueOfFunc);
		noValueOfBlock.addAction(new PushDuplicate());
		Push pushSelfThis = new Push();
		pushSelfThis.addValue(selfVal);
		pushSelfThis.addValue(thisVal);
		noValueOfBlock.addAction(pushSelfThis);
		noValueOfBlock.addAction(new GetVariable());
		noValueOfBlock.addAction(new SetMember());
		noValueOfBlock.addAction(new SetMember());
		
		includeFunc.addAction(new If((short)noValueOfBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(includeFunc.getBody(), noValueOfBlock);
		
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		Push pushProtoReg0 = new Push();
		pushProtoReg0.addValue(protoVal);
		pushProtoReg0.addValue(reg0Val);
		includeFunc.addAction(pushProtoReg0);
		includeFunc.addAction(new SetMember());
		
		//set valueOf method to the class's prototype if needed
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushProtoThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushProto);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(new StoreRegister((short)0));
		includeFunc.addAction(new Pop());
		includeFunc.addAction(pushNull);
		includeFunc.addAction(new SetMember());
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushValueOf);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushNull);
		includeFunc.addAction(new Equals2());
		includeFunc.addAction(new Not());
		
		noValueOfBlock = new ActionBlock();
		noValueOfBlock.addAction(pushThis);
		noValueOfBlock.addAction(new GetVariable());
		noValueOfBlock.addAction(pushPrototype);
		noValueOfBlock.addAction(new GetMember());
		noValueOfBlock.addAction(pushValueOf);
		noValueOfBlock.addAction(valueOfFunc);
		noValueOfBlock.addAction(new PushDuplicate());
		noValueOfBlock.addAction(pushSelfThis);
		noValueOfBlock.addAction(new GetVariable());
		noValueOfBlock.addAction(pushPrototype);
		noValueOfBlock.addAction(new GetMember());
		noValueOfBlock.addAction(new SetMember());
		noValueOfBlock.addAction(new SetMember());
		
		includeFunc.addAction(new If((short)noValueOfBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(includeFunc.getBody(), noValueOfBlock);
		
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(pushPrototype);
		includeFunc.addAction(new GetMember());
		includeFunc.addAction(pushProtoReg0);
		includeFunc.addAction(new SetMember());
		
		//return this
		
		includeFunc.addAction(pushThis);
		includeFunc.addAction(new GetVariable());
		includeFunc.addAction(new Return());
		
		actionTag.addAction(includeFunc);
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Defines the standard functions for class Class
	 * @param actionTag The DoAction tag to append to
	 * @param st The symbol table
	 */
	protected void generateInitClass(DoAction actionTag, SymbolTable st){
		//nothing yet, inherits from Module
	}
	
	protected void generateNumericClass(DoAction actionTag,SymbolTable st){
		DefineFunction2 numericClass = new DefineFunction2("Numeric",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		numericClass.addAction(pushThis);
		numericClass.addAction(new GetVariable());
		Push pushConstructorNumeric = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorNumeric.addValue(constructorVal);
		Push.StackValue numericVal = new Push.StackValue();
		numericVal.setString("Numeric");
		pushConstructorNumeric.addValue(numericVal);
		numericClass.addAction(pushConstructorNumeric);
		numericClass.addAction(new GetVariable());
		numericClass.addAction(new SetMember());
		
		actionTag.addAction(numericClass);
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushNumericNumeric = new Push();
		pushNumericNumeric.addValue(numericVal);
		pushNumericNumeric.addValue(numericVal);
		actionTag.addAction(pushNumericNumeric);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());//Numeric.prototype = new Object();
		actionTag.addAction(new PushDuplicate());//Numeric.__proto__ = Object;
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		Push pushPrototype0Object = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0Object.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0Object.addValue(zeroVal);
		pushPrototype0Object.addValue(objectVal);
		actionTag.addAction(pushPrototype0Object);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Defines the Integer class and its methods
	 * @param actionTag
	 */
	protected void generateIntegerClass(DoAction actionTag,SymbolTable st){
		RegisterParam[] intParams = new RegisterParam[1];
		intParams[0] = new RegisterParam((short)0,"i");
		DefineFunction2 integerClass = new DefineFunction2("Integer",(short)0,intParams);
		
		Push pushI = new Push();
		Push.StackValue iVal = new Push.StackValue();
		iVal.setString("i");
		pushI.addValue(iVal);
		integerClass.addAction(pushI);
		integerClass.addAction(new GetVariable());
		integerClass.addAction(new TypeOf());
		Push pushNumber = new Push();
		Push.StackValue numberVal = new Push.StackValue();
		numberVal.setString("number");
		pushNumber.addValue(numberVal);
		integerClass.addAction(pushNumber);
		integerClass.addAction(new Equals2());
		
		ActionBlock notNumberBlock = new ActionBlock();
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		notNumberBlock.addAction(pushThis);
		notNumberBlock.addAction(new GetVariable());
		Push pushValueAttr0I = new Push();
		Push.StackValue valueAttrVal = new Push.StackValue();
		valueAttrVal.setString("@value");
		pushValueAttr0I.addValue(valueAttrVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushValueAttr0I.addValue(zeroVal);
		pushValueAttr0I.addValue(iVal);
		notNumberBlock.addAction(pushValueAttr0I);
		notNumberBlock.addAction(new GetVariable());
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		notNumberBlock.addAction(pushValueOf);
		
		//push onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		notNumberBlock.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		notNumberBlock.addAction(push1ClosureStack);
		notNumberBlock.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		notNumberBlock.addAction(pushPush);
		notNumberBlock.addAction(new CallMethod());
		notNumberBlock.addAction(new Pop());
		
		notNumberBlock.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		notNumberBlock.addAction(push0ClosureStack);
		notNumberBlock.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		notNumberBlock.addAction(pushPop);
		notNumberBlock.addAction(new CallMethod());
		notNumberBlock.addAction(new Pop());
		
		notNumberBlock.addAction(new SetMember());
		
		ActionBlock numberBlock = new ActionBlock();
		numberBlock.addAction(pushThis);
		numberBlock.addAction(new GetVariable());
		Push pushValueAttrI = new Push();
		pushValueAttrI.addValue(valueAttrVal);
		pushValueAttrI.addValue(iVal);
		numberBlock.addAction(pushValueAttrI);
		numberBlock.addAction(new GetVariable());
		numberBlock.addAction(new SetMember());
		
		notNumberBlock.addAction(new Jump((short)numberBlock.getSize()));
		
		integerClass.addAction(new If((short)notNumberBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(integerClass.getBody(), notNumberBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(integerClass.getBody(), numberBlock);
		
		//set constructor attribute
		integerClass.addAction(pushThis);
		integerClass.addAction(new GetVariable());
		Push pushConstructorInteger = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorInteger.addValue(constructorVal);
		Push.StackValue integerVal = new Push.StackValue();
		integerVal.setString("Integer");
		pushConstructorInteger.addValue(integerVal);
		integerClass.addAction(pushConstructorInteger);
		integerClass.addAction(new GetVariable());
		integerClass.addAction(new SetMember());
		
		actionTag.addAction(integerClass);
		Push pushInteger = new Push();
		pushInteger.addValue(integerVal);
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		Push pushPrototype0Numeric = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype0Numeric.addValue(prototypeValue);
		Push.StackValue numericVal = new Push.StackValue();
		numericVal.setString("Numeric");
		pushPrototype0Numeric.addValue(numericVal);
		actionTag.addAction(pushPrototype0Numeric);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//plus op
		Push pushPlus = new Push();
		Push.StackValue plusVal = new Push.StackValue();
		plusVal.setString("+");
		pushPlus.addValue(plusVal);
		actionTag.addAction(pushPlus);
		RegisterParam[] params = new RegisterParam[1];
		params[0] = new RegisterParam((short)0,"other");
		DefineFunction2 plusFunc = new DefineFunction2("",(short)0,params);
		plusFunc.addAction(pushThis);
		plusFunc.addAction(new GetVariable());
		Push pushValue = new Push();
		pushValue.addValue(valueAttrVal);
		plusFunc.addAction(pushValue);
		plusFunc.addAction(new GetMember());
		Push pushOther = new Push();
		Push.StackValue otherVal = new Push.StackValue();
		otherVal.setString("other");
		pushOther.addValue(otherVal);
		plusFunc.addAction(pushOther);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushValue);
		plusFunc.addAction(new GetMember());
		plusFunc.addAction(new Add2());
		Push push1 = new Push();
		Push.StackValue oneValue = new Push.StackValue();
		oneValue.setInteger(1);
		push1.addValue(oneValue);
		plusFunc.addAction(push1);
		
		//if Float, return a Float
		plusFunc.addAction(pushOther);
		plusFunc.addAction(new GetVariable());
		Push pushConstructor = new Push();
		pushConstructor.addValue(constructorVal);
		plusFunc.addAction(pushConstructor);
		plusFunc.addAction(new GetMember());
		Push pushFloat = new Push();
		Push.StackValue floatVal = new Push.StackValue();
		floatVal.setString("Float");
		pushFloat.addValue(floatVal);
		plusFunc.addAction(pushFloat);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(new Equals2());

		ActionBlock integerBlock = new ActionBlock();
		integerBlock.addAction(pushInteger);
		integerBlock.addAction(new Jump((short)pushFloat.getSize()));

		plusFunc.addAction(new If((short)integerBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(plusFunc.getBody(), integerBlock);
		plusFunc.addAction(pushFloat);
		
		plusFunc.addAction(new NewObject());
		plusFunc.addAction(new Return());
		actionTag.addAction(plusFunc);
		actionTag.addAction(new SetMember());
		
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//minus op
		Push pushMinus = new Push();
		Push.StackValue minusVal = new Push.StackValue();
		minusVal.setString("-");
		pushMinus.addValue(minusVal);
		actionTag.addAction(pushMinus);
		DefineFunction2 minusFunc = new DefineFunction2("",(short)0,params);
		minusFunc.addAction(pushThis);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(pushValue);
		minusFunc.addAction(new GetMember());
		minusFunc.addAction(pushOther);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(pushValue);
		minusFunc.addAction(new GetMember());
		minusFunc.addAction(new Subtract());

		minusFunc.addAction(push1);
		
		//if Float, return a Float
		minusFunc.addAction(pushOther);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(pushConstructor);
		minusFunc.addAction(new GetMember());
		minusFunc.addAction(pushFloat);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(new Equals2());

		minusFunc.addAction(new If((short)integerBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(minusFunc.getBody(), integerBlock);
		minusFunc.addAction(pushFloat);
		
		minusFunc.addAction(new NewObject());
		minusFunc.addAction(new Return());
		actionTag.addAction(minusFunc);
		actionTag.addAction(new SetMember());
		
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//multiply op
		Push pushMultiply = new Push();
		Push.StackValue multiplyVal = new Push.StackValue();
		multiplyVal.setString("*");
		pushMultiply.addValue(multiplyVal);
		actionTag.addAction(pushMultiply);
		DefineFunction2 multiplyFunc = new DefineFunction2("",(short)0,params);
		multiplyFunc.addAction(pushThis);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(pushValue);
		multiplyFunc.addAction(new GetMember());
		multiplyFunc.addAction(pushOther);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(pushValue);
		multiplyFunc.addAction(new GetMember());
		multiplyFunc.addAction(new Multiply());

		multiplyFunc.addAction(push1);
		
		//if Float, return a Float
		multiplyFunc.addAction(pushOther);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(pushConstructor);
		multiplyFunc.addAction(new GetMember());
		multiplyFunc.addAction(pushFloat);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(new Equals2());

		multiplyFunc.addAction(new If((short)integerBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(multiplyFunc.getBody(), integerBlock);
		multiplyFunc.addAction(pushFloat);
		
		multiplyFunc.addAction(new NewObject());
		multiplyFunc.addAction(new Return());
		actionTag.addAction(multiplyFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//divide op
		Push pushDivide = new Push();
		Push.StackValue divideVal = new Push.StackValue();
		divideVal.setString("/");
		pushDivide.addValue(divideVal);
		actionTag.addAction(pushDivide);
		DefineFunction2 divideFunc = new DefineFunction2("",(short)0,params);
		divideFunc.addAction(pushThis);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(pushValue);
		divideFunc.addAction(new GetMember());
		divideFunc.addAction(pushOther);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(pushValue);
		divideFunc.addAction(new GetMember());
		divideFunc.addAction(new Divide());
		divideFunc.addAction(push1);
		
		//if Float, return a Float
		divideFunc.addAction(pushOther);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(pushConstructor);
		divideFunc.addAction(new GetMember());
		divideFunc.addAction(pushFloat);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(new Equals2());

		divideFunc.addAction(new If((short)integerBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(divideFunc.getBody(), integerBlock);
		divideFunc.addAction(pushFloat);
		
		divideFunc.addAction(new NewObject());
		divideFunc.addAction(new Return());
		actionTag.addAction(divideFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//modulo op
		Push pushModulo = new Push();
		Push.StackValue moduloVal = new Push.StackValue();
		moduloVal.setString("%");
		pushModulo.addValue(moduloVal);
		actionTag.addAction(pushModulo);
		DefineFunction2 moduloFunc = new DefineFunction2("",(short)0,params);
		moduloFunc.addAction(pushThis);
		moduloFunc.addAction(new GetVariable());
		moduloFunc.addAction(pushValue);
		moduloFunc.addAction(new GetMember());
		moduloFunc.addAction(pushOther);
		moduloFunc.addAction(new GetVariable());
		moduloFunc.addAction(pushValue);
		moduloFunc.addAction(new GetMember());
		moduloFunc.addAction(new Modulo());
		moduloFunc.addAction(push1);
		moduloFunc.addAction(pushInteger);
		moduloFunc.addAction(new NewObject());
		moduloFunc.addAction(new Return());
		actionTag.addAction(moduloFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//power op
		Push pushPower = new Push();
		Push.StackValue powerVal = new Push.StackValue();
		powerVal.setString("**");
		pushPower.addValue(powerVal);
		actionTag.addAction(pushPower);
		DefineFunction2 powerFunc = new DefineFunction2("",(short)0,params);
		powerFunc.addAction(pushOther);
		powerFunc.addAction(new GetVariable());
		powerFunc.addAction(pushValue);
		powerFunc.addAction(new GetMember());
		powerFunc.addAction(pushThis);
		powerFunc.addAction(new GetVariable());
		powerFunc.addAction(pushValue);
		powerFunc.addAction(new GetMember());
		Push push2Math = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2Math.addValue(twoVal);
		Push.StackValue mathVal = new Push.StackValue();
		mathVal.setString("Math");
		push2Math.addValue(mathVal);
		powerFunc.addAction(push2Math);
		powerFunc.addAction(new GetVariable());
		Push pushPow = new Push();
		Push.StackValue powVal = new Push.StackValue();
		powVal.setString("pow");
		pushPow.addValue(powVal);
		powerFunc.addAction(pushPow);
		powerFunc.addAction(new CallMethod());
		powerFunc.addAction(push1);
		powerFunc.addAction(new PushDuplicate());
		powerFunc.addAction(new PushDuplicate());
		
		//if Float, return a Float, test by checking that val = Math.ceil(val);
		Push push1Math = new Push();
		push1Math.addValue(oneVal);
		push1Math.addValue(mathVal);
		powerFunc.addAction(push1Math);
		powerFunc.addAction(new GetVariable());
		Push pushCeil = new Push();
		Push.StackValue ceilVal = new Push.StackValue();
		ceilVal.setString("ceil");
		pushCeil.addValue(ceilVal);
		powerFunc.addAction(pushCeil);
		powerFunc.addAction(new CallMethod());
		powerFunc.addAction(new Equals2());
		powerFunc.addAction(new Not());

		powerFunc.addAction(new If((short)integerBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(powerFunc.getBody(), integerBlock);
		powerFunc.addAction(pushFloat);
		
		powerFunc.addAction(new NewObject());
		powerFunc.addAction(new Return());
		actionTag.addAction(powerFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//bitwise &
		Push pushBitwiseAnd = new Push();
		Push.StackValue bitwiseAndVal = new Push.StackValue();
		bitwiseAndVal.setString("&");
		pushBitwiseAnd.addValue(bitwiseAndVal);
		actionTag.addAction(pushBitwiseAnd);
		DefineFunction2 bitwiseAndFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		bitwiseAndFunc.addAction(pushThis);
		bitwiseAndFunc.addAction(new GetVariable());
		bitwiseAndFunc.addAction(pushValue);
		bitwiseAndFunc.addAction(new GetMember());
		bitwiseAndFunc.addAction(pushOther);
		bitwiseAndFunc.addAction(new GetVariable());
		bitwiseAndFunc.addAction(pushValue);
		bitwiseAndFunc.addAction(new GetMember());
		bitwiseAndFunc.addAction(new BitAnd());
		Push push1Integer = new Push();
		push1Integer.addValue(oneValue);
		push1Integer.addValue(integerVal);
		bitwiseAndFunc.addAction(push1Integer);
		bitwiseAndFunc.addAction(new NewObject());
		bitwiseAndFunc.addAction(new Return());
		actionTag.addAction(bitwiseAndFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//bitwise |
		Push pushBitwiseOr = new Push();
		Push.StackValue bitwiseOrVal = new Push.StackValue();
		bitwiseOrVal.setString("|");
		pushBitwiseOr.addValue(bitwiseOrVal);
		actionTag.addAction(pushBitwiseOr);
		DefineFunction2 bitwiseOrFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		bitwiseOrFunc.addAction(pushThis);
		bitwiseOrFunc.addAction(new GetVariable());
		bitwiseOrFunc.addAction(pushValue);
		bitwiseOrFunc.addAction(new GetMember());
		bitwiseOrFunc.addAction(pushOther);
		bitwiseOrFunc.addAction(new GetVariable());
		bitwiseOrFunc.addAction(pushValue);
		bitwiseOrFunc.addAction(new GetMember());
		bitwiseOrFunc.addAction(new BitOr());
		bitwiseOrFunc.addAction(push1Integer);
		bitwiseOrFunc.addAction(new NewObject());
		bitwiseOrFunc.addAction(new Return());
		actionTag.addAction(bitwiseOrFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//bitwise ^
		Push pushBitwiseXOr = new Push();
		Push.StackValue bitwiseXOrVal = new Push.StackValue();
		bitwiseXOrVal.setString("^");
		pushBitwiseXOr.addValue(bitwiseXOrVal);
		actionTag.addAction(pushBitwiseXOr);
		DefineFunction2 bitwiseXOrFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		bitwiseXOrFunc.addAction(pushThis);
		bitwiseXOrFunc.addAction(new GetVariable());
		bitwiseXOrFunc.addAction(pushValue);
		bitwiseXOrFunc.addAction(new GetMember());
		bitwiseXOrFunc.addAction(pushOther);
		bitwiseXOrFunc.addAction(new GetVariable());
		bitwiseXOrFunc.addAction(pushValue);
		bitwiseXOrFunc.addAction(new GetMember());
		bitwiseXOrFunc.addAction(new BitXor());
		bitwiseXOrFunc.addAction(push1Integer);
		bitwiseXOrFunc.addAction(new NewObject());
		bitwiseXOrFunc.addAction(new Return());
		actionTag.addAction(bitwiseXOrFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//bitwise <<
		Push pushBitwiseLShift = new Push();
		Push.StackValue bitwiseLShiftVal = new Push.StackValue();
		bitwiseLShiftVal.setString("<<");
		pushBitwiseLShift.addValue(bitwiseLShiftVal);
		actionTag.addAction(pushBitwiseLShift);
		DefineFunction2 bitwiseLShiftFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		bitwiseLShiftFunc.addAction(pushThis);
		bitwiseLShiftFunc.addAction(new GetVariable());
		bitwiseLShiftFunc.addAction(pushValue);
		bitwiseLShiftFunc.addAction(new GetMember());
		bitwiseLShiftFunc.addAction(pushOther);
		bitwiseLShiftFunc.addAction(new GetVariable());
		bitwiseLShiftFunc.addAction(pushValue);
		bitwiseLShiftFunc.addAction(new GetMember());
		bitwiseLShiftFunc.addAction(new BitLShift());
		bitwiseLShiftFunc.addAction(push1Integer);
		bitwiseLShiftFunc.addAction(new NewObject());
		bitwiseLShiftFunc.addAction(new Return());
		actionTag.addAction(bitwiseLShiftFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//bitwise >>
		Push pushBitwiseRShift = new Push();
		Push.StackValue bitwiseRShiftVal = new Push.StackValue();
		bitwiseRShiftVal.setString(">>");
		pushBitwiseRShift.addValue(bitwiseRShiftVal);
		actionTag.addAction(pushBitwiseRShift);
		DefineFunction2 bitwiseRShiftFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		bitwiseRShiftFunc.addAction(pushThis);
		bitwiseRShiftFunc.addAction(new GetVariable());
		bitwiseRShiftFunc.addAction(pushValue);
		bitwiseRShiftFunc.addAction(new GetMember());
		bitwiseRShiftFunc.addAction(pushOther);
		bitwiseRShiftFunc.addAction(new GetVariable());
		bitwiseRShiftFunc.addAction(pushValue);
		bitwiseRShiftFunc.addAction(new GetMember());
		bitwiseRShiftFunc.addAction(new BitRShift());
		bitwiseRShiftFunc.addAction(push1Integer);
		bitwiseRShiftFunc.addAction(new NewObject());
		bitwiseRShiftFunc.addAction(new Return());
		actionTag.addAction(bitwiseRShiftFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//valueOf()
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		valueOfFunc.addAction(pushThis);
		valueOfFunc.addAction(new GetVariable());
		valueOfFunc.addAction(pushValue);
		valueOfFunc.addAction(new GetMember());
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString()
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		toStringFunc.addAction(pushThis);
		toStringFunc.addAction(new GetVariable());
		toStringFunc.addAction(pushValue);
		toStringFunc.addAction(new GetMember());
		toStringFunc.addAction(new Return());
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//compare func, aka <=>
		Push pushCompare = new Push();
		Push.StackValue compareVal = new Push.StackValue();
		compareVal.setString("<=>");
		pushCompare.addValue(compareVal);
		actionTag.addAction(pushCompare);
		
		DefineFunction2 compareFunc = new DefineFunction2("",(short)0,params);
		compareFunc.addAction(pushThis);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushValue);
		compareFunc.addAction(new GetMember());
		compareFunc.addAction(pushOther);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushOther);
		compareFunc.addAction(new GetMember());
		compareFunc.addAction(new Subtract());

		compareFunc.addAction(push1Integer);
		compareFunc.addAction(new NewObject());
		compareFunc.addAction(new Return());
		actionTag.addAction(compareFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//less op
		Push pushLess = new Push();
		Push.StackValue lessVal = new Push.StackValue();
		lessVal.setString("<");
		pushLess.addValue(lessVal);
		actionTag.addAction(pushLess);
		DefineFunction2 lessFunc = new DefineFunction2("",(short)0,params);
		lessFunc.addAction(pushThis);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValue);
		lessFunc.addAction(new GetMember());
		lessFunc.addAction(pushOther);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValue);
		lessFunc.addAction(new GetMember());
		lessFunc.addAction(new Less());
		
		ActionBlock ifTrueBlock = new ActionBlock();
		Push pushEnv = new Push();
		Push.StackValue envName = new Push.StackValue();
		envName.setString("current Env");
		pushEnv.addValue(envName);
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		Push pushTrue = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setString("true");
		pushTrue.addValue(trueVal);
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ActionBlock ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		Push pushFalse = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setString("false");
		pushFalse.addValue(falseVal);
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		lessFunc.addAction(new If((short)ifFalseBlock.getSize()));
		java.util.List actions = ifFalseBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		actions = ifTrueBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		
		lessFunc.addAction(new Return());
		
		actionTag.addAction(lessFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//great op
		Push pushGreat = new Push();
		Push.StackValue greatVal = new Push.StackValue();
		greatVal.setString(">");
		pushGreat.addValue(greatVal);
		actionTag.addAction(pushGreat);
		DefineFunction2 greatFunc = new DefineFunction2("",(short)0,params);
		greatFunc.addAction(pushThis);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValue);
		greatFunc.addAction(new GetMember());
		greatFunc.addAction(pushOther);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValue);
		greatFunc.addAction(new GetMember());
		greatFunc.addAction(new Greater());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		greatFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifTrueBlock);
		
		greatFunc.addAction(new Return());
		
		actionTag.addAction(greatFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//LEQ op
		Push pushLeq = new Push();
		Push.StackValue leqVal = new Push.StackValue();
		leqVal.setString("<=");
		pushLeq.addValue(leqVal);
		actionTag.addAction(pushLeq);
		DefineFunction2 leqFunc = new DefineFunction2("",(short)0,params);
		leqFunc.addAction(pushThis);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValue);
		leqFunc.addAction(new GetMember());
		leqFunc.addAction(pushOther);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValue);
		leqFunc.addAction(new GetMember());
		leqFunc.addAction(new Greater());
		leqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		leqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifTrueBlock);
		
		leqFunc.addAction(new Return());
		
		actionTag.addAction(leqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//GEQ op
		Push pushGeq = new Push();
		Push.StackValue geqVal = new Push.StackValue();
		geqVal.setString(">=");
		pushGeq.addValue(geqVal);
		actionTag.addAction(pushGeq);
		DefineFunction2 geqFunc = new DefineFunction2("",(short)0,params);
		geqFunc.addAction(pushThis);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValue);
		geqFunc.addAction(new GetMember());
		geqFunc.addAction(pushOther);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValue);
		geqFunc.addAction(new GetMember());
		geqFunc.addAction(new Less());
		geqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		geqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifTrueBlock);
		
		geqFunc.addAction(new Return());
		
		actionTag.addAction(geqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//eq op
		Push pushEq = new Push();
		Push.StackValue eqVal = new Push.StackValue();
		eqVal.setString("==");
		pushEq.addValue(eqVal);
		actionTag.addAction(pushEq);
		DefineFunction2 eqFunc = new DefineFunction2("",(short)0,params);
		eqFunc.addAction(pushThis);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValue);
		eqFunc.addAction(new GetMember());
		eqFunc.addAction(pushOther);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValue);
		eqFunc.addAction(new GetMember());
		eqFunc.addAction(new Equals2());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		eqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifTrueBlock);
		
		eqFunc.addAction(new Return());
		
		actionTag.addAction(eqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		Push pushEq3 = new Push();
		Push.StackValue eq3Val = new Push.StackValue();
		eq3Val.setString("===");
		pushEq3.addValue(eq3Val);
		actionTag.addAction(pushEq3);
		
		DefineFunction2 eq3Func = new DefineFunction2("",(short)0,params);
		
		eq3Func.addAction(pushOther);
		eq3Func.addAction(new GetVariable());
		Push push1This = new Push();
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		eq3Func.addAction(push1This);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushEq);
		
		//push null onto the stack
		eq3Func.addAction(pushNull);
		eq3Func.addAction(push1ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPush);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new CallMethod());
		
		//pop from closure stack
		eq3Func.addAction(push0ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPop);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new Return());
		
		actionTag.addAction(eq3Func);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//unary plus
		Push pushUPlus = new Push();
		Push.StackValue uPlusVal = new Push.StackValue();
		uPlusVal.setString("@+");
		pushUPlus.addValue(uPlusVal);
		actionTag.addAction(pushUPlus);
		DefineFunction2 uPlusFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		uPlusFunc.addAction(pushThis);
		uPlusFunc.addAction(new GetVariable());
		uPlusFunc.addAction(pushValue);
		uPlusFunc.addAction(new GetMember());
		uPlusFunc.addAction(push1Integer);
		uPlusFunc.addAction(new NewObject());
		uPlusFunc.addAction(new Return());
		actionTag.addAction(uPlusFunc);
		actionTag.addAction(new SetMember());
		
		//push "Integer.prototype"
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//unary minus
		Push pushUMinus = new Push();
		Push.StackValue uMinusVal = new Push.StackValue();
		uMinusVal.setString("@-");
		pushUMinus.addValue(uMinusVal);
		actionTag.addAction(pushUMinus);
		
		DefineFunction2 uMinusFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		uMinusFunc.addAction(pushThis);
		uMinusFunc.addAction(new GetVariable());
		uMinusFunc.addAction(pushValue);
		uMinusFunc.addAction(new GetMember());
		Push.StackValue negOneVal = new Push.StackValue();
		negOneVal.setInteger(-1);
		Push pushNeg1 = new Push();
		pushNeg1.addValue(negOneVal);
		uMinusFunc.addAction(pushNeg1);
		uMinusFunc.addAction(new Multiply());
		uMinusFunc.addAction(push1Integer);
		uMinusFunc.addAction(new NewObject());
		uMinusFunc.addAction(new Return());
		
		actionTag.addAction(uMinusFunc);
		actionTag.addAction(new SetMember());
		
		//assign Integer to Object["Integer"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushIntInt = new Push();
		pushIntInt.addValue(integerVal);
		pushIntInt.addValue(integerVal);
		actionTag.addAction(pushIntInt);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//assign Integer to Object["Fixnum"]
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushFixnumInt = new Push();
		Push.StackValue fixnumVal = new Push.StackValue();
		fixnumVal.setString("Fixnum");
		pushFixnumInt.addValue(fixnumVal);
		pushFixnumInt.addValue(integerVal);
		actionTag.addAction(pushFixnumInt);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		actionTag.addAction(pushInteger);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//Integer.name = "Integer"
		Push pushNameInteger = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameInteger.addValue(nameVal);
		pushNameInteger.addValue(integerVal);
		actionTag.addAction(pushNameInteger);
		actionTag.addAction(new SetMember());
		
		//assign: Integer.__proto__ = Numeric
		Push pushProtoNumeric = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoNumeric.addValue(protoVal);
		pushProtoNumeric.addValue(numericVal);
		actionTag.addAction(pushProtoNumeric);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateStringClass(DoAction actionTag,SymbolTable st){
		
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushString = new Push();
		Push.StackValue stringVal = new Push.StackValue();
		stringVal.setString("String");
		pushString.addValue(stringVal);
		actionTag.addAction(pushString);

		DefineFunction2 stringFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"aString")});

		Push pushAString = new Push();
		Push.StackValue aStringVal = new Push.StackValue();
		aStringVal.setString("aString");
		pushAString.addValue(aStringVal);
		stringFunc.addAction(pushAString);
		stringFunc.addAction(new GetVariable());
		stringFunc.addAction(new TypeOf());
		Push pushString2 = new Push();
		Push.StackValue string2Val = new Push.StackValue();
		string2Val.setString("string");
		pushString2.addValue(string2Val);
		stringFunc.addAction(pushString2);
		stringFunc.addAction(new Equals2());
		
		ActionBlock notStringBlock = new ActionBlock();
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		notStringBlock.addAction(pushThis);
		notStringBlock.addAction(new GetVariable());
		Push pushValueAttr0I = new Push();
		Push.StackValue valueAttrVal = new Push.StackValue();
		valueAttrVal.setString("@value");
		pushValueAttr0I.addValue(valueAttrVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushValueAttr0I.addValue(zeroVal);
		pushValueAttr0I.addValue(aStringVal);
		notStringBlock.addAction(pushValueAttr0I);
		notStringBlock.addAction(new GetVariable());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		notStringBlock.addAction(pushToString);
		
		//push onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		notStringBlock.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		notStringBlock.addAction(push1ClosureStack);
		notStringBlock.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		notStringBlock.addAction(pushPush);
		notStringBlock.addAction(new CallMethod());
		notStringBlock.addAction(new Pop());
		
		notStringBlock.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		notStringBlock.addAction(push0ClosureStack);
		notStringBlock.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		notStringBlock.addAction(pushPop);
		notStringBlock.addAction(new CallMethod());
		notStringBlock.addAction(new Pop());
		
		notStringBlock.addAction(new SetMember());
		
		ActionBlock stringBlock = new ActionBlock();
		stringBlock.addAction(pushThis);
		stringBlock.addAction(new GetVariable());
		Push pushValueAttrI = new Push();
		pushValueAttrI.addValue(valueAttrVal);
		pushValueAttrI.addValue(aStringVal);
		stringBlock.addAction(pushValueAttrI);
		stringBlock.addAction(new GetVariable());
		stringBlock.addAction(new SetMember());
		
		notStringBlock.addAction(new Jump((short)stringBlock.getSize()));
		
		stringFunc.addAction(new If((short)notStringBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(stringFunc.getBody(), notStringBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(stringFunc.getBody(), stringBlock);
		
		//set constructor attr
		stringFunc.addAction(pushThis);
		stringFunc.addAction(new GetVariable());
		Push pushConstructorArguments = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorArguments.addValue(constructorVal);
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushConstructorArguments.addValue(argVal);
		stringFunc.addAction(pushConstructorArguments);
		stringFunc.addAction(new GetVariable());
		Push pushCallee = new Push();
		Push.StackValue calleeVal = new Push.StackValue();
		calleeVal.setString("callee");
		pushCallee.addValue(calleeVal);
		stringFunc.addAction(pushCallee);
		stringFunc.addAction(new GetMember());
		stringFunc.addAction(new SetMember());
		
		actionTag.addAction(stringFunc);
		
		actionTag.addAction(new PushDuplicate());//String.prototype = new Object()
		actionTag.addAction(new PushDuplicate());//toString
		actionTag.addAction(new PushDuplicate());//valueOf
		actionTag.addAction(new PushDuplicate());//to_s
		actionTag.addAction(new PushDuplicate());//+
		actionTag.addAction(new PushDuplicate());//<
		actionTag.addAction(new PushDuplicate());//>
		actionTag.addAction(new PushDuplicate());//<=
		actionTag.addAction(new PushDuplicate());//>=
		actionTag.addAction(new PushDuplicate());//==
		actionTag.addAction(new PushDuplicate());//<=>
		actionTag.addAction(new PushDuplicate());//name
		actionTag.addAction(new PushDuplicate());//__proto__

		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		Push push0Object = new Push();
		push0Object.addValue(zeroVal);
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		push0Object.addValue(objVal);
		actionTag.addAction(push0Object);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());		
		
		//toString
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		toStringFunc.addAction(pushThis);
		toStringFunc.addAction(new GetVariable());
		Push pushValue = new Push();
		pushValue.addValue(valueAttrVal);
		toStringFunc.addAction(pushValue);
		toStringFunc.addAction(new GetMember());
		toStringFunc.addAction(new Return());
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//set String.prototype.valueOf
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		valueOfFunc.addAction(pushThis);
		valueOfFunc.addAction(new GetVariable());
		valueOfFunc.addAction(pushValue);
		valueOfFunc.addAction(new GetMember());
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
	
		//define to_s to return this
		Push pushToS = new Push();
		Push.StackValue toSVal = new Push.StackValue();
		toSVal.setString("to_s");
		pushToS.addValue(toSVal);
		actionTag.addAction(pushToS);
		DefineFunction2 toSFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		toSFunc.addAction(pushThis);
		toSFunc.addAction(new GetVariable());
		toSFunc.addAction(new Return());
		actionTag.addAction(toSFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		//plus op
		Push pushPlus = new Push();
		Push.StackValue plusVal = new Push.StackValue();
		plusVal.setString("+");
		pushPlus.addValue(plusVal);
		actionTag.addAction(pushPlus);
		RegisterParam[] params = new RegisterParam[1];
		params[0] = new RegisterParam((short)0,"other");
		DefineFunction2 plusFunc = new DefineFunction2("",(short)0,params);
		Push push0 = new Push();
		push0.addValue(zeroVal);
		plusFunc.addAction(push0);
		plusFunc.addAction(pushThis);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushValueOf);
		
		//push onto closure stack
		plusFunc.addAction(pushNull);
		plusFunc.addAction(push1ClosureStack);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushPush);
		plusFunc.addAction(new CallMethod());
		plusFunc.addAction(new Pop());
		
		plusFunc.addAction(new CallMethod());
		
		//pop from closure stack
		plusFunc.addAction(push0ClosureStack);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushPop);
		plusFunc.addAction(new CallMethod());
		plusFunc.addAction(new Pop());
		
		plusFunc.addAction(push0);
		Push pushOther = new Push();
		Push.StackValue otherVal = new Push.StackValue();
		otherVal.setString("other");
		pushOther.addValue(otherVal);
		plusFunc.addAction(pushOther);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushValueOf);
		
		//push onto closure stack
		plusFunc.addAction(pushNull);
		plusFunc.addAction(push1ClosureStack);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushPush);
		plusFunc.addAction(new CallMethod());
		plusFunc.addAction(new Pop());
		
		plusFunc.addAction(new CallMethod());
		
		//pop from closure stack
		plusFunc.addAction(push0ClosureStack);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushPop);
		plusFunc.addAction(new CallMethod());
		plusFunc.addAction(new Pop());
		
		plusFunc.addAction(new Add2());
		Push push1Object = new Push();
		Push.StackValue oneValue = new Push.StackValue();
		oneValue.setInteger(1);
		push1Object.addValue(oneValue);
		push1Object.addValue(objVal);
		plusFunc.addAction(push1Object);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushString);
		plusFunc.addAction(new NewMethod());
		plusFunc.addAction(new Return());
		actionTag.addAction(plusFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		// define <
		Push pushLess = new Push();
		Push.StackValue lessVal = new Push.StackValue();
		lessVal.setString("<");
		pushLess.addValue(lessVal);
		actionTag.addAction(pushLess);
		DefineFunction2 lessFunc = new DefineFunction2("",(short)0,params);
		Push push0This = new Push();
		push0This.addValue(zeroVal);
		push0This.addValue(thisVal);
		lessFunc.addAction(push0This);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValueOf);
		
		//push onto closure stack
		lessFunc.addAction(pushNull);
		lessFunc.addAction(push1ClosureStack);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushPush);
		lessFunc.addAction(new CallMethod());
		lessFunc.addAction(new Pop());
		
		lessFunc.addAction(new CallMethod());
		
		//pop from closure stack
		lessFunc.addAction(push0ClosureStack);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushPop);
		lessFunc.addAction(new CallMethod());
		lessFunc.addAction(new Pop());
		
		Push push0Other = new Push();
		push0Other.addValue(zeroVal);
		push0Other.addValue(otherVal);
		lessFunc.addAction(push0Other);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValueOf);
		
		//push onto closure stack
		lessFunc.addAction(pushNull);
		lessFunc.addAction(push1ClosureStack);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushPush);
		lessFunc.addAction(new CallMethod());
		lessFunc.addAction(new Pop());
		
		lessFunc.addAction(new CallMethod());
		
		//pop from closure stack
		lessFunc.addAction(push0ClosureStack);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushPop);
		lessFunc.addAction(new CallMethod());
		lessFunc.addAction(new Pop());
		
		lessFunc.addAction(new Less2());
		
		ActionBlock ifTrueBlock = new ActionBlock();
		Push pushEnv = new Push();
		Push.StackValue envName = new Push.StackValue();
		envName.setString("current Env");
		pushEnv.addValue(envName);
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		Push pushTrue = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setString("true");
		pushTrue.addValue(trueVal);
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ActionBlock ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		Push pushFalse = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setString("false");
		pushFalse.addValue(falseVal);
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		lessFunc.addAction(new If((short)ifFalseBlock.getSize()));
		java.util.List actions = ifFalseBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		actions = ifTrueBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		
		lessFunc.addAction(new Return());
		
		actionTag.addAction(lessFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//great op
		Push pushGreat = new Push();
		Push.StackValue greatVal = new Push.StackValue();
		greatVal.setString(">");
		pushGreat.addValue(greatVal);
		actionTag.addAction(pushGreat);
		DefineFunction2 greatFunc = new DefineFunction2("",(short)0,params);
		greatFunc.addAction(push0This);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValueOf);
		
		//push onto closure stack
		greatFunc.addAction(pushNull);
		greatFunc.addAction(push1ClosureStack);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushPush);
		greatFunc.addAction(new CallMethod());
		greatFunc.addAction(new Pop());

		greatFunc.addAction(new CallMethod());
		
		//pop from closure stack
		greatFunc.addAction(push0ClosureStack);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushPop);
		greatFunc.addAction(new CallMethod());
		greatFunc.addAction(new Pop());
		
		greatFunc.addAction(push0Other);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValueOf);
		
		//push onto closure stack
		greatFunc.addAction(pushNull);
		greatFunc.addAction(push1ClosureStack);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushPush);
		greatFunc.addAction(new CallMethod());
		greatFunc.addAction(new Pop());
		
		greatFunc.addAction(new CallMethod());
		
		//pop from closure stack
		greatFunc.addAction(push0ClosureStack);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushPop);
		greatFunc.addAction(new CallMethod());
		greatFunc.addAction(new Pop());
		
		greatFunc.addAction(new Greater());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		greatFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifTrueBlock);
		
		greatFunc.addAction(new Return());
		
		actionTag.addAction(greatFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//LEQ op
		Push pushLeq = new Push();
		Push.StackValue leqVal = new Push.StackValue();
		leqVal.setString("<=");
		pushLeq.addValue(leqVal);
		actionTag.addAction(pushLeq);
		DefineFunction2 leqFunc = new DefineFunction2("",(short)0,params);
		leqFunc.addAction(push0This);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		leqFunc.addAction(pushNull);
		leqFunc.addAction(push1ClosureStack);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushPush);
		leqFunc.addAction(new CallMethod());
		leqFunc.addAction(new Pop());
		
		leqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		leqFunc.addAction(push0ClosureStack);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushPop);
		leqFunc.addAction(new CallMethod());
		leqFunc.addAction(new Pop());
		
		leqFunc.addAction(push0Other);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		leqFunc.addAction(pushNull);
		leqFunc.addAction(push1ClosureStack);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushPush);
		leqFunc.addAction(new CallMethod());
		leqFunc.addAction(new Pop());
		
		leqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		leqFunc.addAction(push0ClosureStack);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushPop);
		leqFunc.addAction(new CallMethod());
		leqFunc.addAction(new Pop());
		
		leqFunc.addAction(new Greater());
		leqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		leqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifTrueBlock);
		
		leqFunc.addAction(new Return());
		
		actionTag.addAction(leqFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//GEQ op
		Push pushGeq = new Push();
		Push.StackValue geqVal = new Push.StackValue();
		geqVal.setString(">=");
		pushGeq.addValue(geqVal);
		actionTag.addAction(pushGeq);
		DefineFunction2 geqFunc = new DefineFunction2("",(short)0,params);
		geqFunc.addAction(push0This);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		geqFunc.addAction(pushNull);
		geqFunc.addAction(push1ClosureStack);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushPush);
		geqFunc.addAction(new CallMethod());
		geqFunc.addAction(new Pop());
		
		geqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		geqFunc.addAction(push0ClosureStack);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushPop);
		geqFunc.addAction(new CallMethod());
		geqFunc.addAction(new Pop());
		
		geqFunc.addAction(push0Other);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		geqFunc.addAction(pushNull);
		geqFunc.addAction(push1ClosureStack);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushPush);
		geqFunc.addAction(new CallMethod());
		geqFunc.addAction(new Pop());
		
		geqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		geqFunc.addAction(push0ClosureStack);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushPop);
		geqFunc.addAction(new CallMethod());
		geqFunc.addAction(new Pop());
		
		geqFunc.addAction(new Less2());
		geqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		geqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifTrueBlock);
		
		geqFunc.addAction(new Return());
		
		actionTag.addAction(geqFunc);
		actionTag.addAction(new SetMember());
		
		//prototype
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//eq op
		Push pushEq = new Push();
		Push.StackValue eqVal = new Push.StackValue();
		eqVal.setString("==");
		pushEq.addValue(eqVal);
		actionTag.addAction(pushEq);
		DefineFunction2 eqFunc = new DefineFunction2("",(short)0,params);
		eqFunc.addAction(push0This);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		eqFunc.addAction(pushNull);
		eqFunc.addAction(push1ClosureStack);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushPush);
		eqFunc.addAction(new CallMethod());
		eqFunc.addAction(new Pop());
		
		eqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		eqFunc.addAction(push0ClosureStack);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushPop);
		eqFunc.addAction(new CallMethod());
		eqFunc.addAction(new Pop());
		
		eqFunc.addAction(push0Other);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValueOf);
		
		//push onto closure stack
		eqFunc.addAction(pushNull);
		eqFunc.addAction(push1ClosureStack);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushPush);
		eqFunc.addAction(new CallMethod());
		eqFunc.addAction(new Pop());
		
		eqFunc.addAction(new CallMethod());
		
		//pop from closure stack
		eqFunc.addAction(push0ClosureStack);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushPop);
		eqFunc.addAction(new CallMethod());
		eqFunc.addAction(new Pop());
		
		eqFunc.addAction(new Equals2());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		eqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifTrueBlock);
		
		eqFunc.addAction(new Return());
		
		actionTag.addAction(eqFunc);
		actionTag.addAction(new SetMember());
		
		
		//===
		Push pushEq3 = new Push();
		Push.StackValue eq3Val = new Push.StackValue();
		eq3Val.setString("===");
		pushEq3.addValue(eq3Val);
		DefineFunction2 eq3Func = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"other")});
		
		eq3Func.addAction(pushOther);
		eq3Func.addAction(new GetVariable());
		Push push1This = new Push();
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		eq3Func.addAction(push1This);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushEq);
		
		//push null onto the stack
		eq3Func.addAction(pushNull);
		eq3Func.addAction(push1ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPush);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new CallMethod());
		
		//pop from closure stack
		eq3Func.addAction(push0ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPop);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new Return());
		
		
		//<=>
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushCompare = new Push();
		Push.StackValue compareVal = new Push.StackValue();
		compareVal.setString("<=>");
		pushCompare.addValue(compareVal);
		actionTag.addAction(pushCompare);
		DefineFunction2 compareFunc = new DefineFunction2("",(short)2,new RegisterParam[]{new RegisterParam((short)0,"other")});
		compareFunc.addAction(push0This);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushValueOf);
		
		//push onto closure stack
		compareFunc.addAction(pushNull);
		compareFunc.addAction(push1ClosureStack);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushPush);
		compareFunc.addAction(new CallMethod());
		compareFunc.addAction(new Pop());
		
		compareFunc.addAction(new CallMethod());
		
		//pop from closure stack
		compareFunc.addAction(push0ClosureStack);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushPop);
		compareFunc.addAction(new CallMethod());
		compareFunc.addAction(new Pop());
		
		compareFunc.addAction(new StoreRegister((short)0));
		compareFunc.addAction(push0Other);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushValueOf);
		
		//push onto closure stack
		compareFunc.addAction(pushNull);
		compareFunc.addAction(push1ClosureStack);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushPush);
		compareFunc.addAction(new CallMethod());
		compareFunc.addAction(new Pop());
		
		compareFunc.addAction(new CallMethod());
		
		//pop from closure stack
		compareFunc.addAction(push0ClosureStack);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushPop);
		compareFunc.addAction(new CallMethod());
		compareFunc.addAction(new Pop());
		
		compareFunc.addAction(new StoreRegister((short)1));
		compareFunc.addAction(new Less2());
		
		ActionBlock notLessBlock = new ActionBlock();
		Push pushReg0Reg1 = new Push();
		Push.StackValue reg0Val = new Push.StackValue();
		reg0Val.setRegisterNumber((short)0);
		pushReg0Reg1.addValue(reg0Val);
		Push.StackValue reg1Val = new Push.StackValue();
		reg1Val.setRegisterNumber((short)1);
		pushReg0Reg1.addValue(reg1Val);
		notLessBlock.addAction(pushReg0Reg1);
		notLessBlock.addAction(new Greater());
		
		ActionBlock equalBlock = new ActionBlock();
		Push push01Integer = new Push();
		push01Integer.addValue(zeroVal);
		push01Integer.addValue(oneVal);
		Push.StackValue integerVal = new Push.StackValue();
		integerVal.setString("Integer");
		push01Integer.addValue(integerVal);
		equalBlock.addAction(push01Integer);
		equalBlock.addAction(new NewObject());
		equalBlock.addAction(new Return());
		
		notLessBlock.addAction(new If((short)equalBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(notLessBlock, equalBlock);
		Push push11Integer = new Push();
		push11Integer.addValue(oneVal);
		push11Integer.addValue(oneVal);
		push11Integer.addValue(integerVal);
		notLessBlock.addAction(push11Integer);
		notLessBlock.addAction(new NewObject());
		notLessBlock.addAction(new Return());
		
		compareFunc.addAction(new If((short)notLessBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(compareFunc.getBody(), notLessBlock);
		Push pushMinus11Integer = new Push();
		Push.StackValue minus1Val = new Push.StackValue();
		minus1Val.setInteger(-1);
		pushMinus11Integer.addValue(minus1Val);
		pushMinus11Integer.addValue(oneVal);
		pushMinus11Integer.addValue(integerVal);
		compareFunc.addAction(pushMinus11Integer);
		compareFunc.addAction(new NewObject());
		compareFunc.addAction(new Return());
		
		actionTag.addAction(compareFunc);
		actionTag.addAction(new SetMember());
		
		//String.name = "String"
		Push pushNameString = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameString.addValue(nameVal);
		pushNameString.addValue(stringVal);
		actionTag.addAction(pushNameString);
		actionTag.addAction(new SetMember());
		
		//assign String.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//Object["String"] = String
		actionTag.addAction(new SetMember());
		
		//native string methods
		actionTag.addAction(pushString);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushSplit2 = new Push();
		Push.StackValue split2Val = new Push.StackValue();
		split2Val.setString("split2");
		pushSplit2.addValue(split2Val);
		actionTag.addAction(pushSplit2);
		DefineFunction2 split2Func = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"pattern")});
		Push pushPattern = new Push();
		Push.StackValue patternVal = new Push.StackValue();
		patternVal.setString("pattern");
		pushPattern.addValue(patternVal);
		split2Func.addAction(pushPattern);
		split2Func.addAction(new GetVariable());
		split2Func.addAction(push1This);
		split2Func.addAction(new GetVariable());
		Push pushSplit = new Push();
		Push.StackValue splitVal = new Push.StackValue();
		splitVal.setString("split");
		pushSplit.addValue(splitVal);
		split2Func.addAction(pushSplit);
		split2Func.addAction(new CallMethod());
		split2Func.addAction(new Return());
		actionTag.addAction(split2Func);
		actionTag.addAction(new SetMember());
	}

	protected void generateArrayClass(DoAction actionTag,SymbolTable st){
		//Object["Array"]
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		
		Push pushArrayArray = new Push();
		Push.StackValue arrayVal = new Push.StackValue();
		arrayVal.setString("Array");
		pushArrayArray.addValue(arrayVal);
		pushArrayArray.addValue(arrayVal);
		actionTag.addAction(pushArrayArray);
		actionTag.addAction(new GetVariable());//Object["Array"] = Array
		
		actionTag.addAction(new PushDuplicate());//Array.__proto__ = Object
		actionTag.addAction(new PushDuplicate());//Array.new
		actionTag.addAction(new PushDuplicate());//Array.prototype = new Object()
		actionTag.addAction(new PushDuplicate());//Array.name = "Array"
		actionTag.addAction(new PushDuplicate());//Array.prototype["[]"] = function(index){}
		actionTag.addAction(new PushDuplicate());//Array.prototype["[]="] = function(index,value){}
		actionTag.addAction(new PushDuplicate());//initialize
		actionTag.addAction(new PushDuplicate());//Array.prototype.["native Push"] = Array.prototype.push;
		actionTag.addAction(new PushDuplicate());//<<
		
		//Array.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//new func
		Push pushNew = new Push();
		Push.StackValue newVal = new Push.StackValue();
		newVal.setString("new");
		pushNew.addValue(newVal);
		actionTag.addAction(pushNew);
		DefineFunction2 newFunc = new DefineFunction2("",(short)1,new RegisterParam[0]);
		Push pushIndexArg = new Push();
		Push.StackValue indexVal = new Push.StackValue();
		indexVal.setString("curr Index "+Global.getCounter());
		pushIndexArg.addValue(indexVal);
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushIndexArg.addValue(argVal);
		newFunc.addAction(pushIndexArg);
		newFunc.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		newFunc.addAction(pushLength);
		newFunc.addAction(new GetMember());
		Push push1 = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1.addValue(oneVal);
		newFunc.addAction(push1);
		newFunc.addAction(new Subtract());
		newFunc.addAction(new DefineLocal());
		
		ActionBlock exprBlock = new ActionBlock();
		Push pushIndex = new Push();
		pushIndex.addValue(indexVal);
		exprBlock.addAction(pushIndex);
		exprBlock.addAction(new GetVariable());
		Push push0 = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0.addValue(zeroVal);
		exprBlock.addAction(push0);
		exprBlock.addAction(new Less2());
		
		ActionBlock loopBody = new ActionBlock();
		Push pushArg = new Push();
		pushArg.addValue(argVal);
		loopBody.addAction(pushArg);
		loopBody.addAction(new GetVariable());
		loopBody.addAction(pushIndex);
		loopBody.addAction(new GetVariable());
		loopBody.addAction(new GetMember());
		Push pushIndexIndex = new Push();
		pushIndexIndex.addValue(indexVal);
		pushIndexIndex.addValue(indexVal);
		loopBody.addAction(pushIndexIndex);
		loopBody.addAction(new GetVariable());
		loopBody.addAction(new Decrement());
		loopBody.addAction(new SetVariable());
		loopBody.addAction(new Jump((short)(-1*(new Jump((short)0).getSize()+loopBody.getSize()+new If((short)0).getSize()+exprBlock.getSize()))));
		
		exprBlock.addAction(new If((short)loopBody.getSize()));
		
		rubyToSwf.util.CodeGenUtil.copyTags(newFunc.getBody(), exprBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(newFunc.getBody(), loopBody);
		
		newFunc.addAction(pushArg);
		newFunc.addAction(new GetVariable());
		newFunc.addAction(pushLength);
		newFunc.addAction(new GetMember());
		Push push0Array = new Push();
		push0Array.addValue(zeroVal);
		push0Array.addValue(arrayVal);
		newFunc.addAction(push0Array);
		newFunc.addAction(new NewObject());
		newFunc.addAction(new StoreRegister((short)0));
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		newFunc.addAction(pushInitialize);
		newFunc.addAction(new CallMethod());
		newFunc.addAction(new Pop());
		Push pushReg0 = new Push();
		Push.StackValue reg0Val = new Push.StackValue();
		reg0Val.setRegisterNumber((short)0);
		pushReg0.addValue(reg0Val);
		
		newFunc.addAction(pushReg0);
		Push pushConstructorArray = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorArray.addValue(constructorVal);
		pushConstructorArray.addValue(arrayVal);
		newFunc.addAction(pushConstructorArray);
		newFunc.addAction(new GetVariable());
		newFunc.addAction(new SetMember());
		
		newFunc.addAction(pushReg0);
		newFunc.addAction(new Return());
		
		actionTag.addAction(newFunc);
		actionTag.addAction(new SetMember());
		
		//Array.prototype.__proto__ = new Object()
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushProto0Object = new Push();
		pushProto0Object.addValue(protoVal);
		pushProto0Object.addValue(zeroVal);
		pushProto0Object.addValue(objectVal);
		actionTag.addAction(pushProto0Object);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		
		//Array.name = "Array"
		Push pushNameArray = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameArray.addValue(nameVal);
		pushNameArray.addValue(arrayVal);
		actionTag.addAction(pushNameArray);
		actionTag.addAction(new SetMember());
		
		//Array.prototype["getAt"] = function(index){}
		
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushIndexer = new Push();
		Push.StackValue indexerVal = new Push.StackValue();
		indexerVal.setString("getAt");
		pushIndexer.addValue(indexerVal);
		actionTag.addAction(pushIndexer);
		RegisterParam[] indexerParams = new RegisterParam[1];
		indexerParams[0] = new RegisterParam((short)0, "index");
		DefineFunction2 indexerFunc = new DefineFunction2("",(short)0,indexerParams);
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		indexerFunc.addAction(pushThis);
		indexerFunc.addAction(new GetVariable());
		Push push0Index = new Push();
		push0Index.addValue(zeroVal);
		indexVal = new Push.StackValue();
		indexVal.setString("index");
		push0Index.addValue(indexVal);
		indexerFunc.addAction(push0Index);
		indexerFunc.addAction(new GetVariable());
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		indexerFunc.addAction(pushValueOf);
		
		//push onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		indexerFunc.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		indexerFunc.addAction(push1ClosureStack);
		indexerFunc.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		indexerFunc.addAction(pushPush);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		indexerFunc.addAction(push0ClosureStack);
		indexerFunc.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		indexerFunc.addAction(pushPop);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new GetMember());
		indexerFunc.addAction(new Return());
		actionTag.addAction(indexerFunc);
		actionTag.addAction(new SetMember());
		
		//Array.prototype["setAt"] = function(index,value){}
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushIndexerAssign = new Push();
		Push.StackValue indexerAssignVal = new Push.StackValue();
		indexerAssignVal.setString("setAt");
		pushIndexerAssign.addValue(indexerAssignVal);
		actionTag.addAction(pushIndexerAssign);
		RegisterParam[] indexerAssignParams = new RegisterParam[2];
		indexerAssignParams[0] = new RegisterParam((short)0, "index");
		indexerAssignParams[1] = new RegisterParam((short)0, "value");
		DefineFunction2 indexerAssignFunc = new DefineFunction2("",(short)0,indexerAssignParams);
		indexerAssignFunc.addAction(pushThis);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(push0Index);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushValueOf);
		
		//push onto closure stack
		indexerAssignFunc.addAction(pushNull);
		indexerAssignFunc.addAction(push1ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPush);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerAssignFunc.addAction(push0ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPop);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		Push pushValue = new Push();
		Push.StackValue valueVal = new Push.StackValue();
		valueVal.setString("value");
		pushValue.addValue(valueVal);
		indexerAssignFunc.addAction(pushValue);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(new SetMember());
		indexerAssignFunc.addAction(pushValue);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(new Return());
		actionTag.addAction(indexerAssignFunc);
		actionTag.addAction(new SetMember());
		
		//initialize
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushInitialize);
		DefineFunction2 initializeFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0NilClass = new Push();
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		initializeFunc.addAction(push0NilClass);
		initializeFunc.addAction(new NewObject());
		initializeFunc.addAction(new Return());
		actionTag.addAction(initializeFunc);
		actionTag.addAction(new SetMember());
		
		//Array.prototype["native Push"] = Array.prototype.push
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativePush = new Push();
		Push.StackValue nativePushVal = new Push.StackValue();
		nativePushVal.setString("native Push");
		pushNativePush.addValue(nativePushVal);
		actionTag.addAction(pushNativePush);
		Push pushArray = new Push();
		pushArray.addValue(arrayVal);
		actionTag.addAction(pushArray);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushPush);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//<<
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushAppend = new Push();
		Push.StackValue appendVal = new Push.StackValue();
		appendVal.setString("<<");
		pushAppend.addValue(appendVal);
		actionTag.addAction(pushAppend);
		DefineFunction2 appendFunc = new DefineFunction2("",(short)0,new RegisterParam[]{new RegisterParam((short)0,"anObj")});
		Push pushAnObj = new Push();
		Push.StackValue anObjVal = new Push.StackValue();
		anObjVal.setString("anObj");
		pushAnObj.addValue(anObjVal);
		appendFunc.addAction(pushAnObj);
		appendFunc.addAction(new GetVariable());
		Push push1This = new Push();
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		appendFunc.addAction(push1This);
		appendFunc.addAction(new GetVariable());
		appendFunc.addAction(pushNativePush);
		appendFunc.addAction(new CallMethod());
		appendFunc.addAction(new Pop());
		appendFunc.addAction(pushThis);
		appendFunc.addAction(new GetVariable());
		appendFunc.addAction(new Return());
		actionTag.addAction(appendFunc);
		actionTag.addAction(new SetMember());
		
		//Object["Array"] = Array
		actionTag.addAction(new SetMember());
	}
	
	protected void generateFloatClass(DoAction actionTag,SymbolTable st){
		RegisterParam[] intParams = new RegisterParam[1];
		intParams[0] = new RegisterParam((short)0,"i");
		DefineFunction2 floatClass = new DefineFunction2("Float",(short)0,intParams);
		
		Push pushI = new Push();
		Push.StackValue iVal = new Push.StackValue();
		iVal.setString("i");
		pushI.addValue(iVal);
		floatClass.addAction(pushI);
		floatClass.addAction(new GetVariable());
		floatClass.addAction(new TypeOf());
		Push pushNumber = new Push();
		Push.StackValue numberVal = new Push.StackValue();
		numberVal.setString("number");
		pushNumber.addValue(numberVal);
		floatClass.addAction(pushNumber);
		floatClass.addAction(new Equals2());
		
		ActionBlock notNumberBlock = new ActionBlock();
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		notNumberBlock.addAction(pushThis);
		notNumberBlock.addAction(new GetVariable());
		Push pushValueAttr0I = new Push();
		Push.StackValue valueAttrVal = new Push.StackValue();
		valueAttrVal.setString("@value");
		pushValueAttr0I.addValue(valueAttrVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushValueAttr0I.addValue(zeroVal);
		pushValueAttr0I.addValue(iVal);
		notNumberBlock.addAction(pushValueAttr0I);
		notNumberBlock.addAction(new GetVariable());
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		notNumberBlock.addAction(pushValueOf);
		
		//push onto closure stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		notNumberBlock.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		notNumberBlock.addAction(push1ClosureStack);
		notNumberBlock.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		notNumberBlock.addAction(pushPush);
		notNumberBlock.addAction(new CallMethod());
		notNumberBlock.addAction(new Pop());
		
		notNumberBlock.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		notNumberBlock.addAction(push0ClosureStack);
		notNumberBlock.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		notNumberBlock.addAction(pushPop);
		notNumberBlock.addAction(new CallMethod());
		notNumberBlock.addAction(new Pop());
		
		notNumberBlock.addAction(new SetMember());
		
		ActionBlock numberBlock = new ActionBlock();
		numberBlock.addAction(pushThis);
		numberBlock.addAction(new GetVariable());
		Push pushValueAttrI = new Push();
		pushValueAttrI.addValue(valueAttrVal);
		pushValueAttrI.addValue(iVal);
		numberBlock.addAction(pushValueAttrI);
		numberBlock.addAction(new GetVariable());
		numberBlock.addAction(new SetMember());
		
		notNumberBlock.addAction(new Jump((short)numberBlock.getSize()));
		
		floatClass.addAction(new If((short)notNumberBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(floatClass.getBody(), notNumberBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(floatClass.getBody(), numberBlock);
		
		floatClass.addAction(pushThis);
		floatClass.addAction(new GetVariable());
		Push pushConstructorFloat = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorFloat.addValue(constructorVal);
		Push.StackValue floatVal = new Push.StackValue();
		floatVal.setString("Float");
		pushConstructorFloat.addValue(floatVal);
		floatClass.addAction(pushConstructorFloat);
		floatClass.addAction(new GetVariable());
		floatClass.addAction(new SetMember());
		
		actionTag.addAction(floatClass);
		
		Push pushFloat = new Push();
		pushFloat.addValue(floatVal);
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		Push pushPrototype0Numeric = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype0Numeric.addValue(prototypeValue);
		pushPrototype0Numeric.addValue(zeroVal);
		Push.StackValue numericVal = new Push.StackValue();
		numericVal.setString("Numeric");
		pushPrototype0Numeric.addValue(numericVal);
		actionTag.addAction(pushPrototype0Numeric);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//plus op
		Push pushPlus = new Push();
		Push.StackValue plusVal = new Push.StackValue();
		plusVal.setString("+");
		pushPlus.addValue(plusVal);
		actionTag.addAction(pushPlus);
		RegisterParam[] params = new RegisterParam[1];
		params[0] = new RegisterParam((short)0,"other");
		DefineFunction2 plusFunc = new DefineFunction2("",(short)0,params);
		plusFunc.addAction(pushThis);
		plusFunc.addAction(new GetVariable());
		Push pushValue = new Push();
		pushValue.addValue(valueAttrVal);
		plusFunc.addAction(pushValue);
		plusFunc.addAction(new GetMember());
		Push pushOther = new Push();
		Push.StackValue otherVal = new Push.StackValue();
		otherVal.setString("other");
		pushOther.addValue(otherVal);
		plusFunc.addAction(pushOther);
		plusFunc.addAction(new GetVariable());
		plusFunc.addAction(pushValue);
		plusFunc.addAction(new GetMember());
		plusFunc.addAction(new Add2());
		Push push1Float = new Push();
		Push.StackValue oneValue = new Push.StackValue();
		oneValue.setInteger(1);
		push1Float.addValue(oneValue);
		push1Float.addValue(floatVal);
		plusFunc.addAction(push1Float);
		plusFunc.addAction(new NewObject());
		plusFunc.addAction(new Return());
		actionTag.addAction(plusFunc);
		actionTag.addAction(new SetMember());
		
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//minus op
		Push pushMinus = new Push();
		Push.StackValue minusVal = new Push.StackValue();
		minusVal.setString("-");
		pushMinus.addValue(minusVal);
		actionTag.addAction(pushMinus);
		DefineFunction2 minusFunc = new DefineFunction2("",(short)0,params);
		minusFunc.addAction(pushThis);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(pushValue);
		minusFunc.addAction(new GetMember());
		minusFunc.addAction(pushOther);
		minusFunc.addAction(new GetVariable());
		minusFunc.addAction(pushValue);
		minusFunc.addAction(new GetMember());
		minusFunc.addAction(new Subtract());
		minusFunc.addAction(push1Float);
		minusFunc.addAction(new NewObject());
		minusFunc.addAction(new Return());
		actionTag.addAction(minusFunc);
		actionTag.addAction(new SetMember());
		
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//multiply op
		Push pushMultiply = new Push();
		Push.StackValue multiplyVal = new Push.StackValue();
		multiplyVal.setString("*");
		pushMultiply.addValue(multiplyVal);
		actionTag.addAction(pushMultiply);
		DefineFunction2 multiplyFunc = new DefineFunction2("",(short)0,params);
		multiplyFunc.addAction(pushThis);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(pushValue);
		multiplyFunc.addAction(new GetMember());
		multiplyFunc.addAction(pushOther);
		multiplyFunc.addAction(new GetVariable());
		multiplyFunc.addAction(pushValue);
		multiplyFunc.addAction(new GetMember());
		multiplyFunc.addAction(new Multiply());
		multiplyFunc.addAction(push1Float);
		multiplyFunc.addAction(new NewObject());
		multiplyFunc.addAction(new Return());
		actionTag.addAction(multiplyFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//divide op
		Push pushDivide = new Push();
		Push.StackValue divideVal = new Push.StackValue();
		divideVal.setString("/");
		pushDivide.addValue(divideVal);
		actionTag.addAction(pushDivide);
		DefineFunction2 divideFunc = new DefineFunction2("",(short)0,params);
		divideFunc.addAction(pushThis);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(pushValue);
		divideFunc.addAction(new GetMember());
		divideFunc.addAction(pushOther);
		divideFunc.addAction(new GetVariable());
		divideFunc.addAction(pushValue);
		divideFunc.addAction(new GetMember());
		divideFunc.addAction(new Divide());
		divideFunc.addAction(push1Float);
		divideFunc.addAction(new NewObject());
		divideFunc.addAction(new Return());
		actionTag.addAction(divideFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//power op
		Push pushPower = new Push();
		Push.StackValue powerVal = new Push.StackValue();
		powerVal.setString("**");
		pushPower.addValue(powerVal);
		actionTag.addAction(pushPower);
		DefineFunction2 powerFunc = new DefineFunction2("",(short)0,params);
		powerFunc.addAction(pushOther);
		powerFunc.addAction(new GetVariable());
		powerFunc.addAction(pushValue);
		powerFunc.addAction(new GetMember());
		powerFunc.addAction(pushThis);
		powerFunc.addAction(new GetVariable());
		powerFunc.addAction(pushValue);
		powerFunc.addAction(new GetMember());
		Push push2Math = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2Math.addValue(twoVal);
		Push.StackValue mathVal = new Push.StackValue();
		mathVal.setString("Math");
		push2Math.addValue(mathVal);
		powerFunc.addAction(push2Math);
		powerFunc.addAction(new GetVariable());
		Push pushPow = new Push();
		Push.StackValue powVal = new Push.StackValue();
		powVal.setString("pow");
		pushPow.addValue(powVal);
		powerFunc.addAction(pushPow);
		powerFunc.addAction(new CallMethod());
		powerFunc.addAction(push1Float);
		powerFunc.addAction(new NewObject());
		powerFunc.addAction(new Return());
		actionTag.addAction(powerFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//valueOf()
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		valueOfFunc.addAction(pushThis);
		valueOfFunc.addAction(new GetVariable());
		valueOfFunc.addAction(pushValue);
		valueOfFunc.addAction(new GetMember());
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString()
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		toStringFunc.addAction(pushThis);
		toStringFunc.addAction(new GetVariable());
		toStringFunc.addAction(pushValue);
		toStringFunc.addAction(new GetMember());
		
		toStringFunc.addAction(new Return());
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//compare func, aka <=>
		Push pushCompare = new Push();
		Push.StackValue compareVal = new Push.StackValue();
		compareVal.setString("<=>");
		pushCompare.addValue(compareVal);
		actionTag.addAction(pushCompare);
		
		DefineFunction2 compareFunc = new DefineFunction2("",(short)0,params);
		compareFunc.addAction(pushThis);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushValue);
		compareFunc.addAction(new GetMember());
		compareFunc.addAction(pushOther);
		compareFunc.addAction(new GetVariable());
		compareFunc.addAction(pushOther);
		compareFunc.addAction(new GetMember());
		compareFunc.addAction(new Subtract());

		compareFunc.addAction(push1Float);
		compareFunc.addAction(new NewObject());
		compareFunc.addAction(new Return());
		actionTag.addAction(compareFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//less op
		Push pushLess = new Push();
		Push.StackValue lessVal = new Push.StackValue();
		lessVal.setString("<");
		pushLess.addValue(lessVal);
		actionTag.addAction(pushLess);
		DefineFunction2 lessFunc = new DefineFunction2("",(short)0,params);
		lessFunc.addAction(pushThis);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValue);
		lessFunc.addAction(new GetMember());
		lessFunc.addAction(pushOther);
		lessFunc.addAction(new GetVariable());
		lessFunc.addAction(pushValue);
		lessFunc.addAction(new GetMember());
		lessFunc.addAction(new Less());
		
		ActionBlock ifTrueBlock = new ActionBlock();
		Push pushEnv = new Push();
		Push.StackValue envName = new Push.StackValue();
		envName.setString("current Env");
		pushEnv.addValue(envName);
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		Push pushTrue = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setString("true");
		pushTrue.addValue(trueVal);
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ActionBlock ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		Push pushFalse = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setString("false");
		pushFalse.addValue(falseVal);
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		lessFunc.addAction(new If((short)ifFalseBlock.getSize()));
		java.util.List actions = ifFalseBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		actions = ifTrueBlock.getActions();
		for(int i=0;i<actions.size();i++){
			lessFunc.addAction((Action)actions.get(i));
		}
		
		lessFunc.addAction(new Return());
		
		actionTag.addAction(lessFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//great op
		Push pushGreat = new Push();
		Push.StackValue greatVal = new Push.StackValue();
		greatVal.setString(">");
		pushGreat.addValue(greatVal);
		actionTag.addAction(pushGreat);
		DefineFunction2 greatFunc = new DefineFunction2("",(short)0,params);
		greatFunc.addAction(pushThis);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValue);
		greatFunc.addAction(new GetMember());
		greatFunc.addAction(pushOther);
		greatFunc.addAction(new GetVariable());
		greatFunc.addAction(pushValue);
		greatFunc.addAction(new GetMember());
		greatFunc.addAction(new Greater());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		greatFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(greatFunc.getBody(),ifTrueBlock);
		
		greatFunc.addAction(new Return());
		
		actionTag.addAction(greatFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//LEQ op
		Push pushLeq = new Push();
		Push.StackValue leqVal = new Push.StackValue();
		leqVal.setString("<=");
		pushLeq.addValue(leqVal);
		actionTag.addAction(pushLeq);
		DefineFunction2 leqFunc = new DefineFunction2("",(short)0,params);
		leqFunc.addAction(pushThis);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValue);
		leqFunc.addAction(new GetMember());
		leqFunc.addAction(pushOther);
		leqFunc.addAction(new GetVariable());
		leqFunc.addAction(pushValue);
		leqFunc.addAction(new GetMember());
		leqFunc.addAction(new Greater());
		leqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		leqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(leqFunc.getBody(),ifTrueBlock);
		
		leqFunc.addAction(new Return());
		
		actionTag.addAction(leqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//GEQ op
		Push pushGeq = new Push();
		Push.StackValue geqVal = new Push.StackValue();
		geqVal.setString(">=");
		pushGeq.addValue(geqVal);
		actionTag.addAction(pushGeq);
		DefineFunction2 geqFunc = new DefineFunction2("",(short)0,params);
		geqFunc.addAction(pushThis);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValue);
		geqFunc.addAction(new GetMember());
		geqFunc.addAction(pushOther);
		geqFunc.addAction(new GetVariable());
		geqFunc.addAction(pushValue);
		geqFunc.addAction(new GetMember());
		geqFunc.addAction(new Less());
		geqFunc.addAction(new Not());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		geqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(geqFunc.getBody(),ifTrueBlock);
		
		geqFunc.addAction(new Return());
		
		actionTag.addAction(geqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//eq op
		Push pushEq = new Push();
		Push.StackValue eqVal = new Push.StackValue();
		eqVal.setString("==");
		pushEq.addValue(eqVal);
		actionTag.addAction(pushEq);
		DefineFunction2 eqFunc = new DefineFunction2("",(short)0,params);
		eqFunc.addAction(pushThis);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValue);
		eqFunc.addAction(new GetMember());
		eqFunc.addAction(pushOther);
		eqFunc.addAction(new GetVariable());
		eqFunc.addAction(pushValue);
		eqFunc.addAction(new GetMember());
		eqFunc.addAction(new Equals2());
		
		ifTrueBlock = new ActionBlock();
		ifTrueBlock.addAction(pushEnv);
		ifTrueBlock.addAction(new GetVariable());
		ifTrueBlock.addAction(pushTrue);
		ifTrueBlock.addAction(new GetMember());

		ifFalseBlock = new ActionBlock();
		ifFalseBlock.addAction(pushEnv);
		ifFalseBlock.addAction(new GetVariable());
		ifFalseBlock.addAction(pushFalse);
		ifFalseBlock.addAction(new GetMember());
		ifFalseBlock.addAction(new Jump((short)ifTrueBlock.getSize()));
		
		eqFunc.addAction(new If((short)ifFalseBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifFalseBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(eqFunc.getBody(),ifTrueBlock);
		
		eqFunc.addAction(new Return());
		
		actionTag.addAction(eqFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		Push pushEq3 = new Push();
		Push.StackValue eq3Val = new Push.StackValue();
		eq3Val.setString("===");
		pushEq3.addValue(eq3Val);
		actionTag.addAction(pushEq3);
		
		DefineFunction2 eq3Func = new DefineFunction2("",(short)0,params);
		
		eq3Func.addAction(pushOther);
		eq3Func.addAction(new GetVariable());
		Push push1This = new Push();
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		eq3Func.addAction(push1This);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushEq);
		
		//push null onto the stack
		eq3Func.addAction(pushNull);
		eq3Func.addAction(push1ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPush);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new CallMethod());
		
		//pop from closure stack
		eq3Func.addAction(push0ClosureStack);
		eq3Func.addAction(new GetVariable());
		eq3Func.addAction(pushPop);
		eq3Func.addAction(new CallMethod());
		eq3Func.addAction(new Pop());
		
		eq3Func.addAction(new Return());
		
		actionTag.addAction(eq3Func);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//unary plus
		Push pushUPlus = new Push();
		Push.StackValue uPlusVal = new Push.StackValue();
		uPlusVal.setString("@+");
		pushUPlus.addValue(uPlusVal);
		actionTag.addAction(pushUPlus);
		DefineFunction2 uPlusFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		uPlusFunc.addAction(pushThis);
		uPlusFunc.addAction(new GetVariable());
		uPlusFunc.addAction(pushValue);
		uPlusFunc.addAction(new GetMember());
		uPlusFunc.addAction(push1Float);
		uPlusFunc.addAction(new NewObject());
		uPlusFunc.addAction(new Return());
		actionTag.addAction(uPlusFunc);
		actionTag.addAction(new SetMember());
		
		//push "Float.prototype"
		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//unary minus
		Push pushUMinus = new Push();
		Push.StackValue uMinusVal = new Push.StackValue();
		uMinusVal.setString("@-");
		pushUMinus.addValue(uMinusVal);
		actionTag.addAction(pushUMinus);
		
		DefineFunction2 uMinusFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		uMinusFunc.addAction(pushThis);
		uMinusFunc.addAction(new GetVariable());
		uMinusFunc.addAction(pushValue);
		uMinusFunc.addAction(new GetMember());
		Push.StackValue negOneVal = new Push.StackValue();
		negOneVal.setInteger(-1);
		Push pushNeg1 = new Push();
		pushNeg1.addValue(negOneVal);
		uMinusFunc.addAction(pushNeg1);
		uMinusFunc.addAction(new Multiply());
		uMinusFunc.addAction(push1Float);
		uMinusFunc.addAction(new NewObject());
		uMinusFunc.addAction(new Return());
		
		actionTag.addAction(uMinusFunc);
		actionTag.addAction(new SetMember());
		
		//assign Float to Object["Float"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushFloatFLoat = new Push();
		pushFloatFLoat.addValue(floatVal);
		pushFloatFLoat.addValue(floatVal);
		actionTag.addAction(pushFloatFLoat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());

		actionTag.addAction(pushFloat);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//Float.name = "Float"
		Push pushNameFloat = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameFloat.addValue(nameVal);
		pushNameFloat.addValue(floatVal);
		actionTag.addAction(pushNameFloat);
		actionTag.addAction(new SetMember());
		
		//assign: Float.__proto__ = Numeric
		Push pushProtoNumeric = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoNumeric.addValue(protoVal);
		pushProtoNumeric.addValue(numericVal);
		actionTag.addAction(pushProtoNumeric);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Defines the class TrueClass and its methods.  
	 * Creates an instance of TrueClass and associate it with true
	 * @param actionTag
	 */
	protected void generateTrueClass(DoAction actionTag,SymbolTable st){
		DefineFunction2 trueClass = new DefineFunction2("TrueClass",(short)0,new RegisterParam[0]);
		
		//set constructor attr
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		trueClass.addAction(pushThis);
		trueClass.addAction(new GetVariable());
		Push pushConstructorTrueClass = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorTrueClass.addValue(constructorVal);
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setString("TrueClass");
		pushConstructorTrueClass.addValue(trueVal);
		trueClass.addAction(pushConstructorTrueClass);
		trueClass.addAction(new GetVariable());
		trueClass.addAction(new SetMember());
		
		actionTag.addAction(trueClass);
		
		//push TrueClass.prototype
		Push pushTrue = new Push();
		pushTrue.addValue(trueVal);
		actionTag.addAction(pushTrue);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushTrue2 = new Push();
		Push.StackValue trueVal2 = new Push.StackValue();
		trueVal2.setString("true");
		pushTrue2.addValue(trueVal2);
		toStringFunc.addAction(pushTrue2);
		toStringFunc.addAction(new Return());
		
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//valueOf
		actionTag.addAction(pushTrue);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushBoolTrue = new Push();
		Push.StackValue boolTrueVal = new Push.StackValue();
		boolTrueVal.setBoolean(true);
		pushBoolTrue.addValue(boolTrueVal);
		valueOfFunc.addAction(pushBoolTrue);
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//assign TrueClass to Object["TrueClass"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushTrueTrue = new Push();
		pushTrueTrue.addValue(trueVal);
		pushTrueTrue.addValue(trueVal);
		actionTag.addAction(pushTrueTrue);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());

		actionTag.addAction(pushTrue);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//TrueClass.name = "TrueClass"
		Push pushNameTrue = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameTrue.addValue(nameVal);
		pushNameTrue.addValue(trueVal);
		actionTag.addAction(pushNameTrue);
		actionTag.addAction(new SetMember());
		
		//assign TrueClass.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Defines the class FalseClass and its methods.  
	 * Creates an instance of FalseClass and associate it with false
	 * @param actionTag
	 */
	protected void generateFalseClass(DoAction actionTag,SymbolTable st){
		DefineFunction2 falseClass = new DefineFunction2("FalseClass",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		falseClass.addAction(pushThis);
		falseClass.addAction(new GetVariable());
		Push pushConstructorFalseClass = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorFalseClass.addValue(constructorVal);
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setString("FalseClass");
		pushConstructorFalseClass.addValue(falseVal);
		falseClass.addAction(pushConstructorFalseClass);
		falseClass.addAction(new GetVariable());
		falseClass.addAction(new SetMember());
		
		actionTag.addAction(falseClass);
		
		//push FalseClass.prototype
		Push pushFalse = new Push();
		pushFalse.addValue(falseVal);
		actionTag.addAction(pushFalse);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushFalse2 = new Push();
		Push.StackValue falseVal2 = new Push.StackValue();
		falseVal2.setString("false");
		pushFalse2.addValue(falseVal2);
		toStringFunc.addAction(pushFalse2);
		toStringFunc.addAction(new Return());
		
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//valueOf
		actionTag.addAction(pushFalse);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushBoolFalse = new Push();
		Push.StackValue boolFalseVal = new Push.StackValue();
		boolFalseVal.setBoolean(false);
		pushBoolFalse.addValue(boolFalseVal);
		valueOfFunc.addAction(pushBoolFalse);
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//assign FalseClass to Object["FalseClass"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushFalseFalse = new Push();
		pushFalseFalse.addValue(falseVal);
		pushFalseFalse.addValue(falseVal);
		actionTag.addAction(pushFalseFalse);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());

		actionTag.addAction(pushFalse);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//FalseClass.name = "FalseClass"
		Push pushNameFalse = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameFalse.addValue(nameVal);
		pushNameFalse.addValue(falseVal);
		actionTag.addAction(pushNameFalse);
		actionTag.addAction(new SetMember());
		
		//assign FalseClass.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	/**
	 * Defines the class NilClass and its methods.  
	 * Creates an instance of NilClass and associate it with nil
	 * @param actionTag
	 */
	protected void generateNilClass(DoAction actionTag,SymbolTable st){
		DefineFunction2 nilClass = new DefineFunction2("NilClass",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		nilClass.addAction(pushThis);
		nilClass.addAction(new GetVariable());
		Push pushConstructorNilClass = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorNilClass.addValue(constructorVal);
		Push.StackValue nilVal = new Push.StackValue();
		nilVal.setString("NilClass");
		pushConstructorNilClass.addValue(nilVal);
		nilClass.addAction(pushConstructorNilClass);
		nilClass.addAction(new GetVariable());
		nilClass.addAction(new SetMember());
		
		actionTag.addAction(nilClass);
		
		//push NilClass.prototype
		Push pushNil = new Push();
		pushNil.addValue(nilVal);
		actionTag.addAction(pushNil);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushNil2 = new Push();
		Push.StackValue nilVal2 = new Push.StackValue();
		nilVal2.setString("nil");
		pushNil2.addValue(nilVal2);
		toStringFunc.addAction(pushNil2);
		toStringFunc.addAction(new Return());
		
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		
		//valueOf
		actionTag.addAction(pushNil);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		actionTag.addAction(pushValueOf);
		DefineFunction2 valueOfFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		valueOfFunc.addAction(pushNull);
		valueOfFunc.addAction(new Return());
		actionTag.addAction(valueOfFunc);
		actionTag.addAction(new SetMember());
		
		//assign NilClass to Object["NilClass"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushNilNil = new Push();
		pushNilNil.addValue(nilVal);
		pushNilNil.addValue(nilVal);
		actionTag.addAction(pushNilNil);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());

		actionTag.addAction(pushNil);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//NilClass.name = "NilClass"
		Push pushNameNil = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameNil.addValue(nameVal);
		pushNameNil.addValue(nilVal);
		actionTag.addAction(pushNameNil);
		actionTag.addAction(new SetMember());
		
		//assign NilClass.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateProcClass(DoAction actionTag,SymbolTable st){
		RegisterParam[] procParams = new RegisterParam[1];
		procParams[0] = new RegisterParam((short)0,"binding");
		DefineFunction2 procFunc = new DefineFunction2("Proc",(short)0,procParams);
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		procFunc.addAction(pushThis);
		procFunc.addAction(new GetVariable());
		Push pushBindingBinding = new Push();
		Push.StackValue bindingVal = new Push.StackValue();
		bindingVal.setString("@binding");
		pushBindingBinding.addValue(bindingVal);
		Push.StackValue bindingVal2 = new Push.StackValue();
		bindingVal2.setString("binding");
		pushBindingBinding.addValue(bindingVal2);
		procFunc.addAction(pushBindingBinding);
		procFunc.addAction(new GetVariable());
		procFunc.addAction(new SetMember());
		
		//set constructor attr
		procFunc.addAction(pushThis);
		procFunc.addAction(new GetVariable());
		Push pushConstructorProc = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorProc.addValue(constructorVal);
		Push.StackValue procVal = new Push.StackValue();
		procVal.setString("Proc");
		pushConstructorProc.addValue(procVal);
		procFunc.addAction(pushConstructorProc);
		procFunc.addAction(new GetVariable());
		procFunc.addAction(new SetMember());
		
		actionTag.addAction(procFunc);
		
		//push Proc.prototype
		Push pushProc = new Push();
		pushProc.addValue(procVal);
		actionTag.addAction(pushProc);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeValue = new Push.StackValue();
		prototypeValue.setString("prototype");
		pushPrototype.addValue(prototypeValue);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		
		//toString
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		
		DefineFunction2 toStringFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushProc2 = new Push();
		Push.StackValue procVal2 = new Push.StackValue();
		procVal2.setString("Proc");
		pushProc2.addValue(procVal2);
		toStringFunc.addAction(pushProc2);
		toStringFunc.addAction(new Return());
		
		actionTag.addAction(toStringFunc);
		actionTag.addAction(new SetMember());
		
		//assign Proc to Object["Proc"]
		Push pushObj = new Push();
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushProcProc = new Push();
		pushProcProc.addValue(procVal);
		pushProcProc.addValue(procVal);
		actionTag.addAction(pushProcProc);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());

		actionTag.addAction(pushProc);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		
		//Proc.name = "Proc"
		Push pushNameProc = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameProc.addValue(nameVal);
		pushNameProc.addValue(procVal);
		actionTag.addAction(pushNameProc);
		actionTag.addAction(new SetMember());
		
		//assign Proc.__proto__ = Object
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateRangeClass(DoAction actionTag,SymbolTable st){
		RegisterParam[] rangeParams = new RegisterParam[3];
		rangeParams[0] = new RegisterParam((short)0,"start");
		rangeParams[1] = new RegisterParam((short)0,"end");
		rangeParams[2] = new RegisterParam((short)0,"exclusive");
		DefineFunction2 rangeClass = new DefineFunction2("Range",(short)4,rangeParams);
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		rangeClass.addAction(pushThis);
		rangeClass.addAction(new GetVariable());
		Push pushConstructorRange = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorRange.addValue(constructorVal);
		Push.StackValue rangeVal = new Push.StackValue();
		rangeVal.setString("Range");
		pushConstructorRange.addValue(rangeVal);
		rangeClass.addAction(pushConstructorRange);
		rangeClass.addAction(new GetVariable());
		rangeClass.addAction(new SetMember());
		
		actionTag.addAction(rangeClass);
		
		//set __proto__ to Object
		Push pushRange = new Push();
		pushRange.addValue(rangeVal);
		actionTag.addAction(pushRange);
		actionTag.addAction(new GetVariable());
		Push pushProtoObj = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObj.addValue(protoVal);
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushProtoObj.addValue(objVal);
		actionTag.addAction(pushProtoObj);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//set Range.prototype = new Object();
		actionTag.addAction(pushRange);
		actionTag.addAction(new GetVariable());
		Push pushPrototype0Object = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0Object.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0Object.addValue(zeroVal);
		pushPrototype0Object.addValue(objVal);
		actionTag.addAction(pushPrototype0Object);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		
		//Object["Range"] = Range
		Push pushObj = new Push();
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushRangeRange = new Push();
		pushRangeRange.addValue(rangeVal);
		pushRangeRange.addValue(rangeVal);
		actionTag.addAction(pushRangeRange);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//Range.name = "Range"
		actionTag.addAction(pushRange);
		actionTag.addAction(new GetVariable());
		Push pushNameRange = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameRange.addValue(nameVal);
		pushNameRange.addValue(rangeVal);
		actionTag.addAction(pushNameRange);
		actionTag.addAction(new SetMember());
	}
	
	protected void generateHashClass(DoAction actionTag,SymbolTable st){
		/*
		 * function Hash(){
		 * 	if(arguments.length==0){
		 * 		this["@proc"] = closureStack.peek();
		 * 		this["@defaultObj"] = new NilClass();
		 * 	}else{
		 * 		this["@defaultObj"] = arguments[0];
		 * 		this["@proc"] = closureStack.peek();
		 * 	}
		 * 	this["@keys"] = new Array();
		 * 	this["@hash"] = new Object();
		 * 	this.constructor = Hash;
		 * }
		 */
		DefineFunction2 hashClass = new DefineFunction2("Hash",(short)0,new RegisterParam[0]);
		Push pushArg = new Push();
		Push.StackValue argVal = new Push.StackValue();
		argVal.setString("arguments");
		pushArg.addValue(argVal);
		hashClass.addAction(pushArg);
		hashClass.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		hashClass.addAction(pushLength);
		hashClass.addAction(new GetMember());
		Push push0 = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0.addValue(zeroVal);
		hashClass.addAction(push0);
		hashClass.addAction(new Equals2());
		
		ActionBlock defaultObjBlock = new ActionBlock();
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		defaultObjBlock.addAction(pushThis);
		defaultObjBlock.addAction(new GetVariable());
		Push pushDefaultObjArg = new Push();
		Push.StackValue defaultObjVal = new Push.StackValue();
		defaultObjVal.setString("@defaultObj");
		pushDefaultObjArg.addValue(defaultObjVal);
		pushDefaultObjArg.addValue(argVal);
		defaultObjBlock.addAction(pushDefaultObjArg);
		defaultObjBlock.addAction(new GetVariable());
		defaultObjBlock.addAction(push0);
		defaultObjBlock.addAction(new GetMember());
		defaultObjBlock.addAction(new SetMember());
		defaultObjBlock.addAction(pushThis);
		defaultObjBlock.addAction(new GetVariable());
		Push pushProc0ClosureStack = new Push();
		Push.StackValue procVal = new Push.StackValue();
		procVal.setString("@proc");
		pushProc0ClosureStack.addValue(procVal);
		pushProc0ClosureStack.addValue(zeroVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		pushProc0ClosureStack.addValue(closureStackVal);
		defaultObjBlock.addAction(pushProc0ClosureStack);
		defaultObjBlock.addAction(new GetVariable());
		Push pushPeek = new Push();
		Push.StackValue peekVal = new Push.StackValue();
		peekVal.setString("peek");
		pushPeek.addValue(peekVal);
		defaultObjBlock.addAction(pushPeek);
		defaultObjBlock.addAction(new CallMethod());
		defaultObjBlock.addAction(new SetMember());
		
		ActionBlock procBlock = new ActionBlock();
		procBlock.addAction(pushThis);
		procBlock.addAction(new GetVariable());
		procBlock.addAction(pushProc0ClosureStack);
		procBlock.addAction(new GetVariable());
		procBlock.addAction(pushPeek);
		procBlock.addAction(new CallMethod());
		procBlock.addAction(new SetMember());
		procBlock.addAction(pushThis);
		procBlock.addAction(new GetVariable());
		Push pushDefaultObj0NilClass = new Push();
		pushDefaultObj0NilClass.addValue(defaultObjVal);
		pushDefaultObj0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		pushDefaultObj0NilClass.addValue(nilClassVal);
		procBlock.addAction(pushDefaultObj0NilClass);
		procBlock.addAction(new NewObject());
		procBlock.addAction(new SetMember());
		defaultObjBlock.addAction(new Jump((short)procBlock.getSize()));
		
		hashClass.addAction(new If((short)defaultObjBlock.getSize()));
		
		rubyToSwf.util.CodeGenUtil.copyTags(hashClass.getBody(), defaultObjBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(hashClass.getBody(), procBlock);
		
		hashClass.addAction(pushThis);
		hashClass.addAction(new GetVariable());
		Push pushKeys0Array = new Push();
		Push.StackValue keysVal = new Push.StackValue();
		keysVal.setString("@keys");
		pushKeys0Array.addValue(keysVal);
		pushKeys0Array.addValue(zeroVal);
		Push.StackValue arrayVal = new Push.StackValue();
		arrayVal.setString("Array");
		pushKeys0Array.addValue(arrayVal);
		hashClass.addAction(pushKeys0Array);
		hashClass.addAction(new NewObject());
		hashClass.addAction(new SetMember());
		hashClass.addAction(pushThis);
		hashClass.addAction(new GetVariable());
		Push pushHashAttr0Object = new Push();
		Push.StackValue hashAttrVal = new Push.StackValue();
		hashAttrVal.setString("@hash");
		pushHashAttr0Object.addValue(hashAttrVal);
		pushHashAttr0Object.addValue(zeroVal);
		Push.StackValue objVal = new Push.StackValue();
		objVal.setString("Object");
		pushHashAttr0Object.addValue(objVal);
		hashClass.addAction(pushHashAttr0Object);
		hashClass.addAction(new NewObject());
		hashClass.addAction(new SetMember());
		
		//set constructor attribute
		hashClass.addAction(pushThis);
		hashClass.addAction(new GetVariable());
		Push pushConstructorHash = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorHash.addValue(constructorVal);
		Push.StackValue hashVal = new Push.StackValue();
		hashVal.setString("Hash");
		pushConstructorHash.addValue(hashVal);
		hashClass.addAction(pushConstructorHash);
		hashClass.addAction(new GetVariable());
		hashClass.addAction(new SetMember());
		
		actionTag.addAction(hashClass);
		Push pushHash = new Push();
		pushHash.addValue(hashVal);
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		Push pushObj = new Push();
		pushObj.addValue(objVal);
		actionTag.addAction(pushObj);
		actionTag.addAction(new GetVariable());
		Push pushHashHash = new Push();
		pushHashHash.addValue(hashVal);
		pushHashHash.addValue(hashVal);
		actionTag.addAction(pushHashHash);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		
		actionTag.addAction(pushHash);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		actionTag.addAction(new PushDuplicate());
		
		//assign Hash.name = "Hash"
		Push pushNamePropHash = new Push();
		Push.StackValue namePropVal = new Push.StackValue();
		namePropVal.setString("name");
		pushNamePropHash.addValue(namePropVal);
		pushNamePropHash.addValue(hashVal);
		actionTag.addAction(pushNamePropHash);
		actionTag.addAction(new SetMember());
		
		//assign Hash.__proto__ = Object
		Push pushProtoObject = new Push();
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		/*
		 * if(this["@keys"].include?(index).valueOf()==false){
		 * 	if(this["@proc"] != null){
		 * 		this["@proc"].call(this,index);
		 * 	}
		 * 	return this["@defaultObj"];
		 * }else{
		 * 	return this["@hash"][index.to_s().valueOf()];
		 * }
		 */
		//Hash.prototype["[]"] = function(index){}
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushIndexer = new Push();
		Push.StackValue indexerVal = new Push.StackValue();
		indexerVal.setString("[]");
		pushIndexer.addValue(indexerVal);
		actionTag.addAction(pushIndexer);
		RegisterParam[] indexerParams = new RegisterParam[1];
		indexerParams[0] = new RegisterParam((short)0, "index");
		DefineFunction2 indexerFunc = new DefineFunction2("",(short)0,indexerParams);
		
		Push push0Index = new Push();
		push0Index.addValue(zeroVal);
		Push.StackValue indexVal = new Push.StackValue();
		indexVal.setString("index");
		push0Index.addValue(indexVal);
		indexerFunc.addAction(push0Index);
		indexerFunc.addAction(new GetVariable());
		Push push1This = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1This.addValue(oneVal);
		push1This.addValue(thisVal);
		indexerFunc.addAction(push1This);
		indexerFunc.addAction(new GetVariable());
		Push pushKeysAttr = new Push();
		Push.StackValue keysAttrVal = new Push.StackValue();
		keysAttrVal.setString("@keys");
		pushKeysAttr.addValue(keysAttrVal);
		indexerFunc.addAction(pushKeysAttr);
		indexerFunc.addAction(new GetMember());
		Push pushInclude = new Push();
		Push.StackValue includeVal = new Push.StackValue();
		includeVal.setString("include?");
		pushInclude.addValue(includeVal);
		indexerFunc.addAction(pushInclude);
		
		//push null onto the stack
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		indexerFunc.addAction(pushNull);
		Push push1ClosureStack = new Push();
		push1ClosureStack.addValue(oneVal);
		push1ClosureStack.addValue(closureStackVal);
		indexerFunc.addAction(push1ClosureStack);
		indexerFunc.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		indexerFunc.addAction(pushPush);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new CallMethod());

		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		indexerFunc.addAction(push0ClosureStack);
		indexerFunc.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		indexerFunc.addAction(pushPop);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		Push pushValueOf = new Push();
		Push.StackValue valueOfVal = new Push.StackValue();
		valueOfVal.setString("valueOf");
		pushValueOf.addValue(valueOfVal);
		indexerFunc.addAction(pushValueOf);
		
		//push null onto the stack
		indexerFunc.addAction(pushNull);
		indexerFunc.addAction(push1ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPush);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerFunc.addAction(push0ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPop);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		ActionBlock noKeyBlock = new ActionBlock();
		noKeyBlock.addAction(pushThis);
		noKeyBlock.addAction(new GetVariable());
		Push pushProcAttr = new Push();
		Push.StackValue procAttrVal = new Push.StackValue();
		procAttrVal.setString("@proc");
		pushProcAttr.addValue(procAttrVal);
		noKeyBlock.addAction(pushProcAttr);
		noKeyBlock.addAction(new GetMember());
		noKeyBlock.addAction(pushNull);
		noKeyBlock.addAction(new Equals2());
		
		ActionBlock hasProcBlock = new ActionBlock();
		Push pushIndex = new Push();
		pushIndex.addValue(indexVal);
		hasProcBlock.addAction(pushIndex);
		hasProcBlock.addAction(new GetVariable());
		hasProcBlock.addAction(pushThis);
		hasProcBlock.addAction(new GetVariable());
		Push push2This = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2This.addValue(twoVal);
		push2This.addValue(thisVal);
		hasProcBlock.addAction(push2This);
		hasProcBlock.addAction(new GetVariable());
		hasProcBlock.addAction(pushProcAttr);
		hasProcBlock.addAction(new GetMember());
		Push pushCall = new Push();
		Push.StackValue callVal = new Push.StackValue();
		callVal.setString("call");
		pushCall.addValue(callVal);
		hasProcBlock.addAction(pushCall);
		
		//push null onto the stack
		hasProcBlock.addAction(pushNull);
		hasProcBlock.addAction(push1ClosureStack);
		hasProcBlock.addAction(new GetVariable());
		hasProcBlock.addAction(pushPush);
		hasProcBlock.addAction(new CallMethod());
		hasProcBlock.addAction(new Pop());
		
		hasProcBlock.addAction(new CallMethod());
		
		//pop from closure stack
		hasProcBlock.addAction(push0ClosureStack);
		hasProcBlock.addAction(new GetVariable());
		hasProcBlock.addAction(pushPop);
		hasProcBlock.addAction(new CallMethod());
		hasProcBlock.addAction(new Pop());
		
		hasProcBlock.addAction(new Pop());
		
		noKeyBlock.addAction(new If((short)hasProcBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(noKeyBlock, hasProcBlock);
		
		noKeyBlock.addAction(pushThis);
		noKeyBlock.addAction(new GetVariable());
		Push pushDefaultObjAttr = new Push();
		Push.StackValue defaultObjAttrVal = new Push.StackValue();
		defaultObjAttrVal.setString("@defaultObj");
		pushDefaultObjAttr.addValue(defaultObjAttrVal);
		noKeyBlock.addAction(pushDefaultObjAttr);
		noKeyBlock.addAction(new GetMember());
		noKeyBlock.addAction(new Return());
		
		indexerFunc.addAction(new If((short)noKeyBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(indexerFunc.getBody(), noKeyBlock);
		
		indexerFunc.addAction(pushThis);
		indexerFunc.addAction(new GetVariable());
		Push pushHashAttr = new Push();
		pushHashAttr.addValue(hashAttrVal);
		indexerFunc.addAction(pushHashAttr);
		indexerFunc.addAction(new GetMember());
		Push push00Index = new Push();
		push00Index.addValue(zeroVal);
		push00Index.addValue(zeroVal);
		push00Index.addValue(indexVal);
		indexerFunc.addAction(push00Index);
		indexerFunc.addAction(new GetVariable());
		Push pushToS = new Push();
		Push.StackValue toSVal = new Push.StackValue();
		toSVal.setString("to_s");
		pushToS.addValue(toSVal);
		indexerFunc.addAction(pushToS);
		
		//push null onto the stack
		indexerFunc.addAction(pushNull);
		indexerFunc.addAction(push1ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPush);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
	
		indexerFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerFunc.addAction(push0ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPop);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(pushValueOf);
		
		//push null onto the stack
		indexerFunc.addAction(pushNull);
		indexerFunc.addAction(push1ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPush);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerFunc.addAction(push0ClosureStack);
		indexerFunc.addAction(new GetVariable());
		indexerFunc.addAction(pushPop);
		indexerFunc.addAction(new CallMethod());
		indexerFunc.addAction(new Pop());
		
		indexerFunc.addAction(new GetMember());
		indexerFunc.addAction(new Return());
		
		actionTag.addAction(indexerFunc);
		actionTag.addAction(new SetMember());
		
		//Hash.prototype["[]="] = function(index,value){}
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushIndexerAssign = new Push();
		Push.StackValue indexerAssignVal = new Push.StackValue();
		indexerAssignVal.setString("[]=");
		pushIndexerAssign.addValue(indexerAssignVal);
		actionTag.addAction(pushIndexerAssign);
		RegisterParam[] indexerAssignParams = new RegisterParam[2];
		indexerAssignParams[0] = new RegisterParam((short)0, "index");
		indexerAssignParams[1] = new RegisterParam((short)0, "value");
		DefineFunction2 indexerAssignFunc = new DefineFunction2("",(short)0,indexerAssignParams);
		indexerAssignFunc.addAction(pushThis);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushHashAttr);
		indexerAssignFunc.addAction(new GetMember());
		indexerAssignFunc.addAction(push00Index);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushToS);
		
		//push null onto the stack
		indexerAssignFunc.addAction(pushNull);
		indexerAssignFunc.addAction(push1ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPush);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerAssignFunc.addAction(push0ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPop);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(pushValueOf);
		
		//push null onto the stack
		indexerAssignFunc.addAction(pushNull);
		indexerAssignFunc.addAction(push1ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPush);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerAssignFunc.addAction(push0ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPop);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		Push pushValue = new Push();
		Push.StackValue valueVal = new Push.StackValue();
		valueVal.setString("value");
		pushValue.addValue(valueVal);
		indexerAssignFunc.addAction(pushValue);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(new SetMember());
		
		//check if key exists
		indexerAssignFunc.addAction(push0Index);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(push1This);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushKeysAttr);
		indexerAssignFunc.addAction(new GetMember());
		indexerAssignFunc.addAction(pushInclude);
		
		//push null onto the stack
		indexerAssignFunc.addAction(pushNull);
		indexerAssignFunc.addAction(push1ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPush);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerAssignFunc.addAction(push0ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPop);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(pushValueOf);
		
		//push null onto the stack
		indexerAssignFunc.addAction(pushNull);
		indexerAssignFunc.addAction(push1ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPush);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		indexerAssignFunc.addAction(new CallMethod());
		
		//pop from closure stack
		indexerAssignFunc.addAction(push0ClosureStack);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(pushPop);
		indexerAssignFunc.addAction(new CallMethod());
		indexerAssignFunc.addAction(new Pop());
		
		//append to @keys
		ActionBlock addKeyBlock = new ActionBlock();
		addKeyBlock.addAction(pushIndex);
		addKeyBlock.addAction(new GetVariable());
		addKeyBlock.addAction(push1This);
		addKeyBlock.addAction(new GetVariable());
		Push pushKeys = new Push();
		pushKeys.addValue(keysVal);
		addKeyBlock.addAction(pushKeys);
		addKeyBlock.addAction(new GetMember());
		Push pushNativePush = new Push();
		Push.StackValue nativePushVal = new Push.StackValue();
		nativePushVal.setString("native Push");
		pushNativePush.addValue(nativePushVal);
		addKeyBlock.addAction(pushNativePush);
		addKeyBlock.addAction(new CallMethod());
		addKeyBlock.addAction(new Pop());
		
		indexerAssignFunc.addAction(new If((short)addKeyBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(indexerAssignFunc.getBody(), addKeyBlock);
		
		indexerAssignFunc.addAction(pushValue);
		indexerAssignFunc.addAction(new GetVariable());
		indexerAssignFunc.addAction(new Return());
		actionTag.addAction(indexerAssignFunc);
		actionTag.addAction(new SetMember());
	}
	
	protected void generateMovieClip(DoAction actionTag,SymbolTable st){
		//MovieClip.__proto__ = Object
		Push pushMovieClip = new Push();
		Push.StackValue movieClipVal = new Push.StackValue();
		movieClipVal.setString("MovieClip");
		pushMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//MovieClip.prototype["native GetNextHighestDepth"] = MovieClip.prototype.getNextHighestDepth
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeGetNextHighestDepthMovieClip = new Push();
		Push.StackValue nativeGetNextHighestDepthVal = new Push.StackValue();
		nativeGetNextHighestDepthVal.setString("native GetNextHighestDepth");
		pushNativeGetNextHighestDepthMovieClip.addValue(nativeGetNextHighestDepthVal);
		pushNativeGetNextHighestDepthMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeGetNextHighestDepthMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("getNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		actionTag.addAction(pushGetNextHighestDepth);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//MovieClip.prototype["native CreateEmptyMovieClip"] = MovieClip.prototype.createEmptyMovieClip
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeCreateEmptyMovieClipMovieClip = new Push();
		Push.StackValue nativeCreateEmptyMovieClipVal = new Push.StackValue();
		nativeCreateEmptyMovieClipVal.setString("native CreateEmptyMovieClip");
		pushNativeCreateEmptyMovieClipMovieClip.addValue(nativeCreateEmptyMovieClipVal);
		pushNativeCreateEmptyMovieClipMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeCreateEmptyMovieClipMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushCreateEmptyMovieClip = new Push();
		Push.StackValue createEmptyMovieClipVal = new Push.StackValue();
		createEmptyMovieClipVal.setString("createEmptyMovieClip");
		pushCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		actionTag.addAction(pushCreateEmptyMovieClip);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//MovieClip.prototype["native RemoveMovieClip"] = MovieClip.prototype.removeMovieClip
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeRemoveMovieClipMovieClip = new Push();
		Push.StackValue nativeRemoveMovieClipVal = new Push.StackValue();
		nativeRemoveMovieClipVal.setString("native RemoveMovieClip");
		pushNativeRemoveMovieClipMovieClip.addValue(nativeRemoveMovieClipVal);
		pushNativeRemoveMovieClipMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeRemoveMovieClipMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushRemoveMovieClip = new Push();
		Push.StackValue removeMovieClipVal = new Push.StackValue();
		removeMovieClipVal.setString("removeMovieClip");
		pushRemoveMovieClip.addValue(removeMovieClipVal);
		actionTag.addAction(pushRemoveMovieClip);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//MovieClip.prototype["native LoadMovie"] = MovieClip.prototype.loadMovie
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeLoadMovieMovieClip = new Push();
		Push.StackValue nativeLoadMovieVal = new Push.StackValue();
		nativeLoadMovieVal.setString("native LoadMovie");
		pushNativeLoadMovieMovieClip.addValue(nativeLoadMovieVal);
		pushNativeLoadMovieMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeLoadMovieMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushLoadMovie = new Push();
		Push.StackValue loadMovieVal = new Push.StackValue();
		loadMovieVal.setString("loadMovie");
		pushLoadMovie.addValue(loadMovieVal);
		actionTag.addAction(pushLoadMovie);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//MovieClip.prototype["native CreateTextField"] = MovieClip.prototype.createTextField
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeCreateTextFieldMovieClip = new Push();
		Push.StackValue nativeCreateTextFieldVal = new Push.StackValue();
		nativeCreateTextFieldVal.setString("native CreateTextField");
		pushNativeCreateTextFieldMovieClip.addValue(nativeCreateTextFieldVal);
		pushNativeCreateTextFieldMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeCreateTextFieldMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushCreateTextField = new Push();
		Push.StackValue createTextFieldVal = new Push.StackValue();
		createTextFieldVal.setString("createTextField");
		pushCreateTextField.addValue(createTextFieldVal);
		actionTag.addAction(pushCreateTextField);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		
		//MovieClip.prototype["native AttachMovie"] = MovieClip.prototype.attachMovie
		actionTag.addAction(pushMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushNativeAttachMovieMovieClip = new Push();
		Push.StackValue nativeAttachMovieVal = new Push.StackValue();
		nativeAttachMovieVal.setString("native AttachMovie");
		pushNativeAttachMovieMovieClip.addValue(nativeAttachMovieVal);
		pushNativeAttachMovieMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushNativeAttachMovieMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushAttachMovie = new Push();
		Push.StackValue attachMovieVal = new Push.StackValue();
		attachMovieVal.setString("attachMovie");
		pushAttachMovie.addValue(attachMovieVal);
		actionTag.addAction(pushAttachMovie);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Object["MovieClip"] = MovieClip
		Push pushObject = new Push();
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushMovieClipMovieClip = new Push();
		pushMovieClipMovieClip.addValue(movieClipVal);
		pushMovieClipMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushMovieClipMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateSpriteClass(DoAction actionTag,SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushSprite = new Push();
		Push.StackValue spriteVal = new Push.StackValue();
		spriteVal.setString("Sprite");
		pushSprite.addValue(spriteVal);
		actionTag.addAction(pushSprite);
		DefineFunction2 spriteClass = new DefineFunction2("Sprite",(short)0,new RegisterParam[0]);
		
		//set constructor attr
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		spriteClass.addAction(pushThis);
		spriteClass.addAction(new GetVariable());
		Push pushConstructorSprite = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorSprite.addValue(constructorVal);
		pushConstructorSprite.addValue(spriteVal);
		spriteClass.addAction(pushConstructorSprite);
		spriteClass.addAction(new GetVariable());
		spriteClass.addAction(new SetMember());
		
		actionTag.addAction(spriteClass);
		actionTag.addAction(pushSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());//__proto__
		actionTag.addAction(new PushDuplicate());//prototype
		actionTag.addAction(new PushDuplicate());//name
		actionTag.addAction(new PushDuplicate());//new
		actionTag.addAction(new PushDuplicate());//createInstance
		Push pushProtoMovieClip = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoMovieClip.addValue(protoVal);
		Push.StackValue movieClipVal = new Push.StackValue();
		movieClipVal.setString("MovieClip");
		pushProtoMovieClip.addValue(movieClipVal);
		actionTag.addAction(pushProtoMovieClip);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		Push pushPrototype0MovieClip = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0MovieClip.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0MovieClip.addValue(zeroVal);
		pushPrototype0MovieClip.addValue(movieClipVal);
		actionTag.addAction(pushPrototype0MovieClip);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());		
		Push pushNew = new Push();
		Push.StackValue newVal = new Push.StackValue();
		newVal.setString("new");
		pushNew.addValue(newVal);
		actionTag.addAction(pushNew);
		DefineFunction2 newFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0NilClass = new Push();
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		newFunc.addAction(push0NilClass);
		newFunc.addAction(new NewObject());
		newFunc.addAction(new Return());
		actionTag.addAction(newFunc);
		actionTag.addAction(new SetMember());
		Push pushNameSprite = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameSprite.addValue(nameVal);
		pushNameSprite.addValue(spriteVal);
		actionTag.addAction(pushNameSprite);
		actionTag.addAction(new SetMember());

		//createInstance(parent)
		Push pushCreateInstance = new Push();
		Push.StackValue createInstanceVal = new Push.StackValue();
		createInstanceVal.setString("createInstance");
		pushCreateInstance.addValue(createInstanceVal);
		actionTag.addAction(pushCreateInstance);
		DefineFunction2 createInstanceFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"parent")});
		Push push0Parent = new Push();
		push0Parent.addValue(zeroVal);
		Push.StackValue parentVal = new Push.StackValue();
		parentVal.setString("parent");
		push0Parent.addValue(parentVal);
		createInstanceFunc.addAction(push0Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("native GetNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFunc.addAction(pushGetNextHighestDepth);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushSpriteName = new Push();
		Push.StackValue spriteNameVal = new Push.StackValue();
		spriteNameVal.setString("Sprite Name ");
		pushSpriteName.addValue(spriteNameVal);
		createInstanceFunc.addAction(pushSpriteName);
		createInstanceFunc.addAction(new StackSwap());
		createInstanceFunc.addAction(new Add2());
		Push push2Parent = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2Parent.addValue(twoVal);
		push2Parent.addValue(parentVal);
		createInstanceFunc.addAction(push2Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushCreateEmptyMovieClip = new Push();
		Push.StackValue createEmptyMovieClipVal = new Push.StackValue();
		createEmptyMovieClipVal.setString("native CreateEmptyMovieClip");
		pushCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		createInstanceFunc.addAction(pushCreateEmptyMovieClip);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushProto0Object = new Push();
		pushProto0Object.addValue(protoVal);
		pushProto0Object.addValue(zeroVal);
		pushProto0Object.addValue(objectVal);
		createInstanceFunc.addAction(pushProto0Object);
		createInstanceFunc.addAction(new NewObject());
		createInstanceFunc.addAction(new PushDuplicate());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		pushProtoThis.addValue(thisVal);
		createInstanceFunc.addAction(pushProtoThis);
		createInstanceFunc.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeVal);
		createInstanceFunc.addAction(pushPrototype);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(new SetMember());
		Push pushConstructorThis = new Push();
		pushConstructorThis.addValue(constructorVal);
		pushConstructorThis.addValue(thisVal);
		createInstanceFunc.addAction(pushConstructorThis);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(new SetMember());
		createInstanceFunc.addAction(new SetMember());
		
		
		short regNo = (short)0;//we know that this function only uses 1 register
		createInstanceFunc.addAction(new StoreRegister(regNo));
		createInstanceFunc.addAction(new Pop());
		String counterName = "counter "+Global.getCounter();
		Push pushCounterArguments = new Push();
		Push.StackValue counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		Push.StackValue argumentsVal = new Push.StackValue();
		argumentsVal.setString("arguments");
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFunc.addAction(pushCounterArguments);
		createInstanceFunc.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		Push push1 = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1.addValue(oneVal);
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		createInstanceFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock = new ActionBlock();
		Push pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock.addAction(pushCounter);
		condBlock.addAction(new GetVariable());
		condBlock.addAction(push1);
		condBlock.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock = new ActionBlock();
		Push pushArguments = new Push();
		pushArguments.addValue(argumentsVal);
		bodyBlock.addAction(pushArguments);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(pushCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new GetMember());
		Push pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock.addAction(pushCounterCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new Decrement());
		bodyBlock.addAction(new SetVariable());
		
		condBlock.addAction(new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock.addAction(new Jump((short)-(condBlock.getSize()+bodyBlock.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),condBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),bodyBlock);
		
		createInstanceFunc.addAction(pushArguments);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		Push pushReg = new Push();
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFunc.addAction(pushReg);
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		createInstanceFunc.addAction(pushInitialize);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());
		Push push1Reg1 = new Push();
		push1Reg1.addValue(regNoVal);
		push1Reg1.addValue(oneVal);
		createInstanceFunc.addAction(push1Reg1);
		Push pushKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("Key");
		pushKey.addValue(keyVal);
		createInstanceFunc.addAction(pushKey);
		createInstanceFunc.addAction(new GetVariable());
		Push pushAddListener = new Push();
		Push.StackValue addListenerVal = new Push.StackValue();
		addListenerVal.setString("addListener");
		pushAddListener.addValue(addListenerVal);
		createInstanceFunc.addAction(pushAddListener);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());
		createInstanceFunc.addAction(pushReg);
		createInstanceFunc.addAction(new Return());
		actionTag.addAction(createInstanceFunc);
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetMember());
		
		//set the Sprite instance methods, toString and remove
		
		//toString
		actionTag.addAction(pushSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc2 = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushSpriteInstance = new Push();
		Push.StackValue spriteInstanceVal = new Push.StackValue();
		spriteInstanceVal.setString("Sprite Instance");
		pushSpriteInstance.addValue(spriteInstanceVal);
		toStringFunc2.addAction(pushSpriteInstance);
		toStringFunc2.addAction(new Return());
		actionTag.addAction(toStringFunc2);
		actionTag.addAction(new SetMember());
		
		//remove
		actionTag.addAction(pushSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushRemove = new Push();
		Push.StackValue removeVal = new Push.StackValue();
		removeVal.setString("remove");
		pushRemove.addValue(removeVal);
		actionTag.addAction(pushRemove);
		DefineFunction2 removeFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0This = new Push();
		push0This.addValue(zeroVal);
		push0This.addValue(thisVal);
		removeFunc.addAction(push0This);
		removeFunc.addAction(new GetVariable());
		Push pushRemoveMovieClip = new Push();
		Push.StackValue removeMovieClipVal = new Push.StackValue();
		removeMovieClipVal.setString("native RemoveMovieClip");
		pushRemoveMovieClip.addValue(removeMovieClipVal);
		removeFunc.addAction(pushRemoveMovieClip);
		removeFunc.addAction(new CallMethod());
		removeFunc.addAction(new Pop());
		removeFunc.addAction(push0NilClass);
		removeFunc.addAction(new NewObject());
		removeFunc.addAction(new Return());
		actionTag.addAction(removeFunc);
		actionTag.addAction(new SetMember());
		
		//initialize
		actionTag.addAction(pushSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushInitialize);
		DefineFunction2 initializeFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		initializeFunc.addAction(push0NilClass);
		initializeFunc.addAction(new NewObject());
		initializeFunc.addAction(new Return());
		actionTag.addAction(initializeFunc);
		actionTag.addAction(new SetMember());
	}
	
	protected void generateImageSpriteClass(DoAction actionTag, SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushImageSprite = new Push();
		Push.StackValue imageSpriteVal = new Push.StackValue();
		imageSpriteVal.setString("ImageSprite");
		pushImageSprite.addValue(imageSpriteVal);
		actionTag.addAction(pushImageSprite);
		DefineFunction2 imageSpriteClass = new DefineFunction2("ImageSprite",(short)0,new RegisterParam[0]);
		
		//set constructor attribute
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);		
		imageSpriteClass.addAction(pushThis);
		imageSpriteClass.addAction(new GetVariable());
		Push pushConstructorImageSprite = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorImageSprite.addValue(constructorVal);
		pushConstructorImageSprite.addValue(imageSpriteVal);
		imageSpriteClass.addAction(pushConstructorImageSprite);
		imageSpriteClass.addAction(new GetVariable());
		imageSpriteClass.addAction(new SetMember());
		
		actionTag.addAction(imageSpriteClass);
		actionTag.addAction(pushImageSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());//__proto__
		actionTag.addAction(new PushDuplicate());//prototype
		actionTag.addAction(new PushDuplicate());//name
		actionTag.addAction(new PushDuplicate());//getImageId
		actionTag.addAction(new PushDuplicate());//createInstance
		actionTag.addAction(new PushDuplicate());//createInstanceFromFile
		actionTag.addAction(new PushDuplicate());//createInstanceFromResource
		
		Push pushProtoSprite = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoSprite.addValue(protoVal);
		Push.StackValue spriteVal = new Push.StackValue();
		spriteVal.setString("Sprite");
		pushProtoSprite.addValue(spriteVal);
		actionTag.addAction(pushProtoSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		Push pushPrototype0Sprite = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0Sprite.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0Sprite.addValue(zeroVal);
		pushPrototype0Sprite.addValue(spriteVal);
		actionTag.addAction(pushPrototype0Sprite);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		Push pushNameImageSprite = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameImageSprite.addValue(nameVal);
		pushNameImageSprite.addValue(imageSpriteVal);
		actionTag.addAction(pushNameImageSprite);
		actionTag.addAction(new SetMember());
		Push pushGetImageId = new Push();
		Push.StackValue getImageIdVal = new Push.StackValue();
		getImageIdVal.setString("getImageId");
		pushGetImageId.addValue(getImageIdVal);
		actionTag.addAction(pushGetImageId);
		DefineFunction2 getImageIdFunc = new DefineFunction2("",(short)4,new RegisterParam[0]);
		getImageIdFunc.addAction(pushThis);
		getImageIdFunc.addAction(new GetVariable());
		Push pushImageId = new Push();
		Push.StackValue imageIdVal = new Push.StackValue();
		imageIdVal.setString("@@imageId");
		pushImageId.addValue(imageIdVal);
		getImageIdFunc.addAction(pushImageId);
		getImageIdFunc.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		getImageIdFunc.addAction(pushNull);
		getImageIdFunc.addAction(new Equals2());
		getImageIdFunc.addAction(new Not());
		
		ActionBlock outerIfNullBlock = new ActionBlock();
		Push pushImageSprite1This = new Push();
		pushImageSprite1This.addValue(imageSpriteVal);
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		pushImageSprite1This.addValue(oneVal);
		pushImageSprite1This.addValue(thisVal);
		outerIfNullBlock.addAction(pushImageSprite1This);
		outerIfNullBlock.addAction(new GetVariable());
		Push pushName = new Push();
		pushName.addValue(nameVal);
		outerIfNullBlock.addAction(pushName);
		outerIfNullBlock.addAction(new GetMember());
		Push push1String = new Push();
		push1String.addValue(oneVal);
		Push.StackValue stringVal = new Push.StackValue();
		stringVal.setString("String");
		push1String.addValue(stringVal);
		outerIfNullBlock.addAction(push1String);
		outerIfNullBlock.addAction(new NewObject());
		Push pushIndexOf = new Push();
		Push.StackValue indexOfVal = new Push.StackValue();
		indexOfVal.setString("indexOf");
		pushIndexOf.addValue(indexOfVal);
		outerIfNullBlock.addAction(pushIndexOf);
		outerIfNullBlock.addAction(new CallMethod());
		Push push02This = new Push();
		push02This.addValue(zeroVal);
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push02This.addValue(twoVal);
		push02This.addValue(thisVal);
		outerIfNullBlock.addAction(push02This);
		outerIfNullBlock.addAction(new GetVariable());
		outerIfNullBlock.addAction(pushName);
		outerIfNullBlock.addAction(new GetMember());
		outerIfNullBlock.addAction(push1String);
		outerIfNullBlock.addAction(new NewObject());
		Push pushSubstr = new Push();
		Push.StackValue substrVal = new Push.StackValue();
		substrVal.setString("substr");
		pushSubstr.addValue(substrVal);
		outerIfNullBlock.addAction(pushSubstr);
		outerIfNullBlock.addAction(new CallMethod());
		outerIfNullBlock.addAction(new PushDuplicate());
		Push pushBlank = new Push();
		Push.StackValue blankVal = new Push.StackValue();
		blankVal.setString("");
		pushBlank.addValue(blankVal);
		outerIfNullBlock.addAction(pushBlank);
		outerIfNullBlock.addAction(new Equals2());
		outerIfNullBlock.addAction(new Not());
		Push pushErrMsg = new Push();
		Push.StackValue errMsgVal = new Push.StackValue();
		errMsgVal.setString("Please derive a class named XYZImageSprite, or set the @@imageId class property");
		pushErrMsg.addValue(errMsgVal);
		Throw throwAction = new Throw();
		outerIfNullBlock.addAction(new If((short)(pushErrMsg.getSize() + throwAction.getSize())));
		outerIfNullBlock.addAction(pushErrMsg);
		outerIfNullBlock.addAction(throwAction);
		outerIfNullBlock.addAction(new Return());
		
		getImageIdFunc.addAction(new If((short)outerIfNullBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(getImageIdFunc.getBody(), outerIfNullBlock);
		
		Push push0This = new Push();
		push0This.addValue(zeroVal);
		push0This.addValue(thisVal);
		getImageIdFunc.addAction(push0This);
		getImageIdFunc.addAction(new GetVariable());
		getImageIdFunc.addAction(pushImageId);
		getImageIdFunc.addAction(new GetMember());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		getImageIdFunc.addAction(pushToString);
		
		//push onto closure stack
		getImageIdFunc.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		getImageIdFunc.addAction(push1ClosureStack);
		getImageIdFunc.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		getImageIdFunc.addAction(pushPush);
		getImageIdFunc.addAction(new CallMethod());
		getImageIdFunc.addAction(new Pop());
		
		getImageIdFunc.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		getImageIdFunc.addAction(push0ClosureStack);
		getImageIdFunc.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		getImageIdFunc.addAction(pushPop);
		getImageIdFunc.addAction(new CallMethod());
		getImageIdFunc.addAction(new Pop());
		
		getImageIdFunc.addAction(new Return());
		actionTag.addAction(getImageIdFunc);
		actionTag.addAction(new SetMember());
		
		//createInstance(parent)
		
		Push pushCreateInstance = new Push();
		Push.StackValue createInstanceVal = new Push.StackValue();
		createInstanceVal.setString("createInstance");
		pushCreateInstance.addValue(createInstanceVal);
		actionTag.addAction(pushCreateInstance);
		DefineFunction2 createInstanceFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"parent")});
		Push push0Parent = new Push();
		push0Parent.addValue(zeroVal);
		Push.StackValue parentVal = new Push.StackValue();
		parentVal.setString("parent");
		push0Parent.addValue(parentVal);
		createInstanceFunc.addAction(push0Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("native GetNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFunc.addAction(pushGetNextHighestDepth);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushSpriteName = new Push();
		Push.StackValue spriteNameVal = new Push.StackValue();
		spriteNameVal.setString("Image Sprite Name ");
		pushSpriteName.addValue(spriteNameVal);
		createInstanceFunc.addAction(pushSpriteName);
		createInstanceFunc.addAction(new StackSwap());
		createInstanceFunc.addAction(new Add2());
		
		createInstanceFunc.addAction(push0This);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushGetImageId);
		createInstanceFunc.addAction(new CallMethod());
		
		Push push3Parent = new Push();
		Push.StackValue threeVal = new Push.StackValue();
		threeVal.setInteger(3);
		push3Parent.addValue(threeVal);
		push3Parent.addValue(parentVal);
		createInstanceFunc.addAction(push3Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushAttachMovie = new Push();
		Push.StackValue attachMovieVal = new Push.StackValue();
		attachMovieVal.setString("native AttachMovie");
		pushAttachMovie.addValue(attachMovieVal);
		createInstanceFunc.addAction(pushAttachMovie);
		createInstanceFunc.addAction(new CallMethod());
		
		short regNo = (short)0;//we know that only 1 register is used
		createInstanceFunc.addAction(new StoreRegister(regNo));
		
		Push pushProto0Object = new Push();
		pushProto0Object.addValue(protoVal);
		pushProto0Object.addValue(zeroVal);
		pushProto0Object.addValue(objectVal);
		createInstanceFunc.addAction(pushProto0Object);
		createInstanceFunc.addAction(new NewObject());
		createInstanceFunc.addAction(new PushDuplicate());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		pushProtoThis.addValue(thisVal);
		createInstanceFunc.addAction(pushProtoThis);
		createInstanceFunc.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeVal);
		createInstanceFunc.addAction(pushPrototype);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(new SetMember());
		Push pushConstructorThis = new Push();
		pushConstructorThis.addValue(constructorVal);
		pushConstructorThis.addValue(thisVal);
		createInstanceFunc.addAction(pushConstructorThis);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(new SetMember());
		createInstanceFunc.addAction(new SetMember());
		
		String counterName = "counter "+Global.getCounter();
		Push pushCounterArguments = new Push();
		Push.StackValue counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		Push.StackValue argumentsVal = new Push.StackValue();
		argumentsVal.setString("arguments");
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFunc.addAction(pushCounterArguments);
		createInstanceFunc.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		Push push1 = new Push();
		push1.addValue(oneVal);
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		createInstanceFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock = new ActionBlock();
		Push pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock.addAction(pushCounter);
		condBlock.addAction(new GetVariable());
		condBlock.addAction(push1);
		condBlock.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock = new ActionBlock();
		Push pushArguments = new Push();
		pushArguments.addValue(argumentsVal);
		bodyBlock.addAction(pushArguments);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(pushCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new GetMember());
		Push pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock.addAction(pushCounterCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new Decrement());
		bodyBlock.addAction(new SetVariable());
		
		condBlock.addAction(new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock.addAction(new Jump((short)-(condBlock.getSize()+bodyBlock.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),condBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),bodyBlock);
		
		createInstanceFunc.addAction(pushArguments);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		Push pushReg = new Push();
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFunc.addAction(pushReg);
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		createInstanceFunc.addAction(pushInitialize);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());
		Push pushReg1 = new Push();
		pushReg1.addValue(regNoVal);
		pushReg1.addValue(oneVal);
		createInstanceFunc.addAction(pushReg1);
		Push pushKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("Key");
		pushKey.addValue(keyVal);
		createInstanceFunc.addAction(pushKey);
		createInstanceFunc.addAction(new GetVariable());
		Push pushAddListener = new Push();
		Push.StackValue addListenerVal = new Push.StackValue();
		addListenerVal.setString("addListener");
		pushAddListener.addValue(addListenerVal);
		createInstanceFunc.addAction(pushAddListener);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());
		createInstanceFunc.addAction(pushReg);
		createInstanceFunc.addAction(new Return());

		actionTag.addAction(createInstanceFunc);
		actionTag.addAction(new SetMember());
		
		
		//createInstanceFromFile(url,parent)
		Push pushCreateInstanceFromFile = new Push();
		Push.StackValue createInstanceFromFileVal = new Push.StackValue();
		createInstanceFromFileVal.setString("createInstanceFromFile");
		pushCreateInstanceFromFile.addValue(createInstanceFromFileVal);
		actionTag.addAction(pushCreateInstanceFromFile);
		DefineFunction2 createInstanceFromFileFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"url"),new RegisterParam((short)0,"parent")});
		createInstanceFromFileFunc.addAction(push0Parent);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(pushGetNextHighestDepth);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		createInstanceFromFileFunc.addAction(pushSpriteName);
		createInstanceFromFileFunc.addAction(new StackSwap());
		createInstanceFromFileFunc.addAction(new Add2());
		Push push2Parent = new Push();
		push2Parent.addValue(twoVal);
		push2Parent.addValue(parentVal);
		createInstanceFromFileFunc.addAction(push2Parent);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push pushCreateEmptyMovieClip = new Push();
		Push.StackValue createEmptyMovieClipVal = new Push.StackValue();
		createEmptyMovieClipVal.setString("native CreateEmptyMovieClip");
		pushCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		createInstanceFromFileFunc.addAction(pushCreateEmptyMovieClip);
		createInstanceFromFileFunc.addAction(new CallMethod());
		regNo = (short)0;//we know that only this register is used in this method
		createInstanceFromFileFunc.addAction(new StoreRegister(regNo));
		createInstanceFromFileFunc.addAction(pushProto0Object);
		createInstanceFromFileFunc.addAction(new NewObject());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		createInstanceFromFileFunc.addAction(pushProtoThis);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(pushPrototype);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(new SetMember());
		createInstanceFromFileFunc.addAction(pushConstructorThis);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(new SetMember());
		createInstanceFromFileFunc.addAction(new SetMember());
		//createInstanceFromFileFunc.addAction(new Pop());
		
		Push pushUrl = new Push();
		Push.StackValue urlVal = new Push.StackValue();
		urlVal.setString("url");
		pushUrl.addValue(urlVal);
		createInstanceFromFileFunc.addAction(pushUrl);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push push10RegGetNextHighestDepth = new Push();
		push10RegGetNextHighestDepth.addValue(oneVal);
		push10RegGetNextHighestDepth.addValue(zeroVal);
		push10RegGetNextHighestDepth.addValue(regNoVal);
		push10RegGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFromFileFunc.addAction(push10RegGetNextHighestDepth);
		createInstanceFromFileFunc.addAction(new CallMethod());
		Push pushInnerMovieClip2RegCreateEmptyMovieClip = new Push();
		Push.StackValue innerMovieClipVal = new Push.StackValue();
		innerMovieClipVal.setString("inner MovieClip");
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(innerMovieClipVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(twoVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(regNoVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		createInstanceFromFileFunc.addAction(pushInnerMovieClip2RegCreateEmptyMovieClip);
		createInstanceFromFileFunc.addAction(new CallMethod());
		Push pushLoadMovie = new Push();
		Push.StackValue loadMovieVal = new Push.StackValue();
		loadMovieVal.setString("native LoadMovie");
		pushLoadMovie.addValue(loadMovieVal);
		createInstanceFromFileFunc.addAction(pushLoadMovie);
		createInstanceFromFileFunc.addAction(new CallMethod());
		
		createInstanceFromFileFunc.addAction(new Pop());
		
		counterName = "counter "+Global.getCounter();
		pushCounterArguments = new Push();
		counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFromFileFunc.addAction(pushCounterArguments);		
		createInstanceFromFileFunc.addAction(new GetVariable());		
		createInstanceFromFileFunc.addAction(pushLength);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(push1);
		createInstanceFromFileFunc.addAction(new Subtract());
		
		createInstanceFromFileFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock2 = new ActionBlock();
		pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock2.addAction(pushCounter);
		condBlock2.addAction(new GetVariable());
		Push push2 = new Push();
		push2.addValue(twoVal);
		condBlock2.addAction(push2);
		condBlock2.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock2 = new ActionBlock();
		bodyBlock2.addAction(pushArguments);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(pushCounter);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(new GetMember());
		pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock2.addAction(pushCounterCounter);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(new Decrement());
		bodyBlock2.addAction(new SetVariable());
		
		condBlock2.addAction(new If((short)(bodyBlock2.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock2.addAction(new Jump((short)-(condBlock2.getSize()+bodyBlock2.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFromFileFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromFileFunc.getBody(),condBlock2);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromFileFunc.getBody(),bodyBlock2);
		
		createInstanceFromFileFunc.addAction(pushArguments);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(pushLength);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(push2);
		createInstanceFromFileFunc.addAction(new Subtract());
		
		pushReg = new Push();
		regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFromFileFunc.addAction(pushReg);	
		
		createInstanceFromFileFunc.addAction(pushInitialize);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new Pop());
		
		createInstanceFromFileFunc.addAction(pushReg1);
		createInstanceFromFileFunc.addAction(pushKey);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(pushAddListener);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new Pop());
		createInstanceFromFileFunc.addAction(pushReg);
		createInstanceFromFileFunc.addAction(new Return());

		actionTag.addAction(createInstanceFromFileFunc);
		actionTag.addAction(new SetMember());
		
		
		//createInstanceFromResource(id, parent)
		
		Push pushCreateInstanceFromResource = new Push();
		Push.StackValue createInstanceFromResourceVal = new Push.StackValue();
		createInstanceFromResourceVal.setString("createInstanceFromResource");
		pushCreateInstanceFromResource.addValue(createInstanceFromResourceVal);
		actionTag.addAction(pushCreateInstanceFromResource);
		DefineFunction2 createInstanceFromResourceFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"id"),new RegisterParam((short)0,"parent")});
		createInstanceFromResourceFunc.addAction(push0Parent);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushGetNextHighestDepth);
		createInstanceFromResourceFunc.addAction(new CallMethod());
		createInstanceFromResourceFunc.addAction(new PushDuplicate());
		createInstanceFromResourceFunc.addAction(pushSpriteName);
		createInstanceFromResourceFunc.addAction(new StackSwap());
		createInstanceFromResourceFunc.addAction(new Add2());
		
		Push pushId = new Push();
		Push.StackValue idVal = new Push.StackValue();
		idVal.setString("id");
		pushId.addValue(idVal);
		createInstanceFromResourceFunc.addAction(pushId);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		
		createInstanceFromResourceFunc.addAction(push3Parent);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushAttachMovie);
		createInstanceFromResourceFunc.addAction(new CallMethod());
		
		regNo = (short)0;//only 1 register is used
		
		createInstanceFromResourceFunc.addAction(new StoreRegister(regNo));

		createInstanceFromResourceFunc.addAction(pushProto0Object);
		createInstanceFromResourceFunc.addAction(new NewObject());
		createInstanceFromResourceFunc.addAction(new PushDuplicate());
		createInstanceFromResourceFunc.addAction(new PushDuplicate());
		createInstanceFromResourceFunc.addAction(pushProtoThis);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushPrototype);
		createInstanceFromResourceFunc.addAction(new GetMember());
		createInstanceFromResourceFunc.addAction(new SetMember());
		createInstanceFromResourceFunc.addAction(pushConstructorThis);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(new SetMember());
		createInstanceFromResourceFunc.addAction(new SetMember());
		
		counterName = "counter "+Global.getCounter();
		pushCounterArguments = new Push();
		counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFromResourceFunc.addAction(pushCounterArguments);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushLength);
		createInstanceFromResourceFunc.addAction(new GetMember());
		createInstanceFromResourceFunc.addAction(push1);
		createInstanceFromResourceFunc.addAction(new Subtract());
		createInstanceFromResourceFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock3 = new ActionBlock();
		pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock3.addAction(pushCounter);
		condBlock3.addAction(new GetVariable());
		condBlock3.addAction(push2);
		condBlock3.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock3 = new ActionBlock();
		bodyBlock3.addAction(pushArguments);
		bodyBlock3.addAction(new GetVariable());
		bodyBlock3.addAction(pushCounter);
		bodyBlock3.addAction(new GetVariable());
		bodyBlock3.addAction(new GetMember());
		pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock3.addAction(pushCounterCounter);
		bodyBlock3.addAction(new GetVariable());
		bodyBlock3.addAction(new Decrement());
		bodyBlock3.addAction(new SetVariable());
		
		condBlock3.addAction(new If((short)(bodyBlock3.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock3.addAction(new Jump((short)-(condBlock3.getSize()+bodyBlock3.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFromResourceFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromResourceFunc.getBody(),condBlock3);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromResourceFunc.getBody(),bodyBlock3);
		
		createInstanceFromResourceFunc.addAction(pushArguments);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushLength);
		createInstanceFromResourceFunc.addAction(new GetMember());
		createInstanceFromResourceFunc.addAction(push2);
		createInstanceFromResourceFunc.addAction(new Subtract());
		pushReg = new Push();
		regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFromResourceFunc.addAction(pushReg);
		createInstanceFromResourceFunc.addAction(pushInitialize);
		createInstanceFromResourceFunc.addAction(new CallMethod());
		createInstanceFromResourceFunc.addAction(new Pop());
		pushReg1 = new Push();
		pushReg1.addValue(regNoVal);
		pushReg1.addValue(oneVal);
		createInstanceFromResourceFunc.addAction(pushReg1);
		createInstanceFromResourceFunc.addAction(pushKey);
		createInstanceFromResourceFunc.addAction(new GetVariable());
		createInstanceFromResourceFunc.addAction(pushAddListener);
		createInstanceFromResourceFunc.addAction(new CallMethod());
		createInstanceFromResourceFunc.addAction(new Pop());
		createInstanceFromResourceFunc.addAction(pushReg);
		createInstanceFromResourceFunc.addAction(new Return());
		actionTag.addAction(createInstanceFromResourceFunc);
		actionTag.addAction(new SetMember());

		
		actionTag.addAction(new SetMember());
	}
	
	protected void generateSwfSpriteClass(DoAction actionTag, SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushSwfSprite = new Push();
		Push.StackValue swfSpriteVal = new Push.StackValue();
		swfSpriteVal.setString("SwfSprite");
		pushSwfSprite.addValue(swfSpriteVal);
		actionTag.addAction(pushSwfSprite);
		DefineFunction2 swfSpriteClass = new DefineFunction2("SwfSprite",(short)0,new RegisterParam[0]);
		
		//set constructor attr
		Push pushThis = new Push();
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushThis.addValue(thisVal);
		swfSpriteClass.addAction(pushThis);
		swfSpriteClass.addAction(new GetVariable());
		Push pushConstructorSwfSprite = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorSwfSprite.addValue(constructorVal);
		pushConstructorSwfSprite.addValue(swfSpriteVal);
		swfSpriteClass.addAction(pushConstructorSwfSprite);
		swfSpriteClass.addAction(new GetVariable());
		swfSpriteClass.addAction(new SetMember());
		
		actionTag.addAction(swfSpriteClass);
		actionTag.addAction(pushSwfSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());//__proto__
		actionTag.addAction(new PushDuplicate());//prototype
		actionTag.addAction(new PushDuplicate());//name
		actionTag.addAction(new PushDuplicate());//getSwfFilename
		actionTag.addAction(new PushDuplicate());//createInstance
		actionTag.addAction(new PushDuplicate());//createInstanceFromFile
		
		Push pushProtoSprite = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoSprite.addValue(protoVal);
		Push.StackValue spriteVal = new Push.StackValue();
		spriteVal.setString("Sprite");
		pushProtoSprite.addValue(spriteVal);
		actionTag.addAction(pushProtoSprite);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		Push pushPrototype0Sprite = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype0Sprite.addValue(prototypeVal);
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		pushPrototype0Sprite.addValue(zeroVal);
		pushPrototype0Sprite.addValue(spriteVal);
		actionTag.addAction(pushPrototype0Sprite);
		actionTag.addAction(new NewObject());
		actionTag.addAction(new SetMember());
		Push pushNameImageSprite = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameImageSprite.addValue(nameVal);
		pushNameImageSprite.addValue(swfSpriteVal);
		actionTag.addAction(pushNameImageSprite);
		actionTag.addAction(new SetMember());
		Push pushGetSwfFilename = new Push();
		Push.StackValue getSwfFilenameVal = new Push.StackValue();
		getSwfFilenameVal.setString("getSwfFilename");
		pushGetSwfFilename.addValue(getSwfFilenameVal);
		actionTag.addAction(pushGetSwfFilename);
		DefineFunction2 getSwfFilenameFunc = new DefineFunction2("",(short)4,new RegisterParam[0]);
		getSwfFilenameFunc.addAction(pushThis);
		getSwfFilenameFunc.addAction(new GetVariable());
		Push pushSwfFilename = new Push();
		Push.StackValue swfFilenameVal = new Push.StackValue();
		swfFilenameVal.setString("@@swfFilename");
		pushSwfFilename.addValue(swfFilenameVal);
		getSwfFilenameFunc.addAction(pushSwfFilename);
		getSwfFilenameFunc.addAction(new GetMember());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		getSwfFilenameFunc.addAction(pushNull);
		getSwfFilenameFunc.addAction(new Equals2());
		getSwfFilenameFunc.addAction(new Not());
		
		ActionBlock outerIfNullBlock = new ActionBlock();
		Push pushImageSprite1This = new Push();
		pushImageSprite1This.addValue(swfSpriteVal);
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		pushImageSprite1This.addValue(oneVal);
		pushImageSprite1This.addValue(thisVal);
		outerIfNullBlock.addAction(pushImageSprite1This);
		outerIfNullBlock.addAction(new GetVariable());
		Push pushName = new Push();
		pushName.addValue(nameVal);
		outerIfNullBlock.addAction(pushName);
		outerIfNullBlock.addAction(new GetMember());
		Push push1String = new Push();
		push1String.addValue(oneVal);
		Push.StackValue stringVal = new Push.StackValue();
		stringVal.setString("String");
		push1String.addValue(stringVal);
		outerIfNullBlock.addAction(push1String);
		outerIfNullBlock.addAction(new NewObject());
		Push pushIndexOf = new Push();
		Push.StackValue indexOfVal = new Push.StackValue();
		indexOfVal.setString("indexOf");
		pushIndexOf.addValue(indexOfVal);
		outerIfNullBlock.addAction(pushIndexOf);
		outerIfNullBlock.addAction(new CallMethod());
		Push push02This = new Push();
		push02This.addValue(zeroVal);
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push02This.addValue(twoVal);
		push02This.addValue(thisVal);
		outerIfNullBlock.addAction(push02This);
		outerIfNullBlock.addAction(new GetVariable());
		outerIfNullBlock.addAction(pushName);
		outerIfNullBlock.addAction(new GetMember());
		outerIfNullBlock.addAction(push1String);
		outerIfNullBlock.addAction(new NewObject());
		Push pushSubstr = new Push();
		Push.StackValue substrVal = new Push.StackValue();
		substrVal.setString("substr");
		pushSubstr.addValue(substrVal);
		outerIfNullBlock.addAction(pushSubstr);
		outerIfNullBlock.addAction(new CallMethod());
		outerIfNullBlock.addAction(new PushDuplicate());
		Push pushBlank = new Push();
		Push.StackValue blankVal = new Push.StackValue();
		blankVal.setString("");
		pushBlank.addValue(blankVal);
		outerIfNullBlock.addAction(pushBlank);
		outerIfNullBlock.addAction(new Equals2());
		outerIfNullBlock.addAction(new Not());
		Push pushErrMsg = new Push();
		Push.StackValue errMsgVal = new Push.StackValue();
		errMsgVal.setString("Please derive a class named XYZSwfSprite, or set the @@swfFilename class property");
		pushErrMsg.addValue(errMsgVal);
		Throw throwAction = new Throw();
		outerIfNullBlock.addAction(new If((short)(pushErrMsg.getSize() + throwAction.getSize())));
		outerIfNullBlock.addAction(pushErrMsg);
		outerIfNullBlock.addAction(throwAction);
		Push pushSwf = new Push();
		Push.StackValue swfVal = new Push.StackValue();
		swfVal.setString(".swf");
		pushSwf.addValue(swfVal);
		outerIfNullBlock.addAction(pushSwf);
		outerIfNullBlock.addAction(new Add2());
		outerIfNullBlock.addAction(new Return());
		
		getSwfFilenameFunc.addAction(new If((short)outerIfNullBlock.getSize()));
		rubyToSwf.util.CodeGenUtil.copyTags(getSwfFilenameFunc.getBody(), outerIfNullBlock);
		
		Push push0This = new Push();
		push0This.addValue(zeroVal);
		push0This.addValue(thisVal);
		getSwfFilenameFunc.addAction(push0This);
		getSwfFilenameFunc.addAction(new GetVariable());
		getSwfFilenameFunc.addAction(pushSwfFilename);
		getSwfFilenameFunc.addAction(new GetMember());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		getSwfFilenameFunc.addAction(pushToString);
		
		//push onto closure stack
		getSwfFilenameFunc.addAction(pushNull);
		
		Push push1ClosureStack = new Push();
		push1ClosureStack.addValue(oneVal);
		Push.StackValue closureStackVal = new Push.StackValue();
		closureStackVal.setString("closure Stack");
		push1ClosureStack.addValue(closureStackVal);
		getSwfFilenameFunc.addAction(push1ClosureStack);
		getSwfFilenameFunc.addAction(new GetVariable());
		Push pushPush = new Push();
		Push.StackValue pushVal = new Push.StackValue();
		pushVal.setString("push");
		pushPush.addValue(pushVal);
		getSwfFilenameFunc.addAction(pushPush);
		getSwfFilenameFunc.addAction(new CallMethod());
		getSwfFilenameFunc.addAction(new Pop());
		
		getSwfFilenameFunc.addAction(new CallMethod());
		
		//pop from closure stack
		Push push0ClosureStack = new Push();
		push0ClosureStack.addValue(zeroVal);
		push0ClosureStack.addValue(closureStackVal);
		getSwfFilenameFunc.addAction(push0ClosureStack);
		getSwfFilenameFunc.addAction(new GetVariable());
		Push pushPop = new Push();
		Push.StackValue popVal = new Push.StackValue();
		popVal.setString("pop");
		pushPop.addValue(popVal);
		getSwfFilenameFunc.addAction(pushPop);
		getSwfFilenameFunc.addAction(new CallMethod());
		getSwfFilenameFunc.addAction(new Pop());
		
		getSwfFilenameFunc.addAction(new Return());
		actionTag.addAction(getSwfFilenameFunc);
		actionTag.addAction(new SetMember());
		
		//createInstance(parent)
		//calls createInstanceFromFile(this.getSwfFilename(),parent,...);
		
		Push pushCreateInstance = new Push();
		Push.StackValue createInstanceVal = new Push.StackValue();
		createInstanceVal.setString("createInstance");
		pushCreateInstance.addValue(createInstanceVal);
		actionTag.addAction(pushCreateInstance);
		DefineFunction2 createInstanceFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"parent")});		
		
		String counterName = "counter "+Global.getCounter();
		Push pushCounterArguments = new Push();
		Push.StackValue counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		Push.StackValue argumentsVal = new Push.StackValue();
		argumentsVal.setString("arguments");
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFunc.addAction(pushCounterArguments);
		createInstanceFunc.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		Push push1 = new Push();
		push1.addValue(oneVal);
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		createInstanceFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock = new ActionBlock();
		Push pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock.addAction(pushCounter);
		condBlock.addAction(new GetVariable());
		Push push0 = new Push();
		push0.addValue(zeroVal);
		condBlock.addAction(push0);
		condBlock.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock = new ActionBlock();
		Push pushArguments = new Push();
		pushArguments.addValue(argumentsVal);
		bodyBlock.addAction(pushArguments);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(pushCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new GetMember());
		Push pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock.addAction(pushCounterCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new Decrement());
		bodyBlock.addAction(new SetVariable());
		
		condBlock.addAction(new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock.addAction(new Jump((short)-(condBlock.getSize()+bodyBlock.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),condBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),bodyBlock);
		
		//call getSwfFilename
		createInstanceFunc.addAction(push0This);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushGetSwfFilename);
		createInstanceFunc.addAction(new CallMethod());
		
		createInstanceFunc.addAction(pushArguments);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Add2());
		createInstanceFunc.addAction(pushThis);
		createInstanceFunc.addAction(new GetVariable());
		Push pushCreateInstanceFromFile = new Push();
		Push.StackValue createInstanceFromFileVal = new Push.StackValue();
		createInstanceFromFileVal.setString("createInstanceFromFile");
		pushCreateInstanceFromFile.addValue(createInstanceFromFileVal);
		createInstanceFunc.addAction(pushCreateInstanceFromFile);
		createInstanceFunc.addAction(new CallMethod());
		
		createInstanceFunc.addAction(new Return());
		actionTag.addAction(createInstanceFunc);
		actionTag.addAction(new SetMember());
		
		
		//createInstanceFromFile(url,parent)
		actionTag.addAction(pushCreateInstanceFromFile);
		DefineFunction2 createInstanceFromFileFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"url"),new RegisterParam((short)0,"parent")});
		Push push0Parent = new Push();
		push0Parent.addValue(zeroVal);
		Push.StackValue parentVal = new Push.StackValue();
		parentVal.setString("parent");
		push0Parent.addValue(parentVal);
		createInstanceFromFileFunc.addAction(push0Parent);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("native GetNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFromFileFunc.addAction(pushGetNextHighestDepth);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		Push pushSpriteName = new Push();
		Push.StackValue spriteNameVal = new Push.StackValue();
		spriteNameVal.setString("SwfSprite ");
		pushSpriteName.addValue(spriteNameVal);
		createInstanceFromFileFunc.addAction(pushSpriteName);
		createInstanceFromFileFunc.addAction(new StackSwap());
		createInstanceFromFileFunc.addAction(new Add2());
		Push push2Parent = new Push();
		push2Parent.addValue(twoVal);
		push2Parent.addValue(parentVal);
		createInstanceFromFileFunc.addAction(push2Parent);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push pushCreateEmptyMovieClip = new Push();
		Push.StackValue createEmptyMovieClipVal = new Push.StackValue();
		createEmptyMovieClipVal.setString("native CreateEmptyMovieClip");
		pushCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		createInstanceFromFileFunc.addAction(pushCreateEmptyMovieClip);
		createInstanceFromFileFunc.addAction(new CallMethod());
		short regNo = (short)0;//only this register is used in this method
		createInstanceFromFileFunc.addAction(new StoreRegister(regNo));
		Push pushProto0Object = new Push();
		pushProto0Object.addValue(protoVal);
		pushProto0Object.addValue(zeroVal);
		pushProto0Object.addValue(objectVal);
		createInstanceFromFileFunc.addAction(pushProto0Object);
		createInstanceFromFileFunc.addAction(new NewObject());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		pushProtoThis.addValue(thisVal);
		createInstanceFromFileFunc.addAction(pushProtoThis);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push pushPrototype = new Push();
		pushPrototype.addValue(prototypeVal);
		createInstanceFromFileFunc.addAction(pushPrototype);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(new SetMember());
		Push pushConstructorThis = new Push();
		pushConstructorThis.addValue(constructorVal);
		pushConstructorThis.addValue(thisVal);
		createInstanceFromFileFunc.addAction(pushConstructorThis);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(new SetMember());
		createInstanceFromFileFunc.addAction(new SetMember());
		
		Push pushUrl = new Push();
		Push.StackValue urlVal = new Push.StackValue();
		urlVal.setString("url");
		pushUrl.addValue(urlVal);
		createInstanceFromFileFunc.addAction(pushUrl);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push push10RegGetNextHighestDepth = new Push();
		push10RegGetNextHighestDepth.addValue(oneVal);
		push10RegGetNextHighestDepth.addValue(zeroVal);
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		push10RegGetNextHighestDepth.addValue(regNoVal);
		push10RegGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFromFileFunc.addAction(push10RegGetNextHighestDepth);
		createInstanceFromFileFunc.addAction(new CallMethod());
		Push pushInnerMovieClip2RegCreateEmptyMovieClip = new Push();
		Push.StackValue innerMovieClipVal = new Push.StackValue();
		innerMovieClipVal.setString("inner MovieClip");
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(innerMovieClipVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(twoVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(regNoVal);
		pushInnerMovieClip2RegCreateEmptyMovieClip.addValue(createEmptyMovieClipVal);
		createInstanceFromFileFunc.addAction(pushInnerMovieClip2RegCreateEmptyMovieClip);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new PushDuplicate());
		Push pushLockRootTrue = new Push();
		Push.StackValue lockRootVal = new Push.StackValue();
		lockRootVal.setString("_lockroot");
		pushLockRootTrue.addValue(lockRootVal);
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setBoolean(true);
		pushLockRootTrue.addValue(trueVal);
		createInstanceFromFileFunc.addAction(pushLockRootTrue);
		createInstanceFromFileFunc.addAction(new SetMember());
		Push pushLoadMovie = new Push();
		Push.StackValue loadMovieVal = new Push.StackValue();
		loadMovieVal.setString("native LoadMovie");
		pushLoadMovie.addValue(loadMovieVal);
		createInstanceFromFileFunc.addAction(pushLoadMovie);
		createInstanceFromFileFunc.addAction(new CallMethod());
		
		createInstanceFromFileFunc.addAction(new Pop());
		
		counterName = "counter "+Global.getCounter();
		pushCounterArguments = new Push();
		counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFromFileFunc.addAction(pushCounterArguments);		
		createInstanceFromFileFunc.addAction(new GetVariable());		
		createInstanceFromFileFunc.addAction(pushLength);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(push1);
		createInstanceFromFileFunc.addAction(new Subtract());
		
		createInstanceFromFileFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock2 = new ActionBlock();
		pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock2.addAction(pushCounter);
		condBlock2.addAction(new GetVariable());
		Push push2 = new Push();
		push2.addValue(twoVal);
		condBlock2.addAction(push2);
		condBlock2.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock2 = new ActionBlock();
		bodyBlock2.addAction(pushArguments);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(pushCounter);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(new GetMember());
		pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock2.addAction(pushCounterCounter);
		bodyBlock2.addAction(new GetVariable());
		bodyBlock2.addAction(new Decrement());
		bodyBlock2.addAction(new SetVariable());
		
		condBlock2.addAction(new If((short)(bodyBlock2.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock2.addAction(new Jump((short)-(condBlock2.getSize()+bodyBlock2.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFromFileFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromFileFunc.getBody(),condBlock2);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFromFileFunc.getBody(),bodyBlock2);
		
		createInstanceFromFileFunc.addAction(pushArguments);
		createInstanceFromFileFunc.addAction(new GetVariable());
		createInstanceFromFileFunc.addAction(pushLength);
		createInstanceFromFileFunc.addAction(new GetMember());
		createInstanceFromFileFunc.addAction(push2);
		createInstanceFromFileFunc.addAction(new Subtract());
		
		Push pushReg = new Push();
		regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFromFileFunc.addAction(pushReg);	
		
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		createInstanceFromFileFunc.addAction(pushInitialize);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new Pop());
		
		Push pushReg1 = new Push();
		pushReg1.addValue(regNoVal);
		pushReg1.addValue(oneVal);
		createInstanceFromFileFunc.addAction(pushReg1);
		Push pushKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("Key");
		pushKey.addValue(keyVal);
		createInstanceFromFileFunc.addAction(pushKey);
		createInstanceFromFileFunc.addAction(new GetVariable());
		Push pushAddListener = new Push();
		Push.StackValue addListenerVal = new Push.StackValue();
		addListenerVal.setString("addListener");
		pushAddListener.addValue(addListenerVal);
		createInstanceFromFileFunc.addAction(pushAddListener);
		createInstanceFromFileFunc.addAction(new CallMethod());
		createInstanceFromFileFunc.addAction(new Pop());
		createInstanceFromFileFunc.addAction(pushReg);
		createInstanceFromFileFunc.addAction(new Return());

		actionTag.addAction(createInstanceFromFileFunc);
		actionTag.addAction(new SetMember());
		
		actionTag.addAction(new SetMember());
	}
	
	protected void generateInitKeyMouse(DoAction actionTag, SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		/*
		actionTag.addAction(new PushDuplicate());
		Push pushKeyKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("Key");
		pushKeyKey.addValue(keyVal);
		pushKeyKey.addValue(keyVal);
		actionTag.addAction(pushKeyKey);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		*/
		
		Push pushMouseMouse = new Push();
		Push.StackValue mouseVal = new Push.StackValue();
		mouseVal.setString("Mouse");
		pushMouseMouse.addValue(mouseVal);
		pushMouseMouse.addValue(mouseVal);
		actionTag.addAction(pushMouseMouse);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		/*
		//Key.nativeGetAscii = Key.getAscii
		Push pushKey = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString("Key");
		pushKey.addValue(keyVal);
		actionTag.addAction(pushKey);
		actionTag.addAction(new GetVariable());
		Push pushNativeGetAsciiKey = new Push();
		Push.StackValue nativeGetAsciiVal = new Push.StackValue();
		nativeGetAsciiVal.setString("nativeGetAscii");
		pushNativeGetAsciiKey.addValue(nativeGetAsciiVal);
		pushNativeGetAsciiKey.addValue(keyVal);
		actionTag.addAction(pushNativeGetAsciiKey);
		actionTag.addAction(new GetVariable());
		Push pushGetAscii = new Push();
		Push.StackValue getAsciiVal = new Push.StackValue();
		getAsciiVal.setString("getAscii");
		pushGetAscii.addValue(getAsciiVal);
		actionTag.addAction(pushGetAscii);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Key.nativeGetCode = Key.getCode
		actionTag.addAction(pushKey);
		actionTag.addAction(new GetVariable());
		Push pushNativeGetCodeKey = new Push();
		Push.StackValue nativeGetCodeVal = new Push.StackValue();
		nativeGetCodeVal.setString("nativeGetCode");
		pushNativeGetCodeKey.addValue(nativeGetCodeVal);
		pushNativeGetCodeKey.addValue(keyVal);
		actionTag.addAction(pushNativeGetCodeKey);
		actionTag.addAction(new GetVariable());
		Push pushGetCode = new Push();
		Push.StackValue getCodeVal = new Push.StackValue();
		getCodeVal.setString("getCode");
		pushGetCode.addValue(getCodeVal);
		actionTag.addAction(pushGetCode);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Key.nativeIsDown = Key.isDown
		actionTag.addAction(pushKey);
		actionTag.addAction(new GetVariable());
		Push pushNativeIsDownKey = new Push();
		Push.StackValue nativeIsDownVal = new Push.StackValue();
		nativeIsDownVal.setString("nativeIsDown");
		pushNativeIsDownKey.addValue(nativeIsDownVal);
		pushNativeIsDownKey.addValue(keyVal);
		actionTag.addAction(pushNativeIsDownKey);
		actionTag.addAction(new GetVariable());
		Push pushIsDown = new Push();
		Push.StackValue isDownVal = new Push.StackValue();
		isDownVal.setString("isDown");
		pushIsDown.addValue(isDownVal);
		actionTag.addAction(pushIsDown);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Key.nativeIsToggled = Key.isToggled
		actionTag.addAction(pushKey);
		actionTag.addAction(new GetVariable());
		Push pushNativeIsToggledKey = new Push();
		Push.StackValue nativeIsToggledVal = new Push.StackValue();
		nativeIsToggledVal.setString("nativeIsToggled");
		pushNativeIsToggledKey.addValue(nativeIsToggledVal);
		pushNativeIsToggledKey.addValue(keyVal);
		actionTag.addAction(pushNativeIsToggledKey);
		actionTag.addAction(new GetVariable());
		Push pushIsToggled = new Push();
		Push.StackValue isToggledVal = new Push.StackValue();
		isToggledVal.setString("isToggled");
		pushIsToggled.addValue(isToggledVal);
		actionTag.addAction(pushIsToggled);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Key.nativeRemoveListener = Key.removeListener
		actionTag.addAction(pushKey);
		actionTag.addAction(new GetVariable());
		Push pushNativeRemoveListenerKey = new Push();
		Push.StackValue nativeRemoveListenerVal = new Push.StackValue();
		nativeRemoveListenerVal.setString("nativeRemoveListener");
		pushNativeRemoveListenerKey.addValue(nativeRemoveListenerVal);
		pushNativeRemoveListenerKey.addValue(keyVal);
		actionTag.addAction(pushNativeRemoveListenerKey);
		actionTag.addAction(new GetVariable());
		Push pushRemoveListener = new Push();
		Push.StackValue removeListenerVal = new Push.StackValue();
		removeListenerVal.setString("removeListener");
		pushRemoveListener.addValue(removeListenerVal);
		actionTag.addAction(pushRemoveListener);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		*/
		
		
		//Mouse.nativeHide = Mouse.hide
		Push pushMouse = new Push();
		pushMouse.addValue(mouseVal);
		actionTag.addAction(pushMouse);
		actionTag.addAction(new GetVariable());
		Push pushNativeHideMouse = new Push();
		Push.StackValue nativeHideVal = new Push.StackValue();
		nativeHideVal.setString("nativeHide");
		pushNativeHideMouse.addValue(nativeHideVal);
		pushNativeHideMouse.addValue(mouseVal);
		actionTag.addAction(pushNativeHideMouse);
		actionTag.addAction(new GetVariable());
		Push pushHide = new Push();
		Push.StackValue hideVal = new Push.StackValue();
		hideVal.setString("hide");
		pushHide.addValue(hideVal);
		actionTag.addAction(pushHide);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Mouse.nativeRemoveListener = Mouse.removeListener
		actionTag.addAction(pushMouse);
		actionTag.addAction(new GetVariable());
		Push pushNativeRemoveListenerMouse = new Push();
		Push.StackValue nativeRemoveListenerVal = new Push.StackValue();
		nativeRemoveListenerVal.setString("nativeRemoveListener");
		pushNativeRemoveListenerMouse.addValue(nativeRemoveListenerVal);
		pushNativeRemoveListenerMouse.addValue(mouseVal);
		actionTag.addAction(pushNativeRemoveListenerMouse);
		actionTag.addAction(new GetVariable());
		Push pushRemoveListener = new Push();
		Push.StackValue removeListenerVal = new Push.StackValue();
		removeListenerVal.setString("removeListener");
		pushRemoveListener.addValue(removeListenerVal);
		actionTag.addAction(pushRemoveListener);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
		
		//Mouse.nativeShow = Mouse.show
		actionTag.addAction(pushMouse);
		actionTag.addAction(new GetVariable());
		Push pushNativeShowMouse = new Push();
		Push.StackValue nativeShowVal = new Push.StackValue();
		nativeShowVal.setString("nativeShow");
		pushNativeShowMouse.addValue(nativeShowVal);
		pushNativeShowMouse.addValue(mouseVal);
		actionTag.addAction(pushNativeShowMouse);
		actionTag.addAction(new GetVariable());
		Push pushShow = new Push();
		Push.StackValue showVal = new Push.StackValue();
		showVal.setString("show");
		pushShow.addValue(showVal);
		actionTag.addAction(pushShow);
		actionTag.addAction(new GetMember());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateInitSound(DoAction actionTag, SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushSoundSound = new Push();
		Push.StackValue soundVal = new Push.StackValue();
		soundVal.setString("Sound");
		pushSoundSound.addValue(soundVal);
		pushSoundSound.addValue(soundVal);
		actionTag.addAction(pushSoundSound);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
		
		//Sound.__proto__ = Object
		Push pushSound = new Push();
		pushSound.addValue(soundVal);
		actionTag.addAction(pushSound);
		actionTag.addAction(new GetVariable());
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());
	}
	
	protected void generateTextField(DoAction actionTag,SymbolTable st){
		Push pushObject = new Push();
		Push.StackValue objectVal = new Push.StackValue();
		objectVal.setString("Object");
		pushObject.addValue(objectVal);
		actionTag.addAction(pushObject);
		actionTag.addAction(new GetVariable());
		Push pushTextField = new Push();
		Push.StackValue textFieldVal = new Push.StackValue();
		textFieldVal.setString("TextField");
		pushTextField.addValue(textFieldVal);
		actionTag.addAction(pushTextField);
		actionTag.addAction(pushTextField);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new PushDuplicate());//__proto__
		actionTag.addAction(new PushDuplicate());//name
		actionTag.addAction(new PushDuplicate());//new
		actionTag.addAction(new PushDuplicate());//createInstance
		Push pushProtoObject = new Push();
		Push.StackValue protoVal = new Push.StackValue();
		protoVal.setString("__proto__");
		pushProtoObject.addValue(protoVal);
		pushProtoObject.addValue(objectVal);
		actionTag.addAction(pushProtoObject);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(new SetMember());		
		Push pushNew = new Push();
		Push.StackValue newVal = new Push.StackValue();
		newVal.setString("new");
		pushNew.addValue(newVal);
		actionTag.addAction(pushNew);
		DefineFunction2 newFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0NilClass = new Push();
		Push.StackValue zeroVal = new Push.StackValue();
		zeroVal.setInteger(0);
		push0NilClass.addValue(zeroVal);
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		push0NilClass.addValue(nilClassVal);
		newFunc.addAction(push0NilClass);
		newFunc.addAction(new NewObject());
		newFunc.addAction(new Return());
		actionTag.addAction(newFunc);
		actionTag.addAction(new SetMember());
		Push pushNameTextField = new Push();
		Push.StackValue nameVal = new Push.StackValue();
		nameVal.setString("name");
		pushNameTextField.addValue(nameVal);
		pushNameTextField.addValue(textFieldVal);
		actionTag.addAction(pushNameTextField);
		actionTag.addAction(new SetMember());

		//createInstance(parent)
		Push pushCreateInstance = new Push();
		Push.StackValue createInstanceVal = new Push.StackValue();
		createInstanceVal.setString("createInstance");
		pushCreateInstance.addValue(createInstanceVal);
		actionTag.addAction(pushCreateInstance);
		DefineFunction2 createInstanceFunc = new DefineFunction2("",(short)4,new RegisterParam[]{new RegisterParam((short)0,"parent")});
		Push push30100000Parent = new Push();
		Push.StackValue thirtyVal = new Push.StackValue();
		thirtyVal.setInteger(30);
		push30100000Parent.addValue(thirtyVal);
		Push.StackValue hundredVal = new Push.StackValue();
		hundredVal.setInteger(100);
		push30100000Parent.addValue(hundredVal);
		push30100000Parent.addValue(zeroVal);
		push30100000Parent.addValue(zeroVal);
		push30100000Parent.addValue(zeroVal);
		Push.StackValue parentVal = new Push.StackValue();
		parentVal.setString("parent");
		push30100000Parent.addValue(parentVal);
		createInstanceFunc.addAction(push30100000Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushGetNextHighestDepth = new Push();
		Push.StackValue getNextHighestDepthVal = new Push.StackValue();
		getNextHighestDepthVal.setString("native GetNextHighestDepth");
		pushGetNextHighestDepth.addValue(getNextHighestDepthVal);
		createInstanceFunc.addAction(pushGetNextHighestDepth);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushTextFieldName = new Push();
		Push.StackValue textFieldNameVal = new Push.StackValue();
		textFieldNameVal.setString("TextField Name ");
		pushTextFieldName.addValue(textFieldNameVal);
		createInstanceFunc.addAction(pushTextFieldName);
		createInstanceFunc.addAction(new StackSwap());
		createInstanceFunc.addAction(new Add2());
		short regNo = (short)0;//only this register is used in this method
		createInstanceFunc.addAction(new StoreRegister(regNo));
		
		Push push6Parent = new Push();
		Push.StackValue sixVal = new Push.StackValue();
		sixVal.setInteger(6);
		push6Parent.addValue(sixVal);
		push6Parent.addValue(parentVal);
		createInstanceFunc.addAction(push6Parent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushCreateTextField = new Push();
		Push.StackValue createTextFieldVal = new Push.StackValue();
		createTextFieldVal.setString("native CreateTextField");
		pushCreateTextField.addValue(createTextFieldVal);
		createInstanceFunc.addAction(pushCreateTextField);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());
		Push pushParent = new Push();
		pushParent.addValue(parentVal);
		createInstanceFunc.addAction(pushParent);
		createInstanceFunc.addAction(new GetVariable());
		Push pushReg = new Push();
		Push.StackValue regNoVal = new Push.StackValue();
		regNoVal.setRegisterNumber(regNo);
		pushReg.addValue(regNoVal);
		createInstanceFunc.addAction(pushReg);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(new StoreRegister(regNo));
		
		Push pushProto0Object = new Push();
		pushProto0Object.addValue(protoVal);
		pushProto0Object.addValue(zeroVal);
		pushProto0Object.addValue(objectVal);
		createInstanceFunc.addAction(pushProto0Object);
		createInstanceFunc.addAction(new NewObject());
		createInstanceFunc.addAction(new PushDuplicate());
		createInstanceFunc.addAction(new PushDuplicate());
		Push pushProtoThis = new Push();
		pushProtoThis.addValue(protoVal);
		Push.StackValue thisVal = new Push.StackValue();
		thisVal.setString("this");
		pushProtoThis.addValue(thisVal);
		createInstanceFunc.addAction(pushProtoThis);
		createInstanceFunc.addAction(new GetVariable());
		Push pushPrototype = new Push();
		Push.StackValue prototypeVal = new Push.StackValue();
		prototypeVal.setString("prototype");
		pushPrototype.addValue(prototypeVal);
		createInstanceFunc.addAction(pushPrototype);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(new SetMember());
		Push pushConstructorThis = new Push();
		Push.StackValue constructorVal = new Push.StackValue();
		constructorVal.setString("constructor");
		pushConstructorThis.addValue(constructorVal);
		pushConstructorThis.addValue(thisVal);
		createInstanceFunc.addAction(pushConstructorThis);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(new SetMember());
		createInstanceFunc.addAction(new SetMember());
		
		String counterName = "counter "+Global.getCounter();
		Push pushCounterArguments = new Push();
		Push.StackValue counterVal = new Push.StackValue();
		counterVal.setString(counterName);
		pushCounterArguments.addValue(counterVal);
		Push.StackValue argumentsVal = new Push.StackValue();
		argumentsVal.setString("arguments");
		pushCounterArguments.addValue(argumentsVal);
		createInstanceFunc.addAction(pushCounterArguments);
		createInstanceFunc.addAction(new GetVariable());
		Push pushLength = new Push();
		Push.StackValue lengthVal = new Push.StackValue();
		lengthVal.setString("length");
		pushLength.addValue(lengthVal);
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		Push push1 = new Push();
		Push.StackValue oneVal = new Push.StackValue();
		oneVal.setInteger(1);
		push1.addValue(oneVal);
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		createInstanceFunc.addAction(new DefineLocal());
		
		//condition for while loop
		ActionBlock condBlock = new ActionBlock();
		Push pushCounter = new Push();
		pushCounter.addValue(counterVal);
		condBlock.addAction(pushCounter);
		condBlock.addAction(new GetVariable());
		condBlock.addAction(push1);
		condBlock.addAction(new Less2());
		
		//loop body
		ActionBlock bodyBlock = new ActionBlock();
		Push pushArguments = new Push();
		pushArguments.addValue(argumentsVal);
		bodyBlock.addAction(pushArguments);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(pushCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new GetMember());
		Push pushCounterCounter = new Push();
		pushCounterCounter.addValue(counterVal);
		pushCounterCounter.addValue(counterVal);
		bodyBlock.addAction(pushCounterCounter);
		bodyBlock.addAction(new GetVariable());
		bodyBlock.addAction(new Decrement());
		bodyBlock.addAction(new SetVariable());
		
		condBlock.addAction(new If((short)(bodyBlock.getSize() + new Jump((short)0).getSize())));
		
		bodyBlock.addAction(new Jump((short)-(condBlock.getSize()+bodyBlock.getSize()+new Jump((short)0).getSize())));
		
		//attach back to createInstanceFunc
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),condBlock);
		rubyToSwf.util.CodeGenUtil.copyTags(createInstanceFunc.getBody(),bodyBlock);
		
		createInstanceFunc.addAction(pushArguments);
		createInstanceFunc.addAction(new GetVariable());
		createInstanceFunc.addAction(pushLength);
		createInstanceFunc.addAction(new GetMember());
		createInstanceFunc.addAction(push1);
		createInstanceFunc.addAction(new Subtract());
		createInstanceFunc.addAction(pushReg);
		Push pushInitialize = new Push();
		Push.StackValue initializeVal = new Push.StackValue();
		initializeVal.setString("initialize");
		pushInitialize.addValue(initializeVal);
		createInstanceFunc.addAction(pushInitialize);
		createInstanceFunc.addAction(new CallMethod());
		createInstanceFunc.addAction(new Pop());

		createInstanceFunc.addAction(pushReg);
		createInstanceFunc.addAction(new Return());

		actionTag.addAction(createInstanceFunc);
		actionTag.addAction(new SetMember());
		actionTag.addAction(new SetMember());
		
		//set the Sprite instance methods, toString and remove
		
		//toString
		actionTag.addAction(pushTextField);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushToString = new Push();
		Push.StackValue toStringVal = new Push.StackValue();
		toStringVal.setString("toString");
		pushToString.addValue(toStringVal);
		actionTag.addAction(pushToString);
		DefineFunction2 toStringFunc2 = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push pushTextFieldInstance = new Push();
		Push.StackValue textFieldInstanceVal = new Push.StackValue();
		textFieldInstanceVal.setString("TextField Instance");
		pushTextFieldInstance.addValue(textFieldInstanceVal);
		toStringFunc2.addAction(pushTextFieldInstance);
		toStringFunc2.addAction(new Return());
		actionTag.addAction(toStringFunc2);
		actionTag.addAction(new SetMember());
		
		//remove
		actionTag.addAction(pushTextField);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		Push pushRemove = new Push();
		Push.StackValue removeVal = new Push.StackValue();
		removeVal.setString("remove");
		pushRemove.addValue(removeVal);
		actionTag.addAction(pushRemove);
		DefineFunction2 removeFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		Push push0This = new Push();
		push0This.addValue(zeroVal);
		push0This.addValue(thisVal);
		removeFunc.addAction(push0This);
		removeFunc.addAction(new GetVariable());
		Push pushRemoveMovieClip = new Push();
		Push.StackValue removeMovieClipVal = new Push.StackValue();
		removeMovieClipVal.setString("native RemoveMovieClip");
		pushRemoveMovieClip.addValue(removeMovieClipVal);
		removeFunc.addAction(pushRemoveMovieClip);
		removeFunc.addAction(new CallMethod());
		removeFunc.addAction(push0NilClass);
		removeFunc.addAction(new NewObject());
		removeFunc.addAction(new Return());
		actionTag.addAction(removeFunc);
		actionTag.addAction(new SetMember());
		
		//initialize
		actionTag.addAction(pushTextField);
		actionTag.addAction(new GetVariable());
		actionTag.addAction(pushPrototype);
		actionTag.addAction(new GetMember());
		actionTag.addAction(pushInitialize);
		DefineFunction2 initializeFunc = new DefineFunction2("",(short)0,new RegisterParam[0]);
		initializeFunc.addAction(push0NilClass);
		initializeFunc.addAction(new NewObject());
		initializeFunc.addAction(new Return());
		actionTag.addAction(initializeFunc);
		actionTag.addAction(new SetMember());
	}
}