package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class Component{
	
	private ComponentType type;
	private ActionListener actionListener;
	private String name;
	
	//The attributes of the component
	private Map<String, Object> attributes;
	
	private Set<String> tags;
	
	/**
	 * Creates a new AbstractComponent, giving default values to all variables
	 */
	public Component(){
		this.attributes = new HashMap<String, Object>();
		this.tags = new HashSet<String>();
	}
	
	/**
	 * Creates a new AbstractComponent with the given attributes
	 * @param attributes
	 */
	public Component(Map<String, Object> attributes) {
		
		//if the given attributes was null, create a new Map. Else use the provided attributes
		attributes = (attributes == null) ? new HashMap<String, Object>() : attributes;
		
		//make sure the attributes have default values
		if (!attributes.containsKey("type")) attributes.put("type", "no-type");
		if (!attributes.containsKey("tag")) attributes.put("tag", "no-tag");
		if (!attributes.containsKey("actions")) attributes.put("actions", new LinkedList<Integer>());
		if (!attributes.containsKey("lore")) attributes.put("lore", new LinkedList<String>());
		
		this.attributes = attributes;
		this.tags = new HashSet<String>();
	}
	
	public Component(Map<String, Object> attributes, Set<String> tags){
		//if the given attributes was null, create a new Map. Else use the provided attributes
		attributes = (attributes == null) ? new HashMap<String, Object>() : attributes;
		
		//make sure the attributes have default values
		if (!attributes.containsKey("type")) attributes.put("type", "no-type");
		if (!attributes.containsKey("tag")) attributes.put("tag", "no-tag");
		if (!attributes.containsKey("actions")) attributes.put("actions", new LinkedList<Integer>());
		if (!attributes.containsKey("lore")) attributes.put("lore", new LinkedList<String>());
		
		this.attributes = attributes;
		this.tags = tags;
	}
	
	public ActionListener getListener() {
		return actionListener;
	}
	public void setListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	public boolean hasListener() {
		if (actionListener == null){
			return false;
		}
		return true;
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
	
	public Object getAttribute(ComponentAttribute attribute) {
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
	
	public boolean hasAttribute(ComponentAttribute attribute){
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
	
	public void addAttribute(ComponentAttribute attribute, Object value) {
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
	 * Returns the type of the Component
	 * @return
	 */
	public ComponentType getType(){
		return type;
	}
	
	public boolean isType(ComponentType type) {
		ComponentType cType = this.getType();
		if (cType == null){
			return false;
		}
		if (cType.equals(type)){
			return true;
		}
		return false;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the tag of the Component
	 * @param tag
	 */
	public void setName(String name){
		this.name = name;
	}
	
	public Set<String> getTags(){
		return tags;
	}
	
	public void setTags(Set<String> tags){
		this.tags = tags;
	}
	
	public void addTag(String tag){
		this.tags.add(tag);
	}
	
	public void removeTag(String tag){
		this.tags.remove(tag);
	}
	
	public boolean hasTag(String tag){
		return tags.contains(tag);
	}
	
}
