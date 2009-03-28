package cn.edu.nju.elegate;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

/**
 * this panel is for the game room
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class GameRoomPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roomname;
	private ArrayList<Partner> roomMembers;
	private Othello parent;
	private JPanel  tablesPanel;
	private DefaultTableModel tableModel;
	private JTable  usersTable;
	public  static ImageIcon[] icons;
	private static ImageIcon[] background;
	private static ImageIcon[] chessBoardIcons;
	private static ImageIcon currentBackground;
	static
	{
		FilenameFilter filter=new ImageFileNameFilter();
		File file=new File("resources"
					+File.separator+"icons"+File.separator
					+"chessboard"+File.separator);
		File[] files=file.listFiles(filter);
		chessBoardIcons=new ImageIcon[files.length];
		for(int i=0;i<chessBoardIcons.length;i++)
			chessBoardIcons[i] = new ImageIcon(files[i].getAbsolutePath());
		
		file=new File("resources"
				+File.separator+"faces"+File.separator);
		files=file.listFiles(filter);
		icons=new ImageIcon[files.length];
		for(int i=0;i<files.length;i++)
		{
			icons[i]=new ImageIcon(files[i].getAbsolutePath());
		}
		file=new File("resources"+File.separator+"background"
				+File.separator);
		files=file.listFiles(filter);
		background=new ImageIcon[files.length];
		for(int i=0;i<files.length;i++)
		{
			background[i]=new ImageIcon(files[i].getAbsolutePath());
		}
		int ran=(int)(Math.random()*background.length);
		currentBackground=background[ran];
	}
	
	public static ImageIcon getPlayerIcon()
	{
		int ran=(int)(Math.random()*icons.length);
		return icons[ran];
	}
	
	
	public GameRoomPanel(ArrayList<Partner> roomMembers,String roomname,Othello parent)
	{
		this.roomMembers=roomMembers;
		this.roomname=roomname;
		this.parent=parent;
		tablesPanel=new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Dimension d=this.getSize();
				g.drawImage(currentBackground.getImage(),0,0,d.width,d.height,this);
			}
		};
		tablesPanel.setLayout(new GridLayout(0,2,10,10));
		String[] columns={"ID","Sex","Email","Win","Lose","Draw","Marks","IP","Port"};
		tableModel=new DefaultTableModel(columns,0){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row,
                    int column)
			{
				return false;
			}
		};
		usersTable=new JTable(tableModel);
		JTabbedPane tabbedPane=new JTabbedPane();
		tabbedPane.add("Tables",new JScrollPane(tablesPanel));
		tabbedPane.add("Users",new JScrollPane(usersTable));
		this.setLayout(new BorderLayout());
		this.add(tabbedPane,BorderLayout.CENTER);
		this.rearrangeComponents();
		this.updateUsers();
		this.setMinimumSize(this.getPreferredSize());
	}
	
	private void updateUsers()
	{
		this.tableModel.setRowCount(0);
		for(Partner ele:this.roomMembers)
		{
			OnlineUserInfo user;
			if((user=ele.getFirstPlayer())!=null)
			{
				this.tableModel.addRow(user.toArray());
			}
			if((user=ele.getSecondPlayer())!=null)
			{
				this.tableModel.addRow(user.toArray());
			}
		}
	}
	
	public void setRoomMembers(ArrayList<Partner> members)
	{
		this.roomMembers=members;
		int ran=(int)(Math.random()*background.length);
		currentBackground=background[ran];
		rearrangeComponents();
		updateUsers();
	}
	
	public void setRoomMembers(ArrayList<Partner> members,String roomname)
	{
		this.setRoomMembers(members);
		this.roomname=roomname;
	}
	
	protected void quitRoom()
	{
		for(Partner ele:this.roomMembers)
		{
			if(ele.removePlayer(parent.getID()))
				break;
		}
		this.rearrangeComponents();
	}
	
	private void rearrangeComponents()
	{
		ActionListener listener=new BtnSelectChessBoardL();
		tablesPanel.removeAll();
		for(int index=0;index<roomMembers.size();index++)
		{
			Partner p=roomMembers.get(index);
			int ran=(int)(Math.random()*icons.length);
			UserInfo first=p.getFirstPlayer();
			UserInfo second=p.getSecondPlayer();
			
			JButton btnFirst=new JButton(icons[ran]);
			btnFirst.setActionCommand(String.valueOf(index));
			if(first!=null)
			{
				btnFirst.setText(first.getNickname());
				btnFirst.setVerticalTextPosition(SwingConstants.BOTTOM);
				btnFirst.setHorizontalTextPosition(SwingConstants.CENTER);
			}
			btnFirst.addActionListener(listener);
			ran=(int)(Math.random()*icons.length);
			JButton btnSecond=new JButton(icons[ran]);
			btnSecond.setActionCommand(String.valueOf(index));
			if(second!=null)
			{
				btnSecond.setText(p.getSecondPlayer().getNickname());
				btnSecond.setVerticalTextPosition(SwingConstants.TOP);
				btnSecond.setHorizontalTextPosition(SwingConstants.CENTER);
			}
			btnSecond.addActionListener(listener);
			
			Box b=Box.createHorizontalBox();
			b.add(Box.createHorizontalStrut(20));
			b.add(btnFirst);
			b.add(Box.createHorizontalStrut(20));
			ran=(int)(Math.random()*chessBoardIcons.length);
			b.add(new JLabel(chessBoardIcons[ran]));
			b.add(Box.createHorizontalStrut(20));
			b.add(btnSecond);
			tablesPanel.add(b);
		}
	}
	

	
	class BtnSelectChessBoardL implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0) 
		{
			JButton btn=(JButton)arg0.getSource();
			int index=Integer.parseInt(btn.getActionCommand());
			Partner p=roomMembers.get(index);
			String id=parent.getID();
			if(p.contains(id))
			{
				JOptionPane.showMessageDialog(parent
						,"You haved joined the room!"
						,"Warning",JOptionPane.WARNING_MESSAGE);
			}
			else if(btn.getText().length()==0)
			{
				UserInfo user=p.getOpponent();
				String opponent=(user!=null? user.getNickname():null);
				parent.sendJoinRoomMsg(roomname,opponent);
			}
			else if(p.isAllHandsUp())
			{
				parent.changeToWatcher(p);
			}
			else if(!p.hasSeatLeft())
			{
				JOptionPane.showMessageDialog(parent
						,"The game hasn't started,so you can't enter observer mode!"
						,"Warning",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(parent
						,"The seat has been occupied,please press button refresh!"
						,"Warning",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}


class ImageFileNameFilter implements FilenameFilter
{

	public boolean accept(File dir, String name)
	{
		if(name.endsWith(".jpeg")||name.endsWith(".jpg")
				||name.endsWith(".gif"))
		{
			return true;
		}
		return false;
	}
	
}