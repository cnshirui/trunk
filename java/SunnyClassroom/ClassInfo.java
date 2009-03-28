import java.io.*;
import java.util.*;


public class ClassInfo
{
	// read classroom.dat to construct a array 
	public ClassInfo()
	{
		readData("classroom.dat");
	}
	
	public void readData(String filename)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String s = in.readLine();
			StringTokenizer t = new StringTokenizer(s);
			
			year = Integer.parseInt(t.nextToken());
			half = Integer.parseInt(t.nextToken());
			lines = Integer.parseInt(t.nextToken());
			infoArray = new int[lines][12];
			
			
			for (int i=0; i<lines; i++)
			{
				s = in.readLine();
				t = new StringTokenizer(s);
				for (int j=0; j<12; j++)
				{
					if (t.hasMoreTokens())
					{
						infoArray[i][j] = Integer.parseInt(t.nextToken());
					}
				}
			}
			in.close();
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}	
	}
	
	// 这些参数都是从界面直接返回，下标从0开始，转为现实数据时加1
	public int[] searchRoom(int zone, int week, int single, boolean[] orders)
	{
		//参数验证
		if (zone<0 || zone>5)
		{
			System.out.println("教学区信息错误！");
			return null;
		}
		if (week<0 || week>4)
		{
			System.out.println("日期信息错误！");
			return null;
		}	
		if (single<0 || single>2)
		{
			System.out.println("单双周信息错误！");
			return null;
		}
		if (orders.length!=10)
		{
			System.out.println("节次信息错误！");
			return null;
		}
		
		boolean orderEmpty = true;
		for (int i=0; i<10; i++)
		{
			if (orders[i]==true)
			{
				orderEmpty = false;
			}
		}
		if (orderEmpty)
		{
			System.out.println("查询节次为空！");
			return null;
		}
		
		
		
		int numRoom = (int)(lines-1)/5 + 1;
		System.out.println("numRoom:"+numRoom);
		boolean[] roomValid = new boolean[numRoom];  // 该数据下标减一乘五到infoArray取教室号
		for(int i=0; i<numRoom; i++)
		{
			roomValid[i] = true;						//默认是所有的教室全部符合要求
		}
		
		//星期验证
		int begin = week;								//每个教室五天按顺序排在一起，隔五而搜
		
		//教学区验证
		for (int i=0; i<numRoom; i++)
		{
			if (infoArray[i*5+begin][0]/10000 != (zone+1))
			{
				roomValid[i] = false;
			}
		}
		
		//就单双周和节次检索
		switch(single)
		{
		case 0:  // 要求单双周都没有课，课表该段必须为0
			for (int i=0; i<numRoom; i++)
			{
				for(int j=1; j<11; j++)
				{
					if (roomValid[i] && orders[j-1] && infoArray[i*5+begin][j]!=0)
					{
						roomValid[i] = false;
					}
				}
			}
			break;
		case 1:		//要求单周没课
			for (int i=0; i<numRoom; i++)
			{
				for(int j=1; j<11; j++)
				{
					if (roomValid[i] && orders[j-1] && (infoArray[i*5+begin][j]==1 || infoArray[i*5+begin][j]==2))
					{
						roomValid[i] = false;
					}
				}
			}			
			break;
		case 2:
			for (int i=0; i<numRoom; i++)
			{
				for(int j=1; j<11; j++)
				{
					if (roomValid[i] && orders[j-1] && (infoArray[i*5+begin][j]==1 || infoArray[i*5+begin][j]==3))
					{
						roomValid[i] = false;
					}
				}
			}				
			break;
		default:
			return null;	
		}
		
		int numValid = 0;
		for (int i=0; i<numRoom; i++)
		{
			if (roomValid[i])
			{
				numValid ++;
			}
		}
		int[] res = new int[numValid];
		
		int j = 0;
		for (int i=0; i<numRoom; i++)			// 可行的教室号复制到结果数组中
		{
			if (roomValid[i])
			{
				res[j++] = infoArray[i*5+begin][0];
			}
		} 
		
		return res;
	}

	public void geneArray() //把classroom.dat文件写成数组的格式，复制到J2ME程序中
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter("array.dat"));
			for (int i=0; i<lines; i++)
			{
				out.print("{");
				for (int j=0; j<11; j++)
				{
					out.print(infoArray[i][j]+",");
				}
				out.println(infoArray[i][11]+"},");
			}
			out.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
		System.out.println("Array has been copied into array.dat!");
	}
	
	public void showInfoArray()
	{
		for (int i=0; i<lines; i++)
		{
			for (int j=0; j<12; j++)
			{
				System.out.print(infoArray[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public String displayInfo()
	{
		String info = " 适用于南京大学浦口校区\n" + year;
		String isFirst = half==1?"上半学年":"下半学年";
		info += isFirst;
		info += ("共"+lines+"条记录\n");
		info += "Copyright◎小百合工作室";
		
		return info;
	}
	
	private int[][] infoArray;
	private int year;
	private int half;
	private int lines;
}


/********************************************************************************
			//************************************************
			//显示剩下的结果之用
			System.out.println("after case 0:");
			for (int i=0; i<numRoom; i++)
			{

				if (roomValid[i])
				{
					for (int j=0; j<12; j++)
					{
						System.out.print(infoArray[i*5+begin][j]+" ");
					}
					System.out.println();
//					System.out.println("----------------------------");					
				}
			}
			//************************************************
//先显示
for (int j=0; j<12; j++)
{
	System.out.print(infoArray[i*5+begin][j]+" ");
}
System.out.println();



//*****************************************************
int numValid = 0;
for (int i=0; i<numRoom; i++)
{
	if (roomValid[i])
	{
		numValid ++;
	}
}
int[] res = new int[numValid];

int k = 0;
for (int i=0; i<numRoom; i++)			// 可行的教室号复制到结果数组中
{
	if (roomValid[i])
	{
		res[k++] = infoArray[i*5+begin][0];
	}
} 
System.out.println("**Total:"+res.length);
for (int i=0; i<res.length; i++)
{
	System.out.println(res[i]);
}		

//*****************************************************/
