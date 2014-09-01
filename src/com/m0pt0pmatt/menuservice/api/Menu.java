package com.m0pt0pmatt.menuservice.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Menu is a model of an abstract user interface.
 * A Menu is made of Components, which are normally buttons and/or labels.
 * 
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 */
public final class Menu{

	//Each menu has a title
	private String title;
	
	//The set of components for the menu
	private Set<Component> components;
	
	//The size of the menu. (number of rows)
	private int size;
	//NOTE: size is NOT an abstract attribute, but specific for the inventory menu
	//		implementation. This should me moved
	
	public Menu(){
		components = new HashSet<Component>();
		size = 1;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<Component> getComponents() {
		return components;
	}


	public void setComponents(Set<Component> components) {
		this.components = components;
	}
	
	public boolean hasComponent(Component part) {
		return components.contains(part);
	}
	
	public void addComponent(Component part) {
		components.add(part);
	}

	public void removeComponent(Component part) {
		components.remove(part);
	}
	
	public void removeAllPartsWithTag(String tag){
		
		Iterator<Component> i = components.iterator();
		while(i.hasNext()){
			Component component = i.next();
			
			if (component.hasTag(tag)){
				i.remove();
			}
		}
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
}
