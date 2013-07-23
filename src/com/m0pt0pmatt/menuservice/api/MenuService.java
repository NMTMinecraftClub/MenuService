package com.m0pt0pmatt.menuservice.api;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

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
public interface MenuService {

	/**
	 * Creates a new Menu from the given Plugin and fileName.
	 * 
	 * Since Menus are stored based on the plugin that owns them, 
	 * the plugin needs to be referenced so the correct file location can be found.
	 * 
	 * The fileName is the name of the config file for the Menu.
	 * 
	 * After the Menu is loaded, it is stored in the MenuService
	 * 
	 * @param plugin The Plugin which the Menu belongs to
	 * @param fileName The name of the Menu's config file
	 */
	public Menu loadMenu(Plugin plugin, String fileName);
	
	public boolean saveMenu(Plugin plugin, Menu menu, String fileName);
	
	public void addMenu(Plugin plugin, Menu menu);
	
	public Menu getMenu(Plugin plugin, String menuName);
	
	public boolean hasMenu(Plugin plugin, Menu menu);
	
	public void removeMenu(Plugin plugin, Menu menu);
	
	public Menu removeMenu(Plugin plugin, String menuName);
	
	/**
	 * Creates a MenuInstance from the given Menu.
	 * 
	 * While a Menu is an abstract layout, a MenuInstance is an implementation of the Menu.
	 * Once created, a MenuInstance can be shown to players.
	 * 
	 * The MenuInstance is returned after it is created.
	 * It is also registered under it's Menu in the MenuService
	 * @param menu The Menu which the MenuInstance will be created from
	 * @param provider The MenuProvider which will provide for the MenuInstance
	 */
	public MenuInstance createMenuInstance(Menu menu, String instanceName);
	
	/**
	 * Creates a MenuInstance with given parameters from the given Menu.
	 * MenuInstances are given names so ActionListeners know when they open or close.
	 * 
	 * While a Menu is an abstract layout, a MenuInstance is an implementation of the Menu.
	 * Once created, a MenuInstance can be shown to players.
	 * 
	 * The MenuInstance is returned after it is created.
	 * It is also registered under it's Menu in the MenuService
	 * @param menu The Menu which the MenuInstance will be created from
	 * @param provider The MenuProvider which will provide for the MenuInstance
	 * @param parameters All parameters for the MenuInstance
	 */
	public MenuInstance createMenuInstance(Menu menu, String instanceName, Map<String, Object> parameters);
	
	/**
	 * Opens a MenuInstance to a given player.
	 * 
	 * The MenuService will also register that the player is currently viewing the given MenuInstance.
	 * In order for a MenuInstance to be shown, the Menu of the MenuInstance must be provided for by a MenuProvider.
	 * MenuInstances also need to have at least one Renderer. This can be assigned to the MenuInstance itself, or inherited from the Renderers of its Menu.
	 * 
	 * @param instance The MenuInstane to be shown.
	 * @param playerName The name of the player to show the MenuInstance to.
	 */
	public boolean openMenuInstance(MenuInstance instance, String playerName);
	
	public void removeMenuInstance(MenuInstance instance);
	
	public MenuInstance removeMenuInstance(Menu menu, String instanceName);
	
	public MenuInstance getMenuInstance(Menu menu, String instanceName);
	
	public boolean hasMenuInstance(Menu menu, String instanceName);
	
	/**
	 * Closes the MenuInstance for a player
	 * 
	 * Tells the MenuProvider for the MenuInstance to close the Menu.
	 * Removes the player from the MenuInstance.
	 * 
	 * @param playerName the Name of the player
	 */
	public void closeMenuInstance(String playerName);
	
	/**
	 * Adds a Renderer to the MenuService.
	 * After added, a Renderer can be used to Render MenuInstances.
	 * Renderers can be assigned to Menus (and thus every MenuInstance of a menu) or individual MenuInstances.
	 * @param renderer The Renderer to be added to the MenuService
	 */
	public void addRenderer(Renderer renderer);
	
	/**
	 * Returns a Renderer by name from the MenuService.
	 * @param rendererName The name of the Renderer
	 * @return The specified Renderer, or null if one does not exist
	 */
	public Renderer getRenderer(String rendererName);
	
	public Renderer removeRenderer(String rendererName);
	
	public void removeRenderer(Renderer renderer);
	
	public boolean hasRenderer(Renderer renderer);
	
	public boolean hasRenderer(String rendererName);
	
	public boolean bindMenu(ItemStack item, Menu menu);
	
	public boolean bindMenu(Material material, Menu menu);
	
	public boolean unbindMenu(Menu menu);
	
	public boolean unbindMenu(ItemStack item);
	
	public boolean unbindMenu(Material material);
	
	public void saveAll();
	
}
