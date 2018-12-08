package com.gamebuster19901.minejoy.gson.exception;

/**
 *	Thrown when the gson adapter attempts to read a gson file and determines that the file is malformed and cannot
 *  be deserialized into an object
 */

@SuppressWarnings("serial")
public class GsonFormatException extends GsonLinkageException{
	public GsonFormatException() {
		super();
	}
	
	public GsonFormatException(String message) {
		super(message);
	}
	
	public GsonFormatException(Throwable cause) {
		super(cause);
	}
	
	public GsonFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
