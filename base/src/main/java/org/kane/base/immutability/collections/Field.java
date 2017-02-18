package org.kane.base.immutability.collections;

import org.kane.base.immutability.ImmutableException;

/**
 * The Field interface should be implemented by any object that serves as a
 * member of a StandardImmtuableObject and requires custom code to "freeze" it.
 * Most notably, any collection or map used in a StandardImmutableObject will,
 * typically, be one of the types implemented in this package and will,
 * consequently, implement Field.
 * 
 * @author jim.kane
 *
 */
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
