package cn.edu.nju.shirui;

public class ChessBoard
{
/*  大家参照这副图来理解：
    ddddddddd
    dxxxxxxxx 10..17
    dxxxxxxxx 19..26
    dxxxxxxxx 28..35
    dxxxxxxxx 37..44
    dxxxxxxxx 46..53
    dxxxxxxxx 55..62
    dxxxxxxxx 64..71 
    dxxxxxxxx 73..80 
    dddddddddd 

    d 表示虚拟的(dummy), x 表示空/黑/白
    这样 A1 就是 board[10], H8 就是 board[80]
    棋格(a,b) = board[10+a+b*9] (0<= a,b <=7)
    但是board[0/9/18]等许多位置好像都浪费了(上图用d表示这些位置)。
    其实之所以用虚拟的dummy是要避免超界检查,提高速度。
    这样就不需要做"if (i > 1) and (j < 8)"判断了。    */
    
	private int[] board;
    private final static int arraySize = 91;
    
    private final static int EMPTY = 0;
    private final static int BLACK = -1;
    private final static int WHITE = 1;
    
//  DirInc: array[0..8] of int
//  (1, -1, 8, -8, 9, -9, 10, -10, 0);
//  (r, l ,bl, tr, b, t, br, tl, N)
//  (→,←,↙, ↗,↓, ↑, ↘, ↖, .)
//   判断八个方向的棋子是什么，这样写:
//   for Dir:=0 to 7 do
//       Result:=Board[I + DirInc[Dir]]    
    private int[] dirInc = { 1, -1, 8, -8, 9, -9, 10, -10, 0 };
//    private final static int R  =   1;
//    private final static int L  =  -1;
//    private final static int BL =   8;
//    private final static int TR =  -8;
//    private final static int B  =   9;
//    private final static int T  =  -9;
//    private final static int BR =  10;
//    private final static int TL = -10;
//    private final static int N  =   0;
    private int[] dirMask = 
                {   0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 81, 81, 87, 87, 87, 87, 22, 22,
                    0, 81, 81, 87, 87, 87, 87, 22, 22,
                    0, 121, 121, 255, 255, 255, 255, 182, 182,
                    0, 121, 121, 255, 255, 255, 255, 182, 182,
                    0, 121, 121, 255, 255, 255, 255, 182, 182,
                    0, 121, 121, 255, 255, 255, 255, 182, 182,
                    0, 41, 41, 171, 171, 171, 171, 162, 162,
                    0, 41, 41, 171, 171, 171, 171, 162, 162,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0 
                 };
                // 87 = 1010111
                //  (→,←,↙, ↗,↓, ↑, ↘, ↖, .)
    
    public ChessBoard()
    {
        board = new int[arraySize];
        for(int i=0; i<arraySize; i++)
            board[i] = EMPTY;
    }

    public void setBoard(int a, int b, int state)
    {
        this.board[10+a+b*9] = state;
    }
    
    public int getBoard(int a, int b)
    {
        return board[10+a+b*9];
    }
    
    private int[] globalFlipStack = new int[2048];
    private int flipStack;
    
    // 原稿中sq为指针
    public void drctnlFlips(int sq, int aInc, int sideToGo, int oppSide)
    {
        int pt = sq;
        pt += aInc;
        if(board[pt]==oppSide)
        {
            pt += aInc;
            if(board[pt]==oppSide)
            {
                pt += aInc;
                if(board[pt]==oppSide)
                {
                    pt += aInc;
                    if(board[pt]==oppSide)
                    {
                        pt += aInc;
                        if(board[pt]==oppSide)
                        {
                            pt += aInc;
                            if(board[pt]==oppSide)
                                pt += aInc;
                        }
                    }
                }
            }
            if(board[pt]==sideToGo)
            {
                pt -= aInc;
                while(pt!=(sq))
                {
                    board[pt] = sideToGo;
                    globalFlipStack[flipStack] = pt;
                    flipStack ++;
                    pt -= aInc;
                }
            }
        }
    }

    public void undoFlips(int flipCount, int oppSide)
    {
        if((flipCount&1) != 0)
        { 
            flipCount --;
            flipStack --;
            board[globalFlipStack[flipStack]] = oppSide;
        }
        while(flipCount!=0)
        {
            flipCount -= 2;
            flipStack --;
            board[globalFlipStack[flipStack]] = oppSide;
            flipStack --;
            board[globalFlipStack[flipStack]] = oppSide;
        }
    }

    // 所有方向上的翻转这样写：整个棋盘翻转棋子并且返回翻转的数目
    public int doFlips(int sqNum, int sideToGo, int oppSide)
    {
        int j = dirMask[sqNum];
        int oldFlipStack = flipStack;
        int sq = sqNum;
        if((j&128) != 0)
        {
            drctnlFlips(sq, dirInc[7], sideToGo, oppSide);
        }
        if((j&64) != 0)
        {
            drctnlFlips(sq, dirInc[6], sideToGo, oppSide);
        }
        if((j&32) != 0)
        {
            drctnlFlips(sq, dirInc[5], sideToGo, oppSide);
        }
        if((j&16) != 0)
        {
            drctnlFlips(sq, dirInc[4], sideToGo, oppSide);
        }
        if((j&8) != 0)
        {
            drctnlFlips(sq, dirInc[3], sideToGo, oppSide);
        }
        if((j&4) != 0)
        {
            drctnlFlips(sq, dirInc[2], sideToGo, oppSide);
        }
        if((j&2) != 0)
        {
            drctnlFlips(sq, dirInc[1], sideToGo, oppSide);
        }
        if((j&1) != 0)
        {
            drctnlFlips(sq, dirInc[0], sideToGo, oppSide);
        }
        return (flipStack - oldFlipStack);
    }
    
    int[] worst2best =
            {
              /*B2*/ 20, 25, 65, 70,
              /*B1*/ 11, 16, 19, 26, 64, 71, 74, 79,
              /*C2*/ 21, 24, 29, 34, 56, 61, 66, 69,
              /*D2*/ 22, 23, 38, 43, 47, 52, 67, 68,
              /*D3*/ 31, 32, 39, 42, 48, 51, 58, 59,
              /*D1*/ 13, 14, 37, 44, 46, 53, 76, 77,
              /*C3*/ 30, 33, 57, 60,
              /*C1*/ 12, 15, 28, 35, 55, 62, 75, 78,
              /*A1*/ 10, 17, 73, 80,
              /*D4*/ 40, 41, 49, 50
            };
        
//    procedure PrepareToSolve(Board: TAIBoard);
//    var
//      I, SQNum: Integer;
//      K: Cardinal;
//      PT: PEmList;
//    begin
//       (* 创建空格列表: *)
//      K := 0;
//      PT := @EmHead;
//      for I := 60 - 1 downto 0 do
//      begin
//        SQNum := Worst2Best[I];
//        if Board[SQNum] = EMPTY then
//        begin
//          PT.Succ := @Ems[K];
//          Ems[K].Pred := PT;
//          Inc(K);
//          PT := PT.Succ;
//          PT.Square := SQNum;
//          PT.Hole_ID := 0;
//        end;
//      end;
//      PT.Succ := nil;
//    end;        
        
    public void prepareToSolve()
    {
        int k = 0;
        for(int i=60; i>0; i--)
        {
            sqNum = worst2best[i];
            if(board[sqNum] == EMPTY)
            {
                
            }
        }
    }
    
    public String toString()
    {
        String result = "| ";
        for(int i=0; i<arraySize; i++)
        {
            if(i>=10 && i<=80)
            {
                if(i%9==0)
                {
                    result += "|\n| ";
                }
                else if(board[i]==EMPTY)
                {
                    result += "EMPTY ";
                }
                else if(board[i]==BLACK)
                {
                    result += "BLACK ";
                }
                else if(board[i]==WHITE)
                {
                    result += "WHITE ";
                }
                else{}
            }
        }
        result += "|\n";
        return result;
    }   

}
