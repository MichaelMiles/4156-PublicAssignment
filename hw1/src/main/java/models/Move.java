package models;

public class Move {

  /**
   * the player who makes this move.
   */
  private Player player;

  /**
   * the x coordinate of this move.
   */
  private int moveX;

  /**
   * the y coordinate of this move.
   */
  private int moveY;

  /**
   * Construct a Move of player p with given x and y coordinate 
   * in the gameboard.
   * @param p the player who plays the move 
   * @param mx the x coordinate of this move on gameboard
   * @param my the y coordinate of this move on gameboard
   */
  public Move(final Player p, final int mx, final int my) {
	  this.player = p;
	  this.moveX = mx;
	  this.moveY = my;
  }
  
  // a set of getters 
  
  /**
   * @return the player who made this moves
   */
  public Player getPlayer() {
	  return this.player;
  }
  
  /**
   * 
   * @return the x coordinate on gameboard of this move
   */
  public int getMX() {
	  return this.moveX;
  }
  
  /**
   * 
   * @return the y coordinate on gameboard of this move
   */
  public int getMY() {
	  return this.moveY;
  }
  
  // a set of setters
  
  /**
   * Reset this move's player with the given player.
   * @param p the player who needs to be assigned to this move
   */
  public void setPlayer(final Player p) {
	  this.player = p;
  }
  
  /**
   * reset the x coordinate of this move with the given mx.
   * @param mx the new x coordinate of this move
   */
  public void setMX(final int mx) {
	  this.moveX = mx;
  }
  
  /**
   * reset the y coordinate of this move with the given my.
   * @param my the new y coordinate of this move
   */
  public void setMY(final int my) {
	  this.moveY = my;
  }
  
  
}
