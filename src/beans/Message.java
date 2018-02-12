package beans;

import java.io.Serializable;

public class Message implements Serializable {
	private String sender;
	private String reciever;
	private String content;

	private boolean read;
	
	public Message(){}

	

	public String getSender() {
		return sender;
	}



	public void setSender(String sender) {
		this.sender = sender;
	}



	public String getReciever() {
		return reciever;
	}



	public void setReciever(String reciever) {
		this.reciever = reciever;
	}



	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
	
}
