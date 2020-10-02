/**
 * the package includes all the controllers for this application.
 */

package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
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
    }).start(PORT_NUMBER);


    /*
     * 
     * Redirect to tictactoe for GET request
     */
    app.get("/newgame", ctx -> {
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
    app.stop();
  }
}
