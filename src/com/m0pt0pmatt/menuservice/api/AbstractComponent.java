package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.m0pt0pmatt.menuservice.api.attributes.Attribute;
import com.m0pt0pmatt.menuservice.api.attributes.ContainerAttribute;

/**
 * A AbstractComponent is a basic implementation of a Component.
 * A AbstractComponent is designed to be extended for other ComponentTypes, but this is not necessary.
 * A AbstractComponent handles all attributes that every Component will have.
 * 
 * @author mbroomfield
 *
 */
public class AbstractComponent implements Component{
	
	//The attributes of the component
	private Map<String, Object> attributes;
	
	/**
	 * Creates a new AbstractComponent, giving default values to all variables
	 */
	public AbstractComponent(){
		
		this.attributes = new TreeMap<String, Object>();
		
		//give default values
		attributes.put("type", "no-type");
		attributes.put("tag", "no-tag");
		attributes.put("lore", new LinkedList<String>());
	}
	
	/**
	 * Creates a new AbstractComponent with the given attributes
	 * @param attributes
	 */
	public AbstractComponent(Map<String, Object> attributes) {
		
		//if the given attributes was null, create a new Map. Else use the provided attributes
		attributes = (attributes == null) ? new HashMap<String, Object>() : attributes;
		
		//make sure the attributes have default values
		if (!attributes.containsKey("type")) attributes.put("type", "no-type");
		if (!attributes.containsKey("tag")) attributes.put("tag", "no-tag");
		if (!attributes.containsKey("actions")) attributes.put("actions", new LinkedList<Integer>());
		if (!attributes.containsKey("lore")) attributes.put("lore", new LinkedList<String>());
	}
	
	/**
	 * Sets the type of the Component
	 * @param type
	 */
	@Override
	public void setType(String type){
		attributes.put("type", type);
	}
	
	/**
	 * Returns the type of the Component
	 * @return
	 */
	@Override
	public String getType(){
		Object type = attributes.get("type");
		return (type instanceof String) ? (String) type : null;
	}
	
	/**
	 * Sets the tag of the Component
	 * @param tag
	 */
	@Override
	public void setTag(String tag){
		attributes.put("tag", tag);
	}
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	@Override
	public String getTag(){
		Object tag =  attributes.get("tag");
		return (tag instanceof String) ? (String) tag : null;
	}
	
	/**
	 * Sets the attributes of the Component
	 * @param attributes
	 */
	@Override
	public void setAttributes(Map<String, Object> attributes){
		this.attributes = attributes;
	}
	
	/**
	 * Returns the attributes of the Component
	 * @return
	 */
	@Override
	public Map<String, Object> getAttributes(){
		return attributes;
	}

	/**
	 * Adds a attribute to the Component
	 * @param name
	 * @param value
	 */
	@Override
	public void addAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
	@Override
	public void addAttribute(Attribute attribute, Object value) {
		if (!value.getClass().equals(attribute.getAttributeClass())){
			return;
		}
		
		attributes.put(attribute.getName(), value);
		
	}
	
	/**
	 * Returns a given attribute of the Component
	 * @param name
	 * @return
	 */
	@Override
	public Object getAttribute(String name){
		return attributes.get(name);
	}
	
	@Override
	public Object getAttribute(Attribute attribute) {
		Object o = attributes.get(attribute.getName());
		if (o.getClass().equals(attribute.getClass())){
			return o;
		}
		return null;
	}
	
	/**
	 * Returns a given attribute of the Component
	 * @param name the name of the attribute
	 * @return the attribute if it exists, otherwise null
	 */
	@Override
	public ContainerAttribute getContainerAttribute(String name){
		Object attribute = getAttribute(name);
		if (attribute instanceof ContainerAttribute){
			return (ContainerAttribute) attribute;
		}
		return null;
	}
	
	@Override
	public ContainerAttribute getContainerAttribute(Attribute a) {
		Object attribute = getAttribute(a.getName());
		if (attribute instanceof ContainerAttribute){
			return (ContainerAttribute) attribute;
		}
		return null;
	}
	
	
	/**
	 * Returns a given attribute of the Component, checking if the value of the attribute is a MenuInstance parameter
	 * @param name the name of the attribute
	 * @param instance The menuInstance whose paramters will be checked
	 * @return if a parameter
	 */
	@Override
	public Object getParameteredAttribute(String name, MenuInstance instance) {
		Object attribute = getAttribute(name);
		if (attribute instanceof String){
			String parameterName = (String) attribute;
			if (parameterName.startsWith("^")){
				attribute = instance.getParameter(parameterName.substring(1));
			}
		}
		return attribute;
	}
	
	/**
	 * checks if the Component has a given attribute
	 * @param name
	 * @return
	 */
	@Override
	public boolean hasAttribute(String name){
		return attributes.containsKey(name);
	}
	
	/**
	 * Sets the Lore of the Component
	 * @param lore
	 */
	@Override
	public void setLore(List<String> lore){
		attributes.put("lore", (lore == null) ? new LinkedList<String>() : lore);
	}
	
	/**
	 * Returns the Lore of the Component
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getLore(){
		List<String> actions;
		
		try {
			actions = (List<String>) attributes.get("lore");
		} catch (ClassCastException e){
			return null;
		}
		
		return actions;
	}	
	
	/**
	 * Adds action tags to a given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 */
	@Override
	public void addAction(String type, List<Integer> tags) {
		addAction(type, tags, new LinkedList<String>(), "<player>", new LinkedList<String>());
		
	}
	
	/**
	 * Adds action tags and permission to the given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param permissions the permission to add
	 */
	@Override
	public void addAction(String type, List<Integer> tags, List<String> permissions) {
		addAction(type, tags, new LinkedList<String>(), "<player>", permissions);
		
	}

	/**
	 * Adds action tags and commands to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 */
	@Override
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender) {
		addAction(type, tags, commands, commandSender, new LinkedList<String>());
	}

	/**
	 * Adds action tags, commands, and permissions to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 * @param permissions the permission to add
	 */
	@Override
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender, List<String> permissions) {
		
		if (!this.hasAttribute("actions")){
			addAttribute("actions", new ContainerAttribute("actions", new HashMap<String, Object>()));
		}
		
		ContainerAttribute actions = (ContainerAttribute) getAttribute("actions");
		
		HashMap<String, Object> typeMap = new HashMap<String, Object>();                                            
		typeMap.put("tags", tags);
		typeMap.put("commands", commands);
		typeMap.put("sender", commandSender);
		typeMap.put("permissions", permissions);
		actions.getAttributes().put("leftClick", new ContainerAttribute(type, typeMap));                   
		
		
	}

	/**
	 * Checks if the component has an action for the given interaction
	 * @param type the type of interaction
	 * @return true if there exists an interaction, false otherwise
	 */
	@Override
	public boolean hasInteraction(String type) {
		if (!this.hasAttribute("actions")){
			return false;
		}
		
		ContainerAttribute actions = this.getContainerAttribute("actions");
		if (actions == null){
			return false;
		}
		
		if (actions.hasAttribute(type)){
			return true;
		}
		
		return false;
	}

	/**
	 * Returns an action
	 * @param type
	 * @return
	 */
	@Override
	public ContainerAttribute getAction(String type) {
		if (!this.hasAttribute("actions")){
			return null;
		}
		
		ContainerAttribute actions = this.getContainerAttribute("actions");
		if (actions == null){
			return null;
		}
		
		if (actions.hasAttribute(type)){
			return (ContainerAttribute) actions.getAttribute(type);
		}
		
		return null;
	}

	

	
	
}
