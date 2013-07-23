package com.m0pt0pmatt.menuservice.api;

import java.util.List;
import java.util.Map;

/**
 * A Menu represents an abstract menu which can be rendered multiple ways to multiple players.
 * 
 * Menus are composed of Components. A Menu itself is also a Component.
 * Menus can be shown to multiple players. 
 * When a Menu is shown, the player actually sees a MenuInstance. This is one copy of the Menu
 * specifically for the player.
 * 
 * While a Menu will stay the same once it has been created, MenuInstances can change.
 * 
 * @author mbroomfield
 *
 */
public interface Menu extends Component{
	
	/**
	 * Returns a Map of all Components of the Menu
	 * @return
	 */
	public Map<String, Component> getComponents();
	
	/**
	 * Returns a Map of all attributes of the Menu
	 */
	public Map<String, Object> getAttributes();

	public List<Renderer> getRenderers();

	public void setRenderers(List<Renderer> renderers);
	
	public void addRenderer(Renderer renderer);
	
	public void removeRenderer(Renderer renderer);
	
	public boolean hasRenderer(Renderer renderer);
	
	public Renderer getRenderer(String rendererName);
	
	public String getPlugin();
	
	public String getName();
	
	public boolean equals(Object arg);
	
	public void addAction(String type, List<Integer> tags);
	
	public void addAction(String type, List<Integer> tags, List<String> permissions);
	
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender);
	
	public void addAction(String type, List<Integer> tags, List<String> commands, String commandSender, List<String> permissions);
	
	public boolean hasAction(String type);
	
	public ContainerAttribute getAction(String type);
	
}
