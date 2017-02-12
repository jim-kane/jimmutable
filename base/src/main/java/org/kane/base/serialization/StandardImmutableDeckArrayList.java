package org.kane.base.serialization;

import java.util.Collection;
import java.util.List;

import org.kane.base.serialization.collections.StandardImmutableFieldArrayList;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

abstract public class StandardImmutableDeckArrayList extends StandardImmutableObject
{
	@XStreamImplicit(itemFieldName="contents")
	private StandardImmutableFieldArrayList contents;

	public StandardImmutableDeckArrayList(Collection contents)
	{
		this.contents = new StandardImmutableFieldArrayList(this,contents);
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
			for ( StandardImmutableObject obj : (List<StandardImmutableObject>)contents )
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

	public List getSimpleContents() { return contents; }
	
	public boolean equals(Object obj) 
	{
		if ( obj == null ) return false;
		if ( !(obj instanceof StandardImmutableDeckArrayList) ) return false;
		
		StandardImmutableDeckArrayList other = (StandardImmutableDeckArrayList)obj;
		
		// This test is required to prevent any two empty decks from being equal...
		if ( !Equality.optionalEquals(getOptionalValidationType(null), other.getOptionalValidationType(null)) ) return false;
		
		return getSimpleContents().equals(other.getSimpleContents());
	}
	
	public int compareTo(Object o) 
	{
		if ( !(o instanceof StandardImmutableDeckArrayList) ) return 0;
		StandardImmutableDeckArrayList other = (StandardImmutableDeckArrayList)o;
		
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
}

