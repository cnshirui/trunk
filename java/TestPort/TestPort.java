/*
 * 创建日期 2006-2-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

/**
 * @author shirui
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.net.*;

public class TestPort {
	private static ServerSocket serverSkt;
	public static void main(String args[])
	{
		try
		{
			int port =1433;
			serverSkt=new ServerSocket(port);
			System.out.println("Open server "+port+" success!");
			new server().start();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static class server extends Thread
	{
		public server()
		{
			super("server");
			this.run();
		}
		public void run()
		{
			while(true)
			{
				try
				{
					Socket skt=serverSkt.accept();
					skt.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}
