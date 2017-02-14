package org.kane.base.immutability;

import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.ValidationException;
import org.kane.base.serialization.XStreamSingleton;

import com.thoughtworks.xstream.XStream;

/**
 * An abstract base class representing a standard immutable objects.
 * 
 * A standard immutable object may only be modified *before* it is complete
 * (i.e. before a call is made to the complete method. Calls to complete may
 * only be made one time. Calling complete results in the normalize method and
 * then the validate method being invoked on the object.
 * 
 * @author jim.kane
 *
 */
abstract public class StandardImmutableObject extends StandardObject
{
	transient private boolean is_complete = false;
	
	/**
	 * Complete the object (normailze(), validate(), mark as complete)
	 * 
	 * Attempting to make multiple calls to this function will result in a
	 * ImmutableException being thrown
	 * 
	 */
	public void complete()
	{
		assertNotComplete();
		
		super.complete();
		
		is_complete = true;
	}
	
	/**
	 * Test to see if the object is complete, throw an ImmutableException if the
	 * object is complete
	 */
	public void assertNotComplete()
	{
		if ( is_complete ) 
			throw new ImmutableException("Attempt to modify an object after construction is complete");
	}
	
	public boolean isComplete() { return is_complete; }
	
	public StandardImmutableObject deepMutableCloneForBuilder()
	{
		String data = toXML();
		XStream deserializer = XStreamSingleton.getXMLStream();
		
		try
		{
			StandardObject ret = (StandardObject)deserializer.fromXML(data);
			// DO NOT COMPLETE... the idea is to return a mutable object for use in a builder...
			return (StandardImmutableObject)ret;
		}
		catch(ValidationException validation_exception)
		{
			throw validation_exception;
		}
		catch(Exception other_exception)
		{
			if ( deserializer == XStreamSingleton.getXMLStream() )
				throw new ValidationException("Error constructing StandardObject from XML",other_exception);
			else if ( deserializer == XStreamSingleton.getJSONStream() )
				throw new ValidationException("Error constructing StandardObject from JSON",other_exception);
			else
				throw new ValidationException("Error constructing StandardObject from serialized data",other_exception);
		}
		
	}
}
