package cn.edu.nju.elegate;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Communication extends Thread
{
	private Msg msg;
	private String host;
	private int port;
	private Othello parent;
	public Communication(Msg msg,String host,int port,Othello parent)
	{
		this.msg=msg;
		this.host=host;
		this.port=port;
		this.parent=parent;
	}
	
	public void run()
	{
		try
		{
			Socket skt=new Socket();
			skt.connect(new InetSocketAddress(host,port),5000);
			ObjectOutputStream out=new ObjectOutputStream(skt.getOutputStream());
			ObjectInputStream in=new ObjectInputStream(skt.getInputStream());
			out.writeObject(msg);
			out.flush();
			skt.shutdownOutput();
			Msg ansMsg=(Msg)in.readObject();
			if(ansMsg.getMsg()!= null)
			{
				JOptionPane.showMessageDialog(parent
						,ansMsg.getMsg()
						,"Warning",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				this.dispatchMsg(ansMsg,msg);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			parent.hideQueryDialog();
			JOptionPane.showMessageDialog(null
					,e
					,"Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void dispatchMsg(Msg ansMsg,Msg msg)
	{
		Msg.MsgType type=msg.getMsgType();
		if(type==Msg.MsgType.LOGIN_MSG)
		{
			LoginAnsMsg loginAnsMsg=(LoginAnsMsg)ansMsg;
			parent.setRoomsList(loginAnsMsg.getRooms());
			LoginMsg loginMsg = (LoginMsg)msg;
			parent.setSuccessfullLoginInfo
			(loginAnsMsg.getUserInfo(),loginMsg.getPassword());
		}
		else if(type==Msg.MsgType.REG_MSG)
		{
			JOptionPane.showMessageDialog(parent
					,"Register successful!"
					,"Information"
					,JOptionPane.INFORMATION_MESSAGE);
			parent.restoreRightPanel();
		}
		else if(type==Msg.MsgType.QUERY_ROOM_MEMBERS_MSG)
		{
			QueryRoomMembersAnsMsg ans=(QueryRoomMembersAnsMsg)ansMsg;
			String roomname=((QueryRoomMembersMsg)msg).getRoomName();
			parent.layoutGameRoomPanel(ans.getRoomMembers(),roomname);
		}
		else if(type==Msg.MsgType.JOIN_ROOM_MSG)
		{
			String roomname=((JoinRoomMsg)msg).getRoomname();
			JoinRoomAnsMsg ans=(JoinRoomAnsMsg)ansMsg;
			parent.layoutGamblingChessBoard(ans.getOpponent(),roomname);
		}
		else if(type==Msg.MsgType.QUIT_GAME_MSG)
		{
			parent.quitGameMsg((QuitGameAnsMsg)ansMsg);
			parent.restoreGameRoomPanel();
		}
		else if(type==Msg.MsgType.JOIN_WATCHER_MSG)
		{
			parent.updateChessboard((JoinWatcherAnsMsg)ansMsg);
		}
		else if(type==Msg.MsgType.QUERY_TOP_USERS_MSG)
		{
			parent.updateTopUsersMsg((QueryTopUsersAnsMsg)ansMsg);
		}
	}
}
