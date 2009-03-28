package cn.edu.nju.elegate;

import java.io.*;
import java.net.*;

/**
 * a thread to send message to client
 * @author Elegate
 *
 */
public class SendMsgThread implements Runnable 
{
	private Msg msg;
	private String ip;
	private int port;
	public SendMsgThread(Msg msg,String ip,int port)
	{
		this.msg=msg;
		this.ip=ip;
		this.port=port;
	}
	public void run()
	{
		try
		{
			Socket skt=new Socket();
			skt.connect(new InetSocketAddress(ip,port),5000);
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
			e.printStackTrace();
		}
	}
}
