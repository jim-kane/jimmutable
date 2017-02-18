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
 * A call complete executes normalize(), validate(), and then freeze()
 * 
 * @author jim.kane
 *
 */
abstract public class StandardImmutableObject extends StandardObject
{
	transient private boolean is_complete = false;
	
	/**
	 * StandardImmutableObject's add one additional method to StandardObject's
	 * complete cycle, freeze(). The job of freeze is to make any changes
	 * required to the object to make it immutable. Frequently the only job of
	 * freeze is to call freeze on any StandardImutableField objects contained
	 * herein...
	 */
	abstract public void freeze();
	
	/**
	 * Complete the object (normailze(), validate(), freeze(), mark as complete)
	 * 
	 * Attempting to make multiple calls to this function will result in a
	 * ImmutableException being thrown
	 * 
	 */
	public void complete()
	{
		assertNotComplete();
		
		super.complete();
		freeze();
		
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
		return (StandardImmutableObject)fromSerializedData(toXML(), XStreamSingleton.getXMLStream(), false);
	}
}
