package no.henning.restful.http.builder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import no.henning.restful.RestfulApplication;
import no.henning.restful.converter.json.JsonWriter;
import no.henning.restful.credential.BasicAuthentication;
import no.henning.restful.credential.RestUserAgent;
import no.henning.restful.model.Model;
import no.henning.restful.service.RestService;
import no.henning.restful.utils.GenericHelper;
import no.henning.restful.utils.HttpHelper;

public class RestHttpRequestDetail {

	private Model model = null;
	private Class<? extends Model> modelClass;

	/**
	 * GET, POST, PUT, DELETE
	 */
	private String requestUrl;
	private String requestMethod;
	private String requestBody;

	public RestHttpRequestDetail(String url, String httpVerb, String body) {
		this(null, url, httpVerb, body);
	}

	public RestHttpRequestDetail(Class<? extends Model> modelClass, String url, String httpVerb, String body) {
		this.requestUrl = url;
		this.requestMethod = httpVerb;
		this.requestBody = body;
		this.modelClass = modelClass;
	}

	public RestHttpRequestDetail(Model model) {
		this.model = model;
		this.modelClass = model.getClass();
		this.requestBody = JsonWriter.from(model).toString();

		this.requestUrl = HttpHelper.getAbsolutePathFromModel(modelClass) + ".json";

		this.requestMethod = "GET";
	}

	public RestHttpRequestDetail(Model model, String httpVerb) {
		this(model);

		this.requestMethod = httpVerb;

	}

	public HttpUriRequest buildRequest() {
		HttpUriRequest request = HttpHelper.buildHttpUriRequestFromUrlAndMethod(requestUrl, requestMethod);

		addBasicAuthentication(request);
		addRestHeaders(request);

		try {
			if (requestMethod.equalsIgnoreCase("POST"))
				((HttpPost) request).setEntity(new StringEntity(requestBody, "UTF-8"));
			else if (requestMethod.equalsIgnoreCase("PUT"))
				((HttpPut) request).setEntity(new StringEntity(requestBody, "UTF-8"));
		} catch (UnsupportedEncodingException ex) {

		}

		return request;
	}

	/**
	 * Adds Content-Type and Accepts to JSON
	 * 
	 * @param request
	 */
	private void addRestHeaders(HttpUriRequest request) {
		request.setHeader("User-Agent", RestUserAgent.getUserAgentString());

		if (isNotGetOrDeleteRequest())
			request.setHeader("Content-Type", "application/json");

		request.setHeader("Accept", "application/json");
	}

	@SuppressLint("DefaultLocale")
	private boolean isNotGetOrDeleteRequest() {
		String methodUppercased = requestMethod.toUpperCase();

		if (methodUppercased == "GET")
			return false;

		return true;
	}

	private void addBasicAuthentication(HttpUriRequest request) {
		if (modelClass == null)
			return;

		if (BasicAuthentication.getPassword() == null) {
			return;
		}

		String authString = HttpHelper.getBasicAuthenticationFromModel(modelClass);

		if (authString == null)
			return;
		if (RestfulApplication.DEBUG)
			Log.d("restful", "addBasicAuthentication: This request has been set to include authentication..");
		request.setHeader("Authorization", "Basic " + authString);
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

}
