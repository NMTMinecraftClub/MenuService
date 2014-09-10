package com.m0pt0pmatt.menuservice.api;

import com.m0pt0pmatt.menuservice.api.rendering.InventoryMenuImplementation;

public enum MenuType {

	INVENTORY("inventory", InventoryMenuImplementation.class.asSubclass(MenuImplementation.class));
	
	private Class<? extends MenuImplementation> implementationClass;
	private String name;
	
	private MenuType(String name, Class<? extends MenuImplementation> implementationClass){
		this.name = name;
		this.implementationClass = implementationClass;
	}
	
	public String getName(){
		return name;
	}
	
	protected Class<? extends MenuImplementation> getImplementationClass(){
		return implementationClass;
	}
	
}
