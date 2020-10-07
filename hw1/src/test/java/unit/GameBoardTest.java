package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import models.GameBoard;
import models.Move;
import models.Player;

@TestMethodOrder(OrderAnnotation.class)
class GameBoardTest {

  /**
   * initialize a gameBoard
   */
  static GameBoard g = new GameBoard();
  static Player p1 = new Player('X', 1);
  static Player p2 = new Player('O', 2);

  @BeforeAll
  public static void initGameBoard() {
    g.setP1(p1);
    g.setP2(p2);
  }



  @Test
  @Order(1)
  void testAddMoveWithInvalidTurn() {
    // test add move from player who is not
    // valid in this turn
    Move m = new Move(p2, 0, 0);
    assertFalse(g.addMove(m));
  }

  @Test
  @Order(2)
  void testAddMoveWithInvalidPlayer() {
    // test add move from player with invalid id
    Move m = new Move(new Player('O', 1), 0, 0);
    assertFalse(g.addMove(m));
    g.setTurn(2);
    m = new Move(new Player('X', 2), 0, 0);
    assertFalse(g.addMove(m));
    // reset Turn
    g.setTurn(1);
  }

  @Test
  @Order(3)
  void testAddMoveWithInvalidMove() {
    // test four cases for adding moves with
    // coordinates out of bounds
    Move m1 = new Move(p1, -1, 0);
    Move m2 = new Move(p1, 3, 0);
    Move m3 = new Move(p1, 0, -1);
    Move m4 = new Move(p1, 0, 3);

    assertFalse(g.addMove(m1));
    assertFalse(g.addMove(m2));
    assertFalse(g.addMove(m3));
    assertFalse(g.addMove(m4));
  }

  @Test
  @Order(4)
  void testAddMove() {
    Move m = new Move(p1, 0, 0);
    assertTrue(g.addMove(m));
  }

  @Test
  @Order(5)
  void testAddDuplicateMove() {
    Move m = new Move(p2, 0, 0);
    assertEquals(g.getTurn(), 2);
    assertFalse(g.addMove(m));
  }

  @Test
  @Order(6)
  void testCheckWinnerInRow() {
    Move m = new Move(p2, 1, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 0, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 0, 2);
    assertTrue(g.addMove(m));
    assertEquals(g.checkWinner(), 1);
    assertEquals(g.getWinner(), 1);
    assertFalse(g.getIsDraw());
    // test if we can add another move after winner
    m = new Move(p2, 2, 1);
    assertFalse(g.addMove(m));
  }

  @Test
  @Order(7)
  void testCheckWinnerInColumn() {
    g = new GameBoard();
    this.initGameBoard();
    assertEquals(g.getWinner(), 0);
    Move m = new Move(p1, 0, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 0, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 0, 2);
    assertTrue(g.addMove(m));
    m = new Move(p2, 1, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 1, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 0);
    assertTrue(g.addMove(m));
    assertEquals(g.checkWinner(), 2);
    assertEquals(g.getWinner(), 2);
    assertFalse(g.getIsDraw());
  }


  @Test
  @Order(8)
  void testCheckWinnerInDiagonal() {
    g = new GameBoard();
    this.initGameBoard();
    assertEquals(g.getWinner(), 0);
    Move m = new Move(p1, 0, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 0, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 0, 2);
    assertTrue(g.addMove(m));
    m = new Move(p2, 1, 1);
    assertTrue(g.addMove(m));
    m = new Move(p1, 1, 0);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 2);
    assertTrue(g.addMove(m));
    assertEquals(g.checkWinner(), 2);
    assertEquals(g.getWinner(), 2);
    assertFalse(g.getIsDraw());
  }


  @Test
  @Order(9)
  void testCheckWinnerInBackDiagonal() {
    g = new GameBoard();
    this.initGameBoard();
    assertEquals(g.getWinner(), 0);
    Move m = new Move(p1, 0, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 0, 2);
    assertTrue(g.addMove(m));
    m = new Move(p1, 2, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 1, 1);
    assertTrue(g.addMove(m));
    m = new Move(p1, 1, 0);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 0);
    assertTrue(g.addMove(m));
    assertEquals(g.checkWinner(), 2);
    assertEquals(g.getWinner(), 2);
    assertFalse(g.getIsDraw());
    // test if we can add another move after winner
    m = new Move(p1, 2, 1);
    assertFalse(g.addMove(m));
  }

  @Test
  @Order(8)
  void testCheckIsDraw() {
    g = new GameBoard();
    this.initGameBoard();
    assertEquals(g.getWinner(), 0);
    Move m = new Move(p1, 0, 0);
    assertTrue(g.addMove(m));
    m = new Move(p2, 0, 1);
    assertTrue(g.addMove(m));
    m = new Move(p1, 0, 2);
    assertTrue(g.addMove(m));
    m = new Move(p2, 1, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 1, 1);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 2);
    assertTrue(g.addMove(m));
    m = new Move(p1, 1, 2);
    assertTrue(g.addMove(m));
    m = new Move(p2, 2, 0);
    assertTrue(g.addMove(m));
    m = new Move(p1, 2, 1);
    assertTrue(g.addMove(m));
    assertEquals(g.checkWinner(), -1);
    assertEquals(g.getWinner(), 0);
    assertTrue(g.getIsDraw());
    // test if we can add another move after isDraw
    m = new Move(p2, 2, 1);
    assertFalse(g.addMove(m));
  }

  @Test
  @Order(9)
  void testAddMoveWithNoPlaye1() {
    g = new GameBoard();
    assertFalse(g.addMove(new Move(p1, 0, 0)));
  }

  @Test
  @Order(10)
  void testAddMoveWithNoPlaye2() {
    g = new GameBoard();
    g.setP1(p1);
    assertFalse(g.addMove(new Move(p2, 0, 0)));
  }

  @Test
  @Order(11)
  void testRestoreStateWithEmptyTable() {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection conn;
      conn = DriverManager.getConnection("jdbc:sqlite:game.db");
      conn.setAutoCommit(false);
      Statement stmt;
      stmt = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS GAME";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE GAME " + "(PLAYERONE      CHAR(1)," + " PLAYERTWO      CHAR(1), "
          + " GAMESTARTED    BOOLEAN  DEFAULT  0, " + " TURN           INT      DEFAULT  1, "
          + " BOARDSTATE     CHAR(9), " + " WINNER         INT      DEFAULT  0, "
          + " ISDRAW         BOOLEAN  DEFAULT  0, "
          + " MOVES          INT   PRIMARY KEY   DEFAULT  0);";
      stmt.executeUpdate(sql);
      stmt.close();
      conn.commit();
      GameBoard gameboard = new GameBoard();
      gameboard.restoreState(conn);
      assertEquals(null, gameboard.getP1());
      assertEquals(null, gameboard.getP2());
      assertEquals('\u0000', gameboard.getBoardState()[0][1]);
      assertFalse(gameboard.getGameStarted());
      assertEquals(gameboard.getTurn(), 1);
      assertEquals(gameboard.getWinner(), 0);
      assertFalse(gameboard.getIsDraw());
      assertEquals(gameboard.getMoves(), 0);
      conn.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  @Test
  @Order(12)
  void testRestoreStateWithEmptyData() {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection conn;
      conn = DriverManager.getConnection("jdbc:sqlite:game.db");
      conn.setAutoCommit(false);
      Statement stmt;
      stmt = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS GAME";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE GAME " + "(PLAYERONE      CHAR(1)," + " PLAYERTWO      CHAR(1), "
          + " GAMESTARTED    BOOLEAN  DEFAULT  0, " + " TURN           INT      DEFAULT  1, "
          + " BOARDSTATE     CHAR(9), " + " WINNER         INT      DEFAULT  0, "
          + " ISDRAW         BOOLEAN  DEFAULT  0, "
          + " MOVES          INT   PRIMARY KEY   DEFAULT  0);";
      sql += "INSERT INTO GAME(MOVES) VALUES(0);";
      stmt.executeUpdate(sql);
      stmt.close();
      conn.commit();

      GameBoard gameboard = new GameBoard();
      gameboard.restoreState(conn);
      assertEquals(null, gameboard.getP1());
      assertEquals(null, gameboard.getP2());
      assertEquals('\u0000', gameboard.getBoardState()[0][1]);
      assertFalse(gameboard.getGameStarted());
      assertEquals(gameboard.getTurn(), 1);
      assertEquals(gameboard.getWinner(), 0);
      assertFalse(gameboard.getIsDraw());
      assertEquals(gameboard.getMoves(), 0);
      conn.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  @Test
  @Order(12)
  void testRestoreStateWithData() {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection conn;
      conn = DriverManager.getConnection("jdbc:sqlite:game.db");
      conn.setAutoCommit(false);
      Statement stmt;
      stmt = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS GAME";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE GAME " + "(PLAYERONE      CHAR(1)," + " PLAYERTWO      CHAR(1), "
          + " GAMESTARTED    BOOLEAN  DEFAULT  0, " + " TURN           INT      DEFAULT  1, "
          + " BOARDSTATE     CHAR(9), " + " WINNER         INT      DEFAULT  0, "
          + " ISDRAW         BOOLEAN  DEFAULT  0, "
          + " MOVES          INT   PRIMARY KEY   DEFAULT  0);";
      sql += "INSERT INTO GAME(PLAYERONE, PLAYERTWO, BOARDSTATE) VALUES('X', 'O', '1O1111111');";
      stmt.executeUpdate(sql);
      stmt.close();
      conn.commit();

      GameBoard gameboard = new GameBoard();
      gameboard.restoreState(conn);
      assertEquals('X', gameboard.getP1().getType());
      assertEquals('O', gameboard.getP2().getType());
      assertEquals('O', gameboard.getBoardState()[0][1]);
      assertFalse(gameboard.getGameStarted());
      assertEquals(gameboard.getTurn(), 1);
      assertEquals(gameboard.getWinner(), 0);
      assertFalse(gameboard.getIsDraw());
      assertEquals(gameboard.getMoves(), 0);
      conn.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

}
