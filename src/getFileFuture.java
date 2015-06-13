package fr.haddad.dfs.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Future represents the result of an asynchronous computation. Methods are
 * provided to check if the computation is complete, to wait for its completion,
 * and to retrieve the result of the computation. The result can only be
 * retrieved using method get when the computation has completed, blocking if
 * necessary until it is ready. Cancellation is performed by the cancel method.
 * Additional methods are provided to determine if the task completed normally
 * or was cancelled. Once a computation has completed, the computation cannot be
 * cancelled.
 * 
 * 
 * GettingFileStatus is the result type returned by this Future's get method.
 * 
 * Attempts to cancel execution of this task. This attempt will fail if the task
 * has already completed, has already been cancelled, or could not be cancelled
 * for some other reason. If successful, and this task has not started when
 * cancel is called, this task should never run. If the task has already
 * started, then the mayInterruptIfRunning parameter determines whether the
 * thread executing this task should be interrupted in an attempt to stop the
 * task.
 * 
 * After this method returns, subsequent calls to isDone() will always return
 * true. Subsequent calls to isCancelled() will always return true if this
 * method returned true.
 */

public  class getFileFuture implements Future<GettingFileStatus>,Runnable {
	/**
	 * 
	 * Enumeration that contains the threes states of our dfs copy file methode
	 *
	 */
	private static Callable<GettingFileStatus > callable;
	private static GettingFileStatus fileStatus;
	private static enum State {
		WAITING, DONE, CANCELLED
	}

	/**
	 * default value of state
	 */
	private volatile State state = State.WAITING;

	/**
	 * constructor
	 * @throws Exception 
	 */
	public getFileFuture(Callable<GettingFileStatus> callable,GettingFileStatus fileStatus) throws Exception {
this.callable=callable;
this.fileStatus=fileStatus;
	}

	/**
	 * @param mayInterruptIfRunning
	 *            mayInterruptIfRunning true if the thread executing this task
	 *            should be interrupted; otherwise, in-progress tasks are
	 *            allowed to complete
	 * @return: false if the task could not be cancelled, typically because it
	 *          has already completed normally; true otherwise
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		try {
			System.out.println("Cancel****************************");
			state = State.CANCELLED;
			cleanUp();
			return true;
		} catch (Exception e) {
			// throw Throwables.propagate(e);
		}
		return mayInterruptIfRunning;
	}

	private void cleanUp() {

	}

	/**
	 * isCancelled() true if this task was cancelled before it completed
	 * normally.
	 * 
	 * @return true if this task was cancelled before it completed
	 */
	@Override
	public boolean isCancelled() {
		System.out.println("isCanceled****************************");
		return state == State.CANCELLED;
	}

	/**
	 * isDone() true if this task completed. Completion may be due to normal
	 * termination, an exception, or cancellation -- in all of these cases, this
	 * method will return true.
	 * 
	 * @return: true if this task completed
	 */
	@Override
	public boolean isDone() {
		System.out.println("isDone****************************");
		return state == State.DONE;
	}

	/**
	 * get() Waits if necessary for the computation to complete, and then
	 * retrieves its result.
	 * 
	 * Returns: the computed result Throws:
	 * 
	 * @throws CancellationException
	 *             if the computation was cancelled
	 * @throws ExecutionException
	 *             if the computation threw an exception java.lang.
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 */
	@Override
	public GettingFileStatus get() throws InterruptedException,
			ExecutionException {
		return fileStatus;
	}

	/**
	 * get(long timeout, TimeUnit unit) Waits if necessary for at most the given
	 * time for the computation to complete, and then retrieves its result, if
	 * available.
	 * 
	 * @param: timeout the maximum time to wait unit the time unit of the
	 *         timeout argument
	 * @return: the computed result Throws:
	 * @throws CancellationException
	 *             if the computation was cancelled
	 * 
	 * @throws ExecutionException
	 *             if the computation threw an exception java.lang. @throws
	 *             InterruptedException if the current thread was interrupted
	 *             while waiting
	 * @throws TimeoutException
	 *             if the wait timed out
	 */
	@Override
	public GettingFileStatus get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return fileStatus;
	}

	@Override
	public void run() {

		try {
			callable.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}