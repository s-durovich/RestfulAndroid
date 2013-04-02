package no.henning.restful;

import android.app.Application;

public class RestfulApplication extends Application {

	private static RestfulApplication application = null;

	public static boolean DEBUG = false;

	public RestfulApplication() {
	}

	public static RestfulApplication getInstate() {
		if (application == null)
			application = new RestfulApplication();
		return application;
	}
}
