package org.kane.base.io.snapshot;

abstract public class OperationRunnable implements Runnable
{
	volatile private Result result = null;
	volatile State state = State.AWAITING_START;

	public boolean hasResult() { return result != null; }
	public Result getOptionalResult(Result default_value) { return result != null ? result : default_value; }
	
	public State getSimpleState() { return state; }
	
	public boolean isInState(State state) 
	{
		if ( state == null ) return false;
		return state == state;
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
}
