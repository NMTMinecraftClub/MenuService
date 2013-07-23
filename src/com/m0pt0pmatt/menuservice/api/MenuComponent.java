package com.m0pt0pmatt.menuservice.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;

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
	
	public MenuComponent(){
		super();
		this.addAttribute("components", new HashMap<String, Component>());
		renderers = new HashMap<String, Renderer>();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Component> getComponents(){
		if (!this.hasAttribute("components")){
			return null;
		}
		
		Map<String, Component> components = null;
		try{
			components = (Map<String, Component>) this.getAttribute("components");
		} catch(ClassCastException e){
			return null;
		}
			
		return components;
		
	}
	
	public void setComponents(Map<String, Component> components){
		this.addAttribute("components", components);
	}
	
	public boolean hasComponent(Component component){
		Map<String, Component> components = this.getComponents();
		if (components == null){
			return false;
		}
		
		return components.containsKey(component);
	}
	
	public void addComponent(Component component){
		Map<String, Component> components = this.getComponents();
		if (components == null){
			return;
		}
		
		components.put(component.getTag(), component);
	}

	public static Menu loadMenu(Plugin plugin, String fileName) {
	//	return YAMLBuilder.loadYAML(plugin, fileName);
		return null;
	}

	public void saveMenu(Plugin plugin, String fileName) {
		//YAMLBuilder.saveYAML(plugin, this, fileName);		
	}

	@Override
	public List<Renderer> getRenderers() {
		return new LinkedList<Renderer>(renderers.values());
	}

	@Override
	public void setRenderers(List<Renderer> renderers) {
		this.renderers.clear();
		for (Renderer renderer: renderers){
			this.addRenderer(renderer);
		}	
	}

	@Override
	public void addRenderer(Renderer renderer) {
		renderers.put(renderer.getName(), renderer);
	}

	@Override
	public void removeRenderer(Renderer renderer) {
		renderers.remove(renderer.getName());
	}

	@Override
	public boolean hasRenderer(Renderer renderer) {
		return renderers.containsKey(renderer);
	}

	@Override
	public Renderer getRenderer(String rendererName) {
		return renderers.get(rendererName);
	}

	@Override
	public String getPlugin() {
		Object type = this.getAttribute("plugin");
		return (type instanceof String) ? (String) type : "no-plugin";
	}

	@Override
	public String getName() {
		return getTag();
	}
	
	public boolean equals(Object arg){
		if (!(arg instanceof Menu)){
			return false;
		}
		Menu menu = (Menu) arg;
		if (this.getAttribute("plugin").equals(menu.getAttribute("plugin"))){
			if (this.getAttribute("tag").equals(menu.getAttribute("tag"))){
				return true;
			}
		}
		return false;
	}

	@Override
	public void addAction(String type, List<Integer> tags) {
		addAction(type, tags, new LinkedList<String>(), "<player>", new LinkedList<String>());
		
	}
	
	@Override
	public void addAction(String type, List<Integer> tags, List<String> permissions) {
		addAction(type, tags, new LinkedList<String>(), "<player>", permissions);
		
	}

	@Override
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender) {
		addAction(type, tags, commands, commandSender, new LinkedList<String>());
	}

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

	@Override
	public boolean hasAction(String type) {
		if (!this.hasAttribute("actions")){
			return false;
		}
		
		ContainerAttribute actions = this.getConatinerAttribute("actions");
		if (actions == null){
			return false;
		}
		
		if (actions.hasAttribute(type)){
			return true;
		}
		
		return false;
	}

	@Override
	public ContainerAttribute getAction(String type) {
		if (!this.hasAttribute("actions")){
			return null;
		}
		
		ContainerAttribute actions = this.getConatinerAttribute("actions");
		if (actions == null){
			return null;
		}
		
		if (actions.hasAttribute(type)){
			return (ContainerAttribute) actions.getAttribute(type);
		}
		
		return null;
	}

	

		
	
}
