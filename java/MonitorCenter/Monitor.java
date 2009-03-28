import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;

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
public class Monitor extends JFrame
{

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO 自动生成方法存根

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Monitor thisClass = new Monitor();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    /**
     * This is the default constructor
     */
    public Monitor()
    {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(606, 395);
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
        }
        return jContentPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
