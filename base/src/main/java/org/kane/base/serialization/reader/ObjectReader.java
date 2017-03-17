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
	
	static public Object deserialize(String document) throws SerializeException
	{
		return deserialize(document, true);
	}
	
	static public Object deserialize(String document, boolean complete_standard_object) throws SerializeException
	{
		ReadTree t = Parser.parse(document);
		
		Object ret = t.asObject(null);
		
		if ( ret == null ) 
			throw new SerializeException("Unable to read document!");
		
		if ( complete_standard_object && ret instanceof StandardObject ) 
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
	
	static public void registerTypeName(Class c)
	{
		try
		{
			TypeName type_name = (TypeName)c.getField("TYPE_NAME").get(null);
			registerType(type_name,c);
		}
		catch(Exception e)
		{
			System.err.println(String.format("Unable to register a type name for %s, could not read static public field %s.TYPE_NAME", c.getSimpleName(),c.getSimpleName()));
			e.printStackTrace();
		}
	}
	
	static public boolean isTypeRegistered(TypeName type)
	{
		if ( type == null ) return false;
		return standard_object_types.containsKey(type);
	}
}
