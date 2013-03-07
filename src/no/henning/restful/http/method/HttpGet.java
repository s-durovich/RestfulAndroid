package no.henning.restful.http.method;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpGet extends HttpEntityEnclosingRequestBase
{
	private final static String METHOD_NAME = "GET";
	
	public HttpGet()
	{
		super();
	}
	
	public HttpGet(String url)
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