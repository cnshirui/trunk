package cn.edu.nju.elegate;

import java.util.*;

/**
 * a class to implement the concept of thread pool
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class ThreadPool 
{
	private static ThreadPool instance=null;
	private ArrayList<PooledThread> pool;
	private boolean isShutdown=false;
	
	private ThreadPool()
	{
		pool=new ArrayList<PooledThread>();
	}
	
	public int getThreadsCount()
	{
		return this.pool.size();
	}
	
	public static ThreadPool instance()
	{
		if(instance==null)
			instance=new ThreadPool();
		return instance;
	}
	
	protected synchronized void repool(PooledThread thread)
	{
		this.pool.add(thread);
		notifyAll();
	}
	
	public synchronized void shutdown()
	{
		for(PooledThread ele:this.pool)
		{
			ele.shutdown();
		}
		this.pool.clear();
		notifyAll();
		isShutdown=true;
		for(PooledThread ele:this.pool)
		{
			try
			{
				ele.join();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public boolean isShutdown()
	{
		return this.isShutdown;
	}
	
	public synchronized void start(Runnable target)
	{
		if(pool.size()>0)
		{
			PooledThread p=pool.remove(pool.size()-1);
			p.setTarget(target);
		}
		else
		{
			PooledThread thread=new PooledThread(target,this);
			thread.start();
		}
	}
}


class PooledThread extends Thread
{
	private boolean isShutdown=false;
	private Runnable target;
	private ThreadPool pool;
	public PooledThread(Runnable target,ThreadPool pool)
	{
		super(target);
		this.target=target;
		this.pool=pool;
	}
	
	public synchronized void setTarget(Runnable target)
	{
		this.target=target;
		notifyAll();
	}
	
	public synchronized void shutdown()
	{
		this.isShutdown=true;
		notifyAll();
	}
	public void run()
	{
		while(!isShutdown)
		{
			if(target!=null)
			{
				target.run();
				pool.repool(this);
				target=null;
			}
			try
			{
				synchronized(this)
				{
					wait();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
