package com.m0pt0pmatt.menuservice.api.rendering;

import com.m0pt0pmatt.menuservice.api.Component;

/**
 * Any object which is a RenderableComponent can be drawn by a Renderer.
 * This is useful for dynamic menus which may want to create components on the fly.
 * @author Matthew
 *
 */
public interface RenderableComponent {

	public Component toComponent();
	
}
