package org.kane.base.serialization;

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
