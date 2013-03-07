package no.henning.restful.model;

import java.lang.reflect.Proxy;

import no.henning.restful.handler.RestMethodHandler;

public class ModelWithActions<T> extends Model
{
	public T actions;

	@SuppressWarnings("unchecked")
	public static <T> T enable(Class<?> type)
	{
		return (T) Proxy.newProxyInstance(type.getClassLoader(),
				new Class<?>[] { type }, new RestMethodHandler(type));
	}
}
