package org.kane.base.serialization;

import org.kane.base.examples.book.Book;
import org.kane.base.examples.book.BookDeckList;
import org.kane.base.examples.book.BookDeckMap;
import org.kane.base.examples.book.BookDeckSet;
import org.kane.base.serialization.reader.ObjectReader;

public class JimmutableTypeNameRegister
{
	static public void registerAllTypes()
	{
		ObjectReader.registerTypeName(Book.class);
		ObjectReader.registerTypeName(BookDeckList.class);
		ObjectReader.registerTypeName(BookDeckMap.class);
		ObjectReader.registerTypeName(BookDeckSet.class);
	}
}
