package no.henning.restful.credential;

import android.os.Build;
import android.util.Log;
import no.henning.restful.RestfulApplication;

public class RestUserAgent {

	private static String versionName;

	public static String getVersionName(){
		return versionName;
	}
	
	public static void setVersionName(String version){
		versionName = version;
	}
	
	public static String getUserAgentString() {
		StringBuilder userAgentBuilder = new StringBuilder();
		userAgentBuilder.append("Android/");
		if (versionName != null)
			userAgentBuilder.append(versionName);
		userAgentBuilder.append(" (Android ");
		userAgentBuilder.append(Build.VERSION.RELEASE);
		userAgentBuilder.append(", ");
		userAgentBuilder.append(getDeviceName());
		userAgentBuilder.append(")");
		if (RestfulApplication.DEBUG)
			Log.d("restful", userAgentBuilder.toString());
		return userAgentBuilder.toString();
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
}
