package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
	private Set<Component> components;
	
	public Map<UUID, Renderer> players;
	
	private Map<String, Object> attributes;
	
	public Menu(){
		components = new HashSet<Component>();
		players = new HashMap<UUID, Renderer>();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<Component> getComponents() {
		return components;
	}

	public void setComponents(Set<Component> components) {
		this.components = components;
	}
	
	public boolean hasComponent(Component part) {
		return components.contains(part);
	}
	
	public void addComponent(Component part) {
		components.add(part);
	}

	public void removeComponent(Component part) {
		components.remove(part);
	}
	
	public void removeAllPartsWithTag(String tag){
		
		Iterator<Component> i = components.iterator();
		while(i.hasNext()){
			Component component = i.next();
			
			if (component.hasTag(tag)){
				i.remove();
			}
		}
	}
	
	public void addPlayer(UUID uuid, Renderer renderer){
		players.put(uuid, renderer);
		renderer.drawMenu(this, uuid);
	}
	
	public void removePlayer(UUID uuid){
		players.get(uuid).undrawMenu(uuid);
		players.remove(uuid);
	}
	
	public Map<UUID, Renderer> getPlayers(){
		return players;
	}
	
	public void renderMenu(){
		for (Entry<UUID, Renderer> entry: players.entrySet()){
			entry.getValue().drawMenu(this, entry.getKey());
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
