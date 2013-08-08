package com.m0pt0pmatt.menuservice.menumanager;

import java.util.Map;

import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;

public class ManagerListener implements ActionListener{
	
	private MenuService menuService;
	private MenuManager manager;
	
	public ManagerListener(MenuService menuService, MenuManager manager){
		this.menuService = menuService;
		this.manager = manager;
	}
	
	@Override
	public void handleAction(ActionEvent event) {
		int tag = event.getAction().getTag();
		
		if (tag == MenuType.MAINMENU.getType()){
			handleMainMenu(event);
		} else if (tag == MenuType.MENUMENU.getType()){
			handleInstancesMenu(event);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void handleMainMenu(ActionEvent event){
		MenuInstance instance = event.getAction().getInstance();
		String playerName = event.getAction().getPlayerName();
		int type = (Integer) instance.getParameter(playerName + ":menuSpot");
		int spot = (Integer) instance.getParameter(playerName + ":slot");
		
		MenuType menuType = MenuType.getMenuType(type);
		
		Map<Integer, Menu> menuSpots;
		
		switch (menuType){
		case MAIN_MENUCLICKED:
			menuSpots = (Map<Integer, Menu>) instance.getParameter("menuSpots");
			
			Menu menu = menuSpots.get(spot);
			menuService.closeMenuInstance(playerName);
			MenuInstance newInstance = menuService.createMenuInstance(MenuManager.menus.get("instancesMenu"), "MenuManager-InstancesMenu-" + playerName);
			newInstance.addParameter("menu", menu);
			menuService.openMenuInstance(newInstance, playerName);
			break;
		case MAIN_EDITMENU:
			if (instance.isHighlighted("editButton")){
				instance.unhighlightButton("editButton");
			} else{
				instance.highlightButton("editButton");
			}
			System.out.println("EDIT");
			break;
		case MAIN_OPENMENU:
			System.out.println("OPEN");
			break;
		case MAIN_CLOSEMENU:
			System.out.println("CLOSE");
			break;
		case MAIN_LOADMENU:
			System.out.println("LOAD");
			break;
		case MAIN_SAVEMENU:
			System.out.println("SAVE");
			break;
		case MAIN_RELOADMENU:
			System.out.println("RELOAD");
			break;
		case MAIN_UNLOADMENU:
			System.out.println("UNLOAD");
			break;
		case MAIN_HELP:
			System.out.println("HELP");
			break;
		default:
			
		}
		
	}
	
	private void handleInstancesMenu(ActionEvent event){
		MenuInstance instance = event.getAction().getInstance();
		String playerName = event.getAction().getPlayerName();
		int type = (Integer) instance.getParameter(playerName + ":menuSpot");
		int spot = (Integer) instance.getParameter(playerName + ":slot");
		
		MenuType menuType = MenuType.getMenuType(type);
		
		Map<Integer, Menu> menuSpots;
		
		switch (menuType){
		case MENU_CREATEINSTANCE:
			System.out.println("CREATE");
			break;
		case MENU_OPENINSTANCE:
			System.out.println("OPEN");
			break;
		case MENU_CLOSEINSTANCE:
			System.out.println("CLOSE");
			break;
		case MENU_REMOVEINSTANCE:
			System.out.println("REMOVE");
			break;
		case MENU_INSTANCECLICKED:
			System.out.println("INSTANCECLICKED");
			break;
		default:
			
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
		return "MenuManagerListener";
	}

	@Override
	public String getPlugin() {
		return "MenuService";
	}

}
