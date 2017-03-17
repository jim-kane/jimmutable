package org.kane.base.serialization.reader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kane.base.serialization.SerializeException;
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
	
	static public Object readDocument(String document) throws SerializeException
	{
		return readDocument(document, true);
	}
	
	static public Object readDocument(String document, boolean complete) throws SerializeException
	{
		ReadTree t = Parser.parse(document);
		
		Object ret = t.asObject(null);
		
		if ( ret == null ) 
			throw new SerializeException("Unable to read document!");
		
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
	
	static public boolean isTypeRegistered(TypeName type)
	{
		if ( type == null ) return false;
		return standard_object_types.containsKey(type);
	}
}
