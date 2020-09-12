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
}
