package no.henning.restful.http.callback;

import no.henning.restful.http.status.HttpRestResponse;

public interface HttpRestClientResponseCallback
{
	void onDone(HttpRestResponse response);
}
