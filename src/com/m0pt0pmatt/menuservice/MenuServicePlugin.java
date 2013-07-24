package com.m0pt0pmatt.menuservice;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * MenuService is a plugin that allows other plugins to implement abstract menu
 * systems.
 * @author mbroomfield
 *
 */
public class MenuServicePlugin extends JavaPlugin implements Listener{
	
	/**
	 * The inventoryListener allows menus to be shown to players and interacted with players
	 */
	public static MenuServiceProvider menuService;
	
	/**
	 * The commandHandler handles all commands for the plugin
	 */
	private static CommandHandler commandHandler;
	
	/**
	 * The config file for the plugin
	 */
	private static String configFileName = "config.yml";
	
	/**
	 * The config file which stores all of the bind to menus
	 */
	private static String bindsFileName = "binds.yml";
	
	/**
	 * The config file for the plugin
	 */
	private YamlConfiguration config;
	
	/**
	 * The config file that holds all the menu binds
	 */
	private YamlConfiguration binds;
	
	/**
	 * the verbosity level of the plugin. The higher the level, the more messages will be logged to the terminal.
	 */
	public int verbose = 3;
	
	/**
	 * Executed when the plugin is enabled.
	 * Sets up internal attributes, loads configuration
	 */
	public void onEnable(){
				
		//setup the MenuService Provider
		menuService = new MenuServiceProvider(this);
		commandHandler = new CommandHandler(this);
		log(3, Level.INFO, "MenuService initialized");
		
		//register the MenuServiceProvider as the provider for the MenuService
		Bukkit.getServicesManager().register(MenuService.class, menuService, this, ServicePriority.Normal);
		log(3, Level.INFO, "MenuService registered for the server");
		
		//load the config file
		loadConfig();
		log(1, Level.INFO, "Loaded " + configFileName);
		
		//load all menus in the MenuService folder
		loadMenus();
		
		loadBinds();
		
		//register the plugin so it can listen to open menus
		Bukkit.getPluginManager().registerEvents(this, this);	
		
	}

	/**
	 * Loads the config.yml file and all of its settings
	 */
	private void loadConfig() {
		
		//create data folder if needed
		if (!this.getDataFolder().exists()){
			log(2, Level.INFO, "Creating Data Folder");
			this.getDataFolder().mkdir();
		}
		
		//create configuration file if needed
		File configFile = new File(this.getDataFolder(), configFileName);
		if (!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				log(1, Level.SEVERE, "Unable to create config file!");
			}
		}
		
		//load the configuration file
		config = YamlConfiguration.loadConfiguration(configFile);
		if (config == null){
			log(1, Level.SEVERE, "Unable to load config file!");
		}
		
		//check for verbose level
		if (config.contains("verbose")){
			verbose = config.getInt("verbose");
			log(2, Level.INFO, "Loaded verbosity level. Level is now " + verbose);
		}
		
	}
	
	private void loadBinds() {
		//create data folder if needed
		if (!this.getDataFolder().exists()){
			log(2, Level.INFO, "Creating Data Folder");
			this.getDataFolder().mkdir();
		}
		
		//create configuration file if needed
		File configFile = new File(this.getDataFolder(), bindsFileName);
		if (!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				log(1, Level.SEVERE, "Unable to create binds file!");
			}
		}
		
		//load the configuration file
		binds = YamlConfiguration.loadConfiguration(configFile);
		if (binds == null){
			log(1, Level.SEVERE, "Unable to load binds file!");
		}
		
		if (binds.contains("materials")){
			MemorySection materialSection = (MemorySection) binds.get("materials");
			for (String m: materialSection.getKeys(false)){
				MemorySection s = (MemorySection) materialSection.get(m);
				Material material = null;
				try{
					material = Material.getMaterial(Integer.parseInt(s.getName()));						
				} catch (NumberFormatException e){
					material = Material.getMaterial(s.getName());
				}
				
				if (material == null){
					continue;
				}
				
				if ((!s.contains("menu")) || (!s.contains("plugin"))){
					continue;
				}
				
				Object menu = s.get("menu");
				Object plugin = s.get("plugin");
				
				if (!(menu instanceof String) || !(plugin instanceof String)){
					continue;
				}
					
				this.bindMenu(material, (String) plugin, (String) menu);

			}
		}
	}
	
	/**
	 * Loads menus stored in MenuService
	 */
	private void loadMenus() {
		
		//load menus in the MenuService folder
		for (File file: this.getDataFolder().listFiles()){
			
			//make sure the file is not the config file and has the .yml extension
			if ((!file.getName().equalsIgnoreCase("config.yml")) && file.getName().endsWith(".yml")){
				
				//load the menu
				Menu menu = menuService.loadMenu(this, file.getName());
				
				//attach the default renderer
				Renderer renderer = menuService.getRenderer("inventory");
				menu.addRenderer(renderer);
				
				log(2, Level.INFO, "Loaded file " + file.getName() + " from the MenuService folder");
			}
			
		}
		
	}

	/**
	 * Ran when the plugin is disabled.
	 * Saves menus to file.
	 * Closes all menuInstances
	 */
	@Override
	public void onDisable(){
		
		//save all the menus to file
		log(1, Level.INFO, "Saving all menus to file");
		menuService.saveAll();
		
		//close all menuinstances
		log(1, Level.INFO, "Closing all menus");
		menuService.closeAll();
		
		//save the config file
		try {
			config.save(new File(this.getDataFolder(), "config.yml"));
			log(1, Level.INFO, "Saved " + configFileName);
		} catch (IOException e) {
			log(1, Level.SEVERE, "Unable to save " + configFileName);
		}
		
	}
	
	/**
	 * Logs messages if the verbose level is high enough.
	 * This method should be used plugin wide as the only way to log messages.
	 * @param verboseLevel The verbose level of the message being logged
	 * @param level the Bukkit level of the message
	 * @param msg The message
	 */
	public void log(int verboseLevel, Level level, String msg){
		
		//If the verbose level is high enough
		if (verboseLevel <= verbose){
			
			//log the message
			getLogger().log(level, msg);;
		}
	}
	
	/**
	 * Logs messages if the verbose level is high enough.
	 * This method should be used plugin wide as the only way to log messages.
	 * @param verboseLevel The verbose level of the message being logged
	 * @param level the Bukkit level of the message
	 * @param msg the LogMessage
	 */
	public void log(int verboseLevel, Level level, LogMessage msg, Object object){
		String m = msg.getMessage();
		if (object != null){
			m = m + ": " + object.toString();
		}
		
		log(verboseLevel, level, m);
	}
	
	/**
	 * Executed when a command is ran.
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return commandHandler.onCommand(sender, cmd, label, args);
	}
	
	
	protected void listMenus(CommandSender sender) {
		for (Menu menu: menuService.getMenus()){
			sender.sendMessage(menu.getName() + ": " + menu.getPlugin());
		}
	}

	/**
	 * Closes a menu for a given player
	 * @param playerName the name of the player
	 */
	protected void closeMenu(String playerName) {
		
		//close the menu
		menuService.closeMenuInstance(playerName);
	}

	/**
	 * Saves a Menu to file
	 * @param menuName the name of the menu
	 * @param pluginName the name of the plugin which holds the menu
	 * @param fileName the name to save the menu to
	 * @return true if successful, false if unsuccessful 
	 */
	protected boolean saveMenu(String menuName, String pluginName, String fileName) {
		
		//check menuName
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTSAVEMENU, null);
			return false;
		}
		
		//check the plugin
		if (pluginName == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGINNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTSAVEMENU, menuName);
			return false;
		}
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGIN, menuName);
			log(2, Level.SEVERE, LogMessage.CANTSAVEMENU, menuName);
			return false;
		}
		
		//check the filename
		if (fileName == null){
			log(2, Level.SEVERE, LogMessage.NULLFILENAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTSAVEMENU, menuName);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, LogMessage.NOSUCHMENU, menuName);
			log(2, Level.SEVERE, LogMessage.CANTSAVEMENU, menuName);
			return false;
		}
		
		//save the menu
		return menuService.saveMenu(plugin, menu, fileName);
	}

	/**
	 * Loads a menu from a file
	 * @param pluginName the name of the plugin which holds the menu
	 * @param fileName the name of the file of the menu
	 * @return true if successful, false if unsuccessful 
	 */
	protected boolean loadMenu(String pluginName, String menuName) {
		
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTLOADMENU, null);
			return false;
		}
		
		//check the pluginName
		if (pluginName == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGINNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTLOADMENU, menuName);
		}
		
		//get the Plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGIN, menuName);
			log(2, Level.SEVERE, LogMessage.CANTLOADMENU, menuName);
			return false;
		}
		
		//load the menu
		if (menuService.loadMenu(plugin, menuName) == null){
			
			//failed.
			return false;
		}
		
		//successful
		return true;
	}

	/**
	 * unbinds all materials and items from a menu
	 * @param menuName the name of the menu
	 * @return true if successful, false if unsuccessful
	 */
	protected boolean unbindMenu(String menuName) {
		
		//unbind the menu
		return unbindMenu(this.getName(), menuName);
	}
	
	protected boolean unbindMenu(String pluginName, String menuName) {
		
		//check the menuname
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTUNBINDMENU, null);
			return false;
		}
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTUNBINDMENU, menuName);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, LogMessage.NOSUCHMENU, menuName);
			log(2, Level.SEVERE, LogMessage.CANTUNBINDMENU, menuName);
			return false;
		}
		
		//unbind the menu
		return menuService.unbindMenu(menu);	
	}

	/**
	 * unbinds an Item from a Menu if it is binded
	 * @param itemInHand the item
	 * @return true if successful, false if unsuccessful
	 */
	protected boolean unbindItem(ItemStack itemInHand) {
		
		//check if the item is null
		if (itemInHand == null){
			log(2, Level.SEVERE, LogMessage.NULLITEMSTACK, null);
			log(2, Level.SEVERE, LogMessage.CANTUNBINDITEM, null);
			return false;
		}
		
		//unbind the item
		return menuService.unbindMenu(itemInHand);		
	}

	/**
	 * Unbinds a Material from a Menu if it is binded
	 * @param type the Material
	 * @return true if successful, false if unseuccessful
	 */
	protected boolean unbindMaterial(Material type) {
		
		//check for null
		if (type == null){
			log(2, Level.SEVERE, LogMessage.NULLMATERIAL, null);
			log(2, Level.SEVERE, LogMessage.CANTUNBINDMATERIAL, null);
			return false;
		}
		
		//unbind the material
		return menuService.unbindMenu(type);	
	}

	/**
	 * Opens a Menu for a given player
	 * @param pluginName the name of the Plugin which the Menu belongs to
	 * @param menuName the name of the menu
	 * @param playerName the name of the player to open the Menu for
	 */
	protected boolean openMenu(String pluginName, String menuName, String playerName){
		
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTOPENMENU, null);
			return false;
		}
		
		//check pluginName
		if (pluginName == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGINNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTOPENMENU, menuName);
			return false;
		}
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGIN, menuName);
			log(2, Level.SEVERE, LogMessage.CANTOPENMENU, menuName);
			return false;
		}
		
		//get the Menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, LogMessage.NOSUCHMENU, menuName);
			log(2, Level.SEVERE, LogMessage.CANTOPENMENU, menuName);
			return false;
		}
		
		//get the instance
		MenuInstance instance = menuService.createMenuInstance(menu, menuName + ": " + playerName);
		if (instance == null){
			log(2, Level.SEVERE, LogMessage.CANTCREATEMENUINSTANCE, menuName);
			log(2, Level.SEVERE, LogMessage.CANTOPENMENU, menuName);
			return false;
		}
		
		//open the menu
		return menuService.openMenuInstance(instance, playerName);
	}
	
	/**
	 * Binds a single item to a Menu
	 * @param item the ItemStack to be binded
	 * @param pluginName the name of the plugin which the Menu belongs to
	 * @param menuName the name of the menu
	 */
	protected boolean bindMenu(ItemStack item, String pluginName, String menuName){
		
		//check menuName
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, null);
			return false;
		}
		
		//check item
		if (item == null){
			log(2, Level.SEVERE, LogMessage.NULLITEMSTACK, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, menuName);
			return false;
		}
		
		//check pluginName
		if (pluginName == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGINNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, menuName);
			return false;
		}
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGIN, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, menuName);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, LogMessage.NOSUCHMENU, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, menuName);
			return false;
		}
		
		//bind the item
		return menuService.bindMenu(item, menu);
	}
	
	/**
	 * Binds the Material to a Menu
	 * @param material the Material to be binded
	 * @param pluginName the name of the Plugin that the Menu belongs to
	 * @param menuName the name of the Menu
	 */
	protected boolean bindMenu(Material material, String pluginName, String menuName){
		
		//check menuName
		if (menuName == null){
			log(2, Level.SEVERE, LogMessage.NULLMENUNAME, null);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUITEM, null);
			return false;
		}
		
		//check material
		if (material == null){
			log(2, Level.SEVERE, LogMessage.NULLMATERIAL, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//check pluginName
		if (pluginName == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGINNAME, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, LogMessage.NULLPLUGIN, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, LogMessage.NOSUCHMENU, menuName);
			log(2, Level.SEVERE, LogMessage.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//bind the Material
		return menuService.bindMenu(material, menu);
	}
	
	/**
	 * Shows the CommandSender a help menu
	 * @param sender
	 */
	protected void showHelp(CommandSender sender) {
		sender.sendMessage("Good luck!");
	}
	
	/**
	 * Catch when a player executes a command if a Menu should be opened
	 * @param event
	 */
	@EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
                
        if(menuService.checkCommand(command.substring(1), player)){
            event.setCancelled(true);
        }
    }
	
}
