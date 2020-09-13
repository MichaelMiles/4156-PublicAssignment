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
   * add move if the given move is valid
   * @param m move by the player
   * @return true if move is added, false otherwise
   */
  public boolean addMove(Move m) {
	  // check if move is valid
	  if (!this.checkMove(m)) {
		  return false;
	  }
	  
	  // add the move and return true
	  int x = m.getMX();
	  int y = m.getMY();
	  
	  this.boardState[x][y] = m.getPlayer().getType();
	  return true;
  }
  
  /**
   * check if this game has a winner
   * @return the id of the winner, -1 if no winner 
   */
  public int checkWinner() {
	  // check player 1
	  if(this.checkChar(this.p1.getType())) {
		  return this.p1.getId();
	  }
	  
	  if (this.checkChar(this.p2.getType())) {
		  return this.p2.getId();
	  }
	  
	  return -1;
  }
  
  /**
   * helper method for check winner
   * check if the given char has 3 in a row, or column, or diagonal
   * @pre the c must be X or O 
   * @param c the char we are checking 
   * @return true if the given c met the requirement, false otherwise
   */
  private boolean checkChar(char c) {
	  // check row
	  for (int x = 0; x < 3; x++) {
		  if ((this.boardState[x][0] == this.boardState[x][1]) && (this.boardState[x][1] == this.boardState[x][2]) 
				  && (this.boardState[x][2] == c)) {
			  return true;
		  } 
	  }
	  
	  // check column 
	  for (int y = 0; y < 3; y++) {
		  if ((this.boardState[0][y] == this.boardState[1][y]) && (this.boardState[1][y] == this.boardState[2][y]) 
				  && (this.boardState[2][y] == c)) {
			  return true;
		  } 
	  }
	  
	  // check diagonal
	  if ((this.boardState[0][0] == this.boardState[1][1]) && this.boardState[1][1] == this.boardState[2][2]
			  && this.boardState[1][1] == c) {
		  return true;
	  }
	  
	  if ((this.boardState[0][2] == this.boardState[1][1]) && this.boardState[1][1] == this.boardState[2][0]
			  && this.boardState[1][1] == c) {
		  return true;
	  }
	  
	  // otherwise return false
	  return false;
  }
  
 /**
  * check if the given move is valid for this gameboard 
  * @param m the move that player made 
  * @return true if the move is valid, false otherwise
  */
  private boolean checkMove(Move m) {
	  int mx = m.getMX();
	  int my = m.getMY();
	  Player p = m.getPlayer();
	  
	  char c = 'X';
	  if (p.getId() == this.p1.getId()) {
		  c = this.p1.getType();
	  } else if (p.getId() == this.p2.getId()) {
		  c = this.p2.getType();
	  } else {
		  // this player's id is invalid 
		  return false;
	  }
	  
	  // check if mx or my out of bound 
	  if (mx < 0 || mx > 2 || my < 0 || my  > 2) {
		  return false;
	  }
	  
	  // check if the given coordinate has been taken
	  if (this.boardState[mx][my] != '\u0000') {
		  return false;
	  }
	  return true;
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
	  res += "\"gameStarted\": ";
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
