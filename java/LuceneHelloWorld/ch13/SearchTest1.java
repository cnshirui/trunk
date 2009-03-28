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

public class SearchTest1 
{
	public static void main(String[] args)
	{
		 try 
		 {
			//生成搜索器
		Searcher searcher = new IndexSearcher("C:\\IndexDir");
			//生成分析器
		Analyzer analyzer = new StandardAnalyzer();
			//生成Query对象
		Query query = MultiFieldQueryParser.parse("Lucene", new String[]{"title","contents"}, analyzer);
			//
		System.out.println("Searching for: " + query.toString("contents"));
			//返回检索结果
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
