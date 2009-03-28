package cn.edu.nju.elegate;

public class Algorithms 
{
	private static Learn learn = new Learn();
	/**
	 * min max algorithm
	 * @param depth the depth of the search
	 * @param board the chess board
	 * @param turn  whose turn?computer or person
	 * @param state the chequer state associated with the current turn 
	 * @param depthMax  the max depth
	 * @param r         return the best move
	 * @return          the best move's evaluation
	 */
	public static double minMax(int depth,Chessboard board,
			int turn,Chessboard.ChequerState state,final int depthMax,Result r)
	{
		  return 0;                                        //return the best value
	}
	/**
	 * negative max for opponent
	 * @param depth the depth of the search
	 * @param board the chess board
	 * @param state the chequer state associated with the current turn 
	 * @param depthMax the maximum search depth
	 * @param r        the best move
	 * @return         the best move's evaluation
	 */
	public static double negaMax(int depth,Chessboard board,
			Chessboard.ChequerState state,final int depthMax,Result r)
	{
		double best=Integer.MIN_VALUE;
	    if(depth == 0 )                             
	    {
		    return board.evaluation();
	    }
	    long v=board.getValidMoves(state);
		int firstMove=board.getFirstValidMove(v);
		if(firstMove==-1)
		{
			board.inverseTurn();
			double value = -negaMax(depth - 1,board,Chessboard.ChequerState.inverseState(state),depthMax,r); // 注意这里有个负号。
			if (value > best)
			{ 
				best = value; 
			}
			return best;
		}
		while(firstMove!=-1)
		{
			Chessboard.Pair p=board.simMove(firstMove);
			double value = -negaMax(depth - 1,board,Chessboard.ChequerState.inverseState(state),depthMax,r); // 注意这里有个负号。
			board.restore(p);
			if (value > best)
			{ 
				best = value; 
				if(depth==depthMax)
				{
					r.setValue(best);
					r.setPos(firstMove);
					//System.out.println("\t\t\t\t\thello,depth="+depth+",move="+firstMove+",value="+value+",best="+best);
				}
			}
			firstMove=board.getNextMove(v,firstMove);
		}
		return best;       //the best move
	}
	/**
	 * alpha beta pruning algorithm
	 * @param depth the search depth
	 * @param board the chess board
	 * @param alpha the alpha value
	 * @param beta  the beta value
	 * @param state the chequer state associated with the current turn
	 * @param depthMax the maximum search depth
	 * @param r        the best move
	 * @return         the best move's evaluation
	 */
	public static double alphaBeta(int depth,Chessboard board,double alpha,double beta,
			Chessboard.ChequerState state,final int depthMax,Result r)
	{
			if (depth == 0) 
			{ 
				return learn.pattern_evaluation(board,board.getTotalDiscs());
			} 
			long v=board.getValidMoves(state);
			int firstMove=board.getFirstValidMove(v);
			
			if(firstMove==-1)
			{
				Chessboard.ChequerState turn=board.getCurrentTurn();
				board.inverseTurn();
				double value = -alphaBeta(depth - 1,board,-beta,-alpha,Chessboard.ChequerState.inverseState(state),depthMax,r); // 注意这里有个负号。
				board.setCurrentTurn(turn);
				if(value >= beta) 
				{ 
				   return beta; 
				} 
				if(value > alpha) 
				{ 
				   	alpha = value;
				}
				return alpha;
			}
			while (firstMove!=-1)
			{ 
			   Chessboard.Pair p=board.simMove(firstMove);
			   double val = -alphaBeta(depth - 1, board,-beta
					   , -alpha
					   ,Chessboard.ChequerState.inverseState(state)
					   ,depthMax,r); 
			   board.restore(p);
			   if(val >= beta) 
			   { 
				  if(depth==depthMax)
				  {
						r.setValue(val);
						r.setPos(firstMove);
				  }
			     return beta; 
			   } 
			   if(val > alpha) 
			   { 
			   	 alpha = val;
			   	 if(depth==depthMax)
				 {
			   		 r.setValue(alpha);
			   		 r.setPos(firstMove);
				 }
			   }
			   firstMove=board.getNextMove(v,firstMove);
			 }
			 return alpha; 
	}
}




final class Result
{
	private double value;
	private int pos;
	public Result()
	{
		value=Integer.MIN_VALUE;
		pos=-1;
	}
	public Result(double value,int pos)
	{
		this.value=value;
		this.pos=pos;
	}
	
	public double getValue()
	{
		return this.value;
	}
	public void setValue(double value)
	{
		this.value=value;
	}
	public int getPos()
	{
		return this.pos;
	}
	public void setPos(int pos)
	{
		this.pos=pos;
	}
	
	public String toString()
	{
		return this.getClass()+"[value="+value+",pos="+pos+"]";
	}
}
