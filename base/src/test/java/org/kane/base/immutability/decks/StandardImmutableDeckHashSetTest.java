package org.kane.base.immutability.decks;

import java.util.ArrayList;
import java.util.List;

import org.kane.base.examples.book.BindingType;
import org.kane.base.examples.book.Book;
import org.kane.base.examples.book.BookDeckList;
import org.kane.base.examples.book.BookDeckSet;
import org.kane.base.examples.book.BookDeckSet.Builder;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableDeckHashSetTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StandardImmutableDeckHashSetTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableDeckHashSetTest.class );
    }

    public void testBookSet()
    {
    	List<Book> test_books = new ArrayList();
    	
    	test_books.add(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	
    	
		Builder builder = new Builder();
		
		builder.addBook(test_books.get(0));
		builder.addBook(test_books.get(1));

		BookDeckSet first_library = builder.create();
		
		assertEquals(first_library.getSimpleContents().size(),2);
		
		assert(first_library.getSimpleContents().contains(test_books.get(0)));
		assert(first_library.getSimpleContents().contains(test_books.get(1)));
		
		// now test an "append" builder...
		
		builder = new Builder(first_library);
		
		builder.addBook(test_books.get(2));
		builder.addBook(test_books.get(3));
		
		BookDeckSet second_library = builder.create();
		
		// Confirm that first library has not changed...
		assertEquals(second_library.getSimpleContents().size(),3); // because book 2 and 3 are duplicates...
		
		assert(second_library.getSimpleContents().containsAll(test_books));
		
		
		System.out.println(second_library.toJavaCode("obj"));
    }
    
    public void testSerialization()
    {
    	String obj_as_xml_string = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<book-set>"
    		     , "   <books>"
    		     , "      <contents class=\"set\">"
    		     , "         <book>"
    		     , "            <title>OF MICE AND MEN</title>"
    		     , "            <page__count>1211</page__count>"
    		     , "            <isbn>32423423711</isbn>"
    		     , "            <binding>TRADE_PAPER_BACK</binding>"
    		     , "            <authors>"
    		     , "               <contents class=\"list\">"
    		     , "                  <string>John Steinbeck</string>"
    		     , "               </contents>"
    		     , "            </authors>"
    		     , "         </book>"
    		     , "         <book>"
    		     , "            <title>GRAPES OF WRATH</title>"
    		     , "            <page__count>1211</page__count>"
    		     , "            <isbn>33242347234</isbn>"
    		     , "            <binding>TRADE_PAPER_BACK</binding>"
    		     , "            <authors>"
    		     , "               <contents class=\"list\">"
    		     , "                  <string>John Steinbeck</string>"
    		     , "               </contents>"
    		     , "            </authors>"
    		     , "         </book>"
    		     , "         <book>"
    		     , "            <title>O LOST</title>"
    		     , "            <page__count>1211</page__count>"
    		     , "            <isbn>1123234234</isbn>"
    		     , "            <binding>TRADE_PAPER_BACK</binding>"
    		     , "            <authors>"
    		     , "               <contents class=\"list\">"
    		     , "                  <string>Thomas Wolfe</string>"
    		     , "               </contents>"
    		     , "            </authors>"
    		     , "         </book>"
    		     , "      </contents>"
    		     , "   </books>"
    		     , "</book-set>"
    		);

    	BookDeckSet obj = (BookDeckSet)StandardObject.deserialize(obj_as_xml_string,null);
    	
    	
    	List<Book> test_books = new ArrayList();
    	
    	test_books.add(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	
    	Builder builder = new Builder();
		
		builder.addBook(test_books.get(0));
		builder.addBook(test_books.get(1));
		builder.addBook(test_books.get(2));
		
		BookDeckSet second_library = builder.create();
		
		assertEquals(obj,second_library);
    }
}

