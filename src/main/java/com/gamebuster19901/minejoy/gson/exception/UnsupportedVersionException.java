package com.gamebuster19901.minejoy.gson.exception;

/**
 * Thrown when the gson adapter detects an instance of a class that was serialized with an incompatable
 * serializer version.
 */

@SuppressWarnings("serial")
public class UnsupportedVersionException extends GsonFormatException{

	public UnsupportedVersionException() {
		super();
	}
	
	public UnsupportedVersionException(String message) {
		super(message);
	}
	
	public UnsupportedVersionException(Throwable cause) {
		super(cause);
	}
	
	public UnsupportedVersionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
