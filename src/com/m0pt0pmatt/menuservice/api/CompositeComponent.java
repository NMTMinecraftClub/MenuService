package com.m0pt0pmatt.menuservice.api;

import java.util.Set;

/**
 * A MenuPart is a collection of components and a means to interact with them.
 * @author Matthew
 *
 */
public final class CompositeComponent {

	private Set<Component> components;
	
	public CompositeComponent(Set<Component> components){
		this.components = components;
	}
	
	public Set<Component> getComponents() {
		return components;
	}
	public void setComponents(Set<Component> components) {
		this.components = components;
	}

	
}
