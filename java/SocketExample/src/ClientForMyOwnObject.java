/*
 * 创建日期 2006-3-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import MyOwnObject.testClass;

/**
 * @author mike
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ClientForMyOwnObject {
		public static void main(String args[]){
			
		  	try
		  	  {
		  		Socket socket= new Socket("127.0.0.1", Server.port);
		  		OutputStream os=socket.getOutputStream();
		    	ObjectOutputStream oos = new ObjectOutputStream(os);// 绑定
		    		    	
				oos.writeObject(new testClass());
				oos.flush();
				oos.close();
				socket.close();
			  }catch(Exception e)
			  {
			  	e.printStackTrace();
			  }
		}
}
