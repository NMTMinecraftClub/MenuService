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
	 * Saves a Menu to file
	 * @param plugin the Plugin which will hold the menu
	 * @param menu the Menu to be saved
	 * @param fileName the name of the file to store the menu in
	 * @return true if successful, false is unsuccessful
	 */
	public boolean saveMenu(Plugin plugin, Menu menu, String fileName);
	
	/**
	 * Adds a Menu to the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 */
	public boolean addMenu(Plugin plugin, Menu menu);
	
	/**
	 * Returns a Menu from the MenuService
	 * @param plugin the Plugin which stores the Menu
	 * @param menuName the name of the Menu
	 * @return the Menu if it exists, null otherwise
	 */
	public Menu getMenu(Plugin plugin, String menuName);
	
	/**
	 * Checks if a Menu is loaded into the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 * @return true if the MenuService has the Menu, false otherwise
	 */
	public boolean hasMenu(Plugin plugin, Menu menu);
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param plugin The Plugin which owns the Menu.
	 * @param menu the Menu to remove
	 */
	public void removeMenu(Plugin plugin, Menu menu);
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param plugin The Plugin which owns the Menu.
	 * @param menuName the name of the Menu
	 * @return The Menu if it exists, null otherwise
	 */
	public Menu removeMenu(Plugin plugin, String menuName);
	
	/**
	 * Returns a List of the Menus that are currently loaded in the MenuService
	 * @return all currently loaded Menus
	 */
	public List<Menu> getMenus();
	
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
	
	
	public List<MenuInstance> getMenuInstances(Menu menu);
	
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
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param rendererName the name of the Renderer to be removed
	 * @return the Renderer if it exists, null otherwise
	 */
	public Renderer removeRenderer(String rendererName);
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param renderer the Renderer to be removed
	 */
	public void removeRenderer(Renderer renderer);
	
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
	 * Binds an ItemStack to a Menu, so the Menu can be opened by right-clicking the ItemStack
	 * @param item the ItemStack
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	public boolean bindMenu(ItemStack item, Menu menu);
	
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
	 * Unbinds an ItemStack from a menu if it is binded
	 * @param item the ItemStack
	 * @return true if successful, false if unsuccessful
	 */
	public boolean unbindMenu(ItemStack item);
	
	/**
	 * Unbinds a Material from a menu if it is binded
	 * @param material the Material
	 * @return true if successful, false if unsuccessful
	 */
	public boolean unbindMenu(Material material);
	
	public Map<Material, String> getMaterialBinds();
	public Map<ItemStack, String> getItemStackBinds();
	public void setMaterialBinds(Map<Material, String> materialBinds);
	public void setItemStackBinds(Map<ItemStack, String> itemBinds);

	
	/**
	 * Saves all Menus to file
	 */
	public void saveMenus();
	
	/**
	 * Closes all Menus
	 */
	public void closeMenus();
	
	public void loadMenus();
	
	public void loadBinds();
	
	public void saveBinds();
}
