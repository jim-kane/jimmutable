package org.kane.base.serialization.reader;

import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Optional;
import org.kane.base.serialization.SerializeException;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;

public class ReadTree implements Iterable<ReadTree>
{
	private FieldName field_name; // required
	private String value; // optional
	private TypeName type_hint; // optional
	private LinkedList<ReadTree> children; // optional
	
	public ReadTree(FieldName field_name)
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
		if ( field_name.equals(FieldName.FIELD_NAME_PRIMATIVE_VALUE_BASE64) )
		{
			field_name = FieldName.FIELD_NAME_PRIMATIVE_VALUE;
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
			ReadTree value = find(FieldName.FIELD_NAME_PRIMATIVE_VALUE, null);
			
			if ( value == null ) return default_value;
			
			if ( isTypeHint(TypeName.TYPE_NAME_STRING) && !value.hasValue() ) // a null value in a String primitive object means the empty string...
				return "";
			
			return value.getOptionalValue(default_value);
		}
		
		return default_value;
	}
	
	public Iterator<ReadTree> iterator() 
	{
		if ( children == null ) return Collections.emptyIterator();
		
		return children.iterator();
	}
	
	public void add(ReadTree child)
	{
		if ( children == null ) children = new LinkedList();
		children.add(child);
	}
	
	public ReadTree find(FieldName field_name, ReadTree default_value)
	{
		for ( ReadTree child : this )
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
		
		ReadTree t = find(FieldName.FIELD_NAME_TYPE_HINT,null);
		
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
		
		for ( ReadTree child : this )
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
			
			return default_value; // unknown primative type? (should not be possible)
		}
		
		if ( !hasTypeHint() ) return default_value;
		
		Class c = ObjectReader.standard_object_types.get(getOptionalTypeHint(null)); // type hint can't be null, because hasTypeHint is check above...
		
		if ( c != null )
		{
			try
			{
				Constructor constructor = c.getConstructor(ReadTree.class);
				return constructor.newInstance(this);
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
		
		// Standard object converter...
		return default_value;
	}
	
	public String getString(FieldName field_name, String default_value)
	{
		ReadTree child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asString(default_value);
	}
	
	public Object readObject(FieldName field_name, Object default_value)
	{
		ReadTree child = find(field_name, null);
		if ( child == null ) return default_value;
		return child.asObject(default_value);
	}
}