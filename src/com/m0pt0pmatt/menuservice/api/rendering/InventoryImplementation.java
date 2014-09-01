package com.m0pt0pmatt.menuservice.api.rendering;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Attribute;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuImplementation;

public class InventoryImplementation implements MenuImplementation{

	private Inventory inventory;
	
	private Menu menu;

	private Map<Integer, Component> components;
	
	public InventoryImplementation(Menu menu){
		
		this.menu = menu;
		this.components = new HashMap<Integer, Component>();
		
		//get the title
		String title = null;
		title = (String) menu.getTitle();
		if (title == null){
			title = " ";
		}
		//create the inventory
		if (title.length() > 32){
			title = title.substring(0, 31);
		}
		
		//determine size
		int size = menu.getSize();
		for (Component c: menu.getComponents()){
			if (c.hasAttribute(Attribute.Y)){
				int y = (Integer) c.getAttribute(Attribute.Y);
				if (size < y) size = y;
			}
		}
		
		if (size > 6) size = 6;
		
		//create inventory
		inventory = Bukkit.createInventory(null, size * 9, title);
		
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void removePlayer(UUID uuid){
		menu.removePlayer(uuid);
	}

	public void setLocation(int spot, Component component) {
		components.put(spot, component);
	}

	public Menu getMenu() {
		return menu;
	}

	public Component getComponent(int slot) {
		return components.get(slot);
	}

	public List<ActionListener> getActionListeners() {
		List<ActionListener> listeners = new LinkedList<ActionListener>();
		for (Component part: menu.getComponents()){
			if (part.hasListener()) listeners.add(part.getListener());
		}
		return listeners;
	}
}
