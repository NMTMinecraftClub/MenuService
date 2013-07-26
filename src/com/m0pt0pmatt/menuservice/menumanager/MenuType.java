package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MenuType {

	MAINMENU(0),
	LISTMENU(1),
	EDITMENU(2),
	OPENMENU(3),
	CLOSEMENU(4),
	LOADMENU(5),
	SAVEMENU(6),
	RELOADMENU(7),
	UNLOADMENU(8),
	HELPMENU(9);
	
	private static final Map<Integer, MenuType> lookup = new HashMap<Integer, MenuType>();
	
	private int type;
	
	static {
          for(MenuType s : EnumSet.allOf(MenuType.class))
               lookup.put(s.getType(), s);
     }
	
	MenuType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}

	public static MenuType getMenuType(int tag) {
		return lookup.get(tag);
	}
}
