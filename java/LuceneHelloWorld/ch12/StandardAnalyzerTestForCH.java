package ch12;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class StandardAnalyzerTestForCH {

		// 构造函数
	public StandardAnalyzerTestForCH ()
	{
	}
	
	public static void main(String[] args) 
	{
		// 初始化一个StandardAnalyzer对象
		Analyzer a = new StandardAnalyzer();
		// 测试字符串
		StringReader  sr = new StringReader("龙门石窟位于山西省大同市西郊，是我国古代艺术的瑰宝！");
		// 获取“词流”
		TokenStream ts = a.tokenStream(sr);
		
		try {
			int i=0;
			// 取出下一个切分好的词
			Token t = ts.next();
			while(t!=null)
			{
				i++;
				// 打印
				System.out.println("Line"+i+":"+t.termText());
				// 继续获取
				t=ts.next();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
