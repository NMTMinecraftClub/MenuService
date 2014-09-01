package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;

/**
 * A menu implementation is a view of a Menu object given by a Renderer.
 * @author Matthew
 *
 */
public interface MenuImplementation {
	
	public Menu getMenu();
	
	public void openMenu(UUID uuid);
	
	public void closeMenu(UUID uuid);
	
	public void update();
}
