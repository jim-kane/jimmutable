package org.kane.base.examples;

import java.util.Collection;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldSet;
import org.kane.base.immutability.decks.StandardImmutableDeckSet;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("book-set")
public class BookSet extends StandardImmutableObject implements StandardImmutableDeckSet<Book>
{
	private FieldHashSet<Book> books = new FieldHashSet();
	
	
	private BookSet(Builder builder)
	{
	}
	
	public BookSet(Collection<Book> books)
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
		private BookSet under_construction;
		
		public Builder()
		{
			under_construction = new BookSet(this);
		}
		
		public Builder(BookSet starting_point)
		{
			under_construction = (BookSet)starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			under_construction.assertNotComplete();
			under_construction.getSimpleContents().add(book);
		}
		
		public BookSet create()
		{
			under_construction.complete();
			
			return under_construction;
		}
	}
}