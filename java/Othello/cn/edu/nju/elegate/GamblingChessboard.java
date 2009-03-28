package cn.edu.nju.elegate;

/**
 * a gambling chessboard for network or person to computer battle
 * @author Elegate
 *
 */
public class GamblingChessboard extends Chessboard
{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Chessboard.ChequerState personTurn;
	
	public GamblingChessboard()
	{
		this(Chessboard.ChequerState.Black);
	}
	
	public GamblingChessboard(Chessboard.ChequerState personTurn)
	{
		super();
		this.personTurn=personTurn;
	}
	public GamblingChessboard(Chessboard board
			,Chessboard.ChequerState personTurn)
	{
		super(board);
		this.personTurn=personTurn;
	}
	
	public void setPersonTurn(Chessboard.ChequerState personTurn)
	{
		this.personTurn=personTurn;
	}
	
	public Chessboard.ChequerState getPersonTurn()
	{
		return this.personTurn;
	}
	
	public void reset()
	{
		super.reset();
		this.personTurn=ChequerState.Black;
	}
	
	public boolean isMoveValid(int pos)
	{
		if(this.getCurrentTurn()==personTurn)
			return super.isMoveValid(pos);
		else
			return false;
	}
	
	public void placeChequer(int pos)
	{
		if(pos<0||pos>63)         //means pass
		{
			return;
		}
		
		this.setChequerState(pos,currentTurn);
		this.doFlips(pos,currentTurn);
		if(currentTurn==ChequerState.Black)
			blackMoves++;
		else if(currentTurn==ChequerState.White)
			whiteMoves++;
		currentTurn=ChequerState.inverseState(currentTurn);
		if(this.currentTurn==this.personTurn)
		{
			Pair p=new Pair(this.blackBoard,this.whiteBoard,currentTurn);
			steps.add(p);
			this.indexOfStep=steps.size()-1;
		}
	}
	
	public boolean isOpponentTrun()
	{
		if(this.isGameStart())
			return this.getCurrentTurn()!=this.personTurn;
		else
			return false;
	}
}

/**
 * this chessboard is for observer
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
class ObserverModeChessboard extends GamblingChessboard
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isMoveValid(int pos)
	{
		  return false;
	}
}
