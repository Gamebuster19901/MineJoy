package com.gamebuster19901.minejoy.exception;

/**
 *  A duplicate element has been supplied where a duplicate is not allowed.
 *
 *	Cause: A duplicate element was provided where a duplicate is not allowed/expected.
 *	Action: Ensure that the input data does not contain duplicates.
 * 
 */

public class DuplicateElementException extends IllegalArgumentException{
	public DuplicateElementException(String name) {
		super(name);
	}
	
	public DuplicateElementException(Object o) {
		this(o.toString());
	}
	
	public DuplicateElementException(String name, Throwable cause) {
		super(name, cause);
	}
	
	public DuplicateElementException(Object o, Throwable cause) {
		this(o.toString(), cause);
	}
}
