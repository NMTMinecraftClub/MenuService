package com.m0pt0pmatt.menuservice.api;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A MenuInstance represents a single MenuImpletation for a given player.
 * 
 * A MenuInstance is rendered for one player. Although this is not required, it is ideal.
 * 
 * A MenuInstance has an implementation. The implementation changes based of the type of menu.
 * 
 * MenuInstances can be given variables. These variables can help the MenuImplementation create the Menu.
 * 
 * @author mbroomfield
 *
 */
public final class MenuInstance {

	private final Menu menu;
	private String name;
	private List<String> players;
	private Map<String, Object> parameters;
	private Map<String, Renderer> renderers;
	private Map<String, ActionListener> actionListeners;
	private Set<String> highlightedButtons;
	
	/**
	 * Creates a MenuInstance given a type and parameters
	 * @param menuType
	 * @param playerName 
	 * @param parameters
	 */
	public MenuInstance(Menu menu, String name, List<String> players, Map<String, Object> parameters, Map<String, Renderer> renderers, Map<String, ActionListener> actionListeners) {
		this.menu = menu;
		this.name = name;
		this.players = players;
		this.parameters = parameters;
		this.renderers = renderers;
		this.actionListeners = actionListeners;
		highlightedButtons = new HashSet<String>();
	}

	public boolean isHighlighted(String tag){
		return highlightedButtons.contains(tag);
	}
	
	public void highlightButton(String tag){
		highlightedButtons.add(tag);
		renderAll();
	}
	
	public void unhighlightButton(String tag){
		highlightedButtons.remove(tag);
		renderAll();
	}
	
	public Set<String> getHighlightedButtons(){
		return highlightedButtons;
	}
	
	public void unhighlightAllButtons(){
		highlightedButtons.clear();
		renderAll();
	}
	
	public void toggleHighlightedButton(String tag){
		if (isHighlighted(tag)){
			unhighlightButton(tag);
		} else{
			highlightButton(tag);
		}
	}
	
	public void toggleAndRemoveOtherHighlights(String tag){
		if (isHighlighted(tag)){
			unhighlightAllButtons();
		} else{
			unhighlightAllButtons();
			highlightButton(tag);
		}
	}
	
	public String getName() {
		return name;
	}

	public Menu getMenu() {
		return menu;
	}

	public List<String> getPlayers() {
		return players;
	}
	
	public boolean hasPlayer(String playerName){
		return players.contains(playerName);
	}

	/**
	 * Returns the Map of parameters for this MenuInstance
	 * @return
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * sets a Map of parameters for this MenuInstance
	 * @param parameters
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public Object getParameter(String name){
		return parameters.get(name);
	}
	
	public void addParameter(String name, Object parameter){
		parameters.put(name, parameter);
	}
	
	public boolean hasParameter(String parameter){
		return parameters.containsKey(parameter);
	}
	
	public void removeParameter(String parameter){
		parameters.remove(parameter);
	}
	
	public Map<String, Renderer> getRenderers() {
		return renderers;
	}
	
	public List<Renderer> getAllRenderers(){
		List<Renderer> allRenderers = new LinkedList<Renderer>();
		allRenderers.addAll(renderers.values());
		allRenderers.addAll(menu.getRenderers());
		return allRenderers;
	}

	public void setRenderers(Map<String, Renderer> renderers) {
		this.renderers = renderers;
	}
	
	public void addRenderer(Renderer renderer){
		renderers.put(renderer.getName(), renderer);
	}
	
	public void removeRenderer(Renderer renderer){
		renderers.remove(renderer);
	}
	
	public boolean hasRenderer(String rendererName){
		return renderers.containsKey(rendererName);
	}
	
	public Renderer getRenderer(String rendererName){
		return renderers.get(rendererName);
	}
	
	public Map<String, ActionListener> getActionListeners() {
		return actionListeners;
	}

	public void setActionListeners(Map<String, ActionListener> actionListeners) {
		this.actionListeners = actionListeners;
	}
	
	public void addActionListener(ActionListener actionListener){
		actionListeners.put(actionListener.getName(), actionListener);
	}
	
	public void removeActionListener(String listenerName){
		actionListeners.remove(listenerName);
	}
	
	public boolean hasActionListener(String listenerName){
		return actionListeners.containsKey(listenerName);
	}
	
	public ActionListener getActionListener(String listenerName){
		return actionListeners.get(listenerName);
	}
	
	public void renderAll(){
		for (Renderer renderer: menu.getRenderers()){
			renderer.renderAllPlayers(this);
		}
		for (Renderer renderer: renderers.values()){
			renderer.renderAllPlayers(this);
		}
	}
	
	public void renderAll(List<Renderer> renderers){
		for (Renderer renderer: renderers){
			renderer.renderAllPlayers(this);
		}
		renderAll();
	}
	
	public void renderPlayer(String playerName){
		for (Renderer renderer: menu.getRenderers()){
			renderer.renderPlayer(this, playerName);
		}
		for (Renderer renderer: renderers.values()){
			renderer.renderPlayer(this, playerName);
		}
	}
	
	public void renderPlayer(List<Renderer> renderers, String playerName){
		for (Renderer renderer: renderers){
			renderer.renderPlayer(this, playerName);
		}
		renderPlayer(playerName);
	}
	
	public void removePlayer(String playerName){
		players.remove(playerName);
		for (ActionListener listener: actionListeners.values()){
			listener.playerRemoved(this, playerName);
		}
		if (players.size() == 0){
			if(this.hasParameter("keepOnEmpty")){
				if ((Boolean)this.getParameter("keepOnEmpty") == true){
					return;
				}
			}
		}
	}
	
}
