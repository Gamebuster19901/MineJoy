package com.gamebuster19901.minejoy.controller.layout;

import org.apache.commons.lang3.BooleanUtils;
import org.mariuszgromada.math.mxparser.Function;

import com.gamebuster19901.minejoy.exception.FunctionException;
import com.gamebuster19901.minejoy.exception.InvalidFunctionException;
import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;

import com.google.gson.annotations.JsonAdapter;

import net.minecraft.client.Minecraft;

@JsonAdapter(LayoutElementAdapter.class)
public class AxisElement extends LayoutElement<Float>{
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static final Function DEFAULT_AXIS_FUNCTION = new Function("axisFunction(val, inverted, deadzone) = if(abs(val) > deadzone, if(inverted, -val, val), 0)");
	
	private double deadzone = 0.5d;
	private boolean invertedInGame = false;
	private boolean invertedInGui = false;
	
	public AxisElement() {
		super();
		setDeadzone(deadzone);
	}
	
	public AxisElement(boolean invertedInGame, boolean invertedInGui, double deadzone, String function) {
		super(function);
		this.invertedInGame = invertedInGame;
		this.invertedInGui = invertedInGui;
		this.deadzone = deadzone;
		setDeadzone(deadzone);
	}
	
	public AxisElement(boolean invertedInGame, boolean invertedInGui, double deadzone, Function function) {
		super(function);
		this.invertedInGame = invertedInGame;
		this.invertedInGui = invertedInGui;
		this.deadzone = deadzone;
		setDeadzone(deadzone);
	}
	
	public double getDeadzone() {
		return deadzone;
	}
	
	private void setDeadzone(double deadzone) {
		this.deadzone = deadzone;
	}
	
	public void setInvertedInGame(boolean inversion) {
		invertedInGame = inversion;
	}
	
	public void setInvertedInGui(boolean inversion) {
		invertedInGui = inversion;
	}
	
	public boolean isInvertedInGame() {
		return invertedInGame;
	}
	
	public boolean isInvertedInGui() {
		return invertedInGui;
	}
	
	public boolean getCurrentInversionStatus() {
		if(mc.currentScreen == null) {
			return invertedInGame;
		}
		return invertedInGui;
	}

	@Override
	protected Function getDefaultFunction() {
		return DEFAULT_AXIS_FUNCTION;
	}

	@Override
	public Float getValue(float input) {
		eval(input);
		return (float) functionValue;
	}

	@Override
	public void checkFunctionValid(Function function) throws FunctionException {
		if(function.checkSyntax()) {
			double[] values = new double[] {-1, 0, 1};
			for(double val : values) {
				for(double inverted = 0; inverted < 1; inverted++) {
					double calculatedValue = function.calculate(val, inverted);
					if(Double.isNaN(calculatedValue)) {
						throw new InvalidFunctionException(function.getErrorMessage());
					}
				}
			}
		}
		else {
			throw new InvalidFunctionException(function.getErrorMessage());
		}
	}

	@Override
	protected boolean isValidEvaluation(float value) {
		if(isValid()) {
			double val = function.calculate((double)value, BooleanUtils.toInteger(getCurrentInversionStatus()), deadzone);
			if(isReturnValueValid(val)) {
				functionValue = val;
				return true;
			}
		}
		return false;
	}
}
