/*
 * 创建日期 2005-10-25
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
public class Semaphore 
{
	public int count;
	
	public Semaphore(int i)
	{
		this.count = i;
	}
	
	public synchronized void P()
	{
		boolean interrupted = false;
		while(count == 0)
		{
			try
			{
				wait();
			}
			catch(InterruptedException ie)
			{
				ie.printStackTrace();
				interrupted = true;
			}
		}
		count --;
		if(interrupted)
		{
			Thread.currentThread().interrupt();
		}
	}
	
	public synchronized void V()
	{
		count ++;
		notify();
	}
	
	public synchronized void setCount(int i)
	{
		count = i;
	}
}
