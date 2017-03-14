package org.kane.base.serialization.reader;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.SerializeException;
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
		ReadTree t = Parser.parse(document, null);
		if ( t == null ) return default_value;
		
		ObjectReader r = new ObjectReader(t);
		
		return ObjectReaderUtils.asObject(t, default_value);
	}
	
	public String readString(FieldName field_name, String default_value)
	{
		ReadTree child = t.find(field_name, null);
		if ( child == null ) return default_value;
		
		t.remove(child);
		
		return ObjectReaderUtils.asString(child, default_value);
	}
	
	static public void registerType(TypeName type, Class c)
	{
		Validator.notNull(type, c);
		
		standard_object_types.put(type, c);
	}
}
