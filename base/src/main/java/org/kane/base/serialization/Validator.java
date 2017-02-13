package org.kane.base.serialization;

import java.util.Collection;
import java.util.Map;

/**
 * Various utility functions used for validation.  
 * 
 * @author jim.kane
 *
 */
public class Validator 
{
	/**
	 * Guarantee that obj is not null (i.e. thrown a ValidationException if obj 
	 * is null)
	 * 
	 * @param obj The object to test
	 */
	static public void notNull(Object obj) 
	{
		if ( obj == null ) throw new ValidationException("Required field is null");
	}
	
	/**
	 * Guarantee that an object is equal to or greater than minimum_valid_value
	 * 
	 * @param value
	 *            The value to test
	 * @param minimum_valid_value
	 *            The minimum valid value
	 */
	static public void min(Comparable value, Comparable minimum_valid_value)
	{
		if ( value.compareTo(minimum_valid_value) < 0 ) 
			throw new ValidationException("Value ("+value+") is below minimum allowed value ("+minimum_valid_value+")");
	}
	
	/**
	 * Guarantee that an object is equal to or less than maximum_value
	 * 
	 * @param value
	 *            The value to test
	 * @param maximum_valid_value
	 *            The maximum valid value
	 */
	static public void max(Comparable value, Comparable maximum_valid_value)
	{
		if ( value.compareTo(maximum_valid_value) > 0 ) 
			throw new ValidationException("Value ("+value+") is above the maximum allowed value ("+maximum_valid_value+")");
	}
	
	/**
	 * Guarantee that a collection contains no nulls (i.e. throw a
	 * ValidationException if the collection contains one or more null values)
	 * 
	 * @param c
	 *            The collection to test
	 */
	static public void containsNoNulls(Collection c)
	{
		for ( Object obj : c )
		{
			if ( obj == null ) throw new ValidationException("Collection contained a null element");
		}
	}
}
