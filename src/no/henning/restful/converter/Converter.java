package no.henning.restful.converter;

import no.henning.restful.model.Model;

public interface Converter
{
	Model to(String response);
	String from(Model model);
}
