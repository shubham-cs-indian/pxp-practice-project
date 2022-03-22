package com.cs.core.bgprocess.services;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Task definition in the BGProcess execution framework
 *
 * @author vallee
 */
public interface IBGProcessJob extends Callable<BGPStatus>, Comparable {
    
  /**
   * A maximum number of batches is provided to prevent any infinite loop while
   * running the job
   */
  public static final int NB_MAX_BATCH = 3000; // 3000 x 30 sec. = 25 hours = the maximum for a BGP to be executed
  /**
   * A maximum duration in seconds for batches - above this limit a warning message is generated in logs
   */
  public static final int MAX_BATCH_DURATION = 90;
  
  /**
   * When the task been submitted, the corresponding scheduler handler is registered
   *
   * @param taskHandler the handler returned by the Executor
   */
  public void registerSchedulerHandler(Future<BGPStatus> taskHandler);
  
  /**
   * @return true when the task has been submitted (meaning it contains a scheduler handler)
   */
  public boolean isSubmitted();
  
  /**
   * Wait task termination and return last execution result
   *
   * @return the status returned by last execution
   */
  public BGPStatus getResult();
  
  /**
   * @return the BGP service realized by the job
   */
  String getService();
  
  /**
   * @return the unique job identified
   */
  public long getIID();
  
  /**
   * @return the current status of the job
   */
  public BGPStatus getStatus();
  
  /**
   * @return true if ended with success
   */
  public default boolean isEndedSuccesfully()
  {
    return getStatus() == BGPStatus.ENDED_SUCCESS;
  }
  
  /**
   * @return true if failed by error or exception
   */
  public default boolean isFailed()
  {
    return getStatus().isFailed();
  }
  
  /**
   * @return true if ended by any way
   */
  public default boolean isTerminated()
  {
    return getStatus().isEnded();
  }
  
  /**
   * @return true if waiting to be started
   */
  public default boolean isWaiting()
  {
    return getStatus().isWaiting();
  }
  
  /**
   * @return true if paused to be restarted from an interruption
   */
  public default boolean isPaused()
  {
    return getStatus() == BGPStatus.PAUSED;
  }
  
  /**
   * @return true if running
   */
  public default boolean isStarted()
  {
    return getStatus() == BGPStatus.RUNNING;
  }
  
  /**
   * @return true if running and submitted
   */
  public default boolean isRunning()
  {
    return getStatus() == BGPStatus.RUNNING && isSubmitted();
  }
  
  /**
   * @return true if the task is running and ready to start running a batch
   */
  public boolean isReadyToNewBatch();
  
  /**
   * Update the process progression in database
   *
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void updateProcessData() throws RDBMSException, CSFormatException;
  
  /**
   * Update the process progression in database in case of forced shutdown
   */
  public void updateInterruptedProcessData();
  
  /**
   * Update the process progression in database in case of exception during initialization
   * @param ex the exception caught during batch initialization
   */
  public void updateAbortedProcessData( Exception ex);
  
  /**
   * Stop the job and assign it a new status
   *
   * @param bgpStatus
   */
  public void stopWithStatus(BGPStatus bgpStatus);
  
  /**
   * Transmit information to the BGP task for first execution or resume
   *
   * @param taskData the pending task data retrieved from the BGP table
   * @param defaultUserSession the default user session (if not defined in BGP task)
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  void initBeforeStart(IBGProcessDTO taskData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException;
  
  /**
   * Execute a uninterrupted batch
   *
   * @return the status of the task after the batch: RUNNING, FAILED or ENDED are expected as result
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws PluginException 
   * @throws SQLException 
   */
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception;
}
