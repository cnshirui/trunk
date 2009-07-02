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

package rubyToSwf.common;
/**
* This is the global scope for the compiler, thus consists of public static methods and variable.
* This is necessary for setting of flags, such as verbose mode
* @version 1.0
* @author Lem Hongjian
*/
public class Global{
	public static boolean verbose = true;
	
	public static int parserState = 0;
	
	private static int counter = 1;
	
	public static int getCounter(){
		return counter++;
	}
	
	public static String getFullLoadPath(String filename){
		return "Ruby/" + filename;
	}
}