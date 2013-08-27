package com.m0pt0pmatt.menuservice;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public enum Perm {

	MENUSERVICE("menuservice.*"),
	
	HELP("menuservice.help.*"),
	HELPLIST("menuservice.help.list"),
	HELPOPEN("menuservice.help.open"),
	HELPCLOSE("menuservice.help.close"),
	HELPLOAD("menuservice.help.load"),
	HELPSAVE("menuservice.help.save"),
	HELPRELOAD("menuservice.help.reload"),
	HELPUNLOAD("menuservice.help.unload"),
	
	LIST("menuservice.list.*"),
	LISTMENUS("menuservice.list.menus"),
	LISTINSTANCES("menuservice.list.instances"),
	LISTPLAYERS("menuservice.list.players"),
	
	OPEN("menuservice.open.*"),
	OPENSELF("menuservice.open.self.*"),
	OPENOTHER("menuservice.open.other.*"),
	OPENMENU("menuservice.open.menu.*"),
	OPENMENUSELF("menuservice.open.menu.self"),
	OPENMENUOTHER("menuservice.open.menu.other"),
	OPENINSTANCE("menuservice.open.instance.*"),
	OPENINSTANCESELF("menuservice.open.instance.self"),
	OPENINSTANCEOTHER("menuservice.open.instance.other"),
	OPENPLAYER("menuservice.open.player"),
	
	CLOSE("menuservice.close.*"),
	CLOSEMENU("menuservice.close.menu.*"),
	CLOSEINSTANCE("menuservice.close.instance.*"),
	CLOSEPLAYER("menuservice.close.player"),
	
	LOAD("menuservice.load.*"),
	LOADFROMMENUSERVICE("menuservice.load.menuservice"),
	LOADFROMOTHERPLUGIN("menuservice.load.otherplugins"),
	LOADANDOVERRIDE("menuservice.load.override"),
	
	SAVE("menuservice.save.*"),
	SAVEOTHERFILE("menuservice.save.otherfile"),
	SAVEOTHERPLUGIN("menuservice.save.otherplugin"),
	SAVEFROMMENUSERVICE("menuservice.save.menuservice"),
	
	RELOAD("menuservice.reload.*"),
	RELOADMENUSERVICE("menuservice.reload.menuservice"),
	RELOADOTHERPLUGIN("menuservice.reload.otherplugin"),
	
	UNLOAD("menuservice.unload.*"),
	UNLOADMENUSERVICE("menuservice.unload.menuservice"),
	UNLOADOTHERPLUGIN("menuservice.unload.otherplugin");
	
	private final String permission;
	private Perm(String permission){
		this.permission = permission;
	}
	
	@Override
	public String toString(){
		return permission;
	}
	
	public static boolean has(CommandSender sender, Perm permission){
		if (sender instanceof ConsoleCommandSender){
			return true;
		}
		
		if (sender.hasPermission(permission.toString())){
			return true;
		}
		
		return false;
	}
}
