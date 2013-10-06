package com.m0pt0pmatt.menuservice.api.rendering;

import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuPart;

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

	public void draw(Menu menu, MenuPart p, String playerName);

	public void undraw(Menu menu, MenuPart p, String playerName);
	
}
