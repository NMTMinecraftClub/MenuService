package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		attributes.put("type", "no-type");
		attributes.put("tag", "no-tag");
		attributes.put("actions", new LinkedList<Integer>());
		attributes.put("lore", new LinkedList<String>());
	}
	
	/**
	 * Creates a new AbstractComponent with the given attributes
	 * @param attributes
	 */
	public AbstractComponent(Map<String, Object> attributes) {
		attributes = (attributes == null) ? new HashMap<String, Object>() : attributes;
		
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
	
	/**
	 * Returns a given attribute of the Component
	 * @param name
	 * @return
	 */
	@Override
	public Object getAttribute(String name){
		return attributes.get(name);
	}
	
	/**
	 * Returns an attribute, Assuming it is a List of some type
	 * 
	 */
	public List<?> getListAttribute(String name){
		Object attribute = getAttribute(name);
		if (attribute instanceof List){
			return (List<?>) attribute;
		}
		return null;
	}
	
	public ContainerAttribute getConatinerAttribute(String name){
		Object attribute = getAttribute(name);
		if (attribute instanceof ContainerAttribute){
			return (ContainerAttribute) attribute;
		}
		return null;
	}
	
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
	
}
