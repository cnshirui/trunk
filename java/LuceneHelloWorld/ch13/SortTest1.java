package ch13;


import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;
class SortTest
{
  public static void main(String[] args) 
  {
    try 
    {
		//生成索引目录
      Directory directory = new RAMDirectory();
		//生成分词器
      Analyzer analyzer = new SimpleAnalyzer();
		//索引书写器
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
		//循环依次建立索引
      for (int j = 0; j < docs.length; j++)
      {	
		//生成Document对象
        Document d = new Document();
		//添加内容
        d.add(Field.Text("contents", docs[j]));
		//添加到索引中
        writer.addDocument(d);
      }
		//关闭索引
      writer.close();
		//生成搜索器对象
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
		//生成分析器对象
      QueryParser parser = new QueryParser("contents", analyzer);
   
      for (int j = 0; j < queries.length; j++) 
      {
		//生成检索对象
        Query query = parser.parse(queries[j]);
        	//
        System.out.println("Query: " + query.toString("contents"));

        	//排序对象
        Sort sort = new Sort();
	
		//进行检索，返回检索结果
	    hits = searcher.search(query,sort);
        System.out.println(hits.length() + " total results");

        	//循环遍历返回的检索结果
        for (int i = 0 ; i < hits.length() && i < 10; i++)
        {
		//取得文档
          Document d = hits.doc(i);
		//
          System.out.println(i + " " + hits.score(i)+ " " + d.get("contents"));
        }
      }
	//关闭
      searcher.close();
      
    } catch (Exception e) 
	    {
	      System.out.println(" caught a " + e.getClass() +
				 "\n with message: " + e.getMessage());
	    }
  }
}
