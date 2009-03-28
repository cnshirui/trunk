package ch15;

import com.xjt.nlp.word.*;

public class ICTCLASTest {
	public static void main (String [] args) {
		ICTCLAS instance = ICTCLAS.getInstance();
		String sentence = "乔丹是一位伟大的篮球运动员";
		System.out.println(instance.paragraphProcess(sentence));
	}
}
