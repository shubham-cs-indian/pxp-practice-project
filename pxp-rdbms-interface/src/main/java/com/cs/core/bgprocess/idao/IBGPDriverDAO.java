package com.cs.core.bgprocess.idao;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.dataintegration.idto.IPXONExportPlanDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.io.File;
import java.util.List;

/**
 * PXP Background process access
 *
 * @author vallee
 */
public interface IBGPDriverDAO {

  /**
   * Get a cursor on existing background processes with filtering and ordering capabilities
   *
   * @return a cursor of BGP information
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IBGPCursor getBGProcesses() throws RDBMSException;

  /**
   * @param service the required BGP service name to be prepared
   * @return an empty JSON structure to be filled for running a job corresponding to the required service
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IJSONContent getServiceEntryTemplate(String service) throws RDBMSException;

  /**
   * Submit a new BGP Process for execution
   *
   * @param user the user name who is submitting the job
   * @param service the BGP service name to be run
   * @param callbackURL the URL template to be called back on job completion (or null for polling the task)
   * @param userPriority the user defined priority for the job
   * @param entryData the JSON entry data which depends on the service to be accomplished
   * @return the corresponding job IID
   * @throws RDBMSException
   */
  public long submitBGPProcess(String user, String service, String callbackURL,
          BGPPriority userPriority, IJSONContent entryData) throws RDBMSException;

  /**
   * Get process information from a job IID
   *
   * @param jobIID the ID of the background process job
   * @return the JOB process data
   * @throws RDBMSException
   */
  IBGProcessDTO getBGPProcess(long jobIID) throws RDBMSException;

  /**
   * Get the logs of a BGP job - the corresponding log is placed in a file of the download area
   *
   * @param jobIID the ID of the background process job
   * @return BGP information containing detailed reports of every steps
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public File loadJobLogFile(long jobIID) throws RDBMSException;

  /**
   * Prepare an export plan by compiling scope information
   *
   * @param exportScope the entry scope information
   * @return the export plan that is used as entry of export services
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IPXONExportPlanDTO prepareExport(IPXONExportScopeDTO exportScope) throws RDBMSException;

  /**
   *
   * @return all services present into property file
   * @throws RDBMSException
   */
  public List<String> getServices() throws RDBMSException;
}
