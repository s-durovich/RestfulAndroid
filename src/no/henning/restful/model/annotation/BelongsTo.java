package no.henning.restful.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import no.henning.restful.model.Model;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BelongsTo
{
	Class<? extends Model> value();
}
