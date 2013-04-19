package no.henning.restful;

import android.app.Application;

public class RestfulApplication extends Application {

	public static final boolean DEBUG = false;

	private static RestfulApplication instance;

	private String username;
	private String password;

	public static synchronized RestfulApplication getInstate() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
