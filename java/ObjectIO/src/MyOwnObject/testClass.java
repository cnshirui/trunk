/*
 * 创建日期 2006-3-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package MyOwnObject;
import java.io.Serializable;

/**
 * @author mike
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class testClass implements Serializable{
	public String str="this is my  OBJECT!";
	public transient String str2="this is my OBJECT'S transient String";
	public void test1(){
		System.out.println("the method:   testClass::test1()!");
	}
	public void test2(int int0){
		System.out.println("the method:   testClass::test2("+int0+")!");
		
	}
	public String getStr(){
		System.out.println("method:   testClass::getStr()");
		return this.str;
	}
}
