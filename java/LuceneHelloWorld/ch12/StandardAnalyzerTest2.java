package ch12;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class StandardAnalyzerTest2 {

	/**
	 * @param args
	 */

	public StandardAnalyzerTest2() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Analyzer a = new StandardAnalyzer();

//		StringReader sr = new StringReader("123,456" + "CNN"
//				+ "abc123@cba.com.cn" + "Microsoft" + "I		think ..."
//				+ "192.168.0.1" + "中国驰名商标");

//		 StringReader sr = new StringReader("123,456");

//		 StringReader sr = new StringReader("CNN"+":"+"Cable News Network");

//		 StringReader sr = new StringReader("abc123@cba.com.cn");

//		 StringReader sr = new StringReader("I think ...,");

		 StringReader sr = new StringReader("192.168.0.1");

		// StringReader sr = new StringReader("中国驰名商标");

		TokenStream ts = a.tokenStream(sr);

		try {
			int i = 0;
			Token t = ts.next();
			while (t != null) {
				i++;
				System.out.println("Line" + i + ":" + t.termText());
				t = ts.next();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
