package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableFieldArrayListTest extends TestCase
{
	@XStreamAlias("DummyObject-list")
	static private class DummyObject extends StandardImmutableObject
	{
		private FieldArrayList<String> list;
		transient private ListIterator old_iterator;
		
		public int compareTo(Object o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public int hashCode() { return list.hashCode(); }

		public FieldArrayList getSimpleArrayList() { return list; } 
		
		public DummyObject()
		{
			list = new FieldArrayList(this);
			old_iterator = list.listIterator();
			
			verifyMutable();
			
			list.add("foo"); 
			
			complete();
			
			verifyImmutable();
			verifyOldIteratorImmutable();
		}
		
		public DummyObject(Builder b)
		{
			list = new FieldArrayList(this);
		}
	
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof DummyObject) ) return false;
			
			DummyObject other = (DummyObject)obj;
			
			if ( !getSimpleArrayList().equals(other.getSimpleArrayList()) ) return false;
			
			return true;
		}
		
		public void verifyMutable()
		{
			try
			{
				list.add("foo");
				list.add("bar");
				list.add("baz");
				list.add("quz");
				list.add("quuz");

				assertEquals(list.size(),5);

				list.remove("quz");
				list.remove("quuz");

				assertEquals(list.size(),3);

				ArrayList<String> quz_values = new ArrayList();
				quz_values.add("quz");
				quz_values.add("quuz");

				list.addAll(quz_values);

				assertEquals(list.size(),5);

				list.removeAll(quz_values);

				assertEquals(list.size(),3);

				list.addAll(0, quz_values);

				list.retainAll(quz_values);

				assertEquals(list,quz_values);

				list.set(1, "quuuz");

				assertEquals(list.get(1),"quuuz");

				list.remove(1);

				list.add(1,"blaz");

				Iterator<String> itr = list.iterator();
				itr.next();

				itr.remove(); // Verify that items can be removed using an iterator...


				list.clear();

				assertEquals(list.size(),0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
			}
		}



		public void verifyImmutable()
		{
			assert(!list.isEmpty()); // these tests require list contains at least one element...

			try { list.add("foo"); assert(false); } catch(ImmutableException e) { }
			try { list.remove("foo"); assert(false); } catch(ImmutableException e) { }

			ArrayList<String> quz_values = new ArrayList();
			quz_values.add("quz");
			quz_values.add("quuz");


			try { list.addAll(quz_values); assert(false); } catch(ImmutableException e) { }
			try { list.removeAll(quz_values); assert(false); } catch(ImmutableException e) { }
			try { list.addAll(0, quz_values); assert(false); } catch(ImmutableException e) { }
			try { list.retainAll(quz_values); assert(false); } catch(ImmutableException e) { }

			try { list.set(1, "quuuz"); assert(false); } catch(ImmutableException e) { }

			try { list.remove(1); assert(false); } catch(ImmutableException e) { }
			try { list.add(1,"blaz"); assert(false); } catch(ImmutableException e) { }

			try
			{
				Iterator<String> itr = list.iterator();
				itr.next();

				itr.remove(); // Verify that items can be removed using an iterator...
				assert(false);
			}
			catch(ImmutableException e) {}

			try { list.clear(); assert(false); } catch(ImmutableException e) { }
		}
		
		public void verifyOldIteratorImmutable()
		{
			try { old_iterator.add("foo"); assert(false); } catch(ImmutableException e) { }
			try { old_iterator.remove(); assert(false); } catch(ImmutableException e) { }
			try { old_iterator.set("bar"); assert(false); } catch(ImmutableException e) { }
		}
		
		static public class Builder
		{
			private DummyObject obj;
			
			public Builder()
			{
				obj = new DummyObject(this);
			}
			
			public void add(String el)
			{
				obj.assertNotComplete();
				obj.list.add(el);
			}
			
			public DummyObject create()
			{
				obj.assertNotComplete();
				
				obj.complete();
				
				return obj;
			}
		}
		
		
	}
	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StandardImmutableFieldArrayListTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableFieldArrayListTest.class );
    }
    
    public void testCopyConstructor()
    {
    	new DummyObject();
    }
    
    public void testBuidler()
    {
    	DummyObject.Builder builder = new DummyObject.Builder();
    	
    	builder.obj.verifyMutable();
    	
    	builder.add("foo");
    	
    	DummyObject obj = builder.create();
    	
    	obj.verifyImmutable();
    	
    	// used to create the code for test serialization...
    	//System.out.println(JavaCodeUtils.toJavaStringLiteral(obj));
    }
    
    public void testSerialization()
    {
    	String xml = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<DummyObject-list>"
    		     , "   <list>"
    		     , "      <parent class=\"DummyObject-list\" reference=\"../..\"/>"
    		     , "      <contents>"
    		     , "         <string>foo</string>"
    		     , "      </contents>"
    		     , "   </list>"
    		     , "</DummyObject-list>"
    		);
    	
    	DummyObject obj = (DummyObject)StandardObject.fromXML(xml);
    	
    	assertEquals(obj.list.size(),1);
    	assertEquals(obj.list.get(0),"foo");
    	
    	obj.verifyImmutable();
    }

   
   
}

