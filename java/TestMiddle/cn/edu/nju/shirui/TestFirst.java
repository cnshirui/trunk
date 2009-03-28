package cn.edu.nju.shirui;

public class TestFirst
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		TestFirst tf = new TestFirst();
		tf.arr = new int[][] {{1,2},{3,4}};
		System.out.println(tf.arr.length);
		for(int i=0; i<2; i++)
			for (int j=0; j<2; j++)
				System.out.println(tf.arr[i][j]);
	}
	public int[][] arr;

}
