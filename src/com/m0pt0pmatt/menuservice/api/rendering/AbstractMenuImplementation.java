package com.m0pt0pmatt.menuservice.api.rendering;

import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuImplementation;

public abstract class AbstractMenuImplementation implements MenuImplementation{

	protected Menu menu;
	
	public AbstractMenuImplementation(Menu menu){
		this.menu = menu;
	}
	
	public Menu getMenu(){
		return menu;
	}
	
}
