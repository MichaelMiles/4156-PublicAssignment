package controllers;

import java.io.IOException;
import io.javalin.Javalin;
import java.util.Queue;

import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;

import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  /**
   * the port this game is hosted on.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * the location of the X coordinate in move message.
   */
  private static final int X_LOCATION = 2;
  
  /**
   * the location of the y coordinate in move message.
   */
  private static final int Y_LOCATION = 6;
  
  /**
   * the default value for code in response message.
   */
  private static final int CODE = 100;
  
  /**
   * the interface.
   */
  private static Javalin app;

  /** Main method of the application.
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
     * initialize the gameboard
     */
    GameBoard g = new GameBoard();

    
    /*
     * handle POST request for first player 
     */
    app.post("/startgame", ctx -> {
    	// initialize our gameboard with player 1
    	String type = ctx.body();
    	char t = 'O';
    	if (type.equals("type=X")) {
    		t = 'X';
    	} 
    	// set player 1 in our gameboard
    	g.setP1(new Player(t, 1));
    	
    	// send back json response
    	ctx.result(g.toJson());
    });
   
    /*
     * handle JoinGame GET request from second player
     */
    app.get("/joingame", ctx -> {
    	ctx.redirect("/tictactoe.html?p=2");
    	// set up player2 and update our gameboard
    	char p1Type = g.getP1().getType();
    	char tmp = 'O';
    	if (p1Type == 'O') {
    		tmp = 'X';
    	}
    	g.setP2(new Player(tmp, 2));
    	g.setGameStarted(true);
    	sendGameBoardToAllPlayers(g.toJson());
    });

    /*
     * update the gameboard if the given move is valid
     */
    app.post("/move/:playerId", ctx -> {
    	String player = ctx.pathParam("playerId");
    	int id = Integer.parseInt(player);

    	int x = Integer.parseInt(ctx.body().charAt(X_LOCATION) + "");
    	int y = Integer.parseInt(ctx.body().charAt(Y_LOCATION) + "");

    	Player p = ((id == 1) ? g.getP1() : g.getP2());
    	Move m = new Move(p, x, y);

    	// try to add move
    	Message msg;
    	if (g.addMove(m)) {
    		// added successfully
    		msg = new Message(true, CODE, "");
    	} else {
    		// move invalid
    		msg = new Message(false, CODE, "Move Invalid");
    	}
    	// send message
    	ctx.result(msg.getJson());
    	// update our gameboard
    	sendGameBoardToAllPlayers(g.toJson());
	
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
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

  public static void stop() {
    app.stop();
  }
}
