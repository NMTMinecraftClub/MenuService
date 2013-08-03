package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.Iterator;
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
	
	//the list of instances that the renderer is providing for.
	private List<MenuInstance> instances;
	
	//the MenuService
	private MenuService menuService;

	/**
	 * Creates an AbstractRenderer.
	 * Adds the AbstractRenderer to the MenuService
	 * @param menuService
	 */
	public AbstractRenderer(MenuService menuService){
		
		//create maps
		players = new HashMap<String, MenuInstance>();
		instances = new LinkedList<MenuInstance>();
		
		//assign the menuService
		this.menuService = menuService;
		
		//add the AbstractRenderer to the MenuService
		menuService.addRenderer(this);
	}
	
	/**
	 * Returns the Map of handled players to their MenuInstances
	 * @return the Map of handled players to their MenuInstances
	 */
	protected Map<String, MenuInstance> getPlayers() {
		return players;
	}

	/**
	 * Sets the Map of handled players to their MenuInstances
	 * @param players the Map of handled players to their MenuInstances
	 */
	protected void setPlayers(Map<String, MenuInstance> players) {
		this.players = players;
	}

	/**
	 * Returns the list of MenuInstances provided by the Renderer
	 * @return the list of MenuInstances provided by the Renderer
	 */
	protected List<MenuInstance> getInstances() {
		return instances;
	}

	/**
	 * Sets the list of MenuInstances provided by the Renderer
	 * @param instances the list of MenuInstances provided by the Renderer
	 */
	protected void setInstances(List<MenuInstance> instances) {
		this.instances = instances;
	}

	/**
	 * Returns the MenuService this Renderer is registered with
	 * @return the MenuService this Renderer is registered with
	 */
	protected MenuService getMenuService() {
		return menuService;
	}

	/**
	 * Sets the MenuService this Renderer is registered with
	 * Removes the Renderer from the old MenuService and registers it with the new MenuService
	 * @param menuService the new MenuService
	 */
	protected void setMenuService(MenuService menuService) {
		this.menuService.removeRenderer(this);
		this.menuService = menuService;
		this.menuService.addRenderer(this);
	}
	
	/**
	 * Adds a MenuInstance to be provided for by the Renderer
	 * @param instance the MenuService to be provided for
	 */
	public void addMenuInstance(MenuInstance instance) {
		instances.add(instance);
	}

	/**
	 * Removes a MenuInstance. Stops providing for the MenuInstance.
	 * Stops providing for all players who were part of the MenuInstance.
	 * Removes those players from the MenuInstance and closes the menu for the players
	 * @param instance the MenuInstance to be removed
	 */
	public void removeMenuInstance(MenuInstance instance) {
		
		//Check all provided players
		Iterator<Entry<String, MenuInstance>> i = this.getPlayers().entrySet().iterator();
		while(i.hasNext()){
			Entry<String, MenuInstance> entry = i.next();
			
			//if the player is viewing the MenuInstance
			if (entry.getValue().equals(instance)){
				
				//remove the player from the MenuInstance
				instance.removePlayer(entry.getKey());
				
				//close the menu for the player
				this.closeMenu(entry.getKey());
				
				//remove the player from the Map of handled players
				i.remove();
			}
		}
		
		//remove the MenuInstance
		instances.remove(instance);
		
		//removes the MenuInstance from the MenuService
		menuService.removeMenuInstance(instance);
		
	}
	
	/**
	 * Closes all menus for all players
	 */
	@Override
	public void closeAll() {
		
		//for every player
		Iterator<Entry<String, MenuInstance>> i = players.entrySet().iterator();
		while (i.hasNext()){
			Entry<String, MenuInstance> entry = i.next();
			
			//close the menu
			this.closeMenu(entry.getKey());
			
			//remove the MenuInstance
			this.removeMenuInstance(entry.getValue());
			
			//remove the player entry
			i.remove();
		}
		
	}
	
}
