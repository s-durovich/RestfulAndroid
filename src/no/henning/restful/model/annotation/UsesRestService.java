package no.henning.restful.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import no.henning.restful.service.RestService;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesRestService
{
	Class<? extends RestService> value();
}
