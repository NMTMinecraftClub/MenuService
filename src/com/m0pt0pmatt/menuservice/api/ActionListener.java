package com.m0pt0pmatt.menuservice.api;

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
	 * @param event
	 */
	public void handleAction(ActionEvent event);
	
	public void playerAdded(MenuInstance instance, String playerName);
	
	public void playerRemoved(MenuInstance instance, String playerName);
	
	public void playerCountZero(MenuInstance instance, String playerName);

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
