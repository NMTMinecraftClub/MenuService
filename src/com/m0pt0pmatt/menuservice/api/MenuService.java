package com.m0pt0pmatt.menuservice.api;

import java.util.List;
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

	//--------------------------Methods for all Menus--------------------------
	
	/**
	 * Returns a List of the Menus that are currently loaded in the MenuService
	 * @return all currently loaded Menus
	 */
	public List<Menu> getMenus();
	
	public void loadMenus();
	
	/**
	 * Saves all Menus to file
	 */
	public void saveMenus();
	
	/**
	 * Closes all Menus
	 */
	public void closeMenus();
	
	/**
	 * Reloads all Menus
	 */
	public void reloadMenus();
	
	/**
	 * Unloads all Menus
	 */
	public List<Menu> unloadMenus();
	
	//--------------------------Methods for one Menu--------------------------
	
	/**
	 * Returns a Menu from the MenuService
	 * @param menuName the name of the Menu
	 * @return the Menu if it exists, null otherwise
	 */
	public Menu getMenu(String menuName);
	
	/**
	 * Checks if a Menu is loaded into the MenuService
	 * @param menu the Menu
	 * @return true if the MenuService has the Menu, false otherwise
	 */
	public boolean hasMenu(Menu menu);
	
	/**
	 * Checks if a Menu is loaded into the MenuService
	 * @param menuName the name of the Menu
	 * @return true if the MenuService has the Menu, false otherwise
	 */
	public boolean hasMenu(String menuName);
	
	/**
	 * Adds a Menu to the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 */
	public boolean addMenu(Menu menu);
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param menu the Menu to remove
	 */
	public void removeMenu(Menu menu);
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param menuName the name of the Menu
	 * @return The Menu if it exists, null otherwise
	 */
	public Menu removeMenu(String menuName);
	
	/**
	 * Creates a new Menu from the given Plugin and fileName.
	 * Since Menus are stored based on the plugin that owns them, 
	 * the plugin needs to be referenced so the correct file location can be found.
	 * The fileName is the name of the config file for the Menu.
	 * After the Menu is loaded, it is stored in the MenuService
	 * 
	 * @param plugin The Plugin which the Menu belongs to
	 * @param fileName The name of the Menu's config file
	 * @return The Menu if loaded, null otherwise
	 */
	public Menu loadMenu(Plugin plugin, String fileName);
	
	/**
	 * Saves a menu to file
	 * @param menu the Menu to be saved
	 * @return true if the Menu was saved, false otherwise
	 */
	public boolean saveMenu(Menu menu);
	
	public void reloadMenu(Menu menu);
	
	/**
	 * Removes the Menu and saves it to file
	 * @param menu the Menu to be unloaded
	 */
	public void unloadMenu(Menu menu);
	
	//--------------------------Methods for all MenuInstances of a Menu--------------------------
	
	/**
	 * Returns a List of the MenuInstances that are currently loaded for the given Menu
	 * @param menu the given Menu
	 * @return all menuInstances for the given Menu
	 */
	public List<MenuInstance> getMenuInstances(Menu menu);
	
	//--------------------------Methods for a MenuInstance of a Menu--------------------------
	
	/**
	 * Returns a MenuInstance from the MenuService
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance
	 * @return the MenuInstance if it exists, null otherwise
	 */
	public MenuInstance getMenuInstance(Menu menu, String instanceName);
	
	/**
	 * Checks if the MenuService has a MenuInstance
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance
	 * @return true if it exists, false otherwise
	 */
	public boolean hasMenuInstance(Menu menu, String instanceName);
	
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
	 * Removes a MenuInstance from the MenuService
	 * @param instance the MenuInstance to be removed
	 */
	public void removeMenuInstance(MenuInstance instance);
	
	/**
	 * Removes a MenuInstance from the MenuService
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance to be removed
	 * @return the MenuInstance if it exists, null otherwise
	 */
	public MenuInstance removeMenuInstance(Menu menu, String instanceName);
	
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
	
	/**
	 * Closes the MenuInstance for a player
	 * 
	 * Tells the MenuProvider for the MenuInstance to close the Menu.
	 * Removes the player from the MenuInstance.
	 * 
	 * @param playerName the Name of the player
	 */
	public void closeMenuInstance(String playerName);
	
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
	public void removeRenderer(Renderer renderer);
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param rendererName the name of the Renderer to be removed
	 * @return the Renderer if it exists, null otherwise
	 */
	public Renderer removeRenderer(String rendererName);
	
	//--------------------------Methods for Binds--------------------------
	
	public void loadBinds();
	
	public void saveBinds();
	
	public Map<Material, String> getMaterialBinds();
	
	/**
	 * Binds a Material to a Menu, so the Menu can be opened by right-clicking the Material
	 * @param material the Material
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	public boolean bindMenu(Material material, Menu menu);
	
	/**
	 * Unbinds any Materials and ItemStacks from a given Menu
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	public boolean unbindMenu(Menu menu);
	
	/**
	 * Unbinds a Material from a menu if it is binded
	 * @param material the Material
	 * @return true if successful, false if unsuccessful
	 */
	public boolean unbindMenu(Material material);
	
	
	
}
