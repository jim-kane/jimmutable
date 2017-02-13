package org.kane.base.immutability.decks;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.serialization.Equality;
import org.kane.base.serialization.ValidationException;
import org.kane.base.serialization.Validator;

abstract public class StandardImmutableDeckHashSet extends StandardImmutableObject
{
	private FieldHashSet contents;

	public StandardImmutableDeckHashSet(Collection contents)
	{
		this.contents = new FieldHashSet(this,contents);
	}
	
	abstract public Class getOptionalValidationType(Class default_value);

	@Override
	public void normalize() 
	{	
	}

	public void validate() 
	{
		Validator.containsNoNulls(contents);
		
		Class c = getOptionalValidationType(null);
		if ( c != null )
		{
			for ( StandardImmutableObject obj : (Set<StandardImmutableObject>)contents )
			{
				if ( !c.isInstance(obj) ) 
					throw new ValidationException("Expceted contents to be of type: "+c);
			}
		}
	}
	
	public int hashCode() 
	{
		return contents.hashCode();
	}

	public Set getSimpleContents() { return contents; }
	
	public boolean equals(Object obj) 
	{
		if ( obj == null ) return false;
		if ( !(obj instanceof StandardImmutableDeckHashSet) ) return false;
		
		StandardImmutableDeckHashSet other = (StandardImmutableDeckHashSet)obj;
		
		// This test is required to prevent any two empty decks from being equal...
		if ( !Equality.optionalEquals(getOptionalValidationType(null), other.getOptionalValidationType(null)) ) return false;
		
		return getSimpleContents().equals(other.getSimpleContents());
	}
	
	public int compareTo(Object o) 
	{
		if ( !(o instanceof StandardImmutableDeckHashSet) ) return 0;
		StandardImmutableDeckHashSet other = (StandardImmutableDeckHashSet)o;
		
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
}
