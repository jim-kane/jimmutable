package org.kane.base.serialization.reader;

import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kane.base.exceptions.SerializeException;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.TypeName;
import org.kane.base.utils.Validator;


public class ObjectReader implements Iterable<ObjectReader>
{
	static protected Map<TypeName,Class> standard_object_types = new ConcurrentHashMap();
	
	private FieldName field_name; // required
	private String value; // optional
	private TypeName type_hint; // optional
	private LinkedList<ObjectReader> children; // optional
	
	public ObjectReader(FieldName field_name)
	{
		Validator.notNull(field_name);
		
		this.field_name = field_name;
	}
	
	public void setValue(String value)
	{
		this.value = value;
		
		// We always intern the type hint, for faster comparison later on
		if ( field_name.equals(FieldName.FIELD_NAME_TYPE_HINT) )
		{
			this.type_hint = new TypeName(value);
		}
		
		// The base 64 primative values are handled quite gently...
		if ( field_name.equals(FieldName.FIELD_NAME_PRIMITIVE_VALUE_BASE64) )
		{
			field_name = FieldName.FIELD_NAME_PRIMITIVE_VALUE;
			this.value = new String(Base64.getDecoder().decode(value));
		}
	}
	
	public FieldName getSimpleFieldName() { return field_name; }

	public boolean hasChildren() { return children != null && !children.isEmpty(); }
	
	public boolean hasValue() { return value != null; }
	
	private String getOptionalValue(String default_value) 
	{ 
		if ( !hasValue() ) return default_value;
		return value;
	}
	
	private String getPrimativeValueAsString(String default_value)
	{
		if ( hasValue() )
		{
			return value;
		}
		
		if ( isTypeHint(TypeName.TYPE_NAME_NULL) )
		{
			return default_value;
		}
		
		if ( isPrimativeObject() )
		{
			ObjectReader value = find(FieldName.FIELD_NAME_PRIMITIVE_VALUE, null);
			
			if ( value == null ) return default_value;
			
			if ( isTypeHint(TypeName.TYPE_NAME_STRING) && !value.hasValue() ) // a null value in a String primitive object means the empty string...
				return "";
			
			return value.getOptionalValue(default_value);
		}
		
		return default_value;
	}
	
	public Iterator<ObjectReader> iterator() 
	{
		if ( children == null ) return Collections.emptyIterator();
		
		return children.iterator();
	}
	
	public void add(ObjectReader child)
	{
		if ( children == null ) children = new LinkedList();
		children.add(child);
	}
	
	public ObjectReader find(FieldName field_name, ObjectReader default_value)
	{
		for ( ObjectReader child : this )
		{
			if ( child.getSimpleFieldName().equals(field_name) )
			{
				return child;
			}
		}
		
		return default_value;
	}
	
	public TypeName getOptionalTypeHint(TypeName default_value)
	{
		if ( type_hint != null ) return type_hint;
		
		ObjectReader t = find(FieldName.FIELD_NAME_TYPE_HINT,null);
		
		if ( t == null || t.type_hint == null ) return default_value;
		
		type_hint = t.type_hint;
		return type_hint;
	}
	
	public boolean hasTypeHint() 
	{
		return getOptionalTypeHint(null) != null;
	}
	
	public boolean isPrimativeObject()
	{
		TypeName tn = getOptionalTypeHint(null);
		if ( tn == null ) return false;
		return tn.isPrimative();
	}
	
	public boolean isTypeHint(TypeName name_to_test)
	{
		Validator.notNull(name_to_test);
		
		TypeName tn = getOptionalTypeHint(null);
		if ( tn == null ) return false;
		return tn.equals(name_to_test);
	}
	
	protected void removeLast()
	{
		if ( children == null ) return;
		
		children.removeLast();
	}
	
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		diagnosticPrint(b, 0);
		
		return b.toString().trim();
	}
	
	private void diagnosticPrint(StringBuilder builder, int indent)
	{
		for ( int i = 0; i < indent; i++ )
		{
			builder.append('\t');
		}
		
		builder.append(field_name.getSimpleName());
		
		if ( hasValue() )
		{
			builder.append(String.format(": [%s]", getOptionalValue(null)));
		}
		
		builder.append("\n");
		
		for ( ObjectReader child : this )
		{
			child.diagnosticPrint(builder, indent+1);
		}
	}
	
	public String asString(String default_value)
	{
		return getPrimativeValueAsString(default_value);
	}
	
	public Character asCharacter(Character default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		
		if ( primative_value == null ) return default_value;
		if ( primative_value.length() > 1 ) return default_value;
		
		return primative_value.charAt(0);
	}
	
	public Boolean asBoolean(Boolean default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		
		if ( primative_value == null ) return default_value;
		
		if ( primative_value.equalsIgnoreCase("true") ) return true;
		if ( primative_value.equalsIgnoreCase("t") ) return true;
		if ( primative_value.equals("1") ) return true;
		
		if ( primative_value.equalsIgnoreCase("false") ) return false;
		if ( primative_value.equalsIgnoreCase("f") ) return false;
		if ( primative_value.equals("0") ) return false;
		
		return default_value;
	}
	
	public Byte asByte(Byte default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Byte(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Short asShort(Short default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Short(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Integer asInteger(Integer default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Integer(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Long asLong(Long default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Long(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Float asFloat(Float default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Float(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Double asDouble(Double default_value)
	{
		String primative_value =  getPrimativeValueAsString(null);
		if ( primative_value == null ) return default_value;
		
		try
		{
			return new Double(primative_value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Object asObject(Object default_value) 
	{
		return asObject(default_value,true);
	}
	
	private Object asObject(Object default_value, boolean complete_standard_object) 
	{
		// Special handling for null fields
		if ( !hasChildren() && !hasValue() )
			return default_value;
			
		if ( isPrimativeObject() )
		{
			if ( isTypeHint(TypeName.TYPE_NAME_NULL) ) 
			{
				return default_value;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_STRING) )
			{
				String ret = asString(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_CHAR) )
			{
				Character ret = asCharacter(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_BOOLEAN) )
			{
				Boolean ret = asBoolean(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_BYTE) )
			{
				Byte ret = asByte(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_SHORT) )
			{
				Short ret = asShort(null);
				if ( ret == null ) return default_value;
				return ret;
			} 
			
			if ( isTypeHint(TypeName.TYPE_NAME_INT) )
			{
				Integer ret = asInteger(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_LONG) )
			{
				Long ret = asLong(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_FLOAT) )
			{
				Float ret = asFloat(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_DOUBLE) )
			{
				Double ret = asDouble(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			return default_value; // unknown primitive type? (should not be possible)
		}
		
		TypeName type_name = getOptionalTypeHint(null);
		
		if ( type_name == null )
			throw new SerializeException("Attempt to read object, but not a primitive and no type hint present");
		
		Class c = standard_object_types.get(type_name); 
		
		if ( c == null )
		{
			throw new SerializeException(String.format("The type name %s is not registered.  Register with ObjectReader.registerTypeName",type_name.getSimpleName()));
		}
		
		// Standard object converter...
		if ( c != null )
		{
			try
			{
				Constructor constructor = c.getConstructor(ObjectReader.class);
				Object ret = constructor.newInstance(this);
				
				if ( complete_standard_object && ret instanceof StandardObject )
				{
					((StandardObject)ret).complete();
				}
				
				return ret;
			}
			catch(NoSuchMethodException e)
			{
				throw new SerializeException(String.format("No constructor found %s(ReadTree t)", c.getSimpleName()),e);
			}
			catch(SerializeException e2)
			{
				throw e2;
			}
			catch(Exception e3)
			{
				throw new SerializeException("Error reading object",e3);
			}
		}
		
		return default_value;
	}
	
	public String getString(FieldName field_name, String default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asString(default_value);
	}
	
	public Boolean getBoolean(FieldName field_name, Boolean default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asBoolean(default_value);
	}
	
	public Character getCharacter(FieldName field_name, Character default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asCharacter(default_value);
	}
	
	public Byte getByte(FieldName field_name, Byte default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asByte(default_value);
	}
	
	public Short getShort(FieldName field_name, Short default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asShort(default_value);
	}
	
	public Integer getInt(FieldName field_name, Integer default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asInteger(default_value);
	}
	
	public Long getInt(FieldName field_name, Long default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asLong(default_value);
	}
	
	public Float getFloat(FieldName field_name, Float default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asFloat(default_value);
	}
	
	public Double getDouble(FieldName field_name, Double default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asDouble(default_value);
	}
	
	public Object getObject(FieldName field_name, Object default_value)
	{
		ObjectReader child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asObject(default_value);
	}
	
	static public enum OnError
	{
		SKIP,
		THROW_EXCEPTION;
	}
	
	public <C extends Collection> C getCollection(FieldName field_name, C empty_collection, ReadAs type, OnError on_error)
	{
		Validator.notNull(field_name);
		Validator.notNull(empty_collection);
		Validator.notNull(type);
		
		C ret = empty_collection;
		
		for ( ObjectReader child : children )
		{
			if ( child.getSimpleFieldName().equals(field_name) ) 
			{
				Object obj = type.readAs(child);
				
				if ( obj == null ) 
				{
					if ( on_error == OnError.SKIP ) continue;
					else throw new SerializeException("Could not read object in collection");
				}
				
				ret.add(obj);
			}
		}
		
		return ret;
	}
	
	public <M extends Map> M getMap(FieldName field_name, M empty_map, ReadAs key_type, ReadAs value_type, OnError on_error)
	{
		Validator.notNull(field_name);
		Validator.notNull(empty_map);
		Validator.notNull(key_type);
		Validator.notNull(value_type);
		Validator.notNull(on_error);
		
		M ret = empty_map;
		
		for ( ObjectReader entry : children )
		{
			if ( entry.getSimpleFieldName().equals(field_name) ) 
			{
				ObjectReader key_tree = entry.find(FieldName.FIELD_KEY, null);
				ObjectReader value_tree = entry.find(FieldName.FIELD_VALUE, null);
				
				if ( key_tree == null || value_tree == null ) 
				{
					if ( on_error == OnError.SKIP ) continue;
					if ( on_error == OnError.THROW_EXCEPTION ) throw new SerializeException("Could not read key/value pair");
				}
				
				Object key = key_type.readAs(key_tree);
				Object value = value_type.readAs(value_tree);
				
				if ( key == null || value == null ) 
				{
					if ( on_error == OnError.SKIP ) continue;
					if ( on_error == OnError.THROW_EXCEPTION ) throw new SerializeException("Could not read key/value pair");
				}
				
				ret.put(key, value);
			}
		}
		
		return ret;
	}
	
	static public Object deserialize(String document) throws SerializeException
	{
		return deserialize(document, true);
	}
	
	static public Object deserialize(String document, boolean complete_standard_object) throws SerializeException
	{
		ObjectReader t = Parser.parse(document);
		
		Object ret = t.asObject(null, complete_standard_object);
		
		if ( ret == null ) 
			throw new SerializeException("Unable to read document!");
		
		return ret;
	}
	
	static public void registerTypeName(Class c)
	{
		try
		{
			TypeName type_name = (TypeName)c.getField("TYPE_NAME").get(null);
			if ( type_name.isPrimative() ) throw new SerializeException("Attempt to register a primative type name using registerTypeName.  Did you try to register a Stringable?");
			standard_object_types.put(type_name, c);
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

