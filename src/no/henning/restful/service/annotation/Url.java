package no.henning.restful.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Url
 * 
 * Set a service's API url and also apply a suffix if the
 * service need one.
 * 
 * @author Henning Mosand Stephansen
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Url
{
	String value();
	
	/**
	 * Used to apply eg. ".json" to the end of the Url of the 
	 * service if headers aren't enough
	 * @return
	 */
	String suffix() default "";
}
