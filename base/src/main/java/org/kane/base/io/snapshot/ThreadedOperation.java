package org.kane.base.io.snapshot;

public interface ThreadedOperation
{
	public ThreadedOperationState getSimpleState();
	public void ifInProgressStopOperation();
}
