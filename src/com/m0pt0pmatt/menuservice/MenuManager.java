package com.m0pt0pmatt.menuservice;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

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
public class MenuManager implements ActionListener{

	private enum MenuType{
		
		MAINMENU(0),
		LISTMENU(1),
		EDITMENU(2),
		OPENMENU(3),
		CLOSEMENU(4),
		LOADMENU(5),
		SAVEMENU(6),
		RELOADMENU(7),
		UNLOADMENU(8),
		HELPMENU(9);
		
		private static final Map<Integer, MenuType> lookup = new HashMap<Integer, MenuType>();
		
		private int type;
		
		static {
	          for(MenuType s : EnumSet.allOf(MenuType.class))
	               lookup.put(s.getType(), s);
	     }
		
		MenuType(int type){
			this.type = type;
		}
		
		public int getType(){
			return type;
		}

		public static MenuType getMenuType(int tag) {
			return lookup.get(tag);
		}
		
	} 
	
	private MenuService menuService;
	
	public MenuManager(MenuService menuService){
		this.menuService = menuService;
	}
	
	
	public Menu buildMainMenu(){
		
		MenuComponent mainMenu = new MenuComponent();
		
		Renderer renderer = menuService.getRenderer("inventory");
		if (renderer == null){
			return null;
		}
		
		mainMenu.addRenderer(renderer);
		mainMenu.addAttribute("plugin", Bukkit.getPluginManager().getPlugin("MenuService"));
		mainMenu.addAttribute("size", 2);
		
		Component component;
		List<Integer> actionTags;
		
		//add "list" component
		component = new AbstractComponent();
		component.addAttribute("title", "List Menus");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.LISTMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "edit" component
		component = new AbstractComponent();
		component.setTag("editButton");
		component.setType("button");
		component.addAttribute("title", "Edit a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.EDITMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "open" component
		component = new AbstractComponent();
		component.setTag("openButton");
		component.setType("button");
		component.addAttribute("title", "Open a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.OPENMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "close" component
		component = new AbstractComponent();
		component.setTag("closeButton");
		component.setType("button");
		component.addAttribute("title", "Close a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.CLOSEMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "load" component
		component = new AbstractComponent();
		component.setTag("loadButton");
		component.setType("button");
		component.addAttribute("title", "Load a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.LOADMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "save" component
		component = new AbstractComponent();
		component.setTag("saveButton");
		component.setType("button");
		component.addAttribute("title", "Save a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.SAVEMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "reload" component
		component = new AbstractComponent();
		component.setTag("reloadButton");
		component.setType("button");
		component.addAttribute("title", "Reload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.RELOADMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "unload" component
		component = new AbstractComponent();
		component.setTag("unloadButton");
		component.setType("button");
		component.addAttribute("title", "Unload a Menu");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.UNLOADMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		//add "help" component
		component = new AbstractComponent();
		component.setTag("helpButton");
		component.setType("button");
		component.addAttribute("title", "Help");
		actionTags = new LinkedList<Integer>();
		actionTags.add(MenuType.HELPMENU.type);
		component.addAction("leftClick", actionTags);
		mainMenu.addComponent(component);
		
		return mainMenu;
	}

	@Override
	public void handleAction(ActionEvent event) {
		int tag = event.getAction().getTag();
		
		if (tag >= 0 && tag < 10){
			handleMainMenu(event);
		}
		
		
	}
	
	private void handleMainMenu(ActionEvent event){
		int tag = event.getAction().getTag();
		MenuType menuType = MenuType.getMenuType(tag);
		
		switch (menuType){
		case LISTMENU:
			//open list menu
			break;
		case EDITMENU:
			//open edit menu
			break;
		case OPENMENU:
			//
			break;
		case CLOSEMENU:
			break;
		case LOADMENU:
			break;
		case SAVEMENU:
			break;
		case RELOADMENU:
			break;
		case UNLOADMENU:
			break;
		case HELPMENU:
			break;
		default:
			break;
		}
		
		
	}
	

	@Override
	public void playerAdded(MenuInstance instance, String playerName) {}

	@Override
	public void playerRemoved(MenuInstance instance, String playerName) {}

	@Override
	public void playerCountZero(MenuInstance instance, String playerName) {}

	@Override
	public String getName() {
		return "MenuManager";
	}

	@Override
	public String getPlugin() {
		return "MenuService";
	}
}
