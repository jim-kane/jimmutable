package org.kane.base;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class TestApp 
{
	private JsonGenerator gen;
	
	public TestApp(JsonGenerator generator)
	{
		this.gen = generator;
	}
	
	public void beginDocument(String type) throws IOException
	{
		if ( gen instanceof ToXmlGenerator )
		{
			ToXmlGenerator xgen = (ToXmlGenerator)gen;
			xgen.writeRaw("<?xml version='1.0' encoding='UTF-8'?>");
			xgen.setNextName(new QName("object"));
		}
		
		gen.writeStartObject();
		gen.writeStringField("TYPE", type);
	}
	
	public void endDocument() throws IOException
	{
		gen.writeEndObject();
		gen.close();
	}
	
	public void writeFieldName(String field_name) throws IOException
	{
		gen.writeFieldName(field_name);
	}
	
	
	public void openObject() throws IOException
	{
		gen.writeStartObject();
	}
	
	public void openObject(String explicit_type) throws IOException
	{
		gen.writeStartObject();
		
		gen.writeFieldName("TYPE");
		gen.writeString(explicit_type);
	}
	
	public void closeObject() throws IOException
	{
		gen.writeEndObject();
	}
	
	public void writeString(String str) throws IOException
	{
		gen.writeString(str);
	}
	
	public void writeExplicitlyTypedString(String str) throws IOException
	{
		openObject("string");
		writeFieldName("VALUE");
		writeString(str);
		closeObject();
	}
	
	public void writeFieldString(String field_name, String str) throws IOException
	{
		writeFieldName(field_name);
		writeString(str);
	}
	
	public void writeFieldExplicitlyTypedString(String field_name, String str) throws IOException
	{
		writeFieldName(field_name);
		writeExplicitlyTypedString(str);
	}
	
	public void openArray() throws IOException
	{
		gen.writeStartArray();
	}
	
	public void openFieldArray(String field_name) throws IOException
	{
		gen.writeFieldName(field_name);
		gen.writeStartArray();
	}
	
	public void closeArray() throws IOException
	{
		gen.writeEndArray();
	}
	
	
	static public void main(String args[]) throws Exception
	{
		JsonFactory jfactory = new JsonFactory();
		XmlFactory xfactory = new XmlFactory();
		
		StringWriter string_writer = new StringWriter();
		
		JsonGenerator gen = xfactory.createGenerator(string_writer);
		
		TestApp writer = new TestApp(gen);
		
		writer.beginDocument("person");
		
		writer.writeFieldExplicitlyTypedString("first_name", "Jim");
		writer.writeFieldString("last_name", "Kane");
		
		
		
		writer.openFieldArray("cars_owned");
		{
			writer.openObject("car");
			{
				writer.writeFieldString("make", "BMW");
				writer.writeFieldString("year", "2006");
			
				writer.closeObject();
			}
			
			writer.openObject("car");
			{
				writer.writeFieldString("make", "SUBARU");
				writer.writeFieldString("year", "2015");
			
				writer.closeObject();
			}
			
			writer.closeArray();
		}
		
		writer.endDocument();
		
		
		System.out.println(string_writer.toString()); 
		
		/*
		
		StringWriter writer = new StringWriter();
		ToXmlGenerator gen = f.createGenerator(writer);
		
		gen.setNextName(new QName("root"));
		
		gen.writeStartObject();
		
	
		gen.writeStringField("foo", "bar");
		
		gen.writeEndObject();
		
		
		
		gen.close();
		
		
		System.out.println(writer.toString());*/
	}
}
