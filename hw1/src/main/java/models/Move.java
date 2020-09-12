package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;

  public Move(Player p, int mx, int my) {
	  this.player = p;
	  this.moveX = mx;
	  this.moveY = my;
  }
  
  // a set of getters 
  public Player getPlayer() {
	  return this.player;
  }
  
  public int getMX() {
	  return this.moveX;
  }
  
  public int getMY() {
	  return this.moveY;
  }
  
  // a set of setters
  public void setPlayer(Player p) {
	  this.player = p;
  }
  
  public void setMX(int mx) {
	  this.moveX = mx;
  }
  
  public void setMY(int my) {
	  this.moveY = my;
  }
  
  
}
