package com.gamebuster19901.minejoy.gson.exception;

/**
 *	Thrown when an unexpected error occurs when a gson adapter is serializing or deserializing an object
 */
@SuppressWarnings("serial")
public class GsonError extends Error{

	public GsonError() {
		super();
	}
	
	public GsonError(String message) {
		super(message);
	}
	
	public GsonError(Throwable cause) {
		super(cause);
	}
	
	public GsonError(String message, Throwable cause) {
		super(message, cause);
	}
	
}
