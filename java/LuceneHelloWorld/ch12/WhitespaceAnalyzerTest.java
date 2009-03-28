package ch12;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class WhitespaceAnalyzerTest {

	
	public WhitespaceAnalyzerTest(){}
	
	public static void main(String[] args) 
	{
		// 构建一个WhitespaceAnalyzer
		Analyzer a = new WhitespaceAnalyzer();

		// 输入字符串
		StringReader  sr = new StringReader("In the early days of the settlement of Australia,enterprising settlers unwisely introduced the European rabbit.");
		// 获取TokenStream
		TokenStream ts = a.tokenStream(sr);
		
		try {
			int i=0;
			Token t = ts.next();
			while(t!=null)
			{
				i++;
				System.out.println("Line"+i+":"+t.termText());
				t=ts.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
