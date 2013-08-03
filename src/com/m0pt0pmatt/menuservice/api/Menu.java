package com.m0pt0pmatt.menuservice.api;

import java.util.List;
import java.util.Map;

/**
 * A Menu represents an abstract menu which can be rendered multiple ways to multiple players.
 * 
 * Menus are composed of Components. A Menu itself is also a Component.
 * Menus can be shown to multiple players. 
 * When a Menu is shown, the player actually sees a MenuInstance. This is one copy of the Menu
 * specifically for the player.
 * 
 * While a Menu will stay the same once it has been created, MenuInstances can change.
 * 
 * @author mbroomfield
 *
 */
public interface Menu extends Component{
	
	/**
	 * Returns a Map of all Components of the Menu
	 * @return a Map of all Components of the Menu
	 */
	public Map<String, Component> getComponents();
	
	/**
	 * Returns a Map of all attributes of the Menu
	 * @return a Map of all attributes of the Menu
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Returns the list of Renderers that provide for all instances of the Menu
	 * @return the List of Renderers that provide for all instances of the Menu
	 */
	public List<Renderer> getRenderers();

	/**
	 * Sets the list of Renderers that provide for all instances of the Menu
	 * @param renderers the list of Renderers that provide for all instances of the Menu
	 */
	public void setRenderers(List<Renderer> renderers);
	
	/**
	 * Adds a Renderer to the menu.
	 * @param renderer
	 */
	public void addRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the menu.
	 * @param renderer
	 */
	public void removeRenderer(Renderer renderer);
	
	/**
	 * Checks if the menu has a given Renderer
	 * @param renderer
	 * @return
	 */
	public boolean hasRenderer(Renderer renderer);
	
	/**
	 * Returns a renderer from the menu by name
	 * @param rendererName
	 * @return
	 */
	public Renderer getRenderer(String rendererName);
	
	/**
	 * Returns the plugin that this menu belongs to
	 * @return
	 */
	public String getPlugin();
	
	/**
	 * Returns the name of the menu
	 * @return
	 */
	public String getName();
	
	/**
	 * Checks if the menu is equal to the given object
	 * @param arg
	 * @return
	 */
	public boolean equals(Object arg);
	
}
