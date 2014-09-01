package com.m0pt0pmatt.menuservice.api;

/**
 * An enumeration of all Renderers currently supported by MenuService
 * 
 * @author Matthew
 * 
 */
public enum SupportedRenderer {
	InventoryRenderer("inventoryRenderer");
	
	private String name;
	
	private SupportedRenderer(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
}
