package models;

public class Player {

  private char type;

  private int id;
  
  
  public Player(char type, int id) {
	  this.type = type;
	  this.id = id;
  }
  
  // a set of getters 
  public char getType() {
	  return this.type;
  }
  
  public int getId() {
	  return this.id;
  }
  
  // a set of setters
  public void setType(char type) {
	  this.type = type;
  }
  
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
	  res += "\t\"id\": " + "\"" + id + "\""; 
	  res += "}";
	  
	  return res;
  }

}
