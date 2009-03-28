/*
 * 创建日期 2006-5-23
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

/**
 * @author shirui
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
import java.awt.*;
import javax.swing.*;
//import javax.sound.*;

public class MonitorWindows extends JFrame
{
    private static final  long serialVersionUID = 1L;
    
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    
    private JTextArea reportArea;
    private JButton socketSetButton;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    
    public static void main(String[] args)
    {
        // TODO 自动生成方法存根
        MonitorWindows frame = new MonitorWindows();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
    public MonitorWindows()
    {
        setTitle("AntiHunter Monitor Center");
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        
        Container contentPane = getContentPane();
        buttonPanel = new JPanel();
        socketSetButton = new JButton("Socket Setup");
        buttonPanel.add(socketSetButton);
        contentPane.add(buttonPanel,BorderLayout.SOUTH);
        
        reportArea = new JTextArea();
        scrollPane = new JScrollPane(reportArea);
        
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }
    

}
