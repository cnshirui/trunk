package ch10;

import java.io.IOException;

import org.apache.lucene.index.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;

public class IndexWriterTest {
	
	public static void main (String  [] args) {
		
		Document doc1 = new Document();
		doc1.add(Field.Keyword("name","Robert"));
		doc1.add(Field.Keyword("title","manager"));
		
		Document doc2 = new Document();
		doc2.add(Field.Keyword("name","David"));
		doc2.add(Field.Keyword("title","manager"));
		
		Document doc3 = new Document();
		doc3.add(Field.Keyword("name","Seraph"));
		doc3.add(Field.Keyword("title","Employee"));
		
		Document doc4 = new Document();
		doc4.add(Field.Keyword("name","Cindy"));
		doc4.add(Field.Keyword("title","Employee"));
		
		IndexWriter writer = null;
		try {
			writer = new IndexWriter("c:\\index", new StandardAnalyzer(), true);
			writer.addDocument(doc1);
			writer.addDocument(doc2);
			writer.addDocument(doc3);
			writer.addDocument(doc4);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
