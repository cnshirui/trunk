package ch13;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

class SortTest4
{
  public static void main(String[] args) 
  {
    try 
    {
      Directory directory = new RAMDirectory();  
      Analyzer analyzer = new SimpleAnalyzer();
      IndexWriter writer = new IndexWriter(directory, analyzer, true);
	//需要建立索引的文档内容
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
	//依次将文档添加进索引中
      for (int j = 0; j < docs.length; j++)
      {
		//生成一个文档对象
        Document d = new Document();
		//添加内容
        d.add(Field.Text("contents", docs[j]));
		//将文档添加进索引
        writer.addDocument(d);
      }
		//关闭索引
      writer.close();
		//生成一个搜索器
      Searcher searcher = new IndexSearcher(directory);
      	//备用的检索字符串
      String[] queries = {
 	"\"a b\"",
      };
      Hits hits = null;

      QueryParser parser = new QueryParser("contents", analyzer);
   
	//依次检索
      for (int j = 0; j < queries.length; j++) 
      {
		//生成Query对象
        Query query = parser.parse(queries[j]);
        
        System.out.println("Query: " + query.toString("contents"));
        //生成排序对象
        Sort sort = new Sort(new SortField[]{new SortField("title"),new SortField("contents")});
        //返回检索结果
        hits = searcher.search(query,sort);
        
        System.out.println(hits.length() + " total results");
        //遍历检索结果
        for (int i = 0 ; i < hits.length() && i < 10; i++)
        {
		//取得文档
          Document d = hits.doc(i);
          System.out.println(i + " " + hits.score(i)+ " " + d.get("contents"));
        }
      }
		//关闭搜索器
      searcher.close();
      
    } catch (Exception e) 
	    {
	      System.out.println(" caught a " + e.getClass() +
				 "\n with message: " + e.getMessage());
	    }
  }
}
