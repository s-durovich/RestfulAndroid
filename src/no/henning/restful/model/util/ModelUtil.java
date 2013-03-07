package no.henning.restful.model.util;

import java.lang.reflect.Field;
import no.henning.restful.model.annotation.Ignore;

public class ModelUtil
{
	public static <T> boolean replaceValues(T modelFrom,
			T modelTo)
	{
		if (modelFrom.getClass() != modelTo.getClass())
			return false;
		if (modelFrom.equals(modelTo))
			return false;

		try
		{
			Field[] toDeclaredFields = modelTo.getClass().getDeclaredFields();

			for (Field field : toDeclaredFields)
			{
				if (field.isAnnotationPresent(Ignore.class))
					continue;

				field.setAccessible(true);

				Field fromField = modelFrom.getClass().getDeclaredField(
						field.getName());
				fromField.setAccessible(true);

				field.set(modelTo, fromField.get(modelFrom));

			}

			return true;

		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
