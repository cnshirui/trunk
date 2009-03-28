package ch10;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class UndeleteTest {
	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("name", "word1 word2 word3"));
		Document doc2 = new Document();
		doc2.add(Field.Text("name", "word4 word5 word6"));

		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.close();

		IndexReader reader = IndexReader.open("c:\\index");
		reader.delete(0);
		
		reader.close();
		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Query query = null;
		Hits hits = null;
		
		query = QueryParser.parse("word1", "name", new StandardAnalyzer());
		hits = searcher.search(query);
		System.out.println("查找 word1 共" + hits.length() + "个结果");

		query = QueryParser.parse("word4", "name", new StandardAnalyzer());
		hits = searcher.search(query);
		System.out.println("查找 word4 共" + hits.length() + "个结果");
		
//		reader.undeleteAll();




	}
}
