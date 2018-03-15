package com.gamebuster19901.minejoy.exception;

import java.util.NoSuchElementException;

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
}
