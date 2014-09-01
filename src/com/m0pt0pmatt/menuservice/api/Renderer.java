package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;

/**
 * A Renderer will render (or present) a menu to a player.
 * 
 * A Renderer can either render a Menu or a MenuInstance.
 * When rendering a Menu, the Renderer must create a MenuInstance, add it to the Menu, render the MenuInstance, and return the MenuInstance.
 * 
 * To distinguish Renderers from each other, each Renderer must supply a name.
 * @author mbroomfield
 *
 */
public interface Renderer {
	
	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name
	 * @return the name of the Renderer
	 */
	public String getName();

	public void openMenu(Menu menu, UUID playerName);
	
	public void closeMenu(Menu menu, UUID playerName);
	
	public void draw(Menu menu, Component component);

	public void undraw(Menu menu, Component component);
	
}
