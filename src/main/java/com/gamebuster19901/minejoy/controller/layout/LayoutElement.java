package com.gamebuster19901.minejoy.controller.layout;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.syntaxchecker.ParseException;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.exception.FunctionException;
import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(LayoutElementAdapter.class)
public abstract class LayoutElement<V>{
	protected transient final Lock LOCK = new ReentrantLock();
	
	protected volatile Function function;
	
	private transient volatile boolean valid = true;
	protected transient volatile double functionValue = 0;
	
	public LayoutElement() {
		this.function = getDefaultFunction();
	}
	
	public LayoutElement(String function) {
		setFunction(function);
	}
	
	public LayoutElement(Function function) {
		setFunction(function);
	}

	public void setFunction(String function) {
		setFunction(new Function(function));
	}
	
	protected void setFunction(Function function) {
		boolean valid = true;
		try {
			checkFunctionValid(function);
		} catch (FunctionException e) {
			Minejoy.LOGGER.catching(e);
			valid = false;
		}
		this.function = function;
		this.valid = valid;
	}
	
	protected abstract Function getDefaultFunction();
	
	public abstract V getValue(float input);
	
	protected final float eval(float value) {
		if(valid && !isValidEvaluation(value)) {
			Minejoy.LOGGER.catching(new FunctionException("Function " + function.getFunctionExpressionString() + " returned illegal value or is malformed. Value: " + value));
		}
		return (float) functionValue;
	}
	
	/**
	 * @return true if the the value of the layout element was successfully calculated, false otherwise.
	 * 
	 * If the value was successfully calculated, then functionValue is set to the new value;
	 */
	protected abstract boolean isValidEvaluation(float value);
	
	/**
	 * @return a ParseException containing the error message
	 * 
	 * @throws IllegalStateException if the layoutElement did not error
	 */
	public ParseException getError() {
		if(getDefaultFunction().getFunctionExpressionString().equals(function.getFunctionExpressionString())) {
			return new ParseException("Error in default function: " + getDefaultFunction().getErrorMessage());
		}
		if(!function.getErrorMessage().contains("no errors")) {
			return new ParseException(function.getErrorMessage());
		}
		throw new IllegalStateException("There was no error to get! Call eval() first!");
	}
	
	protected abstract void checkFunctionValid(Function function) throws FunctionException;
	
	/**
	 * @return true if this LayoutElement has not been marked as invalid
	 */
	protected final boolean isValid(){
		return valid;
	}
	
	protected boolean isReturnValueValid(double value) {
		return !(Double.isNaN(value) || Double.isInfinite(value));
	}
	
	public final String getExpressionString() {
		return function.getFunctionExpressionString();
	}
}
