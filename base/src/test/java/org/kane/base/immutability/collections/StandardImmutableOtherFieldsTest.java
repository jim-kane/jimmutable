package org.kane.base.immutability.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.StandardObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableOtherFieldsTest extends TestCase
{
	@XStreamAlias("DummyObject-other-fields")
	static private class DummyObject extends StandardImmutableObject
	{
		private FieldConcurrentHashMap<String,Integer> concurrent_hash_map;
		private FieldConcurrentHashSet<String> concurrent_hash_set;
		private FieldConcurrentSkipListSet<String> concurrent_skip_list_set;
		private FieldCopyOnWriteArrayList copy_on_write_array_list;
		private FieldStringStringHashMap string_string_hashmap;
		
		public int compareTo(Object o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public void freeze() 
		{ 
			concurrent_hash_map.freeze(); 
			concurrent_hash_set.freeze(); 
			concurrent_skip_list_set.freeze(); 
			copy_on_write_array_list.freeze();
			string_string_hashmap.freeze();
		}
		
		public int hashCode() 
		{
			return Objects.hash(concurrent_hash_map, concurrent_hash_set, concurrent_skip_list_set, copy_on_write_array_list, string_string_hashmap);
		}
		
		public DummyObject()
		{
			concurrent_hash_map = new FieldConcurrentHashMap();
			concurrent_hash_set = new FieldConcurrentHashSet();
			concurrent_skip_list_set = new FieldConcurrentSkipListSet();
			copy_on_write_array_list = new FieldCopyOnWriteArrayList();
			string_string_hashmap = new FieldStringStringHashMap();
			
			complete();
		}
		
		public DummyObject(Builder b)
		{
			concurrent_hash_map = new FieldConcurrentHashMap();
			concurrent_hash_set = new FieldConcurrentHashSet();
			concurrent_skip_list_set = new FieldConcurrentSkipListSet();
			copy_on_write_array_list = new FieldCopyOnWriteArrayList();
			string_string_hashmap = new FieldStringStringHashMap();
		}
	
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof DummyObject) ) return false;
			
			DummyObject other = (DummyObject)obj;
			
			if ( !concurrent_hash_map.equals(other.concurrent_hash_map) ) return false;
			if ( !concurrent_hash_set.equals(other.concurrent_hash_set) ) return false;
			if ( !concurrent_skip_list_set.equals(other.concurrent_skip_list_set) ) return false;
			if ( !copy_on_write_array_list.equals(other.copy_on_write_array_list) ) return false;
			if ( !string_string_hashmap.equals(other.string_string_hashmap) ) return false;
			
			return true;
		}
		
		static public class Builder
		{
			private DummyObject obj;
			
			public Builder()
			{
				obj = new DummyObject(this);
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
    public StandardImmutableOtherFieldsTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StandardImmutableOtherFieldsTest.class );
    }
    
    public void testCopyConstructor()
    {
    	new DummyObject();
    }
    
    public void testBuidler()
    {
    	DummyObject.Builder builder = new DummyObject.Builder();
    	
    	builder.obj.concurrent_hash_map.put("foo", 17);
    	builder.obj.concurrent_hash_map.put("bar", 23);
    	builder.obj.concurrent_hash_map.put("baz", 31);
    	
    	builder.obj.concurrent_hash_set.add("washington");
    	builder.obj.concurrent_hash_set.add("jefferson");
    	builder.obj.concurrent_hash_set.add("lincoln");
    	
    	builder.obj.concurrent_skip_list_set.add("hydrogen");
    	builder.obj.concurrent_skip_list_set.add("helium");
    	builder.obj.concurrent_skip_list_set.add("argon");
    	
    	builder.obj.copy_on_write_array_list.add("Alabama");
    	builder.obj.copy_on_write_array_list.add("Alaska");
    	builder.obj.copy_on_write_array_list.add("Arkansas");
    	builder.obj.copy_on_write_array_list.add(27);
    	builder.obj.copy_on_write_array_list.add(new Long(99));
    	builder.obj.copy_on_write_array_list.add(new Long(101));
    	builder.obj.copy_on_write_array_list.add(28);
    	
    	builder.obj.string_string_hashmap.put("The Point Of No Return", "Michael Reed");
    	builder.obj.string_string_hashmap.put("Magical Lasso", "Charles Hart, Michael Reed");
    	
    	DummyObject obj = builder.create();
    	
    	// used to create the code for test serialization...
    	System.out.println(obj.toJavaCode("obj"));
    	
    	System.out.println();
    	
    	System.out.println(obj.toJavaCodeUsingJSON("obj"));
    	
    	System.out.println();
    	
    	System.out.println(obj.toJSON());
    }
    
    public void testSerialization()
    {
    	String obj_as_xml_string = String.format("%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s\r\n"
    		     , "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    		     , "<DummyObject-other-fields>"
    		     , "   <concurrent__hash__map>"
    		     , "      <contents class=\"concurrent-hash-map\">"
    		     , "         <entry>"
    		     , "            <string>bar</string>"
    		     , "            <int>23</int>"
    		     , "         </entry>"
    		     , "         <entry>"
    		     , "            <string>foo</string>"
    		     , "            <int>17</int>"
    		     , "         </entry>"
    		     , "         <entry>"
    		     , "            <string>baz</string>"
    		     , "            <int>31</int>"
    		     , "         </entry>"
    		     , "      </contents>"
    		     , "   </concurrent__hash__map>"
    		     , "   <concurrent__hash__set>"
    		     , "      <string>jefferson</string>"
    		     , "      <string>lincoln</string>"
    		     , "      <string>washington</string>"
    		     , "   </concurrent__hash__set>"
    		     , "   <concurrent__skip__list__set>"
    		     , "      <string>argon</string>"
    		     , "      <string>helium</string>"
    		     , "      <string>hydrogen</string>"
    		     , "   </concurrent__skip__list__set>"
    		     , "   <copy__on__write__array__list>"
    		     , "      <string>Alabama</string>"
    		     , "      <string>Alaska</string>"
    		     , "      <string>Arkansas</string>"
    		     , "      <int>27</int>"
    		     , "      <long>99</long>"
    		     , "   </copy__on__write__array__list>"
    		     , "   <string__string__hashmap>"
    		     , "      <entry>"
    		     , "         <key>The Point Of No Return</key>"
    		     , "         <value>Michael Reed</value>"
    		     , "      </entry>"
    		     , "      <entry>"
    		     , "         <key>Magical Lasso</key>"
    		     , "         <value>Charles Hart, Michael Reed</value>"
    		     , "      </entry>"
    		     , "   </string__string__hashmap>"
    		     , "</DummyObject-other-fields>"
    		);


    	DummyObject obj = (DummyObject)StandardObject.fromXML(obj_as_xml_string);
    	
    	assertEquals(5, obj.copy_on_write_array_list.size());
    	
    	assertEquals("Alabama", obj.copy_on_write_array_list.get(0));
    	assertEquals("Alaska", obj.copy_on_write_array_list.get(1));
    	assertEquals("Arkansas", obj.copy_on_write_array_list.get(2));
    	assertEquals(new Integer(27), obj.copy_on_write_array_list.get(3));
    	assertEquals(new Long(99), obj.copy_on_write_array_list.get(4));
    }
    
    public void testJSONSerialization()
    { 
    	String obj_as_json_string = "{\"DummyObject-other-fields\":{\"concurrent_hash_map\":[{\"contents\":[{\"@class\":\"concurrent-hash-map\",\"entry\":[{\"string\":\"bar\",\"int\":23},{\"string\":\"foo\",\"int\":17},{\"string\":\"baz\",\"int\":31}]}]}],\"concurrent_hash_set\":[{\"string\":[\"jefferson\",\"lincoln\",\"washington\"]}],\"concurrent_skip_list_set\":[{\"string\":[\"argon\",\"helium\",\"hydrogen\"]}],\"copy_on_write_array_list\":[{\"string\":[\"Alabama\",\"Alaska\",\"Arkansas\"],\"int\":27,\"long\":99}],\"string_string_hashmap\":[{\"entry\":[{\"key\":\"The Point Of No Return\",\"value\":\"Michael Reed\"},{\"key\":\"Magical Lasso\",\"value\":\"Charles Hart, Michael Reed\"}]}]}}";

    	DummyObject obj = (DummyObject)StandardObject.fromJSON(obj_as_json_string);

    	
    	assertEquals(5, obj.copy_on_write_array_list.size());
    	
    	assertEquals("Alabama", obj.copy_on_write_array_list.get(0));
    	assertEquals("Alaska", obj.copy_on_write_array_list.get(1));
    	assertEquals("Arkansas", obj.copy_on_write_array_list.get(2));
    	assertEquals(new Integer(27), obj.copy_on_write_array_list.get(3));
    	assertEquals(new Long(99), obj.copy_on_write_array_list.get(4));
    }
}
