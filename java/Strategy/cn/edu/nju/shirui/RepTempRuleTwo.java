/*
 * 创建日期 2006-5-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

public class RepTempRuleTwo extends RepTempRule
{

    @Override
    public void replace()
    {
        // TODO 自动生成方法存根
        newString=oldString.replaceFirst("aaa", "ccc");
        System.out.println("this is replace Two");
    }

}
