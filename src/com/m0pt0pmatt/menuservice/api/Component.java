package com.m0pt0pmatt.menuservice.api;

import java.util.List;
import java.util.Map;

/**
 * A MenuComponent is a part of a Menu.
 * Components must have a type. This allows Renderers to know how to render them.
 * Components also must have a tag. This allows them to be distinguished in a Menu.
 * 
 * Components can have any number of attributes. Attributes can be any Object.
 * Attributes help Renderers add detail to the Component.
 * 
 * One of the most important attributes of a Component is its List of action tags.
 * Action tags are integers. When the player interacts with a Component, actions are thrown.
 * This allows ActionListeners to react to certain actions.
 * Action tags are used for ActionListeners to know how to react.
 * A single Component can have unlimited action tags.
 * 
 * @author mbroomfield
 *
 */
public interface Component {

	/**
	 * Sets the type of the Component
	 * @param type
	 */
	public void setType(String type);
	
	/**
	 * Returns the type of the Component
	 * @return
	 */
	public String getType();
	
	/**
	 * Sets the tag of the Component
	 * @param tag
	 */
	public void setTag(String tag);
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	public String getTag();
	
	/**
	 * Returns the Lore of the Component
	 * @return
	 */
	public List<String> getLore();

	/**
	 * Sets the Lore of the Component
	 * @param lore
	 */
	public void setLore(List<String> lore);
	
	/**
	 * Sets the attributes of the Component
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes);

	/**
	 * Returns the attributes of the Component
	 * @return
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Adds a attribute to the Component
	 * @param name
	 * @param value
	 */
	public void addAttribute(String name, Object value);
	
	/**
	 * Returns a given attribute of the Component
	 * @param name the name of the attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public Object getAttribute(String name);
	
	/**
	 * Returns a given attribute of the Component
	 * @param name the name of the attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public List<?> getListAttribute(String name);
	
	/**
	 * Returns a given attribute of the Component
	 * @param name the name of the attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public ContainerAttribute getConatinerAttribute(String name);
	
	/**
	 * Returns a given attribute of the Component, checking if the value of the attribute is a MenuInstance parameter
	 * @param name the name of the attribute
	 * @param instance The menuInstance whose paramters will be checked
	 * @return if a parameter
	 */
	public Object getParameteredAttribute(String name, MenuInstance instance);

	/**
	 * checks if the Component has a given attribute
	 * @param name
	 * @return
	 */
	public boolean hasAttribute(String name);

}
