package com.m0pt0pmatt.menuservice.commands;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.Keyword;
import com.m0pt0pmatt.menuservice.Logger;
import com.m0pt0pmatt.menuservice.MenuServicePlugin;
import com.m0pt0pmatt.menuservice.Message;
import com.m0pt0pmatt.menuservice.MessageFormat;
import com.m0pt0pmatt.menuservice.Perm;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.menumanager.MenuManager;

/**
 * Handles Commands for MenuServicePlugin
 * This handles commands that are MenuServicePlugin commands. Dynamic Menu commands are handles by MenuServicePlugin
 * @author mbroomfield
 *
 */
public class CommandHandler {
	
	//The MenuService
	private MenuService menuService;
	
	//commands mapped from their name to the set of possible actual commands
	private Map<Keyword, Set<MyCommand>> commands;
	
	protected static MenuManager menuManager;
	
	/**
	 * Creates the CommandHandler
	 * @param plugin the MenuServicePlugin this CommandHandler belongs to
	 */
	public CommandHandler(MenuService menuService, MenuServicePlugin plugin){
		this.menuService = menuService;
		commands = new HashMap<Keyword, Set<MyCommand>>();
		try {
			this.addCommands();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		menuManager = new MenuManager(menuService, plugin);
		
	}
	
	private Class<?>[] getFormalParameters(List<Keyword> arguments){
		
		List<Class<?>> args = new LinkedList<Class<?>>();
		
		//add the sender argument
		args.add(String.class);
		
		for (Keyword o: arguments){
			
			if (o.equals(Keyword.PLACEHOLDER)){
				args.add(o.getClass());
			}		
		}
		
		Class<?>[] type = new Class<?>[1];
		
		return args.toArray(type);
	}
	
	private void addCommands() throws SecurityException, NoSuchMethodException{
		
		List<Keyword> arguments;
		MyCommand command;
		Method method;
		
		//add the sets
		commands.put(Keyword.HELP, new HashSet<MyCommand>());
		commands.put(Keyword.LIST, new HashSet<MyCommand>());
		commands.put(Keyword.OPEN, new HashSet<MyCommand>());
		commands.put(Keyword.CLOSE, new HashSet<MyCommand>());
		commands.put(Keyword.LOAD, new HashSet<MyCommand>());
		commands.put(Keyword.SAVE, new HashSet<MyCommand>());
		commands.put(Keyword.RELOAD, new HashSet<MyCommand>());
		commands.put(Keyword.UNLOAD, new HashSet<MyCommand>());
		commands.put(Keyword.BIND, new HashSet<MyCommand>());
		commands.put(Keyword.UNBIND, new HashSet<MyCommand>());
		
		
		//add commands to the sets
		
		//help
		arguments = new LinkedList<Keyword>();
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.HELP, arguments, Perm.HELP, method);
		commands.get(Keyword.HELP).add(command);
		
		//list menus
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENUS);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.LIST, arguments, Perm.LISTMENUS, method);
		commands.get(Keyword.HELP).add(command);
		
		//list instances [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.INSTANCES);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.LIST, arguments, Perm.LISTINSTANCES, method);
		commands.get(Keyword.HELP).add(command);
		
		//open menu [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.OPEN, arguments, Perm.OPENMENUSELF, method);
		commands.get(Keyword.HELP).add(command);
		
		//open menu [menu] (player)
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.OPEN, arguments, Perm.OPENMENUOTHER, method);
		commands.get(Keyword.HELP).add(command);
		
		//open menu [menu] instance [instance]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.INSTANCE);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.OPEN, arguments, Perm.OPENINSTANCESELF, method);
		commands.get(Keyword.HELP).add(command);
		
		//open menu [menu] instance [instance] (player)
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.INSTANCE);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.OPEN, arguments, Perm.OPENINSTANCEOTHER, method);
		commands.get(Keyword.HELP).add(command);
		
		//open player [player]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLAYER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.OPEN, arguments, Perm.OPENPLAYER, method);
		commands.get(Keyword.HELP).add(command);
		
		//close menu all
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.ALL);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEMENU, method);
		commands.get(Keyword.HELP).add(command);
		
		//close menu [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEMENU, method);
		commands.get(Keyword.HELP).add(command);
		
		//close menu [menu] all
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.ALL);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEMENU, method);
		commands.get(Keyword.HELP).add(command);
		
		//close menu [menu] (player)
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEMENU, method);
		commands.get(Keyword.HELP).add(command);
		
		//close instance [menu] [instance]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.INSTANCE);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEINSTANCE, method);
		commands.get(Keyword.HELP).add(command);
		
		//close instance [menu] [instance] [player]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.MENU);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEINSTANCE, method);
		commands.get(Keyword.HELP).add(command);
		
		//close player [player]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLAYER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.CLOSE, arguments, Perm.CLOSEPLAYER, method);
		commands.get(Keyword.HELP).add(command);
		
		//load [filename]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.LOAD, arguments, Perm.LOADFROMMENUSERVICE, method);
		commands.get(Keyword.HELP).add(command);
		
		//load [filename] [plugin]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.LOAD, arguments, Perm.LOADFROMOTHERPLUGIN, method);
		commands.get(Keyword.HELP).add(command);
		
		//save [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.SAVE, arguments, Perm.SAVE, method);
		commands.get(Keyword.HELP).add(command);
		
		//save [menu] filename [filename]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		arguments.add(Keyword.FILENAME);
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.SAVE, arguments, Perm.SAVEOTHERFILE, method);
		commands.get(Keyword.HELP).add(command);
		
		//reload [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.RELOAD, arguments, Perm.RELOAD, method);
		commands.get(Keyword.HELP).add(command);
		
		//unload [menu]
		arguments = new LinkedList<Keyword>();
		arguments.add(Keyword.PLACEHOLDER);
		method = menuService.getClass().getMethod("help", getFormalParameters(arguments));
		command = new MyCommand(Keyword.UNLOAD, arguments, Perm.UNLOAD, method);
		commands.get(Keyword.HELP).add(command);
		
	}
	
	/**
	 * Executed when a command is run.
	 * Handles all commands for MenuServicePlugin
	 * @param sender the CommandSender
	 * @param cmd the Command
	 * @param label the used Alias for the Command
	 * @param args the Arguments
	 * @return true if a successful command is ran, false otherwise
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if (Keyword.is(Keyword.MENUMANAGER, cmd.getName())){
			menuManager.showMainMenu((Player)sender);
			return true;
		}
		
		//make sure the command is for MenuService
		if (!Keyword.is(Keyword.MENUSERVICE, cmd.getName())){
			return false;
		}
		
		//if there are no args, print out the help menu
		if (args.length == 0){
			return help(sender.getName());
		}
		
		//see if the first argument is a valid keyword
		if (!Keyword.is(args[0])){
			return false;
		}
		Keyword keyword = Keyword.getKeyword(args[0]);
		
		if (!commands.containsKey(keyword)){
			return false;
		}
		
		Set<MyCommand> possibleCommands = commands.get(keyword);
		
		//check if the keyword is valid
		for (MyCommand command: possibleCommands){
								
			//check arguments
			if (command.matchesArguments(args)){

				if (command.getMethod() == null){
											
					//method was not found
					Message.sendMessage(sender, MessageFormat.ERROR, Message.INTERNALMETHODNOTFOUND);
					return false;
				}
				
				//check permission
				if (!Perm.has(sender, command.getPermission())){
					
					Message.sendMessage(sender, MessageFormat.NOPERMISSION, Message.NOPERMISSION, command.getPermission().name());
					return false;
				}
				
				//attempt to execute method
				try {
					Boolean ret = (Boolean) command.getMethod().invoke(this, (Object[]) command.getActualArguments(args));
					
					//check if successful
					if (!ret){
						Message.sendMessage(sender, MessageFormat.ERROR, Message.ERRORINVOKINGMETHOD, command.getName());
					}
					return ret;
				} catch (Exception e) {
					
					//an error occurred when invoking the method
					Message.sendMessage(sender, MessageFormat.ERROR, Message.ERRORINVOKINGMETHOD, command.getName());
					e.printStackTrace();
					return false;
				}		
			}
				
		}
		
		//incorrect usage
		Message.sendMessage(sender, MessageFormat.INCORRECTUSE, Message.INCORRECTUSE);
		return help(sender.getName());
	}
	
	/**
	 * Handles the "help" command
	 * @param sender the CommandSender
	 * @param cmd the Command
	 * @param label the used Alias for the Command
	 * @param args the Arguments
	 * @return true if a successful command is ran, false otherwise
	 */
	public boolean help(String sender){
		
		//help
		
		//list menus
		//list instances [menu]
		
		//open menu [menu]
		//open menu [menu] (player)
		//open menu [menu] instance [instance]
		//open menu [menu] instance [instance] (player)
		//open player [player]
		
		//close menu all
		//close menu [menu]
		//close menu [menu] all
		//close menu [menu] (player)
		//close instance [menu] [instance]
		//close instance [menu] [instance] [player]
		//close player [player]
		
		//load [filename]
		//load [filename] [plugin]
		
		//save [menu]
		//save [menu] filename [filename]
		
		//reload [menu]
		
		//unload [menu]
		
		//bind menu [menu] material
		//bind menu [menu] material [material]
		//bind menu [menu] entity
		//bind menu [menu] entity [entity]
		//bind instance [instance] material
		//bind instance [instance] material [material]
		//bind instance [instance] entity
		//bind instance [instance] entity [entity]
		
		//unbind menu [menu] all
		//unbind menu [menu] material all
		//unbind menu [menu] material [material]
		//unbind menu [menu] entity all
		//unbind menu [menu] entity [entity]
		//unbind instance [instance] all
		//unbind instance [instance] material all
		//unbind instance [instance] material [material]
		//unbind instance [instance] entity all
		//unbind instance [instance] entity [entity]
		
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "MenuService commands:");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice help");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice list menu");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice list instance [menu]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice open menu [menu] (player)");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice open instance [instance] (player)");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice open player [player]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice close menu all");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice close menu [menu]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice close menu [menu] all");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice close instance [instance]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice close player [player]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice load [filename]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice load [filename] -p [plugin]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice save [menu]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice save [menu] -p [plugin]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice reload [menu]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice reload all");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice unload [menu]");
		Message.sendMessage(sender, MessageFormat.HELPMESSAGE, "/menuservice unload all");

		return true;
	}

	/**
	 * Lists menus for the given sender
	 * @param sender
	 * @return
	 */
	public boolean listMenus(String sender) {
		
		if (sender == null){
			return false;
		}
		
		for (Menu menu: menuService.getMenus()){
			Message.sendMessage(sender, MessageFormat.OUTPUT, menu.getName() + ": " + menu.getPlugin());
		}
		return true;
	}
	
	
	/**
	 * Lists the instances of the given Menu for the given sender
	 * @param sender
	 * @param menuName
	 * @return
	 */
	public boolean listInstances(String sender, String menuName){

		
		//get the menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Message.sendMessage(sender, MessageFormat.ERROR, Message.NOSUCHMENU);
			return false;
		}
		
		//get the instances
		List<MenuInstance> instances = menuService.getMenuInstances(menu);
		if (instances == null){
			return false;
		}
		
		//output the instances
		for (MenuInstance instance: instances){
			Message.sendMessage(sender, MessageFormat.OUTPUT, instance.getName());
		}
		
		return true;
	}
	
	/**
	 * Opens the given Menu for the given sender
	 * Creates a new MenuInstance for the sender
	 * @param sender
	 * @param menuName
	 * @return
	 */
	public boolean openMenu(String sender, String menuName){
		return openMenu(sender, menuName, sender);
	}
	
	/**
	 * Opens a Menu for a given player
	 * @param pluginName the name of the Plugin which the Menu belongs to
	 * @param menuName the name of the menu
	 * @param playerName the name of the player to open the Menu for
	 */
	public boolean openMenu(String sender, String menuName, String playerName){
		
		//get the Menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			return false;
		}
		
		//get the instance
		MenuInstance instance;
		if (menuService.hasMenuInstance(menu, menuService.getDefaultMenuInstanceName(menu, playerName))){
			instance = menuService.getMenuInstance(menu, menuService.getDefaultMenuInstanceName(menu, playerName));
		} else{
			instance = menuService.createMenuInstance(menu, menuService.getDefaultMenuInstanceName(menu, playerName));
			if (instance == null){
				Logger.log(2, Level.SEVERE, Message.CANTCREATEMENUINSTANCE, menuName);
				return false;
			}
		}
		
		//open the menu
		return menuService.openMenuInstance(instance, playerName);
	}
	
	public boolean openInstance(String sender, String menuName, String instanceName){
		return openInstance(sender, menuName, instanceName, sender);
	}
	
	public boolean openInstance(String sender, String menuName, String instanceName, String playerName){
		
		//get the Menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			return false;
		}
		
		if (!menuService.hasMenuInstance(menu, instanceName)){
			return false;
		}
		
		return menuService.openMenuInstance(menuService.getMenuInstance(menu, instanceName), playerName);
	}
	
	public boolean openPlayer(String sender, String playerName){
		return false;
	}
	
	public boolean closeAllMenus(String sender){
		return menuService.closeMenus();
	}
	
	public boolean closeMenu(String sender, String menuName) {
		return closeMenu(sender, menuName, sender);
	}

	public boolean closeMenuAll(String sender, String menuName) {
		
		return false;
	}
	
	public boolean closeMenu(String sender, String menuName, String playerName) {
		//close the menu
		menuService.closeMenuInstance(playerName);
		return true;
	}
	
	public boolean closeInstance(String sender, String menuName, String instanceName){
		return false;
	}
	
	public boolean closeInstance(String sender, String menuName, String instanceName, String playerName){
		return false;
	}
	
	/**
	 * Closes a menu for a given player
	 * @param playerName the name of the player
	 */
	public boolean closePlayer(String sender, String playerName) {
		
		//close the menu
		menuService.closeMenuInstance(playerName);
		return true;
	}
	
	public boolean loadMenu(String sender, String menuName) {
		return false;
	}
	
	/**
	 * Loads a menu from a file
	 * @param pluginName the name of the plugin which holds the menu
	 * @param fileName the name of the file of the menu
	 * @return true if successful, false if unsuccessful 
	 */
	public boolean loadMenu(String sender, String pluginName, String menuName) {
		
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, null);
			return false;
		}
		
		//check the pluginName
		if (pluginName == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGINNAME, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, menuName);
		}
		
		//get the Plugin
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLUGIN, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTLOADMENU, menuName);
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
	 * Saves a Menu to file
	 * @param menuName the name of the menu
	 * @param pluginName the name of the plugin which holds the menu
	 * @param fileName the name to save the menu to
	 * @return true if successful, false if unsuccessful 
	 */
	public boolean saveMenu(String sender, String menuName) {
		
		//check menuName
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTSAVEMENU, null);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTSAVEMENU, menuName);
			return false;
		}
		
		//save the menu
		return menuService.saveMenu(menu);
	}

	public boolean saveMenu(String sender, String menuName, String fileName){
		return false;
	}
	
	public boolean reloadMenu(String sender, String menuName) {

		return false;
	}
	
	public boolean unloadMenu(String sender, String menuName){
		
		return false;
	}
	
	/**
	 * Binds the Material to a Menu
	 * @param material the Material to be binded
	 * @param pluginName the name of the Plugin that the Menu belongs to
	 * @param menuName the name of the Menu
	 */
	public boolean bindMenu(Material material, String menuName){
		
		//check menuName
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUITEM, null);
			return false;
		}
		
		//check material
		if (material == null){
			Logger.log(2, Level.SEVERE, Message.NULLMATERIAL, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTBINDMENUMATERIAL, menuName);
			return false;
		}
		
		//bind the Material
		return menuService.bindMenu(material, menu);
	}
	
	public boolean unbindMenu(String menuName) {
		
		//check the menuname
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDMENU, null);
			return false;
		}
		
		//get the menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDMENU, menuName);
			return false;
		}
		
		//unbind the menu
		return menuService.unbindMenu(menu);	
	}

	/**
	 * Unbinds a Material from a Menu if it is binded
	 * @param type the Material
	 * @return true if successful, false if unseuccessful
	 */
	public boolean unbindMaterial(Material type) {
		
		//check for null
		if (type == null){
			Logger.log(2, Level.SEVERE, Message.NULLMATERIAL, null);
			Logger.log(2, Level.SEVERE, Message.CANTUNBINDMATERIAL, null);
			return false;
		}
		
		//unbind the material
		return menuService.unbindMenu(type);	
	}

	
	
	
	
	
	
	/**
	 * This method can be called for any command which is not yet implemented.
	 * @param sender the CommandSender
	 * @param cmd the Command
	 * @param label the used Alias for the Command
	 * @param args the Arguments
	 * @param permission The permission for the Command
	 * @return true unless the player does not have permission
	 */
	public boolean notImplemented(CommandSender sender, Command cmd, String label, String[] args, String permission){
		//check permission
		if (!sender.hasPermission(permission)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		sender.sendMessage("The command " + cmd.getName() + " " + args[0] + " is not implemented yet. Sorry!");
		return true;
	}
	
}
