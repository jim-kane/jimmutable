package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardImmutableObject;
import org.kane.base.serialization.StandardObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableFieldHashMapTest extends TestCase
{
	@XStreamAlias("DummyObject-map")
	static private class DummyObject extends StandardImmutableObject
	{
		private StandardImmutableFieldHashMap<String,Integer> map;
		
		public int compareTo(Object o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public int hashCode() { return map.hashCode(); }

		
		public DummyObject()
		{
			map = new StandardImmutableFieldHashMap(this);
			
			verifyMutable();
			
			map.put("foo", 1);
			
			complete();
			
			verifyImmutable();
		}
		
		public DummyObject(Builder b)
		{
			map = new StandardImmutableFieldHashMap(this);
		}
	
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof DummyObject) ) return false;
			
			DummyObject other = (DummyObject)obj;
			
			if ( !map.equals(other.map) ) return false;
			
			return true;
		}
		
		public void verifyMutable()
		{
			try
			{
				map.put("foo", 1);
				map.put("bar", 2);
				map.put("baz", 3);
				map.put("baz", 4);
				
				assertEquals(map.size(),3);
				
				assert(map.get("foo") == 1);
				assert(map.get("bar") == 2);
				assert(map.get("baz") == 4);
				assert(map.get("quz") == null);
				
				map.remove("baz");
				
				assert(map.get("baz") == null);
				
				Map<String,Integer> test_map = new HashMap();
				test_map.put("quz",100);
				test_map.put("quuz",101);
				
				map.putAll(test_map);
				
				assert(map.get("quz") == 100);
				assert(map.get("quuz") == 101);
				
				Set<String> key_set = map.keySet();
				
				assertEquals(key_set.size(),4);
				assert(key_set.contains("quz"));
				
				Collection<Integer> values = map.values();
				
				assertEquals(values.size(),4);
				assert(values.contains(1));
				assert(values.contains(2));
				assert(values.contains(100));
				assert(values.contains(101)); 
				
				Set<Map.Entry<String, Integer>> entry_set = map.entrySet();
				
				assertEquals(entry_set.size(),4);
				
				Iterator itr = entry_set.iterator();
				itr.next();
				itr.remove();
				
				assertEquals(map.size(),3);
				
				map.clear();
				
				assertEquals(map.size(),0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
			}
		}

		public void verifyImmutable()
		{
			try { map.put("foo", 1); assert(false); } catch(ImmutableException e) {}
			try { map.remove("foo"); assert(false); } catch(ImmutableException e) {}
			try { map.clear(); assert(false); } catch(ImmutableException e) {}
			
			
			Set<Map.Entry<String, Integer>> entry_set = map.entrySet();
			
			assert(entry_set.size() != 0);
			
			try  
			{
				Iterator itr = entry_set.iterator();
				itr.next();
				itr.remove();
				
				assert(false);
			}
			catch(ImmutableException e) {}
		}
		
		static public class Builder
		{
			private DummyObject obj;
			
			public Builder()
			{
				obj = new DummyObject(this);
			}
			
			public void put(String el, int value)
			{
				obj.assertNotComplete();
				obj.map.put(el, value);
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
    public StandardImmutableFieldHashMapTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableFieldHashMapTest.class );
    }
    
    public void testCopyConstructor()
    {
    	new DummyObject();
    }
    
    public void testBuidler()
    {
    	DummyObject.Builder builder = new DummyObject.Builder();
    	
    	builder.obj.verifyMutable();
    	
    	builder.put("foo", 1);
    	
    	DummyObject obj = builder.create();
    	
    	obj.verifyImmutable();
    	
    	// used to create the code for test serialization...
    	// System.out.println(JavaCodeUtils.toJavaStringLiteral(obj));
    	
    	System.out.println(obj.toJSON());
    }
    
    
    public void testSerialization()
    {
    	String xml = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<DummyObject-map>"
    		     , "   <map>"
    		     , "      <contents>"
    		     , "         <entry>"
    		     , "            <string>foo</string>"
    		     , "            <int>1</int>"
    		     , "         </entry>"
    		     , "      </contents>"
    		     , "   </map>"
    		     , "</DummyObject-map>"
    		);
    	
    	DummyObject obj = (DummyObject)StandardObject.fromXML(xml);
    	
    	assertEquals(obj.map.size(),1);
    	assert(obj.map.containsKey("foo"));
    	assert(obj.map.get("foo") == 1);
    	
    	obj.verifyImmutable();
    }

   
   
}



