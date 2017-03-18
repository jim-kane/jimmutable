package org.kane.base.examples.product_data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.serialization.JimmutableTypeNameRegister;

public class OldSpecXMLConverter 
{
	private FieldList<ItemSpecifications> specs = new FieldArrayList();
	
	public OldSpecXMLConverter(File src) throws Exception
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLStreamReader reader = factory.createXMLStreamReader(new BufferedReader(new FileReader(src)));

		StringBuilder char_buffer = new StringBuilder();
		ItemSpecifications.Builder builder = new ItemSpecifications.Builder();
		
		String brand = null, pn = null;
		
		while(reader.hasNext())
		{
			int event = reader.next();
			
			switch(event)
			{
			case XMLStreamConstants.START_ELEMENT:
				
				if ( reader.getLocalName().equalsIgnoreCase("product_specs") )
				{
					builder = new ItemSpecifications.Builder();
					brand = null;
					pn = null;
				}

				
				char_buffer = new StringBuilder();
				break;
				
			case XMLStreamConstants.CHARACTERS:
				char_buffer.append(reader.getText().trim());
				break;
				
				
			case XMLStreamConstants.END_ELEMENT:
				switch(reader.getLocalName().toLowerCase())
				{
				case "product_specs":
					try { specs.add(builder.create()); } catch(Exception e) {}
					
					if ( specs.size() % 1_000 == 0 ) 
						System.out.println(String.format("%,d",specs.size()));
					
					break;
				case "product_data": 
					break;
				
				case "brand":
					brand = char_buffer.toString();
					
					if ( brand != null && pn != null ) 
					{
						try { builder.setItemKey(new ItemKey(brand,pn)); } catch(Exception e) {}
					}
					
					break;
				
				case "pn":
					pn = char_buffer.toString();
					
					if ( brand != null && pn != null ) 
					{
						try { builder.setItemKey(new ItemKey(brand,pn)); } catch(Exception e) {}
					}
					
					break;
					
				default:
					builder.putAttribute(new ItemAttribute(reader.getLocalName()), char_buffer.toString());
				}
			}
		}
	}
	
	static public void main(String args[]) throws Exception
	{
		JimmutableTypeNameRegister.registerAllTypes();
		new OldSpecXMLConverter(new File("c:\\spec_data_small.xml"));
	}
}
