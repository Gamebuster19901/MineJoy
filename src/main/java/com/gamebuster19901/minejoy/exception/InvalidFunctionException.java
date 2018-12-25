package com.gamebuster19901.minejoy.exception;

@SuppressWarnings("serial")
public class InvalidFunctionException extends FunctionException{
	public InvalidFunctionException(String name) {
		super(name);
	}
	
	public InvalidFunctionException(Object o) {
		this(o.toString());
	}
	
	public InvalidFunctionException(String name, Throwable cause) {
		super(name, cause);
	}
	
	public InvalidFunctionException(Object o, Throwable cause) {
		this(o.toString(), cause);
	}
}
