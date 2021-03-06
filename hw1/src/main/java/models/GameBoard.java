/**
 * all the models for back-end.
 */

package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameBoard {

  /**
   * the most total moves players can make on this game board.
   */
  static final int MAX_MOVES = 9;

  /**
   * the length and width of this board.
   */
  public static final int DIMENSION = 3;

  /**
   * special char to trick db.
   */
  public static final char mc = '1';

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
   * return the first player of this game.
   */
  public Player getP1() {
    return p1;
  }

  /**
   * return the second player of this game.
   */
  public Player getP2() {
    return p2;
  }

  /**
   * return if the game has started or not.
   */
  public boolean getGameStarted() {
    return this.gameStarted;
  }

  /**
   * return the id of the player who plays this turn.
   */
  public int getTurn() {
    return this.turn;
  }

  /**
   * the board states.
   */
  public char getBoardState(int i, int j) {
    return this.boardState[i][j];
  }

  /**
   * the moves.
   */
  public int getMoves() {
    return this.moves;
  }


  /**
   * return the id of the winner.
   */
  public int getWinner() {
    return this.winner;
  }

  /**
   * return if the game is a draw or not.
   * 
   * @return if the game is a draw or not.
   */
  public boolean getIsDraw() {
    return this.isDraw;
  }


  // a set of setters

  /**
   * set player 1 with the given player.
   * 
   * @param p the new player for 1st player
   */
  public void setP1(final Player p) {
    this.p1 = p;
  }


  /**
   * set player 2 with the given player.
   * 
   * @param p the new player for 2nd player
   */
  public void setP2(final Player p) {
    this.p2 = p;
  }

  /**
   * set the status of the game(started or not).
   * 
   * @param t the new status
   */
  public void setGameStarted(final boolean t) {
    this.gameStarted = t;
  }
  
  /**
   * set the board states.
   */
  public char setBoardState(int i, int j, char val) {
    return this.boardState[i][j] = val;
  }
  
  /**
   * set the id of the player who should play this turn.
   * 
   * @param t the new assigned turn
   */
  public void setTurn(final int t) {
    this.turn = t;
  }

  /**
   * set the id of the winner.
   * 
   * @param winer the new winner of this game
   */
  public void setWinnder(final int winer) {
    this.winner = winer;
  }

  /**
   * set the draw status of this game.
   * 
   * @param t the new statuse for draw or not
   */
  public void setIsDraw(final boolean t) {
    this.isDraw = t;
  }


  /**
   * add move if the given move is valid.
   * 
   * @param move move by the player
   * @return true if move is added, false otherwise
   */
  public boolean addMove(final Move move) {
    // check if move is valid
    // could add on if max moves has been reached
    if (!this.checkMove(move)) {
      return false;
    }

    // add the move and return true
    int x = move.getMX();
    int y = move.getMY();

    this.boardState[x][y] = move.getPlayer().getType();
    // update turn
    if (move.getPlayer().getId() == 1) {
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
   * 
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
   * helper method for check winner check if the given char has 3 in a row, or column, or diagonal.
   * 
   * @pre the c must be X or O
   * @param c the char we are checking
   * @return true if the given c met the requirement, false otherwise
   */
  private boolean checkChar(final char c) {
    // check row
    for (int x = 0; x < DIMENSION; x++) {
      if ((this.boardState[x][0] == this.boardState[x][1])
          && (this.boardState[x][1] == this.boardState[x][2]) && (this.boardState[x][2] == c)) {
        return true;
      }
    }

    // check column
    for (int y = 0; y < DIMENSION; y++) {
      if ((this.boardState[0][y] == this.boardState[1][y])
          && (this.boardState[1][y] == this.boardState[2][y]) && (this.boardState[2][y] == c)) {
        return true;
      }
    }

    // check diagonal
    if ((this.boardState[0][0] == this.boardState[1][1])
        && this.boardState[1][1] == this.boardState[2][2] && this.boardState[1][1] == c) {
      return true;
    }

    if ((this.boardState[0][2] == this.boardState[1][1])
        && this.boardState[1][1] == this.boardState[2][0] && this.boardState[1][1] == c) {
      return true;
    }

    // otherwise return false
    return false;
  }

  /**
   * check if the given move is valid for this gameboard.
   * 
   * @param m the move that player made
   * @return true if the move is valid, false otherwise
   */
  private boolean checkMove(final Move move) {
    // check if we have a winner or draw
    if ((this.winner != 0)) {
      return false;
    }
    if (this.isDraw) {
      return false;
    }
    if (this.p1 == null) {
      return false;
    }
    if (this.p2 == null) {
      return false;
    }

    Player player = move.getPlayer();

    // check if it is this player's turn
    if (player.getId() != this.turn) {
      return false;
    }

    char c = 'X';
    if (this.turn == 1) {
      c = this.p1.getType();
    } else {
      c = this.p2.getType();
    }
    // check if the player is valid player
    // can't be reached by integration tests
    if (player.getType() != c) {
      return false;
    }

    int mx = move.getMX();
    int my = move.getMY();

    // check if mx or my out of bound
    if (mx < 0 || mx > 2 || my < 0 || my > 2) {
      return false;
    }

    // check if the given coordinate has been taken
    if (this.boardState[mx][my] != '\u0000') {
      return false;
    }

    return true;
  }

  /**
   * restore the gameboard state from the connection to some db.
   * 
   * @param conn the connection to the db
   */
  public void restoreState(Connection conn) {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs =
          stmt.executeQuery("SELECT * FROM GAME WHERE MOVES = " + "(select MAX(moves) from GAME);");
      if (!rs.next()) {
        return;
      }
      String player1 = rs.getString("PLAYERONE");
      String player2 = rs.getString("PLAYERTWO");
      if (player1 != null) {
        this.p1 = new Player(player1.charAt(0), 1);
      }
      if (player2 != null) {
        this.p2 = new Player(player2.charAt(0), 2);
      }
      this.isDraw = rs.getBoolean("ISDRAW");
      this.moves = rs.getInt("MOVES");
      this.winner = rs.getInt("WINNER");
      this.gameStarted = rs.getBoolean("GAMESTARTED");
      this.turn = rs.getInt("TURN");
      String bstate = rs.getString("BOARDSTATE");
      if (bstate != null) {
        for (int i = 0; i < DIMENSION; i++) {
          for (int j = 0; j < DIMENSION; j++) {
            char tmp = bstate.charAt(i * 3 + j);
            if (tmp != this.mc) {
              this.boardState[i][j] = tmp;
            }
          }
        }
      }
      stmt.close();
      rs.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (rs != null) {
            rs.close();
          }
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }


}
