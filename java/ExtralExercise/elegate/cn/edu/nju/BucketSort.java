package elegate.cn.edu.nju;
import java.util.*;

/**
 * a class test bucket sort method
 * @author Elegate
 * @author cs department of NJU
 */
public class BucketSort 
{
	
	public static void main(String[] args)
	{
		int arr[]={0,31,81,1,252,142,45,297,7,18};
		bucketSort(arr);
		System.out.println("Sorted result:");
		for(int i=0;i<arr.length;i++)
			System.out.print(arr[i]+" ");
	}
	/**
	 * sort the array using bucket sort method
	 * @param arr a int array
	 */
	@SuppressWarnings("unchecked")
	public static void bucketSort(int arr[])
	{
		int max=max(arr);
		int min=min(arr);
		int range=max-min+1;  //calculate the range
		ArrayList[] lst=new ArrayList[range];
		//sort with bucket
		for(int i=0;i<arr.length;i++)
		{
			int index=arr[i]-min;
			if(lst[index]==null)
				lst[index]=new ArrayList<Integer>();
			System.out.println("put "+arr[i]+" to bucket "+index);
			lst[index].add(new Integer(arr[i]));
		}
		int index=0;
		//save the sorted data in arr
		for(int i=0;i<lst.length;i++)
		{
			if( lst[i]!=null && lst[i].size()!=0 )
			{
				for(int j=0;j<lst[i].size();j++)
				{
					arr[index++]=
						( ( Integer )lst[i].get(j)).intValue();
				}
			}
		}
	}
	/**
	 * find the minimum number in the int array
	 * @param arr a int array
	 * @return the minimum number in the array
	 */
	public static int min(int arr[])
	{
		int min=arr[0];
		for(int i=1;i<arr.length;i++)
			if(arr[i]<min)
				min=arr[i];
		return min;
	}
	/**
	 * find the maximum number in the int array
	 * @param arr a int array
	 * @return the maximum number int the array
	 */
	public static int max(int arr[])
	{
		int max=arr[0];
		for(int i=1;i<arr.length;i++)
			if(arr[i]>max)
				max=arr[i];
		return max;
	}
}
