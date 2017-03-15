package org.kane.base.serialization.reader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;

public class ObjectReader 
{
	static protected Map<TypeName,Class> standard_object_types = new ConcurrentHashMap();
	
	private ReadTree t;
	
	protected ObjectReader(ReadTree t)
	{
		this.t = t;
	}
	
	static public Object readDocument(String document, Object default_value)
	{
		return readDocument(document, default_value,true);
	}
	
	static public Object readDocument(String document, Object default_value, boolean complete)
	{
		ReadTree t = Parser.parse(document, null);
		
		if ( t == null ) return default_value;
		
		Object ret = t.asObject(default_value);
		
		if ( complete && ret instanceof StandardObject ) 
		{
			StandardObject immutable_object = (StandardObject)ret;
			immutable_object.complete();
		}
		
		return ret;
	}

	static public void registerType(TypeName type, Class c)
	{
		Validator.notNull(type, c);
		
		standard_object_types.put(type, c);
	}
}
