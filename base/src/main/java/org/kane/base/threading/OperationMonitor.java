package org.kane.base.threading;

public interface OperationMonitor
{
	public void onOperationMonitorHeartbeat(OperationRunnable runnable);
}
