package cn.edu.nju.elegate;

import javax.swing.*;

public class DebugFrame extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DebugFrame(Chessboard board)
	{
		this.setModal(true);
		setTitle("Debug");
		JTextArea txtArea=new JTextArea(20,30);
		txtArea.append(board.toString()+"\n");
		txtArea.append("debug:\n"+board.toChessString(board.debug()));
		txtArea.append(String.valueOf("steps:"+ board.getUndoList().size())+"\n");
		txtArea.append("gameover:"+board.gameOver()+"\n");
		txtArea.append("currentTurn:"+board.getCurrentTurn()+"\n");
		txtArea.append("isCurrentCanMove:"+board.isCurrentTurnHasValidMove());
		txtArea.append(board.getCurrentTurn()+"'s valid moves:"+board.toChessString
				(board.getValidMoves(board.getCurrentTurn()))+"\n");
		txtArea.append(Chessboard.ChequerState.inverseState
				(board.getCurrentTurn())
				+"'s valid moves:"+board.toChessString
				(board.getValidMoves(Chessboard.ChequerState.inverseState(board.getCurrentTurn())))+"\n");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().add(new JScrollPane(txtArea));
		pack();
		setVisible(true);
	}
	
}
