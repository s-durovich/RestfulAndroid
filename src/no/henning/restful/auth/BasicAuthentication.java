package no.henning.restful.auth;

import android.util.Base64;

public class BasicAuthentication {
	protected static String username;
	protected static String password;

	@SuppressWarnings("unused")
	private static String encodedString;

	private static void encodeUsernameAndPassword() {
		String authString = String.format("%s:%s", username, password);

		encodedString = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
	}

	public static void setUsernameAndPassword(String user, String pass) {
		username = user;
		password = pass;

		encodeUsernameAndPassword();
	}

	public static String getLogin() {
		return username;
	}

	public static String getPassword() {
		return password;
	}
}