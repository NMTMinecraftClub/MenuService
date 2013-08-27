package com.m0pt0pmatt.menuservice.menumanager;

import java.util.Map;

import org.bukkit.Bukkit;

import com.m0pt0pmatt.menuservice.MessageFormat;
import com.m0pt0pmatt.menuservice.Message;

import com.m0pt0pmatt.menuservice.api.ActionEvent;
import com.m0pt0pmatt.menuservice.api.ActionListener;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;

public class MainMenuListener implements ActionListener{
	
	private MenuService menuService;
	
	public MainMenuListener(MenuService menuService, MenuManager manager){
		this.menuService = menuService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleAction(ActionEvent event) {
		
		int tag = event.getAction().getTag();
		MenuInstance instance = event.getAction().getInstance();
		String playerName = event.getAction().getPlayerName();
		int spot = (Integer) instance.getParameter(playerName + ":slot");
						
		Map<Integer, Menu> menuSpots;
		Menu menu = null;
		
		switch (MainMenuButtons.getMenuType(tag)){
		case MAIN_MENUCLICKED:
			menuSpots = (Map<Integer, Menu>) instance.getParameter("menuSpots");
			menu = menuSpots.get(spot);
			mainMenuClicked(playerName, menu, instance);
			break;
		case MAIN_EDITMENU:
			instance.toggleAndRemoveOtherHighlights("editButton");
			break;
		case MAIN_OPENMENU:
			instance.toggleAndRemoveOtherHighlights("openButton");
			break;
		case MAIN_CLOSEMENU:
			instance.toggleAndRemoveOtherHighlights("closeButton");
			break;
		case MAIN_SAVEMENU:
			instance.toggleAndRemoveOtherHighlights("saveButton");
			break;
		case MAIN_SAVEALL:
			menuService.saveMenus();
			Bukkit.getPlayer(playerName).sendMessage("All menus saved");
			break;
		case MAIN_RELOADMENU:
			instance.toggleAndRemoveOtherHighlights("reloadButton");
			break;
		case MAIN_RELOADALL:
			menuService.reloadMenus();
			instance.renderAll();
			Bukkit.getPlayer(playerName).sendMessage("All menus reloaded");
			break;
		case MAIN_UNLOADMENU:
			instance.toggleAndRemoveOtherHighlights("unloadButton");
			break;
		case MAIN_UNLOADALL:
			menuService.unloadMenus();
			instance.renderAll();
			Bukkit.getPlayer(playerName).sendMessage("All menus unloaded");
			break;
		case MAIN_LEFTBUTTON:
			break;
		case MAIN_RIGHTBUTTON:
			break;
		case MAIN_EXITBUTTON:
			break;
		default:
			
		}
		
	}
	
	private void mainMenuClicked(String playerName, Menu menu, MenuInstance instance){
				
		if (instance.isHighlighted("editButton")){
			Message.sendMessage(playerName, MessageFormat.NOTIFICATION, "The Menu Editor is not working yet.");
		} else if (instance.isHighlighted("openButton")){
			//open the selected menu
			menuService.closeMenuInstance(playerName);
			MenuInstance newInstance = menuService.createMenuInstance(menu, menu.getName() + ": " + playerName);
			newInstance.addParameter("exitOnEmpty", true);
			menuService.openMenuInstance(instance, playerName);
		} else if (instance.isHighlighted("closeButton")){
			System.out.println("HIGHLIGHTED CLOSE");
		} else if (instance.isHighlighted("saveButton")){
			System.out.println("HIGHLIGHTED SAVE");
		} else if (instance.isHighlighted("reloadButton")){
			System.out.println("HIGHLIGHTED RELOAD");
		} else if (instance.isHighlighted("unloadButton")){
			System.out.println("HIGHLIGHTED UNLOAD");
		} else {
			//open the instancesMenu
			menuService.closeMenuInstance(playerName);
			MenuInstance newInstance = menuService.createMenuInstance(MenuManager.menus.get("instancesMenu"), "MenuManager-InstancesMenu-" + playerName);
			newInstance.addParameter("menu", menu);
			newInstance.addActionListener(MenuManager.instancesMenuListener);
			menuService.openMenuInstance(newInstance, playerName);
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
