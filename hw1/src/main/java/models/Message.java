package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  
  public Message(boolean m, int code, String msg) {
	  this.moveValidity = m;
	  this.code = code;
	  this.message = msg;
  }
  
  
  // a set of getters
  public boolean getMoveValidity() {
	  return this.moveValidity;
  }
  
  public int getCode() {
	  return this.code;
  }
  
  public String getMessage() {
	  return this.message;
  }
  
  // a set of setters
  public void setMoveValidity(boolean m) {
	  this.moveValidity = m;
  }
  
  public void setCode(int c) {
	  this.code = c;
  }
  
  public void setMessage(String msg) {
	  this.message = msg;
  }
}
