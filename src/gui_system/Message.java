package gui_system;

public class Message {
	
	private Object objReference;
	private String message;
	
	public Message(Object objReference, String message) {
		this.objReference = objReference;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Object getObjReference() {
		return objReference;
	}

}
