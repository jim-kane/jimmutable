package org.kane.db_experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kane.base.serialization.StandardObject;
import org.kane.io.GZIPUtils;
import org.kane.io.SmallDocumentReader;
import org.kane.io.SmallDocumentWriter;
import org.kane.io.StandardObjectBulkLoader;

// How to build an uber-jar: mvn assembly:assembly -DdescriptorId=jar-with-dependencies
// Good command line switches: -XX:+UseG1GC -Xmx2G


public class IOBenchmark 
{
	private Options options;
	private File dest;
	
	static public void main(String args[])
	{
		if ( true )
		{
			TestObjectProductData.Builder builder = new TestObjectProductData.Builder();
			TestObjectProductData data = builder.createRandomProductData(3500);
			
			String xml = data.toXML();
			
			System.out.println(GZIPUtils.gzipBytes(xml.getBytes(),null).length);
			return;
		}
		
		
		new IOBenchmark(args);
	} 
	
	public IOBenchmark(String args[])
	{
		setupOptions();
		
		//args = new String[]{"--help"};
		//args = new String[]{"--file=c:\\test.dat", "--write=100,000"}; 
		//args = new String[]{"--file=c:\\test.dat", "--read"}; 
		
		CommandLineParser parser = new DefaultParser();
	    try 
	    {
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        
	        boolean had_operation = false;
	        
	        if ( line.hasOption("file") )
	        {
	        	String file_name = line.getOptionValue("file");
	        	dest = new File(file_name);
	        }
	        else
	        {
	        	// The default file
	        	dest = new File("test.dat"); // the default file...
	        }
	        
	        if ( line.hasOption("write") )
	        {
	        	String num_objects_str = line.getOptionValue("write");
	        	if ( num_objects_str == null ) num_objects_str = "100,000";
	        	
	        	int num_objects = 100_000;
	        	
	        	try
	        	{
	        		num_objects = NumberFormat.getNumberInstance(java.util.Locale.US).parse(num_objects_str).intValue();
	        	}
	        	catch(Exception e)
	        	{
	        		System.out.println(String.format("Unable to read the number: %s, defaulting to %,d objects", num_objects_str, num_objects));
	        	}
	        	
	        	write(num_objects);
	        	had_operation = true;
	        }
	        
	        if ( line.hasOption("read") )
	        {
	        	String num_threads_string = line.getOptionValue("read");
	        	if ( num_threads_string == null ) num_threads_string = "32";
	        	
	        	int num_threads = 32;
	        	
	        	try
	        	{
	        		num_threads = NumberFormat.getNumberInstance(java.util.Locale.US).parse(num_threads_string).intValue();
	        	}
	        	catch(Exception e)
	        	{
	        		System.out.println(String.format("Unable to read the number: %s, defaulting to %,d threads", num_threads_string, num_threads));
	        	}
	        	
	        	read(num_threads);
	        	had_operation = true;
	        }
	        
	        if ( !had_operation || line.hasOption("help") )
	        {
	        	onHelp();
	        }
	    }
	    catch( ParseException exp ) 
	    {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	        System.out.println();
	        onHelp();
	    }
	}
	
	private void setupOptions()
	{
		options = new Options();
		
		Option cur;
		
		{
			cur = new Option(null, "file", true, "the file to use for i/o purposes.  If not specified, test.dat is used");
			cur.setOptionalArg(false);
			cur.setArgName("FILENAME");
			options.addOption(cur);
		}
		
		{
			cur = new Option(null, "write", true, "the number of objects to create (in thousands).  If not specified, then 100,000 objects are created.");
			cur.setOptionalArg(true);
			
			options.addOption(cur);
		}
		
		{
			cur = new Option(null, "read", true, "Using the specified number of threads, load all objects from the file into RAM, measuring the time it takes to do so");
			cur.setOptionalArg(true);
			cur.setArgName("THREAD COUNT");
			options.addOption(cur);
		}
		
		{
			cur = new Option(null, "help", false, "Print out help content");
			cur.setOptionalArg(true);
			
			options.addOption(cur);
		}
	}
	
	private void onHelp()
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "io_bench", options );
	}
	
	private void write(int num_objects)
	{
		System.out.println(String.format("Writing %,d objects to %s", num_objects, dest.getAbsolutePath()));
		System.out.println();
		
		TestObjectProductData.Builder builder = new TestObjectProductData.Builder();

		long t1 = System.currentTimeMillis();

		List<TestObjectProductData> items = new ArrayList();

		for ( int i = 0; i < num_objects; i++ )
		{
			items.add(builder.createRandomProductData());

			if ( i % 1_000 == 0 )
				System.out.println(String.format("Building: %,d", i));
		}

		long t2 = System.currentTimeMillis();

		System.out.println();
		System.out.println(String.format("Build Time: %,d ms", (t2-t1)));
		System.out.println();


		try
		{
			//GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File("c:\\test.gz")));
			
			OutputStream raw = new FileOutputStream(dest);
			SmallDocumentWriter out = new SmallDocumentWriter(new OutputStreamWriter(raw));
	
			int write_count = 0;
	
			t1 = System.currentTimeMillis();
	
			for ( TestObjectProductData item : items )
			{
				write_count++;
	
				out.writeDocument(item.toXML());
	
				if ( write_count % 10_000 == 0 )
					System.out.println(String.format("Writing: %,d", write_count));
	
			}
	
			out.close();
	
			t2 = System.currentTimeMillis();
	
			System.out.println();
			System.out.println(String.format("Write Time: %,d ms", (t2-t1)));
			System.out.println(); 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void read(int thread_count)
	{
		System.out.println(String.format("Reading objects from %s using %,d threads", dest.getAbsolutePath(), thread_count));
		System.out.println();

		try
		{
			if ( !dest.exists() )
			{
				System.out.println("No such file: "+dest.getAbsolutePath());
				return;
			}
			
			Reader reader_raw = new BufferedReader(new InputStreamReader(new FileInputStream(dest), "UTF-8"));
			SmallDocumentReader reader = new SmallDocumentReader(reader_raw);
	
			MyListener listener = new MyListener();
			StandardObjectBulkLoader loader = new StandardObjectBulkLoader(listener,thread_count);
			loader.addSource(reader);
			loader.doneAddingSources();
	
			long t1 = System.currentTimeMillis();
			int count = 0;
	
	
			while(true)
			{
				count++;
	
				if ( count % 10 == 0 )
				{
					System.out.println(String.format("loading objects: %,d loaded in %,d ms", listener.data.size(), System.currentTimeMillis()-t1));
				}
	
				try { Thread.currentThread().sleep(100); } catch(Exception e) { e.printStackTrace(); }
				if ( listener.done ) break;
			}
	
			long t2 = System.currentTimeMillis();
	
	
			System.out.println();
			System.out.println(String.format("loading finished: %,d loaded in %,d ms", listener.data.size(), System.currentTimeMillis()-t1));
	
	
			System.out.println("Sleeping forever");
			sleepForever();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static private class MyListener implements StandardObjectBulkLoader.Listener
	{
		private boolean done = false;
		private Set<TestObjectProductData> data = Collections.newSetFromMap(new ConcurrentHashMap());
		
		public void onObjectLoaded(StandardObject obj)
		{
			data.add((TestObjectProductData)obj);
		}

		@Override
		public void onBulkLoaderFinished()
		{
			System.out.println("Done loading objects!");
			done = true;
		}
	}
	
	static public void sleepForever()
	{
		try
		{
			while(true) Thread.currentThread().sleep(1000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
