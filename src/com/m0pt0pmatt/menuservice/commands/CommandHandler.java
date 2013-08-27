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
import org.bukkit.inventory.ItemStack;
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
	
	private Set<MyCommand> commands;
	private Map<String, Method> methods;
	
	protected static MenuManager menuManager;
	
	
	/**
	 * Creates the CommandHandler
	 * @param plugin the MenuServicePlugin this CommandHandler belongs to
	 */
	public CommandHandler(MenuService menuService, MenuServicePlugin plugin){
		this.menuService = menuService;
		commands = new HashSet<MyCommand>();
		methods = new HashMap<String, Method>();
		try {
			this.addCommands();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		menuManager = new MenuManager(menuService, plugin);
		
	}
	
	private Class<?>[] getFormalParameters(List<String> arguments){
		
		List<Class<?>> args = new LinkedList<Class<?>>();
		
		//add the sender argument
		args.add(String.class);
		
		for (String o: arguments){
			
			if (Keyword.is(Keyword.PLACEHOLDER, o)){
				args.add(o.getClass());
			}		
		}
		
		Class<?>[] type = new Class<?>[1];
		
		return args.toArray(type);
	}
	
	private void addCommands() throws SecurityException, NoSuchMethodException{
		
		List<String> arguments;
		MyCommand command;
		
		//help
		arguments = new LinkedList<String>();
		command = new MyCommand(Keyword.HELP.getKeyword(), arguments, Perm.HELP);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
		
		//list menus
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENUS.getKeyword());
		command = new MyCommand(Keyword.LIST.getKeyword(), arguments, Perm.LISTMENUS);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("listMenus", getFormalParameters(arguments)));
		
		//list instances [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.INSTANCES.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.LIST.getKeyword(), arguments, Perm.LISTINSTANCES);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("listInstances", getFormalParameters(arguments)));
		
		//open menu [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUSELF);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("openMenu", getFormalParameters(arguments)));
		
		//open menu [menu] (player)
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("openMenu", getFormalParameters(arguments)));
		System.out.println(command.toString());
		
		//open menu [menu] instance [instance]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.INSTANCE.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENINSTANCESELF);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("openInstance", getFormalParameters(arguments)));
		
		//open menu [menu] instance [instance] (player)
		arguments = new LinkedList<String>();
		arguments.add(Keyword.INSTANCE.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENINSTANCEOTHER);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("openInstance", getFormalParameters(arguments)));
		
		//open player [player]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLAYER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENPLAYER);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("openPlayer", getFormalParameters(arguments)));
		
		//close menu all
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.ALL.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEMENU);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeAllMenus", getFormalParameters(arguments)));
		
		//close menu [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEMENU);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeMenu", getFormalParameters(arguments)));
		
		//close menu [menu] all
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.ALL.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEMENU);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeMenuAll", getFormalParameters(arguments)));
		
		//close menu [menu] (player)
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEMENU);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeMenu", getFormalParameters(arguments)));
		
		//close instance [menu] [instance]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.INSTANCE.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEINSTANCE);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeInstance", getFormalParameters(arguments)));
		
		//close instance [menu] [instance] [player]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.MENU.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEINSTANCE);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closeInstance", getFormalParameters(arguments)));
		
		//close player [player]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLAYER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.CLOSE.getKeyword(), arguments, Perm.CLOSEPLAYER);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("closePlayer", getFormalParameters(arguments)));
		
		//load [filename]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.LOAD.getKeyword(), arguments, Perm.LOADFROMMENUSERVICE);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("loadMenu", getFormalParameters(arguments)));
		
		//load [filename] [plugin]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.LOAD.getKeyword(), arguments, Perm.LOADFROMOTHERPLUGIN);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("loadMenu", getFormalParameters(arguments)));
		
		//save [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.SAVE.getKeyword(), arguments, Perm.SAVE);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("saveMenu", getFormalParameters(arguments)));
		
		//save [menu] filename [filename]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		arguments.add(Keyword.FILENAME.getKeyword());
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.SAVE.getKeyword(), arguments, Perm.SAVEOTHERFILE);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("saveMenu", getFormalParameters(arguments)));
		
		//reload [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.RELOAD.getKeyword(), arguments, Perm.RELOAD);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("reloadMenu", getFormalParameters(arguments)));
		
		//unload [menu]
		arguments = new LinkedList<String>();
		arguments.add(Keyword.PLACEHOLDER.getKeyword());
		command = new MyCommand(Keyword.UNLOAD.getKeyword(), arguments, Perm.UNLOAD);
		commands.add(command);
		methods.put(command.toString(), this.getClass().getMethod("unloadMenu", getFormalParameters(arguments)));
		
//		//bind menu [menu] material
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind menu [menu] material [material]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind menu [menu] entity
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind menu [menu] entity [entity]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind instance [instance] material
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind instance [instance] material [material]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind instance [instance] entity
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//bind instance [instance] entity [entity]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind menu [menu] all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind menu [menu] material all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind menu [menu] material [material]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind menu [menu] entity all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind menu [menu] entity [entity]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind instance [instance] all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind instance [instance] material all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind instance [instance] material [material]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind instance [instance] entity all
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
//		//unbind instance [instance] entity [entity]
//		arguments = new LinkedList<String>();
//		arguments.add(Keyword.SENDER.getKeyword());
//		arguments.add(Keyword.MENU.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		arguments.add(Keyword.PLACEHOLDER.getKeyword());
//		command = new MyCommand(Keyword.OPEN.getKeyword(), arguments, Perm.OPENMENUOTHER);
//		commands.add(command);
//		methods.put(command.toString(), this.getClass().getMethod("help", getFormalParameters(arguments)));
//		
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
		
		//check if the keyword is valid
		for (MyCommand command: commands){
			
			if (command.getName().equalsIgnoreCase(args[0])){
								
				//check arguments
				if (command.matchesArguments(args)){

					Method m = methods.get(command.toString());
					if (m == null){
												
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
						Boolean ret = (Boolean) m.invoke(this, (Object[]) command.getActualArguments(args));
						
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

	public boolean listMenus(String sender) {
		for (Menu menu: menuService.getMenus()){
			Message.sendMessage(sender, MessageFormat.OUTPUT, menu.getName() + ": " + menu.getPlugin());
		}
		return true;
	}
	
	public boolean listInstances(String sender, String menuName){
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Message.sendMessage(sender, MessageFormat.ERROR, Message.NOSUCHMENU);
			return false;
		}
		
		List<MenuInstance> instances = menuService.getMenuInstances(menu);
		for (MenuInstance instance: instances){
			Message.sendMessage(sender, MessageFormat.OUTPUT, instance.getName());
		}
		
		return true;
	}
	
	public boolean openMenu(String sender, String MenuName){
		return false;
	}
	
	/**
	 * Opens a Menu for a given player
	 * @param pluginName the name of the Plugin which the Menu belongs to
	 * @param menuName the name of the menu
	 * @param playerName the name of the player to open the Menu for
	 */
	public boolean openMenu(String sender, String menuName, String playerName){
		
		if (menuName == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUNAME, null);
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENU, null);
			return false;
		}
		
		//get the Menu
		Menu menu = menuService.getMenu(menuName);
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHMENU, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENU, menuName);
			return false;
		}
		
		//get the instance
		MenuInstance instance = menuService.createMenuInstance(menu, menuName + ": " + playerName);
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.CANTCREATEMENUINSTANCE, menuName);
			Logger.log(2, Level.SEVERE, Message.CANTOPENMENU, menuName);
			return false;
		}
		
		//open the menu
		return menuService.openMenuInstance(instance, playerName);
	}
	
	public boolean openInstance(String sender, String menuName, String instanceName){
		return false;
	}
	
	public boolean openInstance(String sender, String menuName, String instanceName, String playerName){
		return false;
	}
	
	public boolean openPlayer(String sender, String playerName){
		return false;
	}
	
	public boolean closeAllMenus(String sender){
		return false;
	}
	
	public boolean closeMenu(String sender, String menuName) {
		
		return false;
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
