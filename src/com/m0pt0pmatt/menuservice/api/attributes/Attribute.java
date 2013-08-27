package com.m0pt0pmatt.menuservice.api.attributes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of the built in Menu attributes.
 * Note that attributes are not limited to ones on this list. A menu can have any attribute.
 * Formally, an Attribute is a name and a Class. A list also has the Class of its element.
 * @author Matthew
 *
 */
public enum Attribute {

	TAG("tag", String.class),
	NAME("tag", String.class),
	PLUGIN("plugin", String.class),
	FILENAME("filename", String.class),
	TYPE("type", String.class),
	COMPONENTS("components", ContainerAttribute.class),
	REMOVEONEXIT("remove-on-exit", Boolean.class),
	REMOVEONEMPTY("remove-on-empty", Boolean.class),
	DYNAMIC("dynamic", Boolean.class),
	PERMANENT("permanent", Boolean.class),
	MATERIAL_ID("material", Integer.class),
	MATERIAL_NAME("material", String.class),
	LORE("lore", List.class, String.class),
	ACTIONTAGS("action-tags", List.class, Integer.class),
	TEXT("text", String.class),
	OPENCOMMAND("open-command", String.class),
	TITLE("text", String.class),
	ACTIONS("actions", ContainerAttribute.class),
	LEFTCLICK("left-click", ContainerAttribute.class),
	RIGHTCLICK("right-click", ContainerAttribute.class),
	COMMANDS("commands", List.class, String.class);
	
	private final String name;
	private final Class<?> type;
	private final boolean isList;
	private final Class<?> elementType;
	
	//a Map of possible names to the list of attributes that the name represents
	private static final Map<String, Set<Attribute>> attributes = new HashMap<String, Set<Attribute>>();
	
	/**
	 * Adds an attribute to the Map.
	 */
	private void add(){
		
		//make sure a set exists for the name
		if (!attributes.containsKey(getName())){
			attributes.put(getName(), new HashSet<Attribute>());
		}
		
		//add the attribute
		attributes.get(getName()).add(this);
	}
	
	/**
	 * Creates an Attribute. Do not use this constructor for lists.
	 * @param name
	 * @param type
	 */
	private Attribute(String name, Class<?> type){

		this.name = name;
		this.type = type;
		isList = false;
		elementType = null;
		
		add();
	}
	
	/**
	 * Creates a list attribute. Use this constructor only if the attribute is a list
	 * @param name
	 * @param type
	 * @param elementType
	 */
	private Attribute(String name, Class<?> type, Class<?> elementType){
		this.name = name;
		this.type = type;
		this.elementType = elementType;
		isList = true;
		
		add();
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
	 * Returns whether or not the Attribute is a list
	 * @return true if the Attribute is a list, false otherwise
	 */
	public boolean isList(){
		return isList;
	}
	
	/**
	 * Returns the Class of the elements of the list of the Attribute
	 * @return null if the Attribute is not a list, otherwise the Class of the elements of the list of the Attribute
	 */
	public Class<?> getElementClass(){
		if (!isList){
			return null;
		}
		return elementType;
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
		
		Set<Attribute> matches = attributes.get(name);
		for (Attribute a: matches){
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
