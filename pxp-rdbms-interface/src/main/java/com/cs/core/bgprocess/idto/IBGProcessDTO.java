package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.IUserDTO;

/**
 * DTO for PXP background process information
 *
 * @author vallee
 */
public interface IBGProcessDTO extends IRootDTO, Comparable {

  // Place holders used in callback template URL
  public static final String JOB_PLACEHOLDER = "\\$\\{job\\}";
  public static final String STATUS_PLACEHOLDER = "\\$\\{status\\}";

  /**
   * @return the JOB ID
   */
  public long getJobIID();

  /**
   * @return the service name run by this BGP
   */
  public String getService();

  /**
   * @return the user who has submitted the job
   */
  public IUserDTO getUser();

  /**
   * @return the current status of the process and FAILED if one of the process step is failed
   */
  public BGPStatus getStatus();

  /**
   * @return the user priority level for this job
   */
  public BGPPriority getUserPriority();

  /**
   * Entry information is unstructured JSON which depends on the service run by the job
   *
   * @return the entry parameters defined when the job has been created
   */
  public IJSONContent getEntryData();

  /**
   * @return progress data information of this task
   */
  public IBGPProgressDTO getProgress();

  /**
   * @return Job summary data
   */
  public IBGPSummaryDTO getSummary();

  /**
   * @return running time
   */
  public long getRunningTime();

  /**
   * @return created time
   */
  public long getCreatedTime();

  /**
   * @return start time
   */
  public long getStartTime();

  /**
   * @return end time
   */
  public long getEndTime();

  /**
   * @return the template URL for callback
   */
  public String getCallbackURLTemplate();

  /**
   * @return get success ids
   */
  public List<String> getSuccessIds();

  /**
   * Set success ids
   * @param successIds
   */
  public void setSuccessIds(List<String> successIds);

  /**
   * @return get failed ids
   */
  public List<String> getFailedIds();

  /**
   * Set failed ids
   * @param failedIds
   */
  public void setFailedIds(List<String> failedIds);
  
  /**
   * Process status definition, the lifecycle of a task is the following: - PENDING before being started - RUNNING after start - PAUSED on
   * interruption by user - INTERRUPTED on interruption by server shutdown - ENDED_SUCCESS when the task ended successfully - ENDED_ERRORS
   * when the task ended with error - ENDED_EXCEPTION when the task crashed on exception - CANCELED when canceled by user
   */
  public enum BGPStatus {

    UNDEFINED, PENDING, RUNNING, ENDED_SUCCESS, ENDED_ERRORS, ENDED_EXCEPTION, PAUSED, INTERRUPTED,
    CANCELED;

    private static final BGPStatus[] values = values();

    public static BGPStatus valueOf(int ordinal) {
      return values[ordinal];
    }

    public boolean isEnded() {
      return this == ENDED_SUCCESS || this == ENDED_ERRORS || this == ENDED_EXCEPTION
              || this == CANCELED;
    }

    public boolean isFailed() {
      return this == ENDED_ERRORS || this == ENDED_EXCEPTION;
    }

    public boolean isWaiting() {
      return this == INTERRUPTED || this == PENDING;
    }

    public boolean isStopped() {
      return this == PAUSED || this == INTERRUPTED || this == CANCELED;
    }
  }

  /**
   * Level of priority defined by user
   */
  public enum BGPPriority {

    UNDEFINED, LOW, MEDIUM, HIGH;

    private static final BGPPriority[] values = values();

    public static BGPPriority valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
