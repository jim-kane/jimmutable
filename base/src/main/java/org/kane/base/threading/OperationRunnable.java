package org.kane.base.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kane.base.serialization.Optional;

abstract public class OperationRunnable implements Runnable
{
	volatile private Result result = null;
	volatile State state = State.AWAITING_START;
	volatile private long start_time = -1;

	public boolean hasResult() { return result != null; }
	public Result getOptionalResult(Result default_value) { return result != null ? result : default_value; }
	
	public boolean hasStartTime() { return Optional.has(start_time, -1); }
	public long getOptionalStartTime(long default_value) { return Optional.getOptional(start_time, -1, default_value); }
	
	public State getSimpleState() { return state; }
	
	public boolean isInState(State state_to_check) 
	{
		if ( state_to_check == null ) return false;
		return state == state_to_check;
	}
	
	public void stop() 
	{ 
		if ( isInState(State.STOPPING) ) return; // nothing to do
		if ( isInState(State.FINISHED) ) return; // nothing to do, already stopped (finished)
		
		state = State.STOPPING;
	}
	
	public boolean shouldStop()
	{
		if ( isInState(State.STOPPING) ) return true;
		if ( isInState(State.FINISHED) ) return true;
		
		return false;
	}
	
	final public void run()
	{
		start_time = System.currentTimeMillis();
		try
		{
			if ( shouldStop() )
			{
				result = Result.STOPPED;
				return;
			}
			
			result = performOperation();
			return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result = Result.ERROR;
			return;
		}
		finally
		{
			state = State.FINISHED;
		}
	}
	
	abstract protected Result performOperation() throws Exception;
	
	static public enum Result
	{
		SUCCESS,
		ERROR,
		STOPPED;
	}
	
	static public enum State
	{
		AWAITING_START,
		RUNNING,
		STOPPING,
		FINISHED;
	}
	
	static public Result execute(OperationRunnable runnable, Result default_value)
	{
		ExecutorService single_thread = Executors.newSingleThreadExecutor();
		Future f = single_thread.submit(runnable);
		
		try 
		{ 
			f.get(); 
			single_thread.shutdown();
		} 
		catch(Exception e) 
		{ 
			e.printStackTrace(); 
			return default_value; 
		}
		
		return runnable.getOptionalResult(default_value);
	}
}
