package org.kane.base.immutability.decks;

import java.util.ArrayList;
import java.util.List;

import org.kane.base.examples.book.BindingType;
import org.kane.base.examples.book.Book;
import org.kane.base.examples.book.BookDeckMap;
import org.kane.base.examples.book.BookDeckMap.Builder;
import org.kane.base.serialization.StandardObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableDeckHashMapTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StandardImmutableDeckHashMapTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableDeckHashMapTest.class );
    }

    public void testBookSet()
    {
    	List<Book> test_books = new ArrayList();
    	
    	test_books.add(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	
		Builder builder = new Builder();
		
		builder.putBook("jim_first_book",test_books.get(0));
		builder.putBook("jim_second_book",test_books.get(1));

		BookDeckMap first_library = builder.create();
		
		assertEquals(first_library.getSimpleContents().size(),2);
		
		assert(first_library.getSimpleContents().containsKey("jim_first_book"));
		assert(first_library.getSimpleContents().containsKey("jim_second_book"));
		
		// now test an "append" builder...
		
		builder = new Builder(first_library);
		
		builder.putBook("jim_first_book",test_books.get(2));
		builder.putBook("jim_third_book",test_books.get(3));
		
		BookDeckMap second_library = builder.create();
		
		// Confirm that first library has not changed...
		assertEquals(second_library.getSimpleContents().size(),3); 
		
		System.out.println(second_library.toJavaCode("obj"));
    }
    
    public void testSerialization()
    {
    	String obj_as_xml_string = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<book-map>"
    		     , "   <books>"
    		     , "      <contents>"
    		     , "         <entry>"
    		     , "            <string>jim_second_book</string>"
    		     , "            <book>"
    		     , "               <title>OF MICE AND MEN</title>"
    		     , "               <page__count>1211</page__count>"
    		     , "               <isbn>32423423711</isbn>"
    		     , "               <binding>TRADE_PAPER_BACK</binding>"
    		     , "               <authors>"
    		     , "                  <contents class=\"list\">"
    		     , "                     <string>John Steinbeck</string>"
    		     , "                  </contents>"
    		     , "               </authors>"
    		     , "            </book>"
    		     , "         </entry>"
    		     , "         <entry>"
    		     , "            <string>jim_third_book</string>"
    		     , "            <book>"
    		     , "               <title>O LOST</title>"
    		     , "               <page__count>1211</page__count>"
    		     , "               <isbn>1123234234</isbn>"
    		     , "               <binding>TRADE_PAPER_BACK</binding>"
    		     , "               <authors>"
    		     , "                  <contents class=\"list\">"
    		     , "                     <string>Thomas Wolfe</string>"
    		     , "                  </contents>"
    		     , "               </authors>"
    		     , "            </book>"
    		     , "         </entry>"
    		     , "         <entry>"
    		     , "            <string>jim_first_book</string>"
    		     , "            <book>"
    		     , "               <title>O LOST</title>"
    		     , "               <page__count>1211</page__count>"
    		     , "               <isbn>1123234234</isbn>"
    		     , "               <binding>TRADE_PAPER_BACK</binding>"
    		     , "               <authors>"
    		     , "                  <contents class=\"list\">"
    		     , "                     <string>Thomas Wolfe</string>"
    		     , "                  </contents>"
    		     , "               </authors>"
    		     , "            </book>"
    		     , "         </entry>"
    		     , "      </contents>"
    		     , "   </books>"
    		     , "</book-map>"
    		);
    	
    		BookDeckMap obj = (BookDeckMap)StandardObject.deserialize(obj_as_xml_string,null);
    	
    	assertEquals(obj.getSimpleContents().size(),3); 
    	assert(obj.getSimpleContents().containsKey("jim_first_book"));
    	assert(obj.getSimpleContents().containsKey("jim_second_book"));
    	assert(obj.getSimpleContents().containsKey("jim_third_book"));
    	
    	List<Book> test_books = new ArrayList();
    	
    	test_books.add(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, "John Steinbeck"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	test_books.add(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, "Thomas Wolfe"));
    	
    	Builder builder = new Builder();
    	
    	builder.putBook("jim_first_book",test_books.get(2));
    	builder.putBook("jim_second_book",test_books.get(1));
    	builder.putBook("jim_third_book",test_books.get(3));
    	
    	BookDeckMap second_library = builder.create();
    	
    	assertEquals(obj,second_library);
    }
}
