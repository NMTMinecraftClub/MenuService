package com.m0pt0pmatt.menuservice.menumanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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

/**
 * The MainMenuRenderer is a custom Renderer for the Main Menu for the MenuManager
 * @author mbroomfield
 *
 */
@SuppressWarnings("unused")
public class MainMenuRenderer extends InventoryRenderer implements Renderer, Listener{
	
	/**
	 * Creates a MainMenuRenderer
	 * @param menuService
	 * @param plugin
	 */
	public MainMenuRenderer(MenuService menuService, MenuServicePlugin plugin) {
		super(menuService, plugin);
	}

	private int getIndex(int x, int y){
		return ((9 * y) + x);
	}
	
	/**
	 * Renders a MenuInstance for a given player
	 * @param menuInstance the MenuInstance to render
	 * @param playerName the name of the player
	 */
	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		super.renderPlayer(menuInstance, playerName);
		List<Menu> menus = getMenuService().getMenus();
		
		int y = 1;
		int x = 0;
		
		Inventory inv = (Inventory) menuInstance.getParameter("inventory");
		
		Map<Integer, Menu> menuSpots = new HashMap<Integer, Menu>();
		
		for (Menu menu: menus){
			
			if (!MenuManager.menus.containsValue(menu)){
				
				ItemStack item = new ItemStack(Material.WOOL);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(menu.getName());
				item.setItemMeta(meta);
				
				menuSpots.put(getIndex(x, y), menu);
				
				inv.setItem(getIndex(x, y), item);
				
				x++;
				if (x == 9){
					x = 0;
					y++;
				}
			}
			
		}
		
		menuInstance.addParameter("menuSpots", menuSpots);
		getPlayers().put(playerName, menuInstance);
		
	}
	
	/**
	 * Renders the MenuInstance for all players who are currently viewing the MenuInstance
	 * @param menuInstance the Menuinstance to render
	 */
	@Override
	public void renderAllPlayers(MenuInstance instance) {
		for (String player: instance.getPlayers()){
			this.renderPlayer(instance, player);
		}
	}

	/**
	 * Returns the name of the Renderer.
	 * No two Renderers can share the same name
	 * @return the name of the Renderer
	 */
	@Override
	public String getName() {
		return "MenuService-MenuManager-MainMenu";
	}
	
	/**
	 * Handles when a player interacts with the Main Menu
	 * @param event
	 */
	@EventHandler
	@Override
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
		instance.addParameter(playerName + ":" + "slot", spot);
		switch (spot){
		case 0:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_EDITMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 1:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_OPENMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 2:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_CLOSEMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 3:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_LOADMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 4:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_SAVEMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 5:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_RELOADMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 6:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_UNLOADMENU.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		case 7:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_HELP.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
			return;
		default:
			instance.addParameter(playerName + ":" + "menuSpot", MenuType.MAIN_MENUCLICKED.getType());
			activateListeners(instance, MenuType.MAINMENU.getType(), playerName, "leftClick");
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
