package com.m0pt0pmatt.menuservice.api;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum ComponentType {

	MENU("menu"),
	BUTTON("button"),
	LABEL("label");
	
	private String type;
	
	private static final Map<String, ComponentType> componentTypes = new HashMap<String, ComponentType>();
	
	static {
        for(ComponentType s : EnumSet.allOf(ComponentType.class))
        	componentTypes.put(s.getType(), s);
	}
	
	private ComponentType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}
	
	public static ComponentType getComponentType(String type){
		return componentTypes.get(type);
	}
	
}
