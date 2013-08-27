package com.m0pt0pmatt.menuservice;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Messages to be displayed to the terminal.
 * These are here so they can be adjusted easily.
 * @author mbroomfield
 *
 */
public enum Message {

	//null input
	NULLPLUGIN ("Error: The specified Plugin was null"),
	NULLPLUGINNAME ("Error: The specified name for the Plugin was null"),
	NULLMENUNAME ("Error: The specified name for the Menu was null"),
	NULLMENU ("Error: The specified Menu was null"),
	NULLFILENAME ("Error: The specified FileName was null"),
	NULLRENDERER ("Error: The specified Renderer was null"),
	NULLRENDERERNAME ("Error: The specified name for the Renderer was null"),
	NULLMENUINSTANCE ("Error: The specified MenuInstance was null"),
	NULLMENUINSTANCENAME ("Error: The specified name for the MenuInstance was null"),
	NULLITEMSTACK ("Error: The specified ItemStack was null"),
	NULLMATERIAL ("Error: The specified Material was null"),
	NULLPARAMETERS ("Error: The specified Parameters were null"),
	NULLMENUINSTANCES ("Error: There are no MenuInstances for the Menu"),
	NULLPLAYERNAME ("Error: The specified name for the Player was null"),
	NULLPLAYER ("Error: The specified Player was null"),
	NULLCOMMAND ("Error: The specified command was null"),
	NULLINVENTORY("Error: The specified Inventory was null"),
	
	//no such object
	NOSUCHMENU ("Error: The specified Menu is not loaded in the MenuService"),
	NOSUCHRENDERER ("Error: The specified Renderer is not loaded in the MenuService"),
	NOSUCHMENUINSTANCE ("Error: The specified MenuInstance is not loaded in the MenuService"),
	NOSUCHPLAYER ("Error: The specified Player does not exist"),
	NOSUCHFILE ("Error: the specified file does not exist"),
	NOSUCHPLUGIN ("Error: the specified menu's plugin does not exist"),
	
	WRONGMENU ("Error: The Menu is loaded but under a different plugin"),
	
	//error running method
	CANTLOADMENU ("Error: Could not load Menu"),
	CANTSAVEMENU ("Error: Could not save Menu"),
	CANTRELOADMENU ("Error: Could not reload Menu"),
	CANTOPENMENU ("Error: Could not open Menu"),
	CANTCLOSEMENU ("Error: Could not close Menu"),
	CANTCREATEMENUINSTANCE ("Error: Could not create MenuInstance"),
	CANTOPENMENUINSTANCE ("Error: Could not open MenuInstance"),
	CANTCLOSEMENUINSTANCE ("Error: Could not close MenuInstance"),
	CANTBINDMENUITEM ("Error: Could not bind the ItemStack to the Menu"),
	CANTBINDMENUMATERIAL ("Error: Could not bind the Material to the Menu"),
	CANTUNBINDITEM ("Error: Could not unbind the Material"),
	CANTUNBINDMATERIAL ("Error: Could not unbind Material"),
	CANTUNBINDMENU ("Error: Could not unbind Material"),
	CANTADDMENU ("Error: Could not add Menu"),
	CANTGETMENU ("Error: Could not get Menu"),
	CANTHASMENU ("Error: Could not check if Menu exists"),
	CANTREMOVEMENU ("Error: Could not remove Menu"),
	CANTREMOVEMENUINSTANCE ("Error: Could not remove MenuInstance"),
	CANTGETMENUINSTANCE ("Error: Could not get MenuInstance"),
	CANTHASMENUINSTANCE ("Error: Could not check if MenuInstance exists"),
	CANTADDRENDERER ("Error: Could not add Renderer"),
	CANTREMOVERENDERER ("Error: Could not remove Renderer"),
	CANTGETRENDERER ("Error: Could not get Renderer"),
	CANTHASRENDERER ("Error: Could not check if Renderer exists"),
	CANTEXECUTECOMMAND ("Error: Could not execute a command to open a Menu"),
	CANTRENDERINSTANCEPLAYER ("Error: Could not Render the MenuInstance"),
	
	CANTCREATEINVENTORY ("Error: Could not create an Inventory"),
	
	EMPTYMENUFORINSTANCE ("Error: The MenuInstance has no Menu"),
	
	//other exceptions
	CANTCASTATTRIBUTE ("Error: Could not cast the Attribute"),
	RENDERERNOTABSTRACTRENDERER ("Error: Renderer did not extend the type AbstractRenderer"),
	MENUALREADYEXISTS ("Error: MenuService arleady has a Menu with the same name"),
	
	//MenuManager Error Messages
	CANTSHOWMAINMENU ("Error: Could not show the Main Menu of the Menu Manager"), 
	
	//Command messages
	INCORRECTUSE("Incorrect use of /menuservice"),
	NOPERMISSION("You do not have permission to do that."),
	WRONGNUMBEROFARGUMENTS("Wrong number of arguments."), 
	BADARGUMENTS("Bad arguments."),
	INTERNALMETHODNOTFOUND("Internal method was not found."),
	ERRORINVOKINGMETHOD("An error occurred when running the command"),
	MENUSAVED("Menu saved"),
	MENUNOTSAVED("Menu not saved"),
	MENULOADED("Menu loaded"),
	MENUNOTLOADED("Menu not loaded"),
	MENURELOADED("Menu reloaded"),
	MENUNOTRELOADED("Menu not reloaded"),
	MENUUNLOADED("Menu unloaded"),
	MENUNOTUNLOADED("Menu not unloaded"), 
	MENUOPENED("Menu opened"),
	MENUNOTOPENED("Menu not opened"),
	MENUREMOVED("Menu removed");
	
	private final String message;
	
	private Message(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	//--------------Static message sending methods-----------------
	/**
	 * Sends a formatted string to a player
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(String playerName, MessageFormat type, String message){
		Player player = Bukkit.getPlayer(playerName);
		sendMessage(player, type, message);
	}
	
	/**
	 * Sends a formatted string to a player
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(String playerName, MessageFormat type, Message message){
		sendMessage(playerName, type, message.getMessage());
	}
	
	/**
	 * Sends a formatted string to a player
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(String playerName, MessageFormat type, String message, Object data){
		Player player = Bukkit.getPlayer(playerName);
		sendMessage(player, type, message.replaceFirst("%", data.toString()));
	}
	
	/**
	 * Sends a formatted string to a CommandSender
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(CommandSender sender, MessageFormat type, String message){
		if (sender instanceof Player){
			sendMessage((Player) sender, type, message);
			return;
		}
		
		if (type.getLabel() == null){
			sender.sendMessage(message);
		} else{
			sender.sendMessage("[" + type.getLabel() + "] " + message);
		}
		
	}
	
	/**
	 * Sends a formatted string to a CommandSender
	 * The first % in the message is replaced by data.toString()
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 * @data data the data
	 */
	public static void sendMessage(CommandSender sender, MessageFormat type, String message, Object data){
		sendMessage(sender, type, message.replaceFirst("%", data.toString()));
	}
	
	/**
	 * Sends a formatted string to a CommandSender
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(CommandSender sender, MessageFormat type, Message message){
		sendMessage(sender, type, message.getMessage());
	}
	
	/**
	 * Sends a formatted string to a CommandSender
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(CommandSender sender, MessageFormat type, Message message, Object data){
		sendMessage(sender, type, message.getMessage(), data);
	}	
	
	/**
	 * Sends a formatted string to a player
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(Player player, MessageFormat type, Message message){
		sendMessage(player, type, message.getMessage());
	}
	
	/**
	 * Sends a formatted string to a player
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 */
	public static void sendMessage(Player player, MessageFormat type, String message){
		
		if (type.getLabelColor() == null || type.getLabel() == null){
			player.sendMessage(type.getMessageColor() + message);
		} else{
			player.sendMessage(type.getLabelColor() + "[" + type.getLabel() + "] " + type.getMessageColor() + message);
		}
	}
	
	/**
	 * Sends a formatted string to a player
	 * The first % in the message is replaced by data.toString()
	 * @param player the player to send the message to
	 * @param type the MessageFormat of the message
	 * @param message the actual message
	 * @data data the data
	 */
	public static void sendMessage(Player player, MessageFormat type, String message, Object data){
		sendMessage(player, type, message.replaceFirst("%", data.toString()));
	}
}
