package cn.edu.nju.elegate;

import javax.swing.*;
import java.awt.*;

/**
 * a dialog to show a user's information
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class UserInfoDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblID;
	private JLabel lblWin;
	private JLabel lblLose;
	private JLabel lblDraw;
	private JLabel lblMark;
	private JLabel lblIP;
	private JLabel lblPort;
	private JLabel lblRoom;
	private static UserInfoDialog userInfoDialog=new UserInfoDialog();
	private UserInfoDialog()
	{
		this.setModal(false);
		this.setUndecorated(true);
		lblID=new JLabel("ID: ");
		lblWin=new JLabel("Win: ");
		lblLose=new JLabel("Lose: ");
		lblDraw=new JLabel("Draw: ");
		lblMark=new JLabel("Mark: ");
		lblIP=new JLabel("IP: ");
		lblPort=new JLabel("Port: ");
		lblRoom=new JLabel("Room: ");
		Box b=Box.createVerticalBox();
		b.add(lblID);
		b.add(Box.createVerticalStrut(10));
		b.add(lblWin);
		b.add(Box.createVerticalStrut(10));
		b.add(lblLose);
		b.add(Box.createVerticalStrut(10));
		b.add(lblDraw);
		b.add(Box.createVerticalStrut(10));
		b.add(lblMark);
		b.add(Box.createVerticalStrut(10));
		b.add(lblIP);
		b.add(Box.createVerticalStrut(10));
		b.add(lblPort);
		b.add(Box.createVerticalStrut(10));
		b.add(lblRoom);
		b.add(Box.createVerticalStrut(10));
		this.getContentPane().add(b,BorderLayout.CENTER);
		this.pack();
		this.getContentPane().setBackground(Color.CYAN);
	}
	public static UserInfoDialog instance()
	{
		return userInfoDialog;
	}
	public void showUserInfo(OnlineUserInfo userInfo,String room,Point p)
	{
		if(userInfo!=null)
		{
			lblID.setText("ID: "+userInfo.getNickname());
			lblWin.setText("Win: "+userInfo.getWin());
			lblLose.setText("Lose: "+userInfo.getLose());
			lblDraw.setText("Draw: "+userInfo.getDraw());
			lblMark.setText("Mark: "+userInfo.getMarks());
			lblIP.setText("IP: "+userInfo.getIP());
			lblPort.setText("Port: "+userInfo.getPort());
			if(room!=null)
				lblRoom.setText("Room: "+room);
			else
				lblRoom.setText("Room:       ");
				
		}
		else
		{
			lblID.setText  ("ID:         ");
			lblWin.setText ("Win:        ");
			lblLose.setText("Lose:       ");
			lblDraw.setText("Draw:       ");
			lblMark.setText("Mark:       ");
			lblIP.setText  ("IP:         ");
			lblPort.setText("Port:       ");
			lblRoom.setText("Room:       ");
		}
		userInfoDialog.pack();
		userInfoDialog.validate();
		userInfoDialog.setLocation(p);
		userInfoDialog.setVisible(true);
	}
}
