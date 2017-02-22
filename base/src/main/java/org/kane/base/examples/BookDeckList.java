package org.kane.base.examples;

import java.util.Collection;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableDeckList;
import org.kane.base.serialization.Validator;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class serves as the pattern for a list backed "Deck"
 * 
 * @author jim.kane
 *
 */

@XStreamAlias("book-list")
public class BookDeckList extends StandardImmutableObject implements StandardImmutableDeckList<Book>
{
	private FieldArrayList<Book> books = new FieldArrayList();
	
	private BookDeckList(Builder builder)
	{
	}
	
	public BookDeckList(Collection<Book> books)
	{
		super();
		
		books.addAll(books);
		
		complete();
	}
	
	public FieldList<Book> getSimpleContents() { return books; }
	
	public void normalize() 
	{
	}

	
	public void validate() 
	{
		Validator.containsNoNulls(books);
		Validator.containsOnlyInstancesOf(Book.class, books);
	}
	
	public void freeze() { freezeContents(); }
	public int hashCode() { return hashContents(); }
	public boolean equals(Object obj) { return contentsEqual(obj); }
	public int compareTo(Object o) { return contentsCompareTo(o); }

	static public class Builder
	{
		private BookDeckList under_construction;
		
		public Builder()
		{
			under_construction = new BookDeckList(this);
		}
		
		public Builder(BookDeckList starting_point)
		{
			under_construction = (BookDeckList)starting_point.deepMutableCloneForBuilder();
		}

		public void addBook(Book book)
		{
			if ( book == null ) return;
			under_construction.getSimpleContents().add(book);
		}
		
		public BookDeckList create()
		{
			// You need to do the "under_construction" swap first because complete may Throw a ValidationException
			BookDeckList ret = under_construction;
			under_construction = new BookDeckList(this);
			
			ret.complete();
			return ret;
		}
	}
}
