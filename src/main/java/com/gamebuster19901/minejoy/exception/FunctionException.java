package com.gamebuster19901.minejoy.exception;

@SuppressWarnings("serial")
public class FunctionException extends Exception {
	public FunctionException(String name) {
		super(name);
	}
	
	public FunctionException(Object o) {
		this(o.toString());
	}
	
	public FunctionException(String name, Throwable cause) {
		super(name, cause);
	}
	
	public FunctionException(Object o, Throwable cause) {
		this(o.toString(), cause);
	}
}
