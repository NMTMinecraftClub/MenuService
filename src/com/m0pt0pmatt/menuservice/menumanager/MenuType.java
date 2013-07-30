package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MenuType {

	MAINMENU(-1),
	
	MAIN_MENUCLICKED(10),
	MAIN_EDITMENU(12),
	MAIN_OPENMENU(13),
	MAIN_CLOSEMENU(14),
	MAIN_LOADMENU(15),
	MAIN_SAVEMENU(16),
	MAIN_RELOADMENU(17),
	MAIN_UNLOADMENU(18),
	MAIN_HELP(19),
	
	MENU_CREATEINSTANCE(20),
	MENU_CLOSEINSTANCE(21),
	MENU_OPENINSTANCE(22),
	MENU_REMOVEINSTANCE(23);
	
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
