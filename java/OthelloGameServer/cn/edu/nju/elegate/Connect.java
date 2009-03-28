package cn.edu.nju.elegate;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * solve the connection to game server
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class Connect implements Runnable
{
	private static Properties properties=new Properties();
	public static final int WIN=10;
	public static final int LOSE=-10;
	public static final int HALF_QUIT=5;
	public static final int DRAW=5;
	static
	{
		try
		{
			FileInputStream in=new FileInputStream("resources"
					+File.separator+"config"+File.separator+"database.properties");
			properties.load(in);
			Class.forName(properties.getProperty("driver","sun.jdbc.odbc.JdbcOdbcDriver")) ;
			checkForTableExist();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
   }
	
	private Socket skt;
	private OthelloGameServer parent;
		
	public Connect(Socket skt,OthelloGameServer parent)
	{
		this.skt=skt;
		this.parent=parent;
	}
	
	public void run()
	{
		try
		{
			ObjectInputStream in=new ObjectInputStream
			(skt.getInputStream());
			ObjectOutputStream out=new ObjectOutputStream
			(skt.getOutputStream());
			Msg msg=(Msg)in.readObject();
			skt.shutdownInput();
			Msg ansMsg=dispatchMsg(msg);
			out.writeObject(ansMsg);
			out.flush();
			out.close();
			skt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private Msg dispatchMsg(Msg msg)
	{
		Msg.MsgType type=msg.getMsgType();
		if(type==Msg.MsgType.REG_MSG)
		{
			return registerUser((RegMsg)msg);
		}
		else if(type==Msg.MsgType.LOGIN_MSG)
		{
			return userLogin((LoginMsg)msg);
		}
		else if(type==Msg.MsgType.LOGOUT_MSG)
		{
			return userLogout((LogoutMsg)msg);
		}
		else if(type==Msg.MsgType.QUERY_ROOM_MEMBERS_MSG)
		{
			return queryRoomMembers((QueryRoomMembersMsg)msg);
		}
		else if(type==Msg.MsgType.JOIN_ROOM_MSG)
		{
			return joinRoomMsg((JoinRoomMsg)msg);
		}
		else if(type==Msg.MsgType.QUIT_GAME_MSG)
		{
			return quitGameMsg((QuitGameMsg)msg);
		}
		else if(type==Msg.MsgType.HAND_UP_MSG)
		{
			return handUpMsg((HandUpMsg)msg);
		}
		else if(type==Msg.MsgType.PLAIN_MSG)
		{
			return transmitPalinMsg((PlainTextMsg)msg);
		}
		else if(type==Msg.MsgType.GAME_OVER_MSG)
		{
			return gameOverMsg((GameOverMsg)msg);
		}
		else if(type==Msg.MsgType.QUERY_TOP_USERS_MSG)
		{
			return queryTopUsersMsg((QueryTopUsersMsg)msg);
		}
		return null;
	}
	
	private Msg queryTopUsersMsg(QueryTopUsersMsg msg)
	{
		Msg ansMsg=null;
		try
		{
			Connection con=getConnection();
			Statement stmt=con.createStatement
			(ResultSet.TYPE_SCROLL_SENSITIVE
					,ResultSet.CONCUR_UPDATABLE);
			String nickname=msg.getID().toLowerCase();
			nickname=nickname.replaceAll("'","''");
			ResultSet rset=stmt.executeQuery("SELECT * FROM "
					+properties.getProperty("usersInfoTableName")+" ORDER BY Marks DESC");
			LinkedList<OrderedUserInfo> topUsers=new LinkedList<OrderedUserInfo>();
			int i=1;
			boolean isTopTen=false;
			while(rset.next())
			{
				String id=rset.getString("Nickname");
				String sex=rset.getString("Sex");
				String email=rset.getString("Email");
				int marks=rset.getInt("Marks");
				int win=rset.getInt("Win");
				int lose=rset.getInt("Lose");
				int draw=rset.getInt("Draw");
				if(i<11)
				{
					if(id.equals(msg.getID().toLowerCase()))
						isTopTen=true;
					OrderedUserInfo userInfo=new OrderedUserInfo
					(id,sex,email,win,lose,draw,marks,i);
					topUsers.add(userInfo);
				}
				else if(!isTopTen && rset.getString("ID").equals
						(msg.getID().toLowerCase()))
				{
					OrderedUserInfo userInfo=new OrderedUserInfo
					(msg.getID(),sex,email,win,lose,draw,marks,i);
					topUsers.add(userInfo);
					break;
				}
				else if(isTopTen)
					break;
				i++;
			}
			con.close();
			ansMsg=new QueryTopUsersAnsMsg(topUsers);
		}
		catch(Exception e)
		{
			ansMsg=new QueryTopUsersAnsMsg(e.toString());
			e.printStackTrace();
		}
		return ansMsg;
	}
	
	private Msg gameOverMsg(GameOverMsg msg)
	{
		String ret=parent.gameOverMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG,ret);
	}
	
	private Msg transmitPalinMsg(PlainTextMsg msg)
	{
		parent.transmitPlainMsg(msg);
		return new Msg(Msg.MsgType.ANS_MSG);
	}
	
	private Msg handUpMsg(HandUpMsg msg)
	{
		String ret=parent.userHandUp(msg.getRoomname(),msg.getID());
		if(ret==null)
			return new Msg(Msg.MsgType.ANS_MSG);
		else
			return new Msg(Msg.MsgType.ANS_MSG,ret);
	}
	
	private Msg quitGameMsg(QuitGameMsg msg)
	{
		OnlineUserInfo userInfo=parent.quitGame(msg.getID(),msg.getRoomname(),msg.getOpponent());
		if(userInfo!=null)
		{
			parent.sendUpdateRoomInfoMsgToAllUsers(msg.getRoomname());
			return new QuitGameAnsMsg(userInfo);
		}
		else
			return new Msg(Msg.MsgType.ANS_MSG,"You haven't joined the room yet!");
		
	}
	
	private Msg joinRoomMsg(JoinRoomMsg msg)
	{
		String ret=parent.joinRoom(msg.getRoomname(),msg.getID(),msg.getOpponent());
		OnlineUserInfo userInfo=parent.getOnlineUserInfo(msg.getOpponent());
		if(ret==null)
		{
			parent.sendUpdateRoomInfoMsgToAllUsers(msg.getRoomname());
			if(userInfo!=null)
				return new JoinRoomAnsMsg(userInfo,msg.getRoomname());
			else
				return new JoinRoomAnsMsg(null,msg.getRoomname());
		}
		else
			return new JoinRoomAnsMsg(ret);
	}
	
	private Msg queryRoomMembers(QueryRoomMembersMsg msg)
	{
		Msg ret=null;
		ArrayList<Partner> list=parent.getRoomMembers(msg.getRoomName());
		if(list!=null)
			ret=new QueryRoomMembersAnsMsg(list);
		else
			ret=new QueryRoomMembersAnsMsg("The room doesn't exist");
		return ret;
	}
	
	private Msg userLogin(LoginMsg msg)
	{
		Msg ansMsg=null;
		if(parent.isUserAreadyLogin(msg.getUsername()))
		{
			return new Msg(Msg.MsgType.ANS_MSG,"You have aready login");
		}
		Connection con=null;
		try
		{
			con=getConnection();
			
			Statement stmt=con.createStatement
			(ResultSet.TYPE_SCROLL_SENSITIVE
					,ResultSet.CONCUR_UPDATABLE);
			String nickname=msg.getUsername().toLowerCase();
			nickname=nickname.replaceAll("'","''");
			ResultSet rset=stmt.executeQuery("Select * from "
					+properties.getProperty("usersInfoTableName")
					+" where nickname=\'"
					+ nickname +"\'");
			String ret=null;
			if(rset.next())
			{
				if(!rset.getString("Password").equals(msg.getPassword()))
					ret="Incorrect password";
			}
			else
			{
				ret="The name not exist";
			}
			if(ret==null)
			{
				String sex=rset.getString("Sex");
				String email=rset.getString("Email");
				int marks=rset.getInt("Marks");
				int win=rset.getInt("Win");
				int lose=rset.getInt("Lose");
				int draw=rset.getInt("Draw");
				OnlineUserInfo userInfo=new OnlineUserInfo(nickname
						,sex,email,msg.getIP(),msg.getPort(),marks,win,lose,draw);
				parent.addOnlineUser(userInfo);
				ansMsg=new LoginAnsMsg(parent.getRooms(),userInfo);
			}
			else
				ansMsg=new LoginAnsMsg(null,null,ret);
			con.commit();
			con.close();
			return ansMsg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new LoginAnsMsg(null,null,e.toString());
		}
		finally
		{
			if(con!=null)
			{
				try
				{
					con.close();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
	
	private Msg userLogout(LogoutMsg msg)
	{
		try
		{
			Connection con=getConnection();	
			String tableName=properties.getProperty
			("usersInfoTableName","UsersInfo");
			
					
			Statement stmt=con.createStatement
			(ResultSet.TYPE_SCROLL_SENSITIVE
					,ResultSet.CONCUR_UPDATABLE);
			String nickname=msg.getUsername().toLowerCase();
			nickname=nickname.replaceAll("'","''");
			ResultSet rset=stmt.executeQuery("Select * from "
					+tableName+" where nickname=\'"
					+ nickname +"\'");
			String ret=null;
			if(rset.next())
			{
				if(!rset.getString("Password").equals(msg.getPassword()))
					ret="Incorrect password";
			}
			else
			{
				ret="The name not exist";
			}
			if(ret==null)
			{
				parent.removeOnineUser(nickname,msg.getRoomname(),msg.getOpponent());
			}
			con.commit();
			con.close();
			return new Msg(Msg.MsgType.ANS_MSG,ret);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new LoginAnsMsg(null,null,e.toString());
		}
	}
	
	private Msg registerUser(RegMsg msg)
	{
		String ret=null;
		try
		{
			Connection con=getConnection();
			String tableName=properties.getProperty
			("usersInfoTableName","UsersInfo");
			UserInfo userInfo=msg.getUserInfo();
			String nickname=userInfo.getNickname().toLowerCase();
			nickname.replaceAll("'","''");
			Statement stmt=con.createStatement
			(ResultSet.TYPE_SCROLL_SENSITIVE
					,ResultSet.CONCUR_UPDATABLE);
				
			ResultSet rset=stmt.executeQuery("Select * from "+tableName+" where Nickname = \'"+nickname+"\'");
			if(! rset.next())
			{
				rset.moveToInsertRow();
				rset.updateString("Nickname",nickname);
				rset.updateString("Sex",userInfo.getSex());
				rset.updateString("Email",userInfo.getEmail());
				rset.updateString("Password",msg.getPassword());
				rset.updateInt("Marks",0);
				rset.updateInt("Win",0);
				rset.updateInt("Lose",0);
				rset.updateInt("Draw",0);
				rset.insertRow();
				parent.appendMsg("A ID of "+nickname+" is registered");
			}
			else
			{
				ret="The ID has existed";
			}
			con.commit();
			con.close();
			return new Msg(Msg.MsgType.ANS_MSG,ret);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new Msg(Msg.MsgType.ANS_MSG,e.toString());
		}
	}
	
	private static void checkForTableExist()
	{
		try
		{
			Connection con=getConnection();
			String tableName=properties.getProperty
			("usersInfoTableName","UsersInfo");
			DatabaseMetaData meta=con.getMetaData();
			ResultSet rs=meta.getTables(null,null,tableName,new String[]{"TABLE"});
			if(!rs.next())
			{
				Statement stat=con.createStatement();
				String command=properties.getProperty("usersInfoTableCreateCommand");
				stat.execute(command);
			}
			con.commit();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static Connection getConnection()
	{
		try
		{
			String url=properties.getProperty("url","jdbc:odbc:OthelloGameServer");
			String usr=properties.getProperty("usr","Elegate");
			String psw=properties.getProperty("password","jessica");
			Connection con = DriverManager.getConnection
			(url,usr,psw);
			return con;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected static void updateUserRecord(String id,int marks)
	{
		Connection con=getConnection();
		String tableName=properties.getProperty
		("usersInfoTableName","UsersInfo");
		try
		{
			Statement stmt=con.createStatement
			(ResultSet.TYPE_SCROLL_SENSITIVE
					,ResultSet.CONCUR_UPDATABLE);
			id=id.toLowerCase();
			id=id.replaceAll("'","''");
			ResultSet rset=stmt.executeQuery("Select * from "
					+tableName+" where Nickname = \'"+id+"\'");
			if(rset.next())
			{
				rset.updateInt("Marks",rset.getInt("Marks")+marks);
				if(marks==Connect.WIN)
					rset.updateInt("Win",rset.getInt("Win")+1);
				else if(marks==Connect.LOSE)
					rset.updateInt("Lose",rset.getInt("Lose")+1);
				else if(marks==Connect.DRAW)
					rset.updateInt("Draw",rset.getInt("Draw")+1);
				rset.updateRow();
			}
			con.commit();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
