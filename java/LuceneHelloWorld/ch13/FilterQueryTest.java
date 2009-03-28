package ch13;
import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;

public class FilterQueryTest  
{

	public static void main(String[] args) throws IOException 
	{
		//生成索引目录
		RAMDirectory directory = new RAMDirectory();
	    IndexWriter writer = new IndexWriter (directory, new WhitespaceAnalyzer(), true);
		
	    //生成Document对象
	    Document doc = new Document();
	    //将检索内容添加到Document对象中
	    doc.add (Field.Text ("field", "one two three four five"));
	    doc.add (Field.Text ("sorter", "b"));
		//将文档添加进索引
	    writer.addDocument (doc);
		
	    //生成Document对象
	    doc = new Document();
		//将检索内容添加到Document对象中
	    doc.add (Field.Text ("field", "one two three four"));
	    doc.add (Field.Text ("sorter", "d"));
		//将文档添加进索引
	    writer.addDocument (doc);

	    ////生成Document对象
	    doc = new Document();
		//将检索内容添加到Document对象中
	    doc.add (Field.Text ("field", "one two three y"));
	    doc.add (Field.Text ("sorter", "a"));
		//将文档添加进索引
	    writer.addDocument (doc);
		
	    //生成Document对象
	    doc = new Document();
		//将检索内容添加到Document对象中
	    doc.add (Field.Text ("field", "one two x"));
	    doc.add (Field.Text ("sorter", "c"));
		//将文档添加进索引
	    writer.addDocument (doc);
	    
	    //优化索引
	    writer.optimize ();
		//关闭索引
	    writer.close ();

	    //生成IndexSearcher对象
	    IndexSearcher searcher = new IndexSearcher (directory);
		//生成查询对象
	    Query query = new TermQuery (new Term ("field", "three"));
		
	    //过滤器对象
	    Filter filter = new Filter() 
	    {
	      public BitSet bits (IndexReader reader) throws IOException 
	      {
	        BitSet bitset = new BitSet(5);
	        bitset.set (1);
	        bitset.set (3);
	        return bitset;
	      }
	    };
	    
	    //生成带有过滤器的查询对象
	    Query filteredquery = new FilteredQuery (query, filter);
		//返回检索结果
	    Hits hits = searcher.search (filteredquery);
	    System.out.println("There is "+hits.length()+" Document(s) matched!");
	    
	    //返回检索结果
	    hits = searcher.search (filteredquery, new Sort("sorter"));
	    System.out.println("There is "+hits.length()+" Document(s) matched!");
	    
	    //生成带有过滤器的查询对象
	    filteredquery = new FilteredQuery (new TermQuery (new Term ("field", "one")), filter);
		//返回检索结果
	    hits = searcher.search (filteredquery);
	    System.out.println("There is "+hits.length()+" Document(s) matched!");
	    
	    //生成带有过滤器的查询对象
	    filteredquery = new FilteredQuery (new TermQuery (new Term ("field", "x")), filter);
		//返回检索结果
	    hits = searcher.search (filteredquery);
	    System.out.println("There is "+hits.length()+" Document(s) matched!");
	    
	    //生成带有过滤器的查询对象
	    filteredquery = new FilteredQuery (new TermQuery (new Term ("field", "y")), filter);
		//返回检索结果
	    hits = searcher.search (filteredquery);
	    System.out.println("There is "+hits.length()+" Document(s) matched!");
	    
	}
}
