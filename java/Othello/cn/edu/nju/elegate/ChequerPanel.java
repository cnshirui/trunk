package cn.edu.nju.elegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ChequerPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int GAP=2;
	private int pos;
	private int discsSum;
	private Chessboard chessBoard;
	private Othello parentFrame;
	
	public ChequerPanel(int pos,Chessboard board,Othello frame)
	{
		this.pos=pos;
		this.chessBoard=board;
		this.parentFrame=frame;
		this.addMouseListener(new MouseL());
		this.setToolTipText( Chessboard.HEADER[ pos%8 ] + ( (pos>>>3)+1 )+"("+pos+")" ) ;
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		if(parentFrame.isAntialiasing())
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING
				,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(parentFrame.getChessBoardBackground());
		Container c=this.getParent();
		Dimension d=c.getSize();
		int width=d.width/8;
		int height=d.height/8;
		this.setPreferredSize(new Dimension(width,height));
		//draw background
		
		Rectangle2D rectangle=new Rectangle2D.Double(GAP/2,GAP/2,width-GAP,height-GAP);
		g2.fill(rectangle);
		int radius=Math.min(width-3*GAP,height-3*GAP);
		int x=( width  - radius  ) /2;
		int y=( height - radius  ) /2;
		//draw chessman
		
		
		Ellipse2D ellipse=new Ellipse2D.Double(x,y,radius,radius);
		Chessboard.ChequerState state=chessBoard.getChequerState(pos);
		
		
		
		if(state==Chessboard.ChequerState.Black)
		{
			g2.setColor(Color.BLACK);
			g2.fill(ellipse);
		}
		else if(state==Chessboard.ChequerState.White)
		{
			g2.setColor(Color.WHITE);
			g2.fill(ellipse);
		}
		else if(chessBoard.isMoveValid(pos))
		{
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(3.0F));
			int gap=4*GAP;
			Line2D line1=new Line2D.Double(x+gap,y+gap,x+radius-gap,y+radius-gap);
			Line2D line2=new Line2D.Double(x+gap,y+radius-gap,x+radius-gap,y+gap);
			g2.draw(line1);
			g2.draw(line2);
		}
		if(discsSum==chessBoard.getTotalDiscs() && chessBoard.isOccupid(pos))
		{
			int r=2*GAP;
			ellipse=new Ellipse2D.Double(width/2-r,height/2-r,r*2,r*2);
			g2.setColor(Color.RED);
			g2.fill(ellipse);
		}
	}
	
    protected void immediatePaint()
    {
        paintComponent(this.getGraphics());
        
    }
    
	protected void placeChequer()
	{
		this.chessBoard.placeChequer(pos);
		discsSum=chessBoard.getTotalDiscs();
         immediatePaint();
	}
	
	protected void updateChessBoard(Chessboard board)
	{
		this.chessBoard=board;
		this.discsSum=0;
	}
	
	
	class MouseL extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(e.getButton()==1 && e.getClickCount()>=1)
			{
				if(chessBoard.isMoveValid(pos))
				{
					discsSum=chessBoard.getTotalDiscs()+1;
					parentFrame.stopTimer();
					parentFrame.placeChequer(pos);
					parentFrame.startTimer();
				}
			}
		}
	}
	
}
