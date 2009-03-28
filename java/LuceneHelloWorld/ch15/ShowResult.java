package ch15;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
//import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class ShowResult {
	public static void show(Analyzer a, String s) throws Exception {
		
		StringReader reader = new StringReader(s);
		TokenStream ts = a.tokenStream(reader);
		
		Token t = ts.next();
		while (t != null) {
			System.out.println(t.termText());
			t = ts.next();
		}
	}
	
	public static void main (String [] args) throws Exception {
		Analyzer a = new StandardAnalyzer();
		String key = "中华人民共和国";
		show(a, key);
	}
}
