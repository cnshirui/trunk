package cn.edu.nju.elegate;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.*;

/**
 * a panel will be displayed when trying to login to the server
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class WaitingScreen extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String msg;
	private Icon icon;
	private Thread thread;
	private Othello parent;
	private Thread selfThread;
	
	public WaitingScreen(String msg,Othello parent)
	{
		this(msg,null,null,parent);
	}
	
	public WaitingScreen(String msg,Thread thread,Othello parent)
	{
		this(msg,null,thread,parent);
	}
	private WaitingScreen(String msg,Icon icon,Thread thread,Othello parent)
	{
		this.msg=msg;
		this.thread=thread;
		this.parent=parent;
		if(icon!=null)
			this.icon=icon;
		else
			this.icon=new ImageIcon("resources"
					+File.separator+"icons"
					+File.separator+"waiting.gif");
		Dimension d=new Dimension(this.icon.getIconWidth(),this.icon.getIconHeight());
		this.setMinimumSize(d);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2=(Graphics2D)g;
		FontRenderContext contex=g2.getFontRenderContext();
		Rectangle2D bounds=this.getFont().getStringBounds(msg,contex);
		icon.paintIcon(this,g,0,0);
		g2.drawString(msg,0,(int) ( icon.getIconHeight()+bounds.getHeight() ));
	}
	
	public void setMessage(String msg)
	{
		this.msg=msg;
	}
	public void setIcon(Icon icon)
	{
		if(icon!=null)
			this.icon=icon;
	}
	
	public void setTarget(Thread thread)
	{
		this.thread=thread;
	}
	
	public void join()
	{
		try
		{
			if( selfThread!=null && selfThread.isAlive() )
				selfThread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		selfThread=new Thread(this);
		selfThread.start();
	}
	
	public void run() 
	{
		try
		{
			thread.start();
			thread.join();
			parent.restoreRightPanel();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
