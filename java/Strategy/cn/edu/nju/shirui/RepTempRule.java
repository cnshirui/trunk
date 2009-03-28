/*
 * 创建日期 2006-5-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

public abstract class RepTempRule
{
    protected String oldString = "";
    public void setOldString(String oldString)
    {
        this.oldString=oldString;
    }
    protected String newString="";

    public String getNewString()
    {
        return newString;
    }
    public abstract void replace();
}
