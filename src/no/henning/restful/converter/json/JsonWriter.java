package no.henning.restful.converter.json;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import no.henning.restful.converter.json.utils.JsonParserUtil;
import no.henning.restful.utils.ConvertingHelper;
import no.henning.restful.utils.GenericHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JsonWriter
{
	@SuppressWarnings("unchecked")
	public static JSONObject from(Object object)
	{
		// TODO: Maybe throw exceptions right?
		if (object == null) return null;
		
		JSONObject json = null;
		try
		{
			json = new JSONObject();
			
			List<Field> validFields = JsonParserUtil
					.getJsonFieldsToPopulate(object.getClass());
			
			for (Field field : validFields)
			{
				field.setAccessible(true);
				
				Class<?> fieldClass = field.getType();
				String jsonPropertyName = JsonParserUtil.getJsonFieldNameFromClassField(field);
				
				Object value = null;
				
				if (GenericHelper.isArray(fieldClass))
				{
					value = fromArray((Object[])field.get(object));
				}
				else if (GenericHelper.isCollection(fieldClass))
				{
					value = fromCollection((Collection<Object>) field.get(object));
				}
				else
				{
					value = field.get(object);
				}
			
				json.put(jsonPropertyName, value);
			}
		}
		catch (Exception ex)
		{
			Log.e("restful", "JsonWriter Exception: " + ex.getMessage());
		}
		
		return json;
	}
	
	public static JSONArray fromArray(Object[] objects)
	{
		if (objects == null) return null;
		
		JSONArray jsonArray = null;
		
		try
		{
			jsonArray = new JSONArray();
			
			for (Object object : objects)
			{
				jsonArray.put(JsonWriter.from(object));
			}
		}
		catch (Exception ex)
		{
			Log.e("restful", "JsonWriter fromArray Exception: " + ex.getMessage());
		}
		
		return jsonArray;
	}
	
	public static JSONArray fromCollection(Collection<Object> objects)
	{
		if (objects == null) return null;
		
		return fromArray(objects.toArray());
	}
}
