package com.m0pt0pmatt.menuservice.api;

/**
 * A Renderer will render (or present) a menu to a player.
 * 
 * A Renderer can either render a Menu or a MenuInstance.
 * When rendering a Menu, the Renderer must create a MenuInstance, add it to the Menu, render the MenuInstance, and return the MenuInstance.
 * 
 * To distinguish Renderers from each other, each Renderer must supply a name.
 * @author mbroomfield
 *
 */
public interface Renderer {
	
	/**
	 * Renders a MenuInstance for a given player
	 * @param menuInstance the MenuInstance to render
	 * @param playerName the name of the player
	 */
	public void renderPlayer(MenuInstance menuInstance, String playerName);
	
	/**
	 * Renders the MenuInstance for all players who are currently viewing the MenuInstance
	 * @param menuInstance the MenuInstance to render
	 */
	public void renderAllPlayers(MenuInstance menuInstance);
	
	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name/
	 * @return the name of the Renderer
	 */
	public String getName();
	
	/**
	 * Closes whatever MenuInstance a player is viewing, assuming that the Renderer is providing for that player
	 * @param playerName the name of the player
	 */
	public void closeMenu(String playerName);
	
	public void closeAll();
	
}
