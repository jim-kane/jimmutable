package org.kane.base.immutability.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardObject;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableFieldHashSetTest // extends TestCase
{
	/*
	static private class DummyObject extends StandardImmutableObject
	{
		private FieldHashSet<String> set;
		transient private Iterator old_iterator;
		
		public int compareTo(Object o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public void freeze() { set.freeze(); }
		public int hashCode() { return set.hashCode(); }

		public FieldHashSet getSimpleArrayList() { return set; } 
		
		public DummyObject()
		{
			set = new FieldHashSet();
			old_iterator = set.iterator();
			
			verifyMutable();
			
			set.add("foo"); 
			
			complete();
			
			verifyImmutable();
			verifyOldIteratorImmutable();
		}
		
		public DummyObject(Builder b)
		{
			set = new FieldHashSet();
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
				set.add("foo");
				set.add("bar");
				set.add("baz");
				set.add("quz");
				set.add("quuz");

				assertEquals(set.size(),5);

				set.remove("quz");
				set.remove("quuz");

				assertEquals(set.size(),3);

				Set<String> quz_values = new HashSet();
				quz_values.add("quz");
				quz_values.add("quuz");

				set.addAll(quz_values);

				assertEquals(set.size(),5);

				set.removeAll(quz_values);

				assertEquals(set.size(),3);

				set.addAll(quz_values);
				set.retainAll(quz_values);

				assertEquals(set,quz_values);

				set.remove(1);

				set.add("blaz");

				Iterator<String> itr = set.iterator();
				itr.next();

				itr.remove(); // Verify that items can be removed using an iterator...


				set.clear();

				assertEquals(set.size(),0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
			}
		}



		public void verifyImmutable()
		{
			assert(!set.isEmpty()); // these tests require list contains at least one element...

			try { set.add("foo"); assert(false); } catch(ImmutableException e) { }
			try { set.remove("foo"); assert(false); } catch(ImmutableException e) { }

			Set<String> quz_values = new HashSet();
			quz_values.add("quz");
			quz_values.add("quuz");


			try { set.addAll(quz_values); assert(false); } catch(ImmutableException e) { }
			try { set.removeAll(quz_values); assert(false); } catch(ImmutableException e) { }
			try { set.retainAll(quz_values); assert(false); } catch(ImmutableException e) { }

			
			try { set.remove(1); assert(false); } catch(ImmutableException e) { }
			
			try
			{
				Iterator<String> itr = set.iterator();
				itr.next();

				itr.remove(); // Verify that items can be removed using an iterator...
				assert(false);
			}
			catch(ImmutableException e) {}

			try { set.clear(); assert(false); } catch(ImmutableException e) { }
		}
		
		public void verifyOldIteratorImmutable()
		{
			try { old_iterator.remove(); assert(false); } catch(ImmutableException e) { }
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
				obj.set.add(el);
			}
			
			public DummyObject create()
			{
				obj.assertNotComplete();
				
				obj.complete();
				
				return obj;
			}
		}
		
		
	}
	
	
    public StandardImmutableFieldHashSetTest( String testName )
    {
        super( testName );
    }

 
    public static Test suite()
    {
        return new TestSuite( StandardImmutableFieldHashSetTest.class );
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
    	System.out.println(obj.toJavaCode("obj"));
    }
    
    public void testSerialization()
    {
    	String obj_as_xml_string = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<DummyObject-set>"
    		     , "   <set>"
    		     , "      <contents class=\"set\">"
    		     , "         <string>foo</string>"
    		     , "      </contents>"
    		     , "   </set>"
    		     , "</DummyObject-set>"
    		);

    	DummyObject obj = (DummyObject)StandardObject.deserialize(obj_as_xml_string, null);
    	
    	assertEquals(obj.set.size(),1);
    	assert(obj.set.contains("foo"));
    	
    	obj.verifyImmutable();
    }
	*/
   
   
}


