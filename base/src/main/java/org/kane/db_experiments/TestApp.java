package org.kane.db_experiments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kane.base.serialization.ValidationException;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.AbstractXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class TestApp
{
	static private long start_time;
	
	static private List<TestObjectProductData> items = new ArrayList();
	static private TestObjectProductData.Builder item_builder = new TestObjectProductData.Builder();
	
	static public void main(String args[])
	{
		AbstractXmlDriver driver = new StaxDriver();
		HierarchicalStreamReader reader = driver.createReader(new File("c:\\spec_data.xml"));
		
		start_time = System.currentTimeMillis();
		
		// read each product_specs tag
		while(reader.hasMoreChildren())
		{
			reader.moveDown();
			
			if ( reader.getNodeName().equalsIgnoreCase("product_specs") )
			{
				readProductSpecs(reader);
			}
			else
			{
			}
			
			reader.moveUp();

			
			if ( items.size() % 10_000 == 0 )
			{
				printStatus();
			}
		}
	}
	
	static private void printStatus()
	{
		System.out.println(String.format("Reading items: %,d read in %,d ms, %,d MB RAM used"
				, items.size()
				, System.currentTimeMillis()-start_time
				, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024));
	}
	
	static public void readProductSpecs(HierarchicalStreamReader reader)
	{ 
		item_builder.setBrandCode(reader.getAttribute("brand"));
		item_builder.setPN(reader.getAttribute("pn"));
		
		while(reader.hasMoreChildren())
		{
			reader.moveDown();
			
			if ( reader.getNodeName().equalsIgnoreCase("BRAND") )
			{
				// skip, read as an attribute (above)
			}
			if ( reader.getNodeName().equalsIgnoreCase("PN") )
			{
				// skip, read as an attribute (above)
			}
			else
			{
				item_builder.putKVData(reader.getNodeName(), reader.getValue());
			}
			
			reader.moveUp();
		}
		
		try
		{
			items.add(item_builder.create());
		}
		catch(ValidationException e)
		{
			e.printStackTrace();
		}
	}
}
