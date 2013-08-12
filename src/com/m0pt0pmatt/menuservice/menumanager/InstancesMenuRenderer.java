package com.m0pt0pmatt.menuservice.menumanager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.m0pt0pmatt.menuservice.MenuServicePlugin;
import com.m0pt0pmatt.menuservice.api.Action;
import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;
import com.m0pt0pmatt.menuservice.renderers.InventoryRenderer;

public class InstancesMenuRenderer extends InventoryRenderer implements Renderer, Listener{
	
	public InstancesMenuRenderer(MenuService menuService, MenuServicePlugin plugin) {
		super(menuService, plugin);
	}
	
	private int getIndex(int x, int y){
		return ((9 * y) + x);
	}

	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		super.renderPlayer(menuInstance, playerName);
		
		int x = 0;
		int y = 1;
						
		Inventory inv = (Inventory) menuInstance.getParameter("inventory");
		
		Menu menu = (Menu) menuInstance.getParameter("menu");
		
		Map<Integer, MenuInstance> instanceSpots = new HashMap<Integer, MenuInstance>();
		
		for (MenuInstance instance: this.getMenuService().getMenuInstances(menu)){
			
			ItemStack item = new ItemStack(Material.WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(instance.getName());
			item.setItemMeta(meta);
			
			instanceSpots.put(getIndex(x, y), instance);
			
			inv.setItem(getIndex(x, y), item);
			
			x++;
			if (x == 9){
				x = 0;
				y++;
			}
		}
		
		menuInstance.addParameter("instanceSpots", instanceSpots);
		getPlayers().put(playerName, menuInstance);
		
	}

	@Override
	public String getName() {
		return "MenuService-MenuManager-MenuMenu";
	}
	
	/**
	 * Handles when a player interacts with the Main Menu
	 * @param event
	 */
	@EventHandler
	public void inventoryClick(InventoryClickEvent event){	
		
		//get the playerName
		String playerName = event.getWhoClicked().getName();
		
		//check if the player was viewing the menu
		if (!(getPlayers().containsKey(playerName))){
			return;
		}
		
		//cancel the event
		event.setCancelled(true);
		
		//get the instance
		MenuInstance instance = getPlayers().get(playerName);
		
		//get the spot
		int spot = event.getSlot();
		switch (spot){
		case 0:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MENU_CREATEINSTANCE.getType());
			activateListeners(instance, MenuType.MENUMENU.getType(), playerName, "leftClick");
			return;
		case 1:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MENU_OPENINSTANCE.getType());
			activateListeners(instance, MenuType.MENUMENU.getType(), playerName, "leftClick");
			return;
		case 2:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MENU_CLOSEINSTANCE.getType());
			activateListeners(instance, MenuType.MENUMENU.getType(), playerName, "leftClick");
			return;
		case 3:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MENU_REMOVEINSTANCE.getType());
			activateListeners(instance, MenuType.MENUMENU.getType(), playerName, "leftClick");
			return;
		default:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MENU_INSTANCECLICKED.getType());
			activateListeners(instance, MenuType.MENUMENU.getType(), playerName, "leftClick");
		}

	}
	
	/**
	 * Activate the ActionListeners for the Instance
	 * @param instance
	 * @param tag
	 * @param playerName
	 * @param interaction
	 */
	private void activateListeners(MenuInstance instance, int tag, String playerName, String interaction){
		for (ActionListener listener: instance.getActionListeners().values()){
			ActionEvent event = new ActionEvent(new Action("MenuService", tag, playerName, instance, interaction));
			listener.handleAction(event);
		}
	}

}
