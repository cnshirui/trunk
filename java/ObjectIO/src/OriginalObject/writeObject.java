/*
 * 创建日期 2006-3-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package OriginalObject;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * @author mike
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class writeObject {
		
	public static void main(String args[]){
		try{
		
			FileOutputStream fos=new FileOutputStream("..\\StringOBJ");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			String strObj="this is a String Object For testing!";
			oos.writeObject(strObj);
			oos.flush();
			oos.close();
			fos.close();
		}catch(FileNotFoundException e){e.printStackTrace();}
		catch(IOException ioe){ioe.printStackTrace();}
	}
}
