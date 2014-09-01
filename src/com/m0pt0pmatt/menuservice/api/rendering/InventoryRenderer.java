package com.m0pt0pmatt.menuservice.api.rendering;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.Attribute;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * InventoryRenderer is a built in Renderer for MenuService.
 * InventoryRenderer shows MenuInstances via player Inventories.
 * 
 * @author mbroomfield
 *
 */
public class InventoryRenderer implements Renderer{
	
	private Map<Menu, InventoryMenuImplementation> implementations;
	private Map<UUID, InventoryMenuImplementation> players;
	private Plugin plugin;
	
	/**
	 * Creates the InventoryRenderer
	 * @param menuService
	 * @param plugin
	 */
	public InventoryRenderer(MenuService menuService, Plugin plugin){
		implementations = new HashMap<Menu, InventoryMenuImplementation>();
		players = new HashMap<UUID, InventoryMenuImplementation>();
		this.plugin = plugin;
	}
	
	
	
	@Override
	public void drawMenu(Menu menu, UUID uuid) {
		
		if (!implementations.containsKey(menu)){
			for (Component p: menu.getComponents()){
				this.drawComponent(menu, p);
			}
		}
		players.put(uuid, implementations.get(menu));
	}

	@Override
	public void undrawMenu(UUID uuid) {
		players.remove(uuid);
	}
	
	public void drawComponent(Menu menu, Component p) {
		if (!implementations.containsKey(menu)){
			implementations.put(menu, new InventoryMenuImplementation(menu, plugin));
		}
		
		InventoryMenuImplementation implementation = implementations.get(menu);
		
		this.renderComponent(implementation, p);	
	}
	
	private void renderComponent(InventoryMenuImplementation implementation, Component component){
		
		Inventory inventory = implementation.getInventory();
		
		//find the position, relative to the current position of the renderer
		int x = -1;
		int y = -1;
		if (component.hasAttribute(Attribute.X)){
			x = (Integer) component.getAttribute(Attribute.X);
		}
		if (component.hasAttribute(Attribute.Y)){
			y = (Integer) component.getAttribute(Attribute.Y);
		}
		
		//create the itemstack
		ItemStack item;
		
		if (component.hasAttribute(Attribute.ITEM)){
			item = (ItemStack) component.getAttribute(Attribute.ITEM);
		} 
		
		else{
			item = new ItemStack(Material.WOOL);
		}
		
		//make meta changes
		ItemMeta meta = item.getItemMeta();
		
		//set the metadata
		item.setItemMeta(meta);
		
		//set the location of the item
		int spot = getSpot(x, y, inventory);
		if (spot != -1){
			inventory.setItem(spot, item);
		}
		
		//set the location in the item map
		implementation.setLocation(spot, component);			
	}

	/**
	 * Returns the spot in the inventory for an item
	 * @param x
	 * @param y
	 * @return
	 */
	private int getSpot(int x, int y, Inventory inv){
		
		if (inv.firstEmpty() == -1){
			return -1;
		}
		
		if (x < 0 && y < 0){
			x = inv.firstEmpty() % 9;
			y = inv.firstEmpty() / 9;
			return (9 * y) + x;
		}
		
		else if (x < 0){			
			for (int i = 0; i < 9; i++){
				if (inv.getItem((9*y) + i) == null){
					x = i;
					break;
				}
			}
		}
		
		else if (y < 0){
			for (int i = 0; i < inv.getSize() / 9; i++){
				if (inv.getItem((9*i) + x) == null){
					y = i;
					break;
				}
			}
		}
		
		if (inv.getItem((9*y) + x) != null){
			return -1;
		}
		
		if ((9 * y) + x >= inv.getSize()){
			return -1;
		}
		
		return (9 * y) + x;
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
