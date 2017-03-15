package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

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
}
