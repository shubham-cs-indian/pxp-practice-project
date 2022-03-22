package com.cs.core.bgprocess.services;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * BGP Service definition and job creation
 *
 * @author vallee
 */
public class BGProcessService {
  
  private final String serviceName;
  private final String classPath;
  private final int    nbThread;
  
  /**
   * @param characteristic
   *          queried in the properties file
   * @return the full property name
   */
  private String propName(String characteristic)
  {
    return String.format("bgp.%s.%s", serviceName, characteristic);
  }
  
  /**
   * Build up a service definition from a service symbol and associated
   * characteristics issued from the properties
   *
   * @param serviceSymbol
   * @throws CSInitializationException
   */
  public BGProcessService(String serviceSymbol) throws CSInitializationException
  {
    serviceName = serviceSymbol;
    classPath = CSProperties.instance().getString(propName("classPath"));
    if (classPath.isEmpty())
      throw new CSInitializationException( "Mandatory property classPath not found for service " + serviceSymbol);
    int nb = CSProperties.instance().getInt(propName("nbThread"));
    nbThread = (nb <= 0 ? 1 : nb); // Nb of threads is set to 1 by default
  }
  
  /**
   * @param nbRunningJobs number of jobs already running for this service
   * @return true when a new job can be started on this service
   */
  public boolean canStartNewJob(long nbRunningJobs)
  {         
    if ( nbRunningJobs >= nbThread ) {
      RDBMSLogger.instance().warn( "%d running jobs have reached nbThread limit (%d) for service %s", 
                  nbRunningJobs, nbThread, serviceName);
      return false;
    }
    return true; // the max number of task for this service has not been reached
  }
  
  /**
   * @return the service of this definition
   */
  public String getService()
  {
    return serviceName;
  }
  
  /**
   * @return the max of threads the service can handle at a time
   */
  public int getNbThread() {
    return nbThread;
  }
  
  /**
   * @param taskData task information pulled from the BGP table
   * @param defaultUserSession current user session that executes the task
   * @return a new task obtained from task data
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  public IBGProcessJob newJobFromBGProcessData(IBGProcessDTO taskData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException
  {
    IBGProcessJob newJob;
    try {
      Class jobClass = Class.forName(classPath);
      newJob = (IBGProcessJob) jobClass.newInstance();
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException exc) {
      RDBMSLogger.instance().exception(exc);
      throw new CSInitializationException("Unable to load job class ", exc);
    }
    
    try {
      newJob.initBeforeStart(taskData, defaultUserSession);
    } catch (CSFormatException | RDBMSException | CSInitializationException ex) {
      RDBMSLogger.instance().warn("Unable to start job  " + newJob.getService() + " / " + newJob.getIID() );
      RDBMSLogger.instance().exception(ex);
      newJob.updateAbortedProcessData(ex);
    } catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      newJob.updateAbortedProcessData(e);
    } catch (Throwable t) {
      RDBMSLogger.instance().exception(t);
      throw t;
    }
    return newJob;
  }
  
  /**
   * Poll the table and check if a new starting job is pending for that service
   *
   * @param defaultUserSession session information to be used by the service
   * @return a new task on the service or null if something went wrong
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IBGProcessJob pollNewStartingJob(IUserSessionDTO defaultUserSession)
      throws RDBMSException, CSInitializationException, CSFormatException
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    return (IBGProcessJob) transactionTemplate.execute(new TransactionCallback<IBGProcessJob>()
    {
      public IBGProcessJob doInTransaction(TransactionStatus status)
      {
        BGProcessDAS bgpDas = new BGProcessDAS();
        try {
          IBGProcessDTO newTaskData = bgpDas.getFirstPendingJob(serviceName);
          if (newTaskData != null) {
            return newJobFromBGProcessData(newTaskData, defaultUserSession);
          }
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
        }
        return null;
      }
    });
  }
}
