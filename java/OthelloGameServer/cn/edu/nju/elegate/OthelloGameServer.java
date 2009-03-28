package cn.edu.nju.elegate;

import javax.swing.*;
import javax.swing.tree.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the server gui frame
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class OthelloGameServer extends JFrame implements Runnable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH=600;
	private static final int HEIGHT=500;
	
	private static final Properties DefaultProperties=new Properties(); 
	private JTree userTree;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;
	private JTextArea txtInfo;
	private DefaultListModel usersListModel;
	private JList usersList;
	private boolean runFlag=true;
	private Properties properties=new Properties(DefaultProperties);
	private ServerSocket serverSkt;
	private Thread serverThread;
	private ThreadPool threadPool;
	private Map<String,RoomInfo> rooms;
	private Map<String,OnlineUserInfo> onlineUsers;
	private CheckUsersOnlineThread checkUsersThread;
	static
	{
		DefaultProperties.put("port","8000");
		DefaultProperties.put("roomfile","resources/config/rooms.txt");
	}
	
	public OthelloGameServer()
	{
		readPropertiesFromFileSystem();
		
		setMenus();
		
		rooms=Collections.synchronizedMap
		(new HashMap<String,RoomInfo>());
		
		onlineUsers=Collections.synchronizedMap
		(new HashMap<String,OnlineUserInfo>());
		
		loadRooms();
		
		threadPool = ThreadPool.instance();
		
		root = new DefaultMutableTreeNode("Rooms");
		for(RoomInfo ele:this.rooms.values())
		{
			DefaultMutableTreeNode node=new DefaultMutableTreeNode(ele);
			node.setAllowsChildren(true);
			root.add(node);
		}
		
		treeModel=new DefaultTreeModel(root);
		treeModel.setAsksAllowsChildren(true);
		userTree=new JTree(treeModel);
		userTree.setShowsRootHandles(true);
		usersListModel=new DefaultListModel();
		usersList=new JList(usersListModel);
		txtInfo=new JTextArea(20,10);
		JScrollPane treePane = new JScrollPane(userTree);
		treePane.setBorder(BorderFactory.createTitledBorder("Rooms"));
		JScrollPane txtPane = new JScrollPane(txtInfo);
		txtPane.setBorder(BorderFactory.createTitledBorder("Information"));
		JScrollPane listPane=new JScrollPane(usersList);
		listPane.setBorder(BorderFactory.createTitledBorder("Users List"));
		JTabbedPane tabbedPane=new JTabbedPane();
		tabbedPane.add("Users",listPane);
		tabbedPane.add("Information",txtPane);
		JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePane,tabbedPane);
		splitPane.setOneTouchExpandable(true);
		this.getContentPane().add(splitPane,BorderLayout.CENTER);
		
		
		startServerSocket();
		
		serverThread=new Thread(this);
		serverThread.start();
		
		this.setSize(WIDTH,HEIGHT);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width-WIDTH)/2,(d.height-HEIGHT)/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
				quitProgram();
			}
		});
		checkUsersThread=new CheckUsersOnlineThread();
		checkUsersThread.start();
	}
	
	private void loadRooms()
	{
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(properties.getProperty("roomfile")));
			String line=null;
			while((line=reader.readLine())!=null)
			{
				rooms.put(line,new RoomInfo(line));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void setMenus()
	{
		JMenu mnuFile=new JMenu("File");
		mnuFile.setMnemonic('F');
		JMenuItem mnuExit=new JMenuItem("Exit");
		mnuExit.setMnemonic('X');
		mnuExit.setAccelerator
		(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		mnuExit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				quitProgram();
			}
			
		});
		
		mnuFile.add(mnuExit);
		
		JMenuBar mnuBar=new JMenuBar();
		mnuBar.add(mnuFile);
		this.setJMenuBar(mnuBar);
	}
	
	protected Collection<OnlineUserInfo> getAllOnlineUsers()
	{
		return this.onlineUsers.values();
	}
	
	protected void transmitPlainMsg(PlainTextMsg msg)
	{
		for(OnlineUserInfo ele:this.onlineUsers.values())
		{
			if(!ele.getNickname().equalsIgnoreCase(msg.getFromID()))
				threadPool.start(new SendMsgThread(msg,ele.getIP(),ele.getPort()));
		}
	}
	
	
	protected boolean isUserAreadyLogin(String name)
	{
		return this.onlineUsers.containsKey(name);
	}
	
	protected boolean isUserAreadyLogin(UserInfo userInfo)
	{
		return this.onlineUsers.containsKey(userInfo.getNickname());
	}
	
	protected void addOnlineUser(OnlineUserInfo userInfo)
	{
	    this.onlineUsers.put(userInfo.getNickname().toLowerCase(),userInfo);
	    this.usersListModel.addElement(userInfo.getNickname().toLowerCase());
		txtInfo.append("User "+userInfo.getNickname()+" login\n");
		
	}
	
	protected OnlineUserInfo getOnlineUserInfo(String id)
	{
		return this.onlineUsers.get(id);
	}
	
	protected String userHandUp(String roomname,String id)
	{
		RoomInfo roomInfo=this.rooms.get(roomname);
		if(roomInfo==null)
			return "The room doesn't exist";
		else
		{
			Partner p=roomInfo.getMember(id);
			boolean b=p.handUP();
			if(p.isAllHandsUp())
			{
				sendStartGameMsg(p);
				this.sendUpdateRoomInfoMsgToAllUsers(roomname);
			}
			if(b)
				return null;
			else
				return "The table is full now";
			
		}
	}
	
	private void sendStartGameMsg(Partner p)
	{
		Msg msg1=new StartGameMsg("Black");
		Msg msg2=new StartGameMsg("White");
		OnlineUserInfo first=p.getFirstPlayer();
		OnlineUserInfo second=p.getSecondPlayer();
		threadPool.start( new SendMsgThread ( msg1,first.getIP(),first.getPort() ) );
		threadPool.start( new SendMsgThread ( msg2,second.getIP(),second.getPort() ) );
	}
	/**
	 * 
	 * @param roomname the roomname to join
	 * @param id       the user's id
	 * @param opponent the opponent selected
	 * @return null if succeed,the failure reason otherwise
	 */
	protected String joinRoom(String roomname,String id,String opponent)
	{
		if(this.onlineUsers.containsKey(id))
		{
			OnlineUserInfo userInfo=this.onlineUsers.get(id);
			RoomInfo roomInfo=this.rooms.get(roomname);
			//has joined the room
			if(roomInfo.containsMember(id))
			{
				return null;
			}
			//get the chessboard for the id
			Partner p=roomInfo.getEmptyMember(opponent);
			if(p!=null) //find a chessboard
			{
				p.addPlayer(userInfo);
				updateRoomsTree(roomname,userInfo);
				String str="User "+id+" join room:"+roomname;
				if(opponent==null)
					str+="\n";
				else
					str+=" play with "+opponent+"\n";
				this.txtInfo.append(str);
				return null;
			}
			else if(opponent!=null) //the chessboard is full now
				return "The seat has been occupied";
			else
				return "Room is full,try later"; //room is full now
		}
		return null;
	}
	
	private synchronized void updateRoomsTree(String roomName,UserInfo userInfo)
	{
		Enumeration e=root.children();
		while(e.hasMoreElements())
		{
			DefaultMutableTreeNode node=
				(DefaultMutableTreeNode)e.nextElement();
			RoomInfo roomInfo=(RoomInfo)node.getUserObject();
			if(roomInfo.getRoomName().equals(roomName))
			{
				DefaultMutableTreeNode newNode=new 
				DefaultMutableTreeNode(userInfo.getNickname());
				newNode.setAllowsChildren(false);
				treeModel.insertNodeInto(newNode,node,node.getChildCount());
				break;
			}
		}
	}
	
	protected ArrayList<Partner> getRoomMembers(String roomName)
	{
		RoomInfo room=this.rooms.get(roomName);
		if(room!=null)
			return room.getRoomMembers();
		else 
			return null;
	}
	
	
	protected UserInfo getOnlineUser(String nickname)
	{
		return this.onlineUsers.get(nickname.toLowerCase());
	}
	
	protected UserInfo removeOnlineUser(String nickname)
	{
		return this.removeOnineUser(nickname,null,null);
	}
	protected  UserInfo removeOnineUser(String nickname,String roomname,String opponent)
	{
		if(this.onlineUsers.containsKey(nickname.toLowerCase()))
		{
			this.quitGame(nickname,roomname,opponent);
		}
		UserInfo userInfo=this.onlineUsers.remove(nickname);
		this.usersListModel.removeElement(nickname.toLowerCase());
		if(userInfo!=null)
			txtInfo.append("User "+nickname+" logout\n");
		return userInfo;
	}
	
	protected OnlineUserInfo quitGame(String id,String roomname,String opponent)
	{
		if(roomname==null)
			return null;
		OnlineUserInfo userInfo=this.onlineUsers.get(id);
		Partner p=removeUserFromRooms(userInfo,roomname);
		this.removeUserFromTree(id);
		txtInfo.append("User "+id+" quit room:"+roomname+"\n");
		if(p!=null && p.contains(opponent))
		{
			if(p.getHandUpCount()>=1)
			{
				Connect.updateUserRecord(opponent,Connect.HALF_QUIT);
				this.onlineUsers.get(opponent).increaseMarks(5);
			}
			sendOpponentQuitMsg(opponent);
		}
		if(p!=null)
			return userInfo;
		else
		  return null;
	}
	
	protected String gameOverMsg(GameOverMsg msg)
	{
		String winner=msg.getWinner();
		String loser=msg.getLoser();
		String roomname=msg.getRoomname();
		OnlineUserInfo winnerUser=this.onlineUsers.get(winner);
		OnlineUserInfo loserUser=this.onlineUsers.get(loser);
		RoomInfo roomInfo=this.rooms.get(roomname);
		Partner p=roomInfo.getMember(winner);
		if(p.contains(winner)&&p.contains(loser)&&p.isAllHandsUp())
		{
			if(msg.getResult()==GameOverMsg.GameResult.WIN)
			{
				Connect.updateUserRecord(loser,Connect.LOSE);
				Connect.updateUserRecord(winner,Connect.WIN);
				winnerUser.increaseMarks(Connect.WIN);
				winnerUser.increaseWin();
				loserUser.increaseMarks(Connect.LOSE);
				loserUser.increaseLose();
			}
			else if(msg.getResult()==GameOverMsg.GameResult.DRAW)
			{
				Connect.updateUserRecord(loser,Connect.DRAW);
				Connect.updateUserRecord(winner,Connect.DRAW);
				winnerUser.increaseMarks(Connect.DRAW);
				winnerUser.increaseDraw();
				loserUser.increaseMarks(Connect.DRAW);
				loserUser.increaseDraw();
			}
			p.handDown();
			p.handDown();
			updateTableOccupierInfoWhenGameover(winnerUser,loserUser);
		}
		else
			return "Illegal game state!";
		return null;
	}
	
	private void updateTableOccupierInfoWhenGameover(OnlineUserInfo winner,OnlineUserInfo loser)
	{
		Msg msg1=new UpdatePartnerUserInfoMsg(winner,loser);
		Msg msg2=new UpdatePartnerUserInfoMsg(loser,winner);
		sendUpdateUserInfoMsg(msg1,winner);
		sendUpdateUserInfoMsg(msg2,loser);
	}
	
	protected void sendUpdateRoomInfoMsgToAllUsers(String roomname)
	{
		for(OnlineUserInfo ele:this.onlineUsers.values())
		{
			Msg msg=new QueryRoomMembersAnsMsg
			(this.rooms.get(roomname).getRoomMembers());
			threadPool.start(new SendMsgThread(msg,ele.getIP(),ele.getPort()));
		}
	}
	
	
	private void sendUpdateUserInfoMsg(Msg msg,OnlineUserInfo userInfo)
	{
		threadPool.start
		(new SendMsgThread(msg,userInfo.getIP()
				,userInfo.getPort()));
	}
	
	private void sendOpponentQuitMsg(String id)
	{
		OnlineUserInfo userInfo=this.onlineUsers.get(id);
		Msg msg=new OpponentQuitGameMsg(userInfo);
		threadPool.start(new SendMsgThread(msg,userInfo.getIP(),userInfo.getPort()));
	}
	
	protected Partner removeUserFromRooms(UserInfo userInfo,String roomname)
	{
		if(roomname==null)
			return null;
		RoomInfo roomInfo=this.rooms.get(roomname);
		return roomInfo.removePlayer(userInfo);
	}
	
	protected void appendMsg(String msg)
	{
		txtInfo.append(msg+"\n");
	}
	
	private synchronized void removeUserFromTree(String nickname)
	{
		Enumeration e=root.children();
		while(e.hasMoreElements())
		{
			DefaultMutableTreeNode node=
				(DefaultMutableTreeNode)e.nextElement();
			Enumeration enumeration=node.children();
			while(enumeration.hasMoreElements())
			{
				DefaultMutableTreeNode n=
					(DefaultMutableTreeNode)enumeration.nextElement();
				if(n.getUserObject().equals(nickname))
				{
					treeModel.removeNodeFromParent(n);
					return;
				}
			}
		}
	}
	
	protected ArrayList<String> getRooms()
	{
		return new ArrayList<String>(this.rooms.keySet());
	}
	
	private void quitProgram()
	{
		setVisible(false);
		dispose();
		new CloseServerThread().start();
	}
	
	
	class CloseServerThread extends Thread
	{
		public CloseServerThread()
		{
			
		}
		public void run()
		{
			try
			{
				runFlag=false;
				if( serverThread != null )
				{
					serverSkt.close();
					serverThread.join();
				}
				threadPool.shutdown();
				checkUsersThread.stopThread();
				System.exit(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void stopSever()
	{
		runFlag=false;
	}
	
	private void readPropertiesFromFileSystem()
	{
		try
		{
			FileInputStream in=new FileInputStream
			("resources"+File.separator+"config"+File.separator+"OthelloGame.properties");
			this.properties.load(in);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	private void startServerSocket()
	{
		try
		{
			serverSkt=new ServerSocket
			(Integer.parseInt(properties.getProperty("port")));
			setTitle("Othello Game Server("
					+InetAddress.getLocalHost().toString()+")");
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this,e
					,"Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void run() 
	{
		try
		{
			while(runFlag)
			{
				
				Socket skt=serverSkt.accept();
				dispatchClient(skt);
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	
	
	
	
	private void dispatchClient(Socket client)
	{
		Connect con=new Connect(client,this);
		threadPool.start(con);
	}
	
	
	
	public class CheckUsersOnlineThread extends Thread
	{
		private boolean runFlag=true;
		public CheckUsersOnlineThread()
		{
		}
		public void stopThread()
		{
			this.runFlag=false;
			try
			{
				this.join();
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
					for(OnlineUserInfo ele:getAllOnlineUsers())
					{
						Msg msg=new Msg(Msg.MsgType.CHECK_ONLINE);
						try
						{
							Socket skt=new Socket();
							skt.connect(new InetSocketAddress(ele.getIP(),ele.getPort()),5000);
							ObjectOutputStream out=new ObjectOutputStream(skt.getOutputStream());
							ObjectInputStream in=new ObjectInputStream(skt.getInputStream());
							out.writeObject(msg);
							out.flush();
							skt.shutdownOutput();
							in.readObject();
							skt.close();
						}
						catch(Exception e)
						{
							OthelloGameServer.this.removeOnlineUser(ele.getNickname());
							//e.printStackTrace();
						}
						
						Thread.sleep(1000);
					}
					Thread.sleep(10000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public static void main(String[] args)
	{
		new OthelloGameServer().setVisible(true);
	}
}



