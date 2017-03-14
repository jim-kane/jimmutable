package org.kane.base.serialization.writer;

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
	
	public void writeNull()
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeNull();
	}
	
	public void writeString(String value)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		
		if ( writer.isBase64Required(value) )
		{
			writer.writeStringObject(value);
		}
		else
		{
			writer.writeString(value);
		}
	}
	
	public void writeBoolean(boolean value) 
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeBoolean(value);
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
	
	public void writeObject(Object obj)
	{
		if ( closed ) throw new SerializeException("Attempt to write to a closed array");
		writer.writeObject(obj);
	}
	
	public void closeArray()
	{
		if ( closed ) return;
		writer.closeArray();
		closed = true;
	}
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return writer.isJSON(); }
	public boolean isXML() { return writer.isXML(); }
}
