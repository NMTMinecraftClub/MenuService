package com.m0pt0pmatt.menuservice.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.m0pt0pmatt.menuservice.Keyword;
import com.m0pt0pmatt.menuservice.Perm;

public class MyCommand {

	private String name;
	private List<String> arguments;
	private Perm permission;
	
	public MyCommand(String name, List<String> arguments, Perm permission){
		this.name = name;
		this.arguments = arguments;
		this.permission = permission;
	}
	
	public String toString(){
		String s = this.name + ":" + permission.toString();
		for (String arg: arguments){
			s = s + ":" + arg;
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	
	public Perm getPermission() {
		return permission;
	}

	public void setPermission(Perm permission) {
		this.permission = permission;
	}

	public boolean matchesArguments(String[] args) {
		if (args.length - 1 != arguments.size()){
			return false;
		}
		
		for (int i = 0; i < args.length - 1; i++){

			if (Keyword.is(Keyword.PLACEHOLDER, arguments.get(i))){
				continue;
			}
			
			if (Keyword.is(arguments.get(i))){
				if (Keyword.is(Keyword.getKeyword(arguments.get(i)), args[i + 1])){
					continue;
				}
			}
			return false;
		}
		return true;
	}

	public String[] getActualArguments(String[] args) {
		if (args.length - 1 != arguments.size()){
			return null;
		}
		
		List<String> actuals = new LinkedList<String>();
		actuals.add(Keyword.SENDER.getKeyword());
		
		for (int i = 0; i < args.length - 1; i++){

			if (Keyword.is(Keyword.PLACEHOLDER, arguments.get(i))){
				actuals.add(args[i + 1]);
			}
			
		}

		return actuals.toArray(new String[1]);
		
	}
}
