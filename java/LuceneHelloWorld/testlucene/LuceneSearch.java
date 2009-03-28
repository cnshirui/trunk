package testlucene;

import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class LuceneSearch {
//	public static void main(String[] args) throws Exception {
//		LuceneSearch test = new LuceneSearch();
//		// 检索“人民”这个关键字
//		Hits h = null;
//		h = test.search("中华");
//		test.printResult(h);
//		h = test.search("人民");
//		test.printResult(h);
//		h = test.search("共和国");
//		test.printResult(h);
//	}
	
	public static void main(String[] args) throws Exception {
		LuceneSearch test = new LuceneSearch();
		Hits h = null;
		h = test.search("华人");
		test.printResult(h);
		h = test.search("共和");
		test.printResult(h);
		h = test.search("人");
		test.printResult(h);
	}

	public LuceneSearch() {
		try {
			searcher = new IndexSearcher(IndexReader
					.open(Constants.INDEX_STORE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 声明一个IndexSearcher对象
	private IndexSearcher searcher = null;

	// 声明一个Query对象
	private Query query = null;

	public final Hits search(String keyword) {
		System.out.println("正在检索关键字 : " + keyword);
		try {
			// 将关键字包装成Query对象
			query = QueryParser.parse(keyword, "contents",
					new StandardAnalyzer());

			Date start = new Date();
			Hits hits = searcher.search(query);
			Date end = new Date();
			System.out.println("检索完成，用时" + (end.getTime() - start.getTime()) + "毫秒");
			return hits;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void printResult(Hits h) {
		if (h.length() == 0) {
			System.out.println("对不起，没有找到您要的结果。");
		} 
		else 
		{
			for (int i = 0; i < h.length(); i++) {
				try {
					Document doc = h.doc(i);
					System.out.print("这是第" + i + "个检索到的结果，文件名为：");
					System.out.println(doc.get("path"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("--------------------------");
	}

}
