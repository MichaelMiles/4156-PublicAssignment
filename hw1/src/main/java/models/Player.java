package models;

public class Player {

  private char type;

  private int id;
  
  /**
   * 
   * @return this player object in Json format
   */
  public String toJson() {
	  String res = "";
      res += "\"p" + id + "\"" ;
	  res += ": {\n";
	  res += "\t\"type\": " + "\"" + type + "\""; 
	  res += "\t\"id\": " + "\"" + id + "\""; 
	  res += "}";
	  
	  return res;
  }

}
