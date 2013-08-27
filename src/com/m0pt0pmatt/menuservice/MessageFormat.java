package com.m0pt0pmatt.menuservice;

import org.bukkit.ChatColor;

/**
 * a MessageFormat is a format for different types of messages.
 * MessageFormats are used by Message.sendMessage
 * The output string is structured "[label]: message", where label and message are colored appropriately
 * @author Matthew
 *
 */
public enum MessageFormat {
		
	NOTIFICATION("MenuService", ChatColor.BLUE, ChatColor.GREEN),
	INCORRECTUSE("MenuService", ChatColor.RED, ChatColor.GREEN),
	HELPMESSAGE(null, null, ChatColor.GRAY),
	NOPERMISSION(null, null, ChatColor.RED), 
	WRONGNUMBEROFARGUMENTS(null, null, ChatColor.RED),
	BADARGUMENTS(null, null, ChatColor.RED), 
	ERROR(null, null, ChatColor.RED), 
	OUTPUT(null, null, ChatColor.GRAY);
	
	//the label of the message
	private final String label;
	
	//the color of the label
	private final ChatColor labelColor;
	
	//the color of the message
	private final ChatColor messageColor;
	
	private MessageFormat(String label, ChatColor labelColor, ChatColor messageColor){
		this.label = label;
		this.labelColor = labelColor;
		this.messageColor = messageColor;
	}
	
	/**
	 * Returns the label printed to the left of the message
	 * @return the label printed to the left of the message
	 */
	public String getLabel(){
		return label;
	}
	
	/**
	 * Returns the color of the label
	 * @return the color of the label
	 */
	public ChatColor getLabelColor(){
		return labelColor;
	}
	
	/**
	 * Returns the color of the message
	 * @return the color of the message
	 */
	public ChatColor getMessageColor(){
		return messageColor;
	}
}
