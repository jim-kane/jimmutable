package org.kane.base.serialization.reader;

import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Optional;
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
	
	public boolean hasValue() { return value != null; }
	
	public String getOptionalValueAsString(String default_value) 
	{ 
		if ( !hasValue() ) return default_value;
		return value;
	}
	
	public Character getOptionalValueAsCharacter(Character default_value)
	{
		if ( !hasValue() ) return default_value;
		if ( value.length() > 1 ) return default_value;
		
		return value.charAt(0);
	}
	
	public Boolean getOptionalValueAsBoolean(Boolean default_value)
	{
		if ( !hasValue() ) return default_value;
		
		if ( value.equalsIgnoreCase("true") ) return true;
		if ( value.equalsIgnoreCase("t") ) return true;
		if ( value.equals("1") ) return true;
		
		if ( value.equalsIgnoreCase("false") ) return false;
		if ( value.equalsIgnoreCase("f") ) return false;
		if ( value.equals("0") ) return false;
		
		return default_value;
	}
	
	public Byte getOptionalValueAsByte(Byte default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Byte(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Short getOptionalValueAsShort(Short default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Short(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Integer getOptionalValueAsInteger(Integer default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Integer(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Long getOptionalValueAsLong(Long default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Long(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Float getOptionalValueAsFloat(Float default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Float(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	public Double getOptionalValueAsDouble(Double default_value)
	{
		if ( !hasValue() ) return default_value;
		
		try
		{
			return new Double(value);
		}
		catch(Exception e)
		{
			return default_value;
		}
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
	
	
	public boolean remove(ReadTree t)
	{
		Iterator<ReadTree> itr = iterator();
		
		while(itr.hasNext())
		{
			if ( itr.next() == t ) 
			{
				itr.remove();
				return true;
			}
		}
		
		return false;
	}
	
	protected void removeLast()
	{
		if ( children == null ) return;
		
		children.removeLast();
	}
	
	
	
	
	/*
	public String readString(FieldName field_name, String default_value)
	{
		ReadTree t = find(field_name, null);
		
		if ( t == null ) return default_value; // no node with the specified name
		
		if ( t.hasValue() )
		{
			remove(t);
			return t.getOptionalValue(default_value);
		}
			
		ReadTree primative_type = t.find(FieldName.FIELD_NAME_TYPE_HINT, null);
		
		if ( isPrimative() )
		{
			if ( isTypeHint(TypeName.TYPE_NAME_NULL) )
			{
				remove(t);
				return default_value;
			}
			
			ReadTree primative_value = t.find(FieldName.FIELD_NAME_PRIMATIVE_VALUE, null);
			
			if ( primative_value != null && primative_value.hasValue() )
			{
				remove(t);
				return primative_value.getOptionalValue(default_value);
			}
			
			if ( isTypeHint(TypeName.TYPE_NAME_STRING) )
			{
				// in XML, an explicitly written primative value for a string will have a null value.  This is taken to mean the blank string (not null, wich would be written explicitly, and not as a string)
				if ( !primative_value.hasValue() )
				{
					remove(t);
					return "";
				}
			}
		}
		
		return default_value; // could not read as a string
	}*/
	
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
			builder.append(String.format(": [%s]", getOptionalValueAsString(null)));
		}
		
		builder.append("\n");
		
		for ( ReadTree child : this )
		{
			child.diagnosticPrint(builder, indent+1);
		}
	}
}
