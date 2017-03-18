package org.kane.base.examples.product_data;

import org.kane.base.exceptions.ValidationException;
import org.kane.base.immutability.Stringable;
import org.kane.base.utils.Validator;

public class PartNumber extends Stringable
{
	public PartNumber(String code)
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
			
			throw new ValidationException(String.format("Illegal character \'%c\' in part number %s.  Only upper case letters and numbers are allowed", ch, getSimpleValue()));
		}
	}
}
