package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;

public class PhraseQueryTest {
	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("content", "david mary smith robert"));
		doc1.add(Field.Keyword("title", "doc1"));

		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
		writer.setUseCompoundFile(true);
		writer.addDocument(doc1);
		writer.close();

		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Term word1 = new Term("content", "david");
		Term word2 = new Term("content","mary");
		Term word3 = new Term("content","smith");
		Term word4 = new Term("content","robert");
		
		Hits hits = null;
		PhraseQuery query = null;

		// 第一种情况，两个词本身紧密相连，先设置坡度为0，再设置坡度为2
		query = new PhraseQuery();
		query.add(word1);
		query.add(word2);
		query.setSlop(0);
		hits = searcher.search(query);
		printResult(hits, "'david'与'mary'紧紧相隔的Document");
		
		query.setSlop(2);
		hits = searcher.search(query);
		printResult(hits, "'david'与'mary'中相隔两个词的短语");
		
		// 第二种情况，两个词本身相隔两个词，先设置坡度为0，再设置坡度为2
		query = new PhraseQuery();
		query.add(word1);
		query.add(word4);
		query.setSlop(0);
		hits = searcher.search(query);
		printResult(hits, "'david'与'robert'紧紧相隔的Document");
		
		query.setSlop(2);
		hits = searcher.search(query);
		printResult(hits, "'david'与'robert'中相隔两个词的短语");
		

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
