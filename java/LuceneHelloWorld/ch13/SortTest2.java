package ch13;

import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

class SortTest2
{
  public static void main(String[] args) 
  {
    try 
    {
		//生成索引目录
      Directory directory = new RAMDirectory();
		//生成分析器
      Analyzer analyzer = new SimpleAnalyzer();
		//生成索引书写器
      IndexWriter writer = new IndexWriter(directory, analyzer, true);
		//将要进行检索的文本内容
      String[] docs = 
      {
        "a b c d e",
        "a b c d e a b c d e",
        "a b c d e f g h i j",
        "a c e",
        "e c a",
        "a c e a c e",
        "a c e a b c"
      };
		//循环遍历检索检索结果
      for (int j = 0; j < docs.length; j++)
      {	
		//取得文档
        Document d = new Document();
		//添加检索内容
        d.add(Field.Text("contents", docs[j]));
		//添加进索引
        writer.addDocument(d);
      }
		//关闭索引
      writer.close();
		//生成检索对象
      Searcher searcher = new IndexSearcher(directory);
      	//备用检索的字符串
      String[] queries = {
 	"\"a b\"",
 	// 	"\"a b c\"",
 	// 	"\"a c\"",
 	// "\"a c e\"",
      };
		//保存检索结果
      Hits hits = null;
		//生成检索对象
      QueryParser parser = new QueryParser("contents", analyzer);
   
		//依次进行检索
      for (int j = 0; j < queries.length; j++) 
      {
		//生成检索对象
        Query query = parser.parse(queries[j]);
        	//
        System.out.println("Query: " + query.toString("contents"));

		//返回检索结果
		 hits = searcher.search(query,Sort.RELEVANCE);

		//生成排序类
        //Sort sort = new Sort();
	    //hits = searcher.search(query,sort);
        System.out.println(hits.length() + " total results");
        	
        //遍历返回的检索结果
        for (int i = 0 ; i < hits.length() && i < 10; i++)
        {
		//取得文档
          Document d = hits.doc(i);
		//
          System.out.println(i + " " + hits.score(i)+ " " + d.get("contents"));
        }
      }
		//关闭检索器
      searcher.close();
    } catch (Exception e) 
	    {
		//
	      System.out.println(" caught a " + e.getClass() +
				 "\n with message: " + e.getMessage());
	    }
  }
}
