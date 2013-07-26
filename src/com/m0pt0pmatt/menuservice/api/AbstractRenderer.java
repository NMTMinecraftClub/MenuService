package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

/**
 * An AbstractRenderer is a Basic Renderer that is designed to help other Renderers easier to create.
 * An AbstractRenderer does not Render or provide for any MenuInstance. It simple does housekeeping.
 * All an AbstractRenderer does it keep track of what players and instances are being provided for
 * @author mbroomfield
 *
 */
public abstract class AbstractRenderer implements Renderer{
	
	//the Map of provided players to provided instances
	private Map<String, MenuInstance> players;
	
	//the map of instances to the list of players being provided by the renderer
	private Map<MenuInstance, List<String>> instances;
	
	//the MenuService
	private MenuService menuService;
	
	private Plugin plugin;

	public AbstractRenderer(MenuService menuService, Plugin plugin){
		players = new HashMap<String, MenuInstance>();
		instances = new HashMap<MenuInstance, List<String>>();
		this.menuService = menuService;
	}
	
	protected Plugin getPlugin() {
		return plugin;
	}
	
	protected Map<String, MenuInstance> getPlayers() {
		return players;
	}

	protected void setPlayers(Map<String, MenuInstance> players) {
		this.players = players;
	}

	protected Map<MenuInstance, List<String>> getInstances() {
		return instances;
	}

	protected void setInstances(Map<MenuInstance, List<String>> instances) {
		this.instances = instances;
	}

	protected MenuService getMenuService() {
		return menuService;
	}

	protected void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	
	public void addMenuInstance(MenuInstance instance) {
		instances.put(instance, new LinkedList<String>());
	}

	public void removeMenuInstance(MenuInstance instance) {
		
		List<String> players = instances.remove(instance);
		for (String player: players){
			this.players.remove(player);
		}
		menuService.removeMenuInstance(instance);
		
	}
	
	@Override
	public void closeAll() {
		for (Entry<MenuInstance, List<String>> entry: this.getInstances().entrySet()){
			for (String player: entry.getValue()){
				this.closeMenu(player);
			}
		}
		
	}
	
}
