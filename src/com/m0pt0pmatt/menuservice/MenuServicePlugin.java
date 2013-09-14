package com.m0pt0pmatt.menuservice;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.commands.CommandHandler;

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
	public static String configFileName = "config.yml";
	
	/**
	 * The config file which stores all of the bind to menus
	 */
	public static String bindsFileName = "binds.yml";
	
	/**
	 * The config file that holds all the menu binds
	 */
	public static YamlConfiguration binds;
	
	/**
	 * The config file for the plugin
	 */
	public static YamlConfiguration config;
	
	/**
	 * the verbosity level of the plugin. The higher the level, the more messages will be logged to the terminal.
	 */
	public int verbose = 3;
	
	/**
	 * Executed when the plugin is enabled.
	 * Sets up internal attributes, loads configuration
	 */
	public void onEnable(){
				
		Logger.setPlugin(this);
		
		//setup the MenuService Provider
		menuService = new MenuServiceProvider(this);
		commandHandler = new CommandHandler(menuService, this);	
		
		Logger.log(3, Level.INFO, "MenuService initialized");
		
		//register the MenuServiceProvider as the provider for the MenuService
		Bukkit.getServicesManager().register(MenuService.class, menuService, this, ServicePriority.Normal);
		Logger.log(3, Level.INFO, "MenuService registered for the server");
		
		//load the config file
		loadConfig();
		Logger.log(1, Level.INFO, "Loaded " + configFileName);
		
		//load all menus in the MenuService folder
		menuService.loadMenus();
		
		menuService.loadBinds();
		
		//register the plugin so it can listen to open menus
		Bukkit.getPluginManager().registerEvents(this, this);	
		
	}

	/**
	 * Ran when the plugin is disabled.
	 * Saves menus to file.
	 * Closes all menuInstances
	 */
	@Override
	public void onDisable(){
		
		//save all the menus to file
		Logger.log(1, Level.INFO, "Saving all menus to file");
		menuService.saveMenus();
		
		//close all menuinstances
		Logger.log(1, Level.INFO, "Closing all menus");
		menuService.closeMenus();
		
		//save the config file
		try {
			config.save(new File(this.getDataFolder(), "config.yml"));
			Logger.log(1, Level.INFO, "Saved " + configFileName);
		} catch (IOException e) {
			Logger.log(1, Level.SEVERE, "Unable to save " + configFileName);
		}
		
	}
	
	/**
	 * Executed when a command is ran.
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return commandHandler.onCommand(sender, cmd, label, args);
	}
	
	/**
	 * Loads the config.yml file and all of its settings
	 */
	private void loadConfig() {
		
		//create data folder if needed
		if (!this.getDataFolder().exists()){
			Logger.log(2, Level.INFO, "Creating Data Folder");
			this.getDataFolder().mkdir();
		}
		
		//create configuration file if needed
		File configFile = new File(this.getDataFolder(), configFileName);
		if (!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				Logger.log(1, Level.SEVERE, "Unable to create config file!");
			}
		}
		
		//load the configuration file
		config = YamlConfiguration.loadConfiguration(configFile);
		if (config == null){
			Logger.log(1, Level.SEVERE, "Unable to load config file!");
		}
		
		//check for verbose level
		if (config.contains("verbose")){
			verbose = config.getInt("verbose");
			Logger.log(2, Level.INFO, "Loaded verbosity level. Level is now " + verbose);
		}
		
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
