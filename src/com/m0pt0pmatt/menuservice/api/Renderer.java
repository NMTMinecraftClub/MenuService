package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;

/**
 * A Renderer will render (or present) a menu to a player.
 * A Renderer is responsible for drawing the menu and for catching player interactions.
 * A Renderer draws a Menu object. It creates a MenuImplementation of that Menu.
 * To distinguish Renderers from each other, each Renderer must supply a name.
 * 
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 */
public interface Renderer {
	
	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name
	 * @return the name of the Renderer
	 */
	public String getName();

	/**
	 * Opens a menu for a player
	 * @param menu
	 * @param playerName
	 */
	public void render(Menu menu, UUID playerName);
	
	public void derender(UUID playerName);
		
}
