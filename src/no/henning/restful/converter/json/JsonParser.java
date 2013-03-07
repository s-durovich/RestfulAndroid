package no.henning.restful.converter.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.henning.restful.callback.Callback;
import no.henning.restful.converter.json.utils.JsonParserUtil;
import no.henning.restful.converter.json.utils.TypeReference;
import no.henning.restful.utils.CallbackHelper;
import no.henning.restful.utils.GenericHelper;
import no.henning.restful.utils.ProxyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser
{
	public static <T> Collection<T> parseCollection(JSONArray jsonArray,
			Class<T> type) throws InstantiationException, IllegalAccessException, JSONException
	{
		Collection<T> collection = new ArrayList<T>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			collection.add(parse(jsonArray.optJSONObject(i), type));
		}

		return collection;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] parseArray(JSONArray jsonArray, Class<T> type) throws InstantiationException, IllegalAccessException, JSONException
	{
		T[] array = (T[]) Array.newInstance(type, jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++)
		{
			array[i] = parse(jsonArray.optJSONObject(i), type);
		}
		return array;
	}

	public static <T> T parse(JSONObject jsonObject, Class<T> type)
			throws InstantiationException, IllegalAccessException,
			JSONException
	{
		if (jsonObject == null) return null;

		Log.d("restful", "Json parsing: " + jsonObject.toString());

		Log.d("restful", "Json parsing: Type is " + type);
		// Create a new instance of this type
		T returnObj = (T) type.newInstance();

		Log.d("restful", "Json parsing: Type is " + returnObj);
		// Lets populate some fields!
		List<Field> validFields = JsonParserUtil
				.getJsonFieldsToPopulate(returnObj.getClass());

		for (Field field : validFields)
		{
			field.setAccessible(true);

			String jsonFieldName = JsonParserUtil
					.getJsonFieldNameFromClassField(field);

			if (jsonObject.isNull(jsonFieldName)) continue;

			Class<?> fieldClass = field.getType();

			Object jsonValue = jsonObject.get(jsonFieldName);

			Log.d("restful", "JsonParser: Field " + field.getName()
					+ " - Class: " + fieldClass.getName());
			Log.d("restful",
					"JsonParser: Json Value Class: " + jsonValue.getClass());

			if (jsonValue instanceof JSONObject)
			{
				JsonParserUtil.castJsonObjectValue(field, returnObj, jsonValue);
			}
			else if (JsonParserUtil.isJsonArray(jsonValue))
			{
				JsonParserUtil.castJsonArrayValue(field, returnObj, jsonValue);
			}
			else
			{
				JsonParserUtil.castJsonValue(field, returnObj, jsonValue);
			}
		}

		return returnObj;
	}

	public static <T> T parse(String json, Class<T> type)
			throws InstantiationException, IllegalAccessException, JSONException
	{
		JSONObject jsonObject = new JSONObject(json);

		return parse(jsonObject, type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T parseGenericType(JSONObject jsonObject, Type type) throws InstantiationException, IllegalAccessException, JSONException
	{
		return (T) parse(jsonObject, (Class<?>) type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T parseGenericType(JSONArray jsonArray, Type type) throws InstantiationException, IllegalAccessException, JSONException
	{
		if (GenericHelper.isCollection(type))
		{
			Type collectionType = GenericHelper
					.getUnderlyingGenericType((ParameterizedType) type);

			return (T) parseCollection(jsonArray, (Class<?>) collectionType);
		}
		else if (GenericHelper.isArray(type))
		{
			Type arrayType = GenericHelper
					.getUnderlyingGenericArrayType((GenericArrayType) type);

			return (T) parseArray(jsonArray, (Class<?>) arrayType);
		}

		return null;
	}

	public static <T> T parse(String json, Type type) throws JSONException, InstantiationException, IllegalAccessException
	{
		// TODO: IS THIS OK!?
		if (json.trim().startsWith("["))
		{
			JSONArray jsonArray = new JSONArray(json);

			return parseGenericType(jsonArray, type);

		}
		else
		{
			JSONObject jsonObject = new JSONObject(json);

			return parseGenericType(jsonObject, type);
		}
	}

	public static <T> T parse(String json, TypeReference<?> type)
			throws JSONException, InstantiationException, IllegalAccessException
	{
		Type genericType = GenericHelper.getTypeReferenceType(type);

		return parse(json, genericType);
	}

	public static <T> T parse(String json, Callback<?> callback)
			throws JSONException, InstantiationException, IllegalAccessException
	{
		Type genericType = CallbackHelper.getCallbackType(callback);

		return parse(json, genericType);
	}
}
