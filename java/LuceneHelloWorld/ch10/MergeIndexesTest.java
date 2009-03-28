package ch10;

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.*;

public class MergeIndexesTest {
	public static void main(String[] args) throws Exception{
		
		FSDirectory fsDir = FSDirectory.getDirectory("c:\\index", true);
		RAMDirectory ramDir = new RAMDirectory();
		
		IndexWriter fsWriter = new IndexWriter(fsDir, new StandardAnalyzer(), true);
		IndexWriter ramWriter = new IndexWriter(ramDir, new StandardAnalyzer(), true);
		
		Document doc1 = new Document();
		doc1.add(Field.Text("name", "word1 word2 word3"));

		Document doc2 = new Document();
		doc2.add(Field.Text("name", "word1 word2 word3"));
		
		ramWriter.addDocument(doc1);
		fsWriter.addDocument(doc2);
		
		ramWriter.close();
		
		fsWriter.addIndexes(new Directory[]{fsDir});
		fsWriter.close();
		
		IndexSearcher searcher = new IndexSearcher("c:\\index");
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
