package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.RangeQuery;

public class RangeQueryTest {
	public static void main (String [] args) throws Exception {
		Document doc1 = new Document();
	    doc1.add(Field.Text("time", "200001"));
	    doc1.add(Field.Keyword("title", "doc1"));
	    
	    Document doc2 = new Document();
	    doc2.add(Field.Text("time", "200002"));
	    doc2.add(Field.Keyword("title", "doc2"));
	    
	    Document doc3 = new Document();
	    doc3.add(Field.Text("time", "200003"));
	    doc3.add(Field.Keyword("title", "doc3"));
	    
	    Document doc4 = new Document();
	    doc4.add(Field.Text("time", "200004"));
	    doc4.add(Field.Keyword("title", "doc4"));
	    
	    Document doc5 = new Document();
	    doc5.add(Field.Text("time", "200005"));
	    doc5.add(Field.Keyword("title", "doc5"));
	    
	    IndexWriter writer = new IndexWriter("c:\\index", new StandardAnalyzer(), true);
        writer.setUseCompoundFile(true);
		writer.addDocument(doc1);
		writer.addDocument(doc2);
		writer.addDocument(doc3);
		writer.addDocument(doc4);
		writer.addDocument(doc5);
		writer.close();
		
		IndexSearcher searcher = new IndexSearcher("c:\\index");
		Term beginTime = new Term("time","200001");
		Term endTime = new Term("time","200005");
		
		Hits hits = null;
		RangeQuery query = null;
		
		query = new RangeQuery(beginTime, endTime, false);
		hits = searcher.search(query);
		printResult(hits, "从200001到200005的文档，不包括200001和200005");
		
		query = new RangeQuery(beginTime, endTime, true);
		hits = searcher.search(query);
		printResult(hits, "从200001到200005的文档，包括200001和200005");
		
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
