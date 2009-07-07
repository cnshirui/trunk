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

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.jswiff.swfrecords.RGBA;
import com.jswiff.swfrecords.RGB;
import java.io.File;


public class ImageProcessor {
	public static RGBA[] loadRGBAFromGif(String filename,int[] widthHeight){
		try{
			File file = new File(filename);
			BufferedImage bufferedImage = ImageIO.read(file);
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			
			if(widthHeight != null && widthHeight.length > 1){
				widthHeight[0] = width;
				widthHeight[1] = height;
			}
			int[] intArray = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
			//System.out.println("Height: "+height+" Width: "+width+" intArray.length: "+intArray.length);
			RGBA[] rgbaArray= new RGBA[width*height];
			
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
					int val = intArray[y*width+x];
					
					int blue = val & 0xff;
					val = val >>> 8;
					int green = val & 0xff;
					val = val >>> 8;
					int red = val & 0xff;
					val = val >>> 8;
					int alpha = val & 0xff;
					red = (red*alpha)/255;
					green = (green*alpha)/255;
					blue = (blue*alpha)/255;
					
					rgbaArray[y*width+x] = new RGBA(red,green,blue,alpha);
				}
			}
			return rgbaArray;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] loadByteFromJpg(String filename,int[] widthHeight){
		try{
			File file = new File(filename);
			BufferedImage bufferedImage = ImageIO.read(file);
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			
			if(widthHeight != null && widthHeight.length > 1){
				widthHeight[0] = width;
				widthHeight[1] = height;
			}
			
			java.io.FileInputStream fis = new java.io.FileInputStream(filename);
		    byte[] jpegData = new byte[fis.available()];
		    fis.read(jpegData);
		    fis.close();
		    
		    return jpegData;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static int rgbToInt(int red, int green, int blue){
		return red * 256 * 256 + green * 256 + blue;
	}
	
	public static RGB intToRgb(int val){
		int blue = val & 255;
		val = val >> 8;
		int green = val & 255;
		val = val >> 8;
		int red = val & 255;
		
		return new RGB(red,green,blue);
	}
}
