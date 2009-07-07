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

import com.jswiff.SWFDocument;
import com.jswiff.swfrecords.AlphaBitmapData;
import com.jswiff.swfrecords.FillStyle;
import com.jswiff.swfrecords.FillStyleArray;
import com.jswiff.swfrecords.LineStyleArray;
import com.jswiff.swfrecords.Matrix;
import com.jswiff.swfrecords.RGBA;
import com.jswiff.swfrecords.Rect;
import com.jswiff.swfrecords.ShapeRecord;
import com.jswiff.swfrecords.ShapeWithStyle;
import com.jswiff.swfrecords.StraightEdgeRecord;
import com.jswiff.swfrecords.StyleChangeRecord;
import com.jswiff.swfrecords.tags.DefineBitsJPEG2;
import com.jswiff.swfrecords.tags.DefineShape;
import com.jswiff.swfrecords.tags.DefineSprite;
import com.jswiff.swfrecords.tags.ExportAssets;
import com.jswiff.swfrecords.tags.PlaceObject2;
import com.jswiff.swfrecords.tags.ShowFrame;

public class JpgResourceFileItem implements IResourceFileItem {

	private String url;
	private String id;
	
	public JpgResourceFileItem(String url, String id){
		this.url = url;
		this.id = id;
	}
	
	public int generateTags(SWFDocument document) {    
	    int jpgId = document.getNewCharacterId();
	    
	    int[] widthHeight = new int[2];
	    byte[] jpegData = rubyToSwf.util.ImageProcessor.loadByteFromJpg(url,widthHeight);
	    int width = widthHeight[0];
	    int height = widthHeight[1];
	    
	    DefineBitsJPEG2 jpg1 = new DefineBitsJPEG2(jpgId,jpegData);
	    document.addTag(jpg1);
	    
	    int jpgShapeId = document.getNewCharacterId();
	    FillStyleArray jpgShapeFillStyleArray = new FillStyleArray();
	    Matrix mat1 = new Matrix(0,0);
	    mat1.setScale(20,20);
	    Matrix mat2 = new Matrix(-width * 10 ,-height * 10);
	    mat2.setScale(20,20);
	    jpgShapeFillStyleArray.addStyle(new FillStyle(65535,mat1,FillStyle.TYPE_CLIPPED_BITMAP));
	    jpgShapeFillStyleArray.addStyle(new FillStyle(jpgId,mat2,FillStyle.TYPE_CLIPPED_BITMAP));
	    ShapeRecord[] jpgShapeShapeRecords = new ShapeRecord[5];
	    jpgShapeShapeRecords[0] = new StyleChangeRecord();
	    ((StyleChangeRecord)jpgShapeShapeRecords[0]).setMoveTo(-width * 10, -height *10);
	    ((StyleChangeRecord)jpgShapeShapeRecords[0]).setFillStyle1(2);
	    jpgShapeShapeRecords[1] = new StraightEdgeRecord(width * 20,0);
	    jpgShapeShapeRecords[2] = new StraightEdgeRecord(0,height * 20);
	    jpgShapeShapeRecords[3] = new StraightEdgeRecord(-width * 20,0);
	    jpgShapeShapeRecords[4] = new StraightEdgeRecord(0,-height * 20);
	    DefineShape jpgShape = new DefineShape(jpgShapeId,new Rect(-width*10,width*10,-height*10,height*10),new ShapeWithStyle(jpgShapeFillStyleArray,new LineStyleArray(),jpgShapeShapeRecords));
	    document.addTag(jpgShape);
	    
	    int jpgSpriteId = document.getNewCharacterId();
	    DefineSprite jpgSprite = new DefineSprite(jpgSpriteId);
	    PlaceObject2 placeGif = new PlaceObject2(1);
	    placeGif.setCharacterId(jpgShapeId);
	    placeGif.setMatrix(new Matrix(0,0));
	    jpgSprite.addControlTag(placeGif);
	    jpgSprite.addControlTag(new ShowFrame());
	    document.addTag(jpgSprite);
	    
	    return jpgSpriteId;
	}

	public String getID() {
		return id;
	}

	public String getUrl() {
		return url;
	}

}
