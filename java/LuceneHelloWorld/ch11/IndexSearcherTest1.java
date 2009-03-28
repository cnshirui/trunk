package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class IndexSearcherTest1 {

	public static void main(String[] args) throws Exception {

		Document doc1 = new Document();
		doc1.add(Field.Text("name", "word1 word2 word3"));
		doc1.add(Field.Keyword("title", "doc1"));

		Document doc2 = new Document();
		doc2.add(Field.Text("name", "word4 word5 word6"));
		doc2.add(Field.Keyword("title", "doc2"));

		Document doc3 = new Document();
		doc3.add(Field.Text("name", "word1 word4"));
		doc3.add(Field.Keyword("title", "doc3"));

		Document doc4 = new Document();
		doc4.add(Field.Text("name", "word2 word5"));
		doc4.add(Field.Keyword("title", "doc4"));

		Document doc5 = new Document();
		doc5.add(Field.Text("name", "word3 word6"));
		doc5.add(Field.Keyword("title", "doc5"));

		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
        writer.setUseCompoundFile(true);
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
		writer.addDocument(doc4);
		writer.addDocument(doc5);
		writer.close();

		Query query = null;
		Hits hits = null;

		String key1 = "word1";
		String key2 = "word2";
		String key3 = "word3";
		String key4 = "word4";
		String key5 = "word5";
		String key6 = "word6";

		IndexSearcher searcher = new IndexSearcher("c:\\index");
		
		query = QueryParser.parse(key1, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key1);
		
		query = QueryParser.parse(key2, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key2);
		
		query = QueryParser.parse(key3, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key3);
		
		query = QueryParser.parse(key4, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key4);
		
		query = QueryParser.parse(key5, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key5);
		
		query = QueryParser.parse(key6, "name", new StandardAnalyzer());
		hits = searcher.search(query);
		printResult(hits, key6);

	}

	public static void printResult(Hits hits, String key) throws Exception {
		System.out.println("查找 \"" + key + "\" :");
		if (hits != null) {
			if (hits.length() == 0) {
				System.out.println("没有找到任何结果");
			} else {
				System.out.print("找到");
				for (int i = 0; i < hits.length(); i++) {
					Document d = hits.doc(i);
					String dname = d.get("title");
					System.out.print(dname + "   " );
				}
				System.out.println();
				System.out.println();
			}
		}
	}
}
