package no.henning.restful;

import android.app.Application;
import android.content.Context;

public class RestfulApplication extends Application{

	private static RestfulApplication application = null;
	
	private Context mContext;
	
	private RestfulApplication() {
		//mContext = getApplicationContext();
	}
	
	public static RestfulApplication getInstate(){
		if(application == null)
			application = new RestfulApplication();
		return application;
	}
}
