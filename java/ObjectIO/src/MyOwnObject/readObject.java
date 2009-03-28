/*
 * 创建日期 2006-3-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package MyOwnObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
/**
 * @author mike
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class readObject {
	public static void main(String args[]){
		try{
			FileInputStream fis=new FileInputStream("..\\testClassOBJ");
			ObjectInputStream ois=new ObjectInputStream(fis);
			testClass myObj=(testClass)ois.readObject();
			System.out.println(myObj.str);
			System.out.println(myObj.str2);
		}catch(FileNotFoundException e){e.printStackTrace();}
		catch(IOException ioe){ioe.printStackTrace();}
		catch(ClassNotFoundException cnfe){cnfe.printStackTrace();}
	}
}
