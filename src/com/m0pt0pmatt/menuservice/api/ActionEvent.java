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
public final class ActionEvent {
	
	//the integer tag
	private int actionTag;
	
	//the name of the player invoking the action
	private String playerName;
	
	//The type of interaction (leftClick, rightClick, etc)
	private String interaction;
	
	/**
	 * Creates an Action object
	 * @param plugin the name of the plugin which this Action belongs to
	 * @param actionTag The integer tag for which action this is
	 * @param playerName The name of the player who is taking Action
	 * @param instance The MenuInstance for the Action
	 * @param interaction 
	 */
	public ActionEvent(int actionTag, String playerName, String interaction){
		this.actionTag = actionTag;
		this.playerName = playerName;
		this.interaction = interaction;
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
	
	/**
	 * Returns the type of interaction for this action (leftClick, rightClick, etc)
	 * @return the type of interaction for this action (leftClick, rightClick, etc)
	 */
	public String getInteraction() {
		return interaction;
	}
	
}
