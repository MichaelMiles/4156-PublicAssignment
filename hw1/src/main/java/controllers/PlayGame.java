/**
 * the package includes all the controllers for this application.
 */

package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;


public final class PlayGame {

  /**
   * the port this game is hosted on.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * the default value for code in response message.
   */
  private static final int CODE = 100;

  /**
   * the interface.
   */
  private static Javalin app;

  /**
   * database interface.
   */
  private static Connection conn;

  /**
   * the gameboard.
   */
  private static GameBoard gameboard;

  /**
   * gson.
   */
  private static Gson gson;


  /**
   * private constructor.
   */
  private PlayGame() {
    // private constructor
  }

  /**
   * Main method of the application.
   * 
   * @param args Command line arguments
   */
  public static void main(final String[] args) {


    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
      try {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:game.db");
        conn.setAutoCommit(false);
        // try to recover from potential crash by checking if such table exists
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "GAME", null);
        if (tables.next()) {
          // we restore
          gameboard = new GameBoard();
          gson = new Gson();
          gameboard.restoreState(conn);
        }
        tables.close();
      } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
      }
      System.out.println("Opened database successfully");
    }).start(PORT_NUMBER);



    /*
     * 
     * Redirect to tictactoe for GET request
     */
    app.get("/newgame", ctx -> {
      // initialize our database and clean table GAME
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
      ctx.redirect("/tictactoe.html");
    });



    /*
     * handle POST request for first player
     */
    app.post("/startgame", ctx -> {
      // initialize our gameboard with player 1
      gameboard = new GameBoard();
      gson = new Gson();

      String type = ctx.body();
      char t = 'O';
      if (type.equals("type=X")) {
        t = 'X';
      }
      // set player 1 in our gameboard
      gameboard.setP1(new Player(t, 1));

      // update the db
      try {
        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO GAME(moves, playerone) values (0, \'" + t + "\');";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
      } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
      }

      // send back json response
      ctx.result(gson.toJson(gameboard));
    });

    /*
     * handle JoinGame GET request from second player
     */
    app.get("/joingame", ctx -> {
      ctx.redirect("/tictactoe.html?p=2");
      // set up player2 and update our gameboard
      char p1Type = gameboard.getP1().getType();
      char tmp = 'O';
      if (p1Type == 'O') {
        tmp = 'X';
      }
      gameboard.setP2(new Player(tmp, 2));
      gameboard.setGameStarted(true);
      // update the db
      try {
        Statement stmt = conn.createStatement();
        String sql = "UPDATE GAME SET playertwo = \'" + tmp + "\', gamestarted = true ;";
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
      } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
      }
      sendGameBoardToAllPlayers(gson.toJson(gameboard));
    });

    /*
     * update the gameboard if the given move is valid
     */
    app.post("/move/:playerId", ctx -> {
      String playerId = ctx.pathParam("playerId");
      int id = Integer.parseInt(playerId);

      String[] param = ctx.body().split("&");
      String x1 = param[0].split("=")[1];
      int x = Integer.parseInt(x1);

      String y1 = param[1].split("=")[1];
      int y = Integer.parseInt(y1);

      Player player = ((id == 1) ? gameboard.getP1() : gameboard.getP2());
      Move move = new Move(player, x, y);

      // try to add move
      Message msg;

      if (gameboard.addMove(move)) {
        // added successfully
        // we update the database first
        String sql = "INSERT INTO GAME VALUES(";
        sql += "\'" + gameboard.getP1().getType() + "\',";
        sql += "\'" + gameboard.getP2().getType() + "\',";
        sql += gameboard.getGameStarted() + ",";
        sql += gameboard.getTurn() + ",";
        sql += "\'";
        char[][] gameState = gameboard.getBoardState();
        for (int i = 0; i < gameboard.DIMENSION; i++) {
          for (int j = 0; j < gameboard.DIMENSION; j++) {
            if (gameState[i][j] != '\u0000') {
              sql += gameState[i][j];
            } else {
              sql += GameBoard.mc;
            }
          }
        }
        sql += "\',";
        sql += gameboard.getWinner() + ",";
        sql += gameboard.getIsDraw() + ",";
        sql += gameboard.getMoves() + ");";


        try {
          Statement stmt = conn.createStatement();
          stmt.executeUpdate(sql);
          stmt.close();
          conn.commit();
        } catch (Exception e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
          System.exit(0);
        }

        msg = new Message(true, CODE, "");
      } else {
        // move invalid
        msg = new Message(false, CODE, "Move Invalid");
      }
      // send message
      ctx.result(gson.toJson(msg));
      // update our gameboard
      sendGameBoardToAllPlayers(gson.toJson(gameboard));
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /**
   * Send message to all players.
   * 
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
        System.out.println("something went wrong");
      }
    }
  }

  /**
   * stop the app.
   */
  public static void stop() {
    try {
      conn.close();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    app.stop();
  }
}
