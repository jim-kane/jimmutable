package org.kane.base;

import org.kane.base.examples.book.Person;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriterUtils;

public class TestApp 
{
	static public void main(String args[]) throws Exception
	{
		Person p = new Person("Jim", "Kane");
		
		String xml = ObjectWriterUtils.writeObject(Format.XML, p, null);
		
		System.out.println(JavaCodeUtils.prettyPrintXML(xml, null));
	}
}
