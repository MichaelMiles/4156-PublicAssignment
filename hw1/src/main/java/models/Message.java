package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * construct a new message 
   * @param m the validity of some move
   * @param code the message code 
   * @param msg the content of the message
   */
  public Message(boolean m, int code, String msg) {
	  this.moveValidity = m;
	  this.code = code;
	  this.message = msg;
  }
  
  
  // a set of getters
  
  /**
   * 
   * @return if the associated move with this message
   *  		  is valid or not
   */
  public boolean getMoveValidity() {
	  return this.moveValidity;
  }
  
  /**
   * 
   * @return the message code
   */
  public int getCode() {
	  return this.code;
  }
  
  /**
   * 
   * @return the content of the message
   */
  public String getMessage() {
	  return this.message;
  }
  
  // a set of setters
  
  /**
   * reset the validity of some move with the given boolean
   * @param m the new validity
   */
  public void setMoveValidity(boolean m) {
	  this.moveValidity = m;
  }
  
  /**
   * reset the message code
   * @param c the new message code
   */
  public void setCode(int c) {
	  this.code = c;
  }
  
  /**
   * reset the new message
   * @param msg the new message content
   */
  public void setMessage(String msg) {
	  this.message = msg;
  }
  
  /**
   * 
   * @return the json msg of this message
   */
  public String getJson() {
	  return "{\"moveValidity\": " + this.moveValidity + ", \"code\": " + this.code + 
			  ", \"message\": \"" + this.message + "\"}";
  }
}
