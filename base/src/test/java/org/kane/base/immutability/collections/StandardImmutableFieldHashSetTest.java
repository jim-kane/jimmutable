package org.kane.base.immutability.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StandardImmutableFieldHashSetTest extends TestCase
{
	static private class SetTestObject extends StandardImmutableObject<SetTestObject>
	{
		static private final TypeName TYPE_NAME = new TypeName("jimmutable.test.SetTestObject"); public TypeName getTypeName() { return TYPE_NAME; }
		static private final FieldName FIELD_SET = new FieldName("set");
		
		static public Class field_class_to_test = FieldHashSet.class;
		
		private FieldSet<String> set;
		transient private Iterator old_iterator;
		
		public int compareTo(SetTestObject o) { return 0; }
		public void normalize() {}
		public void validate() {}
		public void freeze() { set.freeze(); }
		public int hashCode() { return set.hashCode(); }

		public FieldSet getSimpleSet() { return set; } 
		
		public SetTestObject()
		{
			set = createEmtpySet();
			old_iterator = set.iterator();
			
			verifyMutable();
			
			set.add("foo"); 
			
			complete();
			
			verifyImmutable();
			verifyOldIteratorImmutable();
		}
		
		public SetTestObject(Builder b)
		{
			set = new FieldHashSet();
		}
		
		public SetTestObject(ReadTree t)
		{
			set = t.getCollectionOfObjects(FIELD_SET, createEmtpySet(), ReadTree.OnError.SKIP);
		}
		
		public void write(ObjectWriter writer) 
		{
			writer.writeCollection(FIELD_SET, set, WriteAs.NATURAL_PRIMATIVES);
		}
		
		public FieldSet<String> createEmtpySet()
		{
			try
			{
				return (FieldSet<String>)field_class_to_test.newInstance();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				assert(false);
				return null;
			}
		}
	
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof SetTestObject) ) return false;
			
			SetTestObject other = (SetTestObject)obj;
			
			if ( !getSimpleSet().equals(other.getSimpleSet()) ) return false;
			
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
			private SetTestObject obj;
			
			public Builder()
			{
				obj = new SetTestObject(this);
			}
			
			public void add(String el)
			{
				obj.set.add(el);
			}
			
			public SetTestObject create()
			{
				return obj.deepClone();
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
    
    public void testCollections()
    {
    	ObjectReader.registerType(SetTestObject.TYPE_NAME, SetTestObject.class);
    	testCollection(FieldHashSet.class, true);
    }
    
    public void testCollection(Class c, boolean print_output)
    {
    	testImmutability(c);
    	testBuilder(c, print_output);
    }
    
    public void testImmutability(Class c)
    {
    	SetTestObject.field_class_to_test = c;
    	new SetTestObject();
    }
    
    public void testBuilder(Class c, boolean print_output)
    {
    	SetTestObject.field_class_to_test = c;
    	SetTestObject.Builder builder = new SetTestObject.Builder();
    	
    	builder.add("foo");
    	builder.add("bar");
    	builder.add("jimmutable");
    	
    	
    	
    	SetTestObject obj = builder.create();
    	obj.verifyImmutable();
    	
    	if ( print_output )
    	{
    		//System.out.println(obj.toJavaCode(Format.JSON_PRETTY_PRINT, "obj"));
    	}
    }
    
   /* public void testCopyConstructor()
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
    }*/
	
   
   
}


