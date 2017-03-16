package org.kane.base.examples.book;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableListDeck;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;


final public class BookDeckList extends StandardImmutableListDeck<BookDeckList, Book>
{
	static private final TypeName TYPE_NAME = new TypeName("jimmutable.examples.BookDeckList");
	static private final FieldName FIELD_BOOKS = new FieldName("books");
	
	private FieldList<Book> books;
	
	public BookDeckList()
	{
		this(Collections.EMPTY_LIST);
	}
	
	public BookDeckList(Collection<Book> books)
	{
		super();
		
		this.books = new FieldArrayList();
		this.books.addAll(books);
		
		complete();
	}
	
	private BookDeckList(Builder builder)
	{
		books = new FieldArrayList();
	}
	
	public BookDeckList(ReadTree t)
	{
		books = t.getCollectionOfObjects(FIELD_BOOKS, new FieldArrayList(), ReadTree.OnError.SKIP);
	}
	
	public TypeName getTypeName() 
	{
		return TYPE_NAME;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeCollection(FIELD_BOOKS, books, WriteAs.OBJECTS);
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
