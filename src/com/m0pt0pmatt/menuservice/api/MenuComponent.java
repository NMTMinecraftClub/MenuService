package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.m0pt0pmatt.menuservice.api.attributes.Attribute;
import com.m0pt0pmatt.menuservice.api.attributes.ContainerAttribute;

/**
 * A MenuComponent is a MenuComponent which implements a Menu.
 * 
 * A MenuComponent acts like a DefaultComponent with added features.
 * 
 * @author mbroomfield
 *
 */
public class MenuComponent extends AbstractComponent implements Menu{

	private Map<String, Renderer> renderers;
	private boolean hasChanged = false;
	
	public MenuComponent(){
		super();
		this.addAttribute("components", new HashMap<String, Component>());
		renderers = new HashMap<String, Renderer>();
		this.setType("menu");
		setChanged(false);
	}
	
	//--------------------------Wrapper Methods for Component changes--------------------------
	/**
	 * Returns the attributes of the Component
	 * @return
	 */
	public Map<String, Object> getAttributes(){
		setChanged(true);
		return super.getAttributes();
	}
	
	/**
	 * Sets the attributes of the Component
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes){
		setChanged(true);
		super.setAttributes(attributes);
	}
	
	/**
	 * Returns a given attribute of the Component
	 * @param attribute the specified attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public Object getAttribute(Attribute attribute){
		setChanged(true);
		return super.getAttribute(attribute);
	}
	
	/**
	 * Returns a given attribute of the Component
	 * @param attributeName the name of the specified attribute
	 * @return the attribute if it exists, otherwise null
	 */
	public Object getAttribute(String attributeName){
		setChanged(true);
		return super.getAttribute(attributeName);
	}
	
	/**
	 * Adds a attribute to the Component
	 * @param attribute The type of attribute to be added
	 * @param value the value of the attribute
	 */
	public void addAttribute(Attribute attribute, Object value){
		setChanged(true);
		super.addAttribute(attribute, value);
	}

	/**
	 * Adds a attribute to the Component
	 * @param attributeName The name for the attribute
	 * @param value the value of the attribute
	 */
	public void addAttribute(String attributeName, Object value){
		setChanged(true);
		super.addAttribute(attributeName, value);
	}
	
	/**
	 * Returns a given attribute of the Component as a ContainerAttribute if possible
	 * @param attribute the type of attribute
	 * @return the attribute if it exists and is a ContainerAttribute, otherwise null
	 */
	public ContainerAttribute getContainerAttribute(Attribute attribute){
		setChanged(true);
		return super.getContainerAttribute(attribute);
	}
	
	/**
	 * Returns a given attribute of the Component as a ContainerAttribute if possible
	 * @param attributeName the name of the attribute
	 * @return the attribute if it exists and is a ContainerAttribute, otherwise null
	 */
	public ContainerAttribute getContainerAttribute(String attributeName){
		setChanged(true);
		return super.getContainerAttribute(attributeName);
	}
	
	/**
	 * Returns a given attribute of the Component, checking if the value of the attribute is a MenuInstance parameter
	 * @param name the name of the attribute
	 * @param instance The menuInstance whose paramters will be checked
	 * @return if a parameter
	 */
	public Object getParameteredAttribute(String name, MenuInstance instance){
		setChanged(true);
		return super.getParameteredAttribute(name, instance);
	}
	
	//--------------------------Methods for reserved attributes--------------------------
	/**
	 * Returns the type of the Component
	 * @return
	 */
	public String getType(){
		setChanged(true);
		return super.getType();
	}
	
	/**
	 * Sets the type of the Component
	 * @param type
	 */
	public void setType(String type){
		setChanged(true);
		super.setType(type);
	}
	
	/**
	 * Returns the tag of the Component
	 * @return
	 */
	public String getTag(){
		setChanged(true);
		return super.getTag();
	}
	
	/**
	 * Sets the tag of the Component
	 * @param tag
	 */
	public void setTag(String tag){
		setChanged(true);
		super.setTag(tag);
	}
	
	/**
	 * Returns the Lore of the Component
	 * @return
	 */
	public List<String> getLore(){
		setChanged(true);
		return super.getLore();
	}

	/**
	 * Sets the Lore of the Component
	 * @param lore
	 */
	public void setLore(List<String> lore){
		setChanged(true);
		super.setLore(lore);
	}
	
	/**
	 * Returns an action
	 * @param type
	 * @return
	 */
	public ContainerAttribute getAction(String type){
		setChanged(true);
		return super.getAction(type);
	}
	
	/**
	 * Adds action tags to a given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 */
	public void addAction(String type, List<Integer> tags){
		setChanged(true);
		super.addAction(type, tags);
	}
	
	/**
	 * Adds action tags and permission to the given interaction
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param permissions the permission to add
	 */
	public void addAction(String type, List<Integer> tags, List<String> permissions){
		setChanged(true);
		super.addAction(type, tags, permissions);
	}
	
	/**
	 * Adds action tags and commands to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 */
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender){
		setChanged(true);
		super.addAction(type, tags, commands, commandSender);
	}
	
	/**
	 * Adds action tags, commands, and permissions to the given interaction.
	 * @param type the type of interaction
	 * @param tags the action tags to add
	 * @param commands the commands to add
	 * @param commandSender the Entity which executes the commands
	 * @param permissions the permission to add
	 */
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender, List<String> permissions){
		setChanged(true);
		super.addAction(type, tags, commands, commandSender, permissions);
	}
	
	
	//--------------------------Methods for all components--------------------------
	/**
	 * Returns a Map of all Components of the Menu
	 * @return a Map of all Components of the Menu
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Component> getComponents(){
		if (!this.hasAttribute("components")){
			setComponents(new HashMap<String, Component>());
		}
		Map<String, Component> components = (Map<String, Component>) this.getAttribute("components");
		setChanged(true);
		return components;
	}
	
	//--------------------------Methods for one component--------------------------
	/**
	 * Sets all of the components for the Menu.
	 * This removes all previous components of the menu and add all the new components
	 * @param components the new components for the Menu
	 */
	@Override
	public void setComponents(Map<String, Component> components){
		this.addAttribute("components", components);
		setChanged(true);
	}
	
	/**
	 * Returns a Component of the Menu
	 * @param componentName the name of the Component to be returned
	 * @return the specified Component
	 */
	@Override
	public Component getComponent(String componentName){
		Map<String, Component> components = getComponents();
		setChanged(true);
		return components.get(componentName);
	}
	
	/**
	 * Returns whether or not a given Component is part of the Menu
	 * @param component the given Component
	 * @return whether or not a given Component is part of the Menu
	 */
	@Override
	public boolean hasComponent(Component component){
		Map<String, Component> components = getComponents();
		return components.containsValue(component);
	}
	
	/**
	 * Returns whether or not a given Component is part of the Menu
	 * @param componentName the name of the given Component
	 * @return whether or not a given Component is part of the Menu
	 */
	@Override
	public boolean hasComponent(String componentName){
		Map<String, Component> components = getComponents();
		return components.containsKey(componentName);
	}
	
	/**
	 * Adds a component to the Menu
	 * @param component the Component to be added
	 */
	@Override
	public void addComponent(Component component){
		Map<String, Component> components = getComponents();
		components.put(component.getTag(), component);
		setChanged(true);
	}

	//--------------------------Methods for all renderers--------------------------
	/**
	 * Returns the list of Renderers that provide for all instances of the Menu
	 * @return the List of Renderers that provide for all instances of the Menu
	 */
	@Override
	public List<Renderer> getRenderers() {
		return new LinkedList<Renderer>(renderers.values());
	}

	/**
	 * Sets the list of Renderers that provide for all instances of the Menu
	 * @param renderers the list of Renderers that provide for all instances of the Menu
	 */
	@Override
	public void setRenderers(List<Renderer> renderers) {
		this.renderers.clear();
		for (Renderer renderer: renderers){
			this.addRenderer(renderer);
		}	
	}

	//--------------------------Methods for one renderer--------------------------
	/**
	 * Returns a renderer from the menu by name
	 * @param rendererName
	 * @return
	 */
	@Override
	public Renderer getRenderer(String rendererName) {
		return renderers.get(rendererName);
	}
	
	/**
	 * Checks if the menu has a given Renderer
	 * @param renderer the specified Renderer
	 * @return
	 */
	@Override
	public boolean hasRenderer(Renderer renderer) {
		return renderers.containsValue(renderer);
	}
	
	/**
	 * Checks if the menu has a given Renderer
	 * @param rendererName the name of the specified Renderer
	 * @return
	 */
	@Override
	public boolean hasRenderer(String rendererName) {
		return renderers.containsKey(rendererName);
	}
	
	/**
	 * Adds a Renderer to the menu.
	 * @param renderer the Renderer to be added to the Menu
	 */
	@Override
	public void addRenderer(Renderer renderer) {
		renderers.put(renderer.getName(), renderer);
	}

	/**
	 * Removes a Renderer from the menu.
	 * @param renderer the Renderer to be removed from the Menu
	 */
	@Override
	public void removeRenderer(Renderer renderer) {
		renderers.remove(renderer.getName());
	}
	
	/**
	 * Removes a Renderer from the menu.
	 * @param rendererName the name of the Renderer to be removed from the Menu
	 */
	@Override
	public void removeRenderer(String rendererName) {
		renderers.remove(rendererName);
	}
	
	//--------------------------Methods for special menu attributes--------------------------
	/**
	 * Returns the plugin that this menu belongs to
	 * @return the plugin that this menu belongs to
	 */
	@Override
	public String getPlugin() {
		Object type = this.getAttribute("plugin");
		return (type instanceof String) ? (String) type : "no-plugin";
	}

	/**
	 * Returns the name of the menu
	 * @return the name of the Menu
	 */
	@Override
	public String getName() {
		return getTag();
	}

	/**
	 * Returns the fileName of the Menu
	 * @return the fileName of the Menu
	 */
	@Override
	public String getFileName() {
		return (String) this.getAttribute(Attribute.FILENAME);
	}

	/**
	 * Sets the fileName for this Menu
	 * @param fileName the fileName for the Menu
	 */
	@Override
	public void setFileName(String fileName) {
		if (fileName.endsWith(".yml")){
			this.addAttribute(Attribute.FILENAME, fileName);
		} else{
			this.addAttribute(Attribute.FILENAME, fileName + ".yml");
		}
		setChanged(true);
	}
	
	//--------------------------Methods that are related to whether or not the menu needs to be saved--------------------------
	/**
	 * Returns whether or not this menu or any of its components or attributes have been changed
	 * @return true or false
	 */
	@Override
	public boolean hasChanged(){
		return hasChanged;
	}
	
	/**
	 * Sets whether or not the menu has changed. This determines whether or not it needs to be saved to file.
	 * @param hasChanged
	 */
	@Override
	public void setChanged(boolean hasChanged){
		this.hasChanged = hasChanged;
	}
	
}
