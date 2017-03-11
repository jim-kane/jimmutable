package org.kane.base.serialization;

import org.kane.base.immutability.StandardImmutableObject;

public class TypeName extends StandardImmutableObject
{
	static public TypeName TYPE_NAME_OBJECT = new TypeName("object");
	
	private String name; // required, always interned
	
	public TypeName(String name)
	{ 
		if ( name == null ) name = "";
		this.name = name.intern();
		
		complete();
	}
	
	public String getSimpleName() { return name; }

	public int compareTo(Object o)  
	{
		if ( !(o instanceof TypeName) ) return -1;
		
		TypeName other = (TypeName)o;
		
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
			throw new ValidationException("Type names must contain at least one letter");
	
		for ( int i = 0; i < chars.length; i++ )
		{
			char ch = chars[i];
			
			if ( i == 0 )
			{
				if ( ch >= 'a' && ch <= 'z') continue;
				throw new ValidationException("Type names must start with a lower case letter");
			}
			else
			{
				if ( ch >= 'a' && ch <= 'z') continue;
				if ( ch >= '0' && ch <= '9') continue;
				if ( ch == '_' ) continue;
				
				throw new ValidationException(String.format("Illegal character '%c' in type name \"%s\"", ch, name));
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
		if ( !(o instanceof TypeName) ) return false;
		
		TypeName other = (TypeName)o;
		return getSimpleName() == other.getSimpleName();
	}
}