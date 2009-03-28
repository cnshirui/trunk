package cn.edu.nju.elegate;

import java.io.Serializable;
import java.util.*;
import java.util.logging.*;


/**
 * class to represent a chess board
 * @author Elegate,elegate@gmail.com
 * @author cs department of NJU
 */
public class Chessboard implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the state of each chequer in the chess board
	 * @author Elegate,elegate@gmail.com
	 * @author cs department of NJU
	 */
	public enum ChequerState
	{
		Empty,
		Black,
		White;
		
		public final static ChequerState inverseState(ChequerState state)
		{
			if(state==Black)
				return White;
			else if(state==White)
				return Black;
			return Empty;
		}
	}
	final public class ChessboardState implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		long blackBoard;
		long whiteBoard;
		int blackMoves;
		int whiteMoves;
		ChequerState turn;
		public ChessboardState(long blackBoard,long whiteBoard,int blackMoves,int whiteMoves,ChequerState turn)
		{	
			this.blackBoard=blackBoard;
			this.whiteBoard=whiteBoard;
			this.blackMoves=blackMoves;
			this.whiteMoves=whiteMoves;
			this.turn=turn;
		}	
	}
	
	final public class Pair implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long blackBoard;
		private long whiteBoard;
		private ChequerState turn;
		public Pair(long blackBoard,long whiteBoard,ChequerState turn)
		{
			this.blackBoard=blackBoard;
			this.whiteBoard=whiteBoard;
			this.turn=turn;
		}
		public void  setBlackBoard(long black)
		{
			this.blackBoard=black;
		}
		public long getBlackBoard()
		{
			return this.blackBoard;
		}
		
		public void setWhiteBoard(long white)
		{
			this.whiteBoard=white;
		}
		public long getWhiteBoard()
		{
			return this.whiteBoard;
		}
		public ChequerState getTurn()
		{
			return this.turn;
		}
		public void setTurn(ChequerState turn)
		{
			this.turn=turn;
		}
		public String toString()
		{
			return this.getClass()+"[blackBoard="
			+blackBoard+"" + ",whiteBoard="
			+whiteBoard+",turn="+turn+"]";
		}
	}
	
	
	public final static long ONES_64=0XFFFFFFFFFFFFFFFFL;
	public final static int NORTH=-8;
	public final static int SOUTH=8;
	public final static int WEST=-1;
	public final static int EAST=1;
	public final static int NORTH_WEST=-9;
	public final static int NORTH_EAST=-7;
	public final static int SOUTH_WEST=7;
	public final static int SOUTH_EAST=9;
	//the eight directions
	
	public final static int[] directionInc=
	{NORTH,SOUTH,WEST,EAST,NORTH_WEST
		,NORTH_EAST,SOUTH_WEST,SOUTH_EAST};
	
	public final static String[] HEADER=
	{"A","B","C","D","E","F","G","H"};
	
	//directions for each chequer
	public final static int[] dirMask=
	{
		0x51, 0x51, 0x73, 0x73, 0x73, 0x73, 0x62, 0x62,
		0x51, 0x51, 0x73, 0x73, 0x73, 0x73, 0x62, 0x62,
		0xD5, 0xD5, 0xFF, 0xFF, 0xFF, 0xFF, 0xEA, 0xEA,
		0xD5, 0xD5, 0xFF, 0xFF, 0xFF, 0xFF, 0xEA, 0xEA,
		0xD5, 0xD5, 0xFF, 0xFF, 0xFF, 0xFF, 0xEA, 0xEA,
		0xD5, 0xD5, 0xFF, 0xFF, 0xFF, 0xFF, 0xEA, 0xEA,
		0x94, 0x94, 0xBC, 0xBC, 0xBC, 0xBC, 0xA8, 0xA8,
		0x94, 0x94, 0xBC, 0xBC, 0xBC, 0xBC, 0xA8, 0xA8,

	};
	
	public final static int[] staticEval=
	{   
		2000,-1000,20,15,15,20,-1000,2000,
		-1000,-2000,7,4,4,7,-2000,-1000,
		20,4,9,7,7,9,4,20,
		15,4,7,5,5,7,4,15,
		15,4,7,5,5,7,4,15,
		20,4,9,7,7,9,4,20,
		-1000,-2000,7,4,4,7,-2000,-1000,
		2000,-1000,20,15,15,20,-1000,2000
	};
	
	
	protected ChequerState currentTurn;
	
	protected ArrayList<Pair> steps = null;
	protected int indexOfStep;
	
	private final static Logger logger=Logger.getLogger("cn.edu.nju.elegate.ChessBoard");
	static
	{
		try
		{
			FileHandler fileHandler=new FileHandler("./log/log.xml");
			logger.addHandler(fileHandler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.WARNING);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * the black board,a 64 bit number,
	 * a bit of 1 represent the black chessman
	 */
	protected long blackBoard;
	/**
	 * the white board,a 64 bit number,
	 * a bit of 1 represent the white chessman
	 */
	protected long whiteBoard;
	/**
	 * to generate a chessboard
	 *
	 */
	/**
	 * the moves the black have taken
	 */
	protected int blackMoves=0;
	/**
	 * the moves the white have taken
	 */
	protected int whiteMoves=0;
	
	protected boolean gameStart;
	
	
	public Chessboard() 
	{
		gameStart=false;
		this.setBlack(3,4);
		this.setBlack(4,3);
		this.setWhite(3,3);
		this.setWhite(4,4);
		currentTurn=ChequerState.Black;
		steps=new ArrayList<Pair>();
		steps.add(new Pair(this.blackBoard,this.whiteBoard,this.currentTurn));
		indexOfStep=0;
	}
	/**
	 * copy constructor
	 * @param chessBoard a chess board used to 
	 * generate a copy of it 
	 */
	public Chessboard(Chessboard chessBoard)
	{
		this.blackBoard=chessBoard.blackBoard;
		this.whiteBoard=chessBoard.whiteBoard;
		this.blackMoves=chessBoard.blackMoves;
		this.whiteMoves=chessBoard.whiteMoves;
		this.currentTurn=chessBoard.currentTurn;
		this.gameStart=chessBoard.gameStart;
		this.steps=new ArrayList<Pair>(chessBoard.steps);
		logger.warning("in copy constructor");
		this.indexOfStep=chessBoard.indexOfStep;
	}
	
	
	public ChessboardState getChessboardState()
	{
		return new ChessboardState(this.blackBoard
				,this.whiteBoard,this.blackMoves
				,this.whiteMoves,this.currentTurn);
	}
	
	public void restore(ChessboardState state)
	{
		this.whiteBoard=state.whiteBoard;
		this.blackBoard=state.blackBoard;
		this.whiteMoves=state.whiteMoves;
		this.blackMoves=state.blackMoves;
		this.currentTurn=state.turn;
	}
	
	public Pair simMove(int pos)
	{
		Pair p=new Pair(this.blackBoard,this.whiteBoard,currentTurn);
		this.setChequerState(pos,currentTurn);
		this.doFlips(pos,currentTurn);
		this.inverseTurn();
		return p;
	}
	
	public void restore(Pair p)
	{
		this.blackBoard=p.blackBoard;
		this.whiteBoard=p.whiteBoard;
		this.currentTurn=p.turn;
	}
	
	public void startGame()
	{
		this.gameStart=true;
	}
	
	public boolean isGameStart()
	{
		return this.gameStart;
	}
	
	/**
	 * get the state of the specified chequer
	 * @param i the subscript of row
	 * @param j the subscript of column
	 * @return the state of the specified chequer
	 */
	public ChequerState getChequerState(int i,int j)
	{
		int shift=63-(j+(i<<3)); //63-(j+i*8);
		if ( ( blackBoard & (0x1L<<shift) )!=0 )
			return ChequerState.Black;
		else if( ( whiteBoard & (0x1L<<shift))!=0 )
			return ChequerState.White;
		else
			return ChequerState.Empty;
	}
	
	public ChequerState getChequerState(int pos)
	{
		int shift=63-(pos); 
		if ( ( blackBoard & (0x1L<<shift) )!=0 )
			return ChequerState.Black;
		else if( ( whiteBoard & (0x1L<<shift))!=0 )
			return ChequerState.White;
		else
			return ChequerState.Empty;
	}
	/**
	 * set the state of the specified chequer
	 * @param i the subscript of the row
	 * @param j the subscript of the column
	 * @param state the new state
	 */
	public void setChequerState(int i,int j,ChequerState state)
	{
		int shift=63-(j+(i<<3));          //64-(j+i*8);
		if(state==ChequerState.Black)
		{
			blackBoard |= (0x1L<<shift);
			whiteBoard &= ONES_64 ^ (0x1L<<shift);
		}
		else if(state==ChequerState.White)
		{
			whiteBoard |= (0x1L<<shift);
			blackBoard &= ONES_64 ^ (0x1L<<shift);
		}
		else if(state==ChequerState.Empty)
		{
			long tmp=ONES_64^(0x1L<<shift);
			blackBoard &= tmp;
			whiteBoard &= tmp;
		}
	}
	
	public void setChequerState(int pos,ChequerState state)
	{
		int shift=63-pos;          
		if(state==ChequerState.Black)
		{
			blackBoard |= (0x1L<<shift);
			whiteBoard &= ONES_64 ^ (0x1L<<shift);
		}
		else if(state==ChequerState.White)
		{
			whiteBoard |= (0x1L<<shift);
			blackBoard &= ONES_64 ^ (0x1L<<shift);
		}
		else if(state==ChequerState.Empty)
		{
			long tmp=ONES_64^(0X1L<<shift);
			blackBoard &= tmp;
			whiteBoard &= tmp;
		}
	}
	

	
	/**
	 * set the specified chequer to blank
	 * @param i the subscript of the row
	 * @param j the subscript of the column
	 */
	public void setEmpty(int i,int j)
	{
		setChequerState(i,j,ChequerState.Empty);
	}
	
	public int getFirstValidMove(long validMoves)
	{
		int first=Long.numberOfLeadingZeros(validMoves);
		if(first<64)
			return first;
		else
			return -1;
	}
	
	public int getNextMove(long validMoves,int prePos)
	{
		prePos+=1;
		if(prePos<64)
		{
			prePos=Long.numberOfLeadingZeros(validMoves&(ONES_64>>>prePos));
			if(prePos<64)
				return prePos;
			else
				return -1;
		}
		else
			return -1;
	}
	
	public void setEmpty(int pos)
	{
		setChequerState(pos,ChequerState.Empty);
	}
	/**
	 * set the specified chequer to white
	 * @param i the subscript of the row
	 * @param j the subscript of the column
	 */
	public void setWhite(int i,int j)
	{
		setChequerState(i,j,ChequerState.White);
	}
	public void setWhite(int pos)
	{
		setChequerState(pos,ChequerState.White);
	}
	/**
	 * set the specified chequer to black
	 * @param i the subscript of the row
	 * @param j the subscript of the column
	 */
	public void setBlack(int i,int j)
	{
		setChequerState(i,j,ChequerState.Black);
	}
	
	public void setBlack(int pos)
	{
		setChequerState(pos,ChequerState.Black);
	}
	
	public boolean isOccupid(int pos)
	{
		return isBlackOccupied(pos) || isWhiteOccupied(pos);
	}
	
	public boolean isBlackOccupied(int pos)
	{
		return (blackBoard & (0x1L<<(63-pos)) ) != 0;
	}
	
	public boolean isWhiteOccupied(int pos)
	{
		return (whiteBoard & (0x1L<<(63-pos)) ) != 0;
	}
	
	public int stableChequers(ChequerState turn)
	{
		return 0;
	}
	
	public int mobility(ChequerState turn)
	{
		return Long.bitCount(this.getValidMoves(turn));
	}
	
	public double evaluation()
	{
		if(this.getTotalDiscs()<50)
		{
			if(this.currentTurn==ChequerState.Black)
				return ( this.mobility(ChequerState.Black)
				-this.mobility(ChequerState.White) ) ;
				//+ this.getWeightFor(this.currentTurn);
			else
				return ( this.mobility(ChequerState.White)
				-this.mobility(ChequerState.Black) );
				//+this.getWeightFor(this.currentTurn);
		}
		else
		{
			if(this.currentTurn==ChequerState.Black)
				return this.sumBlack()-this.sumWhite();
			else
				return this.sumWhite()-this.sumBlack();
		}
	}
	
	public double getWeightFor(ChequerState turn)
	{
		if(turn==ChequerState.Black)
		{
			return getWeight(this.blackBoard);
		}
		else if(turn==ChequerState.White)
		{
			return getWeight(this.whiteBoard);
		}
		else
			return 0;
	}
	
	private double getWeight(long pos)
	{
		long v=0;
		int shift=Long.numberOfLeadingZeros(pos);
		long mask=0x8000000000000000L;
		mask>>>=shift;
		while( (shift&0xFFC0)==0 && (pos<<shift)!=0)
		{
			if( (pos & mask )!=0 )
			{
				 v+=staticEval[shift];
			}
			mask>>>=1;
			shift+=1;
		}
		return v;
	}
	
	public int sumWhite()
	{
		return Long.bitCount(this.whiteBoard);
	}
	
	public long debug()
	{
		return this.blackBoard & this.whiteBoard; 
	}
	
	public int getBlackMoves()
	{
		return this.blackMoves;
	}
	
	public int getWhiteMoves()
	{
		return this.whiteMoves;
	}
	
	public int sumBlack()
	{
		return Long.bitCount(this.blackBoard);
	}
	
	public boolean isDraw()
	{
		return sumBlack()==sumWhite();
	}
	
	public boolean isWinner(ChequerState turn)
	{
		if(turn==ChequerState.Black)
		{
			return sumBlack()>sumWhite();
		}
		else if(turn==ChequerState.White)
		{
			return sumWhite()>sumBlack();
		}
		return false;
	}
	
	
	protected void setCurrentTurn(ChequerState turn)
	{
		this.currentTurn=turn;
	}
	
	public ChequerState getCurrentTurn()
	{
		return this.currentTurn;
	}
	
	public int getTotalDiscs()
	{
		return Long.bitCount(blackBoard)
		+Long.bitCount(whiteBoard);
	}
	
	public ArrayList<Pair> getUndoList()
	{
		return this.steps;
	}
	
	public long getEmptyLocations()
	{
		return (blackBoard | whiteBoard) ^ ONES_64;
	}
	
	public void doFlips(int i,int j,ChequerState turn)
	{
		doFlips((i<<3)+j,turn);
	}
	
	public boolean canMove(int pos,ChequerState turn)
	{
		boolean ret=false;
		int directions=dirMask[pos];
		if((directions & 0x80) !=0)
			ret|=directionFlipable(directionInc[0],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x40) !=0)
			ret|=directionFlipable(directionInc[1],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x20) !=0)
			ret|=directionFlipable(directionInc[2],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x10) !=0)
			ret|=directionFlipable(directionInc[3],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x08) !=0)
			ret|=directionFlipable(directionInc[4],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x04) !=0 )
			ret|=directionFlipable(directionInc[5],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x02 ) !=0 )
			ret|=directionFlipable(directionInc[6],pos,turn);
		if(ret)
			return true;
		if( (directions & 0x01 ) !=0 )
			ret|=directionFlipable(directionInc[7],pos,turn);
		return ret;
	}
	
	public void doFlips(int pos,ChequerState turn)
	{
		int directions=dirMask[pos];
		if((directions & 0x80) !=0)
			directionFlips(directionInc[0],pos,turn);
		
		if( (directions & 0x40) !=0)
			directionFlips(directionInc[1],pos,turn);
		
		if( (directions & 0x20) !=0)
			directionFlips(directionInc[2],pos,turn);
		
		if( (directions & 0x10) !=0)
			directionFlips(directionInc[3],pos,turn);
		
		if( (directions & 0x08) !=0)
			directionFlips(directionInc[4],pos,turn);
		
		if( (directions & 0x04) !=0 )
			directionFlips(directionInc[5],pos,turn);
		
		if( (directions & 0x02 ) !=0 )
			directionFlips(directionInc[6],pos,turn);
		
		if( (directions & 0x01 ) !=0 )
			directionFlips(directionInc[7],pos,turn);
	}
	
	private int directionFlips(int directionInc,int pos,ChequerState turn)
	{
		ChequerState state;
		int cnt=0;
		int start=pos;
		if(turn==ChequerState.Black)
		{
			state=ChequerState.White;
			for(start+=directionInc;  ; start+=directionInc)
			{
				if( (state=this.getChequerState(start))!=ChequerState.White 
					|| isBoundaryForDirection(start,directionInc)	)
				 break;
				else
					cnt++;
			}
			if(cnt!=0 && state==ChequerState.Black)
			{
				for(start-=directionInc;start!=pos;start-=directionInc)
				{
					setBlack(start);
				}
			}
		}
		else if(turn==ChequerState.White)
		{
			state=ChequerState.Black;
			for(start+=directionInc;  ; start+=directionInc)
			{
				if( (state=this.getChequerState(start)) != ChequerState.Black
						|| isBoundaryForDirection(start,directionInc)	)
				 break;
				else
					cnt++;
			}
			if(cnt!=0 && state==ChequerState.White)
			{
				for(start-=directionInc;start!=pos;start-=directionInc)
				{
					setWhite(start);
				}
			}
		}
		return cnt; 
	}
	
	private boolean isBoundaryForDirection(int pos,int directionInc)
	{
		
		if(directionInc==NORTH)
		{
			return ( pos & 0xF8 ) ==0;
		}
		else if( directionInc == SOUTH)
		{
			return  ( ( pos & 0x38 ) ^ 0x38 ) == 0;
		}
		else if( directionInc == WEST )
		{
			return (pos & 0x07) ==0;
		}
		else if( directionInc== EAST )
		{
			return ( ( pos & 0x07 ) ^ 0x07 )==0;
		}
		else
		return ( ( pos & 0xF8 ) ==0 ) //up boundary
		        ||( (pos & 0x07) ==0 ) //left boundary
				||( ( ( pos & 0x38 ) ^ 0x38 ) == 0 ) //bottom boundary
				||( ( ( pos & 0x07 ) ^ 0x07 )==0 );    //right boundary
	}
	
	public long getValidMoves(ChequerState turn)
	{
		long v=0;
		long emptyLoc=getEmptyLocations();
		int shift=Long.numberOfLeadingZeros(emptyLoc);
		long mask=0x8000000000000000L;
		mask>>>=shift;
		while( (shift&0xFFC0)==0 && (emptyLoc<<shift)!=0)
		{
			if( (emptyLoc & mask )!=0 )
			{
				if(canMove(shift,turn))
					v|=mask;
			}
			mask>>>=1;
			shift+=1;
		}
		return v;
	}
	
	public boolean directionFlipable(int directionInc,int pos,ChequerState turn)
	{
		ChequerState state;
		int cnt=0;
		int start=pos;
		if(turn==ChequerState.Black)
		{
			state=ChequerState.White;
			for(start+=directionInc;  ; start+=directionInc)
			{
				if((state=this.getChequerState(start))!=ChequerState.White
						|| isBoundaryForDirection(start,directionInc)	)
				 break;
				else
					cnt++;
			}
			if( cnt!=0 && state==ChequerState.Black )
			{
				return true;
			}
		}
		else if(turn==ChequerState.White)
		{
			state=ChequerState.Black;
			for(start+=directionInc;  ; start+=directionInc)
			{
				if((state=this.getChequerState(start))!=ChequerState.Black
						|| isBoundaryForDirection(start,directionInc)	)
				 break;
				else
					cnt++;
			}
			if( cnt !=0 && state==ChequerState.White )
			{
				return true;
			}
		}
		return false; 
	}
	
	public int getRandomMove()
	{
		long v=this.getValidMoves(this.currentTurn);
		int ran=(int)(Math.random()*Long.bitCount(v));
		int pos=this.getFirstValidMove(v);
		while(ran>0)
		{
			pos=this.getNextMove(v,pos);
			ran--;
		}
		return pos;
	}
	
	public boolean gameOver()
	{
		if( !hasValidMove(currentTurn) 
				&& !hasValidMove(ChequerState.inverseState(currentTurn)) )
			return true;
		return false;
	}
	
	public boolean isCurrentTurnHasValidMove()
	{
		return hasValidMove(this.currentTurn);
	}
	
	public boolean hasValidMove(ChequerState turn)
	{
		return Long.bitCount(this.getValidMoves(turn))>0;
	}
	
	public boolean isMoveValid(int pos)
	{
		if(this.gameStart)
			return !this.isOccupid(pos) && this.canMove(pos,currentTurn);
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
		Pair p=new Pair(this.blackBoard,this.whiteBoard,currentTurn);
		steps.add(p);
		this.indexOfStep=steps.size()-1;
	}
	
	public boolean canUndo()
	{
		return this.indexOfStep>=1;
	}
	public boolean undo()
	{
		if(canUndo())
		{
			Pair p=this.steps.get(--indexOfStep);
			this.blackBoard=p.blackBoard;
			this.whiteBoard=p.whiteBoard;
			currentTurn=p.turn;
			return true;
		}
		else
			return false;
	}
	
	public boolean canRedo()
	{
		if(this.indexOfStep<this.steps.size()-1)
		{
			return true;
		}
		else
			return false;
	}	
	
	public boolean redo()
	{
		if(canRedo())
		{
			Pair p=this.steps.get(++indexOfStep);
			this.blackBoard=p.blackBoard;
			this.whiteBoard=p.whiteBoard;
			return true;
		}
		else
			return false;
	}
	public void reset()
	{
		this.blackBoard=0;
		this.whiteBoard=0;
		this.currentTurn=ChequerState.Black;
		this.gameStart=false;
		this.whiteMoves=0;
		this.blackMoves=0;
		this.setBlack(3,4);
		this.setBlack(4,3);
		this.setWhite(3,3);
		this.setWhite(4,4);
		this.indexOfStep=0;
		this.steps.clear();
		this.steps.add(new Pair(this.blackBoard
				,this.whiteBoard
				,this.currentTurn));
	}
	public void inverseTurn()
	{
		this.currentTurn = ChequerState.inverseState( this.currentTurn );
	}
	
	
	public String toChessString(long l)
	{
		String str=this.getClass()+"(Empty-0,Occupied=1)\n";
		long mask=0x8000000000000000L;
		for(int i=0;i<8;i++)
		{
			str+="|";
			for(int j=0;j<8;j++)
			{
				if( ( l & ( mask >>> ( i*8+j ) ) ) !=0 )
					str+="1 ";
				else
					str+="0 ";
			}
			str+="|\n";
		}
		return str;
	}
	
	public String toString()
	{
		String str=this.getClass()+"(Empty-0,White=1,Black=2)\n";
		for(int i=0;i<8;i++)
		{
			str+="|";
			for(int j=0;j<8;j++)
			{
				ChequerState state=this.getChequerState(i,j);
				if(state==ChequerState.Black)
					str+=2+" ";
				else if(state==ChequerState.White)
					str+=1+" ";
				else
					str+=0+" ";
			}
			str+="|\n";
		}
		return str;
	}
	
	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(obj==this)
			return true;
		if( obj.getClass() != this.getClass() )
			return false;
		Chessboard c=(Chessboard)obj;
		return c.blackBoard == this.blackBoard
		        && c.whiteBoard == this.whiteBoard;
	}
}