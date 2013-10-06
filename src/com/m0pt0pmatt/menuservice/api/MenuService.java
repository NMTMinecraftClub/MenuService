package com.m0pt0pmatt.menuservice.api;

import com.m0pt0pmatt.menuservice.api.rendering.Renderer;

/**
 * The MenuService interface is the absolute API for the MenuService Plugin.
 * Using this interface, developers can create detailed menus using MenuService's abstract Menu system.
 * 
 * To use MenuService, a developer needs to add the MenuServiceAPI.jar to the build path
 * To use MenuService on a server, MenuService.jar must be in the plugins directory
 * 
 * To reference the MenuService interface, grab it from the Bukkit Services Manager
 * Example: MenuService menuService = Bukkit.getServicesManager().getRegistration(MenuService.class).getProvider();
 * Also, it is very important to make sure your plugin has a soft depend for MenuService.
 * 
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 *
 */
public interface MenuService{
	
	//--------------------------Methods for Renderers--------------------------
	
	/**
	 * Returns a Renderer by name from the MenuService.
	 * @param rendererName The name of the Renderer
	 * @return The specified Renderer, or null if one does not exist
	 */
	public Renderer getRenderer(String rendererName);
	
	/**
	 * Checks if the MenuService has a specified Renderer
	 * @param the Renderer
	 * @return true if the Renderer exists, false otherwise
	 */
	public boolean hasRenderer(Renderer renderer);
	
	/**
	 * Checks if the MenuService has a specified Renderer
	 * @param rendererName the name of the Renderer
	 * @return true if the Renderer exists, false otherwise
	 */
	public boolean hasRenderer(String rendererName);
	
	/**
	 * Adds a Renderer to the MenuService.
	 * After added, a Renderer can be used to Render MenuInstances.
	 * Renderers can be assigned to Menus (and thus every MenuInstance of a menu) or individual MenuInstances.
	 * @param renderer The Renderer to be added to the MenuService
	 */
	public boolean addRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param renderer the Renderer to be removed
	 */
	public boolean removeRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param rendererName the name of the Renderer to be removed
	 * @return the Renderer if it exists, null otherwise
	 */
	public Renderer removeRenderer(String rendererName);
	
}
