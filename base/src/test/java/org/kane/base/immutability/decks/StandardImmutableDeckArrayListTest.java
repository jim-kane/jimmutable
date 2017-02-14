package org.kane.base.immutability.decks;

import java.util.ArrayList;
import java.util.List;

import org.kane.base.examples.BindingType;
import org.kane.base.examples.Book;
import org.kane.base.examples.Library;
import org.kane.base.examples.Library.Builder;
import org.kane.base.serialization.JavaCodeUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableDeckArrayListTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StandardImmutableDeckArrayListTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableDeckArrayListTest.class );
    }

    public void testLibrary()
    {
    	List<String> authors = new ArrayList();
		authors.add("John Steinbeck");
		
		Builder builder = new Builder();
		
		builder.addBook(new Book("Grapes of Wrath", 1211, "33242347234", BindingType.TRADE_PAPER_BACK, authors));
		builder.addBook(new Book("Of Mice and Men", 1211, "32423423711", BindingType.TRADE_PAPER_BACK, authors));
		
		Library first_library = builder.create();
		
		assertEquals(first_library.getSimpleBooks().size(),2);
		
		assertEquals(first_library.getSimpleBooks().get(0).getSimpleTitle(),"GRAPES OF WRATH");
		assertEquals(first_library.getSimpleBooks().get(1).getSimpleTitle(),"OF MICE AND MEN");
		
		System.out.println(JavaCodeUtils.prettyPrintXML(first_library.toXML(), null));
		System.out.println(first_library.toJSON());
		
		// now test an "append" builder...
		
		builder = new Builder(first_library);
		
		
		authors = new ArrayList();
		authors.add("Thomas Wolfe");
		
		builder.addBook(new Book("O Lost", 1211, "1123234234", BindingType.TRADE_PAPER_BACK, authors));
		
		Library second_library = builder.create();
		
		// Confirm that first library has not changed...
		assertEquals(first_library.getSimpleBooks().size(),2);
		
		assertEquals(first_library.getSimpleBooks().get(0).getSimpleTitle(),"GRAPES OF WRATH");
		assertEquals(first_library.getSimpleBooks().get(1).getSimpleTitle(),"OF MICE AND MEN");
		
		// And that second library was properly appended to...
		assertEquals(second_library.getSimpleBooks().size(),3);
		
		assertEquals(second_library.getSimpleBooks().get(0).getSimpleTitle(),"GRAPES OF WRATH");
		assertEquals(second_library.getSimpleBooks().get(1).getSimpleTitle(),"OF MICE AND MEN");
		assertEquals(second_library.getSimpleBooks().get(2).getSimpleTitle(),"O LOST");
    }
}

