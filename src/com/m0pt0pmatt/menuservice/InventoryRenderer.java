package com.m0pt0pmatt.menuservice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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

import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.Action;
import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.ContainerAttribute;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Component;

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
	public InventoryRenderer(MenuService menuService, Plugin plugin){
		super(menuService, plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Creates an Inventory and stores it as a parameter for the MenuInstance
	 * @param instance the MenuInstance to create the Inventory for
	 * @return the Inventory created
	 */
	private Inventory createInventory(MenuInstance instance){
		Menu menu = instance.getMenu();	
		
		//set the inventory title
		String title = null;
		int size;
		if (menu.hasAttribute("title")){
			title = (String) menu.getParameteredAttribute("title", instance);
			if (title == null){
				if (menu.getTag() != null){
					title = menu.getTag();
				}
				else{
					title = "menu";
				}
			}
		}
		
		//set the inventory size
		if (menu.hasAttribute("size")){
			size = ((Integer) menu.getAttribute("size") * 9);
		}
		else{
			size = 9 * 6;
		}
		
		//create the inventory
		Inventory inventory = Bukkit.createInventory(null, size, title);
		
		//setup the item map if it isn't already specified
		if (!instance.getParameters().containsKey("itemMap")){
			instance.getParameters().put("itemMap", new HashMap<Integer, String>());
		}
		
		//add components
		for (Component component: menu.getComponents().values()){
			renderMenuComponent(inventory, component, instance);
		}
		
		//add the created inventory as a parameter
		instance.getParameters().put("inventory", inventory);
		
		return inventory;
	}
	
	@SuppressWarnings("unchecked")
	private void renderMenuComponent(Inventory inventory, Component component, MenuInstance instance){
		
		//find the position, relative to the current position of the renderer
		int x = -1;
		int y = -1;
		if (component.hasAttribute("x")){
			x = (Integer) component.getAttribute("x");
		}
		if (component.hasAttribute("y")){
			y = (Integer) component.getAttribute("y");
		}
		
		//create the correct material
		Material material;
		if (component.hasAttribute("material") && component.getAttribute("material") instanceof Integer){
			material = Material.getMaterial((Integer) component.getAttribute("material"));
		}
		else{
			material = Material.WOOL;
		}
		
		//create the itemstack
		ItemStack item = new ItemStack(material);
		
		//make meta changes
		ItemMeta meta = item.getItemMeta();

		//change the title of the item
		String text = component.getTag();
		if (component.hasAttribute("text")){
			text = (String) component.getAttribute("text");
		}
		meta.setDisplayName("¤r¤f" + text);
		
		//set the lore
		if (meta.hasLore()){
			meta.getLore().clear();
		}
		List<String> lore = component.getLore();
		List<String> newLore = new LinkedList<String>();
		if (lore != null){
			for (String l: lore){
				l = "¤r¤7" + l;
				newLore.add(l);
			}
		}
		meta.setLore(newLore);
		
		//set the metadata
		item.setItemMeta(meta);
		
		//set the location of the item
		//TODO: clean this sick filth
		int spot = inventory.firstEmpty();
		if (x != -1 && y != -1){
			spot = getSpot(x, y);
		}
		
		if (spot >= inventory.getSize()){
			if (inventory.firstEmpty() != -1){
				spot = inventory.firstEmpty();
			}
			else{
				spot = -1;
			}
		}
		
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
	private int getSpot(int x, int y){
		return (9 * y) + x;
	}
	
	/**
	 * Renders the MenuInstance for all players who are currently viewing the MenuInstance
	 * @param menuInstance the Menuinstance to render
	 */
	@Override
	public void renderAllPlayers(MenuInstance instance) {
		
		//create the inventory
		Inventory inv = createInventory(instance);
		if (inv == null){
			return;
		}
		
		//for each player, render the instance
		for (String player: instance.getPlayers()){
			Bukkit.getPlayer(player).openInventory(inv);
			this.getPlayers().put(player, instance);
		}
		
	}
	
	/**
	 * Renders a MenuInstance for a given player
	 * @param menuInstance the MenuInstance to render
	 * @param playerName the name of the player
	 */
	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		Inventory inv = createInventory(menuInstance);
		if (inv == null){
			return;
		}
		Bukkit.getPlayer(playerName).openInventory(inv);
		this.getPlayers().put(playerName, menuInstance);
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
		
		//get the MenuInstance
		MenuInstance instance = this.getPlayers().get(playerName);
		if (instance == null){
			return;
		}
		
		//close the inventory
		Bukkit.getPlayer(playerName).closeInventory();
		getPlayers().remove(playerName);
		instance.removePlayer(playerName);
		
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
			
			//remove the player
			instance.getPlayers().remove(playerName);
			getPlayers().remove(playerName);
			getMenuService().closeMenuInstance(playerName);
		}
		
	}
	
	/**
	 * Handles when a player clicks an item in an inventory.
	 * If the inventory was a menu, handles the menu accordingly
	 * @param event
	 */
	@EventHandler(ignoreCancelled=true)
	public void inventoryClick(InventoryClickEvent event){		
				
		//check if player was viewing a menu
		String playerName = event.getWhoClicked().getName();
		if (!getPlayers().containsKey(playerName)){
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
		ContainerAttribute actions = component.getConatinerAttribute("actions");
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
			player.sendMessage("¤cYou do not have permission to do that.");
			return;
		}
		
		//check if the player has permission to activate the action
		if (!hasPermissions(player, action)){
			player.sendMessage("¤cYou do not have permission to do that.");
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
	
	/**
	 * Checks if the player has permission to interact with the component
	 * @param player
	 * @param component
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean hasPermissions(Player player, Component component){
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
	private boolean hasPermissions(Player player, ContainerAttribute action){
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
	private CommandSender getCommandSender(Player player, String sender){
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
