package com.m0pt0pmatt.menuservice.menumanager;

import java.util.Map;

import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;

public class ManagerListener implements ActionListener{
	
	private MenuService menuService;
	
	public ManagerListener(MenuService menuService){
		this.menuService = menuService;
	}
	
	@Override
	public void handleAction(ActionEvent event) {
		int tag = event.getAction().getTag();
		
		if (tag >= 0 && tag < 10){
			handleMainMenu(event);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void handleMainMenu(ActionEvent event){
		int tag = event.getAction().getTag();
		MenuType menuType = MenuType.getMenuType(tag);
		String playerName = event.getAction().getPlayerName();
		MenuInstance instance = event.getAction().getInstance();
		
		Map<Integer, Menu> menuSpots;
		
		switch (menuType){
		case MAIN_MENUCLICKED:
			menuSpots = (Map<Integer, Menu>) instance.getParameter("menuSpots");
			int spot = (Integer) instance.getParameter("menuSpot");
			Menu menu = menuSpots.get(spot);
			menuService.closeMenuInstance(playerName);
			MenuInstance newInstance = menuService.createMenuInstance(menu, "MenuManager-MenuMenu-" + playerName);
			menuService.openMenuInstance(newInstance, playerName);
			break;
		case MAIN_EDITMENU:
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
