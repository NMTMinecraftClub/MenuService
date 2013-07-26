package com.m0pt0pmatt.menuservice.menumanager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.m0pt0pmatt.menuservice.api.AbstractComponent;
import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Component;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuComponent;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * The MenuManager is an interactive menu that allows server admins to be able to manage the Menus for a server
 * @author mbroomfield
 *
 */
public class MenuManager {
	
	private MenuService menuService;
	
	private Map<String, Menu> menus;
	private Map<String, MenuInstance> instances;
	
	public MenuManager(MenuService menuService){
		this.menuService = menuService;
		menus = new HashMap<String, Menu>();
		instances = new HashMap<String, MenuInstance>();
		menus.put("mainMenu", buildMainMenu());

	}


	


	public Menu buildMainMenu(){
		
		MenuComponent menu = new MenuComponent();
		
		Renderer renderer = menuService.getRenderer("inventory");
		if (renderer == null){
			return null;
		}
		
		menu.addRenderer(renderer);
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService"));
		menu.addAttribute("size", 6);
		
		Component component;
		List<Integer> actionTags;
		
		//add "list" component
		component = new AbstractComponent();
		component.addAttribute("title", "List Menus");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.LISTMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "edit" component
		component = new AbstractComponent();
		component.setTag("editButton");
		component.setType("button");
		component.addAttribute("title", "Edit a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.EDITMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "open" component
		component = new AbstractComponent();
		component.setTag("openButton");
		component.setType("button");
		component.addAttribute("title", "Open a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.OPENMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "close" component
		component = new AbstractComponent();
		component.setTag("closeButton");
		component.setType("button");
		component.addAttribute("title", "Close a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.CLOSEMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "load" component
		component = new AbstractComponent();
		component.setTag("loadButton");
		component.setType("button");
		component.addAttribute("title", "Load a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.LOADMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "save" component
		component = new AbstractComponent();
		component.setTag("saveButton");
		component.setType("button");
		component.addAttribute("title", "Save a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.SAVEMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "reload" component
		component = new AbstractComponent();
		component.setTag("reloadButton");
		component.setType("button");
		component.addAttribute("title", "Reload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.RELOADMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "unload" component
		component = new AbstractComponent();
		component.setTag("unloadButton");
		component.setType("button");
		component.addAttribute("title", "Unload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.UNLOADMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		//add "help" component
		component = new AbstractComponent();
		component.setTag("helpButton");
		component.setType("button");
		component.addAttribute("title", "Help");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.HELPMENU.getType());
		component.addAction("leftClick", actionTags);
		menu.addComponent(component);
		
		return menu;
	}
	
	private Menu buildInstancesMenu() {
		MenuComponent menu = new MenuComponent();
		
		Renderer renderer = menuService.getRenderer("inventory");
		if (renderer == null){
			return null;
		}
		
		menu.addRenderer(renderer);
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService"));
		menu.addAttribute("size", 6);
		
		Component component;
		List<Integer> actionTags;
		
		return menu;
	}
	
	private Menu buildPlayerMenu() {
		MenuComponent menu = new MenuComponent();
		
		Renderer renderer = menuService.getRenderer("inventory");
		if (renderer == null){
			return null;
		}
		
		menu.addRenderer(renderer);
		menu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService"));
		menu.addAttribute("size", 6);
		
		Component component;
		List<Integer> actionTags;
		
		return menu;
	}

	
}
