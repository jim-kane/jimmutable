package org.kane.base.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.serialization.Normalizer;
import org.kane.base.serialization.Validator;

import com.google.common.collect.ComparisonChain;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * An example of a proper implementation of a StandardImmutableObject
 * 
 * @author jim.kane
 *
 */
@XStreamAlias("book")
final public class Book extends StandardImmutableObject
{
	private String title; // required, upper-case
	private int page_count; // required, must be 0 or greater
	
	private String isbn; // optional
	
	private BindingType binding; // required

	private FieldArrayList<String> authors;
	
	// builder constructor...
	private Book(Builder builder)
	{
		super();
		
		// building constructor.  Builder will call complete...
		this.authors = new FieldArrayList();
	}
	
	// copy constructor...
	public Book(String title, int page_count, String isbn, BindingType binding, Collection<String> authors)
	{
		super();
		
		// building copy constructor.  Builder will call complete...
		this.title = title;
		this.page_count = page_count;
		this.isbn = isbn;
		this.binding = binding;
		
		this.authors = new FieldArrayList(authors);
		
		complete();
	}
	
	// copy constructor...
	public Book(String title, int page_count, String isbn, BindingType binding, String author)
	{
		this(title,page_count,isbn,binding,toCollection(author));
	} 
	
	static private Collection<String> toCollection(String author)
	{
		List<String> ret = new ArrayList();
		ret.add(author);
		return ret;
	}

	/**
	 * Normalize the book object (convert the title to upper case)
	 */
	public void normalize() 
	{
		title = Normalizer.upperCase(title);
	}
	
	/**
	 * Validate the object 
	 */
	public void validate()
	{
		Validator.notNull(title);
		Validator.min(page_count, 0);
		
		Validator.notNull(binding);
		
		Validator.containsNoNulls(authors);
	}
	
	/**
	 * Freeze the object
	 */
	public void freeze()
	{
		authors.freeze();
	}
	
	public String getSimpleTitle() { return title; } 
	public int getSimplePageCount() { return page_count; }
	
	public BindingType getSimpleBinding() { return binding; }
	public List<String> getSimpleAuthors() { return authors; }
	
	public boolean hasISBN() { return isbn != null; }
	public String getOptionalISBN(String default_value)
	{
		if ( isbn == null ) return default_value;
		return isbn;
	}

	public int compareTo(Object o) 
	{
		if ( !(o instanceof Book) ) return 0;
		
		Book other = (Book)o;
		
		return ComparisonChain.start()
				.compare(getSimpleTitle(), other.getSimpleTitle())
				.compare(getSimplePageCount(), other.getSimplePageCount())
				.compare(getSimpleBinding(), other.getSimpleBinding())
				.compare(getOptionalISBN(""), other.getOptionalISBN(""))
				.compare(getSimpleAuthors().size(),other.getSimpleAuthors().size()).result();
	}

	public int hashCode() 
	{
		return Objects.hash(getSimpleTitle(), getSimplePageCount(), getOptionalISBN(null), getSimpleBinding());
	}

	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Book) ) return false;
		
		Book other = (Book)obj;
		
		if ( !getSimpleTitle().equals(other.getSimpleTitle()) ) return false;
		if ( getSimplePageCount() != other.getSimplePageCount() ) return false;
		
		if ( !Objects.equals(isbn, other.isbn) ) return false;
		
		if ( getSimpleBinding() != other.getSimpleBinding() ) return false;
		
		if ( !getSimpleAuthors().equals(other.getSimpleAuthors()) ) return false;
		
		return true;
	}
	
	static public class Builder
	{
		private Book under_construction;
		
		public Builder()
		{
			under_construction = new Book(this);
		}
		
		public Builder(Book starting_point)
		{
			under_construction = (Book)starting_point.deepMutableCloneForBuilder();
		}
		
		public void setTitle(String title) 
		{ 
			under_construction.title = title;
		}
		
		public void setPageCount(int count)
		{
			under_construction.page_count = count;
		}
		
		public void setISBN(String isbn)
		{
			under_construction.isbn = isbn;
		}
		
		public void addAuthor(String author)
		{
			under_construction.authors.add(author);
		}
		
		public void setBindingType(BindingType type)
		{
			under_construction.binding = type;
		}
		
		public Book create()
		{
			// You need to do the "under_construction" swap first because complete may Throw a ValidationException
			Book ret = under_construction;
			under_construction = new Book(this); 
			
			ret.complete();
			return ret;
		}
	}
}

