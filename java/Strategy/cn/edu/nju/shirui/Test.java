/*
 * 创建日期 2006-5-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

public class Test
{
    public static void main(String[] args)
    {
        Test test = new Test();
        test.testReplace();
    }
    public void testReplace()
    {
      // 使用第一套替代方案
      RepTempRuleSolve solver = new RepTempRuleSolve(new RepTempRuleOne());
      solver.getNewContext();
      
      // 使用第二套
      solver=new RepTempRuleSolve(new RepTempRuleTwo());
      solver.getNewContext();
    }
}
