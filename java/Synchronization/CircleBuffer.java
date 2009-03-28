import java.util.ArrayList;
import java.util.List;

public class CircleBuffer
{
	private List list;
	private int i = 0, j = 0;
	private int count;
	
	public CircleBuffer(int bufferCount)
	{
		this.count = bufferCount;
		list = new ArrayList(count);
	}
	
	public synchronized void add(Object o)
	{
		list.add(i,o);
		System.out.println("Add(" + i + "):" +  o.toString());
		i = (i + 1) % count;
	}
	
	public synchronized Object remove()
	{
		Object o = list.get(j);
		System.out.println("Remove(" + j + "):" +  o.toString());
		j = (j + 1) % count;
		return o;
	}
	
	
	
}