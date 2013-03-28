package no.henning.restful.http;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import no.henning.restful.http.callback.HttpRestClientResponseCallback;
import no.henning.restful.http.status.HttpRestResponse;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class HttpRestClient extends AsyncTask<HttpUriRequest, Void, HttpRestResponse> {

	private final int CONNECTION_TIMEOUT = 10000;
	private final int SOCKET_TIMEOUT = 10000;

	private final static String SCHEME_NAME = "https";
	private final static int SCHEME_PORT = 443;

	private HttpClient client;

	private HttpRestClientResponseCallback callback;

	public HttpRestClient(HttpRestClientResponseCallback callback) {
		this.callback = callback;
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
		client = new DefaultHttpClient(params);
		client = getSSLClient(client);
	}

	private HttpClient getSSLClient(HttpClient client) {
		try {
			X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext context = SSLContext.getInstance(RestSSLSocketFactory.PROTOCOL_NAME);
			context.init(null, new TrustManager[] { trustManager }, null);
			SSLSocketFactory socketFactory = new RestSSLSocketFactory(context);
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager connectManager = client.getConnectionManager();
			SchemeRegistry scheme = connectManager.getSchemeRegistry();
			scheme.register(new Scheme(SCHEME_NAME, socketFactory, SCHEME_PORT));
			return new DefaultHttpClient(connectManager, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	protected HttpRestResponse doInBackground(HttpUriRequest... requests) {
		try {
			HttpResponse response = client.execute(requests[0]);

			String responseAsString = response.getEntity() == null ? "{}" : EntityUtils.toString(response.getEntity());

			StatusLine responseStatusLine = response.getStatusLine();

			return new HttpRestResponse(responseStatusLine.getStatusCode(), responseStatusLine.getReasonPhrase(),
					responseAsString);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(HttpRestResponse response) {
		if (callback != null)
			callback.onDone(response);
	}

	public static void request(HttpUriRequest request, HttpRestClientResponseCallback callback) {
		new HttpRestClient(callback).execute(request);
	}
}
