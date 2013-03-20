package no.henning.restful.converter.json.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import no.henning.restful.RestfulApplication;
import no.henning.restful.converter.json.JsonParser;
import no.henning.restful.model.annotation.Ignore;
import no.henning.restful.model.annotation.Named;
import no.henning.restful.utils.GenericHelper;

public class JsonParserUtil
{
	public static String getJsonFieldNameFromClassField(Field field)
	{
		if (field == null) return null;
		
		if (!field.isAnnotationPresent(Named.class))
			return field.getName();
		
		Named annotation = field.getAnnotation(Named.class);
		
		return annotation.value();
	}
	
	public static List<Field> getJsonFieldsToPopulate(Class<?> type)
	{
		List<Field> listOfFields = new ArrayList<Field>();
		
		Field[] fields = type.getDeclaredFields();
		
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(Ignore.class))
				continue;
			
			listOfFields.add(field);
		}
		
		return listOfFields;
	}
	
	public static boolean castJsonValue(Field field, Object object, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> fieldClass = field.getType();
		
		if (fieldClass.isInstance(value))
		{if (RestfulApplication.DEBUG)
			Log.d("restful", "castJsonValue: Field can be cast directly!");
			field.set(object, fieldClass.cast(value));
		}
		else if (isNumber(fieldClass))
		{if (RestfulApplication.DEBUG)
			Log.d("restful", "castJsonValue: Field is a type of Number..");
			Object newValue = JsonCasting.getAsNumber(fieldClass, value);
			if (RestfulApplication.DEBUG)
			Log.d("restful", "castJsonValue: Number value: " + newValue);
			field.set(object, newValue);
		}
		
		return false;
	}
	
	public static boolean isNumber(Class<?> clazz)
	{
		return Number.class.isAssignableFrom(clazz);
	}
	
	public static boolean isJsonArray(Object value)
	{
		return value instanceof JSONArray;
	}
	
	public static boolean castJsonArrayValue(Field field, Object object, Object value) throws IllegalArgumentException, IllegalAccessException, InstantiationException, JSONException
	{
		Class<?> fieldClass = field.getType();
		
		Object jsonValue = null;
		
		if (GenericHelper.isArray(fieldClass))
		{
			jsonValue = JsonParser.parseArray((JSONArray)value, GenericHelper.getUnderlyingArrayType(fieldClass));
		}
		else if (GenericHelper.isCollection(fieldClass))
		{
			ParameterizedType type = (ParameterizedType)field.getGenericType();
			jsonValue = JsonParser.parseCollection((JSONArray)value, GenericHelper.getUnderlyingGenericClass(type));
		}
		
		field.set(object, jsonValue);
		
		return jsonValue == null;
	}
	
	public static boolean castJsonObjectValue(Field field, Object object, Object value) throws IllegalArgumentException, IllegalAccessException, InstantiationException, JSONException
	{
		if (!(value instanceof JSONObject)) return false;
		
		Class<?> fieldClass = field.getType();
		
		Object jsonValue = null;
		
		jsonValue = JsonParser.parse((JSONObject)value, fieldClass);
		
		field.set(object, jsonValue);
		
		return jsonValue == null;
	}
}
