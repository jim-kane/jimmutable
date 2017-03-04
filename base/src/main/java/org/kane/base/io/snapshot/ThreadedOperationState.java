package org.kane.base.io.snapshot;

public enum ThreadedOperationState
{
	IN_PROGRESS,
	FINISHED_NO_ERRORS,
	FINISHED_WITH_ERRORS,
	FINISHED_STOPPED;
}
