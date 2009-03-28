//Consumer.java

public class Consumer extends Thread
{
	private CircleBuffer circleBuffer;
	private boolean interrupted = false;
	
	public Consumer(CircleBuffer circleBuffer)
	{
		this.circleBuffer = circleBuffer;
	}
	
	public void interrupte()
	{
		interrupted = true;
	}
	
	private void comsumer(Object o)
	{
		Double d  = (Double) o;
		System.out.println("Consume:" + d.toString());
	}
	
	public void run()
	{
		while(!interrupted)
		{
			Main.full.P();
			Object o = circleBuffer.remove();
			Main.empty.V();
			comsumer(o);
		}
	}
}