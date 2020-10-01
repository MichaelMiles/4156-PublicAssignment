package unit;

import models.GameBoard;
import models.Player;
import models.Move;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.Test;

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
		Move m1= new Move(p1, -1, 0);
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


}
