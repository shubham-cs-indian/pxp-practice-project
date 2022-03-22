package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * Job summary data
 *
 * @author vallee
 */
public interface IBGPSummaryDTO extends ISimpleDTO {

  /**
   * @return the number of errors
   */
  public int getNBOfErrors();

  /**
   * @return the number of warnings
   */
  public int getNbOfWarnings();

  /**
   * @return the total number of lines in current job log
   */
  public int getNbOfLogLines();

  /**
   * @return the exception message or empty whenever the job failed on exception
   */
  public String getExceptionMessage();
  
  /**
   * @return the total numnber of entities count.
   */
  public int getTotalCount();
  
  public void setTotalCount(int totalCount);
  
  /**
   * @return the total number of success entities count.
   */
  public int getSuccessCount();
  
  public void setSuccessCount(int successCount);
}
