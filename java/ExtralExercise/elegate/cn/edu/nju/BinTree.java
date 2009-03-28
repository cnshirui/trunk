package elegate.cn.edu.nju;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.text.*;
import java.util.ArrayList;
import java.awt.event.*;
/**
 * class to implement a binary tree
 * @author Elegate
 * @author cs department of NJU
 */
public class BinTree 
{
	/**
	 * the root of the tree
	 */
	private BinNode root;
	/**
	 *for search or remove or add operation
	 */
	private BinNode current;
	/**
	 * current's parent,for search or remove or add operation
	 */
	private BinNode parent;
	/**
	 * the number of nodes in the tree
	 */
	private int size;
	
	/**
	 * default constructor
	 *
	 */
	public BinTree()
	{
		root=null;
		current=null;
		parent=null;
		size=0;
	}
	/**
	 * constructor with an argument
	 * @param root the root of a tree
	 */
	public BinTree(BinNode root)
	{
		this.root=root;
		size=1;
		current=null;
		parent=null;
	}
	
	/**
	 * insert a node into the tree
	 * <strong>the right child node is larger than the farther node
	 * while the left child node is not larger than the farther node</strong> 
	 * @param node the new node
	 */
	public void insertNode(BinNode node)
	{
		if ( root==null )
		{
			root=node;
			current=null;
			parent=null;
			size++;
			return;
		}

		current=root;
		parent=null;
		boolean right=false;
		while(true)
		{
			if(current==null)
			{
				if(right)
					parent.setRightChild(node);
				else
					parent.setLeftChild(node);
				size++;
				return;
			}
			if( node.getData()>current.getData())
			{
				parent=current;
				current=current.getRightChild();
				right=true;
			}
			else
			{
				right=false;
				parent=current;
				current=current.getLeftChild();
			}
		}
	}
	
	/**
	 * remove a node from the tree
	 * @param node the node to be removed
	 * @return return true if succeed,return false otherwise
	 */
	public boolean removeNode(BinNode node)
	{
		find(node);
		if(current==null)
			return false;
		else if(parent==null &&
				(current.getLeftChild()==null 
				|| current.getRightChild()==null) )
		{
			if(current.getLeftChild()==null)
				root=current.getRightChild();
			else
				root=current.getLeftChild();
		}
		else
		{
			boolean right= parent!=null? ( parent.getRightChild()==current ):false;
			if(current.getLeftChild()==null)
			{
				if(right)
					parent.setRightChild(current.getRightChild());
				else
					parent.setLeftChild(current.getRightChild());
			}
			else if(current.getRightChild()==null)
			{
				if(right)
					parent.setRightChild(current.getLeftChild());
				else
					parent.setLeftChild(current.getLeftChild());
			}
			else
			{
				parent=current;
				BinNode minNode=current.getRightChild();
				while(true)
				{
					if(minNode.getLeftChild()==null)
						break;
					parent=minNode;
					minNode=minNode.getLeftChild();
				}
				int data=current.getData();
				current.setData(minNode.getData());
				minNode.setData(data);
				parent.setLeftChild(minNode.getRightChild());
			}
		}
		size--;
		return true;
	}
	
	/**
	 * get the minimum data of the tree with the argument as its root
	 * @param node the root
	 * @return the node with the minimum data 
	 */
	public BinNode min(BinNode node)
	{
		BinNode minNode=node;
		if(node==null)
			return null;
		while(true)
		{
			if(minNode.getLeftChild()==null)
				return minNode;
			minNode=minNode.getLeftChild();
		}
	}
	/**
	 * get the maximum data of the tree with the argument as its root
	 * @param node the root
	 * @return the node with the maximum data 
	 */
	public BinNode max(BinNode node)
	{
		BinNode maxNode=node;
		if(node==null)
			return null;
		while(true)
		{
			if(maxNode.getRightChild()==null)
				return maxNode;
			maxNode=maxNode.getRightChild();
		}
	}
	
	/**
	 * find the specific node in the tree
	 * @param node the node with the value to find
	 * @return the node which match the specific argument
	 */
	public BinNode find(BinNode node)
	{
	    current=root;
		parent=null;
		while(true)
		{
			if(current==null)
				return null;
			else if(current.getData()==node.getData())
				return current;
			if(node.getData()>current.getData())
			{
				parent=current;
				current=current.getRightChild();
			}
			else
			{
				parent=current;
				current=current.getLeftChild();
			}
		}
	}
	
	/**
	 * get the number of nodes in the tree
	 * @return the number of nodes in the tree
	 */
	public int size()
	{
		return size;
	}
	
	/**
	 * inorder traversal method,and print the data to System.out
	 *
	 */
	public void inorderTraversal()
	{
		ArrayList<Integer> lst=new ArrayList<Integer>();
		this.inorderTraversal( root ,lst);
		System.out.println(lst);
	}
	/**
	 * inorder traversal method,return the data in the second argument<strong> arrLst</strong>
	 * @param node the root node
	 * @param arrLst the ArrayList to save all the data in the tree 
	 */
	public void inorderTraversal(BinNode node,ArrayList<Integer> arrLst)
	{
		if(node!=null)
			inorderTraversal(node.getLeftChild(),arrLst);
		else
			return;
		arrLst.add(new Integer(node.getData()));
		inorderTraversal(node.getRightChild(),arrLst);	
	}
	
	/**
	 * preorder traversal method,and print the data to System.out
	 *
	 */
	public void preorderTraversal()
	{
		ArrayList<Integer> lst=new ArrayList<Integer>();
		this.preorderTraversal(root,lst);
		System.out.println(lst);
	}
	
	/**
	 * preorder traversal method,return the data in the second argument<strong> arrLst</strong>
	 * @param node the root node
	 * @param arrLst the ArrayList to save all the data in the tree 
	 */
	public void preorderTraversal(BinNode node,ArrayList<Integer> arrLst)
	{
		if(node!=null)
			arrLst.add(new Integer(node.getData()));
		else
			return;
		preorderTraversal(node.getLeftChild(),arrLst);
		preorderTraversal(node.getRightChild(),arrLst);
	}
	
	/**
	 * postorder traversal method,and print the data to System.out
	 *
	 */
	public void postorderTraversal()
	{
		ArrayList<Integer> lst=new ArrayList<Integer>();
		this.postorderTraversal(root,lst);
		System.out.println(lst);
	}
	
	/**
	 * postorder traversal method,return the data in the second argument<strong> arrLst</strong>
	 * @param node the root node
	 * @param arrLst the ArrayList to save all the data in the tree 
	 */
	public void postorderTraversal(BinNode node,ArrayList<Integer> arrLst)
	{
		if(node==null)
			return;
		postorderTraversal(node.getLeftChild(),arrLst);
		postorderTraversal(node.getRightChild(),arrLst);
		//System.out.print(node.getData()+" ");
		arrLst.add(new Integer(node.getData()));
	}
	
	/**
	 * get the root of the tree
	 * @return the root
	 */
	public final BinNode getRoot()
	{
		return root;
	}
	/**
	 * toString method
	 */
	public String toString()
	{
		return root.toString();
	}
	
	
	
	public static void main(String[] args)
	{
		JFrame frame=new JFrame("Binary Tree Emulator");
		frame.add(new BinTreePanel());
		JMenu mnuFile=new JMenu("File");
		mnuFile.setMnemonic('F');
		JMenuItem mnuExit=new JMenuItem("Exit");
		mnuExit.setMnemonic('x');
		mnuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK ));
		mnuExit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		JMenuBar mnuBar=new JMenuBar();
		mnuBar.add(mnuFile);
		mnuFile.add(mnuExit);
		frame.setJMenuBar(mnuBar);
		frame.pack();
		frame.setSize(600,600);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width-frame.getWidth())/2,
				(d.height-frame.getHeight())/2);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
/**
 * a class describe a node in a binary tree
 * @author Elegate
 * @author cs department of NJU
 */
class BinNode
{
	/**
	 * data saved in the node
	 */
	private int data;
	
	/**
	 * point to the left child node 
	 */
	private BinNode leftChild;
	
	
	/**
	 * point to the right child node 
	 */
	private BinNode rightChild;
	
	/**
	 * default constructor,set data with 0,
	 * letfChild and rightChild with null
	 *
	 */
	public BinNode()
	{
		data=0;
		this.leftChild=null;
		this.rightChild=null;
	}
	
	/**
	 * constructor with one argument
	 * @param data - integer data
	 */
	public BinNode(int data)
	{
		this.data=data;
		this.leftChild=null;
		this.rightChild=null;
	}
	
	/**
	 * constructor with two arguments
	 * @param data the data to be saved in the node
	 * @param left the pointer pointing to the left child node
	 * @param right the pointer pointing to the right child node
	 */
	public BinNode(int data,BinNode left,BinNode right)
	{
		this.data=data;
		this.leftChild=left;
		this.rightChild=right;
	}
	
	/**
	 * setter method for data field
	 * @param data the new data
	 */
	public final void setData(int data)
	{
		this.data=data;
	}
	
	/**
	 * setter method for leftChild pointer
	 * @param left  the new leftChild pointer
	 */
	public final void setLeftChild(BinNode left)
	{
		this.leftChild=left;
	}
	
	/**
	 * setter method for rightChild pointer
	 * @param right the new rightChild pointer
	 */
	public final void setRightChild(BinNode right)
	{
		this.rightChild=right;
	}
	/**
	 * get the data saved in the node
	 * @return the data saved in the node
	 */
	public final int getData()
	{
		return data;
	}
	
	/**
	 * get the pointer for the left child node
	 * @return the pointer pointing to the next node
	 */
	public final BinNode getLeftChild()
	{
		return leftChild;
	}
	
	/**
	 * get the pointer for the right child node
	 * @return the pointer for the right child node
	 */
	public final BinNode getRightChild()
	{
		return rightChild;
	}
	
	public String toString()
	{
		return this.getClass().getName()
		+":[data="+data+",leftChild="
		+leftChild+",rightChild="+rightChild+"]";
	}
}


class BinTreePanel extends JPanel
{
	/**
	 * inorder traversal method
	 */
	public static final int INORDER=0;
	/**
	 * preorder traversal method
	 */
	public static final int PREORDER=1;
	/**
	 * postorder traversal method
	 */
	public static final int POSTORDER=2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the list to show the data in the tree
	 */
	private JList list;
	/**
	 * the formatted text field for inputing an integer
	 */
	private JFormattedTextField txtField;
	/**
	 * button to add a node
	 */
	private JButton btnAddNode;
	/**
	 * button to find nodes
	 */
	private JButton btnFindNode;
	/**
	 * button to remove nodes
	 */
	private JButton btnRemoveNode;
	/**
	 * text area to show operation results
	 */
	private JTextArea txtArea;
	/**
	 * list model
	 */
	private DefaultListModel lstModel;
	/**
	 * BinTree instance
	 */
	private BinTree tree;
	/**
	 * button group to organize the radio buttons
	 */
	private ButtonGroup  btnGroup;
	/**
	 * radio button for inorder traversal 
	 */
	private JRadioButton btnInorder;
	/**
	 * radio button for preorder traversal
	 */
	private JRadioButton btnPreorder;
	/**
	 * radion button for post order
	 */
	private JRadioButton btnPostorder;
	/**
	 * current traversal method used
	 */
	private int traversalMethod=INORDER;
	
	/**
	 * constructor
	 *
	 */
	public BinTreePanel()
	{
		lstModel=new DefaultListModel();
		list=new JList(lstModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		txtField=new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtField.setColumns(10);
		txtField.setToolTipText("Formatted text field for integer value");
		txtField.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) 
			{
				btnAddNode.doClick();
			}
		});
		
		btnAddNode=new JButton("Add");
		btnAddNode.setMnemonic('A');
		btnAddNode.setToolTipText("Insert a node into the tree");
		btnAddNode.addActionListener(new BtnAddNodeL());
		btnFindNode=new JButton("Search");
		btnFindNode.setMnemonic('S');
		btnFindNode.setToolTipText("Search a node in the tree");
		btnFindNode.addActionListener(new BtnFindNodeL());
		btnRemoveNode=new JButton("Remove");
		btnRemoveNode.setMnemonic('R');
		btnRemoveNode.setToolTipText("Remove a node from the tree");
		btnRemoveNode.addActionListener(new BtnRemoveNodeL());
		JButton btnClear=new JButton("Clear");
		btnClear.setMnemonic('C');
		btnClear.setToolTipText("Clear the infomation area");
		btnClear.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				txtArea.setText("");
			}
			
		});
		
		btnGroup=new ButtonGroup();
		TraversalOrderL l=new TraversalOrderL();
		btnInorder=new JRadioButton("Inorder",true);
		btnInorder.setActionCommand("inorder");
		btnInorder.setMnemonic('I');
		btnInorder.setToolTipText("Change to inorder traversal mode");
		btnInorder.addActionListener(l);
		btnPreorder=new JRadioButton("Preorder");
		btnPreorder.setActionCommand("preorder");
		btnPreorder.setMnemonic('P');
		btnPreorder.setToolTipText("Change to preorder traversal mode");
		btnPreorder.addActionListener(l);
		btnPostorder=new JRadioButton("Postorder");
		btnPostorder.setActionCommand("postorder");
		btnPostorder.setMnemonic('O');
		btnPostorder.setToolTipText("Change to postorder traversal mode");
		btnPostorder.addActionListener(l);
		btnGroup.add(btnInorder);
		btnGroup.add(btnPreorder);
		btnGroup.add(btnPostorder);
		
		txtArea=new JTextArea(25,20);
		txtArea.setEditable(false);
		tree=new BinTree();
		
		
		
		GridBagLayout gbLayout=new GridBagLayout();
		GridBagConstraints gbConstraints=new GridBagConstraints();
		this.setLayout(gbLayout);
		JLabel lbl=new JLabel("Input a integer:");
		gbConstraints.weightx=10;
		gbConstraints.weighty=10;
		this.addComponent(btnInorder,gbConstraints,0,0,1,1);
		this.addComponent(btnPreorder,gbConstraints,1,0,1,1);
		this.addComponent(btnPostorder,gbConstraints,2,0,1,1);
		this.addComponent(lbl,gbConstraints,3,0,1,1);
		this.addComponent(txtField,gbConstraints,4,0,2,1);
		this.addComponent(btnAddNode,gbConstraints,3,1,1,1);
		this.addComponent(btnRemoveNode,gbConstraints,4,1,1,1);
		this.addComponent(btnFindNode,gbConstraints,5,1,1,1);
		this.addComponent(btnClear,gbConstraints,6,1,1,1);
		gbConstraints.fill=GridBagConstraints.BOTH;
		this.addComponent(new JScrollPane(list),gbConstraints,0,2,3,10);
		Border border=BorderFactory.createTitledBorder("Information");
		JScrollPane scrollPane=new JScrollPane(txtArea);
		scrollPane.setBorder(border);
		this.addComponent(scrollPane,gbConstraints,3,2,4,10);
	}
	
	/**
	 * 
	 * @param c component
	 * @param con GridBagConstraints instance
	 * @param gridx x coordinate
	 * @param gridy y coordinate
	 * @param width width 
	 * @param height height
	 */
	private void addComponent(Component c,GridBagConstraints con,int gridx,int gridy,int width,int height)
	{
		con.gridx=gridx;
		con.gridy=gridy;
		con.gridheight=height;
		con.gridwidth=width;
		this.add(c,con);
	}
	
	/**
	 * update the list if change happens
	 *
	 */
	private void updateModel()
	{
		lstModel.removeAllElements();
		ArrayList<Integer> lst=new ArrayList<Integer>();
		if(this.traversalMethod==INORDER)
			tree.inorderTraversal(tree.getRoot(),lst);
		else if(this.traversalMethod==PREORDER)
			tree.preorderTraversal(tree.getRoot(),lst);
		else
			tree.postorderTraversal(tree.getRoot(),lst);
		for(int i=0;i<lst.size();i++)
		{
			lstModel.addElement(lst.get(i));
		}
	}
	
	/**
	 * listener for button AddNode
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class BtnAddNodeL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			Long l=(Long)txtField.getValue();
			if(l==null)
			{
				txtArea.append("Illegal input\n");
				txtField.requestFocusInWindow();
				return;
			}
			tree.insertNode(new BinNode(l.intValue()));
			BinTreePanel.this.updateModel();
			txtField.setValue(null);
			txtField.setText("");
			txtField.requestFocusInWindow();
		}
	}
	
	/**
	 * listener for button RemoveNode
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class BtnRemoveNodeL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			
			Long l=(Long)txtField.getValue();
			if(l==null)
			{
				txtArea.append("Illegal input\n");
				txtField.requestFocusInWindow();
				return;
			}
			boolean ret=tree.removeNode(new BinNode(l.intValue()));
			if(ret)
			{
				txtArea.append("Node("+l+") removed\n");
			}
			else
			{
				txtArea.append("No such a node("+l+")\n");
			}
			BinTreePanel.this.updateModel();
			txtField.setValue(null);
			txtField.setText("");
			txtField.requestFocusInWindow();
		}
	}
	
	/**
	 * listener for button FindNode
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class BtnFindNodeL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			Long l=(Long)txtField.getValue();
			if(l==null)
			{
				txtArea.append("Illegal input\n");
				txtField.requestFocusInWindow();
				return;
			}
			BinNode node=tree.find(new BinNode(l.intValue()));
			if(node==null)
				txtArea.append("No such a node("+l+")\n");
			else
			{
				txtArea.append("Node("+l+") founded\n");
				int size=lstModel.getSize();
				ArrayList<Integer> arrLst=new ArrayList<Integer>();
				for(int i=0;i<size;i++)
				{
					if( ((Integer)lstModel.get(i)).intValue()==node.getData() )
					{
						arrLst.add(new Integer(i));
					}
				}
				int[] arr=new int[arrLst.size()];
				for(int i=0;i<arrLst.size();i++)
					arr[i]=((Integer)arrLst.get(i)).intValue();
				list.setSelectedIndices(arr);
			}
			txtField.setValue(null);
			txtField.setText("");
			txtField.requestFocusInWindow();
		}
	}
	
	/**
	 * listener for the three radion buttons
	 * @author Elegate
	 * @author cs department of NJU
	 */
	class TraversalOrderL implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			
			int tmp=traversalMethod;
			String cmd=e.getActionCommand();
			if(cmd.equals("inorder"))
			{
				traversalMethod=INORDER;
			}
			else if(cmd.equals("preorder"))
			{
				traversalMethod=PREORDER;
			}
			else if(cmd.equals("postorder"))
			{
				traversalMethod=POSTORDER;
			}
			if(tmp!=traversalMethod)
				BinTreePanel.this.updateModel();
		}
	}
}
