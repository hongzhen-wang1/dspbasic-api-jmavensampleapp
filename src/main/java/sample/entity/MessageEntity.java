package sample.entity;

public class MessageEntity {

	private String message = null;

	public MessageEntity() {
		super();
	}

	public MessageEntity(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageEntity [message=" + message + "]";
	}

}
