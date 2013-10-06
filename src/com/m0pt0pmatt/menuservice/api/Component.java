package com.m0pt0pmatt.menuservice.api;

import java.util.List;
import java.util.Map;

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
	
	//--------------------------Methods for one container attribute--------------------------
	/**
	 * Returns a given attribute of the Component as a ContainerAttribute if possible
	 * @param attribute the type of attribute
	 * @return the attribute if it exists and is a ContainerAttribute, otherwise null
	 */
	public ContainerAttribute getContainerAttribute(Attribute attribute);
	
	/**
	 * Returns a given attribute of the Component as a ContainerAttribute if possible
	 * @param attributeName the name of the attribute
	 * @return the attribute if it exists and is a ContainerAttribute, otherwise null
	 */
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
	 * Checks the component if it is the given ComponentType
	 * @param type
	 * @return
	 */
	public boolean isType(String type);
	
	/**
	 * Sets the type of the Component
	 * @param type
	 */
	public void setType(ComponentType type);
	
	/**
	 * Sets the type of the Component
	 * @param type
	 */
	public void setType(String type);
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	public String getTag();
	
	/**
	 * Sets the tag of the Component
	 * @param tag
	 */
	public void setTag(String tag);
	
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
	
	//--------------------------Methods for actions--------------------------
	/**
	 * Returns an action
	 * @param type
	 * @return
	 */
	public ContainerAttribute getAction(String type);
	
	/**
	 * Adds action tags to a given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 */
	public void addAction(String type, List<Integer> tags);
	
	/**
	 * Adds action tags and permission to the given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param permissions the permission to add
	 */
	public void addAction(String type, List<Integer> tags, List<String> permissions);
	
	/**
	 * Adds action tags and commands to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 */
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender);
	
	/**
	 * Adds action tags, commands, and permissions to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 * @param permissions the permission to add
	 */
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender, List<String> permissions);
	
	/**
	 * Checks if the component has an action for the given interaction
	 * @param type the type of interaction
	 * @return true if there exists an interaction, false otherwise
	 */
	public boolean hasInteraction(String type);

}
