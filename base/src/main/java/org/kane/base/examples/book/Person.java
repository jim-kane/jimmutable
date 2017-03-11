package org.kane.base.examples.book;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.StandardWritable;

public class Person implements StandardWritable 
{
	static public final TypeName TYPE_NAME = new TypeName("person");
	
	static public final FieldName FIELD_FIRST_NAME = new FieldName("first_name");
	static public final FieldName FIELD_LAST_NAME = new FieldName("last_name");
	
	
	private String first_name;
	private String last_name;
	
	public Person(String first_name, String last_name)
	{
		this.first_name = first_name;
		this.last_name = last_name;
	}

	
	public TypeName getTypeName() 
	{
		return TYPE_NAME;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeString(FIELD_FIRST_NAME, first_name);
		writer.writeExplicitlyTypedString(FIELD_LAST_NAME, last_name);
	}
}
