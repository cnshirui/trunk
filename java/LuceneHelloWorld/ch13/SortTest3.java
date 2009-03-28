package ch13;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

class SortTest3
{
  public static void main(String[] args) 
  {
    try 
    {
      Directory directory = new RAMDirectory();  
      Analyzer analyzer = new SimpleAnalyzer();
      IndexWriter writer = new IndexWriter(directory, analyzer, true);

      String[] docs = 
      {
        "a b c d e",
        "a b c d e a b c d e",
        "a b c d e f g h i j",
        "a c e",
        "a c e a c e",
        "a c e a b c"
      };
      for (int j = 0; j < docs.length; j++)
      {
        Document d = new Document();
        d.add(Field.Text("contents", docs[j]));
        writer.addDocument(d);
      }
      writer.close();

      Searcher searcher = new IndexSearcher(directory);
      //备用检索字符串
      String[] queries = {
 	"\"a b\"",
 	// "\"a c e\"",
      };

      //保存检索结果
      Hits hits = null;
      //解析查询字符串
      QueryParser parser = new QueryParser("contents", analyzer);
   
      for (int j = 0; j < queries.length; j++) 
      {
    	  //生成查询对象
        Query query = parser.parse(queries[j]);
       //
        System.out.println("Query: " + query.toString("contents"));
        //生成排序类
        Sort sort = new Sort("contents");
        //返回检索结果
        hits = searcher.search(query,sort);
        //
        System.out.println(hits.length() + " total results");
        
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
