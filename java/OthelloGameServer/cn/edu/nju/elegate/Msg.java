package cn.edu.nju.elegate;

import java.io.*;
import java.util.*;

import javax.swing.Icon;
/**
 * this class is for network communication,represent the messages sended or received
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class Msg implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum MsgType
	{	
		REG_MSG,LOGIN_MSG,LOGOUT_MSG,QUERY_ROOM_MEMBERS_MSG
		,JOIN_ROOM_MSG,QUIT_GAME_MSG,ANS_MSG,MOVE_MSG
		,JOIN_GAME_MSG,HAND_UP_MSG,START_GAME_MSG
		,PLAIN_MSG,OPPONENT_QUIT_GAME_MSG,GAME_OVER_MSG
		,UPDATE_USERINFO_MSG,UPDATE_PARTNER_USERINFO_MSG
		,UPDATE_ROOM_MSG,JOIN_WATCHER_MSG,LEAVE_WATCHER_MSG
		,OBERSERVED_PLAYER_QUIT_GAME_MSG,JOIN_WATCHER_ANS_MSG
		,CHECK_ONLINE,QUERY_TOP_USERS_MSG,QUERY_TOP_USERS_ANS_MSG
	};
	
	String msg=null;
	private MsgType type=null;
	
	public Msg(MsgType type)
	{
		this.type=type;
		msg=null;
	}
	public Msg(MsgType type,String msg)
	{
		this.type=type;
		this.msg=msg;
	}
	public MsgType getMsgType()
	{
		return this.type;
	}
	
	protected void setMsgType(MsgType type)
	{
		this.type=type;
	}
	
	public String getMsg()
	{
		return this.msg;
	}
	
	public void setMsg(String msg)
	{
		this.msg=msg;
	}
	
	public String toString()
	{
		return this.getClass()+"[type="+type+",msg="+msg+"]";
	}
}

class ObservedPlayerQuitMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObservedPlayerQuitMsg()
	{
		super(Msg.MsgType.OBERSERVED_PLAYER_QUIT_GAME_MSG);
	}
}

class PlainTextMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Icon icon;
	public PlainTextMsg(String msg,String id)
	{
		this(msg,id,null);
	}
	public PlainTextMsg(String id,Icon icon)
	{
		this(null,id,icon);
	}
	public PlainTextMsg(String msg,String id,Icon icon)
	{
		super(Msg.MsgType.PLAIN_MSG,msg);
		this.id=id;
		this.icon=icon;
	}
	public String getFromID()
	{
		return this.id;
	}
	public Icon getIcon()
	{
		return icon;
	}
}

class MoveMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pos;
	public MoveMsg(int pos)
	{
		super(Msg.MsgType.MOVE_MSG);
		this.pos=pos;
	}
	public int getMovePos()
	{
		return this.pos;
	}
	public String toString()
	{
		return super.toString()+"[pos="+pos+"]";
	}
}


class GameOverMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum GameResult{WIN,LOSE,DRAW};
	private String winner;
	private String loser;
	private String roomname;
	private GameResult result;
	
	public GameOverMsg(String winner,String loser,String roomname,GameResult result)
	{
		super(Msg.MsgType.GAME_OVER_MSG);
		this.winner=winner;
		this.loser=loser;
		this.roomname=roomname;
		this.result=result;
	}
	public String getWinner()
	{
		return this.winner;
	}
	public String getLoser()
	{
		return this.loser;
	}
	public String getRoomname()
	{
		return this.roomname;
	}
	public GameResult getResult()
	{
		return this.result;
	}
	public String toString()
	{
		return this.getClass()+"[winner="+winner+",loser="+loser+",roomname="+roomname+",result="+result+"]";
	}
}

class UpdatePartnerUserInfoMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo userInfo;
	private OnlineUserInfo opponent;
	public UpdatePartnerUserInfoMsg(OnlineUserInfo userInfo,OnlineUserInfo opponent)
	{
		super(Msg.MsgType.UPDATE_PARTNER_USERINFO_MSG);
		this.userInfo=userInfo;
		this.opponent=opponent;
	}
	public OnlineUserInfo getUserInfo()
	{
		return this.userInfo;
	}
	public OnlineUserInfo getOpponentUserInfo()
	{
		return this.opponent;
	}
}

class UpdateUserInfoMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo userInfo;
	public UpdateUserInfoMsg(OnlineUserInfo userInfo)
	{
		this(userInfo,null);
	}
	public UpdateUserInfoMsg(OnlineUserInfo userInfo,String msg)
	{
		super(Msg.MsgType.UPDATE_USERINFO_MSG,msg);
		this.userInfo=userInfo;
	}
	public OnlineUserInfo getUserInfo()
	{
		return this.userInfo;
	}
}

class StartGameMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String turn;
	public StartGameMsg(String turn)
	{
		super(Msg.MsgType.START_GAME_MSG);
		this.turn=turn;
	}
	public String getTurn()
	{
		return this.turn;
	}
}

class HandUpMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String roomname;
	public HandUpMsg(String roomname,String id)
	{
		super(Msg.MsgType.HAND_UP_MSG);
		this.id=id;
		this.roomname=roomname;
	}
	public String getID()
	{
		return this.id;
	}
	public String getRoomname()
	{
		return this.roomname;
	}
	public String toString()
	{
		return this.getClass()+"[id="+id+",roomname="+roomname+"]";
	}
}


class RegMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfo userInfo;
	private String password;
	public RegMsg(UserInfo userInfo,String password)
	{
		this(userInfo,null,password);
	}
	public RegMsg(UserInfo userInfo,String msg,String password)
	{
		super(Msg.MsgType.REG_MSG,msg);
		this.userInfo=userInfo;
		this.password=password;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public UserInfo getUserInfo()
	{
		return this.userInfo;
	}
	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo=userInfo;
	}
	public String toString()
	{
		return super.toString()+"[userInfo="+userInfo+",password="+password+"]";
	}
}

class LoginMsg extends LogoutMsg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;
	public LoginMsg(String username,String password,String ip,int port)
	{
		this(username,password,null,ip,port);
		
	}
	public LoginMsg(String username,String password,String msg,String ip,int port)
	{
		super(username,password,null,msg);
		this.ip=ip;
		this.port=port;
		this.setMsgType(Msg.MsgType.LOGIN_MSG);
	}
	
	
	public String getIP()
	{
		return this.ip;
	}
	
	public int getPort()
	{
		return this.port;
	}
	public String toString()
	{
		return super.toString()+"[IP="+ip+",port="+port+"]";
	}
}

class LogoutMsg extends Msg
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roomname;
	private String opponent;
	private String username;
	private String password;
	public LogoutMsg(String username,String password)
	{
		this(username,password,null);
	}
	
	
	public LogoutMsg(String username,String password,String roomname)
	{
		this(username,password,roomname,null);
	}
	public LogoutMsg(String username,String password,String roomname,String opponent)
	{
		this(username,password,roomname,opponent,null);
	}
	public LogoutMsg(String username,String password,String roomname,String opponent,String msg)
	{
		super(Msg.MsgType.LOGOUT_MSG,msg);
		this.roomname=roomname;
		this.username=username;
		this.password=password;
		this.opponent=opponent;
	}
	public String getUsername()
	{
		return this.username;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getRoomname()
	{
		return this.roomname;
	}
	
	public String getOpponent()
	{
		return this.opponent;
	}
	public String toString()
	{
		return super.toString()+"[roomname="+roomname+"]";
	}
}

class QueryRoomMembersMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roomName;
	public QueryRoomMembersMsg(String roomName)
	{
		super(Msg.MsgType.QUERY_ROOM_MEMBERS_MSG);
		this.roomName=roomName;
	}
	public String getRoomName()
	{
		return this.roomName;
	}
	public String toString()
	{
		return super.toString()+"[roomname="+roomName+"]";
	}
}

class LeaveWatcherMsg extends JoinWatcherMsg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeaveWatcherMsg(OnlineUserInfo userInfo)
	{
		super(userInfo);
		this.setMsgType(MsgType.LEAVE_WATCHER_MSG);
	}
}

class JoinWatcherMsg extends JoinGameMsg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JoinWatcherMsg(OnlineUserInfo userInfo)
	{
		super(userInfo);
		this.setMsgType(Msg.MsgType.JOIN_WATCHER_MSG);
	} 
}

class JoinGameMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo userInfo;
	public JoinGameMsg(OnlineUserInfo userInfo)
	{
		super(Msg.MsgType.JOIN_GAME_MSG);
		this.userInfo=userInfo;
	}
	public OnlineUserInfo getUserInfo()
	{
		return this.userInfo;
	}
}

class JoinRoomMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String opponent;
	private String roomname;
	
	public JoinRoomMsg(String id,String roomname)
	{
		this(id,roomname,null);
	}
	public JoinRoomMsg(String id,String roomname,String opponent)
	{
		super(Msg.MsgType.JOIN_ROOM_MSG);
		this.id=id;
		this.roomname=roomname;
		this.opponent=opponent;
	}
	public String getID()
	{
		return this.id;
	}
	
	public String getOpponent()
	{
		return this.opponent;
	}
	
	public String getRoomname()
	{
		return this.roomname;
	}
	
	public String toString()
	{
		return super.toString()+"[id="+id+",roomname="
		+roomname+",opponent="+opponent+"]";
	}
}

class OpponentQuitGameMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo userInfo;
	public OpponentQuitGameMsg(OnlineUserInfo userInfo)
	{
		super(Msg.MsgType.OPPONENT_QUIT_GAME_MSG);
		this.userInfo=userInfo;
	}
	public OnlineUserInfo getUserInfo()
	{
		return this.userInfo;
	}
}


class QuitGameAnsMsg extends OpponentQuitGameMsg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuitGameAnsMsg(OnlineUserInfo userInfo)
	{
		super(userInfo);
		this.setMsgType(Msg.MsgType.ANS_MSG);
	}
}

class QuitGameMsg extends JoinRoomMsg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public QuitGameMsg(String id,String roomname)
	{
		this(id,roomname,null);
	}
	public QuitGameMsg(String id,String roomname,String opponent)
	{
		super(id,roomname,opponent);
		this.setMsgType(Msg.MsgType.QUIT_GAME_MSG);
	}
}


class JoinRoomAnsMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo opponent;
	private String roomname;
	public JoinRoomAnsMsg(OnlineUserInfo opponent,String roomname)
	{
		super(Msg.MsgType.JOIN_ROOM_MSG);
		this.opponent=opponent;
		this.roomname=roomname;
	}
	public JoinRoomAnsMsg(String msg)
	{
		super(Msg.MsgType.JOIN_ROOM_MSG,msg);
	}
	
	public String getRoomname()
	{
		return this.roomname;
	}
	
	public OnlineUserInfo getOpponent()
	{
		return this.opponent;
	}
}
class QueryRoomMembersAnsMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Partner> roomMembers;
	public QueryRoomMembersAnsMsg(String msg)
	{
		super(Msg.MsgType.UPDATE_ROOM_MSG,msg);
	}
	public QueryRoomMembersAnsMsg(ArrayList<Partner> roomMembers)
	{
		super(Msg.MsgType.UPDATE_ROOM_MSG);
		this.roomMembers=roomMembers;
	}
	public ArrayList<Partner> getRoomMembers()
	{
		return this.roomMembers;
	}
}

class LoginAnsMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> rooms;
	private OnlineUserInfo userInfo;
	public LoginAnsMsg(ArrayList<String> rooms,OnlineUserInfo userInfo)
	{
		this(rooms,userInfo,null);
	}
	
	public LoginAnsMsg(ArrayList<String> rooms,OnlineUserInfo userInfo,String msg)
	{
		super(Msg.MsgType.ANS_MSG,msg);
		this.rooms=rooms;
		this.userInfo=userInfo;
	}
	
	public OnlineUserInfo getUserInfo()
	{
		return this.userInfo;
	}
	
	public Collection<String> getRooms()
	{
		return this.rooms;
	}
}

class QueryTopUsersMsg extends Msg
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	public QueryTopUsersMsg(String id)
	{	
		super(Msg.MsgType.QUERY_TOP_USERS_MSG);
		this.id=id;
	}
	public String getID()
	{
		return this.id;
	}
}

class QueryTopUsersAnsMsg extends Msg
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<OrderedUserInfo> topUsers;
	public QueryTopUsersAnsMsg(String msg)
	{
		super(Msg.MsgType.QUERY_TOP_USERS_ANS_MSG,msg);
	}
	public QueryTopUsersAnsMsg(Collection<OrderedUserInfo> topUsers)
	{
		super(Msg.MsgType.QUERY_TOP_USERS_ANS_MSG);
		this.topUsers=topUsers;
	}
	public Collection<OrderedUserInfo> getTopUsers()
	{
		return this.topUsers;
	}
}
