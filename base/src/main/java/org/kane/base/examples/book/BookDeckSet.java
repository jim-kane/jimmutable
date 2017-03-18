package org.kane.base.examples.book;

import java.util.Collection;
import java.util.Collections;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldHashSet;
import org.kane.base.immutability.collections.FieldSet;
import org.kane.base.immutability.decks.StandardImmutableSetDeck;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.serialization.reader.ObjectParseTree;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;
import org.kane.base.utils.Validator;

/**
 * An example of a set based deck
 * 
 * @author jim.kane
 *
 */
final public class BookDeckSet extends StandardImmutableSetDeck<BookDeckSet, Book>
{
	static public final TypeName TYPE_NAME = new TypeName("jimmutable.examples.BookDeckSet");
	static private final FieldName FIELD_BOOKS = new FieldName("books");
	
	private FieldSet<Book> books;
	
	public BookDeckSet()
	{
		this(Collections.EMPTY_LIST);
	}
	
	public BookDeckSet(Collection<Book> books)
	{
		super();
		
		this.books = new FieldHashSet();
		this.books.addAll(books);
		
		complete();
	}

	private BookDeckSet(Builder builder)
	{
		books = new FieldHashSet<>();
	}
	
	public BookDeckSet(ObjectParseTree t)
	{
		books = t.getCollection(FIELD_BOOKS, new FieldHashSet(), ReadAs.OBJECT, ObjectParseTree.OnError.SKIP);
	}
	
	public TypeName getTypeName() 
	{
		return TYPE_NAME;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeCollection(FIELD_BOOKS, books, WriteAs.OBJECT);
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