package org.kane.base.serialization;

import org.kane.base.immutability.collections.FieldArrayList;

final public class FieldName extends StandardObject
{
	private String name; // required, always interned
	
	public FieldName(String name)
	{ 
		if ( name == null ) name = "";
		this.name = name.intern();
		
		complete();
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
	
	public int hashCode() 
	{
		return getSimpleName().hashCode();
	}

	
	public boolean equals(Object o) 
	{
		if ( !(o instanceof FieldName) ) return false;
		
		FieldName other = (FieldName)o;
		return getSimpleName() == other.getSimpleName();
	}
}
