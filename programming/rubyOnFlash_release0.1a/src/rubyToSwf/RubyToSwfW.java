/*
 * Ruby On Flash is a compiler written in Java that compiles Ruby source code directly into Flash applications(.swf files), 
 * and aims to provide a programmer-friendly approach to casual Flash game development.   
 * 
 * Copyright (C) 2006-2007 Lem Hongjian (http://sourceforge.net/projects/rubyonflash)
 * 
 * This file is part of Ruby On Flash.
 *
 * Ruby On Flash is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ruby On Flash is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ruby On Flash.  If not, see <http://www.gnu.org/licenses/>.
 */

package rubyToSwf;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class RubyToSwfW extends JFrame implements ActionListener {

	static class MyWindowListener extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	}
	private JTextField srcTxt;
	private JButton srcBrowseBtn, resourceBrowseBtn, compileBtn, libAddBtn;
	private JTextField outputTxt;
	private JTextField frameRateTxt, widthTxt, heightTxt, bgColorTxt, resourceTxt,libTxt;
	public RubyToSwfW(){
		Container container = this.getContentPane();
		container.setLayout(new GridLayout(0,2));
		
		container.add(new JLabel("Source: "));
		container.add(new JLabel(""));
		
		srcTxt = new JTextField();
		container.add(srcTxt);
		srcBrowseBtn = new JButton("Browse...");
		srcBrowseBtn.addActionListener(this);
		container.add(srcBrowseBtn);
		
		container.add(new JLabel("Output:"));
		container.add(new JLabel(""));
		
		outputTxt = new JTextField();
		container.add(outputTxt);
		container.add(new JLabel(""));
		
		container.add(new JLabel("Frame rate/fps"));
		frameRateTxt = new JTextField("12");
		container.add(frameRateTxt);
		
		container.add(new JLabel("Width/px"));
		widthTxt = new JTextField("400");
		container.add(widthTxt);

		container.add(new JLabel("Height/px"));
		heightTxt = new JTextField("300");
		container.add(heightTxt);
		
		container.add(new JLabel("Background color/hex"));
		bgColorTxt = new JTextField("ffffff");
		container.add(bgColorTxt);
		
		container.add(new JLabel("Resource:"));
		container.add(new JLabel(""));
		
		resourceTxt = new JTextField();
		container.add(resourceTxt);
		resourceBrowseBtn = new JButton("Browse...");
		resourceBrowseBtn.addActionListener(this);
		container.add(resourceBrowseBtn);
		
		container.add(new JLabel("Libraries: "));
		container.add(new JLabel(""));
		
		libTxt = new JTextField();
		container.add(libTxt);
		libAddBtn = new JButton("Add...");
		libAddBtn.addActionListener(this);
		container.add(libAddBtn);
		
		compileBtn = new JButton("Compile");
		container.add(compileBtn);
		compileBtn.addActionListener(this);
		container.add(new JLabel(""));
		
		this.setSize(400, 400);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(new MyWindowListener());
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==srcBrowseBtn){
			JFileChooser fileChooser = new JFileChooser();
			try{
				int val = fileChooser.showOpenDialog(this);
				if(val==JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					srcTxt.setText(file.getPath());
				}
			}catch(Exception exp){
				exp.printStackTrace();
				System.exit(1);
			}
		}else if(e.getSource()==resourceBrowseBtn){
			JFileChooser fileChooser = new JFileChooser();
			try{
				int val = fileChooser.showOpenDialog(this);
				if(val==JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					resourceTxt.setText(file.getPath());
				}
			}catch(Exception exp){
				exp.printStackTrace();
				System.exit(1);
			}
		}else if(e.getSource()==libAddBtn){
			JFileChooser fileChooser = new JFileChooser();
			try{
				int val = fileChooser.showOpenDialog(this);
				if(val==JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					if(libTxt.getText().trim().length()==0){
						libTxt.setText(file.getPath());
					}else{
						libTxt.setText(libTxt.getText() + ";"+file.getPath());
					}
				}
			}catch(Exception exp){
				exp.printStackTrace();
				System.exit(1);
			}
		}else if(e.getSource()==compileBtn){
			java.util.Vector<String> args = new java.util.Vector<String>();
			args.add("-s");
			args.add(srcTxt.getText());
			
			if(outputTxt.getText().length() > 0){
				args.add("-o");
				args.add(outputTxt.getText());
			}
			
			args.add("-f");
			args.add(frameRateTxt.getText());
			
			args.add("-w");
			args.add(widthTxt.getText());
			
			args.add("-h");
			args.add(heightTxt.getText());
			
			args.add("-b");
			args.add(bgColorTxt.getText());
			
			if(resourceTxt.getText().length()>0){
				args.add("-r");
				args.add(resourceTxt.getText());
			}
			
			if(libTxt.getText().length() >0){
				String[] libs = libTxt.getText().split(";");
				for(int i=0;i <libs.length;i++){
					args.add("-l");
					args.add(libs[i]);
				}
			}
			
			//compile
			rubyToSwf.RubyToSwf.main(args.toArray(new String[0]));
			System.gc();
			
			JOptionPane.showMessageDialog(this, "Compiled successfully!");
			System.exit(0);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length > 0){
			rubyToSwf.RubyToSwf.main(args);
		}else{
			new RubyToSwfW();
		}
	}

}
