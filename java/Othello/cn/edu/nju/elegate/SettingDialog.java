package cn.edu.nju.elegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

/**
 * this class represent a dialog to setting the network for the client
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class SettingDialog extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox serverIp;
	private JFormattedTextField serverPort;
	public SettingDialog(final Othello parent,String ip,int port)
	{
		super(parent,true);
		String[] servers={"210.29.240.251","192.168.1.56","192.168.1.100"
				,"localhost"
		};
		serverIp=new JComboBox(servers);
		serverIp.setEditable(true);
		serverIp.setSelectedItem(ip);
		NumberFormat format=NumberFormat.getIntegerInstance();
		format.setGroupingUsed(false);
		serverPort=new JFormattedTextField(format);
		serverPort.setValue(port);
		JLabel lblServerIp=new JLabel("Server:");
		lblServerIp.setDisplayedMnemonic('S');
		lblServerIp.setLabelFor(serverIp);
		JLabel lblPort=new JLabel("Port:");
		lblPort.setDisplayedMnemonic('P');
		lblPort.setLabelFor(serverPort);
		
		JPanel panel=new JPanel(new GridBagLayout());
		GridBagConstraints gbCon=new GridBagConstraints();
		gbCon.anchor=GridBagConstraints.WEST;
		gbCon.weightx=100;
		gbCon.weighty=100;
		gbCon.fill=GridBagConstraints.HORIZONTAL;
		this.addComponent(lblServerIp,1,1,1,1,gbCon,panel);
		this.addComponent(serverIp,2,1,1,1,gbCon,panel);
		this.addComponent(lblPort,1,2,1,1,gbCon,panel);
		this.addComponent(serverPort,2,2,1,1,gbCon,panel);
		JButton btnOk=new JButton("Ok");
		btnOk.setMnemonic('O');
		btnOk.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0)
			{
				parent.setServer(serverIp.getSelectedItem().toString(),
						((Number)serverPort.getValue()).intValue());
				setVisible(false);
			}
			
		});
		JButton btnCancel=new JButton("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(new ActionListener()
			{

			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(true);
				dispose();
			}
			
		});
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnOk);
		btnPanel.add(btnCancel);
		this.addComponent(btnPanel,2,3,2,1,gbCon,panel);
		this.getContentPane().add(panel,BorderLayout.CENTER);
		this.pack();
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size=this.getSize();
		this.setLocation((d.width-size.width)/2,(d.height-size.height)/2);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void addComponent(Component c,int gridx,int gridy
			,int gridwidth,int gridheight,GridBagConstraints gbCon,Container container)
	{
		gbCon.gridx=gridx;
		gbCon.gridy=gridy;
		gbCon.gridwidth=gridwidth;
		gbCon.gridheight=gridheight;
		container.add(c,gbCon);
	}
}
