/*
 * �������� 2006-3-8
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import MyOwnObject.testClass;

/**
 * @author mike
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class ServerWithThread {
	ServerSocket serversocket;
	public static final int port=10000;
	public ServerWithThread(){
		
	}
	public void start(){
		try{
			this.serversocket=new ServerSocket(port);
			while(true){
				Socket socket=this.serversocket.accept();
				new serverThread(socket).start();
			}
		}catch(IOException ioe){
				ioe.printStackTrace();
			}
	}
	
	public void process(Socket socket)throws IOException{
		try{
			InputStream is=socket.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			Object obj=ois.readObject();
			String str="";
			if(obj.getClass().getName()=="java.lang.String"){
				str=(String)obj;
			}else if(obj.getClass().getName()=="MyOwnObject.testClass"){
				str=((testClass)obj).str;
			}
			String end=socket.getRemoteSocketAddress().toString();
			System.out.println("host:"+end+"send a message to you:"+str);
			ois.close();
			is.close();
			socket.close();
			try{
				Thread.sleep(5000);//����5����
			}catch(InterruptedException ie){ie.printStackTrace();}
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
	}
	class serverThread extends Thread{
		Socket socket;
		public serverThread(Socket s){
			this.socket=s;
		}
		public void run(){
			try{
				process(this.socket);
			}catch(IOException ioe){ioe.printStackTrace();}
		}
		
	}
	public static void main(String args[]){
		ServerWithThread server=new ServerWithThread();
		server.start();
	}

}