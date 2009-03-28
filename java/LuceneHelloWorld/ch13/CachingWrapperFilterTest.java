package ch13;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.MockFilter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class CachingWrapperFilterTest  
{

	public static void main(String[] args) throws IOException 
	{
		//生成索引目录
		Directory dir = new RAMDirectory();
		//索引书写器
	    IndexWriter writer;
		try 
		{
			//
			writer = new IndexWriter(dir, new StandardAnalyzer(), true);
			//关闭
			writer.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	    
		//索引阅读器
	    IndexReader reader = IndexReader.open(dir);
	    
	    //
	    MockFilter filter = new MockFilter();
	    CachingWrapperFilter cacher = new CachingWrapperFilter(filter);
	    
	    //
	    cacher.bits(reader);
	    System.out.println("The first time the Filter was called? "+filter.wasCalled());

	    //清空过滤器
	    filter.clear();
	    cacher.bits(reader);
	    System.out.println("The first time the Filter was called? "+filter.wasCalled());
	}
}
