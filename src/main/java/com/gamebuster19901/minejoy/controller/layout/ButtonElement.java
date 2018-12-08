package com.gamebuster19901.minejoy.controller.layout;

import static com.gamebuster19901.minejoy.controller.layout.ButtonElement.State.*;

import org.mariuszgromada.math.mxparser.Expression;

import com.gamebuster19901.minejoy.gson.LayoutElementAdapter;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(LayoutElementAdapter.class)
public class ButtonElement extends LayoutElement<Boolean>{
	public static final Expression DEFAULT_EXPRESSION = new Expression("0");
	
	
	private boolean inverted = false;
	
	public ButtonElement() {
		super();
	}
	
	public ButtonElement(boolean inverted) {
		super();
		setInverted(inverted);
	}
	
	public void setInverted(boolean inversion) {
		this.inverted = inversion;
		if(inverted) {
			this.setExpression(INVERTED.getExpression());
		}
		else {
			this.setExpression(NORMAL.getExpression());
		}
	}
	
	public boolean isInverted() {
		return inverted;
	}
	
	@Override
	public Boolean getValue() {
		return expressionValue > 0;
	}
	
	@Override
	protected Expression getDefaultExpression() {
		return new Expression("0");
	}
	
	public static enum State{
		NORMAL(new Expression("d")),
		INVERTED(new Expression("~(d)"));
		
		private final Expression expression;
		
		private State(Expression expression) {
			this.expression = expression;
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
		
		public boolean matches(Object o) {
			if(o instanceof Expression) {
				return expression.equals(o) || expression.getExpressionString().equals(((Expression) o).getExpressionString());
			}
			else if (o instanceof String) {
				return expression.getExpressionString().equals(o);
			}
			return false;
		}
	}
	
}
