package com.m0pt0pmatt.menuservice.api;

public interface Renderer {
	
	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name
	 * @return the name of the Renderer
	 */
	public String getName();

	public MenuImplementation createImplementation(Menu menu);
		
}
