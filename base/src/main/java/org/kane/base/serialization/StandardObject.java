package org.kane.base.serialization;

import com.thoughtworks.xstream.XStream;


/**
 * A pattern for a standard object.
 * 
 * Standard objects all support:
 * 
 * 1.) XML Serialization (read & write)
 * 
 * 2.) JSON Serialization (read & write)
 * 
 * 3.) hashing, comparison, equality testing (i.e. can be included in any of the
 * standard java collections, including Set and Map)
 * 
 * 4.) Standardized normalization
 * 
 * 5.) Standardized validation
 * 
 * Extending StandardObject is kind of like shouting out to the world "hey, I am
 * good, little, normal boy that is well behaved"
 * 
 * StandadObject(s) may be mutable. If you wish to create an immutable standard
 * object (by far the most common type) you should extend
 * StandardImmutableObject
 * 
 * @author jim.kane
 *
 */
abstract public class StandardObject implements Comparable
{
	/**
	 * Normalize the fields of the object.
	 * 
	 * In StandardImmutableObject, the object is guaranteed to be mutable at the
	 * point normalize is called.
	 * 
	 * validate is called *after* normalization. The idea is that you are given
	 * a chance to fill in/correct fields before a validation test is made.
	 */
	abstract public void normalize();
	
	/**
	 * Validate the fields of the object. Standard practice is to simply call
	 * the various validation methods in Validator to do the heavy lifting.
	 * Thrown a ValidationException if something is not "up to snuff"
	 */
	abstract public void validate();
	
	abstract public int hashCode();
	abstract public boolean equals(Object obj);
	
	/**
	 * Calling complete normalizes and then validates the object. Complete must
	 * only be called once in an object's life cycle. Typically standard
	 * object's call complete in the end of their standard constructor.
	 * 
	 * When building complicated class hierarchies, the decision of *where* to
	 * call complete can become complicated. Why? Well complete can only be
	 * called once so you need to call it at the end of the constructor in the
	 * "leaf" classes. Declaring leaf classes final is a good "heuristic" way to
	 * get started with this in many cases. In more complicated cases, you may
	 * need to add arguments to base class constructors "am I the leaf?" to
	 * control completion.
	 */
	public void complete()
	{
		normalize();
		validate();
	}
	
	/**
	 * Use XML serialization to deeply clone the object
	 * 
	 * @return A deep copy of this object
	 */
	public StandardObject deepClone()
	{
		return fromXML(toXML());
	}
	
	/**
	 * Default implementation of toString (returns a JSON representation of the
	 * object)
	 */
	public String toString()
	{
		return toJSON();
	}
	

	/**
	 * Serialize this object to JSON
	 * 
	 * @return The JSON serialized version of this object
	 */
	public String toJSON()
	{
		return XStreamSingleton.getJSONStream().toXML(this);
	}
	
	/**
	 * Serialize this object to XML
	 * 
	 * @return The XML serialized version of this object
	 */
	public String toXML()
	{
		return XStreamSingleton.getXMLStream().toXML(this);
	} 
	
	/**
	 * Pretty print the XML of this object
	 * 
	 * @param default_value The value to return if unable to pretty-print the MXL
	 * 
	 * @return A pretty printed XML representation of this object
	 * 
	 */
	
	public String toPrettyPrintXML(String default_value)
	{
		return JavaCodeUtils.prettyPrintXML(toXML(), default_value);
	}
	
	
	/**
	 * Convert this object to a Java source code. This is done by taking the
	 * pretty printed XML and properly escaping it (using
	 * JavaCodeUtils.toJavaStringLiteral) so as to make clean, easy to read Java
	 * source code that will construct the object. (Effectively serializing the
	 * object to java source code!)
	 * 
	 * @return Java statements that will construct the object from XML
	 */
	public String toJavaCode(String variable_name)
	{
		return String.format("String %s_as_xml_string = %s\n\n%s %s = (%s)StandardObject.fromXML(%s_as_xml_string);"
				, variable_name
				, JavaCodeUtils.toJavaStringLiteral(toPrettyPrintXML("unable to pretty print XML!"))
				, getClass().getSimpleName()
				, variable_name
				, getClass().getSimpleName()
				, variable_name
			);
	}
	
	/**
	 * Create a standard object from XML. Objects created from XML are still
	 * subject to completion (i.e. normalization followed by validation)
	 * 
	 * @param xml
	 *            The XML to create the object from. If you pass a null value in
	 *            here, you will generate a validation exception
	 * @return An StandardObject created from the given XML
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             ValidationException. If another exception is generated (for
	 *             example, the XML is invalid etc.) a chained validation
	 *             exception will be thrown
	 */
	static public StandardObject fromXML(String xml) throws ValidationException
	{
		return fromSerializedData(xml, XStreamSingleton.getXMLStream(), true);
	}
	
	/**
	 * Create a standard object from JSON. Objects created from JSON are still
	 * subject to completion (i.e. normalization followed by validation)
	 * 
	 * @param json
	 *            The JSON to create the object from. If you pass a null value in
	 *            here, you will generate a validation exception
	 *            
	 * @return An StandardObject created from the given JSON
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             ValidationException. If another exception is generated (for
	 *             example, the JSON is invalid etc.) a chained validation
	 *             exception will be thrown
	 */
	static public StandardObject fromJSON(String json) throws ValidationException
	{
		return fromSerializedData(json, XStreamSingleton.getJSONStream(), true);
	}
	
	/**
	 * Do the actual work for fromXML and from JSON
	 * 
	 * @param data
	 *            The data to de-serialize from
	 * 
	 * @param deserializer
	 *            The XStream object to use for de-serialization
	 * @return The object de-serialized from data
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method (other
	 *             exceptions are caught and chained)
	 */
	static protected StandardObject fromSerializedData(String data, XStream deserializer, boolean complete) throws ValidationException
	{
		Validator.notNull(data);
		Validator.notNull(deserializer);
		
		try
		{
			StandardObject ret = (StandardObject)deserializer.fromXML(data);
			if ( complete ) ret.complete();
			return ret;
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
	
	/**
	 * Utility function that "quietly" creates an object from XML. If any error
	 * occurs (i.e. any exception is thrown) then the exception is swallowed
	 * and the default_value is returned.
	 * 
	 * @param xml
	 *            The XML data to de-serialize the object from
	 * @param default_value
	 *            The value to return if there is an error
	 * @return The standard object de-serialized from the XML data or
	 *         default_value if there is any error
	 */
	
	static public StandardObject quietFromXML(String xml, StandardObject default_value)
	{
		try
		{
			return fromXML(xml);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
	
	/**
	 * Utility function that "quietly" creates an object from JSON. If any error
	 * occurs (i.e. any exception is thrown) then the exception is swallowed
	 * and the default_value is returned.
	 * 
	 * @param json
	 *            The JSON data to de-serialize the object from
	 * @param default_value
	 *            The value to return if there is an error
	 * @return The standard object de-serialized from the JSON data or
	 *         default_value if there is any error
	 */
	
	static public StandardObject quietFromJSON(String json, StandardObject default_value)
	{
		try
		{
			return fromJSON(json);
		}
		catch(Exception e)
		{
			return default_value;
		}
	}
}
