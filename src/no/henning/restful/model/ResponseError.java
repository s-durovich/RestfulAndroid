package no.henning.restful.model;

import no.henning.restful.model.annotation.Named;

public class ResponseError {

	@Named("error")
	private ResponseErrorMessage error;

	public ResponseErrorMessage getError() {
		return error;
	}

	public void setError(ResponseErrorMessage error) {
		this.error = error;
	}

}
