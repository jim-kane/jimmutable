package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Since it is used *so frequently* a special String/String hash map is
 * provided. It has a custom converter that is (approximately) 75% than the
 * built in, reflection based converter for map.
 * 
 * @author jim.kane
 *
 */
@XStreamConverter(FieldStringStringHashMap.MyConverter.class)
public class FieldStringStringHashMap extends FieldMap<String,String>
{
	public FieldStringStringHashMap()
	{
		super();
	}
	
	public FieldStringStringHashMap(Map<String,String> initial_values)
	{
		super(initial_values);
	}
	
	protected Map<String, String> createNewMutableInstance()
	{
		return new HashMap<>();
	}

	static public class MyConverter implements Converter
	{
		@SuppressWarnings("rawtypes")
		public boolean canConvert(Class type)
		{
			return type.equals(FieldStringStringHashMap.class);
		}
		
		public void marshal(Object source_raw, HierarchicalStreamWriter writer, MarshallingContext context)
		{
			FieldStringStringHashMap source = (FieldStringStringHashMap)source_raw;
			
			for ( Map.Entry<String, String> e : source.entrySet() )
			{
				writer.startNode("entry");
				
				writer.startNode("key");
				writer.setValue(e.getKey());
				writer.endNode();
				
				writer.startNode("value");
				writer.setValue(e.getValue());
				writer.endNode();
				
				writer.endNode();
			}
		}
	
		@Override
		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
		{
			FieldStringStringHashMap ret = new FieldStringStringHashMap();
			
			while(reader.hasMoreChildren())
			{
				reader.moveDown();
				
				if ( reader.getNodeName().equals("entry") )
				{
					readEntry(reader, ret);
				}
				// Anything else is simply ignored...
				
				reader.moveUp();
			}
			
			return ret;
		}
		
		private void readEntry(HierarchicalStreamReader reader, FieldStringStringHashMap ret)
		{
			String key = null;
			String value = null;
			
			while(reader.hasMoreChildren())
			{
				reader.moveDown();
				
				if ( reader.getNodeName().equals("key") )
				{
					key = reader.getValue();
				}
				if ( reader.getNodeName().equals("value") )
				{
					value = reader.getValue();
				}
				
				reader.moveUp();
			}
			
			if ( key != null && value != null )
				ret.put(key, value);
		}
	}
}
