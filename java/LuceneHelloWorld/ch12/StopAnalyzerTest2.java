package ch12;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;


public class StopAnalyzerTest2 {
	
	public StopAnalyzerTest2()
	{
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		String[] stopwords = {"always"};
		Analyzer a = new StopAnalyzer(stopwords);
		StringReader  sr = new StringReader("People are always talking about 'the problem of youth'.");
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
