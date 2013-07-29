package com.m0pt0pmatt.menuservice.menumanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.Action;
import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * The MainMenuRenderer is a custom Renderer for the Main Menu for the MenuManager
 * @author mbroomfield
 *
 */
public class MainMenuRenderer extends AbstractRenderer implements Renderer, Listener{

	/**
	 * The MenuService for the Server
	 */
	private MenuService menuService;
	
	/**
	 * Creates a MainMenuRenderer
	 * @param menuService
	 * @param plugin
	 */
	public MainMenuRenderer(MenuService menuService, Plugin plugin) {
		super(menuService, plugin);
		this.menuService = menuService;
	}

	/**
	 * Renders a MenuInstance for a given player
	 * @param menuInstance the MenuInstance to render
	 * @param playerName the name of the player
	 */
	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		List<Menu> menus = menuService.getMenus();
		
		Inventory inv = (Inventory) menuInstance.getParameter("inventory");
		
		Player player = Bukkit.getPlayer(playerName);
		
		Map<Integer, Menu> menuSpots = new HashMap<Integer, Menu>();
		
		for (Menu menu: menus){
			
			player.sendMessage(menu.getName());
			
			ItemStack item = new ItemStack(Material.WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(menu.getName());
			item.setItemMeta(meta);
			
			menuSpots.put(inv.firstEmpty(), menu);
			
			inv.addItem(item);
			
		}
		
		menuInstance.addParameter("menuSpots", menuSpots);
		getPlayers().put(playerName, menuInstance);
		
		player.openInventory(inv);
		
	}

	/**
	 * Renders the MenuInstance for all players who are currently viewing the MenuInstance
	 * @param menuInstance the MenuInstance to render
	 */
	@Override
	public void renderAllPlayers(MenuInstance menuInstance) {}

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
	 * Closes whatever MenuInstance a player is viewing, assuming that the Renderer is providing for that player
	 * @param playerName the name of the player
	 */
	@Override
	public void closeMenu(String playerName) {}
	
	/**
	 * Closes the Menu when the player closes the Inventory
	 * @param event
	 */
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event){
		
		//get the playerName
		String playerName = event.getPlayer().getName();
		
		//check if the player was viewing the menu
		if (!getPlayers().containsKey(playerName)){
			return;
		}
		
		//remove the player
		getPlayers().remove(playerName);
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
		if (!getPlayers().containsKey(playerName)){
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
			activateListeners(instance, MenuType.MAIN_EDITMENU.getType(), playerName, "leftClick");
			return;
		case 1:
			activateListeners(instance, MenuType.MAIN_OPENMENU.getType(), playerName, "leftClick");
			return;
		case 2:
			activateListeners(instance, MenuType.MAIN_CLOSEMENU.getType(), playerName, "leftClick");
			return;
		case 3:
			activateListeners(instance, MenuType.MAIN_LOADMENU.getType(), playerName, "leftClick");
			return;
		case 4:
			activateListeners(instance, MenuType.MAIN_SAVEMENU.getType(), playerName, "leftClick");
			return;
		case 5:
			activateListeners(instance, MenuType.MAIN_RELOADMENU.getType(), playerName, "leftClick");
			return;
		case 6:
			activateListeners(instance, MenuType.MAIN_UNLOADMENU.getType(), playerName, "leftClick");
			return;
		case 7:
			activateListeners(instance, MenuType.MAIN_HELP.getType(), playerName, "leftClick");
			return;
		default:
			instance.addParameter("menuSpot", spot);
			activateListeners(instance, MenuType.MAIN_MENUCLICKED.getType(), playerName, "leftClick");
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
