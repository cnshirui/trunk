package performance.index;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.*;
//import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
//import org.apache.lucene.queryParser.*;
import org.apache.lucene.store.*;

import performance.document.MyDocument;

public class IndexPerformanceTest {
	
	/**
	 * 索引存放的位置
	 */
	public static final String INDEX_STORE_PATH = "c:\\index";
	
	/**
	 * 合并索引前，内存中最大的文件数量
	 */
	public static final int DEFAULT_MAX_DOCS_IN_RAM = 64;
	
	/**
	 * 最大字段长度
	 */
	public static final int DEFAULT_MAX_FIELD_LENGTH = Integer.MAX_VALUE;
	
	/***
	 * 单例模式
	 */
	private static IndexPerformanceTest indexer = null;
	
	// 两个索引器
	private IndexWriter ramWriter = null;
	private IndexWriter fsWriter = null;	

	// 内存中已经有的文档数量
	private int docs_in_ram;
	// 内存中最大的文档数量
	private int ramMaxFiles = DEFAULT_MAX_DOCS_IN_RAM;
	
	// 内存是否已经被刷新过
	private boolean freshed = true;
	
	// 索引存放的文件系统目录
	FSDirectory fsDir = null;
	// 索引存放的内存目录
	RAMDirectory ramDir = null;
	
	// 私有的构造函数
	private IndexPerformanceTest() throws IOException{
		
	}
	
	/**
	 * 静态方法构造一个单例 
	 */
	public static IndexPerformanceTest getInstance() throws IOException{
		if (indexer == null){
			indexer = new IndexPerformanceTest();
		}
		return indexer;
	}
	
	/**
	 *  构造索引器
	 */
	public void newWriter(){
		
		if (fsWriter != null || ramWriter != null) {
			System.out.println("close the indexer first then to create a new one");
			return;
		}
		
		try{
			boolean rebuild = true;
			
			fsDir = FSDirectory.getDirectory(INDEX_STORE_PATH,rebuild);
			ramDir = new RAMDirectory();
			
			Analyzer analyzer = new StandardAnalyzer();
			
			ramWriter = new IndexWriter(ramDir,analyzer,true);
			fsWriter = new IndexWriter(fsDir,analyzer,rebuild);
			initWriter();
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化索引器
	 * @throws IOException
	 */
	private void initWriter() throws IOException{
		fsWriter.maxFieldLength = DEFAULT_MAX_FIELD_LENGTH;
	}
	
	/**
	 *  优化索引
	 */
	public void optimizeIndex() throws IOException{
		if (fsWriter == null || ramWriter == null) {
			System.out.println("use newWriter() method to initialize a new indexer first");
			return;
		}
		
		refreshRam();
		fsWriter.optimize();
	}
	

	/***
	 * 索引某个目录下的文件
	 * @param path
	 */
	public void toIndex(String path) throws IOException{
		toIndex(new File(path));
	}
	
	/***
	 * 索引某个File对象
	 * @param file
	 */
	public void toIndex(File file) throws IOException{
		if (fsWriter == null || ramWriter == null) {
			System.out.println("use newWriter() method to initialize a new indexer first");
			return;
		}
		freshed = false;
		Date start = new Date();
		int number = indexFiles(file);
		Date end = new Date();
		System.out.println("总共耗时" + (end.getTime()-start.getTime()) + "毫秒" );
		System.out.println("一共为" + number + "个文件建立索引");
	}
	
	/***
	 * 关闭索引
	 *
	 */
	public void close() throws IOException{
		if (fsWriter == null || ramWriter == null) {
			System.out.println("use newWriter() method to initialize a new indexer first");
			return;
		}
//		refreshRam();
		ramWriter.close();
		fsWriter.close();
	}
	
	/***
	 * 刷新内存
	 *
	 */
	private void refreshRam() throws IOException{
		if ( !freshed ){
			
			System.out.println("Refreshing..");
			
			fsWriter.addIndexes(new Directory[] {ramDir});

			ramDir.close();
			ramDir = new RAMDirectory();
			ramWriter = new IndexWriter(ramDir,new StandardAnalyzer(),true);
			
			docs_in_ram = 0;
			freshed = true;
		}
	}
	
	/***
	 * 向索引中加入文档
	 * @throws IOException
	 */
	private void addDocument(File file) throws IOException{
		
		if (docs_in_ram >= ramMaxFiles){
			refreshRam();
		}
		
		ramWriter.addDocument(MyDocument.getDocument(file));
		docs_in_ram++;
		freshed = false;
	}
	
	/***
	 * 递归遍历文件目录来建立索引
	 * 
	 * @throws IOException
	 */
	private int indexFiles(File file) throws IOException{
		if (file.isDirectory()){
			File[] files = file.listFiles();
			int num = 0;
			for (int i=0;i<files.length;i++) {
				num += indexFiles(files[i]);
			}
			return num;
		} else {
			if (file.getPath().endsWith(".txt"))
//			if (true)
			{
				System.out.println("正在建索：" + file);
				addDocument(file);
				return 1;
			}
			else
			{
				System.out.println("文件类型不支持" + file);
				return 0;
			}
		}
	}
	
	public static void main (String [] args) throws IOException {
		IndexPerformanceTest indexer = IndexPerformanceTest.getInstance();
		indexer.newWriter();
		indexer.toIndex("c:\\myweb");
		indexer.close();
	}
}
