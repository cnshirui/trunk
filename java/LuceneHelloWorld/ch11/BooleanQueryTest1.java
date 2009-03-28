/*
 * Created on 2006-3-11
 */
package ch11;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class BooleanQueryTest1
{
  public static void main (String [] args) throws Exception {
    Document doc1 = new Document();
    doc1.add(Field.Text("name", "word1 word2 word3"));
    doc1.add(Field.Keyword("title", "doc1"));
    
    Document doc2 = new Document();
    doc2.add(Field.Text("name", "word1 word4 word5"));
    doc2.add(Field.Keyword("title", "doc2"));
    
    Document doc3 = new Document();
    doc3.add(Field.Text("name", "word1 word2 word6"));
    doc3.add(Field.Keyword("title", "doc3"));
    
    IndexWriter writer = new IndexWriter("c:\\index", new StandardAnalyzer(), true);
    writer.addDocument(doc1);
    writer.addDocument(doc2);
    writer.addDocument(doc3);
    writer.close();
    
    Query query1 = null;
    Query query2 = null;
    BooleanQuery query = null;
    Hits hits = null;
    
    IndexSearcher searcher = new IndexSearcher("c:\\index");
    
    query1 = new TermQuery(new Term("name","word1"));
    query2 = new TermQuery(new Term("name","word2"));
    
    // 构造一个布尔查询
    query = new BooleanQuery();
    
    // 添加两个子查询
    query.add(query1, false, false);
    query.add(query2, false, false);
    
    hits = searcher.search(query);
    printResult(hits, "word1和word2");
    
  }
  
  public static void printResult(Hits hits, String key) throws Exception
  {
    System.out.println("查找 \"" + key + "\" :");
    if (hits != null)
    {
      if (hits.length() == 0)
      {
        System.out.println("没有找到任何结果");
      }
      else
      {
        System.out.println("找到" + hits.length() + "个结果");
        for (int i = 0; i < hits.length(); i++)
        {
          Document d = hits.doc(i);
          String dname = d.get("title");
          System.out.print(dname + "   ");
        }
        System.out.println();
        System.out.println();
      }
    }
  }
}
