package com.m0pt0pmatt.menuservice.api;

import java.util.UUID;

/**
 * A menu implementation is a view of a Menu object given by a Renderer.
 * @author Matthew
 *
 */
public interface MenuImplementation {

	public void addPlayer(UUID uuid);
	
	public void removePlayer(UUID uuid);
	
}
