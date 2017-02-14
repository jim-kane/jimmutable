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
public class BookMap extends StandardImmutableObject implements StandardImmutableDeckMap<String,Book>
{
	private FieldHashMap<String,Book> books = new FieldHashMap();
	
	private BookMap(Builder builder)
	{
	}
	
	public BookMap()
	{
		complete();
	}
	
	public BookMap(Map<String,Book> initial_contents)
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
		private BookMap under_construction;
		
		public Builder()
		{
			under_construction = new BookMap(this);
		}
		
		public Builder(BookMap starting_point)
		{
			under_construction = (BookMap)starting_point.deepMutableCloneForBuilder();
		}

		public void putBook(String key, Book book)
		{
			under_construction.assertNotComplete();
			under_construction.getSimpleContents().put(key,book);
		}
		
		public BookMap create()
		{
			under_construction.complete();
			
			return under_construction;
		}
	}
}
