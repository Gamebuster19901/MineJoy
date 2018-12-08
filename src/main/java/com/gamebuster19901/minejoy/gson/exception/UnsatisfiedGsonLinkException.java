package com.gamebuster19901.minejoy.gson.exception;

/**
 *  Thrown if the gson adapter cannot find an appropriate java class definition for the gson object it's deserializing
 */

@SuppressWarnings("serial")
public class UnsatisfiedGsonLinkException extends GsonLinkageException{

	public UnsatisfiedGsonLinkException() {
		super();
	}
	
	public UnsatisfiedGsonLinkException(String message) {
		super(message);
	}
	
	public UnsatisfiedGsonLinkException(Throwable cause) {
		super(cause);
	}
	
	public UnsatisfiedGsonLinkException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
