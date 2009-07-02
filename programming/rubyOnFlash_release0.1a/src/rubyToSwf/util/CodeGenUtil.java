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

package rubyToSwf.util;

import com.jswiff.*;
import com.jswiff.swfrecords.*;
import com.jswiff.swfrecords.tags.*;
import com.jswiff.swfrecords.actions.*;

/**
 * This class contains utility methods for code generation
 * @author Lem Hongjian
 *
 */
public class CodeGenUtil {
	
	/**
	 * The code sequence can be visualized as such:
	 * --------------
	 * | NullBlock  |- checks if object on stack is null, if so, jump to toFalse block
	 * --------------
	 * | NilClass   |- checks if value on stack is an instance of NilClass, if so, jump to toFalse block
	 * --------------
	 * | FalseClass |- checks if value on stack is an instance of FalseClass, if so, jump to the toFalse block
	 * --------------
	 * | toTrue     |- pushes a true value, jumps after toFalse block
	 * --------------
	 * | toFalse    |- pushes a false value
	 */
	
	public static void genCheckNullNilFalse(DoAction actionTag){
		//toFalse block
		ActionBlock toFalseBlock = new ActionBlock();
		toFalseBlock.addAction(new Pop());
		Push pushFalse = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setBoolean(false);
		pushFalse.addValue(falseVal);
		toFalseBlock.addAction(pushFalse);
		
		//toTrue block
		ActionBlock toTrueBlock = new ActionBlock();
		toTrueBlock.addAction(new Pop());
		Push pushTrue = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setBoolean(true);
		pushTrue.addValue(trueVal);
		toTrueBlock.addAction(pushTrue);
		toTrueBlock.addAction(new Jump((short)toFalseBlock.getSize()));
		
		//FalseClass Block
		ActionBlock falseClassBlock = new ActionBlock();
		falseClassBlock.addAction(new PushDuplicate());
		Push pushCons = new Push();
		Push.StackValue consVal = new Push.StackValue();
		consVal.setString("constructor");
		pushCons.addValue(consVal);
		falseClassBlock.addAction(pushCons);
		falseClassBlock.addAction(new GetMember());
		Push pushFalseClass = new Push();
		Push.StackValue falseClassVal = new Push.StackValue();
		falseClassVal.setString("FalseClass");
		pushFalseClass.addValue(falseClassVal);
		falseClassBlock.addAction(pushFalseClass);
		falseClassBlock.addAction(new GetVariable());
		falseClassBlock.addAction(new Equals2());
		falseClassBlock.addAction(new If((short)toTrueBlock.getSize()));
		
		//NilClass Block
		ActionBlock nilClassBlock = new ActionBlock();
		nilClassBlock.addAction(new PushDuplicate());
		nilClassBlock.addAction(pushCons);
		nilClassBlock.addAction(new GetMember());
		Push pushNilClass = new Push();
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		pushNilClass.addValue(nilClassVal);
		nilClassBlock.addAction(pushNilClass);
		nilClassBlock.addAction(new GetVariable());
		nilClassBlock.addAction(new Equals2());
		nilClassBlock.addAction(new If((short)(toTrueBlock.getSize()+falseClassBlock.getSize())));
		
		//null block
		ActionBlock nullBlock = new ActionBlock();
		nullBlock.addAction(new PushDuplicate());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		nullBlock.addAction(pushNull);
		nullBlock.addAction(new Equals2());
		nullBlock.addAction(new If((short)(toTrueBlock.getSize()+falseClassBlock.getSize()+nilClassBlock.getSize())));
		
		//add the actions to the main actionTag
		java.util.List actions = nullBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = nilClassBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = falseClassBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = toTrueBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = toFalseBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
	}
	
	public static void genCheckNullNilFalse(ActionBlock actionTag){
		//toFalse block
		ActionBlock toFalseBlock = new ActionBlock();
		toFalseBlock.addAction(new Pop());
		Push pushFalse = new Push();
		Push.StackValue falseVal = new Push.StackValue();
		falseVal.setBoolean(false);
		pushFalse.addValue(falseVal);
		toFalseBlock.addAction(pushFalse);
		
		//toTrue block
		ActionBlock toTrueBlock = new ActionBlock();
		toTrueBlock.addAction(new Pop());
		Push pushTrue = new Push();
		Push.StackValue trueVal = new Push.StackValue();
		trueVal.setBoolean(true);
		pushTrue.addValue(trueVal);
		toTrueBlock.addAction(pushTrue);
		toTrueBlock.addAction(new Jump((short)toFalseBlock.getSize()));
		
		//FalseClass Block
		ActionBlock falseClassBlock = new ActionBlock();
		falseClassBlock.addAction(new PushDuplicate());
		Push pushCons = new Push();
		Push.StackValue consVal = new Push.StackValue();
		consVal.setString("constructor");
		pushCons.addValue(consVal);
		falseClassBlock.addAction(pushCons);
		falseClassBlock.addAction(new GetMember());
		Push pushFalseClass = new Push();
		Push.StackValue falseClassVal = new Push.StackValue();
		falseClassVal.setString("FalseClass");
		pushFalseClass.addValue(falseClassVal);
		falseClassBlock.addAction(pushFalseClass);
		falseClassBlock.addAction(new GetVariable());
		falseClassBlock.addAction(new Equals2());
		falseClassBlock.addAction(new If((short)toTrueBlock.getSize()));
		
		//NilClass Block
		ActionBlock nilClassBlock = new ActionBlock();
		nilClassBlock.addAction(new PushDuplicate());
		nilClassBlock.addAction(pushCons);
		nilClassBlock.addAction(new GetMember());
		Push pushNilClass = new Push();
		Push.StackValue nilClassVal = new Push.StackValue();
		nilClassVal.setString("NilClass");
		pushNilClass.addValue(nilClassVal);
		nilClassBlock.addAction(pushNilClass);
		nilClassBlock.addAction(new GetVariable());
		nilClassBlock.addAction(new Equals2());
		nilClassBlock.addAction(new If((short)(toTrueBlock.getSize()+falseClassBlock.getSize())));
		
		//null block
		ActionBlock nullBlock = new ActionBlock();
		nullBlock.addAction(new PushDuplicate());
		Push pushNull = new Push();
		Push.StackValue nullVal = new Push.StackValue();
		nullVal.setNull();
		pushNull.addValue(nullVal);
		nullBlock.addAction(pushNull);
		nullBlock.addAction(new Equals2());
		nullBlock.addAction(new If((short)(toTrueBlock.getSize()+falseClassBlock.getSize()+nilClassBlock.getSize())));
		
		//add the actions to the main actionTag
		java.util.List actions = nullBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = nilClassBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = falseClassBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = toTrueBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
		
		actions = toFalseBlock.getActions();
		for(int i=0;i<actions.size();i++){
			actionTag.addAction((Action)actions.get(i));
		}
	}
	
	/**
	 * Transfers the tags from a src ActionBlock to a dest DoAction Tag
	 * dest and src cannot be null
	 * @param dest The DoAction tag to transfer to
	 * @param src The ActionBlock to transfer from
	 */
	public static void copyTags(DoAction dest, ActionBlock src){
		java.util.List actions = src.getActions();
		for(int i=0; i<actions.size();i++){
			dest.addAction((Action)actions.get(i));
		}
	}
	
	/**
	 * Transfers the tags from a src ActionBlock to a dest ActionBlock
	 * dest and src cannot be null
	 * @param dest The ActionBlock to transfer to
	 * @param src The ActionBlock to transfer from
	 */
	public static void copyTags(ActionBlock dest, ActionBlock src){
		java.util.List actions = src.getActions();
		for(int i=0; i<actions.size();i++){
			dest.addAction((Action)actions.get(i));
		}
	}
	/**
	 * Generates code that calls findObj global function
	 * @param actionTag The DoAction tag to append to
	 * @param key The property name to be accessed
	 */
	public static void callFindObj(DoAction actionTag,String key){
		Push pushKeyEnv = new Push();
		Push.StackValue keyVal = new Push.StackValue();
		keyVal.setString(key);
		pushKeyEnv.addValue(keyVal);
		Push.StackValue envVal = new Push.StackValue();
		envVal.setString("current Env");
		pushKeyEnv.addValue(envVal);
		actionTag.addAction(pushKeyEnv);
		actionTag.addAction(new GetVariable());
		Push push2FindObj = new Push();
		Push.StackValue twoVal = new Push.StackValue();
		twoVal.setInteger(2);
		push2FindObj.addValue(twoVal);
		Push.StackValue findObjVal = new Push.StackValue();
		findObjVal.setString("find Obj");
		push2FindObj.addValue(findObjVal);
		actionTag.addAction(push2FindObj);
		actionTag.addAction(new CallFunction());
	}
}
