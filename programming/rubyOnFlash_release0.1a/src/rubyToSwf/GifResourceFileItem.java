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

import com.jswiff.*;
import com.jswiff.swfrecords.*;
import com.jswiff.swfrecords.tags.*;

import rubyToSwf.common.*;
import rubyToSwf.util.*;

public class GifResourceFileItem implements IResourceFileItem {

	private String url;
	private String id;
	
	public GifResourceFileItem(String url, String id){
		this.url = url;
		this.id = id;
	}
	
	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return url;
	}

	/**
	 * @return The character id of the DefineSprite tag representing this document
	 */
	public int generateTags(SWFDocument document){
		int[] widthHeight = new int[2];
	    RGBA[] rgbaArray = rubyToSwf.util.ImageProcessor.loadRGBAFromGif(url,widthHeight);
	    int width = widthHeight[0];
	    int height = widthHeight[1];
	    
	    int gifId = document.getNewCharacterId();
	    DefineBitsLossless2 gif1 = new DefineBitsLossless2(gifId,DefineBitsLossless2.FORMAT_32_BIT_RGBA,width,height,new AlphaBitmapData(rgbaArray));
	    document.addTag(gif1);
	    
	    int gifShapeId = document.getNewCharacterId();
	    FillStyleArray gifShapeFillStyleArray = new FillStyleArray();
	    Matrix mat1 = new Matrix(0,0);
	    mat1.setScale(20,20);
	    Matrix mat2 = new Matrix(-width * 10 ,-height * 10);
	    mat2.setScale(20,20);
	    gifShapeFillStyleArray.addStyle(new FillStyle(65535,mat1,FillStyle.TYPE_CLIPPED_BITMAP));
	    gifShapeFillStyleArray.addStyle(new FillStyle(gifId,mat2,FillStyle.TYPE_CLIPPED_BITMAP));
	    ShapeRecord[] gifShapeShapeRecords = new ShapeRecord[5];
	    gifShapeShapeRecords[0] = new StyleChangeRecord();
	    ((StyleChangeRecord)gifShapeShapeRecords[0]).setMoveTo(-width * 10, -height *10);
	    ((StyleChangeRecord)gifShapeShapeRecords[0]).setFillStyle1(2);
	    gifShapeShapeRecords[1] = new StraightEdgeRecord(width * 20,0);
	    gifShapeShapeRecords[2] = new StraightEdgeRecord(0,height * 20);
	    gifShapeShapeRecords[3] = new StraightEdgeRecord(-width * 20,0);
	    gifShapeShapeRecords[4] = new StraightEdgeRecord(0,-height * 20);
	    DefineShape gifShape = new DefineShape(gifShapeId,new Rect(-width*10,width*10,-height*10,height*10),new ShapeWithStyle(gifShapeFillStyleArray,new LineStyleArray(),gifShapeShapeRecords));
	    document.addTag(gifShape);
	    
	    int gifSpriteId = document.getNewCharacterId();
	    DefineSprite gifSprite = new DefineSprite(gifSpriteId);
	    PlaceObject2 placeGif = new PlaceObject2(1);
	    placeGif.setCharacterId(gifShapeId);
	    placeGif.setMatrix(new Matrix(0,0));
	    gifSprite.addControlTag(placeGif);
	    gifSprite.addControlTag(new ShowFrame());
	    document.addTag(gifSprite);

	    return gifSpriteId;
	}
}
