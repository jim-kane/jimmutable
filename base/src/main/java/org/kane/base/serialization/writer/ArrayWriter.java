package org.kane.base.serialization.writer;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.SerializeException;

public class ArrayWriter 
{
	private LowLevelWriter writer;
	private boolean closed;
	
	public ArrayWriter(LowLevelWriter writer)
	{
		this.writer = writer;
		this.closed = false;
	}
	
	public void writeString(String value)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeString(value);
	}
	
	public void writeChar(char value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeChar(value);
	}
	
	public void writeByte(byte value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeByte(value);
	}
	
	public void writeShort(short value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeShort(value);
	}
	
	public void writeInt(int value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeInt(value);
	}
	
	public void writeLong(long value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeLong(value);
	}
	
	public void writeFloat(float value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeFloat(value);
	}
	
	public void writeDouble(double value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeDouble(value);
	}
	
	
	public void writeExplicitlyTypedString(String value)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplicitlyTypedString(value);
	}
	
	public void writeExplicitlyTypedByte(byte value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplicitlyTypedByte(value);
	}
	
	public void writeExplicitlyTypedShort(short value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplicitlyTypedShort(value);
	}
	
	public void writeExplicitlyTypedInt(int value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplicitlyTypedInt(value);
	}
	
	public void writeExplicitlyTypedLong(long value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplicitlyTypedLong(value);
	}
	
	public void writeExplicitlyTypedFloat(float value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplitlyTypedFloat(value);
	}
	
	public void writeExplicitlyTypedDouble(double value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeExplitlyTypedDouble(value);
	}
	
	public void writeObject(StandardWritable obj)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.openObject();
		{
			obj.write(new ObjectWriter(writer));
			writer.closeObject();
		}
	}
	
	public void writeExplictlyTypedObject(StandardWritable obj)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.openObject(obj.getTypeName());
		{
			obj.write(new ObjectWriter(writer));
			writer.closeObject();
		}
	} 
	
	public ArrayWriter writeOpenArray()
	{
		writer.openArray();
		
		return new ArrayWriter(writer);
	}
	
	public void writeCloseArray()
	{
		if ( closed ) return;
		writer.closeArray();
		closed = true;
	}
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return getSimpleFormat() == Format.JSON; }
	public boolean isXML() { return getSimpleFormat() == Format.XML; }
}
