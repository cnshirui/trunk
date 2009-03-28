//Producer.java

public class Producer extends Thread
{
	private CircleBuffer circleBuffer;
	private boolean interrupted = false;
	
	public Producer(CircleBuffer circleBuffer)
	{
		this.circleBuffer = circleBuffer;
	}
	
	public void interrupte()
	{
		interrupted = true;
	}
	
	private Object produce()
	{
		Double d  = new Double(Math.random());
		System.out.println("Produce:" + d.toString());
		return d;
	}
	
	public void run()
	{
		while(!interrupted)
		{
			Object o = produce();
			Main.empty.P();
			circleBuffer.add(o);
			Main.full.V();
		}
	}
}