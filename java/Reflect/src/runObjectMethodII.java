import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;



import java.lang.reflect.*;
/*
 * 创建日期 2006-3-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

/**
 * @author hanliang
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class runObjectMethodII {
		public static void main(String args[]){
			try{
				FileInputStream fis=new FileInputStream("..\\testClassOBJ");
				ObjectInputStream ois=new ObjectInputStream(fis);
				Object myObj=ois.readObject();
				Class myClass=myObj.getClass();
				Method method1=myClass.getMethod("test1",new Class[]{});
				Method method2=myClass.getMethod("test2",new Class[]{int.class});
				Method method3=myClass.getMethod("getStr",new Class[]{});
				Field field1=myClass.getField("str");
				
				method1.invoke(myObj,new Object[]{});
				method2.invoke(myObj,new Object[]{new Integer(3)});
				String str=(String)method3.invoke(myObj,new Object[]{});
				System.out.println("get the String: \""+str+"\"");
				System.out.println("field get by Field:"+field1.getName()+":"+field1.get(myObj));
			}catch(FileNotFoundException e){e.printStackTrace();}
			catch(IOException ioe){ioe.printStackTrace();}
			catch(ClassNotFoundException cnfe){cnfe.printStackTrace();}
			catch(NoSuchMethodException nsme){nsme.printStackTrace();}
			catch(NoSuchFieldException nsfe){}
			catch(IllegalAccessException iae){iae.printStackTrace();}
			catch(IllegalArgumentException iArgE){iArgE.printStackTrace();}
			catch(InvocationTargetException ite){ite.printStackTrace();}
		}
}
