package com.m0pt0pmatt.menuservice;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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
import com.m0pt0pmatt.menuservice.renderers.InventoryRenderer;
import com.m0pt0pmatt.menuservice.renderers.TextRenderer;

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
	private Map<Material, String> materialsToMenus;
	
	//The Map of binded ItemStacks
	private Map<ItemStack, String> itemsToMenus;
	
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
		Logger.log(3, Level.INFO, "Starting initialization of MenuServiceProvider");
		
		//initialize the Yaml Builder
		yamlBuilder = new YAMLBuilder();
		Logger.log(3, Level.INFO, "Yaml Builder initialized");
		
		//initialize maps
		menusByName = Collections.synchronizedMap(new HashMap<String, Menu>());
		renderersByName = Collections.synchronizedMap(new HashMap<String, Renderer>());
		playersToInstances = Collections.synchronizedMap(new HashMap<String, MenuInstance>());
		menusToInstances = Collections.synchronizedMap(new HashMap<Menu, List<MenuInstance>>());
		commandsToMenus = Collections.synchronizedMap(new HashMap<String, Menu>());
		materialsToMenus = Collections.synchronizedMap(new HashMap<Material, String>());
		itemsToMenus = Collections.synchronizedMap(new HashMap<ItemStack, String>());
		Logger.log(3, Level.INFO, "Maps initialized");
		
		//add Renderers
		this.addRenderer(new InventoryRenderer(this, plugin));
		this.addRenderer(new TextRenderer(this, plugin));
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Logger.log(3, Level.INFO, "MenuServiceProvider registered in Bukkit");
		
		Logger.log(3, Level.INFO, "MenuServiceProvider initialized");
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
			return;
		}
		
		for (Renderer renderer: instance.getAllRenderers()){
			renderer.closeMenu(playerName);
		}
		
		//remove the player from the instance
		instance.removePlayer(playerName);
		
		//unregister the player from the MenuInstance
		this.playersToInstances.remove(playerName);
		
		//if there are no more players for the instance, notify the ActionListeners
		if (instance.getPlayers().size() == 0){
			for (ActionListener listener: instance.getActionListeners().values()){
				listener.playerCountZero(instance, playerName);
			}
			if (instance.hasParameter("removeOnEmpty")){
				if ((Boolean)instance.getParameter("removeOnEmpty")){
					this.removeMenuInstance(instance);
				}
			}
		}
		
		
		
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
		
		//check the filename
		if (fileName == null){
			Logger.log(2, Level.SEVERE, Message.NULLFILENAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, null);
			return null;
		}
				
		//check the plugin
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, fileName);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, fileName);
			return null;
		}		
		
		//load the file
		Menu menu = yamlBuilder.loadMenu(plugin, fileName);

		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHFILE, fileName);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, fileName);
			return null;
		}
		
		//add the menu
		if (this.addMenu(plugin, menu)){
			Logger.log(2, Level.INFO, "Menu " + menu.getName() + " loaded"); 
			return menu;
		}
		
		Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, fileName);
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
		
		//check the menu
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTSAVEMENU, null);
			return false;
		}
		
		//check the plugin
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTSAVEMENU, menu.getName());
			return false;
		}
		
		//check the fileName
		if (fileName == null){
			Logger.log(2, Level.SEVERE, Message.NULLFILENAME, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTSAVEMENU, menu.getName());
			return false;
		}
		
		//save the Menu
		return this.yamlBuilder.saveYAML(plugin, menu, fileName);
	}
	
	@Override
	public boolean saveMenu(Menu menu){
		
		if (menu == null){
			Logger.log(2, Level.WARNING, Message.NULLMENU);
			return false;
		}
		
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin(menu.getPlugin());
		if (plugin == null){
			Logger.log(2, Level.WARNING, Message.NOSUCHPLUGIN, menu.getName());
			return false;
		}
		
		return saveMenu(plugin, menu, menu.getFileName());
	}
	
	/**
	 * Adds a Menu to the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 */
	@Override
	public boolean addMenu(Plugin plugin, Menu menu) {
		
		//check menu
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTADDMENU, null);
			return false;
		}
		
		//check Plugin
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTADDMENU, menu.getName());
			return false;
		}
		
		//check if menu exists
		if (menusByName.containsKey(menu.getName())){
			Logger.log(2, Level.SEVERE, Message.MENUALREADYEXISTS, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTADDMENU, menu.getName());
			return false;
		}
		
		menusByName.put(menu.getName(), menu);
		menusToInstances.put(menu, new LinkedList<MenuInstance>());
		Logger.log(2, Level.INFO, "Menu " + menu.getName() + " was created and added");
		
		String command = (String) menu.getAttribute("openCommand");
		if (command != null){
			commandsToMenus.put(command, menu);
			Logger.log(2, Level.INFO, "Run " + menu.getName() + " with the command: /" + command);
		}
		
		//add a default renderer if it has none
		if (menu.getRenderers().size() == 0){
			menu.addRenderer(this.getRenderer("inventory"));
		}
		
		return true;
	}

	/**
	 * Returns a Menu from the MenuService
	 * @param plugin the Plugin which stores the Menu
	 * @param menuName the name of the Menu
	 * @return the Menu if it exists, null otherwise
	 */
	@Override
	public Menu getMenu(String menuName) {
		
		//check for null menuName
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTGETMENU, null);
			return null;
		}
		
		//get the menu
		Menu menu = menusByName.get(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTGETMENU, menuName);
			return null;
		}
		
		return menu;
	}

	/**
	 * Checks if a Menu is loaded into the MenuService
	 * @param plugin the Plugin which owns the Menu
	 * @param menu the Menu
	 * @return true if the MenuService has the Menu, false otherwise
	 */
	@Override
	public boolean hasMenu(Plugin plugin, Menu menu) {
		
		//check menu
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTHASMENU, null);
		}
		
		//check plugin
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTHASMENU, menu.getName());
		}
		
		//see if MenuService contains the menu
		if (menusByName.containsValue(menu)){
			
			//check if same
			Menu m = menusByName.get(menu.getName());
			if (m.equals(menu)){
				return true;
			}
		}
		
		//not the same
		return false;
	}
	
	/**
	 * Removes a Menu from the MenuService. This does not save the menu to file.
	 * @param plugin The Plugin which owns the Menu.
	 * @param menu the Menu to remove
	 */
	@Override
	public void removeMenu(Menu menu) {
		
		//check menu
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENU, null);
			return;
		}
		
		//remove all of the MenuInstances for the Menu
		List<MenuInstance> instances = menusToInstances.remove(menu);
		for (MenuInstance instance: instances){
			this.removeMenuInstance(instance);
		}
		
		//remove all commands
		Iterator<Entry<String, Menu>> i = this.commandsToMenus.entrySet().iterator();
		while (i.hasNext()){
			if (i.next().getValue().equals(menu)){
				i.remove();
			}
		}
		
		//remove all binds
		this.unbindMenu(menu);
		
		//remove the menu
		menusByName.remove(menu.getName());
				
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
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENU, null);
			return null;
		}
		
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENU, menuName);
			return null;
		}
		
		//get the menu
		Menu menu = menusByName.get(menuName);
		
		if (plugin.getName().equals(menu.getAttribute("plugin"))){
			//remove the menu
			removeMenu(menu);
			Logger.log(2, Level.SEVERE, "Removed menu " + menuName);
			return menu;
		}
		
		Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
		Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENU, menuName);
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
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTCREATEMENUINSTANCE, null);
			return null;
		}
		
		if (instanceName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCENAME, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTCREATEMENUINSTANCE, menu.getName());
			return null;
		}
		
		if (parameters == null){
			parameters = new HashMap<String, Object>();
		}
		
		if (!menusByName.containsKey(menu.getName())){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTCREATEMENUINSTANCE, null);
			return null;
		}
		
		//create the MenuInstance
		MenuInstance instance = new MenuInstance(menu, instanceName, new LinkedList<String>(), parameters, new HashMap<String, Renderer>(), new HashMap<String, ActionListener>());
		
		//add the MenuInstance to the Menu
		menusToInstances.get(menu).add(instance);
		
		Logger.log(2, Level.INFO, "MenuInstance " + instanceName + " was created");

		return instance;
	}
	
	/**
	 * Removes a MenuInstance from the MenuService
	 * @param instance the MenuInstance to be removed
	 */
	@Override
	public void removeMenuInstance(MenuInstance instance) {
		
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCE, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENUINSTANCE, null);
			return;
		}
		
		if (!menusToInstances.containsKey(instance.getMenu())){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, instance.getMenu().getName());
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENUINSTANCE, instance.getName());
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
		
		if (instanceName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCE, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENUINSTANCE, null);
			return null;
		}
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENUINSTANCE, null);
			return null;
		}
		
		MenuInstance instance =  this.getMenuInstance(menu, instanceName);
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENUINSTANCE, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVEMENUINSTANCE, null);
			return null;
		}
		
		this.removeMenuInstance(instance);
		
		return instance;
	}

	/**
	 * Returns a MenuInstance from the MenuService
	 * @param menu the Menu which the MenuInstance is made of
	 * @param instanceName the name of the MenuInstance
	 * @return the MenuInstance if it exists, null otherwise
	 */
	@Override
	public MenuInstance getMenuInstance(Menu menu, String instanceName) {
		
		if (instanceName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCENAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTGETMENUINSTANCE, null);
			return null;
		}
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, instanceName);
			Logger.log(2, Level.SEVERE, Message.CANTGETMENUINSTANCE, instanceName);
			return null;
		}
		

		List<MenuInstance> instances = menusToInstances.get(menu);
		if (instances == null){
			Logger.log(2, Level.SEVERE, Message.CANTGETMENUINSTANCE, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTGETMENUINSTANCE, instanceName);
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
		
		if (instanceName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCENAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTHASMENUINSTANCE, null);
			return false;
		}
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, instanceName);
			Logger.log(2, Level.SEVERE, Message.CANTHASMENUINSTANCE, instanceName);
			return false;
		}
		
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
		
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCE, null);
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENUINSTANCE, null);
			return false;
		}
		
		if (playerName == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLAYERNAME, instance.getName());
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENUINSTANCE, instance.getName());
			return false;
		}
		
		Menu menu = instance.getMenu();
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, instance.getName());
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENUINSTANCE, instance.getName());
			return false;
		}
		
		Player player = Bukkit.getPlayer(playerName);
		if (player == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHPLAYER, instance.getName());
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENUINSTANCE, instance.getName());
			return false;
		}
		
		if (menu.hasAttribute("permissions")){
			List<String> permissions;
			try{
				permissions = (List<String>) menu.getAttribute("permissions");
			}
			catch (ClassCastException e){
				Logger.log(2, Level.SEVERE, Message.CANTCASTATTRIBUTE, "permissions");
				Logger.log(2, Level.SEVERE, Message.CANTOPENMENUINSTANCE, instance.getName());
				return false;
			}
			if (permissions != null){
				for (String permission: permissions){
					if (!player.hasPermission(permission)){
						player.sendMessage(ChatColor.RED + "You do not have permission to open the menu");
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
	public boolean addRenderer(Renderer renderer) {
		
		//check for null
		if (renderer == null){
			Logger.log(2, Level.SEVERE, Message.NULLRENDERER, null);
			Logger.log(2, Level.SEVERE, Message.CANTADDRENDERER, null);
			return false;
		}
		
		//check to make sure a MenuProvider of the same name isn't already loaded
		if (renderersByName.containsKey(renderer.getName())){
			Logger.log(2, Level.SEVERE, Message.NOSUCHRENDERER, renderer.getName());
			Logger.log(2, Level.SEVERE, Message.CANTADDRENDERER, renderer.getName());
			return false;
		}
		
		//make sure Renderer is an extension of an AbstractRenderer
		if (!(renderer instanceof AbstractRenderer)){
			Logger.log(2, Level.SEVERE, Message.RENDERERNOTABSTRACTRENDERER, renderer.getName());
			Logger.log(2, Level.SEVERE, Message.CANTADDRENDERER, renderer.getName());
			return false;
		}
		
		//add the Renderer
		renderersByName.put(renderer.getName(), renderer);
		
		Logger.log(2, Level.INFO, "Renderer " + renderer.getName() + " was added");
		
		return true;
	}
	
	/**
	 * Returns a Renderer by name from the MenuService.
	 * @param rendererName The name of the Renderer
	 * @return The specified Renderer, or null if one does not exist
	 */
	@Override
	public Renderer getRenderer(String rendererName) {
		
		if (rendererName == null){
			Logger.log(2, Level.SEVERE, Message.NULLRENDERERNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTGETRENDERER, null);
		}
		
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
			Logger.log(2, Level.SEVERE, Message.NULLRENDERERNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVERENDERER, null);
			return null;
		}
		
		Renderer renderer = renderersByName.get(rendererName);
		if (renderer == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHRENDERER, rendererName);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVERENDERER, rendererName);
			return null;
		}
		
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
			Logger.log(2, Level.SEVERE, Message.NULLRENDERER, null);
			Logger.log(2, Level.SEVERE, Message.CANTREMOVERENDERER, null);
			return;
		}
		
		if (!renderersByName.containsKey(renderer)){
			Logger.log(2, Level.SEVERE, Message.NOSUCHRENDERER, renderer.getName());
			Logger.log(2, Level.SEVERE, Message.CANTREMOVERENDERER, renderer.getName());
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
		
		if (renderer == null){
			Logger.log(2, Level.SEVERE, Message.NULLRENDERER, null);
			Logger.log(2, Level.SEVERE, Message.CANTHASRENDERER, null);
			return false;
		}
		
		return renderersByName.containsValue(renderer);
	}

	/**
	 * Checks if the MenuService has a specified Renderer
	 * @param rendererName the name of the Renderer
	 * @return true if the Renderer exists, false otherwise
	 */
	@Override
	public boolean hasRenderer(String rendererName) {
		
		if (rendererName == null){
			Logger.log(2, Level.SEVERE, Message.NULLRENDERERNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTHASRENDERER, null);
			return false;
		}
		
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
		
		if (item == null){
			Logger.log(2, Level.SEVERE, Message.NULLITEMSTACK, null);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUITEM, null);
			return false;
		}
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, item.toString());
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUITEM, item.toString());
			return false;
		}
		
		if (!menusByName.containsKey(menu.getName())){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUITEM, item.toString());
			return false;
		}
		
		itemsToMenus.put(new ItemStack(item), menu.getName());
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
		
		if (material == null){
			Logger.log(2, Level.SEVERE, Message.NULLMATERIAL, null);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUMATERIAL, null);
			return false;
		}
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUMATERIAL, material.toString());
			return false;
		}
		
		if (!menusByName.containsKey(menu.getName())){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menu.getName());
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUMATERIAL, material.toString());
			return false;
		}
		
		materialsToMenus.put(material, menu.getName());
		return true;
	}

	/**
	 * Unbinds any Materials and ItemStacks from a given Menu
	 * @param menu the Menu
	 * @return true if successful, false if unsuccessful
	 */
	@Override
	public boolean unbindMenu(Menu menu) {
		
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENU, null);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDMENU, null);
			return false;
		}
		
		if (itemsToMenus.containsValue(menu)){
			Iterator<Entry<ItemStack, String>> iterator = itemsToMenus.entrySet().iterator();
			while (iterator.hasNext()){
				if (iterator.next().getKey().equals(menu.getName())){
					iterator.remove();
				}
			}
		}
		
		if (materialsToMenus.containsValue(menu)){
			Iterator<Entry<Material, String>> iterator = materialsToMenus.entrySet().iterator();
			while (iterator.hasNext()){
				if (iterator.next().getKey().equals(menu.getName())){
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
		
		if (item == null){
			Logger.log(2, Level.SEVERE, Message.NULLITEMSTACK, null);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDITEM, null);
			return false;
		}
		
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
		
		if (material == null){
			Logger.log(2, Level.SEVERE, Message.NULLMATERIAL, null);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDMATERIAL, null);
			return false;
		}
		
		materialsToMenus.remove(material);
		return true;
	}
	
	@Override
	public Map<Material, String> getMaterialBinds() {
		return materialsToMenus;
	}


	@Override
	public Map<ItemStack, String> getItemStackBinds() {
		return itemsToMenus;
	}
	
	@Override
	public void setMaterialBinds(Map<Material, String> materialBinds) {
		this.materialsToMenus = materialBinds;
	}


	@Override
	public void setItemStackBinds(Map<ItemStack, String> itemBinds) {
		this.itemsToMenus = itemBinds;
	}

	/**
	 * Saves all Menus to file
	 */
	@Override
	public void saveMenus() {
		for (Menu menu: menusByName.values()){
			
			if (menu.hasAttribute("dynamic")){
				if ((Boolean) menu.getAttribute("dynamic")){
					continue;
				}
			}
			
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
	public void closeMenus() {
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

		if (command == null){
			Logger.log(2, Level.SEVERE, Message.NULLCOMMAND, null);
			Logger.log(2, Level.SEVERE, Message.CANTEXECUTECOMMAND, null);
			return false;
		}
		if (player == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLAYER, command);
			Logger.log(2, Level.SEVERE, Message.CANTEXECUTECOMMAND, command);
			return false;
		}
		
		for (Entry<String, Menu> entry: commandsToMenus.entrySet()){
			
			//check command
			if (command.equals(entry.getKey())){
								
				//commands match. Start Menu
				if (this.hasMenuInstance(entry.getValue(),  player.getName() + ": " + entry.getValue().getName())){
					openMenuInstance(this.getMenuInstance(entry.getValue(),  player.getName() + ": " + entry.getValue().getName()), player.getName());
				} else {
					MenuInstance instance = createMenuInstance(entry.getValue(), player.getName() + ": " + entry.getValue().getName());
					instance.addParameter("removeOnEmpty", true);
					openMenuInstance(instance, player.getName());
				}
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
		
		if (event == null){
			return;
		}
		
		ItemStack item = event.getItem();

		if (event.getAction() != Action.RIGHT_CLICK_AIR){
			return;
		}

		if (!event.isBlockInHand()){
			return;
		}

		if (materialsToMenus.containsKey(item.getType())){

			event.setCancelled(true);
			
			String menuName = materialsToMenus.get(item.getType());
			Menu menu = menusByName.get(menuName);
			//TODO: Add checks
			
			this.openMenuInstance(this.createMenuInstance(menu, menu.getName() + ": " + event.getPlayer().getName()), event.getPlayer().getName());
			return;
		}
	}

	/**
	 * Returns a List of the Menus that are currently loaded in the MenuService
	 * @return all currently loaded Menus
	 */
	@Override
	public List<Menu> getMenus() {
		
		return new LinkedList<Menu>(menusByName.values());
	}


	@Override
	public List<MenuInstance> getMenuInstances(Menu menu) {
		if (!menusByName.containsKey(menu.getName())){
			return null;
		}
		
		return menusToInstances.get(menu);
	}


	/**
	 * Loads menus stored in MenuService
	 */
	public void loadMenus() {
		
		//load menus in the MenuService folder
		for (File file: plugin.getDataFolder().listFiles()){
			
			//make sure the file is not the config file and has the .yml extension
			if ((!file.getName().equalsIgnoreCase("config.yml")) && (!file.getName().equalsIgnoreCase("binds.yml")) && file.getName().endsWith(".yml")){
				
				//load the menu
				Menu menu = this.loadMenu(plugin, file.getName());
				if (menu == null){
					continue;
				}
				
				Logger.log(2, Level.INFO, "Loaded file " + file.getName() + " from the MenuService folder");
			}
			
		}
		
	}


	@Override
	public void loadBinds() {
		//create data folder if needed
		if (!plugin.getDataFolder().exists()){
			Logger.log(2, Level.INFO, "Creating Data Folder");
			plugin.getDataFolder().mkdir();
		}
		
		//create configuration file if needed
		File configFile = new File(plugin.getDataFolder(), MenuServicePlugin.bindsFileName);
		if (!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				Logger.log(1, Level.SEVERE, "Unable to create binds file!");
			}
		}
		
		//load the configuration file
		MenuServicePlugin.binds = YamlConfiguration.loadConfiguration(configFile);
		if (MenuServicePlugin.binds == null){
			Logger.log(1, Level.SEVERE, "Unable to load binds file!");
			return;
		}
		
//		if (MenuServicePlugin.binds.contains("materials")){
//			MemorySection materialSection = (MemorySection) MenuServicePlugin.binds.get("materials");
//			for (String m: materialSection.getKeys(false)){
//				//MemorySection s = (MemorySection) materialSection.get(m);
//				
//
//			}
//		}
	}

	@Override
	public void saveBinds() {
		
		//Create file
		File bindsFile = new File(this.plugin.getDataFolder(), "binds.yml");
		try {
			if (bindsFile.exists()) bindsFile.delete();
			bindsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		YamlConfiguration bindsConfig = YamlConfiguration.loadConfiguration(bindsFile);
		
		Map<String, List<ItemStack>> reverseItemMap = new TreeMap<String, List<ItemStack>>();
		
		for (Entry<ItemStack, String> entry: this.itemsToMenus.entrySet()){
			if (!reverseItemMap.containsKey(entry.getValue())){
				reverseItemMap.put(entry.getValue(), new LinkedList<ItemStack>());
			}
			reverseItemMap.get(entry.getValue()).add(entry.getKey());
		}
		
		for (Entry<String, List<ItemStack>> entry: reverseItemMap.entrySet()){
			bindsConfig.set("itemstacks." + entry.getKey(), entry.getValue());
		}
		
		
		Map<String, List<Material>> reverseMaterialMap = new TreeMap<String, List<Material>>();
		
		for (Entry<Material, String> entry: this.materialsToMenus.entrySet()){
			if (!reverseMaterialMap.containsKey(entry.getValue())){
				reverseMaterialMap.put(entry.getValue(), new LinkedList<Material>());
			}
			reverseMaterialMap.get(entry.getValue()).add(entry.getKey());
		}
		
		for (Entry<String, List<Material>> entry: reverseMaterialMap.entrySet()){
			bindsConfig.set("materials." + entry.getKey(), entry.getValue());
		}
		
		try {
			bindsConfig.save(bindsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void reloadMenus() {
		List<Menu> menusToReload = new LinkedList<Menu>();
		
		for (Menu menu: menusByName.values()){
			if (menu.hasAttribute("dynamic")){
				if ((Boolean)menu.getAttribute("dynamic") == true){
					continue;
				}
			}
			menusToReload.add(menu);
		}
		
		for (Menu menu: menusToReload){
			this.reloadMenu(menu);
		}
	}


	@Override
	public List<Menu> unloadMenus() {
		List<Menu> list = new LinkedList<Menu>();
		
		for (Menu menu: menusByName.values()){
			if (menu.hasAttribute("permanent")){
				if ((Boolean)menu.getAttribute("permanent") == true){
					continue;
				}
			}
			list.add(menu);
		}
		for (Menu menu: list){
			this.removeMenu(menu);
		}
		
		return list;
	}


	@Override
	public void reloadMenu(Menu menu) {
		String name = menu.getName();
		String pluginName = menu.getPlugin();
		this.removeMenu(menu);
		this.loadMenu(Bukkit.getPluginManager().getPlugin(pluginName), name);
	}



}
