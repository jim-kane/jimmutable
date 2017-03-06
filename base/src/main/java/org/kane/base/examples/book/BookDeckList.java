package org.kane.base.examples.book;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableListDeck;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("book-list")
final public class BookDeckList extends StandardImmutableListDeck<BookDeckList, Book>
{
	private FieldList<Book> books = new FieldArrayList<>();
	
	
	private BookDeckList(Builder builder)
	{
	}
	
	public BookDeckList()
	{
		this(Collections.emptyList());
	}
	
	public BookDeckList(Collection<Book> books)
	{
		super();
		
		if ( books != null )
		{
			this.books.addAll(books);
		}
		
		complete();
	}

	@Override
	public FieldList<Book> getSimpleContents()
	{
		return books;
	}
	
	public void normalize() 
	{
	}
	
	public void validate() 
	{
		Validator.containsNoNulls(getSimpleContents());
		Validator.containsOnlyInstancesOf(Book.class, getSimpleContents());
	}

	static public class Builder
	{
		private BookDeckList under_construction;
		
		public Builder()
		{
			under_construction = new BookDeckList(this);
		}
		
		public Builder(BookDeckList starting_point)
		{
			under_construction = starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			if ( book == null ) return;
			under_construction.getSimpleContents().add(book);
		}
		
		public BookDeckList create()
		{
			return under_construction.deepClone();
		}
	}
}
