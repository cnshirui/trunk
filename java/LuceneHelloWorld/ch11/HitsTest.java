package ch11;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

public class HitsTest
{

  public static void main(String[] args) throws Exception
  {
      // 构建索引
      buildIndex();

      // 使用已经存在索引目录
      Searcher searcher = new IndexSearcher("c:\\index");
      // 使用标准分析器
      Analyzer aStandardAnalyzer = new StandardAnalyzer();
      // 从标准输入读取查询的字符串
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      while (true)
      {
        System.out.println("-------------------------------------------------");
        System.out.print("Query: ");
        String line = in.readLine();
        // 判断是否直接输入的回车
        if (line.length() == 0)
          break;
        // 构造Query对象
        Query query = QueryParser.parse(line, "contents", aStandardAnalyzer);
        // 输出要搜索的内容
        System.out.println("查找 :    " + query.toString("contents"));
        // 使用searcher对象的search方法进行搜索，返回的是一个Hits类型的对象
        Hits hits = searcher.search(query);
        // 使用Hits对象的length（）方法，输出搜索到的文档的数量
        System.out.println("总共找到 " + hits.length() + " 个文档");
        // 定义每次显示的搜索结果数目
        final int HITS_PER_PAGE = 10;
        // 循环输出
        for (int start = 0; start < hits.length(); start += HITS_PER_PAGE)
        {
          //
          int end = Math.min(hits.length(), start + HITS_PER_PAGE);
          for (int i = start; i < end; i++)
          {
            // 取得搜索结果中的一个文档对象
            Document doc = hits.doc(i);
            // 输出文档的ID编号
            System.out.println("文档的内部ID号:" + hits.id(i));
            // 输出文档的评分
            System.out.println("文档的分值:" + hits.score(i));
            // 输出文档的存放路径
            String path = doc.get("path");
            if (path != null)
            {
              System.out.println("路径为："+path);
            }
          }
          // 判断是否还有结果未输出
          if (hits.length() > end)
          {
            System.out.print("more (y/n) ? ");
            line = in.readLine();
            if (line.length() == 0 || line.charAt(0) == 'n')
              break;
          }
        }
      }
      //
      searcher.close();
  }
  
  public static void buildIndex() throws Exception {
    Document doc1 = new Document();
    doc1.add(Field.Text("contents", "word1 word"));
    doc1.add(Field.Keyword("path", "path\\document1.txt"));
    doc1.setBoost(1.0f);
    
    Document doc2 = new Document();
    doc2.add(Field.Text("contents", "word2 word"));
    doc2.add(Field.Keyword("path", "path\\document2.txt"));
    doc2.setBoost(0.1f);
    
    Document doc3 = new Document();
    doc3.add(Field.Text("contents", "word3 word"));
    doc3.add(Field.Keyword("path", "path\\document3.txt"));
    doc3.setBoost(0.5f);
    
    Document doc4 = new Document();
    doc4.add(Field.Text("contents", "word4 word"));
    doc4.add(Field.Keyword("path", "path\\document4.txt"));
    doc4.setBoost(0.2f);
    
    Document doc5 = new Document();
    doc5.add(Field.Text("contents", "word5 word"));
    doc5.add(Field.Keyword("path", "path\\document5.txt"));
    doc5.setBoost(0.8f);
    
    Document doc6 = new Document();
    doc6.add(Field.Text("contents", "word6 word"));
    doc6.add(Field.Keyword("path", "path\\document6.txt"));
    doc6.setBoost(0.1f);
    
    Document doc7 = new Document();
    doc7.add(Field.Text("contents", "word7 word"));
    doc7.add(Field.Keyword("path", "path\\document7.txt"));
    doc7.setBoost(0.5f);
    
    Document doc8 = new Document();
    doc8.add(Field.Text("contents", "word8 word"));
    doc8.add(Field.Keyword("path", "path\\document8.txt"));
    doc8.setBoost(0.7f);
    
    Document doc9 = new Document();
    doc9.add(Field.Text("contents", "word9 word"));
    doc9.add(Field.Keyword("path", "path\\document9.txt"));
    doc9.setBoost(0.2f);
    
    Document doc10 = new Document();
    doc10.add(Field.Text("contents", "word10 word"));
    doc10.add(Field.Keyword("path", "path\\document10.txt"));
    doc10.setBoost(0.4f);
    
    Document doc11 = new Document();
    doc11.add(Field.Text("contents", "word11 word"));
    doc11.add(Field.Keyword("path", "path\\document11.txt"));
    
    Document doc12 = new Document();
    doc12.add(Field.Text("contents", "word12 word"));
    doc12.add(Field.Keyword("path", "path\\document12.txt"));
    
    IndexWriter writer = new IndexWriter("c:\\index", new StandardAnalyzer(), true);
    
    writer.addDocument(doc1);
    writer.addDocument(doc2);
    writer.addDocument(doc3);
    writer.addDocument(doc4);
    writer.addDocument(doc5);
    writer.addDocument(doc6);
    writer.addDocument(doc7);
    writer.addDocument(doc8);
    writer.addDocument(doc9);
    writer.addDocument(doc10);
    writer.addDocument(doc11);
    writer.addDocument(doc12);
    
    writer.close();
  }
}
