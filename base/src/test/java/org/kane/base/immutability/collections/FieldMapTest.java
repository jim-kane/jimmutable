package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.immutability.collections.FieldCollectionTest.TestObject;
import org.kane.base.immutability.collections.FieldCollectionTest.TestObject.Builder;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldMapTest extends TestCase
{
	static public class TestObject extends StandardImmutableObject<TestObject>
	{
		static public final TypeName TYPE_NAME = new TypeName("jimmutable.test.field_map.dummy_object"); public TypeName getTypeName() { return TYPE_NAME; }
		
		static private final FieldName FIELD_CLASS = new FieldName("field_class");
		static private final FieldName FIELD_MAP = new FieldName("field_map");
		
		private Class field_class;
		private FieldMap<String,Integer> map;
		
		public TestObject(Class field_class)
		{
			Validator.notNull(field_class);
			this.field_class = field_class;
			
			map = createEmptyMap();
			
			verifyMutable();
			
			map.put("foo", 1);
			map.put("bar", 2);
		
			complete();
			
			verifyImmutable();
		}
		
		public TestObject(Class field_class, Builder b)
		{
			Validator.notNull(field_class);
			this.field_class = field_class;
			
			map = createEmptyMap();
		}
		
		public TestObject(ObjectReader t)
		{
			try 
			{ 
				String class_name = t.getString(FIELD_CLASS, null);
				field_class = Class.forName(class_name);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
			}
			
			map = t.getMap(FIELD_MAP, createEmptyMap(), ReadAs.STRING, ReadAs.INTEGER, ObjectReader.OnError.SKIP);
		}
		
		public void write(ObjectWriter writer) 
		{
			writer.writeString(FIELD_CLASS, field_class.getName());
			writer.writeMap(FIELD_MAP, map, WriteAs.STRING, WriteAs.NUMBER);
		}
		
		private FieldMap<String,Integer> createEmptyMap()
		{
			try
			{
				return (FieldMap<String,Integer>)field_class.newInstance();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
				return null;
			}
		}
		
		public int compareTo(TestObject o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public void freeze() { map.freeze(); }
		public int hashCode() { return map.hashCode(); }
		public FieldMap<String,Integer> getSimpleMap() { return map; } 
		
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof TestObject) ) return false;
			
			TestObject other = (TestObject)obj;
			
			if ( !getSimpleMap().equals(other.getSimpleMap()) ) return false;
			
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
			private TestObject obj;
			
			public Builder(Class c)
			{
				obj = new TestObject(c, this);
			}
			
			public void put(String key, int value)
			{
				obj.map.put(key, value);
			}
			
			public TestObject create()
			{
				return obj.deepClone();
			}
		}
	}
  
    public FieldMapTest( String testName )
    {
        super( testName );
    }

   
    public static Test suite()
    {
        return new TestSuite( FieldMapTest.class );
    }
    
    
    public void testMaps()
    {
    	ObjectReader.registerTypeName(TestObject.class);
    	
    	testMap(FieldHashMap.class, true);
    	testMap(FieldConcurrentHashMap.class, true);
    }
    
    public void testMap(Class c, boolean print_output)
    {
    	testImmutability(c);
    	testBuilder(c, print_output);
    }
    
    public void testImmutability(Class c)
    {
    	new TestObject(c);
    }
    
    public void testBuilder(Class c, boolean print_output)
    {
    	TestObject.Builder builder = new TestObject.Builder(c);
    	
    	builder.put("foo",100);
    	builder.put("bar",200);
    	builder.put("jimmutable",300);
    	
    	TestObject obj = builder.create();
    	obj.verifyImmutable();
    	
    	if ( print_output )
    	{
    		//System.out.println(obj.toJavaCode(Format.JSON_PRETTY_PRINT, "obj"));
    		System.out.println(obj.serialize(Format.JSON_PRETTY_PRINT));
    	}
    	
    	assert(obj.map.containsKey("foo"));
    	assert(obj.map.containsKey("bar"));
    	assert(obj.map.containsKey("jimmutable"));

    	assertEquals(obj.map.size(),3);
    	
    	assertEquals(obj.map.get("foo"),new Integer(100));
    	assertEquals(obj.map.get("bar"),new Integer(200));
    	assertEquals(obj.map.get("jimmutable"),new Integer(300));
    	
    	assertEquals(obj.field_class.getName(),c.getName());
    }
}



