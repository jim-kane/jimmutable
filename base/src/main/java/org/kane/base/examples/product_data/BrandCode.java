package org.kane.base.examples.product_data;

import org.kane.base.exceptions.ValidationException;
import org.kane.base.objects.Stringable;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.utils.Normalizer;
import org.kane.base.utils.Validator;

/**
 * An example of a simple, Stringable object
 * 
 * @author jim.kane
 *
 */
public class BrandCode extends Stringable
{
	public BrandCode(String code)
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
			
			throw new ValidationException(String.format("Illegal character \'%c\' in brand code %s.  Only upper case letters, numbers and underscore (_) are allowed", ch, getSimpleValue()));
		}
	}
}
