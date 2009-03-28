package ch12;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

public class StopAnalyzerTest {

	
	public StopAnalyzerTest(String name)
	{
	}
	
	public static void main(String[] args) 
	{
		// 构建一个StopAnalyzer的分析器
		Analyzer a = new StopAnalyzer();
		StringReader  sr = new StringReader("People are always talking about 'the problem of youth'.");
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
