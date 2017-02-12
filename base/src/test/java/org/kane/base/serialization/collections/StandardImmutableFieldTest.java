package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableFieldTest extends TestCase
{
	static private class StandardFieldTest extends StandardImmutableObject
	{
		private AbstractFieldList<String> array_list;
		private StandardImmutableFieldHashSet<String> hash_set;
		
		public int compareTo(Object o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public int hashCode() { return array_list.hashCode() + hash_set.hashCode(); }

		public AbstractFieldList getSimpleArrayList() { return array_list; } 
		public StandardImmutableFieldHashSet getSimpleHashSet() { return hash_set; }
		
		
		public StandardFieldTest()
		{
			array_list = new FieldArrayList(this);
			hash_set = new StandardImmutableFieldHashSet(this);
			
			verifyListMutable(array_list);
			//verifySetMutable(hash_set);
			
			array_list.add("foo"); 
			hash_set.add("foo");
			
			complete();
			
			verifyListImmutable(array_list);
			//verifySetImmutable(hash_set);
		}
	
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof StandardFieldTest) ) return false;
			
			StandardFieldTest other = (StandardFieldTest)obj;
			
			if ( !getSimpleArrayList().equals(other.getSimpleArrayList()) ) return false;
			if ( !getSimpleHashSet().equals(other.getSimpleHashSet()) ) return false;
			
			return true;
		}
	}
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StandardImmutableFieldTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableFieldTest.class );
    }
    
    public void testStandardFields()
    {
    	new StandardFieldTest();
    }

   
    static public void verifyListMutable(List<String> list)
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
    
    
    
    static public void verifyListImmutable(List<String> list)
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
    
    static public void verifySetMutable(Set<String> set)
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
    		
    		assertEquals(set.size(),4);
    		
    		ArrayList<String> quz_values = new ArrayList();
        	quz_values.add("quz");
        	quz_values.add("quuz");
        	
        	set.addAll(quz_values);
        	
        	assertEquals(set.size(),5);
        	
        	set.retainAll(quz_values);
        	
        	assertEquals(set.size(),2);
        	
        	set.add("foo");
    		set.add("bar");
    		set.add("baz");
    		
    		set.removeAll(quz_values);
    		
    		assertEquals(set.size(),3);
    		
    		set.clear();
    		
    		assertEquals(set.size(),0);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		assert(false);
    	}
    }
    
    
    
    static public void verifySetImmutable(Set<String> set)
    {
    	assert(!set.isEmpty()); // these tests require list contains at least one element...
    	
    	try { set.add("foo"); assert(false); } catch(ImmutableException e) { }
    	try { set.remove("foo"); assert(false); } catch(ImmutableException e) { }
    	
    	ArrayList<String> quz_values = new ArrayList();
    	quz_values.add("quz");
    	quz_values.add("quuz");

    	
    	try { set.addAll(quz_values); assert(false); } catch(ImmutableException e) { }
    	try { set.removeAll(quz_values); assert(false); } catch(ImmutableException e) { }
    	try { set.retainAll(quz_values); assert(false); } catch(ImmutableException e) { }
    	
    	try { set.clear(); assert(false); } catch(ImmutableException e) { }
    	
 
    	try
    	{
	    	Iterator<String> itr = set.iterator();
	    	itr.next();
	
	    	itr.remove(); // Verify that items can not be removed using an iterator...
	    	assert(false);
    	}
    	catch(ImmutableException e) {}
    }
}

