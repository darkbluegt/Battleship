import java.util.ArrayList;
import java.util.List;

//plansza 10x10 zawierajaca informacje o polozeniu statkow

public class Board {
	
	private String name;
	private int[][] board;  //0 -nie klikniete, nie ma statku; 
					//1 -nie klikniete, jest statek; 
					//2 -klikniete, bylo pudlo lub klikniete przez automat; 
					//3 -klikniete, byl trafiony
					//4 -klikniete, byl trafiony i zatopiony
	private static boolean startGame = false;
	static final int maxSizeOfShip = 4;
	List<Ship> ships;
	
	
	Board(){		
		board = new int[10][10];
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				board[i][j]=0;
			}
		}
		ships = new ArrayList<Ship>();
		name = "Default";
	}
	
	Board(String name){		
		board = new int[10][10];
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				board[i][j]=0;
			}
		}
		ships = new ArrayList<Ship>();
		this.name = name;
	}
	
	String getName(){
		return name;
	}
	
	int[][] getBoard(){
		return board;
	}
	
	static void startGame(){
		startGame=true;
	}
	
	static boolean isGameStarted(){
		return startGame;
	}
	
	public boolean isMovePossible(){
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				if(board[j][i]==0 || board[j][i]==1)
					return true;
			}
		}
		return false;
	}
	
	boolean isAnyShip(){
		return !ships.isEmpty();
	}
	
	public int shoot(int x, int y){			//-1: blad; 0: pudlo, 1: tylko trafiony, 2: trafiony i zatopiony
		if(!isGameStarted() || x>9 || x<0 || y>9 || y<0)
			return -1;
		else if(board[x][y]==1){
			board[x][y]=3;
			Ship s = Ship.findShip(this, x, y);
			if(s==null)
				System.exit(0); // !!!!!!!
			s.size--;
			if(s.isDestroyed()){
				int[] crd = s.getCoordinates();
				if(crd.length==2){
					board[crd[0]][crd[1]]=4;
					checkEmptyFields(crd[0], crd[1]);
				}else{
					int[] coordinates = new int[4];
					coordinates[0]=crd[0];
					coordinates[1]=crd[1];
					coordinates[2]=crd[crd.length-2];
					coordinates[3]=crd[crd.length-1];
					if(isVertically(coordinates)){
						for(int i=0; i<crd.length/2; i++){
							board[coordinates[0]][coordinates[1]+i]=4;
							checkEmptyFields(coordinates[0], coordinates[1]+i);
						}
					}else{
						for(int i=0; i<crd.length/2; i++){
							board[coordinates[0]+i][coordinates[1]]=4;
							checkEmptyFields(coordinates[0]+i, coordinates[1]);
						}
					}						
				}
				ships.remove(s);
				return 2;
			}else 
				return 1;	
		}else if(board[x][y]==0){
			board[x][y]=2;
			return 0;
		}else 
			return -1;
	}

	private void checkEmptyFields(int x, int y){
		
		if(x==0 && y!=0 && y!=9){ //left side, without corners
			if(board[x][y+1]==0) 	board[x][y+1]=2;
			if(board[x][y-1]==0) 	board[x][y-1]=2;
			if(board[x+1][y+1]==0)	board[x+1][y+1]=2;
			if(board[x+1][y]==0) 	board[x+1][y]=2;
			if(board[x+1][y-1]==0) 	board[x+1][y-1]=2;
		}
		else if(x==0 && y==0){  //top left corner
			if(board[x][y+1]==0) board[x][y+1]=2;
			if(board[x+1][y+1]==0) board[x+1][y+1]=2;
			if(board[x+1][y]==0) board[x+1][y]=2;
		}
		else if(x==0 && y==9){ //bottom left corner
			if(board[x][y-1]==0) board[x][y-1]=2;
			if(board[x+1][y-1]==0) board[x+1][y-1] =2;
			if(board[x+1][y]==0) board[x+1][y]=2;
		}
		else if(x==9 && y!=0 && y!=9){ //right side without corners
			if(board[x][y+1]==0) 	board[x][y+1]=2;
			if(board[x][y-1]==0) 	board[x][y-1]=2;
			if(board[x-1][y+1]==0)	board[x-1][y+1]=2;
			if(board[x-1][y]==0) 	board[x-1][y]=2;
			if(board[x-1][y-1]==0) 	board[x-1][y-1]=2;
		}
		else if(x==9 && y==0){  //top right corner
			if(board[x][y+1]==0) 	board[x][y+1]=2;
			if(board[x-1][y+1]==0)	board[x-1][y+1]=2;
			if(board[x-1][y]==0) 	board[x-1][y]=2;
		}
		else if(x==9 && y==9){  //bottom right corner
			if(board[x][y-1]==0) 	board[x][y-1]=2;
			if(board[x-1][y]==0) 	board[x-1][y]=2;
			if(board[x-1][y-1]==0) 	board[x-1][y-1]=2;
		}
		else if(y==0 && x!=0 && x!=9){  //top edge without corners
			if(board[x-1][y]==0) 	board[x-1][y]=2;
			if(board[x+1][y]==0) 	board[x+1][y]=2;
			if(board[x-1][y+1]==0)	board[x-1][y+1]=2;
			if(board[x][y+1]==0)	board[x][y+1]=2;
			if(board[x+1][y+1]==0)	board[x+1][y+1]=2;
		}
		else if(y==9 && x!=0 && x!=9){  // bottom edge without corners			
			if(board[x-1][y]==0) 	board[x-1][y]=2;
			if(board[x+1][y]==0) 	board[x+1][y]=2;
			if(board[x-1][y-1]==0)	board[x-1][y-1]=2;
			if(board[x][y-1]==0)	board[x][y-1]=2;
			if(board[x+1][y-1]==0)	board[x+1][y-1]=2;
		}		
		else{
			if(board[x-1][y]==0) 	board[x-1][y]=2;
			if(board[x+1][y]==0) 	board[x+1][y]=2;
			if(board[x-1][y+1]==0)	board[x-1][y+1]=2;
			if(board[x][y+1]==0)	board[x][y+1]=2;
			if(board[x+1][y+1]==0) 	board[x+1][y+1]=2;
			if(board[x-1][y-1]==0)	board[x-1][y-1]=2;
			if(board[x][y-1]==0)	board[x][y-1]=2;
			if(board[x+1][y-1]==0) 	board[x+1][y-1]=2;
		}
	}
	
	static private int[] coordinatesToArray(int x1, int y1, int x2, int y2){
		
		int[] coordinates = {x1,y1,x2,y2};
		
		if(x1>x2){  		//sortowanie wspolrzednych X
			int temp = x1;
			x1 = x2;
			x2 = temp;
			coordinates[0]=x1;
			coordinates[2]=x2;
		}
		if(y1>y2){ 			//sortowanie wspolrzednych Y
			int temp = y1;
			y1 = y2;
			y2 = temp;
			coordinates[1]=y1;
			coordinates[3]=y2;
		}
		return coordinates;
	}
	
	static boolean isVertically(int[] coordinates){  //zwraca prawde jesli statek jest pionowo (x1=x2)
		if(coordinates[0]==coordinates[2])
			return true;
		else return false;
	}
	
	private boolean czyMoznaPostawicStatek(int x, int y){
			
		if(x==0 && y!=0 && y!=9){ //lewa krawedz, bez rogow
			if(board[x][y+1]==1 || board[x][y]==1 || board[x][y-1]==1 
					|| board[x+1][y+1]==1 || board[x+1][y]==1 || board[x+1][y-1]==1)
				return false;
		}
		else if(x==0 && y==0){  //lewa górny róg
			if(board[x][y+1]==1 || board[x][y]==1 
					|| board[x+1][y+1]==1 || board[x+1][y]==1)
				return false;
		}
		else if(x==0 && y==9){ //lewy dolny róg
			if(board[x][y-1]==1 || board[x][y]==1 
					|| board[x+1][y-1]==1 || board[x+1][y]==1)
				return false;
		}
		else if(x==9 && y!=0 && y!=9){ //prawa krawedz bez rogów
			if(board[x-1][y+1]==1 || board[x-1][y]==1 || board[x-1][y-1]==1 
					|| board[x][y+1]==1 || board[x][y]==1 || board[x][y-1]==1)
				return false;
		}
		else if(x==9 && y==0){  //prawy górny róg
			if(board[x][y]==1 || board[x][y+1]==1 
					|| board[x-1][y]==1 || board[x-1][y+1]==1)
				return false;
		}
		else if(x==9 && y==9){  //prawy dolny róg
			if(board[x][y]==1 || board[x][y-1]==1 
					|| board[x-1][y]==1 || board[x-1][y-1]==1)
				return false;
		}
		else if(y==0 && x!=0 && x!=9){  //górna krawedz bez rogów
			if(board[x-1][y]==1 || board[x][y]==1 || board[x+1][y]==1 
					|| board[x-1][y+1]==1 || board[x][y+1]==1 || board[x+1][y+1]==1)
				return false;
		}
		else if(y==9 && x!=0 && x!=9){  // dolna krawêdz bez rogów
			if(board[x-1][y]==1 || board[x][y]==1 || board[x+1][y]==1 
					|| board[x-1][y-1]==1 || board[x][y-1]==1 || board[x+1][y-1]==1)
				return false;
		}		
		else{
			if(board[x-1][y+1]==1 || board[x-1][y]==1 || board[x-1][y-1]==1 
					|| board[x][y+1]==1 || board[x][y]==1 || board[x][y-1]==1 
					|| board[x+1][y+1]==1 || board[x+1][y]==1 || board[x+1][y-1]==1)
				return false;			
		}
		return true;
	}
	
	private boolean czyMoznaPostawicStatek(int[] coordinates){
		
		int sizeH = coordinates[2]-coordinates[0]+1;  //size if Horizontal
		int sizeV = coordinates[3]-coordinates[1]+1;  //size if Vertical
		
		if((coordinates[0]!=coordinates[2] && coordinates[1]!=coordinates[3]) 
				|| sizeH>maxSizeOfShip || sizeV>maxSizeOfShip
				|| sizeH<1 || sizeV<1)
			return false;
		
		else if(isVertically(coordinates)){  //for vertically ships
			if(coordinates[0]>0 && coordinates[2]<9 && coordinates[1]>0 && coordinates[3]<9){ //middle
				for(int i = 0; i<sizeV+2; i++){
					if(board[coordinates[0]-1][coordinates[1]-1+i]==1
							|| board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]+1][coordinates[1]-1+i]==1)
						return false;
				}
			}
			else if(coordinates[0]==0 && coordinates[1]>0 && coordinates[3]<9){ //left side
				for(int i = 0; i<sizeV+2; i++){					
					if(board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]+1][coordinates[1]-1+i]==1)
						return false;
				}
			}
			else if(coordinates[2]==9 && coordinates[1]>0 && coordinates[3]<9){ //right side
				for(int i = 0; i<sizeV+2; i++){					
					if(board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]-1][coordinates[1]-1+i]==1)
						return false;
				}
			}
			else if(coordinates[1]==0 && coordinates[0]>0 && coordinates[2]<9){ //up side
				for(int i = 0; i<sizeV+1; i++){
					if(board[coordinates[0]-1][coordinates[1]+i]==1
							|| board[coordinates[0]][coordinates[1]+i]==1
							|| board[coordinates[0]+1][coordinates[1]+i]==1)
						return false;
				}
			}
			else if(coordinates[3]==9 && coordinates[0]>0 && coordinates[2]<9){ //down side
				for(int i = 0; i<sizeV+1; i++){
					if(board[coordinates[0]-1][coordinates[1]-1+i]==1
							|| board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]+1][coordinates[1]-1+i]==1)
						return false;
				}
			}
			else if(coordinates[0]==0 && coordinates[1]==0){ //Left Up
				for(int i = 0; i<sizeV+1; i++){					
					if(board[coordinates[0]][coordinates[1]+i]==1
							|| board[coordinates[0]+1][coordinates[1]+i]==1)
						return false;
				}
			}
			else if(coordinates[0]==0 && coordinates[3]==9){  //Left down
				for(int i = 0; i<sizeV+1; i++){					
					if(board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]+1][coordinates[1]-1+i]==1)
						return false;
				} 
			}
			else if(coordinates[0]==9 && coordinates[1]==0){  //Right Up
				for(int i = 0; i<sizeV+1; i++){					
					if(board[coordinates[0]][coordinates[1]+i]==1
							|| board[coordinates[0]-1][coordinates[1]+i]==1)
						return false;
				}
			}
			else if(coordinates[0]==9 && coordinates[3]==9){  //Right Down
				for(int i = 0; i<sizeV+1; i++){					
					if(board[coordinates[0]][coordinates[1]-1+i]==1
							|| board[coordinates[0]-1][coordinates[1]-1+i]==1)
						return false;
				}
			}
		}		
		else{   //for horizontally ships
			if(coordinates[0]>0 && coordinates[2]<9 && coordinates[1]>0 && coordinates[3]<9){ //middle
				for(int i = 0; i<sizeH+2; i++){
					if(board[coordinates[0]-1+i][coordinates[1]-1]==1
							|| board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[0]==0 && coordinates[1]>0 && coordinates[3]<9){ //left side
				for(int i = 0; i<sizeH+1; i++){					
					if(board[coordinates[0]+i][coordinates[1]-1]==1
							|| board[coordinates[0]+i][coordinates[1]]==1
							|| board[coordinates[0]+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[2]==9 && coordinates[1]>0 && coordinates[3]<9){ //right side
				for(int i = 0; i<sizeH+1; i++){					
					if(board[coordinates[0]-1+i][coordinates[1]-1]==1
							|| board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[1]==0 && coordinates[0]>0 && coordinates[2]<9){ //up side
				for(int i = 0; i<sizeH+2; i++){
					if(board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[3]==9 && coordinates[0]>0 && coordinates[2]<9){ //down side
				for(int i = 0; i<sizeH+2; i++){
					if(board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]-1]==1)
						return false;
				}
			}
			else if(coordinates[1]==0 && coordinates[0]==0){  //Left up
				for(int i = 0; i<sizeH+1; i++){
					if(board[coordinates[0]+i][coordinates[1]]==1
							|| board[coordinates[0]+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[1]==9 && coordinates[0]==0){  //Left down
				for(int i = 0; i<sizeH+1; i++){
					if(board[coordinates[0]+i][coordinates[1]]==1
							|| board[coordinates[0]+i][coordinates[1]-1]==1)
						return false;
				}
			}
			else if(coordinates[1]==0 && coordinates[2]==9){  //Right up
				for(int i = 0; i<sizeH+1; i++){
					if(board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]+1]==1)
						return false;
				}
			}
			else if(coordinates[1]==9 && coordinates[2]==9){  //Right Down
				for(int i = 0; i<sizeH+1; i++){
					if(board[coordinates[0]-1+i][coordinates[1]]==1
							|| board[coordinates[0]-1+i][coordinates[1]-1]==1)
						return false;
				}
			}
		}
		return true;
	}
		
	public boolean addShip(int x, int y){ //add single ship; true-success, false-failure
		if(x<0 || x>9 || y<0 || y>9 || isGameStarted())
			return false;
		else if(czyMoznaPostawicStatek(x, y)){
			ships.add(new Ship(x, y));
			board[x][y]=1;
			System.out.println("Dodano statek ("+x+","+y+")");
			return true;
		}
		else return false;
	}
	
	public boolean addShip(int firstX, int firstY, int lastX, int lastY){  //add multiple ship; true-success, false-failure
		
		if(firstX<0 || firstX>9 || lastX<0 || lastX>9 || firstY<0 || firstY>9 || lastY<0 || lastY>9 || isGameStarted())
			return false;
		
		int[] coordinates = coordinatesToArray(firstX, firstY, lastX, lastY);
		
		if(czyMoznaPostawicStatek(coordinates)){
			if(isVertically(coordinates)){
				int size=coordinates[3]-coordinates[1]+1;
				ships.add(new Ship(coordinates));
				for(int i=0; i<size; i++)
					board[coordinates[0]][coordinates[1]+i]=1;
				System.out.println("Dodano statek ("+firstX+","+firstY+","+lastX+","+lastY+")");
				return true;
			}
			else{
				int size=coordinates[2]-coordinates[0]+1;
				ships.add(new Ship(coordinates));
				for(int i=0; i<size; i++)
					board[coordinates[0]+i][coordinates[1]]=1;
				System.out.println("Dodano statek ("+firstX+","+firstY+","+lastX+","+lastY+")");
				return true;
			}
		}
		else {
			System.out.println("Nie mozna postawic statku");
			return false;
		}
	}
	
	public void printBoard(){
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				System.out.print(board[j][i]+" ");
			}
			System.out.println("");
		}
	}
	
	
	
}
