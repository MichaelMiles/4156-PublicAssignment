package models;

public class Player {

  /**
   * the type X or O of this player.
   */
  private char type;

  /**
   * the id of this player.
   */
  private int id;
  
  /**
   * Create a player with the given type and ID.
   * @param newType the type of the player
   * @param identity the id of the player 
   */
  public Player(final char newType, final int identity) {
	  this.type = newType;
	  this.id = identity;
  }
  
  // a set of getters 
  
  /**
   * Getter for type.
   * @return the type of this player
   */
  public char getType() {
	  return this.type;
  }
  
  /**
   * Getter for ID.
   * @return the id of the player
   */
  public int getId() {
	  return this.id;
  }
  
  // a set of setters

  /**
   * Reset this player's type with the given type.
   * @param newType the type that needs to be set to this player
   */
  public void setType(final char newType) {
	  this.type = newType;
  }
  
  /**
   * Reset this player's id with the given id.
   * @param identity the id that needs to be assigned to this player
   */
  public void setId(final int identity) {
	  this.id = identity;
  }
  
  
  /**
   * 
   * @return this player object in Json format
   */
  public String toJson() {
	  String res = "";
      res += "\"p" + id + "\"";
	  res += ": {\n";
	  res += "\t\"type\": " + "\"" + type + "\"" + ","; 
	  res += "\t\"id\": " + id; 
	  res += "}";
	  
	  return res;
  }

}
