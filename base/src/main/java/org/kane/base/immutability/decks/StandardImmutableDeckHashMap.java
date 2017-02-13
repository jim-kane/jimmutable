package org.kane.base.immutability.decks;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.serialization.Equality;
import org.kane.base.serialization.ValidationException;
import org.kane.base.serialization.Validator;

abstract public class StandardImmutableDeckHashMap extends StandardImmutableObject
{
	private FieldHashMap contents;

	public StandardImmutableDeckHashMap(Map initial_contents)
	{
		this.contents = new FieldHashMap(this,initial_contents);
	}
	
	abstract public Class getOptionalKeyValidationType(Class default_value);
	abstract public Class getOptionalValueValidationType(Class default_value);

	@Override
	public void normalize() 
	{	
	}

	public void validate() 
	{
		Validator.notNull(contents);
		
		// Check the keys' type...
		{
			Class key_validation_type = getOptionalKeyValidationType(null);
			
			if ( key_validation_type != null )
			{
				for ( Object obj : contents.keySet() )
				{
					if ( !key_validation_type.isInstance(obj) ) 
						throw new ValidationException("Expected keys to all be of type: "+key_validation_type);
				}
			}
		}
		
		// Check the values' type...
		{
			Class value_validation_type = getOptionalValueValidationType(null);
			
			if ( value_validation_type != null )
			{
				for ( Object obj : contents.values() )
				{
					if ( !value_validation_type.isInstance(obj) ) 
						throw new ValidationException("Expected values to all be of type: "+value_validation_type);
				}
			}
		}
		
	}
	
	public int hashCode() 
	{
		return contents.hashCode();
	}

	public Map getSimpleContents() { return contents; }
	
	public boolean equals(Object obj) 
	{
		if ( obj == null ) return false;
		if ( !(obj instanceof StandardImmutableDeckHashMap) ) return false;
		
		StandardImmutableDeckHashMap other = (StandardImmutableDeckHashMap)obj;
		
		// This test is required to prevent any two empty decks from being equal...
		if ( !Equality.optionalEquals(getOptionalKeyValidationType(null), other.getOptionalKeyValidationType(null)) ) return false;
		if ( !Equality.optionalEquals(getOptionalValueValidationType(null), other.getOptionalValueValidationType(null)) ) return false;
		
		return getSimpleContents().equals(other.getSimpleContents());
	}
	
	public int compareTo(Object o) 
	{
		if ( !(o instanceof StandardImmutableDeckHashMap) ) return 0;
		StandardImmutableDeckHashMap other = (StandardImmutableDeckHashMap)o;
		
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
}


