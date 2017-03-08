package org.kane.base.examples.book;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldSet;
import org.kane.base.immutability.decks.StandardImmutableSetDeck;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("book-set")
final public class BookDeckSet extends StandardImmutableSetDeck<BookDeckSet, Book>
{
	private FieldHashSet<Book> books = new FieldHashSet<>();
	
	
	private BookDeckSet(Builder builder)
	{
	}
	
	public BookDeckSet()
	{
		this(Collections.emptySet());
	}
	
	public BookDeckSet(Collection<Book> books)
	{
		if ( books != null )
		{
			this.books.addAll(books);
		}
		
		complete();
	}
	
	public void normalize() 
	{	
	}
	
	public void validate() 
	{
		Validator.containsNoNulls(books);
		Validator.containsOnlyInstancesOf(Book.class, books);
	}
	
	public FieldSet<Book> getSimpleContents() { return books; }
	
	
	static public class Builder
	{
		private BookDeckSet under_construction;
		
		public Builder()
		{
			under_construction = new BookDeckSet(this);
		}
		
		public Builder(BookDeckSet starting_point)
		{
			under_construction = starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			if ( book == null ) return;
			under_construction.getSimpleContents().add(book);
		}
		
		public BookDeckSet create()
		{
			return under_construction.deepClone();
		}
	}
}