/*
 * 创建日期 2006-5-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

public class RepTempRuleSolve
{
    private RepTempRule strategy;
    public RepTempRuleSolve(RepTempRule rule)
    {
        this.strategy=rule;
    }
    public void getNewContext() 
    {
       System.out.println("in getNewContext()"); 
        strategy.replace();
    }
    public void changeAlgorithm(RepTempRule newAlgorithm) 
    {
        strategy = newAlgorithm;
    }
}
