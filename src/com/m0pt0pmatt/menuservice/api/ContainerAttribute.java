package com.m0pt0pmatt.menuservice.api;

import java.util.Map;

public final class ContainerAttribute {

	private String name;
	private Map<String, Object> attributes;

	public ContainerAttribute(String name, Map<String, Object> attributes){
		this.name = name;
		this.attributes = attributes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
}
