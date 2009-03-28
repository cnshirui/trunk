package performance.search;

import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class MuiltiSearchTest {

	public static void main(String[] args) {
		for (int i=0; i<50; i++) {
			SearchThread s = new SearchThread();
			s.start();
		}
	}
	
}

class SearchThread extends Thread
{
	public void run(){
		try{
			sleep(10);
			IndexSearcher searcher = new IndexSearcher(IndexReader.open("c:\\index"));
			QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
			parser.setOperator(QueryParser.DEFAULT_OPERATOR_AND);
			Query query = parser.parse("ÖÐ¹ú");
			Date start = new Date();
			Hits h = searcher.search(query);
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}