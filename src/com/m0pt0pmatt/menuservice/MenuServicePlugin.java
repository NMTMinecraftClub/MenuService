package com.m0pt0pmatt.menuservice;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * MenuService is a plugin that allows other plugins to implement abstract menu
 * systems.
 * @author mbroomfield
 *
 */
public class MenuServicePlugin extends JavaPlugin{
		
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
	
	public static Plugin plugin;
	
	/**
	 * the verbosity level of the plugin. The higher the level, the more messages will be logged to the terminal.
	 */
	public int verbose = 3;
	
	public void onLoad(){
		
		plugin = this;
		
	}
	
	/**
	 * Executed when the plugin is enabled.
	 * Sets up internal attributes, loads configuration
	 */
	public void onEnable(){
		
		
		//load the config file
		loadConfig();
		
	}

	/**
	 * Ran when the plugin is disabled.
	 * Saves menus to file.
	 * Closes all menuInstances
	 */
	@Override
	public void onDisable(){
		
		//close all menuinstances
		//Formatting.logger.log(1, Level.INFO, "Closing all menus");		
		
		//save the config file
		try {
			config.save(new File(this.getDataFolder(), "config.yml"));
			//Formatting.logger.log(1, Level.INFO, "Saved " + configFileName);
		} catch (IOException e) {
			//Formatting.logger.log(1, Level.SEVERE, "Unable to save " + configFileName);
		}
		
	}
	
	/**
	 * Loads the config.yml file and all of its settings
	 */
	private void loadConfig() {
		
		//create data folder if needed
		if (!this.getDataFolder().exists()){
			//Formatting.logger.log(2, Level.INFO, "Creating Data Folder");
			this.getDataFolder().mkdir();
		}
		
		//create configuration file if needed
		File configFile = new File(this.getDataFolder(), configFileName);
		if (!configFile.exists()){
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				//Formatting.logger.log(1, Level.SEVERE, "Unable to create config file!");
			}
		}
		
		//load the configuration file
		config = YamlConfiguration.loadConfiguration(configFile);
		if (config == null){
			//Formatting.logger.log(1, Level.SEVERE, "Unable to load config file!");
		}
		
		//check for verbose level
		if (config.contains("verbose")){
			verbose = config.getInt("verbose");
			//Formatting.logger.log(2, Level.INFO, "Loaded verbosity level. Level is now " + verbose);
		}
		
	}
	
}
