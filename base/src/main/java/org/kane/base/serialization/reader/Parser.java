package org.kane.base.serialization.reader;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Stack;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.SerializeException;
import org.kane.base.serialization.writer.Format;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

public class Parser 
{
	private Reader reader;
	private Format format;
	
	private FieldName last_field_name = FieldName.FIELD_DOCUMENT_ROOT;

	private ReadTree result;
	
	private JsonParser json_parser;
	
	private Parser(Reader r) throws Exception
	{
		this.reader = r;
		
		format = figureFormat();
		
		if ( format == Format.JSON || format == Format.JSON_PRETTY_PRINT )
		{
			JsonFactory jfactory = new JsonFactory();
			json_parser = jfactory.createJsonParser(reader);
		}
		else
		{
			XmlFactory xfactory = new XmlFactory();
			json_parser = xfactory.createJsonParser(reader);
		}
		
		if ( json_parser == null )
			throw new SerializeException("Could not create a parser for the format "+format);
		
		result = processObjectTokens(FieldName.FIELD_DOCUMENT_ROOT);
	}
	
	private ReadTree processObjectTokens(FieldName object_field_name) throws Exception
	{
		Stack<ReadTree> stack = new Stack<>();
		
		ReadTree root = new ReadTree(object_field_name);
		stack.push(root);
		
		while(true)
		{
			JsonToken token = json_parser.nextToken();
			if ( token == null ) 
			{
				throw new SerializeException("Unexpected end of input");
			}
			
			
			switch(token)
			{
			case NOT_AVAILABLE: 
				throw new SerializeException("Unknown parse error");
			
			case START_OBJECT: 
				break; 
				
			case END_OBJECT:
				stack.pop(); 
				if ( stack.isEmpty() ) return root;
				break;
				
			case START_ARRAY:
				FieldName array_name = stack.peek().getSimpleFieldName(); // get the field name of the stub object
				stack.pop(); // pop out the "stub" object created by the preceding field event
				stack.peek().removeLast(); // Remove the "stub" object added to the current object by the preceding field event
				
				processArrayTokens(array_name, stack.peek());
				break; 
				
			case END_ARRAY: 
				throw new SerializeException("Unexpected end of array token"); 
				
			case FIELD_NAME:
				
				ReadTree new_object = new ReadTree(new FieldName(json_parser.getValueAsString()));
				stack.peek().add(new_object);
				
				stack.push(new_object);
				
				break;
			
			case VALUE_EMBEDDED_OBJECT: 
				throw new SerializeException("Unsupported value type (embedded object)");
			
			case VALUE_STRING:
			case VALUE_NUMBER_INT:
			case VALUE_NUMBER_FLOAT:
			case VALUE_TRUE:
			case VALUE_FALSE:
				
				stack.peek().setValue(json_parser.getValueAsString());
				stack.pop();
				
				break;
				
			case VALUE_NULL:
				
				stack.peek().setValue(null);
				stack.pop();
				
				break;
			}
		}
	}
	
	private void processArrayTokens(FieldName array_name, ReadTree parent) throws Exception
	{
		while(true)
		{
			JsonToken token = json_parser.nextToken();
			if ( token == null ) 
				throw new SerializeException("Unexpected end of input");
			
			switch(token)
			{
			case NOT_AVAILABLE: 
				throw new SerializeException("Unknown parse error");
			
			case START_OBJECT: 
				parent.add(processObjectTokens(array_name));
				break; 
				
			case END_OBJECT:
				throw new SerializeException("End object token found while processing array");
								
			case START_ARRAY:
				throw new SerializeException("ERROR: Encountered start of array while in array");
				
			case END_ARRAY: 
				return; // done processing array!
				
			case FIELD_NAME:
				throw new SerializeException("ERROR: Encountered field name in array");
			
			case VALUE_EMBEDDED_OBJECT: 
				throw new SerializeException("Unsupported value type (embedded object)");
			
			case VALUE_STRING:
			case VALUE_NUMBER_INT:
			case VALUE_NUMBER_FLOAT:
			case VALUE_TRUE:
			case VALUE_FALSE:
				
				
				ReadTree value_object = new ReadTree(array_name);
				value_object.setValue(json_parser.getValueAsString());
				
				parent.add(value_object);
				
				break;
				
			case VALUE_NULL:
				
				ReadTree null_object = new ReadTree(array_name);
				null_object.setValue(null);
				
				parent.add(null_object);
				
				break;
			}
		}
	}

	
	private Format figureFormat()
	{
		try
		{
			// Make sure that reader supports mark and reset
			if ( !reader.markSupported()  )
			{
				reader = new BufferedReader(reader);
			}
	
			reader.mark(11);
			char buf[] = new char[10];
			int ar = reader.read(buf, 0, 10);
			reader.reset();
			
			String start = new String(buf,0,ar);
			start = start.trim();
			
			if ( start.startsWith("{") ) return Format.JSON;
			if ( start.startsWith("<") ) return Format.XML;
			
			
			throw new SerializeException("Unable to determine the input format: read 10 characters without a definitive answer");
		}
		catch(Exception e)
		{
			throw new SerializeException("Unable to determine the input format",e);
		}
	}
	
	
	
	static public ReadTree parse(Reader r, ReadTree default_value)
	{
		try
		{
			Parser p = new Parser(r);
			if ( p.result == null ) return default_value;
			return p.result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return default_value;
		}
	}
}
