package cn.edu.nju.elegate;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.*;
import javax.swing.*;

/**
 * a sign-on screen
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class SignonScreen extends JPanel
{
	public static void main(String[] args)
	{
		SignonScreen screen=new SignonScreen();
		JFrame frame=new JFrame();
		frame.setUndecorated(true);
		frame.getContentPane().add(screen);
		frame.setSize(screen.getSize());
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size=frame.getSize();
		frame.setLocation((int)(d.width-size.width)/2
				,(int)(d.height-size.height)/2);
		frame.setVisible(true);
		SignonThread thread=new SignonThread();
		try
		{
			thread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		frame.setVisible(false);
		frame.dispose();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon icon;
	private SignonScreen()
	{
		File file=new File("resources"+File.separator+"background"+File.separator);
		File[] icons=file.listFiles(new JpgFileNameFilter());
		icon=new ImageIcon(icons[(int)(Math.random()*icons.length)].getAbsolutePath());
		this.setSize(icon.getIconWidth(),icon.getIconHeight());
		icons=null;
		this.setForeground(Color.RED);
		this.setFont(new Font(this.getFont().getFontName(),Font.BOLD,40));
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING
				,RenderingHints.VALUE_ANTIALIAS_ON);
		icon.paintIcon(this,g2,0,0);
		FontRenderContext contex=g2.getFontRenderContext();
		String str="Othello Game Loading ......";
		Rectangle2D bounds=this.getFont().getStringBounds(str,contex);
		Dimension d=this.getSize();
		g2.drawString(str,(int)(d.width-bounds.getWidth())/2,(int)(d.height-bounds.getHeight())/2);
	}
	
}


class JpgFileNameFilter implements FilenameFilter
{

	public boolean accept(File dir, String name)
	{
		int index=name.lastIndexOf('.');
		String suffix=name.substring(index+1).toLowerCase();
		if(suffix.equals("jpg")||suffix.equals("jpeg"))
		{
			return true;
		}
		return false;
	}
	
}
/**
 * thread to start the main program
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
class SignonThread extends Thread
{
	public SignonThread()
	{
		this.start();
	}
	public void run()
	{
		Othello othello=new Othello();
		othello.setVisible(true);
	}
}
