package com.m0pt0pmatt.menuservice.api;

import java.util.List;
import java.util.Map;

import com.m0pt0pmatt.menuservice.api.rendering.Renderer;

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
	
	
	//--------------------------Methods for all components--------------------------
	/**
	 * Returns a Map of all Components of the Menu
	 * @return a Map of all Components of the Menu
	 */
	public Map<String, Component> getComponents();
	
	/**
	 * Sets all of the components for the Menu.
	 * This removes all previous components of the menu and add all the new components
	 * @param components the new components for the Menu
	 */
	public void setComponents(Map<String, Component> components);
	
	//--------------------------Methods for one component--------------------------
	
	/**
	 * Returns a Component of the Menu
	 * @param componentName the name of the Component to be returned
	 * @return the specified Component
	 */
	public Component getComponent(String componentName);
	
	/**
	 * Returns whether or not a given Component is part of the Menu
	 * @param component the given Component
	 * @return whether or not a given Component is part of the Menu
	 */
	public boolean hasComponent(Component component);
	
	/**
	 * Returns whether or not a given Component is part of the Menu
	 * @param componentName the name of the given Component
	 * @return whether or not a given Component is part of the Menu
	 */
	public boolean hasComponent(String componentName);
	
	/**
	 * Adds a component to the Menu
	 * @param component the Component to be added
	 */
	public void addComponent(Component component);
	
	//--------------------------Methods for all renderers--------------------------
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
	
	//--------------------------Methods for one renderer--------------------------
	/**
	 * Returns a renderer from the menu by name
	 * @param rendererName
	 * @return
	 */
	public Renderer getRenderer(String rendererName);
	
	/**
	 * Checks if the menu has a given Renderer
	 * @param renderer the specified Renderer
	 * @return
	 */
	public boolean hasRenderer(Renderer renderer);
	
	/**
	 * Checks if the menu has a given Renderer
	 * @param rendererName the name of the specified Renderer
	 * @return
	 */
	public boolean hasRenderer(String rendererName);
	
	/**
	 * Adds a Renderer to the menu.
	 * @param renderer the Renderer to be added to the Menu
	 */
	public void addRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the menu.
	 * @param renderer the Renderer to be removed from the Menu
	 */
	public void removeRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the menu.
	 * @param rendererName the name of the Renderer to be removed from the Menu
	 */
	public void removeRenderer(String rendererName);
	
	//--------------------------Methods for special menu attributes--------------------------
	/**
	 * Returns the plugin that this menu belongs to
	 * @return the plugin that this menu belongs to
	 */
	public String getPlugin();
	
	/**
	 * Returns the name of the menu
	 * @return the name of the Menu
	 */
	public String getName();

	/**
	 * Returns the fileName of the Menu
	 * @return the fileName of the Menu
	 */
	public String getFileName();

	/**
	 * Sets the fileName for this Menu
	 * @param fileName the fileName for the Menu
	 */
	public void setFileName(String fileName);
	
	//--------------------------Methods that are related to whether or not the menu needs to be saved--------------------------
	/**
	 * Returns whether or not this menu or any of its components or attributes have been changed
	 * @return true or false
	 */
	public boolean hasChanged();
	
	/**
	 * Sets whether or not the menu has changed. This determines whether or not it needs to be saved to file.
	 * @param hasChanged
	 */
	public void setChanged(boolean hasChanged);
	
}
