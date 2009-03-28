package ch10;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;

public class MaxFieldLengthTest {

	public static final String path = "c:\\index";

	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("name", "word1 word2 word3"));

		Document doc2 = new Document();
		doc2.add(Field.Text("name", "word1 word2 word3"));

		IndexWriter writer = new IndexWriter(path, new StandardAnalyzer(), true);
		writer.maxFieldLength = 1;
		writer.addDocument(doc1);
		writer.maxFieldLength = 3;
		writer.addDocument(doc2);
		writer.close();

		IndexSearcher searcher = new IndexSearcher(path);
		Hits hits = null;
		Query query = null;

		query = QueryParser
				.parse("word1", "name", new StandardAnalyzer());
		hits = searcher.search(query);
		System.out.println("查找 word1 共" + hits.length() + "个结果");

		query = QueryParser
				.parse("word3", "name", new StandardAnalyzer());
		hits = searcher.search(query);
		System.out.println("查找 word3 共" + hits.length() + "个结果");

	}
}
