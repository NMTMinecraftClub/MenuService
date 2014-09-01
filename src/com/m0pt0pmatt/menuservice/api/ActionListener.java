package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;



/**
 * An ActionListener is the interface for catching user input from a Menu.
 * ActionListeners need to provide a name and the plugin they belong to.
 * 
 * The handleAction event is called when an action is activated by a Component from a Menu that the ActionListener is registered to
 * ActionListeners can be registered through the MenuService.
 * Example: menuService.addActionListener(ActionListener actionListener, Menu menu);
 * 
 * It's recommended that developers have different private methods for different actions.
 * 
 * @author mbroomfield
 *
 */
public interface ActionListener {
	
	/**
	 * Executed when an action is activated.
	 */
	public void handleAction(Action action, String playerName, Menu menu, Component component);
	
	/**
	 * Executed when a player is added to the MenuInstance
	 * @param playerName the player who was added to the MenuInstance
	 */
	public void playerAdded(UUID uuid);
	
	/**
	 * Executed when a player is removed from a MenuInstance
	 * @param playerName the player who was removed from the MenuInstance
	 */
	public void playerRemoved(UUID uuid);
	
	/**
	 * Executed when the last player viewing the MenuInstance has left the menu
	 * @param instance the MenuInstance
	 * @param lastPlayerName the last player to view the MenuInstance
	 */
	public void playerCountZero(UUID uuid);

	/**
	 * Returns the name of the ActionListener
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns the plugin which the ActionListener belongs to
	 * @return
	 */
	public String getPlugin();
	
}
