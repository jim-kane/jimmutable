package org.kane.base.examples;

import java.util.Map;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.immutability.collections.FieldMap;
import org.kane.base.immutability.decks.StandardImmutableDeckMap;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A test of a "hash map backed deck"
 * 
 * @author jim.kane
 *
 */
@XStreamAlias("book-map")
public class BookDeckMap extends StandardImmutableObject implements StandardImmutableDeckMap<String,Book>
{
	private FieldHashMap<String,Book> books = new FieldHashMap();
	
	private BookDeckMap(Builder builder)
	{
	}
	
	public BookDeckMap()
	{
		complete();
	}
	
	public BookDeckMap(Map<String,Book> initial_contents)
	{
		books.putAll(initial_contents);
		
		complete();
	}

	public FieldMap<String,Book> getSimpleContents() { return books; }

	public void normalize() 
	{
	}

	
	public void validate() 
	{
		Validator.containsOnlyInstancesOf(String.class, Book.class, books);
	}
	
	public void freeze() { freezeContents();	}
	public int hashCode() { return hashContents(); }
	public boolean equals(Object obj) { return contentsEqual(obj); }
	public int compareTo(Object o) { return contentsCompareTo(o); }

	static public class Builder
	{
		private BookDeckMap under_construction;
		
		public Builder()
		{
			under_construction = new BookDeckMap(this);
		}
		
		public Builder(BookDeckMap starting_point)
		{
			under_construction = (BookDeckMap)starting_point.deepMutableCloneForBuilder();
		}

		public void putBook(String key, Book book)
		{
			if ( key == null || book == null ) return;
			under_construction.getSimpleContents().put(key,book);
		}
		
		public BookDeckMap create()
		{
			return (BookDeckMap)under_construction.deepClone();
		}
	}
}

