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
			((ToXmlGenerator) gen).setNextName(new QName("object"));
		}
		
		gen.writeStartObject();
		gen.writeStringField("TYPE", type);
	}
	
	public void endDocument() throws IOException
	{
		gen.writeEndObject();
		gen.close();
	}
	
	public void openObjectInArray() throws IOException
	{
		gen.writeStartObject();
	}
	
	public void openObjectInArray(String explicit_type) throws IOException
	{
		gen.writeStartObject();
		
		gen.writeFieldName("TYPE");
		gen.writeString(explicit_type);
	}
	
	
	public void openObject(String field_name) throws IOException
	{
		gen.writeFieldName(field_name);
		gen.writeStartObject();
	}
	
	public void openObject(String field_name, String explicit_type) throws IOException
	{
		gen.writeFieldName(field_name);
		gen.writeStartObject();
		
		gen.writeFieldName("TYPE");
		gen.writeString(explicit_type);
	}
	
	public void closeObject() throws IOException
	{
		gen.writeEndObject();
	}
	
	public void writeString(String field_name, String str) throws IOException
	{
		gen.writeStringField(field_name, str);
	}
	
	public void writeExplicitlyTypedString(String field_name, String str) throws IOException
	{
		openObject(field_name, "string");
		gen.writeStringField("VALUE", str);
		closeObject();
	}
	
	public void openArray(String field_name) throws IOException
	{
		gen.writeArrayFieldStart(field_name);
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
		
		writer.writeExplicitlyTypedString("first_name", "Jim");
		writer.writeString("last_name", "Kane");
		
		writer.openArray("cars_owned");
		{
			writer.openObjectInArray("car");
			{
				writer.writeString("make", "BMW");
				writer.writeString("year", "2006");
			
				writer.closeObject();
			}
			
			writer.openObjectInArray("car");
			{
				writer.writeString("make", "SUBARU");
				writer.writeString("year", "2015");
			
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
