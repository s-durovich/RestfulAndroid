package no.henning.restful.converter.json.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import no.henning.restful.RestfulApplication;

import android.util.Log;

public class JsonCasting {
	public static Object getAsNumber(Class<?> returnType, Object value) {
		Number number = (Number) value;
		if (RestfulApplication.DEBUG)
			Log.d("restful", "getAsNumber: Original value: " + value);

		Object newValue = null;

		if (returnType.isAssignableFrom(Integer.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Integer");
			newValue = number.intValue();
		} else if (returnType.isAssignableFrom(Double.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Double");
			newValue = number.doubleValue();
		} else if (returnType.isAssignableFrom(Float.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Float");
			newValue = number.floatValue();
		} else if (returnType.isAssignableFrom(Long.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Long");
			newValue = number.longValue();
		} else if (returnType.isAssignableFrom(Short.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Short");
			newValue = number.shortValue();
		} else if (returnType.isAssignableFrom(BigDecimal.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is BigDecimal");
			newValue = BigDecimal.valueOf(number.doubleValue());
		} else if (returnType.isAssignableFrom(BigInteger.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is BigInteger");
			newValue = BigInteger.valueOf(number.longValue());
		} else if (returnType.isAssignableFrom(Byte.class)) {
			if (RestfulApplication.DEBUG)
				Log.d("restful", "getAsNumber: is Byte");
			newValue = BigDecimal.valueOf(number.byteValue());
		}

		return newValue;
	}
}
