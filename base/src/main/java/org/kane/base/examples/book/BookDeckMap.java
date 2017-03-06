package org.kane.base.examples.book;

import java.util.Collections;
import java.util.Map;

import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.immutability.collections.FieldMap;
import org.kane.base.immutability.decks.StandardImmutableMapDeck;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("book-map")
final public class BookDeckMap extends StandardImmutableMapDeck<BookDeckMap, String, Book>
{
	private FieldHashMap<String,Book> books = new FieldHashMap<>();
	
	private BookDeckMap(Builder builder)
	{
	}
	
	public BookDeckMap()
	{
		this(Collections.emptyMap());
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
	
	static public class Builder
	{
		private BookDeckMap under_construction;
		
		public Builder()
		{
			under_construction = new BookDeckMap(this);
		}
		
		public Builder(BookDeckMap starting_point)
		{
			under_construction = starting_point.deepMutableCloneForBuilder();
		}

		public void putBook(String key, Book book)
		{
			if ( key == null || book == null ) return;
			under_construction.getSimpleContents().put(key,book);
		}
		
		public BookDeckMap create()
		{
			return under_construction.deepClone();
		}
	}
}

