package com.gamebuster19901.minejoy.gson;

import com.gamebuster19901.minejoy.Minejoy;
import com.gamebuster19901.minejoy.controller.layout.AxisElement;
import com.gamebuster19901.minejoy.controller.layout.ButtonElement;
import com.gamebuster19901.minejoy.controller.layout.LayoutElement;

import static com.gamebuster19901.minejoy.gson.LayoutElementAdapter.Names.*;
import static com.gamebuster19901.minejoy.gson.LayoutElementAdapter.Types.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import org.apache.logging.log4j.Level;

public final class LayoutElementAdapter extends TypeAdapter<LayoutElement>{

	private static final long LOWEST_COMPATIBLE_VERSION = 1l;
	private static final long CURRENT_VERSION = 2l;
	
	@Override
	public LayoutElement read(JsonReader reader) throws IOException {
		long version = 0;
		String type = "";
		boolean inverted = false;
		boolean invertedInGame = false;
		boolean invertedInGui = false;
		String expression = "v";
		
		reader.beginObject();
		while(reader.hasNext()) {
			String name = reader.nextName();
			switch(name) {
				case VERSION:
					version = reader.nextLong();
					break;
				case TYPE:
					type = reader.nextString();
					break;
				case INVERTED:
					inverted = reader.nextBoolean();
					break;
				case INVERTED_GAME:
					invertedInGame = reader.nextBoolean();
					break;
				case INVERTED_GUI:
					invertedInGui = reader.nextBoolean();
					break;
				case EXPRESSION:
					expression = reader.nextString();
					break;
				default:
					Minejoy.LOGGER.log(Level.WARN, "Unknown data type detected when deserializing LayoutElement: " + name);
					break;
			}
		}
		reader.endObject();
		
		if(version >= LOWEST_COMPATIBLE_VERSION && version <= CURRENT_VERSION && (type.equals(AXIS) || type.equals(BUTTON))) {
			if(type.equals(AXIS)) {
				return new AxisElement(invertedInGame, invertedInGui, expression);
			}
			else if(type.equals(BUTTON)) {
				return new ButtonElement(inverted);
			}
		}
		else {
			if(version > CURRENT_VERSION) {
				throw new UnsupportedClassVersionError("Unable to support future version " + version + "");
			}
			if(version < LOWEST_COMPATIBLE_VERSION) {
				throw new ClassFormatError("Malformed JSON");
			}
			if(type.equals(AXIS) || type.equals(BUTTON)){
				throw new VerifyError("Unsupported class: " + type);
			}
		}
		throw new AssertionError("Unreachable code, you should never see this!");
	}

	@Override
	public void write(JsonWriter writer, LayoutElement element) throws IOException {
		writer.beginObject();
		
			writer.name(VERSION);
			writer.value(CURRENT_VERSION);
		
			writer.name(TYPE);
			writer.value(Types.getType(element.getClass()));
			
			if(element instanceof ButtonElement) {
				writer.name(INVERTED);
				writer.value(((ButtonElement) element).isInverted());
			}
			else if(element instanceof AxisElement){
				writer.name(INVERTED_GAME);
				writer.value(((AxisElement) element).isInvertedInGame());
				
				writer.name(INVERTED_GUI);
				writer.value(((AxisElement) element).isInvertedInGui());
				
				writer.name(EXPRESSION);
				writer.value(element.getExpression().getExpressionString());
			}
			
		writer.endObject();
	}
	
	static interface Names{
		String VERSION = "version";
		String TYPE = "type";
		String INVERTED = "inverted";
		String INVERTED_GAME = "invertedInGame";
		String INVERTED_GUI = "invertedInGui";
		String EXPRESSION = "expression";
	}
	
	static interface Types{
		String AXIS = AxisElement.class.getSimpleName();
		String BUTTON = ButtonElement.class.getSimpleName();
		
		public static String getType(Class clazz) {
			if(clazz == AxisElement.class) {
				return AXIS;
			}
			if(clazz == ButtonElement.class) {
				return BUTTON;
			}
			throw new IllegalArgumentException(clazz.getCanonicalName() + " is not a valid class to serialize with LayoutElementSerializer");
		}
	}

}
