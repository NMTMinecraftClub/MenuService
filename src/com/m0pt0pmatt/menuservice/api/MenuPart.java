package com.m0pt0pmatt.menuservice.api;

import java.util.List;

import com.m0pt0pmatt.menuservice.api.rendering.Renderer;

/**
 * A MenuPart is a collection of components and a means to interact with them.
 * @author Matthew
 *
 */
public final class MenuPart {

	private Renderer renderer;
	private List<Component> components;
	private ActionListener listener;
	private String name;
	
	public MenuPart(String name, List<Component> components){
		this.name = name;
		this.components = components;
		
		listener = null;
		renderer = null;
	}
	
	public boolean hasRenderer() {
		if (renderer == null){
			return false;
		}
		return true;
	}
	public Renderer getRenderer() {
		return renderer;
	}
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	public List<Component> getComponents() {
		return components;
	}
	public void setComponents(List<Component> components) {
		this.components = components;
	}
	public ActionListener getListener() {
		return listener;
	}
	public void setListener(ActionListener listener) {
		this.listener = listener;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean hasListener() {
		if (listener == null){
			return false;
		}
		return true;
	}
	
}
