package com.m0pt0pmatt.menuservice.api;

import java.util.Map;
import java.util.Set;

import com.m0pt0pmatt.menuservice.api.actions.Action;
import com.m0pt0pmatt.menuservice.api.attributes.Attribute;
import com.m0pt0pmatt.menuservice.api.attributes.ContainerAttribute;

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

	//--------------------------Methods for all attributes--------------------------
	/**
	 * Returns the attributes of the Component
	 * @return
	 */
	public Map<String, Object> getAttributes();
	
	/**
	 * Sets the attributes of the Component
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes);
	
	//--------------------------Methods for one attribute--------------------------
	/**
	 * Returns a given attribute of the Component
	 * @param attribute the specified attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public Object getAttribute(Attribute attribute);
	
	/**
	 * Returns a given attribute of the Component
	 * @param attributeName the name of the specified attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public Object getAttribute(String attributeName);
	
	/**
	 * checks if the Component has a given attribute
	 * @param attribute the given Attribute
	 * @return whether or not the Component has the given attribute
	 */
	public boolean hasAttribute(Attribute attribute);
	
	/**
	 * checks if the Component has a given attribute
	 * @param attributeName the name of the given Attribute
	 * @return whether or not the Component has the given attribute
	 */
	public boolean hasAttribute(String attributeName);
	
	/**
	 * Adds a attribute to the Component
	 * @param attribute The type of attribute to be added
	 * @param value the value of the attribute
	 */
	public void addAttribute(Attribute attribute, Object value);

	/**
	 * Adds a attribute to the Component
	 * @param attributeName The name for the attribute
	 * @param value the value of the attribute
	 */
	public void addAttribute(String attributeName, Object value);
	
	public ContainerAttribute getContainerAttribute(String attributeName);
	
	//--------------------------Methods for reserved attributes--------------------------
	/**
	 * Returns the type of the Component
	 * @return
	 */
	public ComponentType getType();
	
	/**
	 * Checks the component if it is the given ComponentType
	 * @param type
	 * @return
	 */
	public boolean isType(ComponentType type);
	
	/**
	 * Sets the type of the Component
	 * @param type
	 */
	public void setType(ComponentType type);
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	public String getTag();
	
	public void setTag(String tag);
	
	//Methods for actions
	
	public void addAction(Action action, Integer tag);
	
	public void removeAction(Action action, Integer tag);
	
	public void removeAction(Action action);
	
	public Set<Integer> getActionTags(Action action);
}
