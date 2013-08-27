package com.m0pt0pmatt.menuservice;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

public class Logger {

	
	public static int verbose = 2;
	
	public static Plugin plugin;
	
	public static void setPlugin(Plugin plugin){
		Logger.plugin = plugin;
	}
	
	/**
	 * Logs messages if the verbose level is high enough.
	 * This method should be used plugin wide as the only way to log messages.
	 * @param verboseLevel The verbose level of the message being logged
	 * @param level the Bukkit level of the message
	 * @param msg the LogMessage
	 */
	public static void log(int verboseLevel, Level level, Message msg, Object object){
		String m = msg.getMessage();
		if (object != null){
			m = m + ": " + object.toString();
		}
		
		log(verboseLevel, level, m);
	}
	
	public static void log(int verboseLevel, Level level, Message msg){		
		log(verboseLevel, level, msg.getMessage());
	}
	
	/**
	 * Logs messages if the verbose level is high enough.
	 * This method should be used plugin wide as the only way to log messages.
	 * @param verboseLevel The verbose level of the message being logged
	 * @param level the Bukkit level of the message
	 * @param msg The message
	 */
	public static void log(int verboseLevel, Level level, String msg){
		
		//If the verbose level is high enough
		if (verboseLevel <= verbose){
			
			//log the message
			plugin.getLogger().log(level, msg);
		}
		
		if (level == Level.SEVERE){
			for (StackTraceElement e: Thread.currentThread().getStackTrace()){
				System.out.println(e.toString());
			}
		}
	}
}
