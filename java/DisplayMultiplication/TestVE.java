import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/*
 * 创建日期 2006-4-28
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

public class TestVE extends JFrame
{

    private JPanel jContentPane = null;
    private JButton jbtDlg = null;
    private JMenuBar jJMenuBar = null;
    private JMenu jMenu = null;
    private JMenu jMenu1 = null;
    private JMenuItem jMenuItem = null;
    private JMenuItem jMenuItem1 = null;
    public TestVE() throws HeadlessException
    {
        super();
        // TODO 自动生成构造函数存根

        initialize();
    }

    public TestVE(GraphicsConfiguration arg0)
    {
        super(arg0);
        // TODO 自动生成构造函数存根

        initialize();
    }

    public TestVE(String arg0) throws HeadlessException
    {
        super(arg0);
        // TODO 自动生成构造函数存根

        initialize();
    }

    public TestVE(String arg0, GraphicsConfiguration arg1)
    {
        super(arg0, arg1);
        // TODO 自动生成构造函数存根

        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(287, 183);
        this.setJMenuBar(getJJMenuBar());
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJbtDlg(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jbtDlg	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJbtDlg()
    {
        if (jbtDlg == null)
        {
            jbtDlg = new JButton();
            jbtDlg.setText("Show a Dialog");
            jbtDlg.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    JOptionPane.showConfirmDialog(null,"Hello OK!");
                }
            });
        }
        return jbtDlg;
    }

    /**
     * This method initializes jJMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getJJMenuBar()
    {
        if (jJMenuBar == null)
        {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenu());
            jJMenuBar.add(getJMenu1());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenu()
    {
        if (jMenu == null)
        {
            jMenu = new JMenu();
            jMenu.setText("File");
            jMenu.add(getJMenuItem());
        }
        return jMenu;
    }

    /**
     * This method initializes jMenu1	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getJMenu1()
    {
        if (jMenu1 == null)
        {
            jMenu1 = new JMenu();
            jMenu1.setText("Exit");
            jMenu1.add(getJMenuItem1());
        }
        return jMenu1;
    }

    /**
     * This method initializes jMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItem()
    {
        if (jMenuItem == null)
        {
            jMenuItem = new JMenuItem();
            jMenuItem.setText("File");
            jMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    JOptionPane.showConfirmDialog(null,"File");
                }
            });
        }
        return jMenuItem;
    }

    /**
     * This method initializes jMenuItem1	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getJMenuItem1()
    {
        if (jMenuItem1 == null)
        {
            jMenuItem1 = new JMenuItem();
            jMenuItem1.setText("Exit");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                    System.exit(0);
                }
            });
        }
        return jMenuItem1;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
