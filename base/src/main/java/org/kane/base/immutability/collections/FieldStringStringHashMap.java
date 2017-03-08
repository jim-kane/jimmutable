package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * A specialization of a {@link FieldMap} for the extremely common use
 * case of:
 * <ul>
 * 	<li>Backing store is a {@link HashMap}
 * 	<li>Key type ({@code K}) is {@code String}
 * 	<li>Value type ({@code V}) is {@code String}
 * </ul>
 * 
 * <p>It implements a custom {@link Converter} that improves serialization
 * performance by 75% over the default reflection-based converter for {@link MapConverter}.
 * 
 * @author Jim Kane
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 * @see FieldMap
 * @see FieldHashMap
 */
@XStreamAlias("field-string-string-hash-map")
@XStreamConverter(FieldStringStringHashMap.MyConverter.class)
final public class FieldStringStringHashMap extends FieldMap<String,String>
{
	/**
	 * Default constructor (for an empty map)
	 */
	public FieldStringStringHashMap()
	{
		super();
	}
	
	/**
     * Constructs a collection containing the elements of the specified {@link Map},
     * in the order they are returned by the {@link Iterable#iterator() iterator}.
     *
     * @param objs The {@code Map} whose elements are to be placed into this map
     * 
     * @throws NullPointerException if the specified {@code Map} is {@code null}
	 */
	public FieldStringStringHashMap(Map<String,String> initial_values)
	{
		super(initial_values);
	}
	
	@Override
	protected Map<String, String> createNewMutableInstance()
	{
		return new HashMap<>();
	}
	
	/**
	 * A {@link Converter} specialized for
	 * {{@code String} => {@code String}} {@link Map Maps} 
	 * 
	 * @author Jim Kane
	 */

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
