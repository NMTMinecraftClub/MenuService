package com.m0pt0pmatt.menuservice.api;

/**
 * An Action object represents an instance of an action being invoked by a player.
 * 
 * Every Component can have a list of action tags. When activated, corresponding Actions are invoked and can be caught by registered ActionListeners.
 * Actions are stored in configuration files as integers. However, each plugin has their own set of actions. 
 * This way multiple Components can share actions in a plugin, but other plugins may not listen for the action.
 * 
 * Therefore, an Action has an integer tag and a plugin name.
 * 
 * An Action also has a reference to the name of the player who invoked the Action.
 * Actions are the main component of an ActionEvent.
 * An Action also needs to know which player activated it, so anything that catches an ActionEvent can act accordingly.
 * 
 * @author mbroomfield
 *
 */
public final class Action {
	
	//The name of the plugin
	private String plugin;
	
	//the integer tag
	private int actionTag;
	
	//the name of the player invoking the action
	private String playerName;
	
	private MenuInstance instance;
	private String interaction;
	
	/**
	 * Creates an Action object
	 * @param plugin the name of the plugin which this Action belongs to
	 * @param actionTag The integer tag for which action this is
	 * @param playerName The name of the player who is taking Action
	 * @param instance The MenuInstance for the Action
	 * @param interaction 
	 */
	public Action(String plugin, int actionTag, String playerName, MenuInstance instance, String interaction){
		this.plugin = plugin;
		this.actionTag = actionTag;
		this.playerName = playerName;
		this.instance = instance;
		this.interaction = interaction;
	}
	
	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	/**
	 * Returns the MenuInstance for this Action
	 * @return
	 */
	public MenuInstance getInstance() {
		return instance;
	}


	/**
	 * Returns the name of the plugin which this Action belongs to
	 * @return the name of the plugin which this Action belongs to
	 */
	public String getPlugin(){
		return plugin;
	}
	
	/**
	 * Returns The integer tag for which action this is
	 * @return The integer tag for which action this is
	 */
	public int getTag(){
		return actionTag;
	}
	
	/**
	 * Returns The name of the player who is taking Action
	 * @return The name of the player who is taking Action
	 */
	public String getPlayerName(){
		return playerName;
	}
	
}
