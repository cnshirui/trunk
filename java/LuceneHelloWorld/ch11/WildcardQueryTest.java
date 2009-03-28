package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.WildcardQuery;

public class WildcardQueryTest {
	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("content", "whatever"));
		doc1.add(Field.Keyword("title", "doc1"));
		
		Document doc2 = new Document();
		doc2.add(Field.Text("content", "whoever"));
		doc2.add(Field.Keyword("title", "doc2"));
		
		Document doc3 = new Document();
		doc3.add(Field.Text("content", "however"));
		doc3.add(Field.Keyword("title", "doc3"));
		
		Document doc4 = new Document();
		doc4.add(Field.Text("content", "everest"));
		doc4.add(Field.Keyword("title", "doc4"));
		
		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
		writer.addDocument(doc4);
		writer.close();

		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Term word1 = new Term("content", "*ever");
		Term word2 = new Term("content", "wh?ever");
		Term word3 = new Term("content", "h??ever");
		Term word4 = new Term("content", "ever*");
		WildcardQuery query = null;
		Hits hits = null;
		
		query = new WildcardQuery(word1);
		hits = searcher.search(query);
		printResult(hits, "*ever");
		
		query = new WildcardQuery(word2);
		hits = searcher.search(query);
		printResult(hits, "wh?ever");
		
		query = new WildcardQuery(word3);
		hits = searcher.search(query);
		printResult(hits, "h??ever");
		
		query = new WildcardQuery(word4);
		hits = searcher.search(query);
		printResult(hits, "ever*");
	}
	
	public static void printResult(Hits hits, String key) throws Exception {
		System.out.println("查找 \"" + key + "\" :");
		if (hits != null) {
			if (hits.length() == 0) {
				System.out.println("没有找到任何结果");
				System.out.println();
			} else {
				System.out.print("找到");
				for (int i = 0; i < hits.length(); i++) {
					Document d = hits.doc(i);
					String dname = d.get("title");
					System.out.print(dname + "   ");
				}
				System.out.println();
				System.out.println();
			}
		}
	}
}
