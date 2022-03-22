package com.cs.core.eventqueue.impl;

import com.cs.core.rdbms.driver.RDBMSLogger;

import java.util.concurrent.*;

public class CustomExecutorService extends ThreadPoolExecutor {
  
  public CustomExecutorService(int maximumPoolSize)
  {
    super(maximumPoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());
  }
  
  public CustomExecutorService(int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue)
  {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  @Override
  protected void afterExecute(Runnable thread, Throwable exception)
  {
    if (exception == null && thread instanceof Future<?>) {
      try {
        ((Future<?>) thread).get();
      }
      catch (CancellationException ce) {
        exception = ce;
      }
      catch (ExecutionException ee) {
        exception = ee.getCause();
      }
      catch (InterruptedException ie) {
        Thread.currentThread()
            .interrupt();
      }
    }
    if (exception != null) {
      RDBMSLogger.instance().exception(exception);
    }
  }
  
}
