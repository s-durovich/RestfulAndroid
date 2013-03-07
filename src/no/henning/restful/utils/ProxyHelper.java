package no.henning.restful.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import no.henning.restful.callback.Callback;
import no.henning.restful.model.Model;
import no.henning.restful.model.annotation.Entity;
import no.henning.restful.model.annotation.Named;
import no.henning.restful.service.annotation.DELETE;
import no.henning.restful.service.annotation.GET;
import no.henning.restful.service.annotation.POST;
import no.henning.restful.service.annotation.PUT;

public class ProxyHelper
{
	private static final Pattern PATH_PARAMETERS = Pattern
			.compile("\\{([a-z_-]*)\\}");

	public static String getAbsolutePathFromProxyMethod(Method method,
			Object[] arguments)
	{
		Class<? extends Model> model = GenericHelper
				.getModelFromProxyMethod(method);
		String modelAbsolutePath = HttpHelper.getAbsolutePathFromModel(model);
		String proxyMethodPath = getPathFromProxyMethod(method, arguments);

		// Automatically appends "/" to the path if it's missing
		// TODO: Is this really what we want? I dunno at this point (18/01/2013
		// 23:37)
		// modelAbsolutePath = HttpHelper.fixServicePath(modelAbsolutePath);

		return String.format("%s%s", modelAbsolutePath, proxyMethodPath);
	}

	public static String getPathFromProxyMethod(Method method,
			Object[] arguments)
	{
		String path = getPathFromHttpVerbAnnotation(method);

		Map<String, Object> namedValues = getProxyPathNamedParametersWithValues(
				method, arguments);

		return getProxyPathWithNamedValues(path, namedValues);
	}

	public static String getPathFromHttpVerbAnnotation(Method method)
	{
		Annotation[] annotations = method.getAnnotations();

		for (Annotation annotation : annotations)
		{
			if (annotation instanceof GET)
				return ((GET) annotation).value();
			else if (annotation instanceof POST)
				return ((POST) annotation).value();
			else if (annotation instanceof PUT)
				return ((PUT) annotation).value();
			else if (annotation instanceof DELETE)
				return ((DELETE) annotation).value();

		}

		return null;
	}

	public static Set<String> findProxyPathNamedParameters(String path)
	{
		Set<String> namedParameters = new LinkedHashSet<String>();

		Matcher matcher = PATH_PARAMETERS.matcher(path);

		while (matcher.find())
		{
			namedParameters.add(matcher.group(1));
		}

		return namedParameters.size() <= 0 ? null : namedParameters;
	}

	public static Map<String, Object> getProxyPathNamedParametersWithValues(
			Method method, Object[] arguments)
	{
		Map<String, Object> parametersWithValues = new HashMap<String, Object>();

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		int parameterCount = parameterAnnotations.length;

		for (int i = 0; i < parameterCount; i++)
		{
			for (Annotation annotation : parameterAnnotations[i])
			{
				if (!(annotation instanceof Named)) continue;

				String pathParameterKey = ((Named) annotation).value();

				parametersWithValues.put(pathParameterKey, arguments[i]);
			}
		}

		return parametersWithValues.size() > 0 ? parametersWithValues : null;
	}

	public static String getProxyPathWithNamedValues(String path,
			Map<String, Object> parameterNamedValues)
	{
		String temporaryPath = path;

		for (Map.Entry<String, Object> entry : parameterNamedValues.entrySet())
		{
			temporaryPath = temporaryPath.replaceAll("\\{" + entry.getKey()
					+ "\\}", String.valueOf(entry.getValue()));
		}

		return temporaryPath;
	}

	public static String getProxyQueryPath(Method method, Object[] arguments)
	{
		String path = getPathFromHttpVerbAnnotation(method);

		String queryPath = "?";

		Map<String, Object> namedValues = getProxyPathNamedParametersWithValues(
				method, arguments);

		for (Map.Entry<String, Object> entry : namedValues.entrySet())
		{
			if (path.contains("{" + entry.getKey() + "}")) continue;

			queryPath += entry.getKey() + "=" + entry.getValue() + "&";
		}

		return queryPath.substring(0, queryPath.length() - 1);
	}

	/**
	 * getCallbackType
	 * 
	 * If there's a Callback<?> in the method, it should always be the last
	 * argument right?..
	 * 
	 * @param arguments
	 * @return
	 */

	public static Object getEntityObjectFromProxyMethod(Method method,
			Object[] arguments)
	{
		Annotation[][] annotations = method.getParameterAnnotations();

		for (int i = 0; i < annotations.length; i++)
		{
			for (Annotation annotation : annotations[i])
			{
				if (!(annotation instanceof Entity)) continue;

				return arguments[i];
			}
		}

		return null;
	}
}
