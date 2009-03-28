package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.RangeQuery;

public class PhrasePrefixQueryTest {
	public static void main(String[] args) throws Exception {
		Document doc1 = new Document();
		doc1.add(Field.Text("content", "david mary smith robert"));
		doc1.add(Field.Keyword("title", "doc1"));

		IndexWriter writer = new IndexWriter("c:\\index",
				new StandardAnalyzer(), true);
		writer.addDocument(doc1);
		writer.close();

		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Term word1 = new Term("content", "david");
		Term word2 = new Term("content", "mary");
		Term word3 = new Term("content", "smith");
		Term word4 = new Term("content", "robert");

		Hits hits = null;
		PhrasePrefixQuery query = null;
		
		query = new PhrasePrefixQuery();
		// 加入可能的所有不确定的词
		query.add(new Term[]{word1, word2});
		// 加入确定的词
		query.add(word4);
		query.setSlop(2);
		hits = searcher.search(query);
		printResult(hits, "存在短语'david robert'或'mary robert'的文档");
		
		
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
