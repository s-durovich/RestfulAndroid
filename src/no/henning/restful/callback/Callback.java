package no.henning.restful.callback;

import no.henning.restful.http.status.HttpRestResponse;

public interface Callback<T> 
{
	void onSuccess(T response);
	void onError(HttpRestResponse response);
}
