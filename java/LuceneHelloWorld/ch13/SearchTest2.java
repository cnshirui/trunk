package ch13;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

public class SearchTest2 
{
	public static void main(String[] args)
	{
		 try 
		 {
			//
			Searcher searcher = new IndexSearcher("C:\\IndexDir");
			//
			Analyzer analyzer = new StandardAnalyzer();
			//
			Query query = MultiFieldQueryParser.parse("Lucene", new String[]{"title","contents"},new int[]{MultiFieldQueryParser.REQUIRED_FIELD,MultiFieldQueryParser.PROHIBITED_FIELD}, analyzer);
			//
			System.out.println("Searching for: " + query.toString("contents"));
			//
			Hits hits = searcher.search(query);
			//
			System.out.println(hits.length() + " total matching documents");
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}
	}
}
