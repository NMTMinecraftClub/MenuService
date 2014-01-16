package com.m0pt0pmatt.menuservice.api.actions;

import com.m0pt0pmatt.menuservice.api.rendering.Renderer;

public interface Action {
	
	public boolean isPossibleForRenderer(Renderer renderer);
}
