package performance.document;

import java.io.*;
import java.util.StringTokenizer;

import org.apache.lucene.document.*;

public class MyDocument {
	
	/**
	 * 将文本文档转成Lucene的Document格式
	 * @param file
	 * @return document that represents a file
	 * @throws Exception
	 */
	public static Document getDocument(File file) throws IOException {
		Document doc = new Document();

		/**
		 * 为文件路径构建一个字段
		 */
		doc.add(Field.Text("path", file.getPath()));

		/**
		 * 为文件名构建一个字段
		 */
		doc.add(Field.Keyword("title", getFileName(file)));

		/**
		 * 为文件内容构建一个字段
		 */
		FileInputStream is = new FileInputStream(file);
		Reader reader = new BufferedReader(new InputStreamReader(is));
		doc.add(Field.Text("contents", reader));

		/**
		 * 为文件的最后修改时间构建一个字段
		 */
		doc.add(Field.Keyword("modified", DateField.timeToString(file
				.lastModified())));

		return doc;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private static String getFileName(File file) {
		String path = file.getPath();
		StringTokenizer st = new StringTokenizer(path, File.separator);
		String token = "";
		while (st.hasMoreTokens()) {
			token = st.nextToken();
		}

		if (token != null) {
			token = token.substring(0, token.indexOf(".txt"));
		}
		return token;
	}
}
