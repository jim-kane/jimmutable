package org.kane.base.serialization;

/**
 * Various static utility functions that are helpful when writing unit test etc. in Java
 * @author jim.kane
 *
 */
public class JavaCodeUtils 
{
	/**
	 * Lets say you have a string that contains special characters (e.g. quotes,
	 * newlines, etc.) and you want to turn it into a Java String literal (i.e.
	 * something you can copy paste into java). toJavaStringLiteral is your
	 * answer!
	 * 
	 * @param src
	 *            Any valid java string
	 * @return A java string literal, ready to copy/paste into java source code
	 *         without compilation errors
	 */
	
	static public String toJavaStringLiteral(String src)
	{
		StringBuilder ret = new StringBuilder("\"");
		
		for ( char ch : src.toCharArray() )
		{
			if ( ch == '\t' ) { ret.append("\\t"); continue; }
			if ( ch == '\b' ) { ret.append("\\b"); continue; }
			if ( ch == '\n' ) { ret.append("\\n"); continue; }
			if ( ch == '\r' ) { ret.append("\\r"); continue; }
			if ( ch == '\f' ) { ret.append("\\f"); continue; }
			if ( ch == '\'' ) { ret.append("\\'"); continue; }
			if ( ch == '\"' ) { ret.append("\\\""); continue; }
			if ( ch == '\\' ) { ret.append("\\\\"); continue; }
		
			if ( ch >= ' ' && ch <= '~' ) { ret.append(ch); continue; } // quotes etc. are in this range, but are caught by the above checks...
			
			ret.append(String.format("\\u%04X", (int)ch));
		}
		
		ret.append("\"");
		
		return ret.toString();
	}
}
