package no.henning.restful.model.interfaces;

import no.henning.restful.callback.*;
import no.henning.restful.model.Model;

public interface DefaultRestActions
{
	/**
	 * GET methods
	 */
	public void get();
	public void get(Object id);
	public <T> void get(Object id, final Callback<T> callback);
	public <T> void get(final Callback<T> callback);
	
	/**
	 * POST methods
	 */
	public void save();
	public void save(final Callback<Model> callback);
	
	/**
	 * PUT methods
	 */
	public void update();
	public void update(final Callback<Model> callback);
	
	/**
	 * DELETE methods
	 */
	public void delete();
	public void delete(Object id);
	public <T> void delete(Object id, final Callback<T> callback);
	public <T> void delete(final Callback<T> callback);
}
