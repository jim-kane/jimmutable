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
		writer.writeString(FIELD_FIRST_NAME, "");
		writer.writeObject(FIELD_LAST_NAME, null);
	}
	
	
	static public class Car implements StandardWritable 
	{
		static public final TypeName TYPE_NAME = new TypeName("car");
		
		static public final FieldName FIELD_MAKE = new FieldName("make");
		static public final FieldName FIELD_YEAR = new FieldName("year");
		
		private String make;
		private int year;
		
		public Car(String make, int year)
		{
			this.make = make;
			this.year = year;
		}
		
		public TypeName getTypeName() 
		{
			return TYPE_NAME;
		}

		public void write(ObjectWriter writer) 
		{
			writer.writeString(FIELD_MAKE, make);
			writer.writeInt(FIELD_YEAR, year);
		}
		
	}
}
