package org.kane.base.examples.book;

import java.util.Collections;
import java.util.Map;

import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldMap;
import org.kane.base.immutability.decks.StandardImmutableMapDeck;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;


final public class BookDeckMap extends StandardImmutableMapDeck<BookDeckMap, String, Book>
{
	static private final TypeName TYPE_NAME = new TypeName("jimmutable.examples.BookDeckMap");
	static private final FieldName FIELD_BOOKS = new FieldName("books");
	
	private FieldMap<String,Book> books;
	
	private BookDeckMap(Builder builder)
	{
		books = new FieldHashMap<>();
	}
	
	public BookDeckMap()
	{
		this(Collections.emptyMap());
	}
	
	public BookDeckMap(Map<String,Book> initial_contents)
	{
		super();
		
		books = new FieldHashMap<>();
		
		if ( initial_contents != null )
		{
			this.books.putAll(initial_contents);
		}
		
		complete();
	}
	
	public BookDeckMap(ReadTree t)
	{
		books = t.getMap(FIELD_BOOKS, new FieldHashMap(), ReadAs.STRING, ReadAs.OBJECT, ReadTree.OnError.SKIP);
	}
	
	public TypeName getTypeName() 
	{
		return TYPE_NAME;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeMap(FIELD_BOOKS, books, WriteAs.STRING, WriteAs.OBJECT);
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

