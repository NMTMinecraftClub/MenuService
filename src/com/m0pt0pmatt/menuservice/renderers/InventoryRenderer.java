package com.m0pt0pmatt.menuservice.renderers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.m0pt0pmatt.menuservice.Message;
import com.m0pt0pmatt.menuservice.Logger;
import com.m0pt0pmatt.menuservice.MenuServicePlugin;
import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.Action;
import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.attributes.Attribute;
import com.m0pt0pmatt.menuservice.api.attributes.ContainerAttribute;

/**
 * InventoryRenderer is a built in Renderer for MenuService.
 * InventoryRenderer shows MenuInstances via player Inventories.
 * 
 * @author mbroomfield
 *
 */
public class InventoryRenderer extends AbstractRenderer implements Listener{
	
	/**
	 * Creates the InventoryRenderer
	 * @param menuService
	 * @param plugin
	 */
	public InventoryRenderer(MenuService menuService, MenuServicePlugin plugin){
		super(menuService);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Creates an Inventory and stores it as a parameter for the MenuInstance
	 * @param instance the MenuInstance to create the Inventory for
	 * @return the Inventory created
	 */
	private Inventory createInventory(MenuInstance instance){
				
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCE, null);
			Logger.log(2, Level.SEVERE, Message.CANTCREATEINVENTORY, null);
			return null;
		}
		
		Menu menu = instance.getMenu();
		if (menu == null){
			Logger.log(2, Level.SEVERE, Message.EMPTYMENUFORINSTANCE, instance.getName());
			Logger.log(2, Level.SEVERE, Message.CANTCREATEINVENTORY, instance.getName());
			return null;
		}
		
		//set the inventory title
		String title = null;
		int size;
		if (menu.hasAttribute(Attribute.TITLE)){
			title = (String) menu.getAttribute(Attribute.TITLE);
			if (title == null){
				if (menu.getTag() != null){
					title = menu.getTag();
				}
				else{
					title = " ";
				}
			}
		}
		
		//set the inventory size
		if (menu.hasAttribute(Attribute.SIZE)){
			size = ((Integer) menu.getAttribute(Attribute.SIZE) * 9);
		}
		else{
			size = 9 * 6;
		}
		
		//create the inventory
		if (title.length() > 32){
			title = title.substring(0, 31);
		}
		Inventory inventory = Bukkit.createInventory(null, size, title);
		if (inventory == null){
			Logger.log(2, Level.SEVERE, Message.CANTCREATEINVENTORY, instance.getName());
			return null;
		}
		
		//setup the item map if it isn't already specified
		if (!instance.hasParameter("itemMap")){
			instance.addParameter("itemMap", new HashMap<Integer, String>());
		}
		
		//add components
		for (Component component: menu.getComponents().values()){
			renderMenuComponent(inventory, component, instance);
		}
		
		//add the created inventory as a parameter
		if (instance.hasParameter("inventory")){
			instance.removeParameter("inventory");
		}
		instance.addParameter("inventory", inventory);		
		return inventory;
	}
	
	@SuppressWarnings("unchecked")
	private void renderMenuComponent(Inventory inventory, Component component, MenuInstance instance){
		
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
		if (meta.hasLore()){
			meta.getLore().clear();
		}
		List<String> lore = component.getLore();
		List<String> newLore = new LinkedList<String>();
		if (lore != null){
			for (String l: lore){
				l = ChatColor.RESET.toString() + ChatColor.GRAY.toString() + l;
				newLore.add(l);
			}
		}
		meta.setLore(newLore);
		
		//highlight the item if needed
		if (instance.isHighlighted(component.getTag())){
			meta.addEnchant(Enchantment.WATER_WORKER, 1, true);
		} else{
			meta.removeEnchant(Enchantment.WATER_WORKER);
		}
		
		//set the metadata
		item.setItemMeta(meta);
		
		//set the location of the item
		int spot = getSpot(x, y, inventory);
		if (spot != -1){
			inventory.setItem(spot, item);
		}
		
		//set the location in the item map
		((Map<Integer, String>)(instance.getParameters().get("itemMap"))).put(spot, component.getTag());
			
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
	 * Renders a MenuInstance for a given player
	 * @param menuInstance the MenuInstance to render
	 * @param playerName the name of the player
	 */
	@Override
	public void renderPlayer(MenuInstance instance, String playerName) {
		
		if (instance == null){
			Logger.log(2, Level.SEVERE, Message.NULLMENUINSTANCE, this.getName());
			Logger.log(2, Level.SEVERE, Message.CANTRENDERINSTANCEPLAYER, null);
			return;
		}
		
		if (playerName == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLAYERNAME, this.getName());
			Logger.log(2, Level.SEVERE, Message.CANTRENDERINSTANCEPLAYER, instance.getName());
			return;
		}
		
		Inventory inv = createInventory(instance);
		if (inv == null){
			Logger.log(2, Level.SEVERE, Message.NULLINVENTORY, this.getName());
			Logger.log(2, Level.SEVERE, Message.CANTRENDERINSTANCEPLAYER, instance.getName());
			return;
		}
		
		//open the inventory
		Bukkit.getPlayer(playerName).closeInventory();
		Bukkit.getPlayer(playerName).openInventory(inv);
		
		//add the player for bookkeeping
		this.getPlayers().put(playerName, instance);
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
	 * Closes whatever MenuInstance a player is viewing, assuming that the Renderer is providing for that player
	 * @param playerName the name of the player
	 */
	@Override
	public void closeMenu(String playerName) {
		
		if (playerName == null){
			Logger.log(2, Level.SEVERE, Message.NULLPLAYERNAME, this.getName());
			Logger.log(2, Level.SEVERE, Message.CANTCLOSEMENU, this.getName());
			return;
		}
				
		//get the MenuInstance
		MenuInstance instance = this.getPlayers().get(playerName);
		if (instance == null){
			return;
		}

		//get the player
		Player player = Bukkit.getPlayer(playerName);
		if (player == null){
			Logger.log(2, Level.SEVERE, Message.NOSUCHPLAYER, this.getName());
			Logger.log(2, Level.SEVERE, Message.CANTCLOSEMENU, this.getName());
			return;
		}
		
		//close the player's inventory
		player.closeInventory();
		
		//remove the player from the Renderer
		getPlayers().remove(playerName);
		
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
		if (getPlayers().containsKey(playerName)){
			
			//get the instance
			MenuInstance instance = getPlayers().get(playerName);
			
			if (instance == null){
				return;
			}
			
			//remove the player
			getPlayers().remove(playerName);
			getMenuService().closeMenuInstance(playerName);
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
		if (!getPlayers().containsKey(playerName)){
			return;
		}
		
		if (event.getRawSlot() >= 54 || event.getRawSlot() < 0){
			return;
		}
		
		//get the instance and the menu
		MenuInstance instance = getPlayers().get(playerName);
		if (instance == null){
			return;
		}
		
		Menu menu = instance.getMenu();
		if (menu == null){
			return;
		}
		
		instance.addParameter(playerName + ":" + "slot", event.getSlot());
		
		//get the item map
		@SuppressWarnings("unchecked")
		Map<Integer, String> itemMap = (Map<Integer, String>) instance.getParameters().get("itemMap");
		
		//get the component
		Component component = menu.getComponents().get(itemMap.get(event.getSlot()));
		if (component == null){
			event.setResult(org.bukkit.event.Event.Result.DENY);
	        event.setCancelled(true);
	        return;
		}
		
		//get the actions
		ContainerAttribute actions = component.getContainerAttribute("actions");
		if (actions != null){

			//run each action
			for (Object action: actions.getAttributes().values()){
				if (action instanceof ContainerAttribute){
					
					executeAction((ContainerAttribute) action, event, instance, component);
				}
			}
		}
		
		//cancel the clicking of the item
		event.setResult(org.bukkit.event.Event.Result.DENY);
        event.setCancelled(true);
	}

	
	/**
	 * Executes an Action on a given Component
	 * @param action the ContainerAttrubute that specifies the Action
	 * @param event the InventoryClickEvent
	 * @param instance the MenuInstance of the Action
	 * @param component the Component
	 */
	@SuppressWarnings({ "unchecked" })
	private void executeAction(ContainerAttribute action, InventoryClickEvent event, MenuInstance instance, Component component){
		
		Player player = (Player) event.getWhoClicked();
		
		//check if this action is correct for the player's interaction
		if (!isCorrectAction(event, action)){
			return;
		}
		
		//check if the player has permission to interact with the component
		if (!hasPermissions(player, component)){
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return;
		}
		
		//check if the player has permission to activate the action
		if (!hasPermissions(player, action)){
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return;
		}
		
		//get the list of action tags
		List<Integer> actionTags = null;
		try{
			actionTags = ((List<Integer>)action.getAttribute("tags"));
		} catch (ClassCastException e){
			return;
		}
		if (actionTags != null){
			
			//execute each tag for each ActionListener tied to the instance
			for (Integer tag: actionTags){
			
				for (ActionListener listener: instance.getActionListeners().values()){
					String plugin = instance.getMenu().getPlugin();
					listener.handleAction(new ActionEvent(new Action(plugin, tag, event.getWhoClicked().getName(), instance, action.getName())));
				}			
			}
		}
		
		//get the list of commands
		List<String> commands = null;
		try {
			commands = ((List<String>)action.getAttribute("commands"));
		} catch (ClassCastException e){
			return;
		}
		
		if (commands != null){
			
			//get the command sender
			String sender = (String) action.getAttribute("sender");
			CommandSender cSender = this.getCommandSender(player, sender);
			if (cSender == null){
				return;
			}
			
			//execute the string of commands
			for (String command: commands){
				
				//replace placeholders
				command = command.replaceAll("<player>", event.getWhoClicked().getName());
				command = command.replaceAll("<sender>", cSender.getName());

				//execute the command
				Bukkit.dispatchCommand(cSender, command);	
			}
		}
	}

	/**
	 * Checks if the player's interaction matches the interaction of the Action
	 * @param event the InventoryClickEvent
	 * @param action the ContainerAttribute that specifies the action
	 * @return
	 */
	private boolean isCorrectAction(InventoryClickEvent event, ContainerAttribute action){
		
		//check left click
		if (event.isLeftClick() && (action.getName().equalsIgnoreCase("leftClick"))){
			return true;
		} else if (event.isRightClick() && (action.getName().equalsIgnoreCase("rightClick"))){
			return true;
		}
		
		return false;
	}
	
}
