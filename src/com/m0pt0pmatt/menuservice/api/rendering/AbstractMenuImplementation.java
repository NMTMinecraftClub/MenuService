package com.m0pt0pmatt.menuservice.api.rendering;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.m0pt0pmatt.menuservice.api.MenuImplementation;

public class AbstractMenuImplementation implements MenuImplementation{

	private Set<UUID> players;
	
	public AbstractMenuImplementation(){
		players = new HashSet<UUID>();
	}
	
	@Override
	public void addPlayer(UUID uuid){
		players.add(uuid);
	}
	
	@Override
	public void removePlayer(UUID uuid){
		players.remove(uuid);
	}
	
}
