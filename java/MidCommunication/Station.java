//Station.java

public class Station
{
	final long SIFS = 1000;
	final long DIFS = 2000;
	boolean empty = true;
	int resID = -1;
	String buffer = null;
	int rts = -1;
	int cts = -1;
	int ack = -1;
	
	public static void main(String[] args)
	{
		System.out.println("main fuction...");
		Station st = new Station();
		
		Send s1 = st.new Send("A",1,2);
		Send s2 = st.new Send("C",3,2);
		
		Receive r1 = st.new Receive("B",2);
		
		r1.start();
		s1.start();
		s2.start();
	}
	
	public Station() {}
	
	public void binWait()
	{
		for(int waitCount=0; waitCount<16; waitCount++)
		{
			try
			{
				Thread.sleep((long)Math.pow(2,Math.min(waitCount,9))*DIFS);
				if(empty)
					return;
			}
			catch(InterruptedException ex)
			{
				System.out.println(ex);
			}
		}
		System.out.println("error: wait too many times");
		System.exit(1);

	}
	public void sendRTS(int destID)
	{
		rts = destID;
	}
	
	public void sendCTS(int resID)
	{
		cts = resID;	
	}
	
	public void receiceData(int resID)
	{
		ack = resID;
		System.out.println("receive successful...");		
	}
	
	public void waitCurrent()
	{
		do
		{
			try
			{
				Thread.sleep(DIFS);
			}
			catch(InterruptedException ex)
			{
				System.out.println(ex);
			}
		}while(!empty);
	}
	
	class Send extends Thread
	{
		int stationID;
		int destID;
		
		public Send(String str,int self,int dest)
		{
			stationID = self;
			destID = dest;
			setName(str);
		}
		
		public void run()
		{
			System.out.println("station " + stationID + " begin to send to " + destID);
			if(empty)
			{
				try										//如果空闲，等待DIFS时间
				{
					sleep(DIFS);
				}
				catch(InterruptedException ex)
				{
					System.out.println(ex);
				}
				if(!empty)								//第二次若不空闲，则等待当前传输完毕
				{
					System.out.println("now current is busy, and station " + stationID + " is waiting...");
					Station.this.waitCurrent();
					System.out.println("now current is over...");
					
					System.out.println("now begin to binWait...");
					Station.this.binWait();				//传完之后进行指数退避
					System.out.println("binWait over...");
				}
				//send RTS								//第二次空闲，直接传输
				
				empty = false;
				resID = stationID;
				sendRTS(destID);
				System.out.println("*** station " + stationID + " has sent RTS to station " + destID);
				
				while(cts != stationID)
				{
					try
					{
						sleep(SIFS/2);
					}
					catch(InterruptedException ex)
					{
						System.out.println(ex);
					}	
				}		
				System.out.println("station "+ stationID + " has receive CTS from " + destID);
				cts = -1;
					
				System.out.println("station "+ stationID + " is sending data to " + destID);
				try
				{
					Thread.sleep(5000);
				}
				catch(InterruptedException ex)
				{
					System.out.println(ex);
				}	
				buffer = "has sent...";
				
				while(ack != destID)
				{
					try
					{
						sleep(SIFS/2);
					}
					catch(InterruptedException ex)
					{
						System.out.println(ex);
					}	
				}		
				System.out.println("station "+ stationID + " has send data to " + destID + " successful...");
				ack = -1;
				resID = -1;
				empty = true;	
			}
		}	
	}

	
	class Receive extends Thread
	{
		int stationID;
		
		public Receive(String str,int self)
		{
			stationID = self;
			setName(str);
		}
		
		public void run()
		{
			while(true)
			{
				System.out.println("station " + stationID + " begin to receive...");
				
				//监测是否有站点给本站发送数据，并首先接收到对方的请求发送帧RTS
				while(rts != stationID)
				{
					try
					{
						sleep(SIFS/2);
					}
					catch(InterruptedException ex)
					{
						System.out.println(ex);
					}	
				}		
				System.out.println("station " + stationID + " has receive RTS from " + resID);
				rts = -1;
				
				// 接收RTS，在等待SIFS时间后给对方发送允许发送帧CTS
				try
				{
					sleep(SIFS);
				}
				catch(InterruptedException ex)
				{
					System.out.println(ex);
				}
				
				sendCTS(resID);
				System.out.println("station " + stationID + " has sent CTS to station " + resID);
				
				//然后接收数据
				while(buffer != "has sent...")
				{
					try
					{
						sleep(SIFS/2);
					}
					catch(InterruptedException ex)
					{
						System.out.println(ex);
					}	
				}
				
				ack = stationID;
				buffer = null;
				System.out.println("*** station " + stationID + " has received from " + resID + " successful...");
			}
		}
		
	}
}
