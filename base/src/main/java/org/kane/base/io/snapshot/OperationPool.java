package org.kane.base.io.snapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OperationPool extends OperationRunnable
{
	private List<OperationRunnable> seed_operations = new ArrayList();
	private int thread_count;
	
	private Set<OperationRunnable> all_tasks = Collections.newSetFromMap(new ConcurrentHashMap());
	private ExecutorService thread_pool;
	
	
	public OperationPool(OperationRunnable seed_operation, int thread_count)
	{
		this.seed_operations.add(seed_operation);
		this.thread_count = thread_count;
	}
	
	public OperationPool(Collection<OperationRunnable> seed_operations, int thread_count)
	{
		this.seed_operations.addAll(seed_operations);
		this.thread_count = thread_count;
	}
	
	protected Result performOperation() throws Exception
	{
		if ( shouldStop() ) return Result.STOPPED;
		
		thread_pool = Executors.newFixedThreadPool(thread_count);
		
		for ( OperationRunnable operation : seed_operations )
		{
			submitOperation(operation);
		}
		
		while(true)
		{
			if ( shouldStop() )
				break;
			
			
			
			try { Thread.currentThread().sleep(500); } catch(Exception e) { e.printStackTrace(); }
		}
	}
	
	public void submitOperation(OperationRunnable operation)
	{
		if ( shouldStop() ) return; // don't accept any new tasks if this (parent) task shoudl stop...
		
		
		all_tasks.add(operation);
		thread_pool.submit(operation);
	}
	
	
}
