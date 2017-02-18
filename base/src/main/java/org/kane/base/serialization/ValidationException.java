package org.kane.base.serialization;

/**
 * A standard exception to throw when a "validation" error is encounted (e.g. an
 * parameter is unexpectedly null, etc.)
 * 
 * @author jim.kane
 *
 */
public class ValidationException extends RuntimeException
{
	public ValidationException()
	{
		super();
	}
	
	public ValidationException(String message)
	{
		super(message);
	}
	
	public ValidationException(String message, Exception e)
	{
		super(message,e);
	}
}
