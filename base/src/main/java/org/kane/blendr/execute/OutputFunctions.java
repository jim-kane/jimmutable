package org.kane.blendr.execute;

/**
 * This class is used by Executor (made available as the out object in
 * JavaScript). It provides various functions that blendr programs can use to
 * make well formatted output.
 * 
 * @author jim.kane
 *
 */
public class OutputFunctions 
{
	private Executor executor;
	
	/**
	 * Create a new output object
	 * 
	 * @param executor The executor that this output object is bound to
	 */
	public OutputFunctions(Executor executor)
	{
		this.executor = executor;
	}
	
	/**
	 * A nice print function (no newline at the end)
	 * 
	 * @param objects
	 *            The objects to print
	 */
	public void print(Object ...objects)
	{
		int count = 0;
		
		for ( Object obj : objects )
		{
			try 
			{ 
				emit(obj.toString());
				
				if ( count != 0 ) emit(" ");
			}
			catch(Exception e) 
			{ 
				e.printStackTrace(); 
			}
			
			count++;
		}
	}
	
	
	/**
	 * A nice println function (print + carrage return at the end)
	 * 
	 * @param objects
	 *            The objects to print
	 */
	public void println(Object ...objects)
	{
		print(objects);
		println();
	}
	
	/**
	 * Print an integer (in case 10)
	 * 
	 * @param value The value to print
	 */
	public void printInt(int value)
	{
		printInt("%d",value);
	}
	
	/**
	 * Print an integer using a give format code (see String.format). One
	 * particularly useful format code is %,d (integer with comma separator for
	 * thousands)
	 * 
	 * @param format_code The format code that will be used to format the integer
	 * 
	 * @param value The value to print
	 */
	public void printInt(String format_code, int value)
	{
		emit(String.format(format_code, value));
	}
	
	/**
	 * Print a carrage return (\r\n)
	 */
	public void println()
	{
		emit("\r\n");
	}
	
	/**
	 * Utility function to emit a string to the engine's current output
	 * 
	 * @param str
	 *            The string to output. Null strings result in nothign being
	 *            output.
	 */
	private void emit(String str)
	{
		if ( str == null ) return;
		try 
		{ 
			executor.getSimpleScriptEngine().getContext().getWriter().append(str); 
		}
		catch(Exception e) 
		{ 
			e.printStackTrace(); 
		}
	}
}
