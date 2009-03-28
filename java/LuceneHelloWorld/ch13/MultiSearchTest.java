package ch13;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
public class MultiSearchTest  
{
	public static void main(String[] args) throws IOException, ParseException 
	{
		Directory indexStoreA = new RAMDirectory();
        Directory indexStoreB = new RAMDirectory();

        // 创建一个文档
        Document lDoc = new Document();
        lDoc.add(Field.Text("fulltext", "Once upon a time....."));
        lDoc.add(Field.Keyword("id", "doc1"));
        lDoc.add(Field.Keyword("handle", "1"));

        // 创建一个存储文档
        Document lDoc2 = new Document();
        lDoc2.add(Field.Text("fulltext", "in a galaxy far far away....."));
        lDoc2.add(Field.Keyword("id", "doc2"));
        lDoc2.add(Field.Keyword("handle", "1"));

        //创建一个存储文档
        Document lDoc3 = new Document();
        lDoc3.add(Field.Text("fulltext", "a bizarre bug manifested itself...."));
        lDoc3.add(Field.Keyword("id", "doc3"));
        lDoc3.add(Field.Keyword("handle", "1"));

        // 为第一个索引创建一个索引书写器
        IndexWriter writerA = new IndexWriter(indexStoreA, new StandardAnalyzer(), true);
        //为第二个索引创建一个索引书写器，但是什么都不写
        IndexWriter writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), true);
        
        /***********************************/
        // 第一段
	    /***********************************/
        //将文档添加到第一个索引中
        writerA.addDocument(lDoc);
        writerA.addDocument(lDoc2);
        writerA.addDocument(lDoc3);
        writerA.optimize();
        writerA.close();

        // 关闭第二个索引
        writerB.close();

        // 创建一个查询对象
        Query query = QueryParser.parse("handle:1", "fulltext", new StandardAnalyzer());

        // 建立检索对象
        Searcher[] searchers = new Searcher[2];

        searchers[0] = new IndexSearcher(indexStoreB);
        searchers[1] = new IndexSearcher(indexStoreA);
        // 创建一个多索引检索器
        Searcher mSearcher = new MultiSearcher(searchers);
        // 返回检索结果
        Hits hits = mSearcher.search(query);

        System.out.println("There is "+hits.length()+" Document(s) matched!");
        try 
        {
            //遍历检索结果
            for (int i = 0; i < hits.length(); i++) 
            {
                Document d = hits.doc(i);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("ArrayIndexOutOfBoundsException thrown: " + e.getMessage());
            e.printStackTrace();
        } finally
        {
            mSearcher.close();
        }
        
        /***********************************/
                // 第二段
	    /***********************************/
        //将文档添加到一个空索引中
        writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), false);
        writerB.addDocument(lDoc);
        writerB.optimize();
        writerB.close();

        // 建立检索对象
        Searcher[] searchers2 = new Searcher[2];
		//
        searchers2[0] = new IndexSearcher(indexStoreB);
        searchers2[1] = new IndexSearcher(indexStoreA);
        // 创建一个多索引检索器
        Searcher mSearcher2 = new MultiSearcher(searchers);
        // 返回检索结果
        Hits hits2 = mSearcher2.search(query);

        System.out.println("There is "+hits2.length()+" Document(s) matched!");

        try {
            // 遍历检索结果
            for (int i = 0; i < hits2.length(); i++) 			
{
                Document d = hits2.doc(i);
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception thrown: " + e.getMessage());
            e.printStackTrace();
        } finally
        {
            //mSearcher2.close();
        }
        
        /***********************************/
        // 第三段
        /***********************************/

        //
        Term term = new Term("id", "doc1");
        IndexReader readerB = IndexReader.open(indexStoreB);
        readerB.delete(term);
        readerB.close();

        // 生成索引书写器
        writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), false);
		//优化索引
        writerB.optimize();
		//关闭索引
        writerB.close();

        // 建立检索器
        Searcher[] searchers3 = new Searcher[2];

        searchers3[0] = new IndexSearcher(indexStoreB);
        searchers3[1] = new IndexSearcher(indexStoreA);
        // 创建一个多索引检索器
        Searcher mSearcher3 = new MultiSearcher(searchers);
        // 返回检索结果
        Hits hits3 = mSearcher3.search(query);

        System.out.println("There is "+hits3.length()+" Document(s) matched!");
        try {
            // 遍历检索结果
            for (int i = 0; i < hits3.length(); i++)
            {
                Document d = hits3.doc(i);
            }
        }
        catch (IOException e)
        {
        	 System.out.println("IOException thrown: " + e.getMessage());
            e.printStackTrace();
        } finally
        {
            //mSearcher3.close();
        }
	}
}
