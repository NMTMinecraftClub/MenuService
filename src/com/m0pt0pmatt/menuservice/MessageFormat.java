package com.m0pt0pmatt.menuservice;

import org.bukkit.ChatColor;

public enum MessageFormat {
		
	NOTIFICATION("MenuService", ChatColor.BLUE, ChatColor.GREEN),
	INCORRECTUSE("MenuService", ChatColor.RED, ChatColor.GREEN),
	HELPMESSAGE(null, null, ChatColor.GRAY),
	NOPERMISSION(null, null, ChatColor.RED), 
	WRONGNUMBEROFARGUMENTS(null, null, ChatColor.RED),
	BADARGUMENTS(null, null, ChatColor.RED), 
	ERROR(null, null, ChatColor.RED), 
	OUTPUT(null, null, ChatColor.GRAY);
	
	private final String label;
	private final ChatColor labelColor;
	private final ChatColor messageColor;
	
	private MessageFormat(String label, ChatColor labelColor, ChatColor messageColor){
		this.label = label;
		this.labelColor = labelColor;
		this.messageColor = messageColor;
	}
	
	public String getLabel(){
		return label;
	}
	
	public ChatColor getLabelColor(){
		return labelColor;
	}
	
	public ChatColor getMessageColor(){
		return messageColor;
	}
}
