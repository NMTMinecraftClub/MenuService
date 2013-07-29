package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MenuType {

	MAIN_MENUCLICKED(0),
	MAIN_EDITMENU(2),
	MAIN_OPENMENU(3),
	MAIN_CLOSEMENU(4),
	MAIN_LOADMENU(5),
	MAIN_SAVEMENU(6),
	MAIN_RELOADMENU(7),
	MAIN_UNLOADMENU(8),
	MAIN_HELP(9),
	
	MENU_CREATEINSTANCE(10),
	MENU_CLOSEINSTANCE(11),
	MENU_OPENINSTANCE(12),
	MENU_REMOVEINSTANCE(13);
	
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
