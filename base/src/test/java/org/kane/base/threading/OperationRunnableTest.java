package org.kane.base.threading;

import java.util.ArrayList;
import java.util.Collection;

import org.kane.base.threading.OperationRunnable.Result;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OperationRunnableTest extends TestCase
{
   
    public OperationRunnableTest( String testName )
    {
        super( testName );
    }


    public static Test suite()
    {
        return new TestSuite( OperationRunnableTest.class );
    }

   
    public void testSingleThreadExecution()
    {
    	SampleOperation success_op = new SampleOperation(100,true);
    	SampleOperation error_op = new SampleOperation(100,false);
    	
    	Result success_op_result = OperationRunnable.execute(success_op, null);
    	Result fail_op_result = OperationRunnable.execute(error_op, null); 
    	
    	assertEquals(success_op_result, Result.SUCCESS);
    	assertEquals(fail_op_result, Result.ERROR);
    }
    
    public void testMultiThreadSuccess()
    {
    	Collection<OperationRunnable> seed = new ArrayList();
    	
    	
    	seed.add(new SampleOperation(100,true));
    	seed.add(new SampleOperation(200,true));
    	seed.add(new SampleOperation(300,true));
    	seed.add(new SampleOperation(100,true));
    	
    	OperationPool pool = new OperationPool(seed, 2);
    	
    	Result result = OperationRunnable.execute(pool, null);
    	
    	assertEquals(result, Result.SUCCESS);
    }
    
    public void testMultiThreadError()
    {
    	Collection<OperationRunnable> seed = new ArrayList();
    	
    	
    	seed.add(new SampleOperation(100,true));
    	seed.add(new SampleOperation(200,true));
    	seed.add(new SampleOperation(300,false));
    	seed.add(new SampleOperation(100,true));
    	
    	OperationPool pool = new OperationPool(seed, 2);
    	
    	Result result = OperationRunnable.execute(pool, null);
    	
    	assertEquals(result, Result.ERROR);
    }
    
    
    public void testMultiThreadStopped()
    {
    	Collection<OperationRunnable> seed = new ArrayList();
    	
    	SampleOperation to_be_stopped = new SampleOperation(300,true);
    	
    	seed.add(new SampleStopOperation(50,to_be_stopped));
    	seed.add(new SampleOperation(100,true));
    	seed.add(new SampleOperation(200,true));
    	seed.add(new SampleOperation(300,true));
    	seed.add(to_be_stopped);
    	
    	OperationPool pool = new OperationPool(seed, 2);
    	
    	Result result = OperationRunnable.execute(pool, null);
    	
    	assertEquals(result, Result.STOPPED);
    }
}