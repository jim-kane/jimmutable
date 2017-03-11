package org.kane.base.serialization.reader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Optional;
import org.kane.base.serialization.Validator;

public class ReadTree implements Iterable<ReadTree>
{
	private FieldName field_name; // required
	private String value; // optional
	private LinkedList<ReadTree> children; // optional
	
	public ReadTree(FieldName field_name)
	{
		Validator.notNull(field_name);
		
		this.field_name = field_name;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public FieldName getSimpleFieldName() { return field_name; }
	
	public boolean hasValue() { return value != null; }
	public String getOptionalValue(String default_value) { return Optional.getOptional(value, null, default_value); }
	
	public Iterator<ReadTree> iterator() 
	{
		if ( children == null ) return Collections.emptyIterator();
		
		return children.iterator();
	}
	
	public void add(ReadTree child)
	{
		if ( children == null ) children = new LinkedList();
		children.add(child);
	}
	
	private ReadTree find(FieldName field_name, ReadTree default_value)
	{
		for ( ReadTree child : this )
		{
			if ( child.getSimpleFieldName().equals(field_name) )
			{
				return child;
			}
		}
		
		return default_value;
	}
	
	private boolean remove(ReadTree t)
	{
		Iterator<ReadTree> itr = iterator();
		
		while(itr.hasNext())
		{
			if ( itr.next() == t ) 
			{
				itr.remove();
				return true;
			}
		}
		
		return false;
	}
	
	protected void removeLast()
	{
		if ( children == null ) return;
		
		children.removeLast();
	}
	
	public String readString(FieldName field_name, String default_value)
	{
		ReadTree t = find(field_name, null);
		
		if ( t == null ) return default_value; // no node with the specified name
		
		if ( t.hasValue() )
		{
			remove(t);
			return t.getOptionalValue(default_value);
		}
		
		// Last option: if this is a primative (i.e. t has a child with a primative_value field, then we can read that as a string)
		
		ReadTree primative_value = t.find(FieldName.FIELD_NAME_PRIMATIVE_VALUE, null);
		
		if ( primative_value != null && primative_value.hasValue() )
		{
			remove(t);
			return primative_value.getOptionalValue(default_value);
		}
		
		return default_value; // could not read as a string
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		diagnosticPrint(b, 0);
		
		return b.toString().trim();
	}
	
	private void diagnosticPrint(StringBuilder builder, int indent)
	{
		for ( int i = 0; i < indent; i++ )
		{
			builder.append('\t');
		}
		
		builder.append(field_name.getSimpleName());
		
		if ( hasValue() )
		{
			builder.append(String.format(": %s", getOptionalValue(null)));
		}
		
		builder.append("\n");
		
		for ( ReadTree child : this )
		{
			child.diagnosticPrint(builder, indent+1);
		}
	}
}
