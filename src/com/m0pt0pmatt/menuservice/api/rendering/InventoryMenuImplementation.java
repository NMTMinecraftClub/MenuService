package com.m0pt0pmatt.menuservice.api.rendering;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.scheduler.BukkitRunnable;

import com.m0pt0pmatt.menuservice.MenuServicePlugin;
import com.m0pt0pmatt.menuservice.api.Action;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.ComponentAttribute;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuAttribute;

public class InventoryMenuImplementation extends AbstractMenuImplementation implements Listener{

	private Inventory inventory;
	
	private Map<Integer, Component> components;
	
	public InventoryMenuImplementation(Menu menu, Plugin plugin){
		
		super(menu);
		
		this.components = new HashMap<Integer, Component>();
		
		update();
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private void renderComponent(Inventory inventory, Component component){
				
		//find the position, relative to the current position of the renderer
		int x = -1;
		int y = -1;
		if (component.hasAttribute(ComponentAttribute.X)){
			x = (Integer) component.getAttribute(ComponentAttribute.X);
		}
		if (component.hasAttribute(ComponentAttribute.Y)){
			y = (Integer) component.getAttribute(ComponentAttribute.Y);
		}
		
		//create the itemstack
		ItemStack item;
		
		if (component.hasAttribute(ComponentAttribute.ITEM)){
			item = (ItemStack) component.getAttribute(ComponentAttribute.ITEM);
		} 
		
		else{
			item = new ItemStack(Material.WOOL);
		}
		
		//make meta changes
		ItemMeta meta = item.getItemMeta();
		
		if (component.hasAttribute(ComponentAttribute.TITLE)){
			meta.setDisplayName((String) component.getAttribute(ComponentAttribute.TITLE));
		}
		
		//set the metadata
		item.setItemMeta(meta);
		
		//set the location of the item
		int spot = getSpot(x, y, inventory);
		if (spot != -1){
			inventory.setItem(spot, item);
		}
		
		//set the location in the item map
		setLocation(spot, component);			
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

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setLocation(int spot, Component component) {
		components.put(spot, component);
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
	
	/**
	 * Handles when a player closes an inventory.
	 * If the inventory was a menu, remove the player from the open menu hash.
	 * @param event The event
	 */
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event){
		
		final UUID uuid = event.getPlayer().getUniqueId();
		
		//if the player was viewing a MenuInstance that's being provided for
		if (menu.getPlayers().containsKey(uuid)){
			
			if (menu.hasAttribute(MenuAttribute.CANBECLOSE)){
				Boolean canBeClosed = (Boolean) menu.getAttribute(MenuAttribute.CANBECLOSE);
				if (canBeClosed){
					menu.getPlayers().remove(uuid);
				}
				else{
					BukkitRunnable reset = new BukkitRunnable(){

						@Override
						public void run() {
							openMenu(uuid);
						}
						
					};
					
					reset.runTaskLater(MenuServicePlugin.plugin, 20);
				}			
			}
			else{
				menu.getPlayers().remove(uuid);
			}
			
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
		UUID uuid = event.getWhoClicked().getUniqueId();
		if (!menu.getPlayers().containsKey(uuid)){
			return;
		}
		
		if (event.getRawSlot() >= 54 || event.getRawSlot() < 0){
			return;
		}
		
		//get the component
		Component component = getComponent(event.getSlot());
		if (component == null){
			event.setResult(org.bukkit.event.Event.Result.DENY);
	        event.setCancelled(true);
	        return;
		}
		
		executeAction(event, component);
		
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
	private void executeAction(InventoryClickEvent event, Component component){
		
		Player player = (Player) event.getWhoClicked();
		
		//get the action
		Action action = getCorrectAction(event);
		if (action == null) return;
		
		//check if the player has permission to interact with the component
		if (!hasPermissions(player, component)){
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return;
		}
		
		//execute each tag for each ActionListener tied to the instance
		ActionListener listener = component.getListener();
		if (listener != null){
			listener.handleAction(action, event.getWhoClicked().getUniqueId(), menu, component);
		}	
		
	}
	
	private Action getCorrectAction(InventoryClickEvent event) {
		//check left click
		if (event.isLeftClick()){
			return Action.LEFT_CLICK;
		} 
		else if (event.isRightClick()){
			return Action.RIGHT_CLICK;
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

	@Override
	public void openMenu(UUID uuid) {
		Bukkit.getPlayer(uuid).openInventory(inventory);
	}

	@Override
	public void closeMenu(UUID uuid) {
		Bukkit.getPlayer(uuid).closeInventory();
	}

	@Override
	public void update() {
		
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
		int size = 1;
		
		if (menu.hasAttribute(MenuAttribute.SIZE)){
			size = (Integer) menu.getAttribute(MenuAttribute.SIZE);
		}
		for (Component c: menu.getComponents()){
			if (c.hasAttribute(ComponentAttribute.Y)){
				int y = (Integer) c.getAttribute(ComponentAttribute.Y);
				if (size < y) size = y;
			}
		}
		
		if (size > 6) size = 6;
		
		//create inventory
		inventory = Bukkit.createInventory(null, size * 9, title);
		
		for (Component c: menu.getComponents()){
			renderComponent(inventory, c);
		}
	}
}
