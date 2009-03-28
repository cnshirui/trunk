package elegate.cn.edu.nju;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class EncryptedFilter extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		EncryptedFilter f=new EncryptedFilter();
		f.setVisible(true);
	}
	
	private JTextField txtSrc;
	private JTextField txtDst;
	private JFileChooser fileChooser;
	
	private int seed=(int)(Math.random()*10+1);
	
	public EncryptedFilter()
	{
		super("Simple Encryption Stream");
		Container contentPane=this.getContentPane();
		txtSrc=new JTextField(10);
		txtDst=new JTextField(10);
		fileChooser=new JFileChooser();
		
		JButton btnSrcBrowse=new JButton("Browse");
		btnSrcBrowse.setMnemonic('B');
		btnSrcBrowse.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setSelectedFile(new File(""));
				int option=fileChooser.showOpenDialog(EncryptedFilter.this);
				if(option==JFileChooser.APPROVE_OPTION)
				{
					txtSrc.setText(fileChooser.getSelectedFile().getPath());
				}
			}
			
		});
		JButton btnDstBrowse=new JButton("Browse");
		btnDstBrowse.setMnemonic('R');
		btnDstBrowse.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setSelectedFile(new File(""));
				System.out.print(fileChooser.getSelectedFile());
				int option=fileChooser.showOpenDialog(EncryptedFilter.this);
				if(option==JFileChooser.APPROVE_OPTION)
				{
					txtDst.setText(fileChooser.getSelectedFile().getPath());
				}
			}
			
		});
		
		JButton btnEncrypt=new JButton("Encrypt");
		btnEncrypt.setMnemonic('E');
		btnEncrypt.setToolTipText("Encrypt the source file to destination file");
		btnEncrypt.addActionListener(new BtnEncryptL());
		JButton btnDecrypt=new JButton("Decrypt");
		btnDecrypt.setMnemonic('D');
		btnDecrypt.setToolTipText("Decrypt the source file to the destination file");
		btnDecrypt.addActionListener(new BtnDecryptL());
		
		JButton btnSetSeed=new JButton("Seed");
		btnSetSeed.setMnemonic('S');
		btnSetSeed.setToolTipText("Set seed of the filter");
		btnSetSeed.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				String str=JOptionPane.showInputDialog(EncryptedFilter.this,"Input the seed:");
				if(str==null)
					return;
				try
				{
					int i=Integer.parseInt(str);
					seed=i;
				}
				catch(NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog
					(EncryptedFilter.this,nfe.toString()
							,"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		GridBagLayout gbLayout=new GridBagLayout();
		GridBagConstraints gbCon=new GridBagConstraints();
		contentPane.setLayout(gbLayout);
		
		this.addComponent(new JLabel("Source:"),0,0,1,1,gbCon);
		this.addComponent(txtSrc,1,0,1,1,gbCon);
		this.addComponent(btnSrcBrowse,2,0,1,1,gbCon);
		this.addComponent(new JLabel("Destination:"),3,0,1,1,gbCon);
		this.addComponent(txtDst,4,0,1,1,gbCon);
		this.addComponent(btnDstBrowse,5,0,1,1,gbCon);
		this.addComponent(btnEncrypt,1,1,1,1,gbCon);
		this.addComponent(btnDecrypt,3,1,1,1,gbCon);
		this.addComponent(btnSetSeed,4,1,1,1,gbCon);
		this.pack();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size=this.getSize();
		this.setLocation
		((d.width-size.width)/2,(d.height-size.height)/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addComponent(Component c,int gridx,int gridy
			,int gridwidth,int gridheight,GridBagConstraints gbCon)
	{
		gbCon.gridx=gridx;
		gbCon.gridy=gridy;
		gbCon.gridheight=gridheight;
		gbCon.gridwidth=gridwidth;
		this.getContentPane().add(c,gbCon);
	}
	

	
	class BtnEncryptL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				FileInputStream fIn=new FileInputStream(txtSrc.getText());
				EncryptOutputStream output=new EncryptOutputStream
				(new FileOutputStream(txtDst.getText()),seed);
				byte[] b=new byte[100];
				while(fIn.available()>0)
				{
					int len=fIn.read(b);
					output.write(b,0,len);
				}
				fIn.close();
				output.close();
				
				JOptionPane.showMessageDialog
				(EncryptedFilter.this
						,"Encryption complete"
						,"Information"
						,JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog
				(EncryptedFilter.this,ex.toString()
						,"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	class BtnDecryptL implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				FileOutputStream output=new FileOutputStream
				(txtDst.getText());
				DecryptInputStream input=new DecryptInputStream
				(new FileInputStream(txtSrc.getText()),seed);
				byte[] b=new byte[100];
				while(input.available()>0)
				{
					int len=input.read(b);
					output.write(b,0,len);
				}
				output.close();
				input.close();
				JOptionPane.showMessageDialog
				(EncryptedFilter.this
						,"Decryption complete"
						,"Information"
						,JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog
				(EncryptedFilter.this,ex.toString()
						,"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
}

class DecryptInputStream extends BufferedInputStream
{
	private final int seed;
	
	public DecryptInputStream(InputStream in,int seed)
	{
		super(in);
		this.seed=seed;
	}
	
	public DecryptInputStream(InputStream in, int size,int seed) 
	{
		super(in,size);
		this.seed=seed;
	}
	
	public int read() throws IOException
	{
		int i=super.read();
		return Encryption.logicDecrypt(i,seed);
	}
	public int read(byte[] b) throws IOException
	{
		int len=read(b,0,b.length);
		return len;
	}

	public int read(byte[] b, int off, int len) throws IOException
	{
		int readed=super.read(b,off,len);
		for(int i=off;i<readed+off;i++)
		{
			b[i]=(byte)Encryption.logicDecrypt(b[i],seed);
		}
		return readed;
	}
}

class EncryptOutputStream extends BufferedOutputStream
{
	private final int seed;

	public EncryptOutputStream(OutputStream out,int seed)
	{
		super(out);
		this.seed=seed;
	}
	public EncryptOutputStream(OutputStream out,int size,int seed)
	{
		super(out,size);
		this.seed=seed;
	}
	
	public void write(byte[] b) throws IOException
	{
		write(b,0,b.length);
	}
	public void write(byte[] b,int off,int len) throws IOException
	{
		for(int i=off;i<len+off;i++)
			b[i]=(byte)Encryption.logicEncrypt(b[i],seed);
		super.write(b,off,len);
	}
	
	public void write(byte b) throws IOException
	{
		b=(byte)Encryption.logicEncrypt(b,seed);
		super.write(b);
	}
}