package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;

  
  public GameBoard() {
	  this.p1 = null;
	  this.p2 = null;
	  this.gameStarted = false;
	  this.turn = 1;
	  this.boardState = new char[3][3];
	  this.winner = 0;
	  this.isDraw = false;
  }
  
  // a set of getters 
  public Player getP1() {
	  return p1;
  }
  
  public Player getP2() {
	  return p2;
  }
  
  public boolean getGameStarted() {
	  return this.gameStarted;
  }
  
  public int getTurn() {
	  return this.turn;
  }
  
  public char[][] getBoardState() {
	  return this.boardState;
  }
  
  public int getWinner() {
	  return this.winner;
  }
  
  public boolean getIsDraw() {
	  return this.isDraw;
  }
  
  
  
  // a set of setters 
  public void setP1(Player p1) {
	  this.p1 = p1;
  } 
  
  public void setP2(Player p2) {
	  this.p2 = p2;
  }
  
  public void setGameStarted(boolean t) {
	  this.gameStarted = t;
  }
  
  public void setTurn(int turn) {
	  this.turn = turn;
  } 
  
  public void boardState(char[][] c) {
	  this.boardState = c;
  } 
  
  public void setWinnder(int winner) {
	  this.winner = winner;
  }
  
  public void setIsDraw(boolean t) {
	  this.isDraw = t;
  }
  
  
 
  
  /**    
   * @return the information of Gameboard in Json format
   */
  public String toJson() {
	  String res = "";
	  
	  res += "{";
	  
	  // players info
	  if (this.p1 != null) {
		res += p1.toJson();
		res += ",";
	  }
	  
	  if (this.p2 != null) {
		  res += p2.toJson();
		  res += ",";
	  }
	  
	  // gameStarted 
	  res += "\"GameStarted\": ";
	  if (this.gameStarted) {
		  res += "true";
	  } else {
		  res += "false";
	  }
	  res += ",";
	  
	  // turn
	  res += "\"turn\": " + this.turn + ",";
	  
	  
	  // board state
	  res += "\"boardState\": [" ;
	  
	  for(int i =0; i < 3; i++) {
		  res += "[";
		  for (int j = 0; j < 3; j++) {
			  res += "\"" + String.format("\\u%04x", (int) this.boardState[i][j]) + "\"";
			  if (j != 2) {
				  res += ",";
			  }
		  }
		  res += "]";
		  if (i != 2) {
			  res += ",";
		  }
	  }
	  
	  res += "],";
	  
	  // winner
	  res += "\"winner\": " + this.winner + ",";
	  // isDraw
	  res += "\"isDraw\": ";
	  if(this.isDraw) {
		  res += "true";
	  } else {
		  res += "false";
	  }
	  
	  
	  res += "}";

	  return res;
  }
}
