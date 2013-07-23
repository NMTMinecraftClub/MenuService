package com.m0pt0pmatt.menuservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * The MenuServiceProvider is the provider for the Bukkit service MenuService.
 * It is the implementation of the MenuService API
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 *
 */
public class MenuServiceProvider implements MenuService, Listener{

	//Menus are kept for reference
	private Map<String, Menu> menusByName;
	
	//Renderers are kept for reference
	private Map<String, Renderer> renderersByName;
	
	//players and their current MenuInstance are stored
	private Map<String, MenuInstance> playersToInstances;
	
	//The MenuServiceProvider keeps track of all of the instances for a Menu
	private Map<Menu, List<MenuInstance>> menusToInstances;
	
	//Commands to menus, so the MenuService plugin know which menu to show when a registered command is ran
	private Map<String, Menu> commandsToMenus;
	
	//The Map of binded Materials
	private Map<Material, Menu> materialsToMenus;
	
	//The Map of binded ItemStacks
	private Map<ItemStack, Menu> itemsToMenus;
	
	//The plugin which loaded the MenuServiceProvider
	private MenuServicePlugin plugin;
	
	//the Yaml File loader/saver
	private YAMLBuilder yamlBuilder;
	
	/**
	 * Creates the MenuServiceProvider.
	 * The plugin of it's creator is needed so the MenuServiceProvider can register events.
	 * All built in Renderers and MenuProviders are added here.
	 * 
	 * @param plugin The Plugin which owns the MenuServiceProvider
	 */
	public MenuServiceProvider(MenuServicePlugin plugin){
		
		//set the plugin for logging purposes
		this.plugin = plugin;
		plugin.log(3, Level.INFO, "Starting initialization of MenuServiceProvider");
		
		//initialize the Yaml Builder
		yamlBuilder = new YAMLBuilder();
		plugin.log(3, Level.INFO, "Yaml Builder initialized");
		
		//initialize maps
		menusByName = Collections.synchronizedMap(new HashMap<String, Menu>());
		renderersByName = Collections.synchronizedMap(new HashMap<String, Renderer>());
		playersToInstances = Collections.synchronizedMap(new HashMap<String, MenuInstance>());
		menusToInstances = Collections.synchronizedMap(new HashMap<Menu, List<MenuInstance>>());
		commandsToMenus = Collections.synchronizedMap(new HashMap<String, Menu>());
		materialsToMenus = Collections.synchronizedMap(new HashMap<Material, Menu>());
		itemsToMenus = Collections.synchronizedMap(new HashMap<ItemStack, Menu>());
		plugin.log(3, Level.INFO, "Maps initialized");
		
		//add Renderers
		addRenderer(new InventoryRenderer(this, plugin));
		plugin.log(1, Level.INFO, "InventoryRenderer loaded");
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.log(3, Level.INFO, "MenuServiceProvider registered in Bukkit");
		
		plugin.log(3, Level.INFO, "MenuServiceProvider initialized");
	}
	
	
	/**
	 * Closes the MenuInstance for a player
	 * 
	 * Tells the MenuProvider for the MenuInstance to close the Menu.
	 * Removes the player from the MenuInstance.
	 * 
	 * @param playerName the Name of the player
	 */
	@Override
	public void closeMenuInstance(String playerName) {
		
		//get the MenuInstance
		MenuInstance instance = playersToInstances.get(playerName);
		
		//check if null
		if (instance == null){
			plugin.log(2, Level.SEVERE, "No MenuInstance was found for the player " + playerName);
			return;
		}
		
		for (Renderer renderer: instance.getAllRenderers()){
			renderer.closeMenu(playerName);
		}
		
		//remove the player from the instance
		instance.removePlayer(playerName);
		
		//if there are no more players for the instance, notify the ActionListeners
		if (instance.getPlayers().size() == 0){
			for (ActionListener listener: instance.getActionListeners().values()){
				listener.playerCountZero(instance, playerName);
			}
		}
		
		//unregister the player from the MenuInstance
		this.playersToInstances.remove(playerName);
		
		this.plugin.getLogger().info("MenuInstance closed for player " + playerName);
	}
	
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
	@Override
	public Menu loadMenu(Plugin plugin, String fileName) {
		
		//check the plugin
		if (plugin == null){
			this.plugin.log(2, Level.SEVERE, "plugin was null");
			this.plugin.log(2, Level.SEVERE, "could not load menu");
			return null;
		}
		
		//check the filename
		if (fileName == null){
			this.plugin.log(2, Level.SEVERE, "filename was null");
			this.plugin.log(2, Level.SEVERE, "could not load menu");
			return null;
		}
		
		//load the file
		Menu menu = yamlBuilder.loadYAML(plugin, fileName);
		if (menu == null){
			this.plugin.log(2, Level.SEVERE, "Menu " + fileName + " is not loaded");
			this.plugin.log(2, Level.SEVERE, "could not load menu");
			return null;
		}
		
		//add the menu
		this.addMenu(plugin, menu);
		
		this.plugin.log(2, Level.INFO, "Menu " + menu.getName() + " loaded"); 
		return menu;
	}
	
	/**
	 * Saves a Menu to file
	 * @param plugin the Plugin which will hold the menu
	 * @param menu the Menu to be saved
	 * @param fileName the name of the file to store the menu in
	 * @return true if successful, false is unsuccessful
	 */
	@Override
	public boolean saveMenu(Plugin plugin, Menu menu, String fileName) {
		
		//check the plugin
		if (plugin == null){
			this.plugin.getLogger().warning("Plugin was null.");
			this.plugin.getLogger().warning("Could not save Menu.");
			return false;
		}
		
		//check the menu
		if (menu == null){
			this.plugin.getLogger().warning("menuName was null.");
			this.plugin.getLogger().warning("Could not save Menu.");
			return false;
		}
		
		//check the fileName
		if (fileName == null){
			this.plugin.getLogger().warning("filename was null.");
			this.plugin.getLogger().warning("Could not save Menu " + menu.getName());
			return false;
		}
		
		//save the Menu
		return this.yamlBuilder.saveYAML(plugin, menu, fileName);
	}
	
	/**
	 * Adds a Menu to the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 */
	@Override
	public void addMenu(Plugin plugin, Menu menu) {
		if (plugin == null){
			return;
		} else if (menu == null){
			return;
		}
		
		if (menusByName.containsKey(menu.getName())){
			plugin.getLogger().warning("Menu " + menu.getName() + " is already added");
			plugin.getLogger().warning("Menu " + menu.getName() + " created but not added");
			return;
		}
		
		menusByName.put(menu.getName(), menu);
		menusToInstances.put(menu, new LinkedList<MenuInstance>());
		plugin.getLogger().info("Menu " + menu.getName() + " was created and added");
		
		String command = (String) menu.getAttribute("openCommand");
		if (command != null){
			commandsToMenus.put(command, menu);
			plugin.getLogger().info("Run " + menu.getName() + " the the command: /" + command);
		}
		
	}

	/**
	 * Returns a Menu from the MenuService
	 * @param plugin the Plugin which stores the Menu
	 * @param menuName the name of the Menu
	 * @return the Menu if it exists, null otherwise
	 */
	@Override
	public Menu getMenu(Plugin plugin, String menuName) {
		
		//get the menu
		Menu menu = menusByName.get(menuName);
		if (menu == null){
			plugin.getLogger().warning("Menu " + menuName + " is not loaded into MenuService");
			plugin.getLogger().warning("Could not get Menu");
			return null;
		}
		
		if (!((String)menu.getAttribute("plugin")).equals(plugin.getName())){
			plugin.getLogger().warning("Menu " + menuName + " loaded but under a different plugin: " + menu.getPlugin());
			plugin.getLogger().warning("Could not get Menu");
			return menu;
		}
		
		return null;
	}

	/**
	 * Checks if a Menu is loaded into the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 * @return true if the MenuService has the Menu, false otherwise
	 */
	@Override
	public boolean hasMenu(Plugin plugin, Menu menu) {
		
		if (menusByName.containsValue(menu)){
			Menu m = menusByName.get(menu.getName());
			if (m.equals(menu)){
				return true;
			}
		}
		
		plugin.getLogger().info("Menu " + menu.getName() + " is not loaded in MenuService");
		return false;
	}
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param plugin The Plugin which owns the Menu.
	 * @param menu the Menu to remove
	 */
	@Override
	public void removeMenu(Plugin plugin, Menu menu) {
		
		if (plugin == null){
			this.plugin.getLogger().warning("Plugin was null");
			this.plugin.getLogger().warning("Could not remove menu");
		}else if (menu == null){
			this.plugin.getLogger().warning("Menu was null");
			this.plugin.getLogger().warning("Could not remove menu");
			return;
		}
		
		if (!plugin.getName().equals(menu.getAttribute("plugin"))){
			return;
		}
		
		//remove the menu
		menusByName.remove(menu);
		
		//remove all of the MenuInstances for the Menu
		List<MenuInstance> instances = menusToInstances.remove(menu);
		for (MenuInstance instance: instances){
			this.removeMenuInstance(instance);
		}
		
		this.plugin.getLogger().warning("Removed Menu " + menu.getName());
	}

	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param plugin The Plugin which owns the Menu.
	 * @param menuName the name of the Menu
	 * @return The Menu if it exists, null otherwise
	 */
	@Override
	public Menu removeMenu(Plugin plugin, String menuName) {
		
		if (menuName == null){
			this.plugin.getLogger().warning("Menu name was null");
			return null;
		}
		
		//get the menu
		Menu menu = menusByName.get(menuName);
		
		if (plugin.getName().equals(menu.getAttribute("plugin"))){
			//remove the menu
			removeMenu(plugin, menu);
			this.plugin.getLogger().warning("Removed menu " + menuName);
			return menu;
		}
		
		this.plugin.getLogger().warning("Could not remove menu");
		return null;
	}
	
	/**
	 * Creates a MenuInstance from the given Menu.
	 * MenuInstances are given names so ActionListeners know when they open or close.
	 * 
	 * While a Menu is an abstract layout, a MenuInstance is an implementation of the Menu.
	 * Once created, a MenuInstance can be shown to players.
	 * 
	 * The MenuInstance is returned after it is created.
	 * It is also registered under it's Menu in the MenuService
	 * @param menu The Menu which the MenuInstance will be created from
	 * @param provider The MenuProvider which will provide for the MenuInstance
	 */
	@Override
	public MenuInstance createMenuInstance(Menu menu, String instanceName) {
		return createMenuInstance(menu, instanceName, new HashMap<String, Object>());
	}
	
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
	@Override
	public MenuInstance createMenuInstance(Menu menu, String instanceName, Map<String, Object> parameters) {
		
		if (menu == null){
			plugin.getLogger().warning("Menu was null. Could not create MenuInstance.");
			return null;
		} else if (instanceName == null){
			plugin.getLogger().warning("instanceName was null. Could not create MenuInstance.");
			return null;
		} else if (parameters == null){
			parameters = new HashMap<String, Object>();
		}
		
		if (!menusByName.containsKey(menu.getName())){
			plugin.getLogger().warning("Menu not found. Could not create MenuInstance.");
			return null;
		}
		
		//create the MenuInstance
		MenuInstance instance = new MenuInstance(menu, instanceName, new LinkedList<String>(), parameters, new HashMap<String, Renderer>(), new HashMap<String, ActionListener>());
		
		//add the MenuInstance to the Menu
		menusToInstances.get(menu).add(instance);
		
		plugin.getLogger().info("MenuInstance " + instanceName + " was created");

		return instance;
	}
	
	/**
	 * Removes a MenuInstance from the MenuService
	 * @param instance the MenuInstance to be removed
	 */
	@Override
	public void removeMenuInstance(MenuInstance instance) {
		
		if (instance == null){
			return;
		} else if (!menusToInstances.containsKey(instance.getMenu())){
			return;
		}
		
		//unregister the instance with the menu
		menusToInstances.get(instance.getMenu()).remove(instance);
		
		//unregister all players with the menu instance
		for (String playerName: instance.getPlayers()){
			playersToInstances.remove(playerName);
		}
		
	}
	
	/**
	 * Removes a MenuInstance from the MenuService
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance to be removed
	 * @return the MenuInstance if it exists, null otherwise
	 */
	@Override
	public MenuInstance removeMenuInstance(Menu menu, String instanceName) {
		
		if (menu == null){
			return null;
		} else if (instanceName == null){
			return null;
		}
		
		return this.getMenuInstance(menu, instanceName);
	}

	/**
	 * Returns a MenuInstance from the MenuService
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance
	 * @return the MenuInstance if it exists, null otherwise
	 */
	@Override
	public MenuInstance getMenuInstance(Menu menu, String instanceName) {
		if (menu == null){
			return null;
		} else if (instanceName == null){
			return null;
		}
		
		List<MenuInstance> instances = menusToInstances.get(menu);
		if (instances == null){
			return null;
		}
		
		for (MenuInstance instance: instances){
			if (instance.getName().equals(instanceName)){
				return instance;
			}
		}
		
		return null;
		
	}

	/**
	 * Checks if the MenuService has a MenuInstance
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance
	 * @return true if it exists, false otherwise
	 */
	@Override
	public boolean hasMenuInstance(Menu menu, String instanceName) {
		if (this.getMenuInstance(menu, instanceName) != null){
			return true;
		}
		
		return false;
		
	}
	
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean openMenuInstance(MenuInstance instance, String playerName) {
		
		Menu menu = instance.getMenu();
		if (menu == null){
			return false;
		}
		
		Player player = Bukkit.getPlayer(playerName);
		if (player == null){
			return false;
		}
		
		if (menu.hasAttribute("permissions")){
			List<String> permissions;
			try{ permissions = (List<String>) menu.getAttribute("permissions"); }
			catch (ClassCastException e){ return false;}
			if (permissions != null){
				for (String permission: permissions){
					if (!player.hasPermission(permission)){
						player.sendMessage("¤cYou do not have permission to open the menu");
						return false;
					}
				}
			}
		}
		
		//add the player to the instance
		instance.getPlayers().add(playerName);
		for (ActionListener listener: instance.getActionListeners().values()){
			listener.playerAdded(instance, playerName);
		}
		
		//register the player
		playersToInstances.put(playerName, instance);
		
		//render the MenuInstance
		instance.renderPlayer(playerName);
		
		return true;
	}
	
	/**
	 * Adds a Renderer to the MenuService.
	 * After added, a Renderer can be used to Render MenuInstances.
	 * Renderers can be assigned to Menus (and thus every MenuInstance of a menu) or individual MenuInstances.
	 * @param renderer The Renderer to be added to the MenuService
	 */
	@Override
	public void addRenderer(Renderer renderer) {
		
		//check for null
		if (renderer == null){
			plugin.getLogger().warning("Renderer was null.");
			return;
		}
		
		//check to make sure a MenuProvider of the same name isn't already loaded
		if (renderersByName.containsKey(renderer.getName())){
			plugin.getLogger().warning("A Renderer with the name " + renderer.getName() + " is already loaded.");
			plugin.getLogger().warning("Could not load Renderer " + renderer.getName());
			return;
		}
		
		//make sure Renderer is an extension of an AbstractRenderer
		if (!(renderer instanceof AbstractRenderer)){
			plugin.getLogger().warning(renderer.getName() + " does not extend AbstractRenderer");
			plugin.getLogger().warning("Could not load Renderer " + renderer.getName());
			return;
		}
		
		//add the Renderer
		renderersByName.put(renderer.getName(), renderer);
		
		plugin.getLogger().info("Renderer " + renderer.getName() + " was added");
	}
	
	/**
	 * Returns a Renderer by name from the MenuService.
	 * @param rendererName The name of the Renderer
	 * @return The specified Renderer, or null if one does not exist
	 */
	@Override
	public Renderer getRenderer(String rendererName) {
		return renderersByName.get(rendererName);
	}
	
	/**
	 * Removes a Renderer from the MenuService
	 * @param rendererName the name of the Renderer to be removed
	 * @return the Renderer if it exists, null otherwise
	 */
	@Override
	public Renderer removeRenderer(String rendererName) {
		
		if (rendererName == null){
			return null;
		}
		
		Renderer renderer = renderersByName.get(rendererName);
		
		removeRenderer(renderer);
		
		return renderer;
	}

	/**
	 * Removes a Renderer from the MenuService
	 * @param renderer the Renderer to be removed
	 */
	@Override
	public void removeRenderer(Renderer renderer) {
		
		if (renderer == null){
			return;
		} else if (!renderersByName.containsKey(renderer)){
			return;
		}
		
		renderersByName.remove(renderer);
		
	}
	
	/**
	 * Checks if the MenuService has a specified Renderer
	 * @param the Renderer
	 * @return true if the Renderer exists, false otherwise
	 */
	@Override
	public boolean hasRenderer(Renderer renderer) {
		return renderersByName.containsValue(renderer);
	}

	/**
	 * Checks if the MenuService has a specified Renderer
	 * @param rendererName the name of the Renderer
	 * @return true if the Renderer exists, false otherwise
	 */
	@Override
	public boolean hasRenderer(String rendererName) {
		return renderersByName.containsKey(rendererName);
	}

	/**
	 * Binds an ItemStack to a Menu, so the Menu can be opened by right-clicking the ItemStack
	 * @param item the ItemStack
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean bindMenu(ItemStack item, Menu menu) {
		if (item == null || menu == null){
			return false;
		}
		itemsToMenus.put(new ItemStack(item), menu);
		return true;
	}

	/**
	 * Binds a Material to a Menu, so the Menu can be opened by right-clicking the Material
	 * @param material the Material
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean bindMenu(Material material, Menu menu) {
		if (material == null || menu == null){
			return false;
		}
		materialsToMenus.put(material, menu);
		return true;
	}

	/**
	 * Unbinds any Materials and ItemStacks from a given Menu
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean unbindMenu(Menu menu) {
		if (itemsToMenus.containsValue(menu)){
			Iterator<Entry<ItemStack, Menu>> iterator = itemsToMenus.entrySet().iterator();
			while (iterator.hasNext()){
				if (iterator.next().getKey().equals(menu)){
					iterator.remove();
				}
			}
		}
		
		if (materialsToMenus.containsValue(menu)){
			Iterator<Entry<Material, Menu>> iterator = materialsToMenus.entrySet().iterator();
			while (iterator.hasNext()){
				if (iterator.next().getKey().equals(menu)){
					System.out.println("removeeeeed");
					iterator.remove();
				}
			}
		}
		
		return true;
	}

	/**
	 * Unbinds an ItemStack from a menu if it is binded
	 * @param item the ItemStack
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean unbindMenu(ItemStack item) {
		itemsToMenus.remove(item);
		return true;
	}

	/**
	 * Unbinds a Material from a menu if it is binded
	 * @param material the Material
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean unbindMenu(Material material) {
		materialsToMenus.remove(material);
		return true;
	}

	/**
	 * Saves all Menus to file
	 */
	@Override
	public void saveAll() {
		for (Menu menu: menusByName.values()){
			
			Plugin plugin = Bukkit.getPluginManager().getPlugin((String) menu.getAttribute("plugin"));
			if (plugin != null){
				String filename = null;
				if (menu.hasAttribute("filename")){
					filename = (String) menu.getAttribute("filename");
				} else{
					filename = menu.getTag() + ".yml";
				}
				yamlBuilder.saveYAML(plugin, menu, filename);
			}
			
			
		}
	}

	/**
	 * Closes all Menus
	 */
	@Override
	public void closeAll() {
		for (Renderer renderer: renderersByName.values()){
			renderer.closeAll();
		}
		
	}


	/**
	 * Checks if a binded command is being executed. If so, opens the correct Menu.
	 * @param command the Command which was executed
	 * @param player the name of the player who executed the command
	 * @return true if the command was binded, false otherwise
	 */
	protected boolean checkCommand(String command, Player player){

		for (Entry<String, Menu> entry: commandsToMenus.entrySet()){
			
			//check command
			if (command.equals(entry.getKey())){
								
				//commands match. Start Menu
				MenuInstance instance = createMenuInstance(entry.getValue(), player.getName() + ": " + entry.getValue().getName());
				openMenuInstance(instance, player.getName());
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the player right clicked a binded ItemStack or Material
	 * If so, opens the binded menu.
	 * @param event
	 */
	@EventHandler
	public void itemClick(PlayerInteractEvent event){
		
		ItemStack item = event.getItem();

		if (event.getAction() != Action.RIGHT_CLICK_AIR){
			return;
		}

		if (!event.isBlockInHand()){
			return;
		}

		if (materialsToMenus.containsKey(item.getType())){

			event.setCancelled(true);
			Menu menu = materialsToMenus.get(item.getType());
			this.openMenuInstance(this.createMenuInstance(menu, menu.getName() + ": " + event.getPlayer().getName()), event.getPlayer().getName());
			return;
		}
	}

}
