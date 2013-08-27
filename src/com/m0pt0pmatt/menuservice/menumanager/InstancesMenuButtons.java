package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum InstancesMenuButtons {
	
	MENU_EDITINSTANCE(0),
	MENU_OPENINSTANCE(1),
	MENU_CLOSEINSTANCE(2),
	MENU_CLOSEALL(3),
	MENU_REMOVEINSTANCE(4),
	MENU_REMOVEALL(5),
	MENU_INSTANCECLICKED(6),
	MENU_LEFTBUTTON(7),
	MENU_RIGHTBUTTON(8),
	MENU_BACKBUTTON(9);
	
	private static final Map<Integer, InstancesMenuButtons> lookup = new HashMap<Integer, InstancesMenuButtons>();
	
	private int type;
	
	static {
          for(InstancesMenuButtons s : EnumSet.allOf(InstancesMenuButtons.class))
               lookup.put(s.getType(), s);
    }
	
	InstancesMenuButtons(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}

	public static InstancesMenuButtons getMenuType(int tag) {
		return lookup.get(tag);
	}
}
