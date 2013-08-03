package com.m0pt0pmatt.menuservice.api;

import java.util.Map;

/**
 * A ContainerAttribute is a special type of attribute for a Component. 
 * Essentially, a ContainerAttribute is just a name and a Map of attributes
 * @author mbroomfield
 *
 */
public final class ContainerAttribute {

	//the name of the ContainerAttribute
	private String name;
	
	//the attributes of the ContainerAttribute
	private Map<String, Object> attributes;

	/**
	 * Creates a new ContainerAttribute
	 * @param name the name of the ContainerAttribute
	 * @param attributes the Map of attributes for the ContainerAttribute
	 */
	public ContainerAttribute(String name, Map<String, Object> attributes){
		this.name = name;
		this.attributes = attributes;
	}
	
	/**
	 * Returns the name of the ContainerAttribute
	 * @return the name of the ContainerAttribute
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the ContainerAttribute
	 * @param name the name of the ContainerAttribute
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the Map of attributes of the ContainerAttribute
	 * @return the Map of attributes of the ContainerAttribute
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the Map of attributes of the ContainerAttribute
	 * @param attributes the Map of attributes of the ContainerAttribute
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Returns an attribute from the ContainerAttribute
	 * @param name the name of the attribute to be returned
	 * @return the attribute if it exists, null otherwise
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Checks if the ContainerAttribute has a given attribute
	 * @param name the name of the attribute
	 * @return true if the attribute exists, false otherwise
	 */
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
}
