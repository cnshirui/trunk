package cn.edu.nju.elegate;

import java.io.Serializable;
import java.util.*;

/**
 * this class represent the game room 
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class RoomInfo
{
	public static final int NUM_OF_PARTNERS=50;
	private String name;
	private ArrayList<Partner> roomMembers;
	
	public RoomInfo(String name) 
	{
		this.name=name;
		roomMembers=new ArrayList<Partner>(NUM_OF_PARTNERS);
		for(int i=0;i<NUM_OF_PARTNERS;i++)
		{
			roomMembers.add(new Partner());
		}
	}
	public RoomInfo(String name,ArrayList<Partner> map)
	{
		this.name=name;
		roomMembers=new ArrayList<Partner>(map);
	}
	
	public String getRoomName()
	{
		return this.name;
	}
	
	public ArrayList<Partner> getRoomMembers()
	{
		return this.roomMembers;
	}
	
	public Partner getEmptyMember(String opponent)
	{
		Partner p=null;
		if(opponent==null)
		{
			for(int i=0;i<this.roomMembers.size();i++)
			{
				p=roomMembers.get(i);
				if(p.getFirstPlayer()==null
						&&p.getSecondPlayer()==null)
					return p;
			}
		}
		else
		{
			for(int i=0;i<this.roomMembers.size();i++)
			{
				p=roomMembers.get(i);
				if(p.contains(opponent) && p.hasSeatLeft())
					return p;
			}
		}
		return null;
	}
	
	public Partner getMember(String nickname)
	{
		for(int i=0;i<this.roomMembers.size();i++)
		{
			Partner p=roomMembers.get(i);
			if(p.contains(nickname))
				return p;
		}
		return null;
	}
	
	public void removePlayer(String nickname)
	{
		for(Partner ele:this.roomMembers)
		{
			if(ele.removePlayer(nickname))
				break;
		}
	}
	
	public Partner removePlayer(UserInfo userInfo)
	{
		for(Partner ele:this.roomMembers)
		{
			if(ele.removePlayer(userInfo))
				return ele;
		}
		return null;
	}
	
	public boolean containsMember(String nickname)
	{
		Partner p=this.getMember(nickname);
		return p!=null;
	}
	
	
	public String toString()
	{
		return name;
	}
}
/**
 * this class represent the occupier of a game table 
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
final class Partner implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OnlineUserInfo firstPlayer;
	private OnlineUserInfo secondPlayer;
	private int handUpCount=0;
	public Partner()
	{
		this.firstPlayer=null;
		this.secondPlayer=null;
	}
	public Partner(OnlineUserInfo first)
	{
		this.firstPlayer=first;
		this.secondPlayer=null;
	}
	
	public Partner(OnlineUserInfo first,OnlineUserInfo second)
	{
		this.firstPlayer=first;
		this.secondPlayer=second;
	}
	
	public synchronized boolean handDown()
	{
		if(this.handUpCount>0)
		{
			this.handUpCount--;
			return true;
		}
		return false;
	}

	public synchronized boolean handUP()
	{
		this.handUpCount++;
		if(this.handUpCount>2)
			return false;
		return true;
	}
	
	public synchronized boolean isAllHandsUp()
	{
		return this.handUpCount>=2;
	}
	
	public synchronized int getHandUpCount()
	{
		return this.handUpCount;
	}
	
	public boolean hasSeatLeft()
	{
		return this.firstPlayer==null || this.secondPlayer==null;
	}
	
	public boolean isEmpty()
	{
		return this.firstPlayer==null && this.secondPlayer==null;
	}
	public OnlineUserInfo getFirstPlayer()
	{
		return this.firstPlayer;
	}
	
	public OnlineUserInfo getOpponent()
	{
		if(this.firstPlayer!=null)
			return this.firstPlayer;
		else if(this.secondPlayer!=null)
			return this.secondPlayer;
		else
			return null;
	}
	
	public void setFirstPlayer(OnlineUserInfo first)
	{
		this.firstPlayer=first;
	}
	public OnlineUserInfo getSecondPlayer()
	{
		return this.secondPlayer;
	}
	public void setSecondPlayer(OnlineUserInfo second)
	{
		this.secondPlayer=second;
	}
	
	public boolean removePlayer(UserInfo userInfo)
	{
		if(userInfo.equals(this.firstPlayer))
		{
			this.firstPlayer=null;
			if(this.handUpCount>0)
				this.handUpCount--;
			return true;
		}
		else if(userInfo.equals(this.secondPlayer))
		{
			this.secondPlayer=null;
			if(this.handUpCount>0)
				this.handUpCount--;
			return true;
		}
		return false;
	}
	
	public boolean removePlayer(String nickname)
	{
		if(this.firstPlayer!=null && 
				this.firstPlayer.getNickname().equalsIgnoreCase(nickname))
		{
			this.firstPlayer=null;
			return true;
		}
		else if(this.secondPlayer!=null && 
				this.secondPlayer.getNickname().equalsIgnoreCase(nickname))
		{
			this.secondPlayer=null;
			return true;
		}
		return false;
	}
	
	public boolean addPlayer(OnlineUserInfo userInfo)
	{
		if(this.firstPlayer==null)
			this.firstPlayer=userInfo;
		else if(this.secondPlayer==null)
			this.secondPlayer=userInfo;
		else
			return false;
		return true;
	}
	
	public boolean contains(OnlineUserInfo userInfo)
	{
		return this.getFirstPlayer().equals(userInfo)
		||this.getSecondPlayer().equals(userInfo); 
	}
	
	public boolean contains(String nickname)
	{
		if(this.firstPlayer!=null && 
				this.firstPlayer.getNickname().equalsIgnoreCase(nickname))
		{
			return true;
		}
		else if(this.secondPlayer!=null && 
				this.secondPlayer.getNickname().equalsIgnoreCase(nickname))
		{
			return true;
		}
		return false;
	}
	
	public boolean addOpponent(OnlineUserInfo userInfo)
	{
		if(this.secondPlayer==null)
			this.secondPlayer=userInfo;
		else
			return false;
		return true;
	}
	public String toString()
	{
		return this.getClass()+"[firstPlayer="+firstPlayer
		+",secondPlayer="+secondPlayer+"]";
	}
}

