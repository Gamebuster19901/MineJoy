package com.gamebuster19901.minejoy.controller.layout;

import org.apache.commons.lang3.BooleanUtils;
import org.mariuszgromada.math.mxparser.Function;

import com.gamebuster19901.minejoy.exception.FunctionException;
import com.gamebuster19901.minejoy.exception.InvalidFunctionException;
import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(LayoutElementAdapter.class)
public class ButtonElement extends LayoutElement<Boolean>{
	public static final Function DEFAULT_FUNCTION = new Function("buttonFunction(val, inverted) = if(inverted, ~val, val)");
	
	private volatile boolean inverted = false;
	
	public ButtonElement() {
		super();
	}
	
	public ButtonElement(boolean inverted) {
		super();
		setInverted(inverted);
	}
	
	@Override
	public Boolean getValue(float input) {
		eval(input);
		return functionValue > 0;
	}
	
	public Boolean getValue(boolean input) {
		return getValue(BooleanUtils.toInteger(input));
	}
	
	@Override
	protected final Function getDefaultFunction() {
		return DEFAULT_FUNCTION;
	}
	
	protected final void setInverted(boolean inversion) {
		this.inverted = inversion;
	}
	
	public final boolean isInverted() {
		return inverted;
	}
	
	@Override
	protected final boolean isValidEvaluation(float value) {
		if(isValid()) {
			double val = function.calculate((double)value, BooleanUtils.toInteger(inverted));
			if(isReturnValueValid(val)) {
				functionValue = val;
				return true;
			}
		}
		return false;
	}

	@Override
	protected void checkFunctionValid(Function function) throws FunctionException {
		if(function.checkSyntax()) {
			for(double val = 0; val < 1; val++) {
				for(double inverted = 0; inverted < 1; inverted++) {
					double calculatedValue = function.calculate(val, inverted);
					if(Double.isNaN(calculatedValue)) {
						throw new InvalidFunctionException(function.getErrorMessage());
					}
					if(calculatedValue != 0 && calculatedValue != 1) {
						throw new FunctionException("Button functions must always return 0 or 1! The function " + function.getFunctionExpressionString() + " with values " + "val = " + val + " and inverted = " + inverted + " return " + calculatedValue + " instead!");
					}
				}
			}
		}
		else {
			throw new InvalidFunctionException(function.getErrorMessage());
		}
	}
	
}
