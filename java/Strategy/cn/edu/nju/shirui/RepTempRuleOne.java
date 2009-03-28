/*
 * 创建日期 2006-5-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

public class RepTempRuleOne extends RepTempRule
{

    @Override
    public void replace()
    {
        // TODO 自动生成方法存根
        // replaceFirst 是jdk1.4 新特性
        newString = oldString.replaceFirst("aaa", "bbbb");
        System.out.println("this is replace one");
    }

}
