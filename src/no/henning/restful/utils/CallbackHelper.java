package no.henning.restful.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import no.henning.restful.callback.Callback;

public class CallbackHelper
{
	public static Class<?> getCallbackClass(Callback<?> callback)
	{
		return (Class<?>) getCallbackType(callback);
	}

	public static Type getCallbackType(Callback<?> callback)
	{
		Type[] types = callback.getClass().getGenericInterfaces();

		Type callbackType = GenericHelper
				.getUnderlyingGenericType((ParameterizedType) types[0]);

		return callbackType;
	}

	public static Callback<?> getCallbackArgument(Object[] arguments)
	{
		return (Callback<?>) arguments[arguments.length - 1];
	}
}
