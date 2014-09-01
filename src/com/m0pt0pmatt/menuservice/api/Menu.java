package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
	
	private Map<Renderer, MenuImplementation> implementations;
	private Map<UUID, MenuImplementation> players;
	
	private Map<String, Object> attributes;
		
	public Menu(){
		components = new HashMap<String, Component>();
		players = new HashMap<UUID, MenuImplementation>();
		attributes = new HashMap<String, Object>();
		implementations = new HashMap<Renderer, MenuImplementation>();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Map<String, Component> getComponents() {
		return components;
	}

	public void setComponents(Map<String, Component> components) {
		this.components = components;
	}
	
	public boolean hasComponent(Component part) {
		return components.containsValue(part);
	}
	
	public boolean hasComponent(String partName) {
		return components.containsKey(partName);
	}
	
	public void addComponent(Component part) {
		components.put(part.getName(), part);
	}

	public void removeComponent(Component part) {
		components.remove(part.getName());
	}
	
	public void removeComponent(String partName) {
		components.remove(partName);
	}
	
	public Map<UUID, MenuImplementation> getPlayers(){
		return players;
	}
	
	public void addPlayer(UUID uuid, Renderer renderer){
		if (!implementations.containsKey(renderer)){
			implementations.put(renderer, renderer.createImplementation(this));
		}
		
		MenuImplementation implementation = implementations.get(renderer);
		players.put(uuid, implementation);
		implementation.openMenu(uuid);
	}
	
	public void removePlayer(UUID uuid){
		players.get(uuid).closeMenu(uuid);
		players.remove(uuid);
	}
	
	public void update(){
		for (Entry<UUID, MenuImplementation> entry: players.entrySet()){
			entry.getValue().update();
			entry.getValue().openMenu(entry.getKey());
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
	
}
