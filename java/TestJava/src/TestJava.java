import java.io.*;
import java.util.Random;
import java.util.UUID;


public class TestJava {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("shirui");
		String temp_folder = UUID.randomUUID().toString();
		try {
//			File file = File.createTempFile("/xlf_extract", ".tmp");
			File file = new File("extract_temp" + File.separator + temp_folder);
			if(!file.mkdirs())
				System.out.println("create folder error...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}
	
	

}
