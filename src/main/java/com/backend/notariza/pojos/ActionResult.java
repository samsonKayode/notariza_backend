package com.backend.notariza.pojos;


public class ActionResult {
	
	private int status;
	
	private String message;
	
	private long date;
	
	public ActionResult() {
		
	}
	
	
	public ActionResult(int status, String message, long date) {
		super();
		this.status = status;
		this.message = message;
		this.date = date;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	

}
