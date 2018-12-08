package com.gamebuster19901.minejoy.controller.layout;

import java.lang.reflect.Field;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.logging.log4j.Level;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.syntaxchecker.ParseException;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(LayoutElementAdapter.class)
public abstract class LayoutElement<V>{

	protected static final Field ERROR_MESSAGE = ReflectionHelper.findField(Expression.class, "errorMessage");
	
	private volatile Expression expression;
	private transient volatile Argument argument = new Argument("v");
	
	protected transient volatile double expressionValue = Double.NaN;
	
	public LayoutElement() {
		this("v");
	}
	
	public LayoutElement(String expression) {
		setExpression(expression);
	}
	
	public LayoutElement(Expression expression) {
		setExpression(expression);
	}
	
	public void setExpression(String expression) {
		setExpression(new Expression(expression));
	}
	
	protected void setExpression(Expression expression) {
		this.expression = expression;
		this.expression.addArguments(argument);
	}
	
	protected abstract Expression getDefaultExpression();
	
	public final float eval(float value) {
		argument.setArgumentValue(value);
		if(eval()) {
			Object v = getValue();
			if(v instanceof Boolean) {
				if (((Boolean)v) == false) {
					return 0;
				}
				return 1;
			}
			else if (v instanceof Float) {
				return (float) v;
			}
			throw new AssertionError("Unknown evaluation type: " + v.getClass().getCanonicalName());
		}
		else {
			Minejoy.LOGGER.log(Level.ERROR, getError());
			return 0;
		}
	}
	
	/**
	 * @return true if the the value of the layout element could be calculated, false otherwise.
	 */
	private boolean eval() {
		synchronized(expression) {
			return (expressionValue = getDefaultExpression().calculate() + expression.calculate()) != Double.NaN;
		}
	}
	
	public abstract V getValue();
	
	/**
	 * @return a ParseException containing the error message
	 * 
	 * @throws IllegalStateException if the layoutElement did not error
	 */
	public ParseException getError() {
		if(expression.getErrorMessage() != null) {
			return new ParseException(expression.getErrorMessage());
		}
		throw new IllegalStateException("There was no error to get! Call eval() first!");
	}
	
	public Expression getExpression() {
		Expression expression = new Expression(this.expression.getExpressionString());
		try {
			ERROR_MESSAGE.set(expression, this.expression.getErrorMessage());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
		return expression;
	}
	
}
