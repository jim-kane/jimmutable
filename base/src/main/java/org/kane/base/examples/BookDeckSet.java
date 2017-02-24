package org.kane.base.examples;

import java.util.Collection;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldSet;
import org.kane.base.immutability.decks.StandardImmutableDeckSet;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("book-set")
public class BookDeckSet extends StandardImmutableObject implements StandardImmutableDeckSet<Book>
{
	private FieldHashSet<Book> books = new FieldHashSet();
	
	
	private BookDeckSet(Builder builder)
	{
	}
	
	public BookDeckSet(Collection<Book> books)
	{
		this.books.addAll(books);
		
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
	
	public void freeze() { freezeContents();	}
	public int hashCode() { return hashContents(); }
	public boolean equals(Object obj) { return contentsEqual(obj); }
	public int compareTo(Object o) { return contentsCompareTo(o); }

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
			under_construction = (BookDeckSet)starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			if ( book == null ) return;
			under_construction.getSimpleContents().add(book);
		}
		
		public BookDeckSet create()
		{
			return (BookDeckSet)under_construction.deepClone();
		}
	}
}