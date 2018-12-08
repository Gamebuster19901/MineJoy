package com.gamebuster19901.minejoy.gson.exception;

import java.io.IOException;

/**
 *	Subclasses of GsonLinkageException are thrown when the gson adapter or the object its deserializing have incompatibly changed.
 */

@SuppressWarnings("serial")
public class GsonLinkageException extends IOException {

	public GsonLinkageException() {
		super();
	}
	
	public GsonLinkageException(String message) {
		super(message);
	}
	
	public GsonLinkageException(Throwable cause) {
		super(cause);
	}
	
	public GsonLinkageException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
