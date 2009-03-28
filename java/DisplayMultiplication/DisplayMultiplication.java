import java.awt.*;
import javax.swing.*;

public class DisplayMultiplication extends JFrame
{
	DisplayMultiplication()
	{
		MulPanel mulpanel = new MulPanel();
		getContentPane().add(mulpanel);
	}
	public static void main(String[] args)
	{
		DisplayMultiplication frame = new DisplayMultiplication();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,500);
	
		frame.setTitle("Display multiplication table");
		frame.setVisible(true);
	}
	
}
//supose that height is longer than width
class MulPanel extends JPanel
{
	public MulPanel()
	{
		System.out.println("Creating MulPanel");
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		
		
		//declare the frame's height and width
		int xsize=getSize().width;
		int ysize=getSize().height;
		//declare title and table
		String title="Multiplication Table";
	//	StringBuffer table;
		String table="";
		//set font
//		setFont(new Font("SanSerif",Font.BOLD,20));
		FontMetrics fm=g.getFontMetrics();
		int w=fm.stringWidth(title);
		int h=fm.getAscent();
		
		int x1=(xsize-w)/2;
		int y1=(ysize-xsize+h)/2;
			
		g.drawString(title,x1,y1);
		
		//diaplay multiplication table
	    int i=0;
	    int xarea=xsize-20;
	    int yarea=xarea;
	    
	    for(;i<10;i++)
	    {
	    	table=table+"  "+i;
	    }
	    
	    w=fm.stringWidth(table);
		h=fm.getAscent();
		System.out.println(table);
		x1=(xsize-w)/2;
		y1=ysize-xsize+(20+h)/2;
		
		g.drawString(table,x1,y1);		
	    
	    
	}
}

/*	    	try
	    	{
		    	Thread.sleep(500);
	    	}
	    	catch(InterruptedException ie)
	    	{
	    		ie.printStackTrace();
	    	}
*/
 