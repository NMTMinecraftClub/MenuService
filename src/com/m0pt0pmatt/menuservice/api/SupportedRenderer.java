package com.m0pt0pmatt.menuservice.api;

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
