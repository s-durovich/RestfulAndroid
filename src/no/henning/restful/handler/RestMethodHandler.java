package no.henning.restful.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import no.henning.restful.RestfulApplication;
import no.henning.restful.callback.Callback;
import no.henning.restful.callback.CallbackWrapper;
import no.henning.restful.converter.json.JsonParser;
import no.henning.restful.converter.json.JsonWriter;
import no.henning.restful.http.HttpRestClient;
import no.henning.restful.http.builder.RestHttpRequestDetail;
import no.henning.restful.http.callback.HttpRestClientResponseCallback;
import no.henning.restful.http.status.HttpRestResponse;
import no.henning.restful.model.Model;
import no.henning.restful.utils.CallbackHelper;
import no.henning.restful.utils.GenericHelper;
import no.henning.restful.utils.HttpHelper;
import no.henning.restful.utils.ProxyHelper;

import android.util.Log;

public class RestMethodHandler implements InvocationHandler {
	private Class<?> declaringClass;

	public RestMethodHandler(Class<?> type) {
		declaringClass = type;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		if (RestfulApplication.DEBUG)
			Log.d("restful", "RestMethodHandler: Getting Model associated with " + method.getName());
		Class<? extends Model> model = GenericHelper.getModelFromProxyMethod(method);
		if (RestfulApplication.DEBUG)
			Log.d("restful", "RestMethodHandler: Getting absolute path");
		String path = ProxyHelper.getAbsolutePathFromProxyMethod(method, arguments);
		if (RestfulApplication.DEBUG)
			Log.d("restful", "RestMethodHandler: Getting what HTTP verb to use");
		String httpVerb = HttpHelper.getHttpRequestVerbFromProxyMethod(method);
		if (RestfulApplication.DEBUG)
			Log.d("restful", "RestMethodHandler: " + httpVerb + " HTTP Verb");

		String queryPath = ProxyHelper.getProxyQueryPath(method, arguments);
		if (RestfulApplication.DEBUG)
			Log.d("restful", "RestMethodHandler: Full path: " + path + queryPath);

		String absolutePath = path + queryPath;

		Object entityObject = ProxyHelper.getEntityObjectFromProxyMethod(method, arguments);
		String entityAsJsonString = null;

		if (entityObject != null) {
			entityAsJsonString = JsonWriter.from(entityObject).toString();
			if (RestfulApplication.DEBUG)
				Log.d("restful", "RestMethodHandler: Post body (JSON): " + entityAsJsonString);
		}

		final Callback<?> callback = CallbackHelper.getCallbackArgument(arguments);
		final Type callbackType = CallbackHelper.getCallbackType(callback);

		HttpUriRequest httpRequest = new RestHttpRequestDetail(model, absolutePath, httpVerb, entityAsJsonString)
				.buildRequest();
		HttpRestClient.request(httpRequest, new HttpRestClientResponseCallback() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onDone(HttpRestResponse response) {
				if (response != null) {
					// TODO Auto-generated method stub
					if (RestfulApplication.DEBUG)
					Log.d("restful", "Response: " + response.getResponse());

					if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
						try {
							Object t = JsonParser.parse(response.getResponse(), callback);
							new CallbackWrapper(callback).success(t);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							new CallbackWrapper(callback).error(response);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						new CallbackWrapper(callback).error(Model.parseResponseError(response));
					}
				}
			}
		});

		return true;
	}
}
