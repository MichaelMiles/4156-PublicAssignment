package models;

public class Player {

  private char type;

  private int id;
  
  /**
   * Create a player with the given type and ID 
   * @param type the type of the player
   * @param id the id of the player 
   */
  public Player(char type, int id) {
	  this.type = type;
	  this.id = id;
  }
  
  // a set of getters 
  
  /**
   * Getter for type
   * @return the type of this player
   */
  public char getType() {
	  return this.type;
  }
  
  /**
   * Getter for ID
   * @return the id of the player
   */
  public int getId() {
	  return this.id;
  }
  
  // a set of setters
  
  
  /**
   * Reset this player's type with the given type
   * @param type the type that needs to be set to this player
   */
  public void setType(char type) {
	  this.type = type;
  }
  
  /**
   * Reset tis player's id with the given id
   * @param id the id that needs to be assigned to this player
   */
  public void setId(int id) {
	  this.id = id;
  }
  
  
  /**
   * 
   * @return this player object in Json format
   */
  public String toJson() {
	  String res = "";
      res += "\"p" + id + "\"" ;
	  res += ": {\n";
	  res += "\t\"type\": " + "\"" + type + "\"" + ","; 
	  res += "\t\"id\": " + id; 
	  res += "}";
	  
	  return res;
  }

}
