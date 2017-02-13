package org.kane.base.immutability;

/**
 * A RuntimeException with the semantic meaning "hey! you just tried to modify
 * and object that is immutable"
 * 
 * @author jim.kane
 *
 */
public class ImmutableException extends RuntimeException
{
	public ImmutableException()
	{
		super();
	}
	
	public ImmutableException(String message)
	{
		super(message);
	}
}