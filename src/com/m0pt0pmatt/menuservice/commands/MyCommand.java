package com.m0pt0pmatt.menuservice.commands;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.m0pt0pmatt.menuservice.Keyword;
import com.m0pt0pmatt.menuservice.Perm;

/**
 * This class represents a possible command for MenuService
 * @author Matthew
 *
 */
public class MyCommand {

	private Keyword name;
	private List<Keyword> arguments;
	private Perm permission;
	private Method method;
	
	public MyCommand(Keyword name, List<Keyword> arguments, Perm permission, Method method){
		this.name = name;
		this.arguments = arguments;
		this.permission = permission;
		this.method = method;
	}

	public Keyword getName() {
		return name;
	}

	public void setName(Keyword name) {
		this.name = name;
	}

	public List<Keyword> getArguments() {
		return arguments;
	}

	public void setArguments(List<Keyword> arguments) {
		this.arguments = arguments;
	}
	
	public Perm getPermission() {
		return permission;
	}

	public void setPermission(Perm permission) {
		this.permission = permission;
	}
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}


	public boolean matchesArguments(String[] args) {
		if (args.length - 1 != arguments.size()){
			return false;
		}
		
		for (int i = 0; i < args.length - 1; i++){

			if (arguments.get(i).equals(Keyword.PLACEHOLDER)){
				continue;
			}
			
			if (Keyword.is(arguments.get(i), args[i + 1])){
				continue;
			}
			
			return false;
		}
		return true;
	}

	public String[] getActualArguments(String sender, String[] args) {
		if (args.length - 1 != arguments.size()){
			return null;
		}
		
		List<String> actuals = new LinkedList<String>();
		actuals.add(sender);
		
		for (int i = 0; i < args.length - 1; i++){

			if (arguments.get(i).equals(Keyword.PLACEHOLDER)){
				actuals.add(args[i + 1]);
			}
			
		}

		return actuals.toArray(new String[1]);
		
	}
	
	public String toString(){
		String s = this.name + ":" + permission.toString();
		for (Keyword arg: arguments){
			s = s + ":" + arg.getKeyword();
		}
		return s;
	}
}
