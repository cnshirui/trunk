package cn.edu.nju.elegate;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * the main and the longest class for the main frame(GUI) 
 * both for stand-alone mode or network mode
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class Othello extends JFrame 
{
	
	
	public enum ProgramStatus{Stand_Alone,Network};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH=800;
	private static final int HEIGHT=600;
	
	private static Chessboard PracticeChessboard=new Chessboard();
	private static GamblingChessboard GamblingChessboard=new GamblingChessboard();
	private static ObserverModeChessboard ObserverModeChessboard 
	=new ObserverModeChessboard();
	
	private static Result result=new Result();;
	
	public  static final Color DEFAULT_CHESSBOARD_BACKGROUND = 
		new Color(51,120,59);
	
	private Color chessBoardColor=DEFAULT_CHESSBOARD_BACKGROUND;
	private Chessboard chessBoard;
	
	private ChequerPanel[] chequers;
	
	private JMenuItem mnuSave;
	private JMenuItem mnuLoad;
	private JMenuItem mnuTakeBack;
	private JMenuItem mnuStepForward;
	private JMenuItem mnuPracticeGame;
	/**
	 * new a network playing game
	 */
	private JButton btnNewGame;
	/**
	 * new a person to computer game
	 */
	private JButton btnNew;
	private JButton btnUndo;
	private JButton btnRedo;
	private JButton btnRefresh;
	private JButton btnQuitGame;
	private JButton btnQuitRoom;
	
	private JLabel lblBlack;
	private JLabel lblWhite;
	private JTextArea txtArea;
	private JButton btnStartGame;
	private JLabel lblUserInfo;
	private JLabel lblSelf;
	private JLabel lblOpponent;
	private JLabel lblTimer;
	private JTextPane txtPane;
	private JComboBox messageInput;
	
	private JPanel loginPanel;
	private JPanel chessBoardPanel;
	private JPanel tipPanel;
	private JPanel registerPanel;
	private JTabbedPane roomsAndTopUsersPane;
	private JPanel roomsPanel;
	private JPanel roomsListUpperPanel;
	private JPanel gameInfoPanel;
	private DefaultTableModel usersTableModel;
	private JTable topUsersTable;
	private GameRoomPanel gameRoomPanel;
	private WaitingScreen waitingScreen;
	private KeyL escKeyL=new KeyL();
	
	private DefaultListModel listModel;
	private JList roomsList;
	
	private boolean isLoginFail=true;
	private String roomnameJoined;
	
	private JTextField txtLoginID;
	private JPasswordField txtLoginPassword;
	private JTextField txtRegID;
	private JPasswordField txtRegPassword;
	private JPasswordField txtConfirmRegPassword;
	private JTextField txtRegEmail;
	private JComboBox comboSex;
	
	private QueryRoomMembersDialog queryDialog;
	private TrickWindow trickWindow;
	
	protected JSplitPane splitPane;
	
	//private double dividerPortion=1.0;
	
	private JToolBar toolBar;
	private JMenuBar mnuBar;
	private JFileChooser fileChooser;
	
	private boolean isFullScreen=false;
	private boolean isAntialiasing=false;
	private boolean isCurrentWaitingFroRegister=false;
	private boolean isPrivateChatting=true;
	
	private OnlineUserInfo selfUserInfo;
	private String loginPsw;
	private OnlineUserInfo opponentUserInfo;
	private MessageListener messageListener;
	private int depth=5;
	
	private String serverIp="localhost";
	private int serverPort=8000;
	
	private UserInfoDialog userInfoDialog=UserInfoDialog.instance();
	private ThreadPool threadPool=ThreadPool.instance();
	private List<OnlineUserInfo> watchers;
	private Partner observedPartner;

	private Timer timer;
	private ActionListener timerAction;
	private static final int ONE_SECOND=1000;
	private int deadSeconds=10;
	private int deadTimes=deadSeconds;
	
	
	private ProgramStatus programStatus=ProgramStatus.Stand_Alone;
	
	public Othello()
	{
		super("Othello");
		
		chessBoard=PracticeChessboard;
		watchers=Collections.synchronizedList(new LinkedList<OnlineUserInfo>());
		fileChooser=new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		DefaultFileFilter filter=new DefaultFileFilter("og","Game");
		fileChooser.setFileFilter(filter);
		
		lblUserInfo=new JLabel();
		lblUserInfo.addMouseListener(new LblUserInfoMouseL());
		
		setMenus();
		setToolbar();
		setChessBoard();
		setLoginPanel();
		setRegisterPanel();
		setRoomsListPanel();
		setGameInfoPanel();
		
		
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,chessBoardPanel,loginPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(1.0);
		splitPane.setContinuousLayout(true);
		
		this.getContentPane().add(splitPane,BorderLayout.CENTER);
		this.setSize(WIDTH,HEIGHT);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width-WIDTH)/2,(d.height-HEIGHT)/2);
		
		splitPane.setFocusable(true);
		splitPane.addKeyListener(escKeyL);
		
		this.setFocusable(true);
		this.addKeyListener(escKeyL);
		this.getContentPane().setFocusable(true);
		this.getContentPane().addKeyListener(escKeyL);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we)
			{
				quitProgram();
			}
			/*public void windowActivated(WindowEvent we)
			{
				txtLoginID.requestFocusInWindow();
			}*/
		});
		messageListener=new MessageListener(this);
		messageListener.start();
		this.setTitle("Othello@"
				+messageListener.getHostIP()
				+":"+messageListener.getOpeningPort());
		trickWindow=new TrickWindow(this);
	}
	
	private void forceMove()
	{
		this.placeChequer(chessBoard.getRandomMove());
		this.startTimer();
	}
	
	private void info(String msg,String title)
	{
		JOptionPane.showMessageDialog(this,msg
				,title
				,JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void moveMsg(MoveMsg msg)
	{		
		if(msg.getMovePos()!=-1)
		{
			int pos=msg.getMovePos();
			Chessboard.ChequerState turn=chessBoard.getCurrentTurn();
			this.chequers[pos].placeChequer();
			this.updateDisplay(pos>>>3,pos%3,turn);
		}
		else
		{
			info(this.chessBoard.getCurrentTurn()+" passes!","Pass");
			this.chessBoard.inverseTurn();
			repaint();
		}
		if(this.chessBoard.gameOver())
		{
			/*info("Black vs White:"
					+chessBoard.sumBlack()
					+"-"+chessBoard.sumWhite(),"Game Over");*/
			this.btnNewGame.setEnabled(true);
			return;
		}
		else if(!this.chessBoard.isCurrentTurnHasValidMove())
		{
			info("You have to pass","Pass");
			this.chessBoard.inverseTurn();   //opponent pass
			this.sendMoveMsg(-1);
			repaint();
		}
	}
	
	private void sendHandUpMsg()
	{
		Msg msg=new HandUpMsg(this.roomnameJoined,this.selfUserInfo.getNickname());
		threadPool.start(new Communication(msg,serverIp,serverPort,this));
	}
	
	private void setGameInfoPanel()
	{
		gameInfoPanel=new JPanel(new BorderLayout());
		btnNewGame=new JButton("New Game");
		btnNewGame.setMnemonic('G');
		btnNewGame.setEnabled(false);
		btnNewGame.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				Othello.this.chessBoard=GamblingChessboard;
				Othello.this.chessBoard.reset();
				Othello.this.btnStartGame.setEnabled(true);
				repaint();
				Othello.this.updateChequers();
				resetGameState();
				sendNewGameMsg();
				btnNewGame.setEnabled(false);
				btnStartGame.setEnabled(true);
			}
			
		});
		btnStartGame=new JButton("Start Game");
		btnStartGame.setMnemonic('S');
		btnStartGame.addActionListener(new BtnStartGameL());
		if(this.selfUserInfo!=null)
		{
			lblSelf=new JLabel(selfUserInfo.getNickname());
			lblSelf.setIcon(GameRoomPanel.getPlayerIcon());
			lblSelf.setVerticalTextPosition(SwingConstants.BOTTOM);
			lblSelf.setHorizontalTextPosition(SwingConstants.CENTER);
		}
		else
		{
			lblSelf=new JLabel(GameRoomPanel.getPlayerIcon());
			lblSelf.addMouseListener(new SelfUserInfoMouseL());
		}
		if(this.opponentUserInfo!=null)
		{
			lblOpponent=new JLabel(opponentUserInfo.getNickname());
			lblOpponent.setIcon(GameRoomPanel.getPlayerIcon());
			lblOpponent.setVerticalTextPosition(SwingConstants.BOTTOM);
			lblOpponent.setHorizontalTextPosition(SwingConstants.CENTER);
		}
		else
		{
			lblOpponent=new JLabel(GameRoomPanel.getPlayerIcon());
			lblOpponent.addMouseListener(new OpponentUserInfoMouseL());
		}
		txtPane=new JTextPane();
		txtPane.setEditable(false);
		messageInput=new JComboBox();
		messageInput.setEditable(true);
		messageInput.getEditor().addActionListener(new MessageInputL());
		JButton btnTrick=new JButton(GameRoomPanel.getPlayerIcon());
		btnTrick.addActionListener(new BtnTrickL());
		
		Box b=Box.createVerticalBox();
		b.add(Box.createVerticalStrut(20));
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnNewGame);
		btnPanel.add(btnStartGame);
		b.add(btnPanel);
		b.add(Box.createVerticalStrut(20));
		b.add(lblSelf);
		b.add(Box.createVerticalStrut(20));
		b.add(lblOpponent);
		b.add(Box.createVerticalStrut(20));
		JScrollPane scrollPane=new JScrollPane(txtPane);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(scrollPane,BorderLayout.CENTER);
		Box hb=Box.createHorizontalBox();
		hb.add(Box.createHorizontalStrut(20));
		hb.add(messageInput);
		hb.add(Box.createHorizontalStrut(10));
		hb.add(btnTrick);
		final JCheckBox checkPrivate=new JCheckBox("Private",true);
		checkPrivate.setMnemonic('P');
		checkPrivate.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				isPrivateChatting=checkPrivate.isSelected();
			}
			
		});
		hb.add(checkPrivate);
		hb.add(Box.createHorizontalStrut(20));
		//JPanel messagePanel =new JPanel();
		//messagePanel.add(hb);
		panel.add(hb,BorderLayout.SOUTH);
		b.add(panel);
		gameInfoPanel.add(b,BorderLayout.CENTER);
	}
	
	private void sendNewGameMsg()
	{
		Msg msg=new JoinGameMsg(this.selfUserInfo);
		threadPool.start(new Communication
				(msg,this.opponentUserInfo.getIP()
				,this.opponentUserInfo.getPort(),this));
	}
	
	protected void receivePlainTextMsg(PlainTextMsg msg)
	{
		if(!this.btnQuitGame.isEnabled())
			return;
		if(msg.getMsg()!=null)
			insertString(msg.getFromID()+" : "+msg.getMsg()+"\n");
		if(msg.getIcon()!=null)
		{
			insertString(msg.getFromID()+" : ");
		    insertIcon(msg.getIcon());
			insertString("\n");
		}
	}
	
	private void insertIcon(Icon i)
	{
		txtPane.setCaretPosition(txtPane.getDocument().getLength());
		txtPane.insertIcon(i);
	}
	private void insertString(String msg)
	{
		Document d=txtPane.getDocument();
		try
		{
				d.insertString(d.getLength()
					,msg
					,null);
				txtPane.setCaretPosition(d.getLength());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	protected void setServer(String ip,int port)
	{
		this.serverIp=ip;
		this.serverPort=port;
	}
	
	
	protected void sendPlainMessage(String msg,Icon icon)
	{
		if(msg!=null)
		{
			if(msg.trim().length()==0)
			{
				warning("You can't send empty message!");
				return;
			}
			insertString(selfUserInfo.getNickname()+" : "+msg+"\n");
		}
		if(icon!=null)
		{
			insertString(selfUserInfo.getNickname()+" : ");
			insertIcon(icon);
			insertString("\n");
		}
		Msg m=new PlainTextMsg(msg,this.selfUserInfo.getNickname(),icon);
		if(this.isPrivateChatting)
		{
			if(this.opponentUserInfo!=null)
			{
				threadPool.start(
						new Communication(m
						,this.opponentUserInfo.getIP()
						,this.opponentUserInfo.getPort(),this));
			}
		}
		else
		{
			threadPool.start(
					new Communication(m
					,serverIp
					,serverPort,this));
		}
	}
	
	class MessageInputL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			sendPlainMessage(messageInput.getEditor().getItem().toString(),null);
			messageInput.getEditor().setItem(null);
		}
		
	}
	
	private void setRoomsListPanel()
	{
		String[] columns={"ID","Sex","Email","Win"
				,"Lose","Draw","Marks","Rank"};
		
		usersTableModel=new DefaultTableModel(columns,0){

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
	
		topUsersTable=new JTable(usersTableModel);
		
		
		roomsPanel=new JPanel(new BorderLayout());
		
		listModel=new DefaultListModel();
		roomsList = new JList(listModel);
		roomsList.setVisibleRowCount(20);
		roomsList.addMouseListener(new RoomListMouseL());
		//roomsList.setFixedCellWidth(50);
		JScrollPane roomListPane=new JScrollPane(roomsList);
		roomListPane.setBorder(BorderFactory.createTitledBorder("Rooms List"));
		
		btnQuitRoom=new JButton("Quit Room");
		btnQuitRoom.setMnemonic('R');
		btnQuitRoom.setEnabled(false);
		btnQuitRoom.addActionListener(new BtnQuitRoomL());
		
		Box vb=Box.createVerticalBox();
		vb.add(lblUserInfo);
		vb.add(Box.createVerticalStrut(20));
		vb.add(btnQuitRoom);
		JPanel panel=new JPanel();
		panel.add(vb);
		roomsListUpperPanel=new JPanel(new BorderLayout());
		roomsListUpperPanel.add(panel,BorderLayout.NORTH);
		roomsListUpperPanel.add(roomListPane,BorderLayout.CENTER);
		roomsPanel.add(roomsListUpperPanel,BorderLayout.CENTER);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setMnemonic('R');
		btnRefresh.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				Othello.this.sendQueryRoomMembersMsg(roomnameJoined);
			}
			
		});
		
		btnRefresh.setEnabled(false);
		btnQuitGame=new JButton("Quit Game");
		btnQuitGame.setMnemonic('Q');
	    btnQuitGame.addActionListener(new BtnQuitGameL());
	    btnQuitGame.setEnabled(false);

		JButton btnLogout=new JButton("Logout");
		btnLogout.setMnemonic('L');
		btnLogout.addActionListener(new BtnLogoutL());
		
		JPanel btnPanel = new JPanel();
		btnPanel.add(btnRefresh);
		btnPanel.add(btnQuitGame);
		btnPanel.add(btnLogout);
		roomsPanel.add(btnPanel,BorderLayout.SOUTH);
		
		JButton btnRefreshTopUsers = new JButton("Refresh");
		btnRefreshTopUsers.setMnemonic('R');
		btnRefreshTopUsers.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				sendQueryTopUsersMsg();
			}
			
		});
		Box box=Box.createVerticalBox();
		box.add(new JScrollPane(topUsersTable));
		box.add(Box.createVerticalStrut(20));
		box.add(btnRefreshTopUsers);
		this.roomsAndTopUsersPane=new JTabbedPane();
		roomsAndTopUsersPane.add("Rooms",roomsPanel);
		roomsAndTopUsersPane.add("Top players",box);
	}
	
	protected void updateTopUsersMsg(QueryTopUsersAnsMsg msg)
	{
		this.usersTableModel.setRowCount(0);
		for(OrderedUserInfo ele:msg.getTopUsers())
		{
			this.usersTableModel.addRow(ele.toArray());
		}
	}
	
	private void sendQueryTopUsersMsg()
	{
		Msg msg=new QueryTopUsersMsg(this.selfUserInfo.getNickname());
		threadPool.start(new Communication(msg,serverIp,serverPort,this));
	}
	
	protected void setRoomsList(Collection<String> rooms)
	{
		isLoginFail=false;
		listModel.clear();
		for(String ele:rooms)
		listModel.addElement(ele);
	}
	
	private void setRegisterPanel()
	{
		registerPanel = new JPanel(new GridBagLayout());
		
		JLabel lblID=new JLabel("ID:");
		lblID.setDisplayedMnemonic('I');
		
		JLabel lblPassword=new JLabel("Passowrd:");
		lblPassword.setDisplayedMnemonic('P');
		
		JLabel lblConfirmPassword=new JLabel("Confirm Password:");
		lblConfirmPassword.setDisplayedMnemonic('P');
		
		JLabel lblSex = new JLabel("Sex:");
		lblSex.setDisplayedMnemonic('S');
		
		JLabel lblEmail=new JLabel("Email:");
		lblEmail.setDisplayedMnemonic('E');
		
		ActionListener listener=new BtnRegSubmitL();
		
		txtRegID=new JTextField(10);
		txtRegPassword=new JPasswordField(10);
		txtConfirmRegPassword=new JPasswordField(10);
		txtConfirmRegPassword.addActionListener(listener);
		txtRegEmail=new JTextField(10);
		txtRegEmail.addActionListener(listener);
		comboSex=new JComboBox(new String[]{"Male","Female"});
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setMnemonic('S');
		btnSubmit.addActionListener(listener);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(new BtnRegCancelL());
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnSubmit);
		btnPanel.add(btnCancel);
		GridBagConstraints gbCon=new GridBagConstraints();
		gbCon.anchor=GridBagConstraints.WEST;
		
		this.addComponent(lblID,1,1,1,1,gbCon,registerPanel);
		this.addComponent(txtRegID,2,1,1,1,gbCon,registerPanel);
		this.addComponent(lblPassword,1,2,1,1,gbCon,registerPanel);
		this.addComponent(txtRegPassword,2,2,1,1,gbCon,registerPanel);
		this.addComponent(lblConfirmPassword,1,3,1,1,gbCon,registerPanel);
		this.addComponent(txtConfirmRegPassword,2,3,1,1,gbCon,registerPanel);
		this.addComponent(lblSex,1,4,1,1,gbCon,registerPanel);
		this.addComponent(comboSex,2,4,1,1,gbCon,registerPanel);
		this.addComponent(lblEmail,1,5,1,1,gbCon,registerPanel);
		this.addComponent(txtRegEmail,2,5,1,1,gbCon,registerPanel);
		this.addComponent(btnPanel,1,6,3,1,gbCon,registerPanel);
		registerPanel.setMinimumSize(registerPanel.getPreferredSize());
	}
	
	private void setLoginPanel()
	{
		loginPanel=new JPanel(new GridBagLayout());
		//loginPanel.setFocusable(true);
		JLabel lblName=new JLabel("ID:");
		lblName.setDisplayedMnemonic('I');
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setHorizontalTextPosition(SwingConstants.LEFT);
		JLabel lblPassword=new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setHorizontalTextPosition(SwingConstants.LEFT);
		lblPassword.setDisplayedMnemonic('P');
		
		ActionListener listener=new BtnLoginL();
		
		txtLoginID=new JTextField(10);
		lblName.setLabelFor(txtLoginID);
		txtLoginPassword=new JPasswordField(10);
		txtLoginPassword.addActionListener(listener);
		lblPassword.setLabelFor(txtLoginPassword);
		
		JButton btnLogin=new JButton("Login");
		btnLogin.setMnemonic('L');
		btnLogin.addActionListener(listener);
		JButton btnRegister=new JButton("Register");
		btnRegister.setMnemonic('R');
		btnRegister.addActionListener(new BtnRegisterL());
		
		GridBagConstraints gbCon=new GridBagConstraints();
		gbCon.anchor=GridBagConstraints.WEST;
		
		this.addComponent(lblName,1,1,1,1,gbCon,loginPanel);
		this.addComponent(txtLoginID,2,1,1,1,gbCon,loginPanel);
		this.addComponent(lblPassword,1,2,1,1,gbCon,loginPanel);
		this.addComponent(txtLoginPassword,2,2,1,1,gbCon,loginPanel);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnLogin);
		btnPanel.add(btnRegister);
		this.addComponent(btnPanel,1,3,2,1,gbCon,loginPanel);
		
		loginPanel.setMinimumSize(loginPanel.getPreferredSize());
	}
	
	private void addComponent(Component c,int gridx,int gridy
			,int gridwidth,int gridheight
			,GridBagConstraints gbCon,Container container)
	{
		gbCon.gridx=gridx;
		gbCon.gridy=gridy;
		gbCon.gridwidth=gridwidth;
		gbCon.gridheight=gridheight;
		container.add(c,gbCon);
	}
	
	private void setChessBoard()
	{
		this.setTipPanel();
		JPanel chessBoardHeaderPanel=new JPanel(new GridLayout(1,8));
		for(int i=0;i<8;i++)
		{
			JLabel lbl=new JLabel(Chessboard.HEADER[i]);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setForeground(Color.BLUE);
			//lbl.setBackground(Color.RED);
			//lbl.setOpaque(true);
			chessBoardHeaderPanel.add(lbl);
		}
		JPanel chessBoardLeftHeaderPanel=new JPanel(new GridLayout(8,1));
		for(int i=0;i<8;i++)
		{
			JLabel lbl=new JLabel(" "+(i+1)+" ");
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setForeground(Color.BLUE);
			//lbl.setBackground(Color.RED);
			//lbl.setOpaque(true);
			chessBoardLeftHeaderPanel.add(lbl);
		}
		chequers=new ChequerPanel[64];
		JPanel chequersPanel=new JPanel(new GridLayout(8,8));
		for(int i=0;i<64;i++)
		{
			chequersPanel.add(
				( chequers[i] = new ChequerPanel ( i,chessBoard ,this) ) );
		}
	
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(chessBoardHeaderPanel,BorderLayout.NORTH);
		panel.add(chessBoardLeftHeaderPanel,BorderLayout.WEST);
		panel.add(chequersPanel,BorderLayout.CENTER);
		chessBoardPanel=new JPanel(new BorderLayout());
		chessBoardPanel.add(panel,BorderLayout.CENTER);
		chessBoardPanel.add(tipPanel,BorderLayout.EAST);
		chessBoardPanel.setMinimumSize
		(chessBoardPanel.getPreferredSize());
		//this.getContentPane().add(chessBoardPanel,BorderLayout.CENTER);
	}
	
	private void setTipPanel()
	{
		Box v=Box.createVerticalBox();
		lblBlack=new JLabel("Black: "
				+this.toTwoDigitsString(chessBoard.sumBlack())
				+" discs "+this.toTwoDigitsString(0)+" moves");
		lblWhite=new JLabel("White: "
				+this.toTwoDigitsString(chessBoard.sumWhite())
				+" discs "+this.toTwoDigitsString(0)+" moves");
		txtArea=new JTextArea(10,4);
		txtArea.setEditable(false);
		txtArea.getDocument().addDocumentListener(new DocL());
		
		JPanel currentTurnPanel=new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private static final int SIZE=70;

			public Dimension getPreferredSize()
			{
				return new Dimension(SIZE,SIZE);
			}
			public void paintComponent(Graphics g)
			{
				Ellipse2D ellipse=new Ellipse2D.Double(2,2,SIZE-4,SIZE-4);
				Rectangle2D rectangle=new Rectangle2D.Double(0,0,SIZE,SIZE);
				Graphics2D g2=(Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING
						,RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(DEFAULT_CHESSBOARD_BACKGROUND);
				g2.fill(rectangle);
				if(chessBoard.getCurrentTurn()==Chessboard.ChequerState.Black)
				{
					g2.setColor(Color.BLACK);
					g2.fill(ellipse);
				}
				else
				{
					g2.setColor(Color.WHITE);
					g2.fill(ellipse);
				}
			}
		};
		v.add(lblBlack);
		v.add(lblWhite);
		v.add(new JScrollPane(txtArea));
		v.add(Box.createVerticalStrut(50));
		JLabel lbl=new JLabel("          Current Turn");
		lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl.setHorizontalTextPosition(SwingConstants.RIGHT);
		v.add(lbl);
		JPanel panel=new JPanel();
		panel.add(currentTurnPanel);
		v.add(panel);
		lblTimer=new JLabel("Time: 0");
		v.add(lblTimer);
		tipPanel=new JPanel(new BorderLayout());
		tipPanel.add(v,BorderLayout.CENTER);
		//this.getContentPane().add(v,BorderLayout.EAST);
		
	}
	
	
	private void setMenus()
	{
		//menu file
		JMenu  mnuFile = new JMenu("File");
		mnuFile.setMnemonic('F');
		
		mnuSave = new JMenuItem("Save game");
		mnuSave.setMnemonic('S');
		mnuSave.setAccelerator(KeyStroke.getKeyStroke
				(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		mnuSave.addActionListener(new MnuSaveGameL());
		
		mnuLoad = new JMenuItem("Load game");
		mnuLoad.setMnemonic('L');
		mnuLoad.setAccelerator(KeyStroke.getKeyStroke
				(KeyEvent.VK_L,InputEvent.CTRL_MASK));
		mnuLoad.addActionListener(new MnuLoadGameL());
		
		JMenuItem mnuExit=new JMenuItem("Exit");
		mnuExit.setMnemonic('X');
		mnuExit.setAccelerator(KeyStroke.getKeyStroke
				(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		mnuExit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				quitProgram();
			}
		});
		//menu game
		JMenu mnuGame = new JMenu("Game");
		mnuGame.setMnemonic('G');
		mnuPracticeGame = new JMenuItem("Practice game");
		mnuPracticeGame.setMnemonic('P');
		mnuPracticeGame.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				chessBoard=PracticeChessboard;
				chessBoard.reset();
				chessBoard.startGame();
				Othello.this.updateChequers();
				Othello.this.updateMenusAndButtons();
				repaint();
				resetGameState();
			}
			
		});
		mnuTakeBack = new JMenuItem("Take back");
		mnuTakeBack.setMnemonic('T');
		mnuTakeBack.setEnabled(false);
		mnuTakeBack.addActionListener(new BtnUndoL());
		mnuStepForward = new JMenuItem("Step forward");
		mnuStepForward.setMnemonic('S');
		mnuStepForward.setEnabled(false);
		mnuStepForward.addActionListener(new BtnRedoL());
		
		JMenu mnuSetting=new JMenu("Setting");
		mnuSetting.setMnemonic('S');
		JMenu mnuSearch=new JMenu("Search Depth");
		ButtonGroup btnGroupSearch=new ButtonGroup();
		ActionListener searchListener=new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				depth=Integer.parseInt(
						((JRadioButtonMenuItem)
								e.getSource()).getActionCommand());
			}
			
		};
		for(int i=1;i<12;i++)
		{
			JRadioButtonMenuItem radioMenu;
			if(i==depth)
			 radioMenu=new JRadioButtonMenuItem(i+" moves",true);
			else
				radioMenu=new JRadioButtonMenuItem(i+" moves",false);
			radioMenu.setActionCommand(i+"");
			radioMenu.addActionListener(searchListener);
			btnGroupSearch.add(radioMenu);
			mnuSearch.add(radioMenu);
		}
		
		JMenu mnuTimer=new JMenu("Timer");
		ButtonGroup btnGroupTimer=new ButtonGroup();
		ActionListener timerListener=new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				
				deadSeconds=Integer.parseInt(((JRadioButtonMenuItem)
						e.getSource()).getActionCommand());
			}
			
		};
		for(int i=5;i<=30;i+=5)
		{
			JRadioButtonMenuItem radioMenu=null;
			if(i==deadTimes)
				radioMenu=new JRadioButtonMenuItem(i+" seconds",true);
			else
				radioMenu=new JRadioButtonMenuItem(i+" seconds",false);
			radioMenu.setActionCommand(i+"");
			radioMenu.addActionListener(timerListener);
			btnGroupTimer.add(radioMenu);
			mnuTimer.add(radioMenu);
		}
		JRadioButtonMenuItem radioMenu=new JRadioButtonMenuItem("No timer",false);
		radioMenu.setActionCommand("-1");
		radioMenu.addActionListener(timerListener);
		btnGroupTimer.add(radioMenu);
		mnuTimer.add(radioMenu);
		
		//menu options
		JMenu mnuOptions=new JMenu("Options");
		mnuOptions.setMnemonic('O');
		JMenu mnuColor=new JMenu("Color");
		mnuColor.setMnemonic('C');
		JMenuItem mnuChangeChessboardbackground=new JMenuItem("Change chessboard background");
		mnuChangeChessboardbackground.setMnemonic('B');
		mnuChangeChessboardbackground.setToolTipText("Set the chessboard background");
		mnuChangeChessboardbackground.addActionListener(new MnuChangeChessBoardBackgroundL());
		JMenuItem mnuUseDefaultChessboardBackground=new JMenuItem("Use default chessboard background");
		mnuUseDefaultChessboardBackground.setMnemonic('U');
		mnuUseDefaultChessboardBackground.setToolTipText("Set the chessboard background to default");
		mnuUseDefaultChessboardBackground.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				chessBoardColor=DEFAULT_CHESSBOARD_BACKGROUND;
				repaint();
			}
			
		});
		mnuColor.add(mnuChangeChessboardbackground);
		mnuColor.add(mnuUseDefaultChessboardBackground);
		JCheckBoxMenuItem mnuAntialiasing = new JCheckBoxMenuItem("Antialiasing (but may become slow)",false);
		mnuAntialiasing.setMnemonic('A');
		mnuAntialiasing.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				JCheckBoxMenuItem mnu=(JCheckBoxMenuItem)e.getSource();
				if(mnu.isSelected())
					isAntialiasing=true;
				else
					isAntialiasing=false;
				repaint();
			}
			
		});
		JMenuItem mnuNetworkSetting=new JMenuItem("Network Setting");
		mnuNetworkSetting.setMnemonic('N');
		mnuNetworkSetting.addActionListener(new MnuNetworkSettingL());
		
		
		//menu window
		JMenu mnuWindow=new JMenu("Window");
		mnuWindow.setMnemonic('W');
		JMenuItem mnuFullScreen=new JMenuItem("Full Screen");
		mnuFullScreen.setMnemonic('F');
		mnuFullScreen.addActionListener(new MnuFullScreenL());
		
		//add menu items to menu
		mnuFile.add(mnuSave);
		mnuFile.add(mnuLoad);
		mnuFile.add(mnuExit);
		
		mnuGame.add(mnuPracticeGame);
		mnuGame.add(mnuTakeBack);
		mnuGame.add(mnuStepForward);
		//mnuGame.add(mnuConnectServer);
		
		mnuSetting.add(mnuSearch);
		mnuSetting.add(mnuTimer);
		
		mnuOptions.add(mnuColor);
		mnuOptions.add(mnuAntialiasing);
		mnuOptions.add(mnuNetworkSetting);
		
		mnuWindow.add(mnuFullScreen);
		//add menu to toolbar
		mnuBar=new JMenuBar();
		mnuBar.add(mnuFile);
		mnuBar.add(mnuGame);
		mnuBar.add(mnuSetting);
		mnuBar.add(mnuOptions);
		mnuBar.add(mnuWindow);
		this.setJMenuBar(mnuBar);
	}
	
	public boolean isAntialiasing()
	{
		return this.isAntialiasing;
	}
	
	private void quitProgram()
	{
		this.setVisible(false);
		dispose();
		new CloseProgramThread().start();
	}
	class CloseProgramThread extends Thread
	{
		public CloseProgramThread()
		{
			if(selfUserInfo!=null)
			{
				sendLogoutMsg();
			}
		}
		public void run()
		{
			threadPool.shutdown();
			System.exit(0);
		}
	}
	
	
	private void setToolbar()
	{
		toolBar=new JToolBar();
		btnNew=new JButton("New");
		btnNew.setMnemonic('N');
		btnNew.addActionListener(new BtnNewL());
		btnUndo=new JButton("Undo");
		btnUndo.setMnemonic('U');
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new BtnUndoL());
		btnRedo=new JButton("Redo");
		btnRedo.setEnabled(false);
		btnRedo.setMnemonic('R');
		btnRedo.addActionListener(new BtnRedoL());
		
		
		
		/*JButton btnDebug=new JButton("Debug");
		btnDebug.setMnemonic('D');
		btnDebug.addActionListener(new BtnDebugL());
		*/
		toolBar.add(btnNew);
		toolBar.add(btnUndo);
		toolBar.add(btnRedo);
		//toolBar.add(btnDebug);
		//this.getContentPane().add(toolBar,BorderLayout.NORTH);
	}
	
	protected void updateGameStateDisplay()
	{
		lblBlack.setText("Black: "
				+this.toTwoDigitsString(chessBoard.sumBlack())+" discs "
				+this.toTwoDigitsString(chessBoard.getBlackMoves())+" moves");
		
		lblWhite.setText("White: "
				+this.toTwoDigitsString(chessBoard.sumWhite())+" discs "
				+this.toTwoDigitsString(chessBoard.getWhiteMoves())+" moves");
	}
	
	
	protected void updateDisplay(int i,int j,Chessboard.ChequerState turn)
	{
		repaint();
		updateGameStateDisplay();
		txtArea.append(turn+" : "+Chessboard.HEADER[j]+(i+1)+"\n");
		updateMenusAndButtons();
		if(chessBoard.gameOver())
		{
			txtArea.append("Black vs White : "
					+chessBoard.sumBlack()+"-"+chessBoard.sumWhite()+"\n");
			
			info("Black vs White:"
					+chessBoard.sumBlack()+"-"+chessBoard.sumWhite()
					,"Game Over");
			if(timer!=null && timer.isRunning())
				timer.stop();
		}
		else if( this.chessBoard==PracticeChessboard && ! chessBoard.isCurrentTurnHasValidMove() )
		{
			 info(chessBoard.getCurrentTurn()
			    		+" have to pass","Pass");
				txtArea.append(chessBoard.getCurrentTurn()
						+" passes\n");
			this.placeChequer(-1);
		}
	}
	
	protected void updatePartnerUserInfoMsg(UpdatePartnerUserInfoMsg msg)
	{
		this.selfUserInfo=msg.getUserInfo();
		this.opponentUserInfo=msg.getOpponentUserInfo();
		this.updateGameInfoPanel(this.selfUserInfo
				,this.opponentUserInfo);
	}
	
	protected void updateUserInfoMsg(UpdateUserInfoMsg msg)
	{
		this.selfUserInfo=msg.getUserInfo();
		this.updateGameInfoPanel(this.selfUserInfo
				,this.opponentUserInfo);
	}
	
	private void sendGameOverMsg(GameOverMsg.GameResult result)
	{
		Msg msg=null;
		if(result==GameOverMsg.GameResult.LOSE)
			msg=new GameOverMsg(this.opponentUserInfo.getNickname(),
					this.selfUserInfo.getNickname()
				,roomnameJoined,GameOverMsg.GameResult.WIN);
		else
			msg=new GameOverMsg(this.selfUserInfo.getNickname(),
					this.opponentUserInfo.getNickname()
				,roomnameJoined,result);
		threadPool.start
		(new Communication(msg,serverIp,serverPort,this));
		this.btnNewGame.setEnabled(true);
		this.btnStartGame.setEnabled(false);
	}
	protected void updateMenusAndButtons()
	{
		if(this.opponentUserInfo==null)
		{
			btnUndo.setEnabled(chessBoard.canUndo());
			btnRedo.setEnabled(chessBoard.canRedo());
			mnuTakeBack.setEnabled(chessBoard.canUndo());
			mnuStepForward.setEnabled(chessBoard.canRedo());
		}
	}
	
	public Color getChessBoardBackground()
	{
		return this.chessBoardColor;
	}
	
	private void readGameFromFile(File f)
	{
		try
		{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
			chessBoard=(Chessboard)in.readObject();
			in.close();
			for(int i=0;i<64;i++)
				chequers[i].updateChessBoard(chessBoard);
			updateMenusAndButtons();
			txtArea.setText("");
			lblBlack.setText("Black: "+this.toTwoDigitsString(chessBoard.sumBlack())
					+" discs "+this.toTwoDigitsString
					(chessBoard.getBlackMoves())+" moves");
			
			lblWhite.setText("White: "+this.toTwoDigitsString(chessBoard.sumWhite())
					+" discs "+this.toTwoDigitsString
					(chessBoard.getWhiteMoves())+" moves");
			repaint();
		}
		catch(Exception e)
		{
			error(e.toString());
			e.printStackTrace();
		}
	}
	
	private void saveGameToFile(File f)
	{
		try
		{
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(chessBoard);
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			error(e.toString());
			e.printStackTrace();
		}
	}
	
	private void updateChequers()
	{
		for(int i=0;i<64;i++)
			chequers[i].updateChessBoard(chessBoard);
	}
	
	class BtnNewL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			chessBoard=GamblingChessboard;
			chessBoard.reset();
			chessBoard.startGame();
			updateChequers();
			updateMenusAndButtons();
			repaint();
			resetGameState();
			stopTimer();
			startTimer();
		    //btnStartGame.setEnabled(true);
		}
		
	}
	 
	protected void stopTimer()
	{
		if(timer!=null)
			timer.stop();
		lblTimer.setText("Timer: 0");
	}
	
	protected void startTimer()
	{
		if(chessBoard.gameOver() || programStatus==ProgramStatus.Network)
			return;
		deadTimes=deadSeconds;
		if(deadSeconds==-1)
			return;
		if(timer==null)
		{
			timerAction=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					deadTimes--;
					lblTimer.setText("Timer: "+deadTimes);
					if(deadTimes==0)
					{
						forceMove();
						deadTimes=deadSeconds;
					}
				}
			};
			timer=new Timer(ONE_SECOND,timerAction);
		    timer.start();
		}
		else
		{
			if(timer.isRunning())
			{
				timer.stop();
				deadTimes=deadSeconds;
			}
			timer=new Timer(ONE_SECOND,timerAction);
			timer.start();
		}
	}
	
	private void resetGameState()
	{
		txtArea.setText("");
		lblBlack.setText("Black: "+toTwoDigitsString(chessBoard.sumBlack())
				+" discs "+toTwoDigitsString(0)+" moves");
		lblWhite.setText("White: "+toTwoDigitsString(chessBoard.sumWhite())
				+" discs "+toTwoDigitsString(0)+" moves");
	}
	
	class BtnUndoL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			chessBoard.undo();
			repaint();
			updateMenusAndButtons();
		}
		
	}
	
	class BtnRedoL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			chessBoard.redo();			
			repaint();
			updateMenusAndButtons();
		}
		
	}
	
	class MnuLoadGameL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
			int opt=fileChooser.showSaveDialog(Othello.this);
			if(opt==JFileChooser.APPROVE_OPTION)
			{
				File f=fileChooser.getSelectedFile();
				readGameFromFile(f);
			}
		}
		
	}
	class MnuSaveGameL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			int opt=fileChooser.showSaveDialog(Othello.this);
			if(opt==JFileChooser.APPROVE_OPTION)
			{
				File f=fileChooser.getSelectedFile();
				if(!f.getName().endsWith(".og"))
				{
					f=new File(f.getAbsolutePath()+".og");
				}
				saveGameToFile(f);
			}
		}
		
	}
	
	class MnuNetworkSettingL implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			new SettingDialog(Othello.this
					,serverIp,serverPort).setVisible(true);
		}
		
	}
	
	class DocL implements DocumentListener
	{

		public void insertUpdate(DocumentEvent arg0) {
			txtArea.setCaretPosition(txtArea.getDocument().getLength());
		}

		public void removeUpdate(DocumentEvent arg0) 
		{
			txtArea.setCaretPosition(txtArea.getDocument().getLength());
		}

		public void changedUpdate(DocumentEvent arg0) {
			
		}
		
	}
	
	class MnuChangeChessBoardBackgroundL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			Color c=JColorChooser.showDialog(Othello.this,"Change the chessboard background",chessBoardColor);
			if(c!=null)
			{
				chessBoardColor=c;
				repaint();
			}
		}
		
	}
	
	class KeyL extends KeyAdapter
	{

		public void keyPressed(KeyEvent e) 
		{
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE && isFullScreen)
			{
	     		GraphicsEnvironment ge = 
	     		GraphicsEnvironment.getLocalGraphicsEnvironment();
	     		
				GraphicsDevice gd=ge.getDefaultScreenDevice();
				if(gd.isFullScreenSupported())
				{
					try 
					{
						dispose();
						setUndecorated(false);
						setResizable(true);
						gd.setFullScreenWindow(null); // comment this line if you want only undecorated frame
						getContentPane().add(toolBar,BorderLayout.NORTH);
						setJMenuBar(mnuBar);
						setVisible(true);
					}
					catch(Exception ex)
					{
					    ex.printStackTrace();
					}
				}
			}
		}
	}
	
	class MnuFullScreenL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			GraphicsEnvironment ge = GraphicsEnvironment.
			   getLocalGraphicsEnvironment();
			GraphicsDevice gd=ge.getDefaultScreenDevice();
			if(gd.isFullScreenSupported())
			{
				try 
				{
					getContentPane().remove(toolBar);
					setJMenuBar(null);
					dispose();
					setUndecorated(true);
					setResizable(false);
					gd.setFullScreenWindow(Othello.this);
					setVisible(true);
					isFullScreen=true;
					splitPane.requestFocusInWindow();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				warning("Full screen unsupported!");
			}
		}
	}
	
	
	protected void setSuccessfullLoginInfo(OnlineUserInfo userInfo,String psw)
	{
		this.loginPsw=psw;
		this.selfUserInfo=userInfo;
		if(selfUserInfo!=null)
			this.setTitle("Othello@"
					+messageListener.getHostIP()+":"
					+messageListener.getOpeningPort()
					+"("+this.selfUserInfo.getNickname()+")");
		else
			this.setTitle("Othello@"
					+messageListener.getHostIP()+":"
					+messageListener.getOpeningPort());
		this.btnQuitGame.setEnabled(false);
		if(userInfo!=null)
		{
			lblUserInfo.setIcon(GameRoomPanel.getPlayerIcon());
			lblUserInfo.setText(userInfo.getNickname());
		}
	}
	
	protected void placeChequer(int pos)
	{
		//pos==-1 means pass here
		if(pos!=-1)
		{
			Chessboard.ChequerState turn=chessBoard.getCurrentTurn();
			chessBoard.placeChequer(pos);
             this.chequers[pos].immediatePaint();
			updateDisplay(pos>>>3,pos%8,turn);
		}
		else
			chessBoard.inverseTurn();
		if(this.chessBoard!=PracticeChessboard)
		{
			
			if(chessBoard.gameOver() && this.opponentUserInfo!=null)
			{
				if(chessBoard.isWinner(GamblingChessboard.getPersonTurn()))
					this.sendGameOverMsg(GameOverMsg.GameResult.WIN);
				else if(chessBoard.isDraw())
					this.sendGameOverMsg(GameOverMsg.GameResult.DRAW);
				else
					this.sendGameOverMsg(GameOverMsg.GameResult.LOSE);
				btnNewGame.setEnabled(true);
			}
			sendMoveMsg(pos);
		}
	}
	
	private void sendMsgToWatchers(Msg msg)
	{
		for(OnlineUserInfo ele:this.watchers)
		{
			threadPool.start(new Communication(msg,ele.getIP()
					,ele.getPort(),this));
		}
	}
	
	
	protected void changeToWatcher(Partner p)
	{
		OnlineUserInfo first=p.getFirstPlayer();
		OnlineUserInfo second=p.getSecondPlayer();
		observedPartner=new Partner(first,second);
		JoinWatcherMsg msg=new JoinWatcherMsg(this.selfUserInfo);
		threadPool.start(new Communication(msg,first.getIP(),first.getPort(),this));
		threadPool.start(new Communication(msg,second.getIP(),second.getPort(),this));
		this.layoutGamblingChessBoard(null,roomnameJoined);
		this.updateGameInfoPanel(first,second);
		this.btnStartGame.setEnabled(false);
		this.btnQuitGame.setEnabled(true);
		this.chessBoard=ObserverModeChessboard;
		this.chessBoard.reset();
	}
	public void updateChessboard(JoinWatcherAnsMsg msg)
	{
		programStatus=ProgramStatus.Network;
		this.chessBoard.restore(msg.getChessboardState());
		this.updateChequers();
		this.updateGameStateDisplay();
		repaint();
	}
	private void sendMoveMsg(int pos)
	{
		if(this.opponentUserInfo!=null)
		{
			MoveMsg msg=new MoveMsg(pos);
			threadPool.start(new Communication(msg,
					this.opponentUserInfo.getIP()
					,this.opponentUserInfo.getPort(),this));
			sendMsgToWatchers(msg);
		}
		else
		{
			Algorithms.alphaBeta(depth,chessBoard,Integer.MIN_VALUE,
					Integer.MAX_VALUE,chessBoard.getCurrentTurn()
					,depth,result);
			if(result.getPos()!=-1)
			{
				Chessboard.ChequerState turn=chessBoard.getCurrentTurn();
				this.chequers[result.getPos()].placeChequer();
				this.updateDisplay(result.getPos()>>>3,result.getPos()%8,turn);
			}
			if(!chessBoard.gameOver())
			{
				while(!chessBoard.isCurrentTurnHasValidMove() 
					&& GamblingChessboard.getPersonTurn()
					==chessBoard.getCurrentTurn())
				{
					info("You have to pass","Pass");
					chessBoard.inverseTurn();
				}
				if(result.getPos()==-1)
				{
					info("Computer have to pass","Pass");
					chessBoard.inverseTurn();
				}
				repaint();
				if(chessBoard.getCurrentTurn()
						!=GamblingChessboard.getPersonTurn())
				{
					sendMoveMsg(-1);
				}
			}
			result.setPos(-1);
		}
	}
	
	private void sendLoginMsg()
	{
		String nickname=txtLoginID.getText().trim();
		String psw=String.valueOf(txtLoginPassword.getPassword());
		if(nickname.length()==0||psw.length()==0)
		{
			warning("Illegal ID or password!");
			return;
		}
		else
		{
			isLoginFail=true;
			String ip=messageListener.getHostIP();
			int port=messageListener.getOpeningPort();
			LoginMsg msg=new LoginMsg(nickname,psw,ip,port);
			if(waitingScreen==null)
				waitingScreen=new WaitingScreen
				("Login to game server....",this);
			else
				waitingScreen.setMessage("Login to game server......");
			
			waitingScreen.setTarget
			(new Communication(msg,serverIp,serverPort,Othello.this));
			splitPane.setRightComponent(waitingScreen);
			validate();
			waitingScreen.start();
			isCurrentWaitingFroRegister=false;
			//this.btnRefresh.setEnabled(true);
		}
	}
	
	private void sendLeaveWatcherMsg(Partner p)
	{
		if(p==null)
			return;
		OnlineUserInfo first=p.getFirstPlayer();
		OnlineUserInfo second=p.getSecondPlayer();
		Msg msg=new LeaveWatcherMsg(this.selfUserInfo);
		threadPool.start(new Communication(msg,first.getIP(),first.getPort(),this));
		threadPool.start(new Communication(msg,second.getIP(),second.getPort(),this));
	}
	
	private void sendQuitGameMsg()
	{
		if(this.roomnameJoined==null)    //should never be run
		{
			this.restoreGameRoomPanel();
			return;
		}
		if(this.observedPartner!=null) //current is in watcher mode,so leave watcher mode
		{
			this.sendLeaveWatcherMsg(this.observedPartner);
			quitObserverMode(null);
			return;
		}
		this.gameRoomPanel.quitRoom();
		String opponent=
			this.opponentUserInfo != null ? this.opponentUserInfo.getNickname():null;
		Msg msg=new QuitGameMsg(this.selfUserInfo.getNickname()
				,this.roomnameJoined
				,opponent);
		if(queryDialog==null)
		{
			queryDialog=new QueryRoomMembersDialog(
				new Communication(msg,serverIp,serverPort,this),
				"Quiting this game room .....");
		}
		else
		{
			queryDialog.setTarget(new Communication(msg,serverIp,serverPort,this));
			queryDialog.setMsg("Quiting this game room.....");
		}
		queryDialog.start();
		this.btnQuitGame.setEnabled(false);
		this.btnStartGame.setEnabled(false);
		this.roomsList.setEnabled(true);
		this.btnRefresh.setEnabled(true);
		this.btnQuitRoom.setEnabled(true);
		//this.btnNew.setEnabled(true);
		sendQuitGameMsgToObservers();
		//this.roomnameJoined=null;
	}
	
	
	class BtnStartGameL implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			sendHandUpMsg();
			btnStartGame.setEnabled(false);
			mnuPracticeGame.setEnabled(false);
			btnNew.setEnabled(false);
			mnuSave.setEnabled(true);
			mnuLoad.setEnabled(true);
			lblSelf.setText(selfUserInfo.getNickname()+": Hand UP");
		}
	}
	
	class BtnQuitGameL implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			sendQuitGameMsg();
		}	
	}
	
	class BtnLoginL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			sendLoginMsg();
		}
	}
	
	private void sendLogoutMsg()
	{
		isLoginFail=true;
		isCurrentWaitingFroRegister=false;
		
		LogoutMsg msg=new LogoutMsg
		(this.selfUserInfo.getNickname(),this.loginPsw
				,roomnameJoined,
		this.opponentUserInfo==null ? null:
			this.opponentUserInfo.getNickname());
		
		if(waitingScreen==null)
			waitingScreen=new WaitingScreen("Logout from game server....",this);
		else 
			waitingScreen.setMessage("Logout from game server.....");
		waitingScreen.setTarget
		(new Communication(msg,serverIp,serverPort,Othello.this));
		splitPane.setRightComponent(waitingScreen);
		waitingScreen.start();
		if(splitPane.getLeftComponent()==this.gameRoomPanel)
		{
			splitPane.setLeftComponent(this.chessBoardPanel);
		}
		validate();
		this.setSuccessfullLoginInfo(null,null);
		this.roomnameJoined=null;
		this.roomsList.setEnabled(true);
		this.btnRefresh.setEnabled(false);
		this.btnQuitRoom.setEnabled(false);
		sendQuitGameMsgToObservers();
		this.sendLeaveWatcherMsg(this.observedPartner);
	}
	
	protected void quitObserverMode(ObservedPlayerQuitMsg msg)
	{
		if(msg!=null && this.observedPartner!=null)
			info("Player quit the game,so you are forced to " +
				"quit the observer mode"
				,"Information");
		else
		{
			this.sendLeaveWatcherMsg(this.observedPartner);
		}
		restoreGameRoomPanel();
		this.btnQuitGame.setEnabled(false);
		this.btnStartGame.setEnabled(false);
		this.roomsList.setEnabled(true);
		this.btnRefresh.setEnabled(true);
		this.btnQuitRoom.setEnabled(true);
		observedPartner=null;
		Msg queryMsg = new QueryRoomMembersMsg(roomnameJoined);
		threadPool.start(new Communication(queryMsg,serverIp,serverPort,this));
	}
	
	private void sendQuitGameMsgToObservers()
	{
		for(OnlineUserInfo ele:this.watchers)
		{
			Msg msg=new ObservedPlayerQuitMsg();
			threadPool.start(new Communication(msg,ele.getIP(),ele.getPort(),this));
		}
		this.watchers.clear();
	}
	
	class BtnLogoutL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			sendLogoutMsg();
		}
		
	}
	
	class BtnRegisterL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			splitPane.setRightComponent(registerPanel);
			validate();
		}
	}
	
	class BtnRegCancelL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
			splitPane.setRightComponent(loginPanel);
			validate();
		}
		
	}
	
	private void warning(String msg)
	{
		JOptionPane.showMessageDialog(this,msg
				,"Warning",JOptionPane.WARNING_MESSAGE);
	}
	
	private void error(String msg)
	{
		JOptionPane.showMessageDialog(this,msg
				,"Error",JOptionPane.ERROR_MESSAGE);
	}
	
	protected void startGame(String turn)
	{
		Chessboard.ChequerState t;
		if(turn.equalsIgnoreCase("black"))
			t=Chessboard.ChequerState.Black;
		else
			t=Chessboard.ChequerState.White;
		((GamblingChessboard)chessBoard).setPersonTurn(t);
		this.chessBoard.startGame();
		this.lblSelf.setText(this.selfUserInfo.getNickname()
				+":"+t);
		this.lblOpponent.setText(
				this.opponentUserInfo.getNickname()
				+": "+Chessboard.ChequerState.inverseState(t));
		repaint();
		Msg ansMsg=new JoinWatcherAnsMsg(chessBoard.getChessboardState());
		this.sendMsgToWatchers(ansMsg);
	}
	
	private String toTwoDigitsString(int i)
	{
		if(i<0||i>100)
			return null;
		String ret=String.valueOf(i);
		if(ret.length()==1)
			return "0"+ret;
		else
			return ret;
	}
	
	private void sendRegisterMsg()
	{
		String id=txtRegID.getText().trim();
		String psw=String.valueOf(txtRegPassword.getPassword());
		String confirmPsw=String.valueOf
		(txtConfirmRegPassword.getPassword());
		String sex=comboSex.getSelectedItem().toString();
		String email=txtRegEmail.getText();
		if(id.length()==0)
		{
			warning("Illegal ID!");
			return;
		}
		if(!psw.equals(confirmPsw))
		{
			warning("The password doesn't match!");
			return ;
		}
		if(psw.length()==0)
		{
			warning("Empty password is not permitted!");
			return;
		}
		UserInfo userInfo=new UserInfo(id,sex,email);
		RegMsg msg=new RegMsg(userInfo,psw);
		if(waitingScreen==null)
		{
			waitingScreen=new WaitingScreen("Registering ......",this);
		}
		else
			waitingScreen.setMessage("Registering......");
		isCurrentWaitingFroRegister=true;
		waitingScreen.setTarget
		(new Communication(msg,serverIp,serverPort,this));
		
		splitPane.setRightComponent(waitingScreen);
		validate();
		waitingScreen.start();
	}
	
	
	protected void restoreRightPanel()
	{
		if(isCurrentWaitingFroRegister==true)
		{
			splitPane.setRightComponent(loginPanel);
			isCurrentWaitingFroRegister=false;
		}
		else if(isLoginFail)
		{
			splitPane.setRightComponent(loginPanel);
		}
		else
		{
			roomsPanel.remove(this.gameInfoPanel);
			roomsPanel.add(this.roomsListUpperPanel,BorderLayout.CENTER);
			splitPane.setRightComponent(this.roomsAndTopUsersPane);
		}
		validate();
	}
	
	class BtnRegSubmitL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			sendRegisterMsg();
		}
		
	}
	
	class BtnQuitRoomL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			programStatus=ProgramStatus.Stand_Alone;
			roomnameJoined=null;
			splitPane.setLeftComponent(chessBoardPanel);
			btnQuitRoom.setEnabled(false);
			btnRefresh.setEnabled(false);
			Othello.this.mnuPracticeGame.setEnabled(true);
			Othello.this.mnuSave.setEnabled(true);
			Othello.this.mnuLoad.setEnabled(true);
			Othello.this.btnNew.setEnabled(true);
		}
		
	}
	
	/*class BtnDebugL implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0) 
		{
			new DebugFrame(chessBoard).setVisible(true);
		}
		
	}*/
	
	protected void layoutGamblingChessBoard(OnlineUserInfo opponent,String roomname)
	{
		programStatus=ProgramStatus.Network;
		this.stopTimer();
		this.opponentUserInfo= opponent;
		this.roomnameJoined=roomname;
		if(this.opponentUserInfo!=null)
		{
			this.chessBoard= GamblingChessboard;
			GamblingChessboard.setPersonTurn(Chessboard.ChequerState.White);
			this.chessBoard.reset();
		}
		else
		{
			this.chessBoard= GamblingChessboard;
			GamblingChessboard.setPersonTurn(Chessboard.ChequerState.Black);
			this.chessBoard.reset();
		}
		this.updateChequers();
		this.updateGameInfoPanel(this.selfUserInfo,this.opponentUserInfo);
		splitPane.setLeftComponent(this.chessBoardPanel);
		setRightGameInfoPanel();
		if(this.opponentUserInfo!=null)
		{
			sendJoinGameMsg();
		}
	    this.btnQuitRoom.setEnabled(false);
	    disableButtonsWhenEnteringNetworkMode();
	}
	public void disableButtonsWhenEnteringNetworkMode()
	{
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);
		this.mnuTakeBack.setEnabled(false);
		this.mnuStepForward.setEnabled(false);
		this.btnNew.setEnabled(false);
	}
	/**
	 * 
	 *called when the local user quit game 
	 *or the remote opponent quit game
	*/
	protected void quitGameMsg(OpponentQuitGameMsg msg)
	{
		programStatus=ProgramStatus.Network;
		this.opponentUserInfo=null;
		this.selfUserInfo=msg.getUserInfo();
		if(this.chessBoard.isGameStart())
		{
			this.chessBoard.reset();
			this.btnStartGame.setEnabled(true);
			this.btnNewGame.setEnabled(false);
			repaint();
		}
		this.updateGameInfoPanel(this.selfUserInfo,this.opponentUserInfo);
	}
	
	protected void removeWatcher(LeaveWatcherMsg msg)
	{
		this.watchers.remove(msg.getUserInfo());
	}
	
	protected Chessboard.ChessboardState addWatcher(JoinWatcherMsg msg)
	{
		this.watchers.add(msg.getUserInfo());
		return this.chessBoard.getChessboardState();
	}
	
	/**
	 * called when a remote user want to join the game
	 * @param userInfo the remote opponent's information
	 */
	protected void joinGame(OnlineUserInfo userInfo)
	{
		this.opponentUserInfo=userInfo;
		this.chessBoard.reset();
		this.updateGameInfoPanel(this.selfUserInfo,this.opponentUserInfo);
		this.btnNewGame.setEnabled(false);
		this.btnStartGame.setEnabled(true);
		repaint();
	}
	/**
	 * called when local user want to join a game
	 *
	 */
	private void sendJoinGameMsg()
	{
		JoinGameMsg msg=new JoinGameMsg(this.selfUserInfo);
		threadPool.start(new Communication
		(msg,this.opponentUserInfo.getIP()
				,this.opponentUserInfo.getPort(),this));
	}
	
	private void setRightGameInfoPanel()
	{
		this.roomsPanel.remove(this.roomsListUpperPanel);
		this.roomsPanel.add(this.gameInfoPanel,BorderLayout.CENTER);
		validate();
	}
	
	protected void restoreGameRoomPanel()
	{
		splitPane.setLeftComponent(gameRoomPanel);
		this.roomsPanel.remove(gameInfoPanel);
		this.roomsPanel.add(this.roomsListUpperPanel,BorderLayout.CENTER);
		this.roomsList.setEnabled(false);
		validate();
	}
	
	private void updateGameInfoPanel(OnlineUserInfo first,OnlineUserInfo second)
	{
		if(first!=null)
		{
			lblSelf.setText(first.getNickname());
			lblSelf.setIcon(GameRoomPanel.getPlayerIcon());
		}
		else
		{
			lblSelf.setText("");
		}
		if(second!=null)
		{
			lblOpponent.setText(second.getNickname());
			lblOpponent.setIcon(GameRoomPanel.getPlayerIcon());
		}
		else
			lblOpponent.setText("");
	}
	
	protected void updateGameRoom(ArrayList<Partner>members)
	{
		if(this.gameRoomPanel==null)
		{
			gameRoomPanel=new GameRoomPanel(members,roomnameJoined,this);
		}
		else 
			this.gameRoomPanel.setRoomMembers(members,roomnameJoined);
		if(this.splitPane.getLeftComponent()==this.gameRoomPanel)
			this.splitPane.setLeftComponent(gameRoomPanel);
		this.btnRefresh.setEnabled(true);
		this.btnQuitRoom.setEnabled(true);
		this.mnuPracticeGame.setEnabled(false);
		this.mnuSave.setEnabled(false);
		this.mnuLoad.setEnabled(false);
		this.btnNew.setEnabled(false);
	}
	
	protected void layoutGameRoomPanel(ArrayList<Partner> members,String roomname)
	{
		this.roomnameJoined=roomname;
		if(this.gameRoomPanel==null)
		{
			gameRoomPanel=new GameRoomPanel(members,roomname,this);
		}
		else 
			this.gameRoomPanel.setRoomMembers(members,roomname);
		splitPane.setLeftComponent(gameRoomPanel);
		this.btnRefresh.setEnabled(true);
		this.btnQuitRoom.setEnabled(true);
		this.mnuPracticeGame.setEnabled(false);
		this.mnuSave.setEnabled(false);
		this.mnuLoad.setEnabled(false);
		this.btnNew.setEnabled(false);
		repaint();
	}
	
	
	
	protected String getID()
	{
		if(this.selfUserInfo!=null)
			return this.selfUserInfo.getNickname();
		else
			return "";
	}
	
	protected void sendJoinRoomMsg(String roomname,String opponent)
	{
		Msg msg=new JoinRoomMsg(this.selfUserInfo.getNickname(),roomname,opponent);
		if(queryDialog==null)
			queryDialog=new QueryRoomMembersDialog
		(new Communication(msg,serverIp,serverPort,this)
				,"Querying game information....");
		else
		{
			queryDialog.setTarget
			(new Communication(msg,serverIp,serverPort,this));
			queryDialog.setMsg("Querying game information.....");
		}
		queryDialog.start();
		this.btnQuitGame.setEnabled(true);
		this.btnRefresh.setEnabled(false);
	}
	

	private void sendQueryRoomMembersMsg(String roomName)
	{
		Msg msg = new QueryRoomMembersMsg(roomName);
		if(queryDialog==null)
		{
			queryDialog=new QueryRoomMembersDialog
			(new Communication(msg,serverIp,serverPort,this)
				,"Querying room information....");
		}
		else
		{
			queryDialog.setTarget
			(new Communication(msg,serverIp,serverPort,this));
			queryDialog.setMsg("Querying room information....");
		}
		queryDialog.start();
	}
	
	protected void hideQueryDialog()
	{
		if(this.queryDialog!=null)
		{
			this.queryDialog.setVisible(false);
		}
	}
	
	class QueryRoomMembersDialog extends JDialog implements Runnable
	{
		private static final long serialVersionUID = 1L;
		private Thread thread;
		private JLabel lbl;
		public QueryRoomMembersDialog(Thread thread,String msg)
		{
			super(Othello.this,true);
			this.setUndecorated(true);
			this.setTitle("Query");
			lbl=new JLabel(msg);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setForeground(Color.BLUE);
			lbl.setBackground(Color.YELLOW);
			lbl.setOpaque(true);
			this.getContentPane().add(lbl,BorderLayout.CENTER);
			this.thread=thread;
			this.setSize(200,50);
			Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size=this.getSize();
			this.setLocation((d.width-size.width)/2
					,(d.height-size.height)/2);
		}
		
		public void setTarget(Thread t)
		{
			this.thread=t;
		}
		public void setMsg(String msg)
		{
			this.lbl.setText(msg);
		}
		
		public void start()
		{
			new Thread(this).start();
			this.setVisible(true);
		}
		
		public void run()
		{
			try
			{
				thread.start();
				thread.join();
				this.setVisible(false);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	class RoomListMouseL extends  MouseAdapter
	{

		public void mouseClicked(MouseEvent e)
		{
			if(!roomsList.isEnabled())
				return;
			if(e.getButton()==1 && e.getClickCount()>=2)
			{
				//roomsList.clearSelection();
				int index=roomsList.locationToIndex(e.getPoint());
				if(index!=-1)
				{
					Rectangle rec=roomsList.getCellBounds
					(index>0?(index-1):0,index);
					if(rec.contains(e.getPoint()))
					{
						sendQueryRoomMembersMsg
						(listModel.get(index).toString());
					}
					roomsList.clearSelection();
				}
			}
		}		
	}
	
	class OpponentUserInfoMouseL extends MouseAdapter
	{
		public void mouseEntered(MouseEvent me)
		{
	
			Point p=lblOpponent.getLocationOnScreen();
			Rectangle rect=lblOpponent.getBounds();
			p.setLocation(p.x+rect.getWidth(),p.y+rect.getHeight());
			if(Othello.this.observedPartner==null)
			{
				userInfoDialog.showUserInfo
				(opponentUserInfo,roomnameJoined,p);
			}
			else
			{
				userInfoDialog.showUserInfo
				(observedPartner.getSecondPlayer(),roomnameJoined,p);
			}
		}
		public void mouseExited(MouseEvent me)
		{
			userInfoDialog.setVisible(false);
		}
	}
	
	class LblUserInfoMouseL extends MouseAdapter
	{
		public void mouseEntered(MouseEvent me)
		{
			Point p=lblUserInfo.getLocationOnScreen();
			Rectangle rect=lblUserInfo.getBounds();
			p.setLocation(p.x+rect.getWidth(),p.y+rect.getHeight());
			
				userInfoDialog.showUserInfo(selfUserInfo,roomnameJoined,p);
		}
		public void mouseExited(MouseEvent me)
		{
			userInfoDialog.setVisible(false);
		}
	}
	
	class SelfUserInfoMouseL extends MouseAdapter
	{
		public void mouseEntered(MouseEvent me)
		{
			Point p=lblSelf.getLocationOnScreen();
			Rectangle rect=lblSelf.getBounds();
			p.setLocation(p.x+rect.getWidth(),p.y+rect.getHeight());
			if(Othello.this.observedPartner==null)
			{
				userInfoDialog.showUserInfo(selfUserInfo,roomnameJoined,p);
			}
			else
			{
				userInfoDialog.showUserInfo
				(observedPartner.getFirstPlayer(),roomnameJoined,p);
			}
		}
		public void mouseExited(MouseEvent me)
		{
			userInfoDialog.setVisible(false);
		}
	}
	
	class BtnTrickL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			JButton button=(JButton)e.getSource();
			if(!trickWindow.isVisible())
			{
				Point p=button.getLocationOnScreen();
				Dimension d=trickWindow.getSize();
				trickWindow.setLocation(p.x-d.width,p.y-d.height);
				trickWindow.setVisible(true);
			}
			else
			{
				trickWindow.setVisible(false);
			}
			/*sendPlainMessage(null,button.getIcon());
			try
			{
				Document d=txtPane.getDocument();
				d.insertString(d.getLength(),selfUserInfo.getNickname()+":",null);
				txtPane.setCaretPosition(txtPane.getSelectionStart());
				txtPane.insertIcon(button.getIcon());
				d.insertString(d.getLength(),"\n",null);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}*/
		}		
	}
}