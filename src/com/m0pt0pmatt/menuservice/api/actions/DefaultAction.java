package com.m0pt0pmatt.menuservice.api.actions;

import java.util.HashSet;
import java.util.Set;

import com.m0pt0pmatt.menuservice.api.rendering.Renderer;

public enum DefaultAction implements Action{
	LEFT_CLICK("inventory"),
	RIGHT_CLICK("inventory")
	;

	private Set<String> supportedRenderers;
	
	private DefaultAction(String... renderers){
		supportedRenderers = new HashSet<String>();
		for (String rendererName: renderers){
			supportedRenderers.add(rendererName);
		}
	}

	@Override
	public boolean isPossibleForRenderer(Renderer renderer) {
		if (supportedRenderers.contains(renderer.getName())){
			return true;
		}
		return false;
	}
	
}
