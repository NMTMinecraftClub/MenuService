package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MainMenuButtons {
	
	
	MAIN_EDITMENU(0),
	MAIN_OPENMENU(1),
	MAIN_CLOSEMENU(2),
	MAIN_SAVEMENU(3),
	MAIN_SAVEALL(4),
	MAIN_RELOADMENU(5),
	MAIN_RELOADALL(6),
	MAIN_UNLOADMENU(7),
	MAIN_UNLOADALL(8),
	MAIN_MENUCLICKED(9),
	MAIN_LEFTBUTTON(10),
	MAIN_RIGHTBUTTON(11),
	MAIN_EXITBUTTON(12);
	
	private static final Map<Integer, MainMenuButtons> lookup = new HashMap<Integer, MainMenuButtons>();
	
	private int type;
	
	static {
          for(MainMenuButtons s : EnumSet.allOf(MainMenuButtons.class))
               lookup.put(s.getType(), s);
    }
	
	MainMenuButtons(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}

	public static MainMenuButtons getMenuType(int tag) {
		return lookup.get(tag);
	}
}
