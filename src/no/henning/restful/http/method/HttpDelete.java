package no.henning.restful.http.method;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpDelete extends HttpEntityEnclosingRequestBase
{
	private final static String METHOD_NAME = "DELETE";

	public HttpDelete()
	{
		super();
	}

	public HttpDelete(String url)
	{
		super();

		try
		{
			setURI(new URI(url));
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getMethod()
	{
		// TODO Auto-generated method stub
		return METHOD_NAME;
	}
}
