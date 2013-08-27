package com.m0pt0pmatt.menuservice;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Keyword {

	MENUSERVICE("menuservice"),
	
	EDIT("edit"),
	OPEN("open"),
	CLOSE("close"),
	LIST("list"),
	LOAD("load"),
	SAVE("save"),
	RELOAD("reload"),
	UNLOAD("unload"),
	DELETE("delete"),
	BIND("bind"),
	UNBIND("unbind"),
	HELP("help"),
	
	MENU("menu"),
	MENUS("menus"),
	INSTANCE("instance"),
	INSTANCES("instances"),
	PLAYER("player"),
	PLAYERS("players"),
	MATERIAL("material"),
	MATERIALS("materials"),
	ITEMSTACK("item"),
	ITEMSTACKS("items"),
	ENTITY("entity"),
	ENTITIES("entities"),
	
	KEYWORD("keyword"),
	PLACEHOLDER("placeholder"),
	FILENAME("filename"),
	
	ALL("all"),
	SENDER("sender"), MENUMANAGER("menumanager");
	
	private final String command;
	private static final Map<String, Keyword> keywords = new HashMap<String, Keyword>();
	
	static {
        for(Keyword s : EnumSet.allOf(Keyword.class))
             keywords.put(s.getKeyword(), s);
	}
	
	private Keyword(String command){
		this.command = command;
	}

	public String getKeyword(){
		return command;
	}
	
	@Override
	public String toString(){
		return command;
	}
	
	public static boolean is(Keyword keyword, String string){
		if (string.equalsIgnoreCase(keyword.toString())){
			return true;
		}
		return false;
	}
	
	public static boolean is(String keyword){
		if (keywords.containsKey(keyword)){
			return true;
		}
		return false;
	}

	public static Keyword getKeyword(String string) {
		return keywords.get(string);
	}
}
