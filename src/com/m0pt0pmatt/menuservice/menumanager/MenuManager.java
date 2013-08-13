package com.m0pt0pmatt.menuservice.menumanager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

import com.m0pt0pmatt.menuservice.LogMessage;
import com.m0pt0pmatt.menuservice.Logger;
import com.m0pt0pmatt.menuservice.MenuServicePlugin;
import com.m0pt0pmatt.menuservice.api.AbstractComponent;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuComponent;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;

/**
 * The MenuManager is an interactive menu that allows server admins to be able to manage the Menus for a server.
 * @author mbroomfield
 *
 */
public class MenuManager {
	
	/**
	 * The MenuService for the Server
	 */
	private MenuService menuService;
	
	/**
	 * A Map of the Menus used by the Menu Manager
	 */
	public static Map<String, Menu> menus;
	
	/**
	 * The Renderer for the Main Menu
	 */
	private static MainMenuRenderer mainMenuRenderer;
	
	/**
	 * The Renderer for the Menu Menu
	 */
	private static InstancesMenuRenderer instancesMenuRenderer;
	
	/**
	 * The Listener for the MenuManager
	 */
	private static ManagerListener managerListener;
	
	/**
	 * Creates a Menu Manager
	 * @param menuService
	 * @param plugin
	 */
	public MenuManager(MenuService menuService, MenuServicePlugin plugin){
		
		//store the MenuService
		this.menuService = menuService;
		
		//Create the Renderers
		mainMenuRenderer = new MainMenuRenderer(menuService, plugin);
		instancesMenuRenderer = new InstancesMenuRenderer(menuService, plugin);
		
		//add the Renderers
		menuService.addRenderer(mainMenuRenderer);
		menuService.addRenderer(instancesMenuRenderer);
		
		//Create the Menus
		menus = new HashMap<String, Menu>();
		menus.put("mainMenu", buildMainMenu());
		menus.put("instancesMenu", buildInstancesMenu());
		menus.put("playersMenu", buildPlayersMenu());
		
		//Add the Menus to the MenuService
		for (Menu menu: menus.values()){
			menuService.addMenu(plugin, menu);
		}
		
		//create the listener
		managerListener = new ManagerListener(menuService, this);

	}

	/**
	 * Builds the Main Menu
	 * @return the Main Menu
	 */
	public Menu buildMainMenu(){
		
		//create the Component
		MenuComponent menu = new MenuComponent();
		
		//add the custom renderer
		menu.addRenderer(mainMenuRenderer);
		
		//add attributes to the menu
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService").getName());
		menu.setTag("MenuService-MenuManager-MainMenu");
		menu.addAttribute("size", 6);
		menu.addAttribute("title", "Menu Manager: Main Menu");
		menu.addAttribute("autoSave", false);
		
		//add Components to the Menu
		Component component;
		List<Integer> actionTags;
		
		//add "edit" component
		component = new AbstractComponent();
		component.setTag("editButton");
		component.setType("button");
		component.addAttribute("text", "Edit a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_EDITMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 0);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "open" component
		component = new AbstractComponent();
		component.setTag("openButton");
		component.setType("button");
		component.addAttribute("text", "Open a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_OPENMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 1);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "close" component
		component = new AbstractComponent();
		component.setTag("closeButton");
		component.setType("button");
		component.addAttribute("text", "Close a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_CLOSEMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 2);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "load" component
		component = new AbstractComponent();
		component.setTag("loadButton");
		component.setType("button");
		component.addAttribute("text", "Load a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_LOADMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 3);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "save" component
		component = new AbstractComponent();
		component.setTag("saveButton");
		component.setType("button");
		component.addAttribute("text", "Save a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_SAVEMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 4);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "reload" component
		component = new AbstractComponent();
		component.setTag("reloadButton");
		component.setType("button");
		component.addAttribute("text", "Reload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_RELOADMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 5);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "unload" component
		component = new AbstractComponent();
		component.setTag("unloadButton");
		component.setType("button");
		component.addAttribute("text", "Unload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_UNLOADMENU.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 6);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "help" component
		component = new AbstractComponent();
		component.setTag("helpButton");
		component.setType("button");
		component.addAttribute("text", "Help");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_HELP.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 7);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		return menu;
	}
	
	/**
	 * Builds the Instances Menu
	 * @return the Instances Menu
	 */
	private Menu buildInstancesMenu() {
		
		//create the Component
		MenuComponent menu = new MenuComponent();
		
		//add the custom renderer
		menu.addRenderer(instancesMenuRenderer);
		
		//add attributes to the menu
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService").getName());
		menu.addAttribute("size", 6);
		menu.setTag("MenuService-MenuManager-InstancesMenu");
		menu.addAttribute("title", "MenuService Menu Manager: Instances");
		menu.addAttribute("autoSave", false);
		
		//add Components to the Menu
		Component component;
		List<Integer> actionTags;
		
		//add "create" component
		component = new AbstractComponent();
		component.setTag("createButton");
		component.setType("button");
		component.addAttribute("text", "Create Instance");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_HELP.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 0);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "open" component
		component = new AbstractComponent();
		component.setTag("openButton");
		component.setType("button");
		component.addAttribute("text", "Open Instance");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_HELP.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 1);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "close" component
		component = new AbstractComponent();
		component.setTag("closeButton");
		component.setType("button");
		component.addAttribute("text", "Close Instance");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_HELP.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 2);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		//add "remove" component
		component = new AbstractComponent();
		component.setTag("removeButton");
		component.setType("button");
		component.addAttribute("text", "Remove Instance");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.MAIN_HELP.getType());
		component.addAction("leftClick", actionTags);
		component.addAttribute("item", new Wool(DyeColor.ORANGE).toItemStack());
		component.addAttribute("x", 3);
		component.addAttribute("y", 0);
		menu.addComponent(component);
		
		return menu;
	}
	
	/**
	 * Builds the Instance Menu
	 * @return the Instance Menu
	 */
	private Menu buildPlayersMenu() {
		
		//create the Component
		MenuComponent menu = new MenuComponent();
		
		//add the custom renderer

		//add attributes to the menu		
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService").getName());
		menu.addAttribute("size", 6);
		menu.setTag("MenuService-MenuManager-PlayersMenu");
		menu.addAttribute("title", "MenuService Menu Manager: Players");
		menu.addAttribute("autoSave", false);
		
		//add Components to the Menu
		//Component component;
		//List<Integer> actionTags;
		
		return menu;
	}

	/**
	 * Shows the Main Menu to the specified player
	 * @param player the player to show the Main Menu to
	 * @return true if Menu was shown, false otherwise
	 */
	public boolean showMainMenu(Player player) {
		
		//check player
		if (player == null){
			Logger.log(2, Level.SEVERE, LogMessage.NULLPLAYER, null);
			Logger.log(2, Level.SEVERE, LogMessage.CANTSHOWMAINMENU, null);
			return false;
		}
		
		MenuInstance instance = menuService.createMenuInstance(menus.get("mainMenu"), "MenuService-MainMenu-" + player.getName());
		if (instance == null){
			Logger.log(2, Level.SEVERE, LogMessage.CANTCREATEMENUINSTANCE, player.getName());
			Logger.log(2, Level.SEVERE, LogMessage.CANTSHOWMAINMENU, player.getName());
			return false;
		}
		instance.addActionListener(managerListener);
		
		return menuService.openMenuInstance(instance, player.getName());
	}

	
}
