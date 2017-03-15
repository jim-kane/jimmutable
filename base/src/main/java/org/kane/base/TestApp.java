package org.kane.base;

import java.io.StringReader;

import org.kane.base.examples.book.Person;
import org.kane.base.serialization.reader.Parser;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriterUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class TestApp 
{
	static public void main(String args[]) throws Exception
	{
		Person p = new Person("Jim", "Kane");
		
		char ch = 0;
		
		String xml = ObjectWriterUtils.writeObject(Format.XML_PRETTY_PRINT, "", null);
		
		System.out.println(xml);
		
		JsonFactory jfactory = new JsonFactory();
		
		
		ReadTree tree = Parser.parse(new StringReader(xml), null);
		
		System.out.println(tree);
		
		String str = (String)tree.asObject(null);
		
		System.out.println("Str = "+str);
	}
}
