/*
 * 创建日期 2006-2-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package testMySQL;

import java.sql.*;

/**
 * @author shirui
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class TestMySQL 
{
    public static void main(String args[])
    {
        System.out.println("Hello Java!");
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Success loading MySQL Driver...");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mycap","root","nju");
            System.out.println("Success entering MySQL...");
            Statement st = con.createStatement();
            System.out.println( "创建Statement成功!" );

            ResultSet rs = st.executeQuery("select * from customers;");
            System.out.println( "操作数据表成功!" );
            System.out.println( "--------------------------------------" );
            
//          System.out.println(rs.getS)

            while(rs.next())
            {
                System.out.print(rs.getString("cid") + "    ");
                System.out.print(rs.getString("cname") + "    ");
                System.out.print(rs.getString("city") + "    ");
                System.out.println(rs.getString("discnt") + "    ");
            }
            rs.close();
            st.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("Error MySQL Driver...");
            e.printStackTrace();
        }
     
    }

}
