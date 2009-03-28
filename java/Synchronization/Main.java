//Main.java

public class Main
{
	public static int BUFFER_LEN = 20;
	public static Semaphore full = new Semaphore(0), empty = new Semaphore(BUFFER_LEN);
	
	public static void main(String[] argv)
	{
		CircleBuffer circleBuffer = new CircleBuffer(BUFFER_LEN);
		Producer producer = new Producer(circleBuffer);
		Consumer consumer = new Consumer(circleBuffer);
		
		producer.start();
		consumer.start();
	
	
		try
		{
			Thread.sleep(500);
		}catch(InterruptedException ie)
		{
			ie.printStackTrace();
		}
		producer.interrupt();
		consumer.interrupt();
	}
}