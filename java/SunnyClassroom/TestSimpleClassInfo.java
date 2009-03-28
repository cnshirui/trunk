
public class TestSimpleClassInfo
{
	public static void main(String[] args)
	{
		SimpleClassInfo cl = new SimpleClassInfo();
		System.out.println(cl.displayInfo());
		cl.showInfoArray();
		
		int zone = 0;
		int week = 1;
		int single = 2;
		boolean[] orders = {true, true, false, false, false, false, false, false, false, false};
		int[] res = cl.searchRoom(zone, week, single, orders);
		
		if (res == null)
		{
			System.out.println("There is no result!");
		}
		else
		{
			System.out.println("Total:"+res.length);
			for (int i=0; i<res.length; i++)
			{
				System.out.println(res[i]);
			}			
		}
	}
}
