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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
  * A wrapper class that wraps an input reader stream.  
  * Facilitates the push back operations
  * achieved via double buffering
  * @author Lem Hongjian
  * @version 1.0
*/
public class SourceReader{
	private static final int BUF_SIZE = 256;
	private int lex_p; //current pointer
	private int newBufferLen; //the length of the new buffer
	private int charsRead; //number of characters read in total
	private char[] oldBuffer;
	private char[] newBuffer;
	private boolean eof;
	
	private BufferedReader reader;
	
	private FileWriter fos;
	
	/**
	  * The default constructor uses the standard in as input
	*/
	public SourceReader() throws Exception{
		this(System.in);
	}
	
	/**
	  * Opens a file stream from filename to be used as input
	  * @param	filename	the name of the file
	*/
	public SourceReader(String filename) throws Exception{
		this(new FileInputStream(filename));
	}
	
	/**
	  * uses an input stream directly
	  * @param	is	the input stream to be used
	*/
	public SourceReader(InputStream is) throws Exception{
		reader = new BufferedReader(new InputStreamReader(is));
		
		oldBuffer = new char[BUF_SIZE];
		newBuffer = new char[BUF_SIZE];
		eof = false;
		lex_p = 0;
		charsRead = 0;
		nextChunk();
	}
	
	/**
	  * A private method called by nextChar and peek, to read the next chunk of data into newBuffer
	  * sets eof flag to true
	*/
	private void nextChunk(){
		//swap the buffers
		char[] temp = oldBuffer;
		oldBuffer = newBuffer;
		newBuffer = temp;
		
		//read next chunk of data
		try{
			newBufferLen = reader.read(newBuffer,0,BUF_SIZE);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(newBufferLen < BUF_SIZE){
			eof = true;
		}
	}
	
	/**
	  * sets the pointer one step back, effectively unreading a character
	*/
	public void pushback(){
		
		try{
			if(fos != null){
				fos.write("pushback called\r\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(charsRead <= 0){
			System.out.println("WARNING: You've pushed back too far!!!");
		}else{
			if(lex_p <= (-1 * BUF_SIZE)){
				System.out.println("WARNING: You've pushed back too far!!!");
			}else{
				lex_p--;
				charsRead--;
			}
		}
	}
	
	/**
	  * reads the next char, set pointer to point at the next character
	  * @see rubyToSwf.parser.SourceReader#peek() peek()
	  * @return the desired character as an integer, or -1 if eof reached
	*/
	public int nextChar(){
		if(lex_p >= newBufferLen){
			if(eof){
				return -1;
			}else{
				nextChunk();
				lex_p = 0;
			}
		}
		
		char c;
		if(lex_p < 0){
			c = oldBuffer[BUF_SIZE + lex_p];
		}else{
			c = newBuffer[lex_p];
		}
		
		try{
			if(fos != null){
				if(c=='\n'){
					fos.write("\\n");
				}else if(c=='\r'){
					fos.write("\\r");
				}else if(c=='\f'){
					fos.write("\\f");
				}else if(c=='\t'){
					fos.write("\\t");
				}else{
					fos.write(c);
				}
				fos.flush();
			}
			//System.out.print(c);
		}catch(Exception e){
			e.printStackTrace();
		}
			
		lex_p++;
		charsRead++;
		return c;
	}
	
	/**
	  * reads the next char, DOES NOT set pointer to point at the next character, must manually call nextChar() in order to advance the pointer
	  * @see rubyToSwf.parser.SourceReader#nextChar() nextChar()
	  * @return the desired character as an integer, or -1 if eof reached
	*/
	public int peek(){
		if(lex_p >= newBufferLen){
			if(eof){
				return -1;
			}else{
				nextChunk();
				lex_p = 0;
			}
		}
		
		if(lex_p < 0){
			return oldBuffer[BUF_SIZE + lex_p];
		}else{
			return newBuffer[lex_p];
		}
	}
	
	public static void main(String[] arg){
		SourceReader sr;
		try{
			sr = new SourceReader();
			
			int c;
			while((c=sr.nextChar())!=-1){
				System.out.print((char)c);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}