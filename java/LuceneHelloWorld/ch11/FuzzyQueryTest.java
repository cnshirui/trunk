package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;

public class FuzzyQueryTest {
	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("content", "david"));
		doc1.add(Field.Keyword("title", "doc1"));

		Document doc2 = new Document();
		doc2.add(Field.Text("content", "sdavid"));
		doc2.add(Field.Keyword("title", "doc2"));

		Document doc3 = new Document();
		doc3.add(Field.Text("content", "davie"));
		doc3.add(Field.Keyword("title", "doc3"));

		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
		writer.close();

		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Term word1 = new Term("content", "david");

		Hits hits = null;
		FuzzyQuery query = null;

		query = new FuzzyQuery(word1);
		hits = searcher.search(query);
		printResult(hits,"与'david'相似的词");
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
