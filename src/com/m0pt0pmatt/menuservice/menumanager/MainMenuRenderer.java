package com.m0pt0pmatt.menuservice.menumanager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

public class MainMenuRenderer extends AbstractRenderer implements Renderer{

	private MenuService menuService;
	
	public MainMenuRenderer(MenuService menuService, Plugin plugin) {
		super(menuService, plugin);
		this.menuService = menuService;
	}

	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		List<Menu> menus = menuService.getMenus();
		
		Inventory inv = (Inventory) menuInstance.getParameter("inventory");
		
		Player player = Bukkit.getPlayer(playerName);		
		
		for (Menu menu: menus){
			
			player.sendMessage(menu.getName());
			
			ItemStack item = new ItemStack(Material.WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(menu.getName());
			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		player.openInventory(inv);
		
	}

	@Override
	public void renderAllPlayers(MenuInstance menuInstance) {}

	@Override
	public String getName() {
		return "MenuService-MenuManager-MainMenu";
	}

	@Override
	public void closeMenu(String playerName) {}

}
