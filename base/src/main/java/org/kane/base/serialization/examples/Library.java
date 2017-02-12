package org.kane.base.serialization.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kane.base.serialization.StandardImmutableDeckArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class serves as a "deck of books" (or a Library... he he)
 * 
 * This class is the model immutable deck pattern in the StandardObject pattern
 * 
 * @author jim.kane
 *
 */

@XStreamAlias("library")
public class Library extends StandardImmutableDeckArrayList
{
	private Library(Builder builder)
	{
		super(Collections.EMPTY_LIST);
	}
	
	public Library(Collection<Book> books)
	{
		super(books);
		
		complete();
	}
	
	public Class getOptionalValidationType(Class default_value) { return Book.class; }
	public List<Book> getSimpleBooks() { return getSimpleContents(); }
	
	static public class Builder
	{
		private Library under_construction;
		
		public Builder()
		{
			under_construction = new Library(this);
		}
		
		public Builder(Library starting_point)
		{
			under_construction = new Library(starting_point.getSimpleContents());
		}

		public void addBook(Book book)
		{
			under_construction.assertNotComplete();
			under_construction.getSimpleContents().add(book);
		}
		
		public Library create()
		{
			under_construction.complete();
			
			return under_construction;
		}
	}
	
	static public void main(String args[])
	{
		List<String> authors = new ArrayList();
		authors.add("John Steinbeck");
		
		Builder b = new Builder();
		
		b.addBook(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, authors));
		b.addBook(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, authors));
		
		System.out.println(b.create().toXML());
	}
}
