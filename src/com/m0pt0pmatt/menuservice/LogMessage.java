package com.m0pt0pmatt.menuservice;

/**
 * Messages to be displayed to the terminal.
 * These are here so they can be adjusted easily.
 * @author mbroomfield
 *
 */
public enum LogMessage {

	//null input
	NULLPLUGIN ("Error: The specified Plugin was null"),
	NULLPLUGINNAME ("Error: The specified name for the Plugin was null"),
	NULLMENUNAME ("Error: The specified name for the Menu was null"),
	NULLMENU ("Error: The specified Menu was null"),
	NULLFILENAME ("Error: The specified FileName was null"),
	NULLRENDERER ("Error: The specified Renderer was null"),
	NULLRENDERERNAME ("Error: The specified name for the Renderer was null"),
	NULLMENUINSTANCE ("Error: The specified MenuInstance was null"),
	NULLMENUINSTANCENAME ("Error: The specified name for the MenuInstance was null"),
	NULLITEMSTACK ("Error: The specified ItemStack was null"),
	NULLMATERIAL ("Error: The specified Material was null"),
	
	//no such object
	NOSUCHMENU ("Error: The specified Menu is not loaded in the MenuService"),
	NOSUCHRENDERER ("Error: The specified Renderer is not loaded in the MenuService"),
	NOSUCHMENUINSTANCE ("Error: The specified MenuInstance is not loaded in the MenuService"),
	
	//error running method
	CANTLOADMENU ("Error: Could not load Menu"),
	CANTSAVEMENU ("Error: Could not save Menu"),
	CANTRELOADMENU ("Error: Could not reload Menu"),
	CANTOPENMENU ("Error: Could not open Menu"),
	CANTCLOSEMENU ("Error: Could not close Menu"),
	CANTCREATEMENUINSTANCE ("Error: Could not create MenuInstance"),
	CANTOPENMENUINSTANCE ("Error: Could not open MenuInstance"),
	CANTCLOSEMENUINSTANCE ("Error: Could not close MenuInstance"),
	CANTBINDMENUITEM ("Error: Could not bind the ItemStack to the Menu"),
	CANTBINDMENUMATERIAL ("Error: Could not bind the Material to the Menu"),
	CANTUNBINDITEM ("Error: Could not unbind the Material"),
	CANTUNBINDMATERIAL ("Error: Could not unbind Material"),
	CANTUNBINDMENU ("Error: Could not unbind Material"),
	CANTADDMENU ("Error: Could not add Menu");
	
	private final String message;
	
	private LogMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
