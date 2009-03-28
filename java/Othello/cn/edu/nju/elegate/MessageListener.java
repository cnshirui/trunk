package cn.edu.nju.elegate;

import java.io.*;
import java.net.*;

/**
 * listen the messages sended to this client
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class MessageListener extends Thread
{
	private Othello parent;
	private ServerSocket serverSkt;
	private boolean runFlag;
	private ThreadPool threadPool;
	public MessageListener(Othello parent)
	{
		this.parent=parent;
		runFlag=true;
		this.threadPool=ThreadPool.instance();
		try
		{
			serverSkt=new ServerSocket(0);
		}
		catch(Exception e)
		{
			runFlag=false;
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public String  getHostIP()
	{
		String ip="localhost";
		try
		{
			ip=InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ip;
	}
	
	public int getOpeningPort()
	{
		if(this.serverSkt!=null)
			return this.serverSkt.getLocalPort();
		else
			return -1;
	}
	
	private void dispatchMsg(Socket skt)
	{
		threadPool.start(new RemoteMessageDispatcher(skt,parent));
	}
	
	public void stopListener()
	{
		try
		{
			runFlag=false;
			serverSkt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(runFlag)
		{
			try
			{
				Socket skt=serverSkt.accept();
				dispatchMsg(skt);
			}
			catch(Exception e)
			{
				
			}
		}
	}
}


class RemoteMessageDispatcher implements Runnable
{

	private Othello parent;
	private Socket skt;
	
	public RemoteMessageDispatcher(Socket skt,Othello parent)
	{
		this.parent=parent;
		this.skt=skt;
	}
	public void run() 
	{
		try
		{
			ObjectInputStream in=new ObjectInputStream(skt.getInputStream());
			ObjectOutputStream out=new ObjectOutputStream(skt.getOutputStream());
			Msg msg=(Msg)in.readObject();
			Msg ansMsg=dispatchMsg(msg);
			out.writeObject(ansMsg);
			out.flush();
			skt.close();
		}
		catch(Exception e)
		{
			
		}
	}
	
	private Msg dispatchMsg(Msg msg)
	{
		Msg.MsgType type=msg.getMsgType();
		if(type==Msg.MsgType.MOVE_MSG)
		{
			return moveMsg((MoveMsg)msg);
		}
		else if(type==Msg.MsgType.JOIN_GAME_MSG)
		{
			return joinGameMsg((JoinGameMsg)msg);
		}
		else if(type==Msg.MsgType.START_GAME_MSG)
		{
			return startGameMsg((StartGameMsg)msg);
		}
		else if(type==Msg.MsgType.PLAIN_MSG)
		{
			return plainTextMsg((PlainTextMsg)msg);
		}
		else if(type==Msg.MsgType.OPPONENT_QUIT_GAME_MSG)
		{
			return quitGameMsg((OpponentQuitGameMsg)msg);
		}
		else if(type==Msg.MsgType.UPDATE_USERINFO_MSG)
		{
			return updateUserInfoMsg((UpdateUserInfoMsg)msg);
		}
		else if(type==Msg.MsgType.UPDATE_PARTNER_USERINFO_MSG)
		{
			return updatePartnerUserInfoMsg((UpdatePartnerUserInfoMsg)msg);
		}
		else if(type==Msg.MsgType.UPDATE_ROOM_MSG)
		{
			return updateGameRoomMembersMsg((QueryRoomMembersAnsMsg)msg);
		}
		else if(type==Msg.MsgType.JOIN_WATCHER_MSG)
		{
			return joinWatcherMsg( (JoinWatcherMsg) msg);
		}
		else if(type==Msg.MsgType.LEAVE_WATCHER_MSG)
		{
			return leaveWatcherMsg((LeaveWatcherMsg)msg);
		}
		else if(type==Msg.MsgType.OBERSERVED_PLAYER_QUIT_GAME_MSG)
		{
			return observedPlayerQuitGameMsg((ObservedPlayerQuitMsg)msg);
		}
		else if(type==Msg.MsgType.CHECK_ONLINE)
		{
			return new Msg(Msg.MsgType.ANS_MSG);
		}
		else if(type==Msg.MsgType.JOIN_WATCHER_ANS_MSG)
		{
			parent.updateChessboard((JoinWatcherAnsMsg)msg);
			return new Msg(Msg.MsgType.ANS_MSG);
		}
		return null;
	}
	
	private Msg observedPlayerQuitGameMsg(ObservedPlayerQuitMsg msg)
	{
		parent.quitObserverMode(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg leaveWatcherMsg(LeaveWatcherMsg msg)
	{
		parent.removeWatcher(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg joinWatcherMsg(JoinWatcherMsg msg)
	{
		Chessboard.ChessboardState state=parent.addWatcher(msg);
		return new JoinWatcherAnsMsg(state);
	}
	
	private Msg updateGameRoomMembersMsg(QueryRoomMembersAnsMsg msg)
	{
		parent.updateGameRoom(msg.getRoomMembers());
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg updatePartnerUserInfoMsg(UpdatePartnerUserInfoMsg msg)
	{
		parent.updatePartnerUserInfoMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg updateUserInfoMsg(UpdateUserInfoMsg msg)
	{
		parent.updateUserInfoMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg quitGameMsg(OpponentQuitGameMsg msg)
	{
		parent.quitGameMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg plainTextMsg(PlainTextMsg msg)
	{
		parent.receivePlainTextMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	private Msg startGameMsg(StartGameMsg msg)
	{
		parent.startGame(msg.getTurn());
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	private Msg joinGameMsg(JoinGameMsg msg)
	{
		parent.joinGame(msg.getUserInfo());
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg moveMsg(MoveMsg msg)
	{
		parent.moveMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
}

