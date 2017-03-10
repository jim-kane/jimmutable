package org.kane.base.serialization;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import org.kane.base.immutability.StandardImmutableObject;

import com.thoughtworks.xstream.XStream;


/**
 * The root for all "standard" objects. All data in the framework,
 * whether abstract, final, component, or container, should - directly
 * or indirectly - inherit from {@code StandardObject}.
 * 
 * <p>Standard objects all support:
 * <ul>
 * 	<li>XML Serialization (read & write)</li>
 *  <li>JSON Serialization (read & write)</li>
 *  <li>Hashing, comparison, equality testing (i.e. can be included in any of the standard Java collection implementations, including {@code Set} and {@code Map})</li>
 *  <li>Standardized normalization of data field values</li>
 *  <li>Standardized validation of data field values</li>
 * </ul>
 * 
 * <p>Extending {@code StandardObject} is kind of like shouting out to the world "hey, I am
 * good, little, normal boy that is well behaved".
 * 
 * <p>{@code StandadObject}(s) may be mutable. If you wish to create an immutable standard
 * object (by far the most common type) you should extend {@link StandardImmutableObject}.
 * 
 * @author Jim Kane
 *
 * @param <T> The object that is extending {@code StandardObject}. This fixes the typing for
 * 			  serialization, cloning, etc.
 * 
 * @see StandardImmutableObject
 */
abstract public class StandardObject<T extends StandardObject<T>> implements Comparable<T>
{
	/**
	 * Normalize the fields of the object. The idea is to clean up the values of
	 * fields when possible (e.g. all uppercase letters, empty collection).
	 *
	 * <p>It is expected that implementations of {@code normalize} will never throw an
	 * Exception. Work done during normalization should be tolerant of bad vales
	 * (e.g. {@code null}). If there's something wrong with a value, {@link #validate()}
	 * will catch it.
	 * 
	 * <p>In {@link StandardImmutableObject}, the object is guaranteed to be mutable
	 * at the point {@code normalize} is called.
	 * 
	 * <p>{@link #validate()} is called <em>after</em> normalization. The idea is that
	 * you are given a chance to fill in/correct fields before a validation check is made.
	 */
	abstract public void normalize();
	
	/**
	 * Validate the fields of the object. Standard practice is to simply call
	 * the various validation methods in {@link Validator} to do the heavy lifting.
	 * 
	 * @throws ValidationException if something is not "up to snuff"
	 */
	abstract public void validate();
	
	@Override
	abstract public int hashCode();
	
	@Override
	abstract public boolean equals(Object obj);
	
	/**
	 * Declare that an object is ready to use. The practical effect is that
	 * {@link #normalize() normalization} and {@link #validate() validation}
	 * is done on all fields.
	 * 
	 * <p>{@code Complete} must only be called <em>once</em> in an object's life cycle.
	 * Typically, standard object's call {@code complete} at the end of their
	 * standard constructor.
	 * 
	 * <p>When building complicated class hierarchies, the decision of <em>where</em>
	 * to call {@code complete} can become complicated. Why? Well, {@code complete}
	 * can only be called <em>once</em> so you need to call it at the end of the constructor
	 * in the "leaf" classes. Declaring leaf classes {@code final} is a good "heuristic"
	 * way to get started with this in many cases. In more complicated cases, you may
	 * need to add arguments to base class constructors "am I the leaf?" to control
	 * completion.
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
	 * 
	 * @see #fromXML(String)
	 * @see #toXML()
	 */
	public T deepClone()
	{
		return fromXML(toXML());
	}
	
	/**
	 * Default implementation of {@link Object#toString() toString()} that returns a
	 * {@link #toJSON() JSON} representation of the object
	 */
	@Override
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
	 * Serialize this object to XML
	 * 
	 * @param writer The {@link Writer} to send the XML representation to
	 */
	public void toXML(Writer writer)
	{
		XStreamSingleton.getXMLStream().toXML(this, writer);
	} 
	
	/**
	 * Pretty print the XML of this object
	 * 
	 * @param default_value The value to return if unable to pretty-print the XML
	 * 						(e.g. an error occurs)
	 * 
	 * @return A pretty printed XML representation of this object
	 */
	public String toPrettyPrintXML(String default_value)
	{
		return JavaCodeUtils.prettyPrintXML(toXML(), default_value);
	}
	
	/**
	 * Create Java source code that will construct an identical copy of this object.
	 * 
	 * <p>This is done by taking the pretty printed XML and properly escaping it (using
	 * {@link JavaCodeUtils#toJavaStringLiteral(String) JavaCodeUtils}) so as to make
	 * clean, easy to read Java source code that will construct the object. (Effectively
	 * serializing the object to Java source code!)
	 * 
	 * <p>This is super useful when creating unit tests of serialization... Just
	 * saying...
	 * 
	 * @return Java statements that will construct a copy of this object from XML
	 */
	public String toJavaCode(String variable_name)
	{
		return String.format("String %s_as_xml_string = %s;\n\n%s %s = (%s)StandardObject.fromXML(%s_as_xml_string);"
				, variable_name
				, JavaCodeUtils.toJavaStringLiteral(toPrettyPrintXML("unable to pretty print XML!"))
				, getClass().getSimpleName()
				, variable_name
				, getClass().getSimpleName()
				, variable_name
			);
	}
	
	
	/**
	 * Create Java source code that will construct an identical copy of this object.
	 * 
	 * <p>This is done by taking the pretty printed XML and properly escaping it (using
	 * {@link JavaCodeUtils#toJavaStringLiteral(String) JavaCodeUtils}) so as to make
	 * clean, easy to read Java source code that will construct the object. (Effectively
	 * serializing the object to Java source code!)
	 * 
	 * <p>This is super useful when creating unit tests of serialization... Just
	 * saying...
	 * 
	 * @return Java statements that will construct a copy of this object from JSON
	 */
	public String toJavaCodeUsingJSON(String variable_name)
	{
		return String.format("String %s_as_json_string = %s;\n\n%s %s = (%s)StandardObject.fromJSON(%s_as_json_string);"
				, variable_name
				, JavaCodeUtils.toJavaStringLiteral(toJSON())
				, getClass().getSimpleName()
				, variable_name
				, getClass().getSimpleName()
				, variable_name
			);
	}
	
	/**
	 * Low-level method for de-serializing an {@link StandardObject object} using {@link XStream}.
	 * 
	 * <p>Nice implementations for common serialization formats (e.g. {@link #fromXML(Reader) XML}
	 * and {@link #fromJSON(Reader) JSON}) are preferred. This implementation is exposed in the
	 * case that a custom or unusual de-serialization format is needed.
	 * 
	 * @param data The data to de-serialize from
	 * @param deserializer The {@link XStream} object to use to convert {@code data} into object(s)
	 * @param complete Whether or not to call {@link #complete()} on the de-serialized object
	 * 
	 * @return The object de-serialized from data
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., {@code data} is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromSerializedData(String data, XStream deserializer, boolean complete) throws ValidationException
	{
		return fromSerializedData(new StringReader(data),deserializer, complete);
	}
	
	/**
	 * Low-level method for de-serializing an {@link StandardObject object} using {@link XStream}.
	 * 
	 * <p>Nice implementations for common serialization formats (e.g. {@link #fromXML(Reader) XML}
	 * and {@link #fromJSON(Reader) JSON}) are preferred. This implementation is exposed in the
	 * case that a custom or unusual de-serialization format is needed.
	 * 
	 * @param data The data to de-serialize from
	 * @param deserializer The {@link XStream} object to use to convert {@code data} into object(s)
	 * @param complete Whether or not to call {@link #complete()} on the de-serialized object
	 * 
	 * @return The object de-serialized from data
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., {@code data} is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromSerializedData(Reader data, XStream deserializer, boolean complete) throws ValidationException
	{
		Validator.notNull(data);
		Validator.notNull(deserializer);
		
		try
		{
			@SuppressWarnings("unchecked")
            T ret = (T) deserializer.fromXML(data);
            if (complete) ret.complete();
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
	 * Create a standard object from XML. Objects created from XML are still
	 * subject to {@link #complete() completion}.
	 * 
	 * @param xml The XML to create the object from. If you pass a {@code null}
	 * 		      value in here, you will generate a {@link ValidationException}.
	 * 
	 * @return A {@link StandardObject} created from the given XML
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., the XML is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromXML(String xml) throws ValidationException
	{
		return fromSerializedData(xml, XStreamSingleton.getXMLStream(), true);
	}
	
	/**
	 * Create a standard object from XML. Objects created from XML are still
	 * subject to {@link #complete() completion}.
	 * 
	 * @param xml The {@link Reader} that will provide the XML content to create
	 * 			  the object from. If you pass a {@code null} value in here, you
	 * 			  will generate a {@link ValidationException}.
	 * 
	 * @return A {@link StandardObject} created from the given XML
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., the XML is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromXML(Reader xml) throws ValidationException
	{
		return fromSerializedData(xml, XStreamSingleton.getXMLStream(), true);
	}
	
	/**
	 * Utility function that "quietly" creates an object from XML. If any error
	 * occurs (i.e. any exception is thrown) then the exception is swallowed
	 * and the {@code default_value} is returned.
	 * 
	 * @param xml The XML data to de-serialize the object from
	 * @param default_value The value to return if there is an error
	 * 
	 * @return The {@link StandardObject standard object} de-serialized from the
	 * 		   XML data or {@code default_value} if there is any error
	 * 
	 * @see #fromXML(String)
	 */
	static public <T extends StandardObject<T>> T quietFromXML(String xml, T default_value)
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
	 * Create a {@link StandardObject standard object} from JSON. Objects created
	 * from JSON are still subject to {@link #complete() completion.
	 * 
	 * @param json The JSON to create the object from. If you pass a {@code null}
	 * 		       value in here, you will generate a {@link ValidationException}.
	 * 
	 * @return A {@link StandardObject} created from the given JSON
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., the JSON is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromJSON(String json) throws ValidationException
	{
		return fromSerializedData(json, XStreamSingleton.getJSONStream(), true);
	}
	
	/**
	 * Create a {@link StandardObject standard object} from JSON. Objects created
	 * from JSON are still subject to {@link #complete() completion.
	 * 
	 * @param json The {@link Reader} that will provide the JSON content to create
	 * 			   the object from. If you pass a {@code null} value in here, you
	 * 			   will generate a {@link ValidationException}.
	 * 
	 * @return A {@link StandardObject} created from the given JSON
	 * 
	 * @throws ValidationException
	 *             The only exception that can be thrown from this method is a
	 *             {@code ValidationException}. If another exception is generated
	 *             (e.g., the JSON is invalid), a chained {@code ValidationException}
	 *             will be thrown.
	 * 
	 * @param <T> The type of object created
	 */
	static public <T extends StandardObject<T>> T fromJSON(Reader json) throws ValidationException
	{
		return fromSerializedData(json, XStreamSingleton.getJSONStream(), true);
	}
	
	/**
	 * Utility function that "quietly" creates an object from JSON. If any error
	 * occurs (i.e. any exception is thrown) then the exception is swallowed
	 * and the {@code default_value} is returned.
	 * 
	 * @param json The JSON data to de-serialize the object from
	 * @param default_value The value to return if there is an error
	 * 
	 * @return The {@link StandardObject standard object} de-serialized from the
	 * 		   JSON data or {@code default_value} if there is any error
	 * 
	 * @see #fromJSON(String)
	 */
	static public <T extends StandardObject<T>> T quietFromJSON(String json, T default_value)
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
