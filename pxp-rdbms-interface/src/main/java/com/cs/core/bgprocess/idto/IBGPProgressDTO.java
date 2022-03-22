package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.util.List;

/**
 * A structure containing progress information
 *
 * @author vallee
 */
public interface IBGPProgressDTO extends ISimpleDTO {

  /**
   * Initialize the progress as a step-based process
   *
   * @param stepNames the step series names
   */
  public void initSteps(String... stepNames);

  /**
   * @return true if progress is based on percentage or false if based of number of accomplished steps
   */
  public boolean isPercentageBased();

  /**
   * @return the percentage of completion between 0 and 100 When the process is not based on completion, this is the (currentStepNo - 1) /
   * totalStepNb
   */
  public int getPercentageCompletion();

  /**
   * When the progress is percentage based, set the progress status in %
   *
   * @param percent overwritten completion between 0-100
   */
  public void setPercentageCompletion(int percent);

  /**
   * @return the current step number in the process When the process is not based on steps, this returns 1
   */
  public int getCurrentStepNo();

  /**
   * When the progress is step based, signal the progression to the next steps
   */
  public void incrStepNo();

  /**
   * @return the current step name in the process When the process is completed, it returns the mention "[COMPLETED]"
   */
  public String getCurrentStepName();

  /**
   * @return the total number of steps to be accomplished by the process When the process is not based on steps, this returns 1
   */
  public int getTotalStepNb();

  /**
   * @return the list of step names (which are depending on the service) When the process is not based on steps, this returns a list of one
   * element = the service name
   */
  public List<String> getStepNames();

  /**
   * @return the current internal batch number (which is an indication of progress)
   */
  public int getBatchNo();
}
