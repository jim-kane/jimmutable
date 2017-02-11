package org.kane.base.serialization;

/**
 * Various static utility functions that are useful for making equality tests
 * 
 * @author jim.kane
 *
 */
public class Equality 
{
	/**
	 * Test if two optional fields are equal in the common case where the
	 * "un-set" value is represented by a null
	 * 
	 * @param optional_field_one
	 *            The first optional field
	 * @param optional_field_two
	 *            The second optional field
	 * 
	 * @return true if optional_field_one and optional_field_two are both null;
	 *         true if optional_field_one.equals(optional_field_two); false
	 *         otherwise
	 */
	static public boolean optionalEquals(Object optional_field_one, Object optional_field_two)
	{
		if ( optional_field_one == optional_field_two ) return true; 
		
		if ( optional_field_one == null && optional_field_two == null ) return true;
		if ( optional_field_one == null && optional_field_two != null ) return false;
		if ( optional_field_one != null && optional_field_two == null ) return false;
		
		if ( optional_field_one != null && optional_field_two != null )
		{
			return optional_field_one.equals(optional_field_two);
		}
		
		// Technically, there is no way to get here...
		return false;
	}
}
