package cn.edu.nju.elegate;

import java.io.Serializable;

/**
 * a class to represent a user
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class UserInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String nickname;
	private String sex;
	private String email;
	
	public UserInfo(String nickname)
	{
		this(nickname,"Male");
	}
	
	public UserInfo(String nickname,String sex)
	{
		this(nickname,sex,"");
	}
	
	public UserInfo(String nickname,String sex,String email) 
	{
		this.nickname=nickname;
		this.sex=sex;
		this.email=email;
	}

	public String getNickname()
	{
		return this.nickname;
	}
	public String getSex()
	{
		return this.sex;
	}
	public void setSex(String sex)
	{
		this.sex=sex;
	}
	public String getEmail()
	{
		return this.email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	
	public String toString()
	{
		return this.getClass()+"[nickname="+this.nickname
		+",sex="+this.sex+",email="+this.email;
	}
	
	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(obj==this)
			return true;
		if(this.getClass()!= obj.getClass())
			return false;
		UserInfo userInfo=(UserInfo)obj;
		return this.nickname.equals(userInfo.nickname);
	}
}


class ExtendedUserInfo extends UserInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int marks;
	private int win;
	private int lose;
	private int draw;
	public ExtendedUserInfo(String nickname,String sex,String email,int win,int lose,int draw,int marks)
	{
		super(nickname,sex,email);
		this.marks=marks;
		this.win=win;
		this.lose=lose;
		this.draw=draw;
	}
	public int getWin()
	{
		return this.win;
	}
	public void setWin(int win)
	{
		this.win=win;
	}
	public int getLose()
	{
		return this.lose;
	}
	public void setLose(int lose)
	{
		this.lose=lose;
	}
	public int getDraw()
	{
		return this.draw;
	}
	public void setDraw(int draw)
	{
		this.draw=draw;
	}
	public int getMarks()
	{
		return this.marks;
	}
	public void setMarks(int marks)
	{
		this.marks=marks;
	}
	public void increaseMarks(int inc)
	{
		this.marks+=inc;
	}
	public void increaseWin()
	{
		this.win++;
	}
	public void increaseLose()
	{
		this.lose++;
	}
	public void increaseDraw()
	{
		this.draw++;
	}
	public String toString()
	{
		return this.getClass()+"[win="+win+",lose="
		+lose+",draw="+draw+",marks="+marks+"]";
	}
}


class OrderedUserInfo extends ExtendedUserInfo
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int rank;
	
	public OrderedUserInfo(String nickname,int win,int lose,int draw,int marks,int rank)
	{
		this(nickname,"Male",win,lose,draw,marks,rank);
	}
	
	public OrderedUserInfo(String nickname,String sex,int win,int lose,int draw,int marks,int rank)
	{
		this(nickname,sex,null,win,lose,draw,marks,rank);
	}
	
	public OrderedUserInfo(String nickname,String sex,String email,int win,int lose,int draw,
			int marks,int rank) 
	{
		super(nickname,sex,email,win,lose,draw,marks);
		this.rank=rank;
	}
	
	
	
	public String toString()
	{
		return super.toString()+"[rank="+rank+"]";
	}
	public Object[] toArray()
	{
		Object[] ret=new Object[8];
		ret[0]=super.getNickname();
		ret[1]=super.getSex();
		ret[2]=super.getEmail();
		ret[3]=this.getWin();
		ret[4]=this.getLose();
		ret[5]=this.getDraw();
		ret[6]=this.getMarks();
		ret[7]=this.rank;
		return ret;
	}
}


class OnlineUserInfo extends ExtendedUserInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;

	public OnlineUserInfo(String nickname,String ip,int port,int marks,int win,int lose,int draw)
	{
		this(nickname,"Male",ip,port,marks,win,lose,draw);
	}
	
	public OnlineUserInfo(String nickname,String sex,String ip,int port,int marks,int win,int lose,int draw)
	{
		this(nickname,sex,null,ip,port,marks,win,lose,draw);
	}
	
	public OnlineUserInfo(String nickname,String sex,String email,
		String ip,int port,int marks,int win,int lose,int draw) 
	{
		super(nickname,sex,email,win,lose,draw,marks);
		this.ip = ip;
		this.port = port;
	}
	
	public  String getIP()
	{
		return this.ip;
	}
	public void setIP(String ip)
	{
		this.ip=ip;
	}
	public int getPort()
	{
		return this.port;
	}
	public void setPort(int port)
	{
		this.port=port;
	}
	public String toString()
	{
		return super.toString()+"[ip="+this.ip+",port="+port+"]";
	}
	public Object[] toArray()
	{
		Object[] ret=new Object[9];
		ret[0]=super.getNickname();
		ret[1]=super.getSex();
		ret[2]=super.getEmail();
		ret[3]=this.getWin();
		ret[4]=this.getLose();
		ret[5]=this.getDraw();
		ret[6]=this.getMarks();
		ret[7]=this.ip;
		ret[8]=this.port;
		return ret;
	}
}
