package org.kane.base.serialization;

import org.kane.base.exceptions.ValidationException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.writer.ObjectWriter;

final public class FieldName extends StandardImmutableObject
{
	static private final TypeName TYPE_NAME = new TypeName("jimmutable.FieldName");
	static private final FieldName FIELD_NAME = new FieldName("name");
	
	static public FieldName FIELD_NAME_TYPE_HINT = new FieldName("type_hint");
	static public FieldName FIELD_NAME_PRIMATIVE_VALUE = new FieldName("primative_value");
	static public FieldName FIELD_NAME_PRIMATIVE_VALUE_BASE64 = new FieldName("primative_value_base_64");
	static public FieldName FIELD_DOCUMENT_ROOT = new FieldName("parsed_document_root_element");
	
	static public final FieldName FIELD_KEY = new FieldName("key");
	static public final FieldName FIELD_VALUE = new FieldName("value");
	
	static public final FieldName FIELD_ARRAY_ELEMENT = new FieldName("array_element_do_not_write_field_name");
	
	private String name; 
	
	public FieldName(String name)
	{ 
		if ( name == null ) name = "";
		this.name = name;
		
		complete();
	}
	
	public FieldName(ObjectReader t)
	{
		name = t.getString(FIELD_NAME, null);
	}
	
	public TypeName getTypeName() { return TYPE_NAME; }

	public void write(ObjectWriter writer) 
	{
		writer.writeString(FIELD_NAME, getSimpleName());
	}

	public String getSimpleName() { return name; }

	public int compareTo(Object o)  
	{
		if ( !(o instanceof FieldName) ) return -1;
		
		FieldName other = (FieldName)o;
		
		return getSimpleName().compareTo(other.getSimpleName());
	}

	
	public void normalize()
	{
	}

	
	public void validate() 
	{
		Validator.notNull(name);
		
		char chars[] = name.toCharArray(); 
		
		if ( chars.length == 0 )
			throw new ValidationException("Field names must contain at least one letter");
	
		for ( int i = 0; i < chars.length; i++ )
		{
			char ch = chars[i];
			
			if ( i == 0 )
			{
				if ( ch >= 'a' && ch <= 'z') continue;
				throw new ValidationException("Field names must start with a lower case letter");
			}
			else
			{
				if ( ch >= 'a' && ch <= 'z') continue;
				if ( ch >= '0' && ch <= '9') continue;
				if ( ch == '_' ) continue;
				
				throw new ValidationException(String.format("Illegal character '%c' in field name \"%s\"", ch, name));
			}
		}
	}
	
	public void freeze() {}
	
	public int hashCode() 
	{
		return getSimpleName().hashCode();
	}

	
	public boolean equals(Object o) 
	{
		if ( !(o instanceof FieldName) ) return false;
		
		FieldName other = (FieldName)o;
		return name.equals(other.name);
	}
}
