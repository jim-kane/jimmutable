package org.kane.base.examples.product_data;

import org.kane.base.exceptions.ValidationException;
import org.kane.base.immutability.Stringable;
import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.utils.Validator;

public class ItemAttribute extends Stringable
{
	static public final ReadAs READ_AS = new ReadAsItemAttribute();
	
	public ItemAttribute(String code)
	{
		super(code);
	}

	
	public void normalize() 
	{
		normalizeTrim();
		normalizeUpperCase();
	}

	
	public void validate() 
	{
		Validator.notNull(getSimpleValue());
		Validator.min(getSimpleValue().length(), 1);
		
		char chars[] = getSimpleValue().toCharArray();
		for ( char ch : chars )
		{
			if ( ch >= 'A' && ch <= 'Z' ) continue;
			if ( ch >= '0' && ch <= '9' ) continue;
			if ( ch == '_' ) continue;
			
			throw new ValidationException(String.format("Illegal character \'%c\' in item attribute %s.  Only upper case letters, numbers, and underscores are allowed", ch, getSimpleValue()));
		}
	}
	
	static private class ReadAsItemAttribute extends ReadAs
	{
		public Object readAs(ObjectReader t) 
		{
			return new ItemAttribute(t.asString(null));
		}
	}
}
