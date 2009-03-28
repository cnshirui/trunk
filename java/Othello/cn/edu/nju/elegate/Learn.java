package cn.edu.nju.elegate;



import java.io.*;



public class Learn {
	static final int MAX_BLOCKS=200;
	static final int WHITESQ=2;
	static final int BLACKSQ=0;
	static final String MIRROR_ERROR="Mirror Error";
	static final String MEMORY_ERROR="Memory Allocated Error";
	static final int[] pow3 = { 1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683,59049 };
	static final int EMPTY=1;
	
	/* An evaluation indicating a won midgame position where no
	   player has any moves available. */

	
    static final String FILE_NAME="resources/config/coeff.bin";
	
	static int[] flip8=Pattern.flip8; 
	//find flip has been ???
	 static int stage_count=12;

	 
	 
	 static CoeffSet[] set=new CoeffSet[12];
	 static PatAppearence[] pat=new PatAppearence[60];
	 
	 static CoeffCount[] coeffcount=new CoeffCount[stage_count];
	 static int pCoeffcount=1;
	 static final int EPOCHLENGTH=10;
	 
	 
	 
	 
	 static final int EPOCH=20;//the total count of epoches is 100;
	 static 
	 {
		 for(int i=0;i<60;i++){
			 pat[i]=new PatAppearence();
		 }
	 }
	 static
	 {
		  for(int i=0;i<12;i++)
	        {
	        	set[i]=new CoeffSet();
	        }
		  //readFromFile();//that means the coeff file should be exists;
	 }
	 /*static
	 {
		 
			 for(int j=0;j<stage_count;j++)
	        	coeffcount[j]=new CoeffCount();
	       
	 }*/
	public Learn(){
		
        
        readFromFile();
        //Mouse();
        //hehe  finally I finished
		System.out.println("just a test "+set[4].afile2x[34]);
	}
	/*static void Mouse(){
		Chessboard aboard=new Chessboard();
		
		//writeToFile();
		for(int i=0;i<EPOCH;i++){
			for(int k=0;k<stage_count;k++){
				initialCoeffcount(coeffcount[k]);
			}
//			to initial current coeffcount to sum the gt)
			for (int j=0;j<EPOCHLENGTH;j++){
				aboard.reset();
				System.out.println("game "+j+" ");
				selfPlay(aboard);
		
				
			}
			changeParameter(EPOCHLENGTH);
			writeToFile();
			System.out.println("now epoch "+i+" end");
		}
	}*/
	 void initialCoeffcount(CoeffCount current){
		for(int i=0;i<59049;i++){
			  current.vafile2x[i]=0;
			  current.cafile2x[i]=0;
			  current.vconner52[i]=0;
			  current.ccorner52[i]=0;
			  if(i<19683){
				  current.vconner33[i]=0;
				  current.ccorner33[i]=0;
			  }
			  if(i<6561){
				  current.vbfile[i]=0;
				  current.vcfile[i]=0;
				  current.vdfile[i]=0;
				  current.vdiag8[i]=0;
				  current.cbfile[i]=0;
				  current.ccfile[i]=0;
				  current.cdfile[i]=0;
				  current.cdiag8[i]=0;
			  }
			  if(i<2187){
				  current.vdiag7[i]=0;
				  current.cdiag7[i]=0;
				  
			  }
			  if(i<729){
				  current.vdiag6[i]=0;
				  current.cdiag6[i]=0;
			  }
			  if(i<243){
				  current.vdiag5[i]=0;
				  current.cdiag5[i]=0;
			  }
			  if(i<81){
				  current.vdiag4[i]=0;
				  current.cdiag4[i]=0;
			  }
		}
		
	}
	 void selfPlay(Chessboard aboard){
		int RandomMoves=8;
		int depth=8;
		int position=0;
		Result result=new Result();
		for(int i=0;i<60;i++){
			pat[i].reSet();
		}
		aboard.startGame();
        for(int i=0;i<RandomMoves;i++){
        	
        	position=aboard.getRandomMove();
        	if(position==-1){
        		if(aboard.gameOver()==false){
        			i--;
        			aboard.inverseTurn();
            		continue;
        		}
        		else
        			return;
        	}
        	System.out.println("step "+i+" "+position);
        	
        	aboard.simMove(position);
        	countPattern(aboard,aboard.getTotalDiscs());
        }
        while(aboard.gameOver()==false)
        {
        	//System.out.println("CurrentTurn:"+aboard.getCurrentTurn());
        	Algorithms.alphaBeta(depth,aboard,Integer.MIN_VALUE,
        			Integer.MAX_VALUE,aboard.getCurrentTurn(),
        			depth,result);
        	//System.out.println("result:"+result);
        	if((position=result.getPos())!=-1)
        	{
        		System.out.println("step "+aboard.getTotalDiscs()+" "+position);
        		aboard.simMove(position);
        		countPattern(aboard,aboard.sumBlack()+aboard.sumWhite());
        	}
        	else
        	{
        		//System.out.println("CurrentTurn:"+aboard.getCurrentTurn());
        		aboard.inverseTurn();
        		//System.out.println("Turn:"+aboard.getCurrentTurn());
        	}
        	result.setPos(-1);
        	//if(aboard.getTotalDiscs()>40)
        	//	System.out.println("board="+aboard+",game over="+aboard.gameOver());
        }
        countPattern(aboard,aboard.getTotalDiscs());
        System.out.println("result: black "+aboard.sumBlack()+" white "+aboard.sumWhite());
        recordError(aboard.sumBlack()-aboard.sumWhite());
	}
	 void recordError(int error){
		
		int k=0;
		
		for(int i=0;i<60;i++){
			double error1=(error-pat[i].val)/300;
			double error2=(error-pat[i].val)/150;
			System.out.println("error1 "+error1);
			System.out.println("error2 "+error2);
			System.out.println(pat[i].afile2x1);
			System.out.println(pat[i].afile2x2);
			System.out.println(pat[i].afile2x3);
			System.out.println(pat[i].afile2x4);
			System.out.println(pat[i].bfile1);
			System.out.println(pat[i].bfile2);
			System.out.println(pat[i].bfile3);
			System.out.println(pat[i].bfile4);
			System.out.println(pat[i].cfile1);
			System.out.println(pat[i].cfile2);
			System.out.println(pat[i].cfile3);
			System.out.println(pat[i].cfile4);
			System.out.println(pat[i].dfile1);
			System.out.println(pat[i].dfile2);
			System.out.println(pat[i].dfile3);
			System.out.println(pat[i].dfile4);
			System.out.println(pat[i].diag81);
			k=Math.round((float)((i/5)-0.5));
			System.out.println(k);
			System.out.println(pat[i].diag81);
			System.out.println(pat[i].diag82);
			coeffcount[k].vafile2x[pat[i].afile2x1]+=error2;
			coeffcount[k].vafile2x[pat[i].afile2x2]+=error2;
			coeffcount[k].vafile2x[pat[i].afile2x3]+=error2;
			coeffcount[k].vafile2x[pat[i].afile2x4]+=error2;
			coeffcount[k].vbfile[pat[i].bfile1]+=error1;
			coeffcount[k].vbfile[pat[i].bfile2]+=error1;
			coeffcount[k].vbfile[pat[i].bfile3]+=error1;
			coeffcount[k].vbfile[pat[i].bfile4]+=error1;
			coeffcount[k].vcfile[pat[i].cfile1]+=error1;
			coeffcount[k].vcfile[pat[i].cfile2]+=error1;
			coeffcount[k].vcfile[pat[i].cfile3]+=error1;
			coeffcount[k].vcfile[pat[i].cfile4]+=error1;
			coeffcount[k].vdfile[pat[i].dfile1]+=error1;
			coeffcount[k].vdfile[pat[i].dfile2]+=error1;
			coeffcount[k].vdfile[pat[i].dfile3]+=error1;
			coeffcount[k].vdfile[pat[i].dfile4]+=error1;
			coeffcount[k].vdiag8[pat[i].diag81]+=error2;
			coeffcount[k].vdiag8[pat[i].diag82]+=error2;
			coeffcount[k].vdiag7[pat[i].diag71]+=error1;
			coeffcount[k].vdiag7[pat[i].diag72]+=error1;
			coeffcount[k].vdiag7[pat[i].diag73]+=error1;
			coeffcount[k].vdiag7[pat[i].diag74]+=error1;
			coeffcount[k].vdiag6[pat[i].diag61]+=error1;
			coeffcount[k].vdiag6[pat[i].diag62]+=error1;
			coeffcount[k].vdiag6[pat[i].diag63]+=error1;
			coeffcount[k].vdiag6[pat[i].diag64]+=error1;
			coeffcount[k].vdiag5[pat[i].diag51]+=error1;
			coeffcount[k].vdiag5[pat[i].diag52]+=error1;
			coeffcount[k].vdiag5[pat[i].diag53]+=error1;
			coeffcount[k].vdiag5[pat[i].diag54]+=error1;
			coeffcount[k].vdiag4[pat[i].diag41]+=error1;
			coeffcount[k].vdiag4[pat[i].diag42]+=error1;
			coeffcount[k].vdiag4[pat[i].diag43]+=error1;
			coeffcount[k].vdiag4[pat[i].diag44]+=error1;
			coeffcount[k].vconner33[pat[i].corner331]+=error1;
			coeffcount[k].vconner33[pat[i].corner332]+=error1;
			coeffcount[k].vconner33[pat[i].corner333]+=error1;
			coeffcount[k].vconner33[pat[i].corner334]+=error1;
			coeffcount[k].vconner52[pat[i].corner521]+=error2;
			coeffcount[k].vconner52[pat[i].corner522]+=error2;
			coeffcount[k].vconner52[pat[i].corner523]+=error2;
			coeffcount[k].vconner52[pat[i].corner524]+=error2;
			coeffcount[k].vconner52[pat[i].corner525]+=error2;
			coeffcount[k].vconner52[pat[i].corner526]+=error2;
			coeffcount[k].vconner52[pat[i].corner527]+=error2;
			coeffcount[k].vconner52[pat[i].corner528]+=error2;
			}
	}
	 void changeParameter(int epochlength){
		  int i, j, k;
		  int mirror_pattern;
		  int[] row=new int[10];
		  int[] map_mirror3;
		  int[] map_mirror4;
		  int[] map_mirror5;
		  int[] map_mirror6;
		  int[] map_mirror7;
		  int[] map_mirror8;
		  int[] map_mirror33;
		  int[] map_mirror8x2;

		  /* Allocate the memory needed for the temporary mirror maps from the
		     heap rather than the stack to reduce memory requirements. */

		  map_mirror3 = new int[27];
		  map_mirror4 = new int[81];
		  map_mirror5 = new int[243];
		  map_mirror6 = new int[729];
		  map_mirror7 = new int[2187];
		  map_mirror8 = new int[6561];
		  map_mirror33 = new int[19683];
		  map_mirror8x2 = new int[59049];

		  /* Build the pattern tables for 8*1-patterns */

		  for ( i = 0; i < 8; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 6561; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 8; j++ )
		      mirror_pattern += row[j] * pow3[7 - j];
		    /* Create the symmetry map */
		    map_mirror8[i] = i<mirror_pattern ? i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1] == 0) && (j < 8) );
		  }

		  /* Build the tables for 7*1-patterns */

		  for ( i = 0; i < 7; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 2187; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 7; j++ )
		      mirror_pattern += row[j] * pow3[6 - j];
		    map_mirror7[i] = i<mirror_pattern ?i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1]) == 0 && (j < 7) );
		  }

		  /* Build the tables for 6*1-patterns */

		  for ( i = 0; i < 6; i++ )
		    row[i] = 0;
		  for ( i = 0; i < 729; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 6; j++ )
		      mirror_pattern += row[j] * pow3[5 - j];
		    map_mirror6[i] = i<mirror_pattern ? i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1]) == 0 && (j < 6) );
		  }

		  /* Build the tables for 5*1-patterns */

		  for ( i = 0; i < 5; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 243; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 5; j++ )
		      mirror_pattern += row[j] * pow3[4 - j];
		    map_mirror5[i] = i<mirror_pattern ? i :mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1] == 0) && (j < 5) );
		  }

		  /* Build the tables for 4*1-patterns */   

		  for ( i = 0; i < 4; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 81; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 4; j++ )
		      mirror_pattern += row[j] * pow3[3 - j];
		    map_mirror4[i] = i<mirror_pattern ? i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1]) == 0 && (j < 4) );
		  }

		  /* Build the tables for 3*1-patterns */   

		  for ( i = 0; i < 3; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 27; i++ ) {
		    mirror_pattern = 0;
		    for ( j = 0; j < 3; j++ )
		      mirror_pattern += row[j] * pow3[2 - j];
		    map_mirror3[i] = i<mirror_pattern ? i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1] == 0) && (j < 3) );
		  }

		  /* Build the tables for 5*2-patterns */

		  /* --- none needed --- */

		  /* Build the tables for edge2X-patterns */

		  for ( i = 0; i < 6561; i++ )
		    for ( j = 0; j < 3; j++ )
		      for ( k = 0; k < 3; k++ )
			map_mirror8x2[i + 6561 * j + 19683 * k] =
			  ( flip8[i] + 6561 * k + 19683 * j)<(i + 6561 * j + 19683 * k )?
					  (flip8[i] + 6561 * k + 19683 * j ):(i + 6561 * j + 19683 * k );	  

		  /* Build the tables for 3*3-patterns */

		  for ( i = 0; i < 9; i++ )
		    row[i] = 0;

		  for ( i = 0; i < 19683; i++ ) {
		    mirror_pattern =
		      row[0] + 3 * row[3] + 9 * row[6] +
		      27 * row[1] + 81 * row[4] + 243 * row[7] +
		      729 * row[2] + 2187 * row[5] + 6561 * row[8];
		    map_mirror33[i] = i<mirror_pattern ? i:mirror_pattern;

		    /* Next configuration */
		    j = 0;
		    do {  /* The odometer principle */
		      row[j]++;
		      if ( row[j] == 3 )
			row[j] = 0;
		      j++;
		    } while ( (row[j - 1] == 0) && (j < 9) );
		  }
		
		 for ( k=0;k<stage_count;k++){
			  adjustParameter( epochlength,set[k].afile2x,coeffcount[k].vafile2x, map_mirror8x2, 59049 );
			  adjustParameter( epochlength,set[k].bfile,coeffcount[k].vbfile, map_mirror8, 6561 );
			  adjustParameter( epochlength,set[k].cfile,coeffcount[k].vcfile, map_mirror8, 6561);
			  adjustParameter( epochlength,set[k].dfile,coeffcount[k].vdfile, map_mirror8, 6561 );
			  adjustParameter( epochlength,set[k].diag8,coeffcount[k].vdiag8, map_mirror8, 6561 );
			  adjustParameter( epochlength,set[k].diag7,coeffcount[k].vdiag7, map_mirror7, 2187 );
			  adjustParameter( epochlength,set[k].diag6,coeffcount[k].vdiag6, map_mirror6, 729 );
			  adjustParameter( epochlength,set[k].diag5,coeffcount[k].vdiag5, map_mirror5, 243);
			  adjustParameter( epochlength,set[k].diag4,coeffcount[k].vdiag4, map_mirror4, 81 );
			  adjustParameter(epochlength,set[k].corner33,coeffcount[k].vconner33, map_mirror33, 19683 );
			  adjustParameter(epochlength,set[k].corner52,coeffcount[k].vconner52,null,59049);
		 }
		  
		    
	}
	 void adjustParameter(int epochlength,double[] param,double[] item,int mirror[],int count){
		
		if(mirror!=null){
        //差值都累积在coeffcount[k]中
			
			for(int j=0;j<count;j++){
				if(item[j]!=0&&(mirror[j]!=j)){
					item[mirror[j]]+=item[j];
					item[j]+=item[mirror[j]];
					param[j]+=item[j]/epochlength;
					param[mirror[j]]+=item[mirror[j]]/epochlength;
					System.out.println(j);
					}
			}
		}
		else{
			for(int j=0;j<count;j++){
				param[j]+=item[j]/epochlength;
			}
		}
	   
		
		
	}
	
	 void writeToFile(){
		try{
			FileOutputStream fout=new FileOutputStream(new File(FILE_NAME));
			try{
				DataOutputStream  dout=new DataOutputStream(fout);
				
				
				generate_coeffs(dout);
				
				dout.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("create gzip file to write failed");
			}
			fout.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("create file failed");
		}
		
	}
	 void readFromFile(){
		try{
			FileInputStream fin=new FileInputStream(new File(FILE_NAME)); 
			
			try{
				DataInputStream din=new DataInputStream(fin);
			   
				unpack_coeffs(din);
				din.close();
			}
			catch(Exception ex){
				System.out.println("create gzip file to read failed");
			}
			fin.close();
		}
		catch(Exception ex){
			System.out.println("open file failed");
		}
	}
	
	
	
	 //set has not been initialed

	 
	 
	 void
	 terminal_patterns(CoeffSet aset) {
	   double result;
	   double[][] value=new double[8][8];
	   int i, j, k;
	   int[] row=new int [10];
	   int[][] hit=new int [8][8];

	   /* Count the number of times each square is counted */

	   for ( i = 0; i < 8; i++ )
	     for ( j = 0; j < 8; j++ )
	       hit[i][j] = 0;
	   for ( i = 0; i < 8; i++ ) {
	     hit[0][i]++;
	     hit[i][0]++;
	     hit[7][i]++;
	     hit[i][7]++;
	   }
	   for ( i = 0; i < 8; i++ ) {
	     hit[1][i]++;
	     hit[i][1]++;
	     hit[6][i]++;
	     hit[i][6]++;
	   }
	   for ( i = 0; i < 8; i++ ) {
	     hit[2][i]++;
	     hit[i][2]++;
	     hit[5][i]++;
	     hit[i][5]++;
	   }
	   for ( i = 0; i < 8; i++ ) {
	     hit[3][i]++;
	     hit[i][3]++;
	     hit[4][i]++;
	     hit[i][4]++;
	   }
	   for ( i = 0; i < 3; i++ )
	     for ( j = 0; j < 3; j++ ) {
	       hit[i][j]++;
	       hit[i][7 - j]++;
	       hit[7 - i][j]++;
	       hit[7 - i][7 - j]++;
	     }
	   for ( i = 0; i < 2; i++ )
	     for ( j = 0; j < 5; j++ ) {
	       hit[i][j]++;
	       hit[j][i]++;
	       hit[i][7 - j]++;
	       hit[j][7 - i]++;
	       hit[7 - i][j]++;
	       hit[7 - j][i]++;
	       hit[7 - i][7 - j]++;
	       hit[7 - j][7 - i]++;
	     }
	   for ( i = 0; i < 8; i++ ) {
	     hit[i][i]++;
	     hit[i][7 - i]++;
	   }
	   for ( i = 0; i < 7; i++ ) {
	     hit[i][i + 1]++;
	     hit[i + 1][i]++;
	     hit[i][6 - i]++;
	     hit[i + 1][7 - i]++;
	   }
	   for ( i = 0; i < 6; i++ ) {
	     hit[i][i + 2]++;
	     hit[i + 2][i]++;
	     hit[i][5 - i]++;
	     hit[i + 2][7 - i]++;
	   }
	   for ( i = 0; i < 5; i++ ) {
	     hit[i][i + 3]++;
	     hit[i + 3][i]++;
	     hit[i][4 - i]++;
	     hit[i + 3][7 - i]++;
	   }
	   for ( i = 0; i < 4; i++ ) {
	     hit[i][i + 4]++;
	     hit[i + 4][i]++;
	     hit[i][3 - i]++;
	     hit[i + 4][7 - i]++;
	   }
	   hit[1][1] += 4;
	   hit[1][6] += 4;
	   hit[6][1] += 4;
	   hit[6][6] += 4;
       hit[2][3]+=3;
       hit[2][4]+=3;
       hit[3][2]+=3;
       hit[3][3]+=3;
       hit[3][4]+=3;
       hit[3][5]+=3;
       hit[4][2]+=3;
       hit[4][3]+=3;
       hit[4][4]+=3;
       hit[4][5]+=3;
       hit[4][3]+=3;
       hit[4][4]+=3;
       hit[0][2]+=2;
       hit[0][6]+=2;
       hit[1][0]+=2;
       hit[1][7]+=2;
       hit[6][0]+=2;
       hit[6][7]+=2;
       hit[7][1]+=2;
       hit[7][6]+=2;
       hit[0][0]-=2;
       hit[0][7]-=2;
       hit[7][0]-=2;
       hit[7][7]-=2;
       
	   for ( i = 0; i < 8; i++ )
	     for ( j = 0; j < 8; j++ )
	       value[i][j] = 1.0 / hit[i][j];

	   for ( i = 0; i < 10; i++ )
	     row[i] = 0;

	   for ( i = 0; i < 59049; i++ ) {
	     /*59049次循环*/
	     result = 0.0;
	     for ( j = 0; j < 8; j++ )
	       if ( row[j] == BLACKSQ )
	 	result += value[0][j];
	       else if ( row[j] == WHITESQ )
	 	result += value[0][j];
	     if ( row[8] == BLACKSQ )
	       result += value[1][1];
	     else if ( row[8] == WHITESQ )
	       result += value[1][1];
	     if ( row[9] == BLACKSQ )
	       result += value[1][6];
	     else if ( row[9] == WHITESQ )
	       result -= value[1][6];
	     aset.afile2x[i] =    result   ;

	     result = 0.0;
	     for ( j = 0; j < 5; j++ )
	       for ( k = 0; k < 2; k++ )
	 	if ( row[5 * k + j] == BLACKSQ )
	 	  result += value[j][k];
	 	else if ( row[5 * k + j] == WHITESQ )
	 	  result += value[j][k];
	     aset.corner52[i] =     result   ;

	     if ( i < 19683 ) {
	       result = 0.0;
	       for ( j = 0; j < 3; j++ )
	 	for ( k = 0; k < 3; k++ )
	 	  if ( row[3 * j + k] == BLACKSQ )
	 	    result += value[j][k];
	 	  else if ( row[3 * j + k] == WHITESQ )
	 	    result += value[j][k];
	       aset.corner33[i] =    result   ;
	     }
	     if ( i < 6561 ) {
	       result = 0.0;
	       for ( j = 0; j < 8; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[1][j];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[1][j];
	       aset.bfile[i] =    result   ;
	          
	       result = 0.0;
	       for ( j = 0; j < 8; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[2][j];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[2][j];
	       aset.cfile[i] =  result  ;
	          
	       result = 0.0;
	       for ( j = 0; j < 8; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[3][j];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[3][j];
	       aset.dfile[i] =  result  + 0.5;
	          
	       result = 0.0;
	       for ( j = 0; j < 8; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[j][j];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[j][j];
	       aset.diag8[i] = result  + 0.5;
	     }
	     if ( i < 2187 ) {
	       result = 0.0;
	       for ( j = 0; j < 7; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[j][j + 1];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[j][j + 1];
	       aset.diag7[i] =  result  ;
	     }
	     if ( i < 729 ) {
	       result = 0.0;
	       for ( j = 0; j < 6; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[j][j + 2];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[j][j + 2];
	       aset.diag6[i] =  result ;
	     }
	     if ( i < 243 ) {
	       result = 0.0;
	       for ( j = 0; j < 5; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[j][j + 3];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[j][j + 3];
	       aset.diag5[i] = result  ;
	     }
	     if ( i < 81 ) {
	       result = 0.0;
	       for ( j = 0; j < 4; j++ )
	 	if ( row[j] == BLACKSQ )
	 	  result += value[j][j + 4];
	 	else if ( row[j] == WHITESQ )
	 	  result += value[j][j + 4];
	       aset.diag4[i] =  result  ;
	     }

	     /* Next configuration */
	     j = 0;
	     do {  /* The odometer principle */
	       row[j]++;
	       if ( row[j] == 3 )
	 	row[j] = 0;
	       j++;
	     } while ( (row[j - 1] == 0) && (j < 10) );
	   }
	   
	 }
	 
	
	
	
	 double 
	get_double(DataInputStream stream){
		  double val=0;
		  
	      if(stream==null){
	    	  System.out.println("stream is null");
	      }
		  try{
	    	  val=stream.readDouble();
	     }
	     catch(IOException ex){
	    	 ex.printStackTrace();
	     }
		  
		
		  

		  return val;
	}
	 void 
	write_double(DataOutputStream stream,double val){
       try{
			
			stream.writeDouble(val);
		}
		catch(IOException ex){
			System.out.println("write into file error");
		}
	}
	/**
	 * 
	 * @param item
	 * @param mirror
	 * @param count
	 * @param stream
	 */
	/*
	   UNPACK_BATCH
	   Reads feature values for one specific pattern
	*/

	 void
	unpack_batch( double[] item, int [] mirror, int count, DataInputStream stream ) {
	  int i;
	  double [] buffer;
	  
	  buffer = new double[count];
	  
	  /* Unpack the coefficient block where the score is scaled
	     so that 512 units corresponds to one disk. */

	  for ( i = 0; i < count; i++ )
	    if ( (mirror == null) || (mirror[i] == i) )
	      buffer[i] = get_double(stream);
	    else
	      buffer[i] = buffer[mirror[i]];

	  for ( i = 0; i < count; i++ )
	    item[i] = buffer[i];
	  if ( mirror != null )
	    for ( i = 0; i < count; i++ )
	      if ( item[i] != item[mirror[i]] ) {
		System.out.println( MIRROR_ERROR+"@"+
			i+ mirror[i]+"of "+count );
		System.out.println( item[i]+" <-->  "+ item[mirror[i]]);
		System.exit(1);
	      }
}

	

	/*
	   UNPACK_COEFFS
	   Reads all feature values for a certain stage. To take care of
	   symmetric patterns, mirror tables are calculated.
	*/

	 void
	unpack_coeffs( DataInputStream stream ) {
	  int i, j, k;
	  int mirror_pattern;
	  int[] row=new int[10];
	  int[] map_mirror3;
	  int[] map_mirror4;
	  int[] map_mirror5;
	  int[] map_mirror6;
	  int[] map_mirror7;
	  int[] map_mirror8;
	  int[] map_mirror33;
	  int[] map_mirror8x2;

	  /* Allocate the memory needed for the temporary mirror maps from the
	     heap rather than the stack to reduce memory requirements. */

	  map_mirror3 = new int[27];
	  map_mirror4 = new int[81];
	  map_mirror5 = new int[243];
	  map_mirror6 = new int[729];
	  map_mirror7 = new int[2187];
	  map_mirror8 = new int[6561];
	  map_mirror33 = new int[19683];
	  map_mirror8x2 = new int[59049];

	  /* Build the pattern tables for 8*1-patterns */

	  for ( i = 0; i < 8; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 6561; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 8; j++ )
	      mirror_pattern += row[j] * pow3[7 - j];
	    /* Create the symmetry map */
	    map_mirror8[i] = i<mirror_pattern ? i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1] == 0) && (j < 8) );
	  }

	  /* Build the tables for 7*1-patterns */

	  for ( i = 0; i < 7; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 2187; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 7; j++ )
	      mirror_pattern += row[j] * pow3[6 - j];
	    map_mirror7[i] = i<mirror_pattern ?i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1]) == 0 && (j < 7) );
	  }

	  /* Build the tables for 6*1-patterns */

	  for ( i = 0; i < 6; i++ )
	    row[i] = 0;
	  for ( i = 0; i < 729; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 6; j++ )
	      mirror_pattern += row[j] * pow3[5 - j];
	    map_mirror6[i] = i<mirror_pattern ? i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1]) == 0 && (j < 6) );
	  }

	  /* Build the tables for 5*1-patterns */

	  for ( i = 0; i < 5; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 243; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 5; j++ )
	      mirror_pattern += row[j] * pow3[4 - j];
	    map_mirror5[i] = i<mirror_pattern ? i :mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1] == 0) && (j < 5) );
	  }

	  /* Build the tables for 4*1-patterns */   

	  for ( i = 0; i < 4; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 81; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 4; j++ )
	      mirror_pattern += row[j] * pow3[3 - j];
	    map_mirror4[i] = i<mirror_pattern ? i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1]) == 0 && (j < 4) );
	  }

	  /* Build the tables for 3*1-patterns */   

	  for ( i = 0; i < 3; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 27; i++ ) {
	    mirror_pattern = 0;
	    for ( j = 0; j < 3; j++ )
	      mirror_pattern += row[j] * pow3[2 - j];
	    map_mirror3[i] = i<mirror_pattern ? i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1] == 0) && (j < 3) );
	  }

	  /* Build the tables for 5*2-patterns */

	  /* --- none needed --- */

	  /* Build the tables for edge2X-patterns */

	  for ( i = 0; i < 6561; i++ )
	    for ( j = 0; j < 3; j++ )
	      for ( k = 0; k < 3; k++ )
		map_mirror8x2[i + 6561 * j + 19683 * k] =
		  ( flip8[i] + 6561 * k + 19683 * j)<(i + 6561 * j + 19683 * k )?
				  (flip8[i] + 6561 * k + 19683 * j ):(i + 6561 * j + 19683 * k );	  

	  /* Build the tables for 3*3-patterns */

	  for ( i = 0; i < 9; i++ )
	    row[i] = 0;

	  for ( i = 0; i < 19683; i++ ) {
	    mirror_pattern =
	      row[0] + 3 * row[3] + 9 * row[6] +
	      27 * row[1] + 81 * row[4] + 243 * row[7] +
	      729 * row[2] + 2187 * row[5] + 6561 * row[8];
	    map_mirror33[i] = i<mirror_pattern ? i:mirror_pattern;

	    /* Next configuration */
	    j = 0;
	    do {  /* The odometer principle */
	      row[j]++;
	      if ( row[j] == 3 )
		row[j] = 0;
	      j++;
	    } while ( (row[j - 1] == 0) && (j < 9) );
	  }
      
	  /* Read and unpack - using symmetries - the coefficient tables. */
      ///////////////////////here stage_count????????????????
	  
	  for ( i = 0; i < stage_count - 1; i++ ) {
	  
	    
	    
	      
	    unpack_batch( set[i].afile2x, map_mirror8x2, 59049, stream );
	    unpack_batch( set[i].bfile, map_mirror8, 6561, stream );
	    unpack_batch( set[i].cfile, map_mirror8, 6561, stream );
	    unpack_batch( set[i].dfile, map_mirror8, 6561, stream );
	    unpack_batch( set[i].diag8, map_mirror8, 6561, stream );
	    unpack_batch( set[i].diag7, map_mirror7, 2187, stream );
	    unpack_batch( set[i].diag6, map_mirror6, 729, stream );
	    unpack_batch( set[i].diag5, map_mirror5, 243, stream );
	    unpack_batch( set[i].diag4, map_mirror4, 81, stream );
	    unpack_batch( set[i].corner33, map_mirror33, 19683, stream );
	    unpack_batch( set[i].corner52, null, 59049, stream );
	  }

	  /* Free the mirror tables - the symmetries are now implicit
	     in the coefficient tables. */

	  
	}
     void generate_coeffs(DataOutputStream stream){
    	int i, j, k;
  	  int mirror_pattern;
  	  int[] row=new int[10];
  	  int[] map_mirror3;
  	  int[] map_mirror4;
  	  int[] map_mirror5;
  	  int[] map_mirror6;
  	  int[] map_mirror7;
  	  int[] map_mirror8;
  	  int[] map_mirror33;
  	  int[] map_mirror8x2;

  	  /* Allocate the memory needed for the temporary mirror maps from the
  	     heap rather than the stack to reduce memory requirements. */

  	  map_mirror3 = new int[27];
  	  map_mirror4 = new int[81];
  	  map_mirror5 = new int[243];
  	  map_mirror6 = new int[729];
  	  map_mirror7 = new int[2187];
  	  map_mirror8 = new int[6561];
  	  map_mirror33 = new int[19683];
  	  map_mirror8x2 = new int[59049];

  	  /* Build the pattern tables for 8*1-patterns */

  	  for ( i = 0; i < 8; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 6561; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 8; j++ )
  	      mirror_pattern += row[j] * pow3[7 - j];
  	    /* Create the symmetry map */
  	    map_mirror8[i] = i<mirror_pattern ? i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1] == 0) && (j < 8) );
  	  }

  	  /* Build the tables for 7*1-patterns */

  	  for ( i = 0; i < 7; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 2187; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 7; j++ )
  	      mirror_pattern += row[j] * pow3[6 - j];
  	    map_mirror7[i] = i<mirror_pattern ?i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1]) == 0 && (j < 7) );
  	  }

  	  /* Build the tables for 6*1-patterns */

  	  for ( i = 0; i < 6; i++ )
  	    row[i] = 0;
  	  for ( i = 0; i < 729; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 6; j++ )
  	      mirror_pattern += row[j] * pow3[5 - j];
  	    map_mirror6[i] = i<mirror_pattern ? i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1]) == 0 && (j < 6) );
  	  }

  	  /* Build the tables for 5*1-patterns */

  	  for ( i = 0; i < 5; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 243; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 5; j++ )
  	      mirror_pattern += row[j] * pow3[4 - j];
  	    map_mirror5[i] = i<mirror_pattern ? i :mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1] == 0) && (j < 5) );
  	  }

  	  /* Build the tables for 4*1-patterns */   

  	  for ( i = 0; i < 4; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 81; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 4; j++ )
  	      mirror_pattern += row[j] * pow3[3 - j];
  	    map_mirror4[i] = i<mirror_pattern ? i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1]) == 0 && (j < 4) );
  	  }

  	  /* Build the tables for 3*1-patterns */   

  	  for ( i = 0; i < 3; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 27; i++ ) {
  	    mirror_pattern = 0;
  	    for ( j = 0; j < 3; j++ )
  	      mirror_pattern += row[j] * pow3[2 - j];
  	    map_mirror3[i] = i<mirror_pattern ? i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1] == 0) && (j < 3) );
  	  }

  	  /* Build the tables for 5*2-patterns */

  	  /* --- none needed --- */

  	  /* Build the tables for edge2X-patterns */

  	  for ( i = 0; i < 6561; i++ )
  	    for ( j = 0; j < 3; j++ )
  	      for ( k = 0; k < 3; k++ )
  		map_mirror8x2[i + 6561 * j + 19683 * k] =
  		  ( flip8[i] + 6561 * k + 19683 * j)<(i + 6561 * j + 19683 * k )?
  				  (flip8[i] + 6561 * k + 19683 * j ):(i + 6561 * j + 19683 * k );	  

  	  /* Build the tables for 3*3-patterns */

  	  for ( i = 0; i < 9; i++ )
  	    row[i] = 0;

  	  for ( i = 0; i < 19683; i++ ) {
  	    mirror_pattern =
  	      row[0] + 3 * row[3] + 9 * row[6] +
  	      27 * row[1] + 81 * row[4] + 243 * row[7] +
  	      729 * row[2] + 2187 * row[5] + 6561 * row[8];
  	    map_mirror33[i] = i<mirror_pattern ? i:mirror_pattern;

  	    /* Next configuration */
  	    j = 0;
  	    do {  /* The odometer principle */
  	      row[j]++;
  	      if ( row[j] == 3 )
  		row[j] = 0;
  	      j++;
  	    } while ( (row[j - 1] == 0) && (j < 9) );
  	  }
      for (i=0;i<stage_count-1;i++)
       {
    	  System.out.println(set[i]);
    	  if(set[i].afile2x==null)
    	  {
    		  System.out.println("null pointer");
    		  System.exit(1);
    	  }
    	  generate_batch( set[i].afile2x, map_mirror8x2, 59049, stream );
    	  generate_batch( set[i].bfile, map_mirror8, 6561, stream );
    	  generate_batch( set[i].cfile, map_mirror8, 6561, stream );
    	  generate_batch( set[i].dfile, map_mirror8, 6561, stream );
    	  generate_batch( set[i].diag8, map_mirror8, 6561, stream );
    	  generate_batch( set[i].diag7, map_mirror7, 2187, stream );
    	  generate_batch( set[i].diag6, map_mirror6, 729, stream );
    	  generate_batch( set[i].diag5, map_mirror5, 243, stream );
    	  generate_batch( set[i].diag4, map_mirror4, 81, stream );
    	  generate_batch( set[i].corner33, map_mirror33, 19683, stream );
    	  generate_batch( set[i].corner52, null, 59049, stream );
    	  System.out.println("write ok");
      }
    }
    
    
     void 
    generate_batch(double[] item, int [] mirror, int count, DataOutputStream stream ){
    	int i;
    	for ( i = 0; i < count; i++ ){

    			if ( (mirror == null) || (mirror[i] == i) ){
    			     
    			   write_double(stream,item[i]);
    	    }
    	}
    	    
    	
    }
    
	 void countPattern(Chessboard aboard,int discs){
		
		countAfile2x(aboard,discs);
		countBfile(aboard,discs);
		countCfile(aboard,discs);
		countDfile(aboard,discs);
		countDiag8(aboard,discs);
		countDiag7(aboard,discs);
		countDiag6(aboard,discs);
		countDiag5(aboard,discs);
		countDiag4(aboard,discs);
		countCorner33(aboard,discs);
		countCorner52(aboard,discs);
		pat[discs-5].val=pattern_evaluation(aboard,discs);
	}
    
	 void countAfile2x(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard & (1l<<49)) ==(1l<<49) ){
			  pattern0=2;
		  }
		  else if((aboard.blackBoard & (1l<<49)) ==(1l<<49) ){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard & (1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		  pat[discs-5].afile2x1=pattern0;
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  pat[discs-5].afile2x2=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  pat[discs-5].afile2x3=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;

		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].afile2x[pattern0] );
		#endif*/
		  pat[discs-5].afile2x4=pattern0;
  	  
	}
		
	 void countBfile(Chessboard aboard,int discs){
	    
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].bfile[pattern0] );
		#endif*/
		  pat[discs-5].bfile1=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].bfile[pattern0] );
		#endif*/
		  pat[discs-5].bfile2=pattern0;
		 
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].bfile[pattern0] );
		#endif*/
		  pat[discs-5].bfile3=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].bfile[pattern0] );
		#endif*/
		  pat[discs-5].bfile4=pattern0;
	}
	
	
	 void countCfile(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;

	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].cfile[pattern0] );
		#endif*/
		  pat[discs-5].cfile1=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  pat[discs-5].cfile2=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  pat[discs-5].cfile3=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  pat[discs-5].cfile4=pattern0;
		  
	}
	
	 void countDfile(Chessboard aboard,int discs){
    	int pattern0=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  pat[discs-5].dfile1=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  pat[discs-5].dfile2=pattern0;
		  
		  pattern0=0;
		  
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		 pat[discs-5].dfile3=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  pat[discs-5].dfile4=pattern0;
	}
    
	 void countDiag8(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag8[pattern0] );
		#endif*/
		  pat[discs-5].diag81=pattern0;
		  
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag8[pattern0] );
		#endif*/
		  pat[discs-5].diag82=pattern0;
	}
	
	 void countDiag7(Chessboard aboard,int discs){
		int pattern0=0;
    	
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].diag7[pattern0] );
		#endif*/
		  pat[discs-5].diag71=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  pat[discs-5].diag72=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  pat[discs-5].diag73=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  pat[discs-5].diag74=pattern0;
	}
	
	 void countDiag6(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  pat[discs-5].diag61=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  pat[discs-5].diag62=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  pat[discs-5].diag63=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  pat[discs-5].diag64=pattern0;
		  
	}
	
	 void countDiag5(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  pat[discs-5].diag51=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  pat[discs-5].diag52=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  pat[discs-5].diag53=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  pat[discs-5].diag54=pattern0;
		
	}
	 void countDiag4(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  pat[discs-5].dfile1=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  pat[discs-5].dfile2=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  pat[discs-5].dfile3=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  pat[discs-5].dfile4=pattern0;
		
	}
	 void countCorner33(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  pat[discs-5].corner331=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  pat[discs-5].corner332=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  pat[discs-5].corner333=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  pat[discs-5].corner334=pattern0;
	}
	 void countCorner52(Chessboard aboard,int discs){
		int pattern0=0;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner521=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner522=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner523=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner524=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner525=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner526=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  pat[discs-5].corner527=pattern0;
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  pat[discs-5].corner528=pattern0;
	}
	 double valAfile2x(Chessboard aboard,int stage){
    	  double score=0;
    	  int pattern0=0;
		  if((aboard.whiteBoard & (1l<<49)) ==(1l<<49) ){
			  pattern0=2;
		  }
		  else if((aboard.blackBoard & (1l<<49)) ==(1l<<49) ){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard & (1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  score += set[stage].afile2x[ pattern0];
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  score += set[stage].afile2x[ pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].afile2x[pattern0] );
		#endif*/
		  score += set[stage].afile2x[ pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;

		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].afile2x[pattern0] );
		#endif*/
		  score += set[stage].afile2x[pattern0];
		  return score;
    	
    }
    
     double valBfile(Chessboard aboard,int stage){
    	  int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].bfile[pattern0] );
		#endif*/
		  score += set[stage].bfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].bfile[pattern0] );
		#endif*/
		  score += set[stage].bfile[pattern0];
		 
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].bfile[pattern0] );
		#endif*/
		  score += set[stage].bfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].bfile[pattern0] );
		#endif*/
		  score += set[stage].bfile[pattern0];
		  
		  return score;
    }
    
     double valCfile(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;

	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].cfile[pattern0] );
		#endif*/
		  score += set[stage].cfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  score += set[stage].cfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  score += set[stage].cfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].cfile[pattern0] );
		#endif*/
		  score += set[stage].cfile[pattern0];
		  
		  return score;
    }
    
     double valDfile(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  score += set[stage].dfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  score += set[stage].dfile[pattern0];
		  
		  pattern0=0;
		  
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  score += set[stage].dfile[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].dfile[pattern0] );
		#endif*/
		  score += set[stage].dfile[pattern0];
		  
		  return score;
    }
    
     double valDiag8(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag8[pattern0] );
		#endif*/
		  score += set[stage].diag8[pattern0];
		  
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag8[pattern0] );
		#endif*/
		  score += set[stage].diag8[pattern0];
		 
		  return score;
    }
    
     double valDiag7(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
    	
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<28))==(1l<<28)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<28))==(1l<<28)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[stage].diag7[pattern0] );
		#endif*/
		  score += set[stage].diag7[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<35))==(1l<<35)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<35))==(1l<<35)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		 
	/*	#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  score += set[stage].diag7[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<27))==(1l<<27)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<27))==(1l<<27)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  score += set[stage].diag7[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<36))==(1l<<36)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<36))==(1l<<36)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag7[pattern0] );
		#endif*/
		  score += set[stage].diag7[pattern0];
		  
		  return score;
    }
    
    
     double valDiag6(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<29))==(1l<<29)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<29))==(1l<<29)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<20))==(1l<<20)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<20))==(1l<<20)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  score += set[stage].diag6[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<44))==(1l<<44)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<44))==(1l<<44)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<37))==(1l<<37)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<37))==(1l<<37)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  score += set[stage].diag6[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<26))==(1l<<26)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<26))==(1l<<26)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<19))==(1l<<19)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<19))==(1l<<19)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  score += set[stage].diag6[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<43))==(1l<<43)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<43))==(1l<<43)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<34))==(1l<<34)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<34))==(1l<<34)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag6[pattern0] );
		#endif*/
		  score += set[stage].diag6[pattern0];
		  
		  return score;
    }
    
     double valDiag5(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  score += set[stage].diag5[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  score += set[stage].diag5[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  score += set[stage].diag5[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag5[pattern0] );
		#endif*/
		  score += set[stage].diag5[pattern0];
		  
		  return score;
    }
    
    
     double valDiag4(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  score += set[stage].diag4[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  score += set[stage].diag4[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  score += set[stage].diag4[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].diag4[pattern0] );
		#endif*/
		  score += set[stage].diag4[pattern0];
		  
		  return score;
    }
     double valCorner33(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<18))==(1l<<18)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<18))==(1l<<18)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  score += set[stage].corner33[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<42))==(1l<<42)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<42))==(1l<<42)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  score += set[stage].corner33[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<21))==(1l<<21)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<21))==(1l<<21)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  score += set[stage].corner33[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<45))==(1l<<45)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<45))==(1l<<45)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner33[pattern0] );
		#endif*/
		  score += set[stage].corner33[pattern0];
		 
		  return score;
    }
    double valCorner52(Chessboard aboard,int stage){
    	int pattern0=0;double score=0;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<10))==(1l<<10)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<10))==(1l<<10)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<2))==(1l<<2)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<2))==(1l<<2)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<50))==(1l<<50)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<50))==(1l<<50)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<58))==(1l<<58)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<58))==(1l<<58)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<11))==(1l<<11)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<11))==(1l<<11)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<12))==(1l<<12)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<12))==(1l<<12)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<13))==(1l<<13)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<13))==(1l<<13)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<3))==(1l<<3)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<3))==(1l<<3)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<4))==(1l<<4)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<4))==(1l<<4)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<5))==(1l<<5)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<5))==(1l<<5)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<51))==(1l<<51)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<51))==(1l<<51)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<52))==(1l<<52)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<52))==(1l<<52)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<53))==(1l<<53)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<53))==(1l<<53)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<59))==(1l<<59)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<59))==(1l<<59)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<60))==(1l<<60)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<60))==(1l<<60)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<61))==(1l<<61)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<61))==(1l<<61)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		 
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<17))==(1l<<17)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<17))==(1l<<17)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<9))==(1l<<9)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<9))==(1l<<9)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<1))==(1l<<1)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<1))==(1l<<1)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<16))==(1l<<16)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<16))==(1l<<16)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<8))==(1l<<8)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<8))==(1l<<8)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1))==1){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1))==1){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<22))==(1l<<22)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<22))==(1l<<22)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<14))==(1l<<14)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<14))==(1l<<14)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<6))==(1l<<6)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<6))==(1l<<6)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<23))==(1l<<23)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<23))==(1l<<23)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<15))==(1l<<15)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<15))==(1l<<15)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<7))==(1l<<7)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<7))==(1l<<7)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<25))==(1l<<25)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<25))==(1l<<25)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<33))==(1l<<33)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<33))==(1l<<33)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<41))==(1l<<41)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<41))==(1l<<41)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<49))==(1l<<49)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<49))==(1l<<49)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<57))==(1l<<57)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<57))==(1l<<57)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<24))==(1l<<24)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<24))==(1l<<24)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<32))==(1l<<32)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<32))==(1l<<32)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<40))==(1l<<40)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<40))==(1l<<40)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<48))==(1l<<48)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<48))==(1l<<48)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<56))==(1l<<56)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<56))==(1l<<56)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		/*#ifdef LOG_EVAL
		  fprintf( stream, "pattern0=%d\n", pattern0 );
		  fprintf( stream, "score=%d\n", set[eval_phase].corner52[pattern0] );
		#endif*/
		  score += set[stage].corner52[pattern0];
		  
		  pattern0=0;
		  if((aboard.whiteBoard &(1l<<30))==(1l<<30)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<30))==(1l<<30)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<38))==(1l<<38)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<38))==(1l<<38)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<46))==(1l<<46)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<46))==(1l<<46)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<54))==(1l<<54)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<54))==(1l<<54)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<62))==(1l<<62)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<62))==(1l<<62)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<31))==(1l<<31)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<31))==(1l<<31)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<39))==(1l<<39)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<39))==(1l<<39)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<47))==(1l<<47)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<47))==(1l<<47)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<55))==(1l<<55)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<55))==(1l<<55)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  if((aboard.whiteBoard &(1l<<63))==(1l<<63)){
			  pattern0=3*pattern0+2;
			  
		  }
		  else if((aboard.blackBoard &(1l<<63))==(1l<<63)){
			  ;
		  }
		  else
			  pattern0=3*pattern0+1;
		  
		  score += set[stage].corner52[pattern0];
		  
		  return score;
    }
    

    /*
     * GENERATE_BATCH
	   Interpolates between two stages.
	*/   

	/*static void
	generate_batch( short []target, int count, short[] source1, int weight1,
			short[] source2, int weight2 ) {
	  int i;
	  int total_weight;

	  total_weight = weight1 + weight2;
	  for ( i = 0; i < count; i++ )
	    target[i] = (short) ((weight1 * source1[i] + weight2 * source2[i]) /
	      total_weight);
	}*/

    
	/*
	   FIND_MEMORY_BLOCK
	   Maintains an internal memory handler to boost
	   performance and avoid heap fragmentation.
	*/
    

    
	  double
		pattern_evaluation( Chessboard aboard,int discs) {
		
		  
		  double score=0;
		  
		  
		  if((aboard.gameOver()||aboard.getTotalDiscs()>60)&&( aboard.getCurrentTurn() == Chessboard.ChequerState.Black )){
			  return aboard.sumBlack()-aboard.sumWhite();
		  }
		  if((aboard.gameOver()||aboard.getTotalDiscs()>60)&&( aboard.getCurrentTurn() == Chessboard.ChequerState.Black )){
			  return aboard.sumWhite()-aboard.sumBlack();
		  }
		  
          int stage=Math.round((float)((discs-5)/5-0.5));
		

		  /* The pattern features. */

		  if ( aboard.getCurrentTurn() == Chessboard.ChequerState.Black ) {
		
		  
		  score+=valAfile2x(aboard,stage);
		  score+=valBfile(aboard,stage);
		  score+=valCfile(aboard,stage);
		  score+=valDfile(aboard,stage);
		  score+=valDiag8(aboard,stage);
		  score+=valDiag7(aboard,stage);
		  score+=valDiag6(aboard,stage);
		  score+=valDiag5(aboard,stage);
		  score+=valDiag4(aboard,stage);
		  score+=valCorner33(aboard,stage);
		  score+=valCorner52(aboard,stage);
		  }
		  else {
		      long tem=aboard.blackBoard;
		      aboard.blackBoard=aboard.whiteBoard;
		      aboard.whiteBoard=tem;
		      score+=valAfile2x(aboard,stage);
			  score+=valBfile(aboard,stage);
			  score+=valCfile(aboard,stage);
			  score+=valDfile(aboard,stage);
			  score+=valDiag8(aboard,stage);
			  score+=valDiag7(aboard,stage);
			  score+=valDiag6(aboard,stage);
			  score+=valDiag5(aboard,stage);
			  score+=valDiag4(aboard,stage);
			  score+=valCorner33(aboard,stage);
			  score+=valCorner52(aboard,stage);
			  
			  aboard.whiteBoard=aboard.blackBoard;
			  aboard.blackBoard=tem;
		      
		  }
		

		  return score;
		}
	

	
	}
class CoeffSet{
	  
    public   	int loaded;
	  public    int stage;
	 
	  public    double[] afile2x, bfile, cfile, dfile;
	  public    double[] diag8, diag7, diag6, diag5, diag4;
	  public    double[] corner33, corner52;
	  
    
   
	  public CoeffSet(){
		  loaded=0;
		  stage=0;
		  this.afile2x=new double[59049];
		  this.bfile=new double[6561];
		  this.cfile=new double[6561];
		  this.dfile=new double[6561];
		  this.diag8=new double[6561];
		  this.diag7=new double[2187];
		  this.diag6=new double[729];
		  this.diag5=new double[243];
		  this.diag4=new double[81];
		  this.corner33=new double[19683];
		  this.corner52=new double[59049];
		  
		  
		  
	  }
	}
class CoeffCount{
	public int [] cafile2x=new int[59049];
	public int [] cbfile=new int[6561];
	public int [] ccfile=new int[6561];
	public int [] cdfile=new int[6561];
	public int [] cdiag8=new int[6561];
	public int [] cdiag7=new int[2187];
	public int [] cdiag6=new int[729];
	public int [] cdiag5=new int[243];
	public int [] cdiag4=new int[81];
	public int [] ccorner33=new int[19683];
	public int [] ccorner52=new int[59049];
    
	
	public double [] vafile2x=new double[59049];
	public double [] vbfile=new double[6561];
	public double [] vcfile=new double[6561];
	public double [] vdfile=new double[6561];
	public double [] vdiag8=new double[6561];
	public double [] vdiag7=new double[2187];
	public double [] vdiag6=new double[729];
	public double [] vdiag5=new double[243];
	public double [] vdiag4=new double[81];
	public double [] vconner33=new double[19683];
	public double [] vconner52=new double[59049];
	
	                                                         
}
class PatAppearence{
	public int afile2x1;
	public int afile2x2;
	public int afile2x3;
	public int afile2x4;
	public int bfile1;
	public int bfile2;
	public int bfile3;
	public int bfile4;
	public int cfile1;
	public int cfile2;
	public int cfile3;
	public int cfile4;
	public int dfile1;
	public int dfile2;
	public int dfile3;
	public int dfile4;
	public int diag81;
	public int diag82;
	public int diag71;
	public int diag72;
	public int diag73;
	public int diag74;
	public int diag61;
	public int diag62;
	public int diag63;
	public int diag64;
	public int diag51;
	public int diag52;
	public int diag53;
	public int diag54;
	public int diag41;
	public int diag42;
	public int diag43;
	public int diag44;
	public int corner331;
	public int corner332;
	public int corner333;
	public int corner334;
	public int corner521;
	public int corner522;
	public int corner523;
	public int corner524;
	public int corner525;
	public int corner526;
	public int corner527;
	public int corner528;
	public double val;
	public void reSet(){
		 val=0;
		 afile2x1=0;
		 afile2x2=0;
		 afile2x3=0;
		 afile2x4=0;
		 bfile1=0;
		 bfile2=0;
		 bfile3=0;
		 bfile4=0;
		 cfile1=0;
		 cfile2=0;
		 cfile3=0;
		 cfile4=0;
		 dfile1=0;
		 dfile2=0;
		 dfile3=0;
		 dfile4=0;
		 diag81=0;
		 diag82=0;
		 diag71=0;
		 diag72=0;
		 diag73=0;
		 diag74=0;
		 diag61=0;
		 diag62=0;
		 diag63=0;
		 diag64=0;
		 diag51=0;
		 diag52=0;
		 diag53=0;
		 diag54=0;
		 diag41=0;
		 diag42=0;
		 diag43=0;
		 diag44=0;
		 corner331=0;
		 corner332=0;
		 corner333=0;
		 corner334=0;
		 corner521=0;
		 corner522=0;
		 corner523=0;
		 corner524=0;
		 corner525=0;
		 corner526=0;
		 corner527=0;
		 corner528=0;
	}
}

/////add by ssq
