package com.gamebuster19901.minejoy.exception;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class UnknownConsoleException extends NoSuchElementException{
	public UnknownConsoleException() {
		super();
	}
	
	public UnknownConsoleException(short codepoint) {
		this(codepoint + "");
	}
	
	public UnknownConsoleException(String message) {
		super(message);
	}

	public UnknownConsoleException(String console, Throwable cause) {
		this(console);
		this.initCause(cause);
	}
}
