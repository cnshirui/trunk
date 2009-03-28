/*
 * 创建日期 2006-4-30
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package cn.edu.nju.shirui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Othello extends JFrame
{
    public Othello()
    {
        OthelloTable drawPane = new OthelloTable(getBackground());
        Container pane = getContentPane();
        pane.add(drawPane, BorderLayout.CENTER);
        setSize(600, 580);
        //f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //f.setUndecorated(true);
        //f.setAlwaysOnTop(true);
        //f.setLocationByPlatform(true);

        setLocation(208, 104);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setBackground(Color.PINK);
        setResizable(false);
        setVisible(true);        
    }
    
    public static void main(String[] args) 
    {
        new Othello();
    }
}
