package ch15;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class DivideWordTest1 {
	public static void main(String[] args) throws Exception {
		StringReader reader = new StringReader("中华人民共和国");
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream ts = analyzer.tokenStream(reader);
		
		Token t = ts.next();
		while (t != null) {
			System.out.println(t.termText());
			t = ts.next();
		}
	}
}
