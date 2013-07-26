package com.m0pt0pmatt.menuservice.menumanager;

import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
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
	
	private void handleMainMenu(ActionEvent event){
		int tag = event.getAction().getTag();
		MenuType menuType = MenuType.getMenuType(tag);
		String playerName = event.getAction().getPlayerName();
		
		switch (menuType){
		case EDITMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case OPENMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case CLOSEMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case LOADMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case SAVEMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case RELOADMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case UNLOADMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
			break;
		case HELPMENU:
			menuService.closeMenuInstance(playerName);
			//menuService.openMenuInstance(, playerName);
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
