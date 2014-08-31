package com.m0pt0pmatt.menuservice.api.rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuPart;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.actions.Action;
import com.m0pt0pmatt.menuservice.api.actions.ActionListener;
import com.m0pt0pmatt.menuservice.api.actions.DefaultAction;
import com.m0pt0pmatt.menuservice.api.attributes.Attribute;
import com.m0pt0pmatt.menuservice.api.attributes.ContainerAttribute;

/**
 * InventoryRenderer is a built in Renderer for MenuService.
 * InventoryRenderer shows MenuInstances via player Inventories.
 * 
 * @author mbroomfield
 *
 */
public class InventoryRenderer implements Renderer, Listener{
	
	private Map<Menu, InventoryImplementation> implementations;
	private Map<UUID, InventoryImplementation> players;
	
	private InventoryRenderer(){
		implementations = new HashMap<Menu, InventoryImplementation>();
		players = new HashMap<UUID, InventoryImplementation>();
	}
	
	/**
	 * Creates the InventoryRenderer
	 * @param menuService
	 * @param plugin
	 */
	public InventoryRenderer(MenuService menuService, Plugin plugin){
		this();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void draw(Menu menu, MenuPart p) {
		if (!implementations.containsKey(menu)){
			implementations.put(menu, new InventoryImplementation(menu));
		}
		
		InventoryImplementation implementation = implementations.get(menu);
		
		this.renderMenuPart(implementation, p);
				
	}

	@Override
	public void undraw(Menu menu, MenuPart p) {
	}
	
	@Override
	public void openMenu(Menu menu, UUID playerName) {
		
		if (!implementations.containsKey(menu)){
			for (MenuPart p: menu.getParts()){
				this.draw(menu, p);
			}
		}
		
		Player player = Bukkit.getPlayer(playerName);
		
		
		//open the inventory
		player.closeInventory();
		player.openInventory(implementations.get(menu).getInventory());
		players.put(playerName, implementations.get(menu));
	}

	@Override
	public void closeMenu(Menu menu, UUID playerName) {
		Player player = Bukkit.getPlayer(playerName);
		player.closeInventory();
		players.remove(playerName);
	}
	
	private void renderMenuPart(InventoryImplementation implementation, MenuPart part){
		for (Component c: part.getComponents()){
			renderComponent(implementation, c);
		}
	}
	
	private void renderComponent(InventoryImplementation implementation, Component component){
		
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
		
		//create the correct material
		Material material;
		
		if (component.hasAttribute(Attribute.ITEM)){
			item = (ItemStack) component.getAttribute(Attribute.ITEM);
		} 
		
		else{
			if (component.hasAttribute(Attribute.MATERIAL_ID)){
				material = Material.getMaterial((Integer) component.getAttribute(Attribute.MATERIAL_ID));
				item = new ItemStack(material);
			}
			else if (component.hasAttribute(Attribute.MATERIAL_NAME)){
				material = Material.getMaterial((String) component.getAttribute(Attribute.MATERIAL_NAME));
				item = new ItemStack(material);
			}
			else{
				material = Material.WOOL;
				item = new ItemStack(Material.WOOL);
			}
			
		}
		
		//make meta changes
		ItemMeta meta = item.getItemMeta();

		//change the title of the item
		String text = component.getTag();
		if (component.hasAttribute(Attribute.TEXT)){
			text = (String) component.getAttribute(Attribute.TEXT);
		}
		meta.setDisplayName(ChatColor.RESET.toString() + ChatColor.WHITE.toString() + text);
		
		//set the lore
//		if (meta.hasLore()){
//			meta.getLore().clear();
//		}
//		List<String> lore = component.getLore();
//		List<String> newLore = new LinkedList<String>();
//		if (lore != null){
//			for (String l: lore){
//				l = ChatColor.RESET.toString() + ChatColor.GRAY.toString() + l;
//				newLore.add(l);
//			}
//		}
//		meta.setLore(newLore);
		
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
		return "inventory";
	}
	
	/**
	 * Handles when a player closes an inventory.
	 * If the inventory was a menu, remove the player from the open menu hash.
	 * @param event The event
	 */
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event){
		
		String playerName = event.getPlayer().getName();
		
		//if the player was viewing a MenuInstance that's being provided for
		if (players.containsKey(playerName)){
			
			players.get(playerName).removePlayer(playerName);
			players.remove(playerName);
		}
		
	}
	
	/**
	 * Handles when a player clicks an item in an inventory.
	 * If the inventory was a menu, handles the menu accordingly
	 * @param event
	 */
	@EventHandler
	public void inventoryClick(InventoryClickEvent event){	
				
		//check if player was viewing a menu
		String playerName = event.getWhoClicked().getName();
		if (!players.containsKey(playerName)){
			return;
		}
		
		if (event.getRawSlot() >= 54 || event.getRawSlot() < 0){
			return;
		}
		
		//get the implementation
		InventoryImplementation implementation = players.get(playerName);
		
		//get the component
		Component component = implementation.getComponent(event.getSlot());
		if (component == null){
			event.setResult(org.bukkit.event.Event.Result.DENY);
	        event.setCancelled(true);
	        return;
		}
		
		executeAction(event, implementation, component);
		
		//cancel the clicking of the item
		event.setResult(org.bukkit.event.Event.Result.DENY);
        event.setCancelled(true);
	}

	
	/**
	 * Executes an Action on a given Component
	 * @param event the InventoryClickEvent
	 * @param instance the MenuInstance of the Action
	 * @param component the Component
	 */
	private void executeAction(InventoryClickEvent event, InventoryImplementation implementation, Component component){
		
		Player player = (Player) event.getWhoClicked();
		
		//get the action
		Action action = getCorrectAction(event);
		if (action == null) return;
		
		//get the list of action tags
		Set<Integer> tags = component.getActionTags(action);
		if (tags == null || tags.isEmpty()) return;
		
		//check if the player has permission to interact with the component
		if (!hasPermissions(player, component)){
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return;
		}
		
		//check if the player has permission to activate the action
		//if (!hasPermissions(player, action)){
		//	player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
		//	return;
		//}
		
		//execute each tag for each ActionListener tied to the instance
		for (Integer tag: tags){
			for (ActionListener listener: implementation.getActionListeners()){
				listener.handleAction(action, tag, event.getWhoClicked().getName(), component);
			}			
		}
		
	}

	private Action getCorrectAction(InventoryClickEvent event) {
		//check left click
		if (event.isLeftClick()){
			return DefaultAction.LEFT_CLICK;
		} 
		else if (event.isRightClick()){
			return DefaultAction.RIGHT_CLICK;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Checks if the player has permission to interact with the component
	 * @param player
	 * @param component
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean hasPermissions(Player player, Component component){
		if (component.hasAttribute("permissions")){
			List<String> permissions = null;
			try{
				permissions = (List<String>) component.getAttribute("permissions");
			} catch (ClassCastException e){
				return true;
			}
			if (permissions == null){
				return true;
			}
			for (String permission: permissions){
				if (!player.hasPermission(permission)){
					return false;
				}
			}

		}

		return true;
	}

	/**
	 * Checks if a player can activate a given action
	 * @param player
	 * @param action
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean hasPermissions(Player player, ContainerAttribute action){
		if (action.hasAttribute("permissions")){
			List<String> permissions = null;
			try{
				permissions = (List<String>) action.getAttribute("permissions");
			} catch (ClassCastException e){
				return true;
			}
			if (permissions == null){
				return true;
			}
			for (String permission: permissions){
				if (!player.hasPermission(permission)){
					return false;
				}
			}

		}

		return true;
	}

	/**
	 * Retrieves the CommandSender for the given placeholder
	 * @param player the player who interacted with the action
	 * @param sender the placeholder
	 * @return
	 */
	protected CommandSender getCommandSender(Player player, UUID sender){
		CommandSender cSender;
		if (sender == null){
			cSender = null;
		} else if (sender.equals("<server>")){
			cSender = Bukkit.getServer().getConsoleSender();
		} else if (sender.equals("<player>")){
			cSender = player;
		} else {
			cSender = Bukkit.getPlayer(sender);
		}
		if (cSender == null){
			cSender = player;
		}
		return cSender;
	}

	
	
}
