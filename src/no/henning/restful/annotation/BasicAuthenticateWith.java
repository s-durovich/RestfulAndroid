package no.henning.restful.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import no.henning.restful.credential.BasicAuthentication;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BasicAuthenticateWith
{
	Class<? extends BasicAuthentication> value();
}