package org.kane.base.serialization;

import org.kane.base.examples.book.Book;
import org.kane.base.examples.book.BookDeckList;
import org.kane.base.examples.book.BookDeckMap;
import org.kane.base.examples.book.BookDeckSet;
import org.kane.base.examples.product_data.ItemKey;
import org.kane.base.examples.product_data.ItemSpecifications;
import org.kane.base.serialization.reader.ObjectParseTree;

public class JimmutableTypeNameRegister
{
	static public void registerAllTypes()
	{
		ObjectParseTree.registerTypeName(Book.class);
		ObjectParseTree.registerTypeName(BookDeckList.class);
		ObjectParseTree.registerTypeName(BookDeckMap.class);
		ObjectParseTree.registerTypeName(BookDeckSet.class);
		
		ObjectParseTree.registerTypeName(ItemKey.class);
		ObjectParseTree.registerTypeName(ItemSpecifications.class);
	}
}
