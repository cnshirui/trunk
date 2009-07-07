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

package rubyToSwf.parser;

import rubyToSwf.common.*;
import java_cup.runtime.*;
import rubyToSwf.ast.*;

import java.util.*;

/**
  * A lexer class to be used in conjunction with java cup
  * Reads the source code and returns tokens
  * @author Lem Hongjian
  * @version 1.0
*/
public class Scanner implements java_cup.runtime.Scanner{
	protected SourceReader reader;
	protected Vector<SourceReader>readerStack;
	protected int sourceline;
	
	protected java.util.Hashtable keywords;
	
	private Symbol prevSymbol = null;
	
	public Scanner(){
		try{
			reader = new SourceReader();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Scanner(String filename){
		try{
			reader = new SourceReader(filename);
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	/**
	  * called once to initialize this lex scanner
	  * mainly used to initialize variables
	*/
	public void init(){
		try{
			readerStack = new Vector<SourceReader>();
			sourceline = 0;
			
			buildKeywordsTable();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	* This builds a hashtable containing the keywords of [ruby], in the form <keyword, Token>
	* Whenever a string is encountered, execute keywords.get(str).  If it returns null, it means that the string is not a keyword, else returns the Symbol object associated with it
	*/
	private void buildKeywordsTable(){
		keywords = new java.util.Hashtable();
		keywords.put("true", new Integer(sym.kTRUE));
		keywords.put("false", new Integer(sym.kFALSE));
		keywords.put("nil", new Integer(sym.kNIL));
		keywords.put("do", new Integer(sym.kDO));
		keywords.put("end", new Integer(sym.kEND));
		keywords.put("if", new Integer(sym.kIF));
		keywords.put("then", new Integer(sym.kTHEN));
		keywords.put("else", new Integer(sym.kELSE));
		keywords.put("elsif", new Integer(sym.kELSIF));
		keywords.put("while", new Integer(sym.kWHILE));
		keywords.put("and", new Integer(sym.kAND));
		keywords.put("or", new Integer(sym.kOR));
		keywords.put("not", new Integer(sym.kNOT));
		keywords.put("until", new Integer(sym.kUNTIL));
		keywords.put("unless", new Integer(sym.kUNLESS));
		keywords.put("break", new Integer(sym.kBREAK));
		keywords.put("next", new Integer(sym.kNEXT));
		keywords.put("def", new Integer(sym.kDEF));
		keywords.put("yield", new Integer(sym.kYIELD));
		keywords.put("class", new Integer(sym.kCLASS));
		keywords.put("return", new Integer(sym.kRETURN));
		keywords.put("AS", new Integer(sym.kAS));
		keywords.put("alias", new Integer(sym.kALIAS));
		keywords.put("undef", new Integer(sym.kUNDEF));
		keywords.put("redo", new Integer(sym.kREDO));
		keywords.put("super", new Integer(sym.kSUPER));
		keywords.put("defined?", new Integer(sym.kDEFINED));
		keywords.put("case", new Integer(sym.kCASE));
		keywords.put("when", new Integer(sym.kWHEN));
		keywords.put("module", new Integer(sym.kMODULE));
		keywords.put("load", new Integer(sym.kLOAD));
		keywords.put("raise", new Integer(sym.kRAISE));
	}
	
	/**
	  * called by the parser (cup file) to yield the next token
	  * Note: This scanner will not read beyond the last char of a token, eg, it will not read the whitespace immediately after the current token.  
	  * @return a Symbol object, that represents the token being recognized
	  * @throws java.io.IOException when an IO exception is encountered
	*/
	public Symbol next_token() throws java.io.IOException{
		boolean whitespaceSeen = eatWhitespace();
		
		int c = reader.peek();
		
		//System.out.println((char)c);
		
		switch(c){
			case '#':{
				//comments
				reader.nextChar();
				int c2 = 0;
				while((c2=reader.nextChar())!=-1){
					if(c2=='\n' || c2=='\r'){
						break;
					}
				}
				
				//no need to pushback if c2==-1, cos the reader would not increment its pointer if it's eof
				prevSymbol = new Symbol(sym.tNLINE,new AstObject(sourceline));
				sourceline++;
				return prevSymbol;
			}
			case '\n':{
				reader.nextChar();
							
				prevSymbol = new Symbol(sym.tNLINE,new AstObject(sourceline));
				sourceline++;
				return prevSymbol;
			}
			case '\r':{
				reader.nextChar();
							
				prevSymbol = new Symbol(sym.tNLINE,new AstObject(sourceline));
				sourceline++;
				return prevSymbol;
			}
			case '\f':{
				reader.nextChar();
							
				prevSymbol = new Symbol(sym.tNLINE,new AstObject(sourceline));
				sourceline++;
				return prevSymbol;
			}
			case ';':{
				reader.nextChar();
				
				prevSymbol = new Symbol(sym.tSEMI_COLON,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case '.':{
				reader.nextChar();
				
				int c2 = reader.peek();
				
				if(c2=='.'){
					reader.nextChar();
					
					int c3=reader.peek();
					if(c3=='.'){
						reader.nextChar();
						
						prevSymbol = new Symbol(sym.tDOT3,new AstObject(sourceline));
						return prevSymbol;
					}
					
					prevSymbol = new Symbol(sym.tDOT2,new AstObject(sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tDOT,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case ':':{
				reader.nextChar();
				
				int c2 = reader.peek();
				if(c2==':'){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tCOLON2,new AstObject(sourceline));
					
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tCOLON,new AstObject(sourceline));
				return prevSymbol;
			}
			case '$':{
				reader.nextChar();
				
				int c2 = reader.peek();
				
				if(!((c2>='a' && c2<='z') || (c2>='A' && c2<='Z') || c2=='_')){
					if(Global.verbose){
						System.out.println("Scanner.next_symbol: Invalid global variable name.  Line: "+sourceline);
					}
					throw new RuntimeException("Error: Invalid global variable name.  Line: "+sourceline);
				}
				
				prevSymbol = new Symbol(sym.tVAR_GLOBAL,new AstVariableGlobal("$"+readStringToken(),sourceline));
				return prevSymbol;
			}
			case '@':{
				reader.nextChar();
				
				int c2 = reader.peek();
				if(c2=='@'){
					reader.nextChar();
					int c3 = reader.peek();
					if(!((c3>='a' && c3<='z') || (c3>='A' && c3<='Z') || c3=='_')){
						if(Global.verbose){
							System.out.println("Scanner.next_symbol: Invalid class variable name.  Line: "+sourceline);
						}
						throw new RuntimeException("Error: Invalid class variable name.  Line: "+sourceline);
					}
					prevSymbol = new Symbol(sym.tVAR_CLASS,new AstVariableClass("@@"+readStringToken(),sourceline));
					
					return prevSymbol;
				}
				
				if(!((c2>='a' && c2<='z') || (c2>='A' && c2<='Z') || c2=='_')){
					if(Global.verbose){
						System.out.println("Scanner.next_symbol: Invalid instance variable name.  Line: "+sourceline);
					}
					throw new RuntimeException("Error: Invalid instance variable name.  Line: "+sourceline);
				}
				
				prevSymbol = new Symbol(sym.tVAR_INSTANCE,new AstVariableInstance("@"+readStringToken(),sourceline));
				return prevSymbol;
			}
			case '(':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tLPAREN,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case ')':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tRPAREN,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case '[':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tLSQUAREBRACE,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case ']':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tRSQUAREBRACE,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case '{':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tLCURLYBRACE,new AstObject(sourceline));
				
				return prevSymbol;
			}
			case '}':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tRCURLYBRACE,new AstObject(sourceline));
				return prevSymbol;
			}
			case ',':{
				reader.nextChar();
				prevSymbol = new Symbol(sym.tCOMMA,new AstObject(sourceline));

				return prevSymbol;
			}
			case '\'':
				prevSymbol = new Symbol(sym.tSINGLE_QUOTE_STR,new AstSingleQuote(readSingleQuote(),sourceline));

				return prevSymbol;
			case '"':
				prevSymbol = new Symbol(sym.tDOUBLE_QUOTE_STR,new AstDoubleQuote(readDoubleQuote(),sourceline));

				return prevSymbol;
			case '+':{
				reader.nextChar();			
				int c2 = reader.peek();
				
				// +=
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("+"),sourceline));

					return prevSymbol;
				}
				//unary???
				if(isUnary()){
					System.out.println("tUPLUS returned");
					prevSymbol = new Symbol(sym.tUPLUS,new AstObject(sourceline));

					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tPLUS,new AstObject(sourceline));

				return prevSymbol;
			}
			case '-':{
				reader.nextChar();
				
				int c2 = reader.peek();
				// -=
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("-"),sourceline));

					return prevSymbol;
				}
				
				//unary
				if(isUnary()){
					prevSymbol = new Symbol(sym.tUMINUS,new AstObject(sourceline));

					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tMINUS,new AstObject(sourceline));

				return prevSymbol;
			}
			case '*':{
				reader.nextChar();
				int c2 = reader.peek();
				if(c2=='*'){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						//use ' to represent pow, since there is no such '= token
						prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("**"),sourceline));
						return prevSymbol;
					}
				
					prevSymbol = new Symbol(sym.tPOW,new AstObject(sourceline));
					return prevSymbol;
				}
				
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("*"),sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tMULTIPLY,new AstObject(sourceline));
				return prevSymbol;
			}
			case '/':{
				reader.nextChar();
								
				int c2 = reader.peek();
				// /=
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("/"),sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tDIVIDE, new AstObject(sourceline));
				return prevSymbol;
			}
			case '%':{
				reader.nextChar();
				
				int c2 = reader.peek();
				// -=
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String("%"),sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tMODULO,new AstObject(sourceline));
				return prevSymbol;
			}
			case '>':{
				reader.nextChar();
				int c2 = reader.peek();
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tGEQ,new AstObject(sourceline));
					return prevSymbol;
				}else if(c2=='>'){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String(">>"),sourceline));
						return prevSymbol;
					}
					prevSymbol = new Symbol(sym.tRIGHT_SHIFT,new AstObject(sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tGREAT, new AstObject(sourceline));
				return prevSymbol;
			}
			case '<':{			
				reader.nextChar();
				int c2 = reader.peek();
				if(c2=='='){
					reader.nextChar();
					
					int c3 = reader.peek();
					if(c3=='>'){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tCOMPARE,new AstObject(sourceline));
						return prevSymbol;
					}
					
					prevSymbol = new Symbol(sym.tLEQ,new AstObject(sourceline));

					return prevSymbol;
				}else if(c2=='<'){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("<<"),sourceline));
						return prevSymbol;
					}
					prevSymbol = new Symbol(sym.tLEFT_SHIFT,new AstObject(sourceline));
					return prevSymbol;
				}
				
				prevSymbol = new Symbol(sym.tLESS, new AstObject(sourceline));

				return prevSymbol;
			}
			case '=':{
				reader.nextChar();
				int c2=reader.peek();
				if(c2=='='){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tEQQ,new AstObject(sourceline));
						return prevSymbol;
					}
					

					prevSymbol = new Symbol(sym.tEQ, new AstObject(sourceline));
					return prevSymbol;
				}
				// =>, the hash => symbol, not the smiley emoticon
				if(c2=='>'){
					reader.nextChar();

					prevSymbol = new Symbol(sym.tHASH_ARROW,new AstObject(sourceline));
					return prevSymbol;
				}
				
				// plain assign, use space to represent

				prevSymbol = new Symbol(sym.tASSIGN, new AstObject(new String(" "),sourceline));
				return prevSymbol;
				
			}
			case '&':{
				reader.nextChar();
				
				int c2 = reader.peek();
				if(c2=='&'){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("&&"),sourceline));
						return prevSymbol;
					}
					prevSymbol = new Symbol(sym.tANDOP,new AstObject(sourceline));
					return prevSymbol;
				}else if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("&"),sourceline));
					return prevSymbol;
				}

				prevSymbol = new Symbol(sym.tAMP,new AstObject(sourceline));
				return prevSymbol;
			}
			case '|':{
				reader.nextChar();
				
				int c2 = reader.peek();
				if(c2=='|'){
					reader.nextChar();
					int c3 = reader.peek();
					if(c3=='='){
						reader.nextChar();
						prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("||"),sourceline));
						return prevSymbol;
					}
					prevSymbol = new Symbol(sym.tOROP,new AstObject(sourceline));
					return prevSymbol;
				}else if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("|"),sourceline));
					return prevSymbol;
				}
				//TODO: bitwase or
				prevSymbol = new Symbol(sym.tBAR,new AstObject(sourceline));
				return prevSymbol;
			}
			case '^':{
				reader.nextChar();
				int c2 = reader.peek();
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tASSIGN,new AstObject(new String("^"),sourceline));
					return prevSymbol;
				}
				prevSymbol = new Symbol(sym.tBIT_XOR,new AstObject(sourceline));
				return prevSymbol;
			}
			case '!':{
				
				reader.nextChar();
				int c2 = reader.peek();
				if(c2=='='){
					reader.nextChar();
					prevSymbol = new Symbol(sym.tNEQ,new AstObject(sourceline));
					return prevSymbol;
				}
				

				prevSymbol = new Symbol(sym.tNOT, new AstObject(sourceline));
				return prevSymbol;
			}
			case '?':{
				//eat the '?'
				reader.nextChar();
				int c2 = reader.nextChar();
				if(c2!=-1){
					if(c2=='\\'){
						int c3 = reader.nextChar();
						if(c3!=-1){
							if(isWhitespace((char)c3)){
								// ?\space
								throw new RuntimeException("Not supported!!! Line "+sourceline);
							}else if(c3=='x'||c3=='X'){
								//hex

								prevSymbol = new Symbol(sym.tINTEGER,new AstInteger(readHex(),sourceline));
								return prevSymbol;
							}else if(Character.isDigit((char)c3)){
								//octal
								reader.pushback();

								prevSymbol = new Symbol(sym.tINTEGER,new AstInteger(readOctal(),sourceline));
								return prevSymbol;
							}else{
								int c4 = reader.nextChar();
								if(c4==-1 || isWhitespace((char)c4)){
									if(c4!=-1){
										reader.pushback();
									}
									int ordinal = getEscapedChar((char)c3);
									if(ordinal==-1){
										throw new RuntimeException("Invalid escaped character: \\"+(char)c3+" Line: "+sourceline);
									}else{

										prevSymbol = new Symbol(sym.tINTEGER, new AstInteger(new Integer(ordinal),sourceline));
										return prevSymbol;
									}
								}else{
									//?\st
									throw new RuntimeException("Not supported!!! Line "+sourceline);
								}
							}
						}else{
							// ?\eof
							throw new RuntimeException("sudden termination!!! Line: "+sourceline);
						}
					}else{
						reader.pushback();
						prevSymbol = new Symbol(sym.tQMARK,new AstObject(sourceline));
						return prevSymbol;
					}
				}
				throw new RuntimeException("Not supported yet!!! Line: "+sourceline);
				}
			case '0':{
				//eat the 1st 0
				reader.nextChar();
				int c2 = reader.nextChar();
				if(c2==-1){
					prevSymbol = new Symbol(sym.tINTEGER, new AstInteger(new Integer(0),sourceline));
					return prevSymbol;
				}else if(c2=='.'){
					//0.something
					
					int c3 = reader.peek();
					
					//if digit, then it's a float, else could be something else
					if(c3 >= '0' && c3<='9'){
						reader.pushback();
						reader.pushback();
						

						prevSymbol = new Symbol(sym.tFLOAT, new AstFloat(readFloat(null),sourceline));
						return prevSymbol;
					}else{
						reader.pushback();

						prevSymbol = new Symbol(sym.tINTEGER, new AstInteger(0,sourceline));
						return prevSymbol;
					}
				}else{
					if(isWhitespace((char)c2)){
						reader.pushback();
						prevSymbol = new Symbol(sym.tINTEGER, new AstInteger(new Integer(0),sourceline));
						return prevSymbol;
					}else if(c2=='x'||c2=='X'){

						prevSymbol = new Symbol(sym.tINTEGER,new AstInteger(readHex(),sourceline));
						return prevSymbol;
					}else if(c2=='b'||c2=='B'){

						prevSymbol = new Symbol(sym.tINTEGER,new AstInteger(readBinary(),sourceline));
						return prevSymbol;
					}else if(Character.isDigit((char)c2)){
						reader.pushback();

						prevSymbol = new Symbol(sym.tINTEGER,new AstInteger(readOctal(),sourceline));
						return prevSymbol;
					}else{
						reader.pushback();
						prevSymbol = new Symbol(sym.tINTEGER, new AstInteger(new Integer(0),sourceline));
						return prevSymbol;
						//throw new RuntimeException("Not supported! Line "+sourceline);
					}
				}
			}
			case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':{
				Object res = readNumber();
				if(res instanceof Integer){

					prevSymbol = new Symbol(sym.tINTEGER,new AstInteger((Integer)res,sourceline));
					return prevSymbol;
				}else{

					prevSymbol = new Symbol(sym.tFLOAT,new AstFloat((Float)res,sourceline));
					return prevSymbol;
				}
			}
			case -1:{
				//System.out.println("eof");
				prevSymbol = new Symbol(sym.EOF,new AstObject(sourceline));
				return prevSymbol;
			}
			default:{
				if((c >= 'a' && c<='z') || (c>='A' && c<='Z') || (c=='_')){
					//start with alphabets
					prevSymbol = readStringSymbol();
					return prevSymbol;
				}else{
					prevSymbol = new Symbol(sym.EOF,new AstObject(sourceline));
					return prevSymbol;
				}
			}
		}
	}
	
	/**
	  * helper function to eat/discard leading whitespace
	  * @return true if whitespace encountered
	*/
	protected boolean eatWhitespace(){
		boolean bWhitespace = false;
		int c;
		
		while(true){
			c = reader.nextChar();
			if(c==-1){
				break;
			}
			
			if(isNonBreakWhitespace((char)c)){
				bWhitespace = true;
			}else{
				if(c=='\r' || c=='\f' || c=='\n'){
					reader.pushback();
					break;
				}else{
					reader.pushback();
					break;
				}
			}

		}
		
		return bWhitespace;
	}
	
	/**
	  * helper function to read a single-quote string literal, substitutes for \\ and \'
	  * stops at next occurrence of closing single quote.  
	  * @return the string literal
	  * @throws RuntimeException if exception occurred while parsing
	*/
	protected String readSingleQuote(){
		StringBuffer buffer = new StringBuffer();
		
		//eat 1st quote
		reader.nextChar();
		
		int c;
		while(true){
			c = reader.nextChar();
			if(c==-1){
				System.out.println("Please unquote your string: line "+sourceline);
				throw new RuntimeException("Please unquote your string: line "+sourceline);
			}
			
			if(c=='\''){
				break;
			}
			
			if(c=='\\'){
				int c2 = reader.nextChar();
				if(c2==-1){
					System.out.println("Please unquote your string: line "+sourceline);
					throw new RuntimeException("Please unquote your string: line "+sourceline);
				}
			
				if(c2=='\\' || c2=='\''){
					buffer.append((char)c2);
				}else{
					buffer.append((char)c);
					buffer.append((char)c2);
				}
			}else{
				buffer.append((char)c);
			}
		}
		
		return buffer.toString();
	}
	/**
	  * helper function to read a double-quote string literal, substitutes for \\ and \'
	  * stops at next occurrence of closing double quote.  
	  * @return the string literal
	  * @throws RuntimeException if exception occurred while parsing
	*/
	protected String readDoubleQuote(){
		StringBuffer buffer = new StringBuffer();
		
		//eat 1st quote
		reader.nextChar();
		
		int c;
		while(true){
			c = reader.nextChar();
			if(c==-1){
				System.out.println("Please unquote your string: line "+sourceline);
				throw new RuntimeException("Please unquote your string: line "+sourceline);
			}
			
			if(c=='"'){
				break;
			}
			
			if(c=='\\'){
				int c2 = reader.nextChar();
				if(c2==-1){
					System.out.println("Please unquote your string: line "+sourceline);
					throw new RuntimeException("Please unquote your string: line "+sourceline);
				}
			
				if(c2=='\\' || c2=='\''){
					buffer.append((char)c2);
				}else{
					switch(c2){
					case 'n':
					case 'r':
					case 'b':
					case 'f':
					case 'a':
					case 'e':
					case 's':
						buffer.append((char)getEscapedChar((char)c2));
						break;
					default:
						buffer.append((char)c);
						buffer.append((char)c2);
					}
				}
			}else{
				buffer.append((char)c);
			}
		}
		
		return buffer.toString();
	}
	
	/**
	  * helper function to read a number (integer or float).  Delegates to readFloat() in order to return a float
	  * stops at whitespace
	  * @return Integer or Float instance that represents the number read
	*/
	protected Object readNumber(){
		StringBuffer buffer = new StringBuffer();
		
		int c;
		
		while(true){
			c = reader.nextChar();
			
			if(c==-1){
				break;
			}
			
			if(Character.isDigit((char)c)){
				buffer.append((char)c);
			}else{
				if(isWhitespace((char)c)){
					reader.pushback();
					break;
				}else if(c=='.'){
					int c2 = reader.peek();

					if(c2>='0' && c2 <='9'){
						reader.pushback();
						return readFloat(buffer);
					}else{
						reader.pushback();
						break;
					}
					
				}else if(c=='E' || c=='e'){
					reader.pushback();
					return readFloat(buffer);
				}else{
					//a number cannot be followed immediately by alphabets
					if((c>='A'&&c<='Z')||(c>='a'&&c<='z')){
						System.out.println("Not a digit: "+(char)c+" Line "+sourceline);
						throw new RuntimeException("Not a digit: Line "+sourceline);
					}else{
						//might be operators
						reader.pushback();
						break;
					}
				}
			}
		}
		
		return new Integer(buffer.toString());
	}
	
	/**
	  * helper function to determine if character is a whitespace
	  * @param c the character to check
	  * @return true is this character is a whitespace
	*/
	protected boolean isWhitespace(char c){
		if(c=='\r' || c=='\n' || c=='\t' || c==' ' || c=='\f'){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	  * helper function to determine if character is a non break whitespace
	  * @param c the character to check
	  * @return true is this character is a non break whitespace
	*/
	protected boolean isNonBreakWhitespace(char c){
		if(c=='\t' || c==' '){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	  * helper function to return the character being encoded by the escaped sequence
	  * @param c the character immediately following \
	  * @return the encoded character as int, or -1 if invalid
	*/
	protected int getEscapedChar(char c){
		switch(c){
			case '\\':
				return '\\';
			case 'n':
				return '\n';
			case 'r':
				return '\r';
			case 'f':
				return '\f';
			case 'b':
				return '\b';
			case 'a':
				return 0x07;
			case 'e':
				return 0x1b;
			case 's':
				return ' ';
			default:
				return -1;
		}
	}
	
	/**
	  * helper function that converts a character stream from binary to integer
	  * terminated by whitespace
	  * @return the integer encoded by the binary string
	*/
	protected Integer readBinary(){
		StringBuffer buffer = new StringBuffer();
		
		while(true){
			int c = reader.nextChar();
			if(c==-1){
				break;
			}else{
				if(c >= '0' && c <= '1'){
					buffer.append((char)c);
				}else{
					if(isWhitespace((char)c)){
						reader.pushback();
						break;
					}else{
						throw new RuntimeException("Invalid binary value!!! Line "+ sourceline);
					}
				}
			}
		}
		
		if(buffer.length() > 0){
			return new Integer(Integer.parseInt(buffer.toString(),2));
		}else{
			throw new RuntimeException("Empty octal string!!! Line "+sourceline);
		}
	}
	
	/**
	  * helper function that converts a character stream from octal to integer
	  * terminated by whitespace
	  * @return the integer encoded by the octal string
	*/
	protected Integer readOctal(){
		StringBuffer buffer = new StringBuffer();
		
		while(true){
			int c = reader.nextChar();
			if(c==-1){
				break;
			}else{
				if(c >= '0' && c <= '7'){
					buffer.append((char)c);
				}else{
					if(isWhitespace((char)c)){
						reader.pushback();
						break;
					}else{
						throw new RuntimeException("Invalid octal value!!! Line "+ sourceline);
					}
				}
			}
		}
		
		if(buffer.length() > 0){
			return new Integer(Integer.parseInt(buffer.toString(),8));
		}else{
			throw new RuntimeException("Empty octal string!!! Line "+sourceline);
		}
	}
	
	/**
	  * helper function that converts a character stream from hex to an integer
	  * terminated by whitespace
	  * @return the integer encoded by the hex string
	*/
	protected Integer readHex(){
		StringBuffer buffer = new StringBuffer();
		
		while(true){
			int c = reader.nextChar();
			if(c==-1){
				break;
			}else{
				if((c >= '0' && c <= '9') || (c>='A' && c<='F') || (c>='a' && c<='f')){
					buffer.append((char)c);
				}else{
					if(isWhitespace((char)c)){
						reader.pushback();
						break;
					}else{
						throw new RuntimeException("Invalid hex value!!! Line "+ sourceline);
					}
				}
			}
		}
		
		if(buffer.length() > 0){
			return new Integer(Integer.parseInt(buffer.toString(),16));
		}else{
			throw new RuntimeException("Empty octal string!!! Line "+sourceline);
		}
	}
	
	/**
	  * helper function that returns a float
	  * a float can be represented by <number1>E<number2>
	  * terminated by whitespace
	  * @param prev a StringBuffer object which holds the characters read so far (eg, by readNumber()), null otherwise
	  * @return the Float object represented by the string
	*/
	protected Float readFloat(StringBuffer prev){
		Float f1 = readBasicFloat(prev);
		
		int c = reader.nextChar();
		if(c!=-1){
			if(c=='E' || c=='e'){
				Float f2 = readBasicFloat(null);
				
				return new Float(f1.floatValue() * Math.pow(10.0,f2.floatValue()));
			}else{
				//cannot be alphabets
				if((c>='A'&&c<='Z')||(c>='a'&&c<='z')){
					System.out.println("Not a digit: "+(char)c+" Line "+sourceline);
					throw new RuntimeException("Not a digit: Line "+sourceline);
				}
				reader.pushback();
			}
		}
		
		return f1;
	}
	
	/**
	  * helper function that reads a float in its basic format, namely <integer>.<integer>
	  * Note: if a dot exists, this dot must be read by this function!  The existence of the dot is used for validity check, eg there should not be more than 1 dot, unless the 2nd dot is used for method calls
	  * @param prev a StringBuffer object which holds the characters read so far (eg, by readNumber()), null otherwise
	  * @return the Float object represented by the string
	*/
	protected Float readBasicFloat(StringBuffer prev){
		//basic float, nnn.nnn, no E or anything fanciful

		StringBuffer buffer;
		if(prev==null){
			buffer = new StringBuffer();
		}else{
			buffer = prev;
		}
		
		boolean dot = false;
		
		while(true){
			int c = reader.nextChar();

			if(c==-1){
				break;
			}else if(isWhitespace((char)c) || (!(Character.isDigit((char)c)) && c != '.')){
				reader.pushback();
				break;
			}
			
			if(c=='.'){

				if(!dot){
					dot = true;
					
					int c2 = reader.peek();
					if(c2==-1 || isWhitespace((char)c2)){
						throw new RuntimeException("Float terminated suddenly!!! Line: "+sourceline);
					}
				}else{
					reader.pushback();
					break;
				}
			}

			buffer.append((char)c);
		}
		
		return new Float(Float.parseFloat(buffer.toString()));
	}
	
	/**
	* converts characters of a string token to its respective symbols
	* @return tVAR_LOCAL, tCONSTANT, or a keyword symbol such as kNIL
	*/
	protected Symbol readStringSymbol(){
		String str = readStringToken();
		
		if(str==null || str.length()==0){
			throw new RuntimeException("empty string token! line: "+sourceline);
		}
		
		Integer keywordInt = (Integer)keywords.get(str);
		
		if(keywordInt != null){
			//a keyword
			prevSymbol = new Symbol(keywordInt.intValue(),new AstObject(sourceline));
			return prevSymbol;
		}else{
			//lower case
			if(!Character.isUpperCase(str.charAt(0))){
				prevSymbol = new Symbol(sym.tVAR_LOCAL, new AstVariableLocal(str,sourceline));
				return prevSymbol;
			}else{
				prevSymbol = new Symbol(sym.tCONSTANT, new AstConstant(str,sourceline));
				return prevSymbol;
			}
		}
	}
	
	/**
	* helper function to read characters of a string token
	* normally called by readStringSymbol to retrieve the string
	* last char can be ! or ?
	* @return a string
	*/
	protected String readStringToken(){
		StringBuffer buffer = new StringBuffer();
		while(true){
			int c = reader.nextChar();
			
			if(c==-1){
				break;
			}
			
			if((c>='A' && c<='Z') || (c>='a' && c<='z') || (c=='_') || (c>='0' && c<='9')){
				buffer.append((char)c);
			}else{
				reader.pushback();
				break;
			}
		}
		int c = reader.peek();
		if(c=='!' || c=='?'){
			buffer.append((char)c);
			reader.nextChar();
		}
		return buffer.toString();
	}
	
	protected boolean isUnary(){
		if(prevSymbol==null){
			return true;
		}
		
		int prevSym = prevSymbol.sym;
		switch(prevSym){
		case sym.tPLUS:case sym.tNLINE:case sym.tMINUS:case sym.tMULTIPLY:case sym.tDIVIDE:case sym.tMODULO:case sym.kUNLESS:case sym.kUNTIL:case sym.tCOMPARE:
		case sym.tUMINUS:case sym.tPOW:case sym.tEQ:case sym.tEQQ:case sym.tLESS:case sym.tGREAT:case sym.tLEQ:case sym.tGEQ:case sym.tNEQ:case sym.tNOT: 
		case sym.tANDOP:case sym.tOROP:case sym.tLPAREN:case sym.tCOMMA:case sym.tLSQUAREBRACE:case sym.tLCURLYBRACE:case sym.tASSIGN:case sym.tSEMI_COLON:
		case sym.tHASH_ARROW:case sym.kDO:case sym.kIF:case sym.kTHEN:case sym.kELSE:case sym.kELSIF:case sym.kWHILE:case sym.kAND:case sym.kOR:case sym.kNOT:
		case sym.kRETURN:case sym.tLEFT_SHIFT:case sym.tRIGHT_SHIFT:case sym.tDOT2:case sym.tDOT3:case sym.tAMP:case sym.tBIT_XOR:case sym.tBAR:case sym.kNEXT:
		case sym.kYIELD:case sym.kREDO:case sym.kSUPER:case sym.kCASE:case sym.kWHEN:
			return true;
		}
		
		return false;
	}
}