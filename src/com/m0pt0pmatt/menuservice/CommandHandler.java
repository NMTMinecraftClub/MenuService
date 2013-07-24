package com.m0pt0pmatt.menuservice;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	private MenuServicePlugin plugin;
	
	public CommandHandler(MenuServicePlugin plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		//make sure the command is for MenuService
		if (!cmd.getName().equalsIgnoreCase(plugin.getName())){
			return false;
		}
		
		//if there are no args, print out the help menu
		if (args.length == 0){
			plugin.showHelp(sender);
			return true;
		}
		
		//check if the command is a "list" command
		if (args[0].equalsIgnoreCase("list")){
			return handleList(sender, cmd, label, args);
		}
		
		//check if the command is an "open" command
		if (args[0].equalsIgnoreCase("open")){
			return handleOpen(sender, cmd, label, args);
		}
		
		//check if the command is a "close" command
		if (args[0].equalsIgnoreCase("close")){
			return handleClose(sender, cmd, label, args);
		}
		
		//check if the command is a "load" command
		else if (args[0].equalsIgnoreCase("load")){
			return handleLoad(sender, cmd, label, args);
		}
		
		//check if the command is a "save" command
		else if (args[0].equalsIgnoreCase("save")){
			return handleSave(sender, cmd, label, args);
		}
		
		//check if the command is a "reload" command
		else if (args[0].equalsIgnoreCase("reload")){
			return handleReload(sender, cmd, label, args);
		}
		
		//check if the command is a "unload" command
		else if (args[0].equalsIgnoreCase("unload")){
			return handleUnload(sender, cmd, label, args);
		}
		
		//check if the command is an "edit" command
		else if (args[0].equalsIgnoreCase("edit")){
			return handleEdit(sender, cmd, label, args);
		}
		
		//check if the command is a "delete" command
		else if (args[0].equalsIgnoreCase("delete")){
			return handleDelete(sender, cmd, label, args);
		}
		
		//check if the command is a "bind" command
		else if (args[0].equalsIgnoreCase("bind")){
			return handleBind(sender, cmd, label, args);
		}
		
		//check if the command is a "unbind" command
		else if (args[0].equalsIgnoreCase("unbind")){
			return handleUnbind(sender, cmd, label, args);
		}
		
		//check if the command is a "help" command
		else if (args[0].equalsIgnoreCase("help")){
			return handleHelp(sender, cmd, label, args);
		}
		
		sender.sendMessage("Incorrect use of /menuservice");
		return false;
	}
	
	private boolean handleHelp(CommandSender sender, Command cmd, String label, String[] args){
		return notImplemented(sender, cmd, label, args, "menuservice.help");
	}
	
	private boolean handleList(CommandSender sender, Command cmd, String label, String[] args){
		
		//check permission
		if (!sender.hasPermission("menuservice.list")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//check if player wants to list menus
		if (args.length == 1){
			plugin.listMenus(sender);
			return true;
		}
		
		return false;
	}
	
	private boolean handleLoad(CommandSender sender, Command cmd, String label, String[] args){ 

		//check permission
		if (!sender.hasPermission("menuservice.load")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//load a MenuService menu
		if (args.length == 2){
			if(plugin.loadMenu(plugin.getName(), args[1])){
				sender.sendMessage("Menu loaded");
			} else{
				sender.sendMessage("Menu not loaded");
			}
			return true;
		}
		
		//load a menu from another plugin
		else if (args.length == 3){
			if(plugin.loadMenu(args[1], args[2])){
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
	private boolean handleSave(CommandSender sender, Command cmd, String label, String[] args){
		
		//check permission
		if (!sender.hasPermission("menuservice.save")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//save a MenuService menu to MenuService
		if (args.length == 3){
			if(plugin.saveMenu(args[1], plugin.getName(), args[2])){
				sender.sendMessage("Menu saved");
			} else{
				sender.sendMessage("Menu not saved");
			}
			return true;
		}
		
		//save a menu to another plugin
		else if (args.length == 4){
			if(plugin.saveMenu(args[1], args[2], args[3])){
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
	
	private boolean handleReload(CommandSender sender, Command cmd, String label, String[] args){
		return notImplemented(sender, cmd, label, args, "menuservice.reload");
	}
	private boolean handleOpen(CommandSender sender, Command cmd, String label, String[] args){
		//check permission
		if (!sender.hasPermission("menuservice.open")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//check if player and menuName were specified
		if (args.length == 3){
			if(plugin.openMenu(plugin.getName(), args[1], args[2])){
				sender.sendMessage("Menu opened");
			} else{
				sender.sendMessage("Could not open menu");
			}
			return true;
		}
		
		//check if player, plugin, and menuName were specified
		if (args.length == 4){
			if(plugin.openMenu(args[1], args[2], args[3])){
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
	private boolean handleClose(CommandSender sender, Command cmd, String label, String[] args){
		//check permission
		if (!sender.hasPermission("menuservice.close")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//check if nothing was specified
		if (args.length == 1){
			plugin.closeMenu(sender.getName());
			return true;
		}
		
		//check if player was specified
		if (args.length == 2){
			plugin.closeMenu(args[1]);
			return true;
		}
		
		//incorrect use of open
		sender.sendMessage("Incorrect use of /menuservice close");
		return false;
	}
	
	private boolean handleBind(CommandSender sender, Command cmd, String label, String[] args){ 
		
		//check permission
		if (!sender.hasPermission("menuservice.bind")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
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
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
					return true;
				}
				
				//bind the item
				if(plugin.bindMenu(((Player)sender).getItemInHand(), plugin.getName(), args[2])){
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
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
					return true;
				}
				//bind the item
				if(plugin.bindMenu(((Player)sender).getItemInHand(), args[3], args[2])){
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
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
					return true;
				}

				//bind the material
				if (plugin.bindMenu(((Player)sender).getItemInHand().getType(), plugin.getName(), args[2])){
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
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
					return true;
				}
				
				//bind the material
				if(plugin.bindMenu(((Player)sender).getItemInHand().getType(), args[3], args[2])){
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
	private boolean handleUnbind(CommandSender sender, Command cmd, String label, String[] args){ 

		//check permission
		if (!sender.hasPermission("menuservice.unbind")){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		//check if material was specified
		if (args.length == 2 && args[1].equalsIgnoreCase("material")){
			
			//make sure a Player is executing the command
			if (!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
				return true;
			}
			
			if(plugin.unbindMaterial(((Player)sender).getItemInHand().getType())){
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
				sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
				return true;
			}
			
			if(plugin.unbindItem(((Player)sender).getItemInHand())){
				sender.sendMessage("Item unbinded");
			} else{
				sender.sendMessage("Item not unbinded");
			}
			return true;
		}
		
		//check if material was specified
		if (args.length == 3 && args[1].equalsIgnoreCase("menu")){
			
			if(plugin.unbindMenu(args[2])){
				sender.sendMessage("Menu unbinded");
			} else{
				sender.sendMessage("Menu not unbinded");
			}
			return true;
		}
		
		//check if material was specified
		if (args.length == 4 && args[1].equalsIgnoreCase("menu")){
			
			if(plugin.unbindMenu(args[2], args[3])){
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
	
	private boolean handleEdit(CommandSender sender, Command cmd, String label, String[] args){
		return notImplemented(sender, cmd, label, args, "menuservice.edit");
	}
	
	private boolean handleDelete(CommandSender sender, Command cmd, String label, String[] args){ 
		return notImplemented(sender, cmd, label, args, "menuservice.delete");
	}
	
	private boolean handleUnload(CommandSender sender, Command cmd, String label, String[] args){
		return notImplemented(sender, cmd, label, args, "menuservice.unload");
	}

	private boolean notImplemented(CommandSender sender, Command cmd, String label, String[] args, String permission){
		//check permission
		if (!sender.hasPermission(permission)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to do that");
			return false;
		}
		
		sender.sendMessage("The command " + cmd.getName() + " " + args[0] + " is not implemented yet. Sorry!");
		return true;
	}
	
}
