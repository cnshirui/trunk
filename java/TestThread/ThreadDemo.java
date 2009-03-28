
 public class ThreadDemo extends Thread
{
	int ID = 0;
	
	public ThreadDemo(int num)
	{
		this.ID = num;
	}

	public void run()
	{
		System.out.println("Thread " + ID + " Runing");
	}
	
	public static void main(String[] args)
	{
		for(int i=0; i<200; i++)
		{
			new ThreadDemo(i).start();
		}
	}
}
