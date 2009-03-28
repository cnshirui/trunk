package cn.edu.nju.elegate;

import java.util.Vector;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

/**
 * to display the icons to be sended to friend.
 * @author Elegate
 * @author cs department of NJU
 */
public class TrickWindow extends JWindow 
{
	private static final long serialVersionUID = 1L;
	
	public final static int CHAT_DIALOG=0;
	public final static int GROUP_DIALOG=1;
	
	private Othello parFrame=null;
	private JTable trickTable=null;
	public TrickWindow(Othello parent)
	{
		super(parent);
		this.parFrame=parent;
		
		TrickTableModel trickTableModel=new TrickTableModel();
		trickTable=new JTable(trickTableModel);
		trickTable.setRowHeight(25);
		trickTable.setRowMargin(5);
		trickTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		trickTable.setShowGrid(true);
		trickTable.setRowSelectionAllowed(false);
		trickTable.addMouseListener(new TrickTableMouseL());
		
		this.getContentPane().add("Center",new JScrollPane(trickTable));		
		this.setSize(300,250);
		trickTable.setFocusable(true);
		trickTable.addFocusListener(new FocusAdapter()
		{
			public void focusLost(FocusEvent e)
			{
				setVisible(false);
			}
			
		});
	}
	/**
	 * the table's MouseListener,double click the icon to choose it
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class TrickTableMouseL extends MouseAdapter
	{

		public void mouseClicked(MouseEvent arg0)
		{
			JTable trickTable=(JTable)arg0.getSource();
			TrickTableModel tableModel=(TrickTableModel)trickTable.getModel();
			if(arg0.getClickCount()==2&&arg0.getButton()==MouseEvent.BUTTON1)
			{
				int column=trickTable.getSelectedColumn();
				int row=trickTable.getSelectedRow();
				Icon icon=(Icon)tableModel.getValueAt(row,column);
				parFrame.sendPlainMessage(null,icon);
				setVisible(false);
			}
		}
		public void mouseEntered(MouseEvent arg0)
		{
			TrickWindow.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		public void mouseExited(MouseEvent arg0)
		{
			TrickWindow.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
	}
	/**
	 * user defined table model to display the icons
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class TrickTableModel extends DefaultTableModel
	{
		private static final long serialVersionUID = 1L;

		public TrickTableModel()
		{
			this.setColumnCount(12);
			for(int i=0;i<8;i++)
			{
				Object obj[]=new Object[12];
				for(int j=0;j<12;j++)
				{
					String imageSrcPath="resources"
						+File.separator+"trick"
						+File.separator+(12*i+j)+".gif";
					obj[j]=new ImageIcon(imageSrcPath);
				}
				this.addRow(obj);
			}
		}
	 
		public boolean isCellEditable(int row,int col)
		{
			return false;
		}
		
		@SuppressWarnings("unchecked")
		public Class getColumnClass(int column)
		{
			Vector v=(Vector)dataVector.elementAt(0);
			return v.elementAt(column).getClass();
		}
	}
}

