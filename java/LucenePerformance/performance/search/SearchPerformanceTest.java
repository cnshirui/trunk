package performance.search;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public final class SearchPerformanceTest {

	private IndexSearcher searcher = null;

	private Analyzer analyzer = null;

	public SearchPerformanceTest() throws IOException {
		analyzer = new StandardAnalyzer();
		searcher = new IndexSearcher(IndexReader.open("c:\\index"));
	}

	/**
	 * 
	 * @param queryString
	 *            The keyword a user typed
	 * @param field
	 *            The field to search for
	 * @return search result, or null if anything went wrong
	 */
	public final Hits search(String queryString, String field) {
		if (searcher != null) {
			try {
				QueryParser parser = new QueryParser(field, analyzer);
				parser.setOperator(QueryParser.DEFAULT_OPERATOR_AND);
				Query query = parser.parse(queryString);
				return searcher.search(query);
			} catch (ParseException e) {
				// logs here
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// logs here
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public static void printResult(Hits h)
	{
		if (h.length() == 0)
		{
			System.out.println("Sorry, nothing found!");
		}
		else
		{
			for (int i=0; i<h.length(); i++)
			{
				try
				{
					Document doc = h.doc(i);
					System.out.print("This is " + i + "result, filename: ");
					System.out.println(doc.get("path"));
				}
				catch ( Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println("----------------------------------------");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
/*		SearchPerformanceTest searcher = new SearchPerformanceTest();
		Date start = new Date();
		Hits h = searcher.search("爱","contents");
		Date end = new Date();
		System.out.println("总共耗时" + (end.getTime()-start.getTime()) + "毫秒" );
		System.out.println("总共找到" + h.length() + "个文件");
		
		printResult(h);
*/
		SearchPerformanceTest searcher = new SearchPerformanceTest();
		searcher.displaySearch("爱");
	}
	
	public void displaySearch(String str)
	{
		Date start = new Date();
		Hits h = this.search(str,"contents");
		Date end = new Date();
		System.out.println("总共耗时" + (end.getTime()-start.getTime()) + "毫秒" );
		System.out.println("总共找到" + h.length() + "个文件");
		
		printResult(h);
	}

}
