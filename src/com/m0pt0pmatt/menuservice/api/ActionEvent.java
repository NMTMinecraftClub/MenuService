package com.m0pt0pmatt.menuservice.api;

import java.util.EventObject;

/**
 * An ActionEvent is an EventObject that is triggered when a Component is activated by a certain player.
 * ActionEvents can be caught by ActionListeners, which can then act accordingly, depending upon the Action.
 * 
 * An ActionEvent has one purpose: to supply the Listener with the Action which was activated.
 * This is done by its getAction() method.
 * 
 * @author mbroomfield
 *
 */
public class ActionEvent extends EventObject{

	/**
	 * EventObject implements Serializable, so ActionEvents need a default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an ActionEvent, given an Action
	 * @param action the Action which was activated
	 */
	public ActionEvent(Action action) {
		super(action);
	}
	
	/**
	 * Returns the Action of the ActionEvent
	 * @return the Action of the ActionEvent
	 */
	public Action getAction(){
		return (Action) super.getSource();
	}
	

}
