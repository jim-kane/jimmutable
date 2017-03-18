package org.kane.base.serialization.reader;

abstract public class ReadAs 
{
	static public ReadAs OBJECT = new ReadAsObject();
	static public ReadAs STRING = new ReadAsString();
	static public ReadAs BOOELAN = new ReadAsBoolean();
	static public ReadAs CHARACTER = new ReadAsCharacter();
	static public ReadAs BYTE = new ReadAsByte();
	static public ReadAs SHORT = new ReadAsShort();
	static public ReadAs INTEGER = new ReadAsInt();
	static public ReadAs LONG = new ReadAsLong();
	static public ReadAs FLOAT = new ReadAsFloat();
	static public ReadAs DOUBLE = new ReadAsDouble();
	
	/**
	 * Read a read tree (t) and convert into a given type
	 * 
	 * @param t
	 *            The read tree to construct the object from
	 * @return A value in a uniform type, or null if the read tree t could not
	 *         be converted into the target type.
	 */
	abstract public Object readAs(ObjectReader t);
	
	
	static private class ReadAsObject extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asObject(null);
		}
		
	}
	
	static private class ReadAsString extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asString(null);
		}
	}
	
	static private class ReadAsBoolean extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asBoolean(null);
		}
	}
	
	static private class ReadAsCharacter extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asCharacter(null);
		}
	}
	
	static private class ReadAsByte extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asByte(null);
		}
	}
	
	static private class ReadAsShort extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asShort(null);
		}
	}
	
	static private class ReadAsInt extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asInteger(null);
		}
	}
	
	static private class ReadAsLong extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asLong(null);
		}
	}
	
	static private class ReadAsFloat extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asFloat(null);
		}
	}
	
	static private class ReadAsDouble extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return t.asDouble(null);
		}
	}
}
