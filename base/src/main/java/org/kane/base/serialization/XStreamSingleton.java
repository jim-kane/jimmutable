package org.kane.base.serialization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * A singleton used to standardized the creation of XStream objects and
 * guarantee the processing of annotations
 * 
 * You should never have to do very much with this class, with the notable
 * exception of calling addRootPackage once to have the singleton scan your
 * packages and process all annotations therein (via reflection). For example,
 * imagine you are building a large application under com.some.company..., you
 * simply add a call very early in your application (say, first line of main?)
 * XStreamSingleton.addRootPackage("com.some.company"). This wills scan an
 * process all annotations on classes in any package or sub-package of
 * "com.some.company".
 * 
 * @author jim.kane
 *
 */
public class XStreamSingleton 
{
	static private Set<String> root_packages = createDefaultRootPackages();
	
	static private XStream xml_stream = null;
	static private XStream json_stream = null;
	
	/**
	 * Scan and process the annotations contained in the specified package
	 * (inclusive of all sub-packages)
	 * 
	 * Calling this function multiple times for the same pacakge will have no
	 * effect and will return immediately.
	 * 
	 * @param package_name
	 *            The package (and sub package) to scan and process.
	 */
	static public void addRootPackage(String package_name)
	{
		if ( package_name == null ) throw new RuntimeException("package_name may not be null");
		
		if ( root_packages.contains(package_name) ) return;
		
		Set<String> new_root_packages = new HashSet(root_packages);
		new_root_packages.add(package_name);
		
		xml_stream = null;
		json_stream = null;
	}
	
	/**
	 * Create a new XStream object
	 * 
	 * @return A new XStream object, configured to read/write XML
	 */
	static synchronized public XStream getXMLStream()
	{
		if ( xml_stream != null ) return xml_stream;
		
		xml_stream = new XStream(new StaxDriver()); 
		processAnnotations(xml_stream);
		return xml_stream;
	}
	
	/**
	 * Create a new JSON XStream object
	 * 
	 * @return A new XStream object, configured to read/write JSON
	 */
	static synchronized public XStream getJSONStream()
	{
		if ( json_stream != null ) return json_stream;
	
		json_stream = new XStream(new JettisonMappedXmlDriver()); 
		processAnnotations(json_stream);
		return json_stream;
	}
	
	static private void processAnnotations(XStream xstream)
	{
		for ( String root_package : root_packages )
		{
			Reflections reflections = new Reflections(root_package);    
		    Set<Class<? extends StandardObject>> classes = reflections.getSubTypesOf(StandardObject.class);
				
		    for ( Class c : classes )
		    {
		    	xstream.processAnnotations(c);
		    }
		}
	}
	
	static private Set<String> createDefaultRootPackages()
	{
		Set<String> ret = new HashSet();
		
		ret.add("org.kane");
		
		return Collections.unmodifiableSet(ret);
	}
}
