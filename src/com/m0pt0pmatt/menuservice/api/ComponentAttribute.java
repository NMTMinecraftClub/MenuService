package com.m0pt0pmatt.menuservice.api;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

/**
 * An enumeration of the built in Menu attributes.
 * Note that attributes are not limited to ones on this list. A menu can have any attribute.
 * Formally, an Attribute is a name and a Class. A list also has the Class of its element.
 * @author Matthew
 *
 */
public enum ComponentAttribute {

	TAG("tag", String.class),
	FILENAME("filename", String.class),
	ITEM("item", ItemStack.class),
	X("x", Integer.class),
	Y("y", Integer.class);
	
	private final String name;
	private final Class<?> type;
	
	//a Map of possible names to the list of attributes that the name represents
	private static final Map<String, Set<ComponentAttribute>> attributes = new HashMap<String, Set<ComponentAttribute>>();
	
	static {
        for(ComponentAttribute s : EnumSet.allOf(ComponentAttribute.class)){
        	if (attributes.get(s.getName()) == null){
        		attributes.put(s.getName(), new HashSet<ComponentAttribute>());
        	}
        	attributes.get(s.getName()).add(s);
        }  	
	}
	
	/**
	 * Creates an Attribute. Do not use this constructor for lists.
	 * @param name
	 * @param type
	 */
	private ComponentAttribute(String name, Class<?> type){
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Return the name of the attribute
	 * @return the name of the attribute
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Return the Class of the value of the Attribute
	 * @return the Class of the value of the Attribute
	 */
	public Class<?> getAttributeClass(){
		return type;
	}
	
	/**
	 * Checks if the Attribute object has a valid type
	 * @param name
	 * @param value
	 * @return
	 */
	public static boolean isValid(String name, Object value){
		if (!attributes.containsKey(name)){
			return false;
		}
		
		Set<ComponentAttribute> matches = attributes.get(name);
		for (ComponentAttribute a: matches){
			if (value.getClass().equals(a.getAttributeClass())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if the Attribute object is a valid type of List.
	 * I have no idea if this works
	 * @param value
	 * @param elementType
	 * @return false if not a List, the List is empty, or the List's first element is of wrong type, true otherwise
	 */
	public static boolean isValidList(Object value, Class<?> elementType){
		if (!(value instanceof List)){
			return false;
		}
		List<?> list = (List<?>) value;
		if (list.size() == 0){
			return false;
		}

		if (list.get(0).getClass().equals(elementType)){
			return true;
		}
		
		return false;		
	}
	
}
