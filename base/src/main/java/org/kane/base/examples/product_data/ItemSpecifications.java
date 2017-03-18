package org.kane.base.examples.product_data;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.immutability.collections.FieldMap;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Format;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;
import org.kane.base.utils.Validator;

public class ItemSpecifications extends StandardImmutableObject<ItemSpecifications>
{
	static public final TypeName TYPE_NAME = new TypeName("jimmutable.examples.ItemSpecifications"); public TypeName getTypeName() { return TYPE_NAME; }
	
	static private final FieldName FIELD_ITEM_KEY = new FieldName("item_key");
	static private final FieldName FIELD_ATTRIBUTES = new FieldName("attributes");
	
	private ItemKey item_key; // required
	private FieldMap<ItemAttribute,String> attributes;
	
	public ItemSpecifications(Builder builder)
	{
		this.attributes = new FieldHashMap();
	}
	
	public ItemSpecifications(ObjectReader reader)
	{
		item_key = (ItemKey)reader.getObject(FIELD_ITEM_KEY, null);
		attributes = reader.getMap(FIELD_ATTRIBUTES, new FieldHashMap(), ItemAttribute.READ_AS, ReadAs.STRING, ObjectReader.OnError.SKIP);
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeObject(FIELD_ITEM_KEY, item_key);
		writer.writeMap(FIELD_ATTRIBUTES, attributes, WriteAs.STRING, WriteAs.STRING);
	}

	public int compareTo(ItemSpecifications o) 
	{
		return getSimpleItemKey().compareTo(o.getSimpleItemKey());
	}

	public void freeze() 
	{
		attributes.freeze();
	}

	
	public void normalize() 
	{
		// These are attributes in the PKV, but in the object they are expressed in the item key
		// If they happen to ahve been set, remove them
		
		attributes.remove(ItemAttribute.ATTRIBUTE_BRAND);
		attributes.remove(ItemAttribute.ATTRIBUTE_PN);
	}

	public void validate() 
	{
		Validator.notNull(item_key);		
	}

	public int hashCode()
	{
		return getSimpleItemKey().hashCode();
	}

	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof ItemSpecifications) ) return false;
		
		ItemSpecifications other = (ItemSpecifications)obj;
		
		if ( !getSimpleItemKey().equals(other.getSimpleItemKey()) ) return false;
		
		return attributes.equals(other.attributes);
	}

	static public class Builder
	{
		private ItemSpecifications under_construction;
		
		public Builder()
		{
			under_construction = new ItemSpecifications(this);
		}
		
		public Builder(ItemSpecifications starting_point)
		{
			under_construction = (ItemSpecifications)starting_point.deepMutableCloneForBuilder();
		}
		
		public void setItemKey(ItemKey key)
		{
			Validator.notNull(key);
			under_construction.item_key = key;
		}
		
		public void putAttribute(ItemAttribute attribute, String value)
		{
			Validator.notNull(attribute,value);
			under_construction.attributes.put(attribute, value);
		}
		
		public ItemSpecifications create()
		{
			return under_construction.deepClone();
		}
	}
	
	public ItemKey getSimpleItemKey() { return item_key; }
	
	public FieldMap<ItemAttribute,String> getSimpleAttributes() { return attributes; }
}
