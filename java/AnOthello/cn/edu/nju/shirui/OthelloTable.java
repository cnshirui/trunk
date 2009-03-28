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

public class OthelloTable extends JPanel 
{
    public static void main(String[] args) 
    {
        new Othello();
    }

    private Insets insets;
    final static int BLACK = -1;
    final static int WHITE = 1;
    private Point point = null;
    private int colorStatus;
    private int[][] status = null;
    private int count;
    private int chessNum;
    private JTextArea infoText = null;
    private int[][] statusW = null;
    private int level = 1 ;
    private int whiteCount = 0;
    private int blackCount = 0;
    private Color bgColor = null;
    
    public OthelloTable(Color c) 
    {

        initComponent();
        initChess();
        bgColor = c;
        addMouseListener(new MouseAdapter() 
                             {
                                public void mouseClicked(MouseEvent e) 
                                {
                                    response(e.getPoint());
                                }
                             }
                        );
        setFocusable(true);
        addKeyListener(new myKeyListener());
    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (insets == null) 
        {
            insets = getInsets();
        }
        //super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);
        g2d.fill3DRect(50, 50, 400, 400, true);
        g2d.setColor(Color.RED);
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                // each grid has 50*50 pixels
                g2d.drawString((char)('A'+i)+"", 50 * i + 70, 45);
                g2d.drawString(j + "", 40, 50 * j + 80);
                g2d.drawRect(50 + 50 * i, 50 + 50 * j, 50, 50);
                if (status[i][j] == BLACK) 
                {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(i * 50 + 53, j * 50 + 53, 44, 44);
                }
                if (status[i][j] == WHITE) 
                {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(i * 50 + 53, j * 50 + 53, 44, 44);
                }
                if (status[i][j] > 1) 
                {
                    // show evaluation function
                    g2d.setColor(Color.BLUE);
                    g2d.drawString(""+(status[i][j]-1), i*50+65, j*50+65);
                    
                    // which can be put
                    g2d.setColor(Color.RED);
                    g2d.drawString("X", i * 50 + 75, j * 50 + 75);
                }

                // besides which shows how many are each?
                g2d.setColor(Color.BLACK);
                g2d.fillOval(200, 470, 30, 30); 
                g2d.setColor(Color.WHITE);
                g2d.fillOval(300, 470, 30, 30);

/*               g2d.setColor(bgColor);
                g2d.fillRect(240, 470, 60, 30);
                g2d.fillRect(340, 470, 60, 30);
 */               
                g2d.setColor(Color.RED);
                g2d.drawString("" + blackCount, 240, 490);
                g2d.drawString("" + whiteCount, 340, 490);

            }
        }
        //jug(colorStatus);
    }

    public void initComponent() 
    {        
        infoText = new JTextArea();        
        setLayout(new BorderLayout());
        infoText.setEditable(false);
        //infoText.setBackground(Color.PINK);
        infoText.setForeground(Color.BLACK);
        JScrollPane jsp = new JScrollPane(infoText);
        JPanel infoPanel = new JPanel(new BorderLayout());
        //infoPanel.setBackground(Color.PINK);
        infoPanel.add(jsp, BorderLayout.CENTER);

        infoPanel.add(new JLabel("  "), BorderLayout.NORTH);
        infoPanel.add(new JLabel("  "), BorderLayout.SOUTH);

        add(infoPanel, BorderLayout.EAST);

    }

    public void initChess() 
    {
        chessNum = 4;
        count = 0;
        colorStatus = BLACK;        // black first
        status = new int[8][8];
        status[3][3] = BLACK;
        status[4][4] = BLACK;
        status[3][4] = WHITE;
        status[4][3] = WHITE;
        statusW = new int[8][8];   // status weight
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                statusW[i][j] = 2;          
            }
        }

        for (int i = 0; i < 4; i++) 
        {
            statusW[i + 2][0] = 3;
            statusW[i + 2][1] = 1;
            statusW[i + 2][7] = 3;
            statusW[i + 2][6] = 1;
            statusW[0][i + 2] = 3;
            statusW[1][i + 2] = 1;
            statusW[7][i + 2] = 3;
            statusW[6][i + 2] = 1;
        }
        for (int i = 0; i < 2; i++) 
        {
            for (int j = 0; j < 2; j++) 
            {
                statusW[i][j] = 0;
                statusW[7 - i][j] = 0;
                statusW[i][7 - j] = 0;
                statusW[7 - i][7 - j] = 0;
            }
        }
        statusW[0][0] = 4;
        statusW[0][7] = 4;
        statusW[7][0] = 4;
        statusW[7][7] = 4;
        
/*         设置棋盘权重如下：
            4 0 3 3 3 3 0 4 
            0 0 1 1 1 1 0 0 
            3 1 2 2 2 2 1 3 
            3 1 2 2 2 2 1 3 
            3 1 2 2 2 2 1 3 
            3 1 2 2 2 2 1 3 
            0 0 1 1 1 1 0 0 
            4 0 3 3 3 3 0 4 */
         

        jug(colorStatus);
        String msg="F1:   级别1\nF2:   级别2\nF3:   重新开始";
        infoText.setText(msg+"\t\n");
        repaint();
    }

    public void response(Point p) 
    {
        boolean interrupt = false;
        point = setPoint(p);
        if (point != null) {
            int x = point.x / 50 - 1;           // 所在表格的坐标(x,y)
            int y = point.y / 50 - 1;

            if (check(x, y, colorStatus) > 0) 
            {
                manualChess(x, y, BLACK);
                if (level == 1) 
                {
                    autoChess(WHITE);
                } 
                else 
                {
                    autoChess(WHITE, 2);
                }
                if (blPass()) 
                {
                    if (!blPass()) 
                    {
                        repaint();
                    } 
                    else 
                    {
                        interrupt = true;
                    }
                }
                if (blOver(interrupt)) 
                {
                    initChess();
                }
            }

        }

    }

    public Point setPoint(Point p) 
    {
        if (p.x < 50 || p.x > 450 || p.y < 50 || p.y > 450) 
        {
            return null;
        } 
        else 
        {
            p.x = (p.x / 50) * 50;
            p.y = (p.y / 50) * 50;
            return p;
        }
    }

    public boolean change(int i, int j, int sta) 
    {
        // 估计是把sta填入(x,y)后所发生的变化
        boolean flag = false;

        //north
        if (j != 0) 
        {
            if (sta != status[i][j - 1] && 1 == Math.abs(status[i][j - 1])) 
            {
                int m = j - 1;
                while (status[i][j - 1] == status[i][m] && m > 0) 
                {
                    m--;
                    // System.out.println(m);
                }
                if (status[i][m] == sta) 
                {
                    flag = true;
                    for (int n = j - 1; n > m; n--) 
                    {
                        status[i][n] = sta;
                        // System.out.println("[" + i + "," + n + "]" + status[i][n]);
                    }
                }
            }
        }

        //south
        if (j != 7) 
        {
            if (sta != status[i][j + 1] && 1 == Math.abs(status[i][j + 1])) 
            {
                int m = j + 1;
                while (status[i][j + 1] == status[i][m] && m < 7) 
                {
                    m++;
                    // System.out.println(m);
                }
                if (status[i][m] == sta) 
                {
                    flag = true;
                    for (int n = j + 1; n < m; n++) 
                    {
                        status[i][n] = sta;
                        // System.out.println("[" + i + "," + n + "]" + status[i][n]);
                    }
                }

            }
        }

        //west
        if (i != 0) 
        {
            if (sta != status[i - 1][j] && 1 == Math.abs(status[i - 1][j])) 
            {
                int m = i - 1;
                while (status[i - 1][j] == status[m][j] && m > 0) 
                {
                    m--;
                    //                    System.out.println(m);
                }
                if (status[m][j] == sta) 
                {
                    flag = true;
                    for (int n = i - 1; n > m; n--) 
                    {
                        status[n][j] = sta;
                        //                        System.out.println("[" + n + "," + j + "]"
                        //                                + status[n][j]);
                    }
                }
            }
        }

        //east
        if (i != 7) 
        {
            if (sta != status[i + 1][j] && 1 == Math.abs(status[i + 1][j])) 
            {
                int m = i + 1;
                while (status[i + 1][j] == status[m][j] && m < 7) 
                {
                    m++;
                    //                    System.out.println(m);
                }
                if (status[m][j] == sta) 
                {
                    flag = true;
                    for (int n = i + 1; n < m; n++) 
                    {
                        status[n][j] = sta;
                        //                        System.out.println("[" + n + "," + j + "]"
                        //                                + status[n][j]);
                    }
                }

            }
        }

        //northwest
        if (j != 0 && i != 0) 
        {
            if (sta != status[i - 1][j - 1]
                    && 1 == Math.abs(status[i - 1][j - 1])) 
            {
                int m = i - 1;
                int n = j - 1;
                while (status[i - 1][j - 1] == status[m][n] && m > 0 && n > 0) {
                    m--;
                    n--;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) {
                    flag = true;
                    for (int x = i - 1, y = j - 1; x > m; x--, y--) {
                        //  for (int y = j - 1; y > n; y--) {
                        status[x][y] = sta;
                        //                        System.out.println("[" + x + "," + y + "]"
                        //                                + status[x][y]);
                        // }

                    }
                }
            }
        }

        //southeast
        if (j != 7 && i != 7) 
        {
            if (sta != status[i + 1][j + 1]
                    && 1 == Math.abs(status[i + 1][j + 1])) 
            {
                int m = i + 1;
                int n = j + 1;
                while (status[i + 1][j + 1] == status[m][n] && m < 7 && n < 7) 
                {
                    m++;
                    n++;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) {
                    flag = true;
                    for (int x = i + 1, y = j + 1; x < m; x++, y++) 
                    {
                        //  for (int y = j + 1; y < n; y++) {
                        status[x][y] = sta;
                        //                        System.out.println("[" + x + "," + y + "]"
                        //                                + status[x][y]);
                        // }

                    }
                }
            }
        }

        //northeast
        if (j != 0 && i != 7) 
        {
            if (sta != status[i + 1][j - 1]
                    && 1 == Math.abs(status[i + 1][j - 1])) 
            {
                int m = i + 1;
                int n = j - 1;
                while (status[i + 1][j - 1] == status[m][n] && m < 7 && n > 0) 
                {
                    m++;
                    n--;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = true;
                    for (int x = i + 1, y = j - 1; x < m; x++, y--) 
                    {
                        //  for (int y = j - 1; y > n; y--) {
                        status[x][y] = sta;
                        //                        System.out.println("[" + x + "," + y + "]"
                        //                                + status[x][y]);
                        // }

                    }
                }
            }
        }

        //southwest
        if (j != 7 && i != 0) 
        {
            if (sta != status[i - 1][j + 1]
                    && 1 == Math.abs(status[i - 1][j + 1])) 
            {
                int m = i - 1;
                int n = j + 1;
                while (status[i - 1][j + 1] == status[m][n] && m > 0 && n < 7) 
                {
                    m--;
                    n++;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = true;

                    for (int x = i - 1, y = j + 1; x > m; x--, y++) 
                    {
                        // for (int y = j + 1; y < n; y++) {

                        status[x][y] = sta;
                        //                        System.out.println("[" + x + "," + y + "]"
                        //                                + status[x][y]);
                        //}

                    }
                }
            }
        }
        return flag;

    }

    public int check(int i, int j, int sta)    // 检测填入该子后能吸收多少个对方的棋子
    {
        int flag = 0;
        if (Math.abs(status[i][j]) == 1)         // 若(i,j)已填入棋子，则返回0
        {
            return 0;
        }

        //north
        if (j != 0) 
        {
            if (sta != status[i][j - 1] && 1 == Math.abs(status[i][j - 1])) 
            {
                int m = j - 1;
                while (status[i][j - 1] == status[i][m] && m > 0) 
                {
                    m--;
                    // System.out.println(m);
                }
                if (status[i][m] == sta) 
                {
                    flag = flag + (j - 1 - m);
                }
            }
        }

        //south
        if (j != 7) {
            if (sta != status[i][j + 1] && 1 == Math.abs(status[i][j + 1])) 
            {
                int m = j + 1;
                while (status[i][j + 1] == status[i][m] && m < 7) 
                {
                    m++;
                    // System.out.println(i+" " + j + " check:" + m);
                }
                if (status[i][m] == sta) 
                {
                    flag = flag + (m - j - 1);
                }

            }
        }

        //west
        if (i != 0) {
            if (sta != status[i - 1][j] && 1 == Math.abs(status[i - 1][j])) 
            {
                int m = i - 1;
                while (status[i - 1][j] == status[m][j] && m > 0) 
                {
                    m--;
                    //                    System.out.println(m);
                }
                if (status[m][j] == sta) 
                {
                    flag = flag + (i - 1 - m);

                }
            }
        }

        //east
        if (i != 7) 
        {
            if (sta != status[i + 1][j] && 1 == Math.abs(status[i + 1][j])) 
            {
                int m = i + 1;
                while (status[i + 1][j] == status[m][j] && m < 7) 
                {
                    m++;
                    // System.out.println(m);
                }
                if (status[m][j] == sta) 
                {
                    flag = flag + (m - i - 1);

                }

            }
        }

        //northwest
        if (j != 0 && i != 0) 
        {
            if (sta != status[i - 1][j - 1]
                    && 1 == Math.abs(status[i - 1][j - 1])) 
            {
                int m = i - 1;
                int n = j - 1;
                while (status[i - 1][j - 1] == status[m][n] && m > 0 && n > 0) 
                {
                    m--;
                    n--;
                    // System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = flag + (i - 1 - m);

                }
            }
        }

        //southeast
        if (j != 7 && i != 7) 
        {
            if (sta != status[i + 1][j + 1]
                    && 1 == Math.abs(status[i + 1][j + 1])) 
            {
                int m = i + 1;
                int n = j + 1;
                while (status[i + 1][j + 1] == status[m][n] && m < 7 && n < 7) 
                {
                    m++;
                    n++;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = flag + (m - i - 1);

                }
            }
        }

        //northeast
        if (j != 0 && i != 7) 
        {
            if (sta != status[i + 1][j - 1]
                    && 1 == Math.abs(status[i + 1][j - 1])) 
            {
                int m = i + 1;
                int n = j - 1;
                while (status[i + 1][j - 1] == status[m][n] && m < 7 && n > 0) 
                {
                    m++;
                    n--;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = flag + (j - 1 - n);

                }
            }
        }

        //southwest
        if (j != 7 && i != 0) 
        {
            if (sta != status[i - 1][j + 1]
                    && 1 == Math.abs(status[i - 1][j + 1])) 
            {
                int m = i - 1;
                int n = j + 1;
                while (status[i - 1][j + 1] == status[m][n] && m > 0 && n < 7) 
                {
                    m--;
                    n++;
                    //                    System.out.println("m=" + m + " n=" + n);
                }
                if (status[m][n] == sta) 
                {
                    flag = flag + (i - 1 - m);

                }
            }
        }
        return flag;

    }

    public void release() 
    {
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                if (status[i][j] > 1) 
                {
                    status[i][j] = 0;
                }
            }
        }
    }

    public boolean jug(int color) 
    {
        //Hashtable table=new Hashtable();
        int flag = 0;
        int isPass = 0;
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                if (status[i][j] == 0) 
                {
                    flag = check(i, j, color);
                    {
                        if (flag != 0) 
                        {
                            status[i][j] = flag + 1;
                            isPass++;
                        }
                    }

                } 
                else 
                {
                    flag = 0;
                }
            }
        }
        if (isPass > 0) 
        {
            return true;
        } 
        else 
        {
            return false;
        }

    }

    public boolean blOver(boolean interrupt) 
    {
        String msg = "";
        whiteCount = 0;
        blackCount = 0;
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                if (status[i][j] == BLACK) 
                {
                    blackCount++;
                }
                if (status[i][j] == WHITE) 
                {
                    whiteCount++;
                }
            }
        }
        if ((whiteCount + blackCount) == 64 || blackCount == 0 || whiteCount == 0 || interrupt) 
        {

            if (whiteCount > blackCount) 
            {
                msg = "白方胜！白方:黑方=" + whiteCount + ":" + blackCount;
            } 
            else 
            {
                msg = "黑方胜！黑方:白方" + blackCount + ":" + whiteCount;
            }
            JOptionPane.showConfirmDialog(this, msg, "Game Over", 
                    JOptionPane.CLOSED_OPTION);
            return true;
        } 
        else 
        {
            return false;
        }

    }

    public boolean blPass() 
    {
        if (chessNum >= 64) 
        {
            return false;
        }
        if (!jug(colorStatus)) 
        {
            JOptionPane.showMessageDialog(null, "pass");
            colorStatus = -colorStatus;
            count++;
            infoText.append(count + ":     --\n");
            return true;
        } 
        else 
        {
            return false;
        }
    }

    public void manualChess(int x, int y, int color) 
    {
        // it's turn for people to put chess
        if (colorStatus == color) 
        {
            release();
            if (status[x][y] == 0) 
            {
                status[x][y] = colorStatus;
            }
            change(x, y, colorStatus);
            repaint();
            count++;
            chessNum++;
            String value = "" + (char)('A'+x) + "*" + y;
            infoText.append(count + ":     " + value + "\n");
            colorStatus = -colorStatus;
        }
    }

    public void autoChess(int color) 
    {
        if (colorStatus == color) 
        {
            int flag = 0;
            int x = 0;
            int y = 0;
            for (int i = 0; i < 8; i++) 
            {
                for (int j = 7; j >= 0; j--) 
                {
                    int temp = check(i, j, colorStatus);
                    if (temp > flag) 
                    {
                        flag = temp;
                        x = i;
                        y = j;
                    }
                }
            }
            if (flag > 0) 
            {
                if (status[x][y] == 0) 
                {
                    status[x][y] = color;

                }
                change(x, y, colorStatus);
                repaint();
                colorStatus = -colorStatus;
                count++;
                chessNum++;
                String value = "" + x + "*" + y;
                infoText.append(count + ":     " + value + "\n");
            } 
            else 
            {
                colorStatus = -colorStatus;
            }
        }
    }

    public void autoChess(int color, int level) 
    {
        if (colorStatus == color && level == 2) 
        {
            int flag = -1;
            int x = -1;
            int y = -1;
            for (int i = 0; i < 8; i++) 
            {
                for (int j = 7; j >= 0; j--) 
                {
                    if (status[i][j] == 0 && statusW[i][j] > flag) 
                    {
                        if (check(i, j, colorStatus) > 0) 
                        {
                            flag = statusW[i][j];
                            x = i;
                            y = j;
                        }
                    }

                }
            }
            if (flag >= 0 && x > -1 && y > -1) 
            {
                if (status[x][y] == 0) 
                {
                    status[x][y] = color;

                }
                change(x, y, colorStatus);
                repaint();
                colorStatus = -colorStatus;
                count++;
                chessNum++;
                String value = "" + x + "*" + y;
                infoText.append(count + ":     " + value + "\n");
            } 
            else 
            {
                colorStatus = -colorStatus;
            }
        }
    }

    class myKeyListener extends KeyAdapter 
    {
        public void keyPressed(java.awt.event.KeyEvent e) 
        {
            System.out.println(e.getKeyChar());
            if (e.getKeyCode() == KeyEvent.VK_F5) 
            {
                initChess();

            }
            if (e.getKeyCode() == KeyEvent.VK_F1) 
            {
                level = 1;
                initChess();
            }
            if (e.getKeyCode() == KeyEvent.VK_F2) 
            {
                level = 2;
                initChess();
            }
        }
    }
}
