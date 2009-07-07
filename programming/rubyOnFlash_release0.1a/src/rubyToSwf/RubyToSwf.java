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

package rubyToSwf;

import rubyToSwf.common.Global;
import rubyToSwf.ast.IAstNode;
import rubyToSwf.codegen.SwfProgram;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;

public class RubyToSwf{
	public static void main(String[] arg){
		String sourcePath = "";
		String outputPath = "";
		String resourcePath = "";
		Vector<String> libraries = new Vector<String>();
		int width = 550;
		int height = 400;
		int frameRate = 12;
		//default is white
		int background = rubyToSwf.util.ImageProcessor.rgbToInt(255, 255, 255);
		
		//must have even no. of parameters
		if(arg.length % 2 != 0){
			if(!arg[0].equalsIgnoreCase("--help") && !arg[0].equalsIgnoreCase("-h")){
				throw new RuntimeException("The arguments should be of this form: java rubyToSwf.RubyToSwf [-flag arg]*.  Use --help or -h for more information.  ");
			}else{
				System.out.println("Flag             	Description");
				System.out.println("================	===========");
				System.out.println("[-s|--source]    	The source path");
				System.out.println("[-o|--output]    	The output path");
				System.out.println("[-r|--resource]  	The resource path");
				System.out.println("[-l|--lib]  		A library to link");
				System.out.println("[-w|--width]     	Width(pixels)");
				System.out.println("[-h|--height]    	Height(pixels)");
				System.out.println("[-f|--framerate] 	Frame rate(frames/sec)");
				System.out.println("[-b|--background] 	Background color(hex)");
				System.out.println("[-h|--help]      	Displays this help menu");
			}
		}else{
			for(int i=0;i<arg.length/2;i++){
				String flag = arg[i*2];
				String value = arg[i*2+1];
				if(flag.equalsIgnoreCase("-s") || flag.equalsIgnoreCase("--source")){
					sourcePath = value;
				}else if(flag.equalsIgnoreCase("-o") || flag.equalsIgnoreCase("--output")){
					outputPath = value;
				}else if(flag.equalsIgnoreCase("-r") || flag.equalsIgnoreCase("--resource")){
					resourcePath = value;
				}else if(flag.equalsIgnoreCase("-l") || flag.equalsIgnoreCase("--lib")){
					libraries.add(value);
				}else if(flag.equalsIgnoreCase("-w") || flag.equalsIgnoreCase("--width")){
					try{
						width = Integer.parseInt(value);
					}catch(NumberFormatException e){
						throw new RuntimeException("Invalid value for width: "+value);
					}
				}else if(flag.equalsIgnoreCase("-h") || flag.equalsIgnoreCase("--height")){
					try{
						height = Integer.parseInt(value);
					}catch(NumberFormatException e){
						throw new RuntimeException("Invalid value for height: "+value);
					}
				}else if(flag.equalsIgnoreCase("-f") || flag.equalsIgnoreCase("--framerate")){
					try{
						frameRate = Integer.parseInt(value);
					}catch(NumberFormatException e){
						throw new RuntimeException("Invalid value for frame rate: "+value);
					}
				}else if(flag.equalsIgnoreCase("-b") || flag.equalsIgnoreCase("--background")){
					try{
						background = Integer.parseInt(value,16);
					}catch(NumberFormatException e){
						throw new RuntimeException("Invalid value for background color: "+value);
					}
				}else{
					throw new RuntimeException("Invalid flag: "+flag);
				}
			}
			
			/*
			if(sourcePath.trim().length()==0){
				p = new rubyToSwf.parser.parser(new rubyToSwf.parser.Scanner());
				System.out.println("Using standard input...");
			}else{
				p = new rubyToSwf.parser.parser(new rubyToSwf.parser.Scanner(sourcePath));
			}
			*/
			
			try{
				//IAstNode ast = (IAstNode)p.parse().value;
				java.util.Vector<IResourceFileItem> resourceFileItems = new java.util.Vector<IResourceFileItem>();
				
				//read resource file
				if(resourcePath.trim().length()>0){
					BufferedReader resourceFileStream = new BufferedReader(new FileReader(resourcePath));
					java.util.Hashtable ht = new java.util.Hashtable();
					
					int lineNo = 0;
					while(true){
						lineNo++;
						String line=resourceFileStream.readLine();
						if(line==null || line.trim().length()==0){
							break;
						}
						
						String url = "";
						String id = "";
						int dividerIndex = line.indexOf(":");
						if(dividerIndex==-1){
							continue;
						}else{
							id = line.substring(0,dividerIndex).trim();
							url = line.substring(dividerIndex + 1).trim();
							Object temp = ht.get(id);
							if(temp != null){
								throw new RuntimeException("Error: Duplicate resource id: \""+id+"\" at lines "+temp+" and "+lineNo);
							}else{
								ht.put(id, lineNo);
							}
						}
						
						String ext = url.substring(url.lastIndexOf(".")+1).trim();
							
						if(ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")){
							resourceFileItems.add(new JpgResourceFileItem(url.trim(),id));
						}else{
							//note: for gif, png, bmp, or other formats they are represented as DefineBitsLossless2
							resourceFileItems.add(new GifResourceFileItem(url.trim(),id));
						}
					}
				}
				//derive the output path
				if(outputPath.trim().length()==0){
					if(sourcePath.trim().length() > 0){
						int firstSlash = sourcePath.lastIndexOf("/");
						if(firstSlash==-1){
							firstSlash = sourcePath.lastIndexOf("\\");
						}
						int lastDot = sourcePath.lastIndexOf(".");
						
						if(lastDot==-1 || lastDot < firstSlash){
							outputPath = sourcePath.substring(firstSlash+1);
						}else{
							outputPath = sourcePath.substring(firstSlash+1,lastDot);
						}
						
						outputPath = outputPath.trim() + ".swf";
					}else{
						outputPath = "default.swf";
					}
				}
				
				//generate the swf file
				System.out.println("Creating "+outputPath);
				SwfProgram swf = new SwfProgram(outputPath,sourcePath,resourceFileItems,libraries,width,height,frameRate,background);
				swf.generate();
				System.out.println(outputPath + " created");
				
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}