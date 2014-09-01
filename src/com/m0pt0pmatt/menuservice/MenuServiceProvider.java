package com.m0pt0pmatt.menuservice;

import java.util.HashMap;
import java.util.Map;

import com.m0pt0pmatt.menuservice.api.MenuService;
import com.m0pt0pmatt.menuservice.api.Renderer;

/**
 * The MenuServiceProvider is the provider for the Bukkit service MenuService.
 * It is the implementation of the MenuService API
 * @author Matthew Broomfield (m0pt0pmatt) <m0pt0pmatt17@gmail.com>
 *
 */
public class MenuServiceProvider implements MenuService{
	
	//Renderers are kept for reference
	private Map<String, Renderer> renderers;
	
	public MenuServiceProvider(MenuServicePlugin plugin){
		renderers = new HashMap<String, Renderer>();
	}
	
	@Override
	public Renderer getRenderer(String rendererName) {
		return renderers.get(rendererName);
	}

	@Override
	public boolean hasRenderer(Renderer renderer) {
		return renderers.containsValue(renderer);
	}

	@Override
	public boolean hasRenderer(String rendererName) {
		return renderers.containsKey(rendererName);
	}

	@Override
	public boolean addRenderer(Renderer renderer) {
		if (renderers.containsValue(renderer)){
			return false;
		}
		renderers.put(renderer.getName(), renderer);
		return true;
	}

	@Override
	public boolean removeRenderer(Renderer renderer) {
		if (renderers.remove(renderer.getName()) == null){
			return false;
		}
		return true;
	}

	@Override
	public Renderer removeRenderer(String rendererName) {
		return renderers.remove(rendererName);
	} 

}
