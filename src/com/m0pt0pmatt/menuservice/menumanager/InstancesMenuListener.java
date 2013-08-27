package com.m0pt0pmatt.menuservice.menumanager;

import java.util.Map;

import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;

public class InstancesMenuListener implements ActionListener{

	private MenuService menuService;
	
	public InstancesMenuListener(MenuService menuService, MenuManager manager){
		this.menuService = menuService;
	}

	@Override
	public void handleAction(ActionEvent event) {
		
		int tag = event.getAction().getTag();
		MenuInstance instance = event.getAction().getInstance();
		String playerName = event.getAction().getPlayerName();
		int spot = (Integer) instance.getParameter(playerName + ":slot");
						
		Map<Integer, Menu> menuSpots;
		Menu menu = null;
		
		switch (InstancesMenuButtons.getMenuType(tag)){
		case MENU_EDITINSTANCE:
			break;
		case MENU_OPENINSTANCE:
			break;
		case MENU_CLOSEINSTANCE:
			break;
		case MENU_CLOSEALL:
			break;
		case MENU_REMOVEINSTANCE:
			break;
		case MENU_REMOVEALL:
			break;
		case MENU_INSTANCECLICKED:
			break;
		case MENU_LEFTBUTTON:
			break;
		case MENU_RIGHTBUTTON:
			break;
		case MENU_BACKBUTTON:
			break;
		default:
		
		}
		
	}

	@Override
	public void playerAdded(MenuInstance instance, String playerName) {}

	@Override
	public void playerRemoved(MenuInstance instance, String playerName) {}

	@Override
	public void playerCountZero(MenuInstance instance, String lastPlayerName) {}

	@Override
	public String getName() {
		return "InstancesManagerListener";
	}

	@Override
	public String getPlugin() {
		return "MenuService";
	}
}
