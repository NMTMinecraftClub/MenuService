package com.m0pt0pmatt.menuservice.menumanager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.m0pt0pmatt.menuservice.api.AbstractRenderer;
import com.m0pt0pmatt.menuservice.api.Menu;
import com.m0pt0pmatt.menuservice.api.MenuInstance;
import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

public class MenuMenuRenderer extends AbstractRenderer implements Renderer{
	
	public MenuMenuRenderer(MenuService menuService, Plugin plugin) {
		super(menuService, plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void renderPlayer(MenuInstance menuInstance, String playerName) {
		
		Inventory inv = (Inventory) menuInstance.getParameter("inventory");
		
		Menu menu = menuInstance.getMenu();
		
		for (MenuInstance instance: this.getMenuService().getMenuInstances(menu)){
			ItemStack item = new ItemStack(Material.WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(instance.getName());
			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		Bukkit.getPlayer(playerName).openInventory(inv);
		
	}

	@Override
	public void renderAllPlayers(MenuInstance menuInstance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeMenu(String playerName) {
		// TODO Auto-generated method stub
		
	}

}
