package com.m0pt0pmatt.menuservice;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	
	private static String configFileName = "config.yml";
	
	/**
	 * The config file for the plugin
	 */
	private YamlConfiguration config;
	
	/**
	 * the verbosity level of the plugin. The higher the level, the more messages will be logged to the terminal.
	 */
	public int verbose = 2;
	
	/**
	 * Executed when the plugin is enabled.
	 * Sets up internal attributes, loads configuration
	 */
	public void onEnable(){
				
		//setup the MenuService Provider
		menuService = new MenuServiceProvider(this);
		log(3, Level.INFO, "MenuService initialized");
		
		//register the MenuServiceProvider as the provider for the MenuService
		Bukkit.getServicesManager().register(MenuService.class, menuService, this, ServicePriority.Normal);
		log(3, Level.INFO, "MenuService registered for the server");
		
		//load the config file
		loadConfig();
		log(1, Level.INFO, "Loaded " + configFileName);
		
		//load all menus in the MenuService folder
		loadMenus();
		
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
	 * Executed when a command is ran.
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		//make sure the command is for MenuService
		if (!cmd.getName().equalsIgnoreCase(this.getName())){
			return false;
		}
		
		//if there are no args, print out the help menu
		if (args.length == 0){
			showHelp(sender);
			return true;
		}
		
		
		//check if the command is an "open" command
		if (args[0].equalsIgnoreCase("open")){
			
			//check permission
			if (!sender.hasPermission("menuservice.open")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//check if player and menuName were specified
			if (args.length == 3){
				if(openMenu(this.getName(), args[1], args[2])){
					sender.sendMessage("Menu opened");
				} else{
					sender.sendMessage("Could not open menu");
				}
				return true;
			}
			
			//check if player, plugin, and menuName were specified
			if (args.length == 4){
				if(openMenu(args[1], args[2], args[3])){
					sender.sendMessage("Menu opened");
				} else{
					sender.sendMessage("Could not open menu");
				}
				return true;
			}
			
			//incorrect use of open
			sender.sendMessage("Incorrect use of /menuservice open");
			return false;
		}
		
		//check if the command is an "close" command
		if (args[0].equalsIgnoreCase("close")){
			
			//check permission
			if (!sender.hasPermission("menuservice.close")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//check if nothing was specified
			if (args.length == 1){
				closeMenu(sender.getName());
				return true;
			}
			
			//check if player was specified
			if (args.length == 2){
				closeMenu(args[1]);
				return true;
			}
			
			//incorrect use of open
			sender.sendMessage("Incorrect use of /menuservice close");
			return false;
		}
		
		//check if the command is a "reload" command
		else if (args[0].equalsIgnoreCase("reload")){
			
			//check permission
			if (!sender.hasPermission("menuservice.reload")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			sender.sendMessage("This is not implemented yet. Sorry!");
			return true;
		}
		
		//check if the command is a "load" command
		else if (args[0].equalsIgnoreCase("load")){
			
			//check permission
			if (!sender.hasPermission("menuservice.load")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//load a MenuService menu
			if (args.length == 2){
				if(loadMenu(this.getName(), args[1])){
					sender.sendMessage("Menu loaded");
				} else{
					sender.sendMessage("Menu not loaded");
				}
				return true;
			}
			
			//load a menu from another plugin
			else if (args.length == 3){
				if(loadMenu(args[1], args[2])){
					sender.sendMessage("Menu loaded");
				} else{
					sender.sendMessage("Menu not loaded");
				}
				return true;
			}
			
			//incorrect use of load
			sender.sendMessage("Incorrect use of /menuservice load");
			return false;
		}
		
		//check if the command is a "save" command
		else if (args[0].equalsIgnoreCase("save")){
			
			//check permission
			if (!sender.hasPermission("menuservice.save")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//save a MenuService menu to MenuService
			if (args.length == 3){
				if(saveMenu(args[1], this.getName(), args[2])){
					sender.sendMessage("Menu saved");
				} else{
					sender.sendMessage("Menu not saved");
				}
				return true;
			}
			
			//save a menu to another plugin
			else if (args.length == 4){
				if(saveMenu(args[1], args[2], args[3])){
					sender.sendMessage("Menu saved");
				} else{
					sender.sendMessage("Menu not saved");
				}
				return true;
			}
			
			//incorrect use of save
			sender.sendMessage("Incorrect use of /menuservice save");
			return false;
		}
		
		//check if the command is an "edit" command
		else if (args[0].equalsIgnoreCase("edit")){
			
			//check permission
			if (!sender.hasPermission("menuservice.edit")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			sender.sendMessage("This is not implemented yet. Sorry!");
			return true;
		}
		
		//check if the command is a "delete" command
		else if (args[0].equalsIgnoreCase("delete")){
			
			//check permission
			if (!sender.hasPermission("menuservice.delete")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			sender.sendMessage("This is not implemented yet. Sorry!");
			return true;
		}
		
		//check if the command is a "bind" command
		else if (args[0].equalsIgnoreCase("bind")){
			
			//check permission
			if (!sender.hasPermission("menuservice.bind")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//make sure there are enough arguments
			if (args.length < 3){
				sender.sendMessage("Not enough arguments to /menuservice bind");
				return false;
			}
			
			//check if the bind will bind a single item to a menu
			if (args[1].equalsIgnoreCase("item")){
				
				//check if the player specified just the menu
				if (args.length == 3){
					
					//make sure a Player is executing the command
					if (!(sender instanceof Player)){
						sender.sendMessage("¤cYou must be a player to execute this command");
						return true;
					}
					
					//bind the item
					if(bindMenu(((Player)sender).getItemInHand(), this.getName(), args[2])){
						sender.sendMessage("Item Binded");
					} else{
						sender.sendMessage("Item not Binded");
					}
					return true;
				}
				
				//check if the player specified the plugin and the menu
				else if (args.length == 4){
					
					//make sure a Player is executing the command
					if (!(sender instanceof Player)){
						sender.sendMessage("¤cYou must be a player to execute this command");
						return true;
					}
					//bind the item
					if(bindMenu(((Player)sender).getItemInHand(), args[3], args[2])){
						sender.sendMessage("Item Binded");
					} else{
						sender.sendMessage("Item not Binded");
					}
					return true;
				}
				
				//incorrect use of bind item
				sender.sendMessage("Incorrect use of /menuservice bind item");
				return false;
			}
			
			
			//check if the bind will bind the material of an item to a menu
			else if (args[1].equalsIgnoreCase("material")){
				
				//check if the player specified just the menu
				if (args.length == 3){
					
					//make sure a Player is executing the command
					if (!(sender instanceof Player)){
						sender.sendMessage("¤cYou must be a player to execute this command");
						return true;
					}

					//bind the material
					if (bindMenu(((Player)sender).getItemInHand().getType(), this.getName(), args[2])){
						sender.sendMessage("Material Binded");
					} else{
						sender.sendMessage("Material not Binded");
					}
					return true;
				}
				
				//check if the player specified the plugin and the menu
				else if (args.length == 4){
					
					//make sure a Player is executing the command
					if (!(sender instanceof Player)){
						sender.sendMessage("¤cYou must be a player to execute this command");
						return true;
					}
					
					//bind the material
					if(bindMenu(((Player)sender).getItemInHand().getType(), args[3], args[2])){
						sender.sendMessage("Material Binded");
					} else{
						sender.sendMessage("Material not Binded");
					}
					return true;
				}
				
				//incorrect use of bind material
				sender.sendMessage("Incorrect use of /menuservice bind material");
				return false;
			}
			
			//incorrect use of bind
			sender.sendMessage("Incorrect use of /menuservice bind");
			return false;
			
		}
		
		//check if the command is a "unbind" command
		else if (args[0].equalsIgnoreCase("unbind")){
			
			//check permission
			if (!sender.hasPermission("menuservice.unbind")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			//check if material was specified
			if (args.length == 2 && args[1].equalsIgnoreCase("material")){
				
				//make sure a Player is executing the command
				if (!(sender instanceof Player)){
					sender.sendMessage("¤cYou must be a player to execute this command");
					return true;
				}
				
				if(unbindMaterial(((Player)sender).getItemInHand().getType())){
					sender.sendMessage("Material unbinded");
				} else{
					sender.sendMessage("Material not unbinded");
				}
				return true;
			}
			
			//check if material was specified
			else if (args.length == 2 && args[1].equalsIgnoreCase("item")){
				
				//make sure a Player is executing the command
				if (!(sender instanceof Player)){
					sender.sendMessage("¤cYou must be a player to execute this command");
					return true;
				}
				
				if(unbindItem(((Player)sender).getItemInHand())){
					sender.sendMessage("Item unbinded");
				} else{
					sender.sendMessage("Item not unbinded");
				}
				return true;
			}
			
			//check if material was specified
			if (args.length == 3 && args[1].equalsIgnoreCase("menu")){
				
				if(unbindMenu(args[2])){
					sender.sendMessage("Menu unbinded");
				} else{
					sender.sendMessage("Menu not unbinded");
				}
				return true;
			}
			
			//check if material was specified
			if (args.length == 4 && args[1].equalsIgnoreCase("menu")){
				
				if(unbindMenu(args[2], args[3])){
					sender.sendMessage("Menu unbinded");
				} else{
					sender.sendMessage("Menu not unbinded");
				}
				return true;
			}
			
			//incorrect use
			sender.sendMessage("Incorrect use of /menuservice unbind");
			return false;
		}
		
		//check if the command is a "help" command
		else if (args[0].equalsIgnoreCase("help")){
			
			//check permission
			if (!sender.hasPermission("menuservice.help")){
				sender.sendMessage("¤cYou do not have permission to do that");
				return false;
			}
			
			sender.sendMessage("This is not implemented yet. Sorry!");
			return true;
		}
		
		sender.sendMessage("Incorrect use of /menuservice");
		return false;
	}
	
	
	/**
	 * Closes a menu for a given player
	 * @param playerName the name of the player
	 */
	private void closeMenu(String playerName) {
		
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
	private boolean saveMenu(String menuName, String pluginName, String fileName) {
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not save Menu.");
			return false;
		}
		
		//check the filename
		if (fileName == null){
			log(2, Level.SEVERE, "Filename was null.");
			log(2, Level.SEVERE, "Could not save Menu.");
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, "Menu was null.");
			log(2, Level.SEVERE, "Could not save Menu.");
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
	private boolean loadMenu(String pluginName, String menuName) {
		
		//get the Plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not load Menu.");
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
	private boolean unbindMenu(String menuName) {
		
		//unbind the menu
		return unbindMenu(this.getName(), menuName);
	}
	
	private boolean unbindMenu(String pluginName, String menuName) {
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not unbind Menu.");
			return false;
		}
		
		//check the menuname
		if (menuName == null){
			log(2, Level.SEVERE, "menuName was null");
			log(2, Level.SEVERE, "Could not unbind menu");
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, "Menu was null.");
			log(2, Level.SEVERE, "Could not unbind Menu.");
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
	private boolean unbindItem(ItemStack itemInHand) {
		
		//check if the item is null
		if (itemInHand == null){
			log(2, Level.SEVERE, "item was null.");
			log(2, Level.SEVERE, "Could not unbind Menus from Item.");
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
	private boolean unbindMaterial(Material type) {
		
		//check for null
		if (type == null){
			log(2, Level.SEVERE, "Material was null.");
			log(2, Level.SEVERE, "Could not unbind Menus from Material.");
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
	private boolean openMenu(String pluginName, String menuName, String playerName){
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not open Menu.");
			return false;
		}
		
		//get the Menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, "Menu " + menuName + " is not loaded in MenuService.");
			log(2, Level.SEVERE, "Could not open Menu.");
			return false;
		}
		
		//get the instance
		MenuInstance instance = menuService.createMenuInstance(menu, menuName + ": " + playerName);
		if (instance == null){
			log(2, Level.SEVERE, "a MenuInstance could not be created for the menu " + menuName);
			log(2, Level.SEVERE, "Could not open Menu.");
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
	private boolean bindMenu(ItemStack item, String pluginName, String menuName){
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not bind Menu.");
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, "Menu was null.");
			log(2, Level.SEVERE, "Could not bind Menu.");
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
	private boolean bindMenu(Material material, String pluginName, String menuName){
		
		//get the plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not bind Menu.");
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(plugin, menuName);
		if (menu == null){
			log(2, Level.SEVERE, "Plugin was null.");
			log(2, Level.SEVERE, "Could not bind Menu.");
			return false;
		}
		
		//bind the Material
		return menuService.bindMenu(material, menu);
	}
	
	/**
	 * Shows the CommandSender a help menu
	 * @param sender
	 */
	private void showHelp(CommandSender sender) {
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
