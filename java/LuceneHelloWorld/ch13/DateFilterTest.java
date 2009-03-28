package ch13;
import java.io.IOException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DateFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;

public class DateFilterTest  
{

	public static void main(String[] args) throws IOException
	{
		//	创建一个索引
        RAMDirectory indexStore = new RAMDirectory();
		//生成索引书写器
        IndexWriter writer = new IndexWriter(indexStore, new SimpleAnalyzer(), true);
			//取得系统当前时间
        long now = System.currentTimeMillis();
		//生成一个Document对象
        Document doc = new Document();
        // 添加过去的一个时间
        doc.add(Field.Keyword("datefield", DateField.timeToString(now - 1000)));
		//添加要索引的内容
        doc.add(Field.Text("body", "Today is a very sunny day in New York City"));
        
	  	try
	  	{
			//将文本添到索引中
			writer.addDocument(doc);
			//优化索引
			writer.optimize();
			//关闭
			writer.close();
		} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
 		//创建检索器
		IndexSearcher searcher = new IndexSearcher(indexStore);
	
		//过滤应该在检索结果中保存下来的
		DateFilter df1 = DateFilter.Before("datefield", now);

		//过滤不应该在检索结果中出现的
		DateFilter df2 = DateFilter.Before("datefield", now - 999999);
	
		// 没有使用过滤器的检索
		Query query1 = new TermQuery(new Term("body", "NoMatchForThis"));
	
		// 检索不存在的内容
		Query query2 = new TermQuery(new Term("body", "sunny"));
		//生成保存检索结果的对象
		Hits result;

			try 
		{
		//使用query1,返回检索结果
			result = searcher.search(query1);
		
		System.out.println("There is "+result.length()+"Document(s) matched!");
		} catch (IOException e) 
			{
				e.printStackTrace();
			}
		
	}
}
