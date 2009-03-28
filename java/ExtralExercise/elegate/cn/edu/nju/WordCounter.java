package elegate.cn.edu.nju;

import java.io.*;

public class WordCounter 
{
	private static long bytesCnt=0;
	private static long wordCnt=0;
	private static long lineCnt=0;
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			System.out.println("Input your text:");
			countWords(System.in);
			System.out.println("bytesCnt="
					+bytesCnt+",wordCnt="
					+wordCnt+",lineCnt="+lineCnt);
		}
		else
		{
			for(int i=0;i<args.length;i++)
			{
				try
				{
					FileInputStream in=new FileInputStream(args[i]);
					countWords(in);
					in.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			System.out.println("bytesCnt="
					+bytesCnt+",wordCnt="
					+wordCnt+",lineCnt="+lineCnt);
		}
	}
	
	private static void countWords(InputStream input)
	{
		boolean wordStart=false;
		try
		{
			int b=input.read();
			while(b>0)
			{
				bytesCnt++;
				if(Character.isLetterOrDigit(b)||b=='_')
				{
					wordStart=true;
					b=input.read();
				}
				else
				{
					if(wordStart)
					{
						wordCnt++;
						wordStart=false;
					}
					if( b=='\n')
						lineCnt++;
					b=input.read();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
