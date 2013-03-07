package no.henning.restful.model;

import no.henning.restful.model.annotation.Named;

public class ResponseErrorMessage {

	@Named("message")
	private String message = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
