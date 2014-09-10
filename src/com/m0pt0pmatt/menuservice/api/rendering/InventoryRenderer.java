package com.m0pt0pmatt.menuservice.api.rendering;

import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuImplementation;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * InventoryRenderer is a built in Renderer for MenuService.
 * InventoryRenderer shows MenuInstances via player Inventories.
 * 
 * @author mbroomfield
 *
 */
public class InventoryRenderer implements Renderer{
	
	private Plugin plugin;
	
	/**
	 * Creates the InventoryRenderer
	 * @param menuService
	 * @param plugin
	 */
	public InventoryRenderer(MenuService menuService, Plugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public MenuImplementation createImplementation(Menu menu) {
		return new InventoryMenuImplementation(menu, plugin);
	}

	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name/
	 * @return the name of the Renderer
	 */
	@Override
	public String getName() {
		return "inventoryRenderer";
	}
	
}
