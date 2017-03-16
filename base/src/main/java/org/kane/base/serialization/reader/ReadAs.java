package org.kane.base.serialization.reader;

abstract public class ReadAs 
{
	static public ReadAs READ_AS_TYPE_HINT = new ReadAsTypeHint();
	static public ReadAs READ_AS_STRING = new ReadAsString();
	
	/**
	 * Read a read tree (t) and convert into a given type
	 * 
	 * @param t
	 *            The read tree to construct the object from
	 * @return A value in a uniform type, or null if the read tree t could not
	 *         be converted into the target type.
	 */
	abstract Object readAs(ReadTree t);
	
	
	static private class ReadAsTypeHint extends ReadAs
	{
		Object readAs(ReadTree t) 
		{
			return t.asObject(null);
		}
		
	}
	
	static private class ReadAsString extends ReadAs
	{
		public Object readAs(ReadTree t) 
		{
			return t.asString(null);
		}
	}
}
