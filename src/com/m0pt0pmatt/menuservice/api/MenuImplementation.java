package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.MenuImplementation;

public abstract class MenuImplementation implements Listener{

	protected Menu menu;
	protected Plugin plugin;
	
	public MenuImplementation(Menu menu, Plugin plugin){
		this.menu = menu;
		this.plugin = plugin;
	}
	
	public Menu getMenu(){
		return menu;
	}
	
	public abstract void openMenu(UUID uuid);

	public abstract void closeMenu(UUID uuid);

	public abstract void update();

	public abstract String getImplementationName();
	
}
