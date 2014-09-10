package com.m0pt0pmatt.menuservice.api;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * A Menu is a model of an abstract user interface.
 * A Menu is made of Components, which are normally buttons and/or labels.
 * 
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 */
public final class Menu{

	//Each menu has a title
	private String title;
	
	//The set of components for the menu
	private Map<String, Component> components;
	
	private Map<String, MenuImplementation> implementations;
	
	private Map<UUID, MenuImplementation> players;
	
	//MenuAttributes for the menu
	private Map<String, Object> attributes;
		
	public Menu(){
		components      = new HashMap<String, Component>();
		implementations = new HashMap<String, MenuImplementation>();
		players         = new HashMap<UUID, MenuImplementation>();
		attributes      = new HashMap<String, Object>();
	}
	
	/**
	 * Gets the title of the menu.
	 * @return the title of the menu
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the menu.
	 * @param title the new title of the menu
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the Map of Components.
	 * @return the Map of Components.
	 */
	public Map<String, Component> getComponents() {
		return components;
	}

	/**
	 * Sets all of the Components of the menu
	 * @param components the new Map of Components
	 */
	public void setComponents(Map<String, Component> components) {
		this.components = components;
	}
	
	/**
	 * Checks if the menu has a certain Component
	 * @param component
	 * @return
	 */
	public boolean hasComponent(Component component) {
		return components.containsValue(component);
	}
	
	/**
	 * Checks if the menu has a certain Component
	 * @param componentName the name of the Component
	 * @return
	 */
	public boolean hasComponent(String componentName) {
		return components.containsKey(componentName);
	}
	
	/**
	 * Adds a Component to the menu.
	 * @param component the Component to be added
	 */
	public void addComponent(Component component) {
		components.put(component.getName(), component);
	}

	/**
	 * Removes a Component from the menu.
	 * @param component The Component to be removed
	 */
	public void removeComponent(Component component) {
		components.remove(component.getName());
	}
	
	/**
	 * Removes a Component from the menu.
	 * @param componentName The name of the Component to be removed
	 */
	public void removeComponent(String componentName) {
		components.remove(componentName);
	}
	
	/**
	 * Returns a set of all players currently viewing the menu.
	 * Editing this set does not remove or add players to the menu.
	 * @return
	 */
	public Set<UUID> getPlayers(){
		return new HashSet<UUID>(players.keySet());
	}
	
	/**
	 * Adds a player to the menu, thus opening the menu for the player
	 * @param uuid The UUIF of the player
	 * @param renderer How the menu should look to the player
	 */
	public void addPlayer(UUID uuid, MenuType type){
		
		//Create an implementation if one does not exist for the given renderer
		if (!implementations.containsKey(type.getName())){
			
			MenuImplementation implementation;
			try {
				try {
					implementation = type.getImplementationClass().getConstructor(Menu.class, Plugin.class).newInstance(this, Bukkit.getPluginManager().getPlugin("MenuService"));
					implementations.put(type.getName(), implementation);
				} catch (IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			
		}
		
		//Get the implementation for the renderer
		MenuImplementation implementation = implementations.get(type.getName());
		if (implementation == null){
			return;
		}
		
		//Keep track of the player and his/her implementation
		players.put(uuid, implementation);
		
		//Open the menu
		implementation.openMenu(uuid);
	}
	
	/**
	 * Removes a player from the enu, thus closing the menu for the player.
	 * @param uuid the UUID of the player
	 */
	public void removePlayer(UUID uuid){
		
		System.out.println("Menu is removing");
		
		//Remove the player from the players set
		MenuImplementation i = players.get(uuid);		
		players.remove(uuid);
		
		//Close the menu for the player
		i.closeMenu(uuid);
	}
	
	/**
	 * Updates the menu.
	 * Call this if the menu has changed and needs to be updated to players
	 */
	public void update(){
		
		//Update every implementation
		for (MenuImplementation implementation: players.values()){
			implementation.update();
		}
	}
	
	/**
	 * Returns the attributes of the Component
	 * @return
	 */
	public Map<String, Object> getAttributes(){
		return attributes;
	}
	
	/**
	 * Sets the attributes of the Component
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes){
		this.attributes = attributes;
	}
	
	public Object getAttribute(MenuAttribute attribute) {
		Object o = attributes.get(attribute.getName());
		if (o == null){
			return null;
		}
		if (o.getClass().equals(attribute.getAttributeClass())){
			return o;
		}
		return null;
	}
	
	/**
	 * Returns a given attribute of the Component
	 * @param name
	 * @return
	 */
	public Object getAttribute(String attributeName){
		return attributes.get(attributeName);
	}
	
	public boolean hasAttribute(MenuAttribute attribute){
		Object o = getAttribute(attribute);
		if (o == null){
			return false;
		}
		return true;
	}
	
	/**
	 * checks if the Component has a given attribute
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String attributeName){
		return attributes.containsKey(attributeName);
	}
	
	public void addAttribute(MenuAttribute attribute, Object value) {
		if (!value.getClass().equals(attribute.getAttributeClass())){
			return;
		}
		
		attributes.put(attribute.getName(), value);
	}
	
	/**
	 * Adds a attribute to the Component
	 * @param name
	 * @param value
	 */
	public void addAttribute(String attributeName, Object value) {
		attributes.put(attributeName, value);
	}
	
	/**
	 * Closes the menu for all players.
	 * When reloading the server, it is super important for this to be called on all menus
	 */
	public void closeAll(){
		Set<UUID> oldplayers = new HashSet<UUID>();
		oldplayers.addAll(players.keySet());
		for (UUID player: oldplayers){
			this.removePlayer(player);
		}
	}
	
	public Set<Component> getComponentsWithTag(String tag){
		Set<Component> componentsWithTag = new HashSet<Component>();
		
		for (Component component: components.values()){
			if (component.hasTag(tag)){
				componentsWithTag.add(component);
			}
		}
		
		return componentsWithTag;
	}
	
}
