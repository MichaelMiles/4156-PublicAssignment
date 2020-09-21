package models;

public class GameBoard {
  
  /**
   * the most total moves players can make on this game board.
   */
  static final int MAX_MOVES = 9; 
	
  /**
   * the length and width of this board.
   */
  static final int DIMENSION = 3;
  
  /**
   * the first player in this game.
   */
  private Player p1; 

  /**
   * the second player in this game.
   */
  private Player p2;

  /**
   * if the game started or not.
   */
  private boolean gameStarted;

  /**
   * the id of the player who plays this turn.
   */
  private int turn;

  /**
   * board status.
   */
  private char[][] boardState;

  /**
   * the id of the winner.
   */
  private int winner;

  /**
   * if the game is draw or not.
   */
  private boolean isDraw;
  
  /**
   * to keep track of total moves by all players.
   */
  private int moves;

  
  
  /**
   * construct a new gameboard and initialize the fields.
   */
  public GameBoard() {
	  this.p1 = null;
	  this.p2 = null;
	  this.gameStarted = false;
	  this.turn = 1;
	  this.boardState = new char[DIMENSION][DIMENSION];
	  this.winner = 0;
	  this.isDraw = false;
	  this.moves = 0;
  }
  
  // a set of getters 
  
  /**
   * 
   * @return the first player of this game.
   */
  public Player getP1() {
	  return p1;
  }
  
  /**
   * 
   * @return the second player of this game.
   */
  public Player getP2() {
	  return p2;
  }
  
  /**
   * 
   * @return if the game has started or not.
   */
  public boolean getGameStarted() {
	  return this.gameStarted;
  }
  
  /**
   * 
   * @return the id of the player who plays this turn.
   */
  public int getTurn() {
	  return this.turn;
  }
  
  /**
   * 
   * @return the game board status.
   */
  public char[][] getBoardState() {
	  return this.boardState;
  }
  
  /**
   * 
   * @return the id of the winner.
   */
  public int getWinner() {
	  return this.winner;
  }
  
  /**
   * 
   * @return if the game is a draw or not.
   */
  public boolean getIsDraw() {
	  return this.isDraw;
  }
  
  
  // a set of setters 
  
  /**
   * set player 1 with the given player.
   * @param p the new player for 1st player
   */
  public void setP1(final Player p) {
	  this.p1 = p;
  } 
  
  
  /**
   * set player 2 with the given player.
   * @param p the new player for 2nd player
   */
  public void setP2(final Player p) {
	  this.p2 = p;
  }
  
  /**
   * set the status of the game(started or not).
   * @param t the new status
   */
  public void setGameStarted(final boolean t) {
	  this.gameStarted = t;
  }
  
  /**
   * set the id of the player who should play this turn.
   * @param t the new assigned turn
   */
  public void setTurn(final int t) {
	  this.turn = t;
  } 
  
  /**
   * set the game board status.
   * @param c the new status
   */
  public void boardState(final char[][] c) {
	  this.boardState = c;
  } 
  
  /**
   * set the id of the winner.
   * @param winer the new winner of this game
   */
  public void setWinnder(final int winer) {
	  this.winner = winer;
  }
  
  /**
   * set the draw status of this game.
   * @param t the new statuse for draw or not
   */
  public void setIsDraw(final boolean t) {
	  this.isDraw = t;
  }
  
  
  
  /**
   * add move if the given move is valid.
   * @param m move by the player
   * @return true if move is added, false otherwise
   */
  public boolean addMove(final Move m) {
	  // check if move is valid
	  if (!this.checkMove(m)) {
		  return false;
	  }
	  
	  // add the move and return true
	  int x = m.getMX();
	  int y = m.getMY();
	  
	  this.boardState[x][y] = m.getPlayer().getType();
	  // update turn 
	  if (m.getPlayer().getId() == 1) {
		  this.turn = 2;
	  } else {
		  this.turn = 1;
	  }
	  
	  // update moves
	  this.moves++;
	  
	  // check if we have a winner
	  // update our gameboard if there is winner or draw 
	  int winer = this.checkWinner();
	  if (winer != -1) {
		  this.winner = winer;
		  return true;
	  }
	  
	  // otherwise check isDraw
	  if (this.moves == MAX_MOVES) {
		  this.isDraw = true;
	  }
	  
	  return true;
  }
  
  /**
   * check if this game has a winner.
   * @return the id of the winner, -1 if no winner 
   */
  public int checkWinner() {
	  // check player 1
	  if (this.checkChar(this.p1.getType())) {
		  return this.p1.getId();
	  }
	  
	  if (this.checkChar(this.p2.getType())) {
		  return this.p2.getId();
	  }
	  
	  return -1;
  }
  
  /**
   * helper method for check winner
   * check if the given char has 3 in a row, or column, or diagonal.
   * @pre the c must be X or O 
   * @param c the char we are checking 
   * @return true if the given c met the requirement, false otherwise
   */
  private boolean checkChar(final char c) {
	  // check row
	  for (int x = 0; x < DIMENSION; x++) {
		  if ((this.boardState[x][0] == this.boardState[x][1])
			&& (this.boardState[x][1] == this.boardState[x][2])
			&& (this.boardState[x][2] == c)) {
			  return true;
		  } 
	  }
	  
	  // check column 
	  for (int y = 0; y < DIMENSION; y++) {
		  if ((this.boardState[0][y] == this.boardState[1][y]) 
			&& (this.boardState[1][y] == this.boardState[2][y])
			&& (this.boardState[2][y] == c)) {
			  return true;
		  } 
	  }
	  
	  // check diagonal
	  if ((this.boardState[0][0] == this.boardState[1][1])
			  && this.boardState[1][1] == this.boardState[2][2]
			  && this.boardState[1][1] == c) {
		  return true;
	  }
	  
	  if ((this.boardState[0][2] == this.boardState[1][1])
			  && this.boardState[1][1] == this.boardState[2][0]
			  && this.boardState[1][1] == c) {
		  return true;
	  }
	  
	  // otherwise return false
	  return false;
  }
  
 /**
  * check if the given move is valid for this gameboard.
  * @param m the move that player made 
  * @return true if the move is valid, false otherwise
  */
  private boolean checkMove(final Move m) {
	  int mx = m.getMX();
	  int my = m.getMY();
	  Player p = m.getPlayer();
	  
	  // check if it is this player's turn
	  if (p.getId() != this.turn) {
		  return false;
	  }
	  
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
	  res += "\"boardState\": [";
	  
	  for (int i = 0; i < DIMENSION; i++) {
		  res += "[";
		  for (int j = 0; j < DIMENSION; j++) {
			  res += "\"";
			  int state = (int) this.boardState[i][j];
			  res += String.format("\\u%04x", state);
			  res += "\"";
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
	  if (this.isDraw) {
		  res += "true";
	  } else {
		  res += "false";
	  }
	  
	  
	  res += "}";

	  return res;
  }
}
