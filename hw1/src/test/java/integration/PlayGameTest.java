package integration;

import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import models.Message;
import models.Move;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.gson.Gson;

import org.junit.jupiter.api.Test;


@TestMethodOrder(OrderAnnotation.class) 
public class PlayGameTest {
	
    /**
    * Runs only once before the testing starts.
    */
    @BeforeAll
	public static void init() {
		// Start Server
    	PlayGame.main(null);
    }
	
    /**
    * This method starts a new game before every test run. It will run every time before a test.
    */
    @BeforeEach
    public void startNewGame() {
    	// Test if server is running. You need to have an endpoint /
        // If you do not wish to have this end point, it is okay to not have anything in this method.
    	HttpResponse response = Unirest.get("http://localhost:8080/").asString();
        int restStatus = response.getStatus();

    	System.out.println("Before Each, test server");
    }
	
    /**
    * This is a test case to evaluate the newgame endpoint.
    */
    @Test
    @Order(1)
    public void newGameTest() {
    	
    	// Create HTTP request and get response
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
    }
    
    /**
    * This is a test case to evaluate the startgame endpoint.
    */
    @Test
    @Order(2)
    public void startGameTest() {
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('X', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
    }
    
    @Test
    @Order(3)
    public void joinGameTest() {
    	HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
    	int restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    }
    
    @Test
    @Order(4)
    public void moveInInvalidTurnTest() {
    	
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to turn: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    	
    } 
    
    @Test
    @Order(5)
    public void moveValidTest() {
       	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Valid move: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertTrue(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    @Test
    @Order(6)
    public void moveInvalidCoordinatesXBiggerThan2Test() {
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=3&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to coordinates: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    }
    
    
    @Test
    @Order(7)
    public void moveInvalidCoordinatesXNegativeTest() {
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=-1&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to coordinates: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    }
    
    @Test
    @Order(8)
    public void moveInvalidCoordinatesYBiggerThan2Test() {
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=3").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to coordinates: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    }
    
    @Test
    @Order(9)
    public void moveInvalidCoordinatesYNegativeTest() {
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=-1").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to coordinates: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    }
    
    @Test
    @Order(10)
    public void moveInvalidDuplicateMove() {
    	// ---------------------------- one single move -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to coordinates: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    	assertEquals(m.getMessage(), "Move Invalid");
    }
    
    
    @Test
    @Order(11)
    public void endTheGameWithWinnerTest() {
    	Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    	HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
    	
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertTrue(m.getMoveValidity());
    	assertEquals(100, m.getCode());

    }
    
    /**
     * we test if we can make move when the game is over
     */
    @Test
    @Order(12)
    public void moveReachMaxMovesTest() {
       	// ---------------------------- many moves -------------------------------------/
    	// try invalid move from p2
    	HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    	String responseBody =  (String) response.getBody();
    	JSONObject jsonObject = new JSONObject(responseBody);
    	System.out.println("Invalid move due to max_moves: " + responseBody);
    	
    	// gson 
    	Gson gson = new Gson();
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
     	assertEquals(m.getMessage(), "Move Invalid");
    }
    

    /**
     * we test if we can add a player1 with 'O' and produce draw situation
     */
    @Test
    @Order(13)
    public void addPlayer1WithOCharTestAndTestIsDraw() {
    	// we reset
    	this.close();
    	this.init();
    	
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
        
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
        
        // join game
        response = Unirest.get("http://localhost:8080/joingame").asString();
        restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    	
    	// produce draw situation
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    	response =Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertTrue(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    		
    	// test if we can make move after draw
    	response =Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    

    
    /**
     * we test if a player can win in row
     */
    @Test
    @Order(14)
    public void rowWinnerTest() {
    	// we reset
    	this.close();
    	this.init();
    	
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
        
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
        
        // join game
        response = Unirest.get("http://localhost:8080/joingame").asString();
        restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    	
    	// produce draw situation
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    	response =Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    /**
     * we test if a player can win in col
     */
    @Test
    @Order(15)
    public void colWinnerTest() {
    	// we reset
    	this.close();
    	this.init();
    	
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
        
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
        
        // join game
        response = Unirest.get("http://localhost:8080/joingame").asString();
        restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    	
    	// produce draw situation
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    	response =Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    /**
     * we test if a player can win in diagonal
     */
    @Test
    @Order(15)
    public void diagonalWinnerTest() {
    	// we reset
    	this.close();
    	this.init();
    	
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
        
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
        
        // join game
        response = Unirest.get("http://localhost:8080/joingame").asString();
        restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    	
    	// produce draw situation
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    	response =Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    /**
     * we test if a player can win in another diagonal
     */
    @Test
    @Order(16)
    public void backDiagonalWinnerTest() {
    	// we reset
    	this.close();
    	this.init();
    	
        HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
        int restStatus = response.getStatus();
        
        // Check assert statement (New Game has started)
        assertEquals(restStatus, 200);
        System.out.println("Test New Game");
        
    	
    	// Create a POST request to startgame endpoint and get the body
        // Remember to use asString() only once for an endpoint call. Every time you call asString(), a new request will be sent to the endpoint. Call it once and then use the data in the object.
        response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
        String responseBody =  (String) response.getBody();
        
        // --------------------------- JSONObject Parsing ----------------------------------
        
        System.out.println("Start Game Response: " + responseBody);
        
        // Parse the response to JSON object
        JSONObject jsonObject = new JSONObject(responseBody);

        // Check if game started after player 1 joins: Game should not start at this point
        assertEquals(false, jsonObject.get("gameStarted"));
        
        // ---------------------------- GSON Parsing -------------------------
        
        // GSON use to parse data to object
        Gson gson = new Gson();
        GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
        Player player1 = gameBoard.getP1();
        
        // Check if player type is correct
        assertEquals('O', player1.getType());
        
        // check winner, isDraw, turn, id
        assertEquals(1, player1.getId());
        assertEquals(1, gameBoard.getTurn());
        assertEquals(0, gameBoard.getWinner());
        assertFalse(gameBoard.getIsDraw());
        
        System.out.println("Test Start Game");
        
        // join game
        response = Unirest.get("http://localhost:8080/joingame").asString();
        restStatus = response.getStatus();
    	assertEquals(restStatus, 200);
    	
    	// produce draw situation
    	Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    	Unirest.post("http://localhost:8080/move/2").body("x=0&y=1").asString();
    	Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    	response =Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    	
        responseBody =  (String) response.getBody();
        jsonObject = new JSONObject(responseBody);
    	System.out.println("valid moves: " + responseBody);
   
    	Message m = gson.fromJson(jsonObject.toString(), Message.class);

    	// check validity code and message
    	assertFalse(m.getMoveValidity());
    	assertEquals(100, m.getCode());
    }
    
    /**
    * This will run every time after a test has finished.
    */
    @AfterEach
    public void finishGame() {
    	System.out.println("After Each test");
    }
    
    /**
     * This method runs only once after all the test cases have been executed.
     */
    @AfterAll
    public static void close() {
	// Stop Server
    	PlayGame.stop();
    }
}