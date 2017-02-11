package org.kane.base.serialization;

import java.util.Collections;
import java.util.List;

/**
 * Various static methods useful when normalizing objects
 * 
 * @author jim.kane
 *
 */
public class Normalizer 
{
	/**
	 * Guarantees that str (if it is non-null) contains only upper case
	 * characters.
	 * 
	 * Remember: normalization happens *before* validation, therefore required
	 * fields are *not yet guaranteed to be non-null*. This function handles the
	 * messiness of this for you by checking for nulls etc.
	 * 
	 * @param str
	 *            The string to normalize to upperCase
	 * @return null if str is null, otherwise a version of str that contains
	 *         only upper case characters
	 */
	static public String upperCase(String str)
	{
		if ( str == null ) return null;
		return str.toUpperCase();
	}
	
	/**
	 * Guarantees that str (if it is non-null) contains only lower case
	 * characters.
	 * 
	 * Remember: normalization happens *before* validation, therefore required
	 * fields are *not yet guaranteed to be non-null*. This function handles the
	 * messiness of this for you by checking for nulls etc.
	 * 
	 * @param str
	 *            The string to normalize to lowerCase
	 * @return null if str is null, otherwise a version of str that contains
	 *         only lower case characters
	 */
	static public String lowerCase(String str)
	{
		if ( str == null ) return null;
		return str.toLowerCase();
	}
}

