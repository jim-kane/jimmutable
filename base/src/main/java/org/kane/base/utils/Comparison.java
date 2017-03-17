package org.kane.base.utils;

public class Comparison 
{
	static public int startCompare() { return 0; }
	
	static public int continueCompare(int ret, Comparable one, Comparable two)
	{
		if ( ret != 0 ) return ret; // only do the compare if a preceeding compare has not resolved the whole comarison
		
		if ( one == null && two == null ) return 0; // two nulls are always equal
		
		// Sort nulls to the front...
		if ( one == null && two != null ) return -1; 
		if ( one != null && two == null ) return 1;
		
		// actually do the compare
		return one.compareTo(two);
	}
}
