package elegate.cn.edu.nju;

import java.util.*;
/**
 * encypt or decrypt a string
 * @author Elegate
 * @author cs department of NJU
 */
public class Encryption 
{
	/**
	 * put the string to a smallest square matrix according row,
	 * then read according column to encrypt the original string,
	 * the null elements are ignored
	 * E.g.
	 * <pre>
	 * abcas is put into
	 * a b c
	 * a s #
	 * # # #
	 * </pre> 
	 * (a 3*3 matrix,# indicates there's no element put into)
	 * so the encrypted string is aabsc
	 * @param str the original string 
	 * @return the encrypted string 
	 */
	public static String arrayEncrypt(String str)
	{
		String encrypted="";
		int len=str.length();
		int a=leastSquare(len);
		for(int i=0;i<a;i++)
			for(int j=0;j<a;j++)
			{
				int tmp=a*j+i;
				if(tmp<len)
					encrypted+=str.charAt(tmp);
			}
		return encrypted;
	}
	/**
	 * addcording the means used in encryption of this method,decrypt the given string:str
	 * @param str the original string
	 * @return the decrypted string
	 */
	public static String  arrayDecrypt(String str)
	{
		String decrypted="";
		int len=str.length();
		int a=leastSquare(len);
		int row=(int)Math.ceil((len+0.0)/a);
		int blankCol=row*a-len;
		int col=a-blankCol;
		int count=0;
		int blankRow=a-row;
		int x=blankRow+(blankCol>0?1:0);
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<a;j++)
			{
				if(!(i==row-1&&j>=col))
				{
					int tmp=a*j+i;					
					count=j<col?(j*blankRow):(col*blankRow+x*((j-col)));
					if(j>0)
					 tmp-=count;
					if(tmp<len)
					{
						decrypted+=str.charAt(tmp);
					}
				}
			}
		}
		return decrypted;
	}
	/**
	 * add a random number to each character to encrypt a string
	 * @param str the original string
	 * @param seed the seed to generate random number
	 * @return the encrypted string
	 */
	public static String algebraEncrypt(String str,long seed)
	{
		String encrypted="";
		Random random=new Random(seed);
		int len=str.length();
		for(int i=0;i<len;i++)
		{
			encrypted+=(char)(str.charAt(i)+random.nextInt()%10);
		}
		return encrypted;
	}
	/**
	 * subtract a random number to each character to decrypt a string if and only if
	 * the seed is is correct 
	 * @param str the original string
	 * @param seed the seed to generate random number
	 * @return the decrypted string
	 */
	public static String algebraDecrypt(String str,long seed)
	{
		String decrypted="";
		Random random=new Random(seed);
		int len=str.length();
		for(int i=0;i<len;i++)
		{
			decrypted+=(char)(str.charAt(i)-random.nextInt()%10);
		}
		return decrypted;
	}
	/**
	 * xor each character in the string with the given key to encrypt a string
	 * @param str the original string
	 * @param key used to xor with each character
	 * @return the encrypted string 
	 */
	public static String logicEncrypt(String str,char key)
	{
		String encrypted="";
		int len=str.length();
		for(int i=0;i<len;i++)
		{
			encrypted+=(char)(str.charAt(i)^key);
		}
		return encrypted;
	}
	/**
	 * xor each character in the string with the given key to decrypt a string if and
	 * only if the key is correct
	 * @param str the original string
	 * @param key used to xor with each character
	 * @return the decrypted string 
	 */
	public static String logicDecrypt(String str,char key)
	{
		String decrypted="";
		int len=str.length();
		for(int i=0;i<len;i++)
		{
			decrypted+=(char)(str.charAt(i)^key);
		}
		return decrypted;
	}
	
	/**
	 * encrypt a byte using a key
	 * @param value the value to be encrypted
	 * @param key the key used to do xor operation
	 * @return the encrypted value
	 */
	public static int logicEncrypt(int value,int key)
	{
		return value^key;
	}
	/**
	 * decrypt a byte using a key
	 * @param value the value to be decrypted
	 * @param key	the key used to do xor operation
	 * @return 		the decrypted  value
	 */
	public static int logicDecrypt(int value,int key)
	{
		return value^key;
	}
	
	/**
	 * return i,where (i-1)^2<len and i^2>=len
	 * @param len a int number
	 * @return the least square root for the <em>len</em>
	 */
	private static int leastSquare(int len)
	{
		int i=(int)Math.sqrt(len);
		if(i*i==len)
			return i;
		else
			return i+1;
	}
}
