package org.kane.base.immutability.collections;

import org.kane.base.immutability.ImmutableException;

public interface Field 
{
	public void freeze();
	public boolean getSimpleIsFrozen();
	
	
	default public void assertNotFrozen() 
	{ 
		if ( getSimpleIsFrozen() ) 
			throw new ImmutableException();
	}
}
