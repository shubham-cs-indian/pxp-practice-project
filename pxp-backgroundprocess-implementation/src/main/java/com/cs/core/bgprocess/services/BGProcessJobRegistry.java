package com.cs.core.bgprocess.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

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
 * The current task registry used by Task Scheduler Store registered services,
 * running tasks and nb of running tasks per service
 *
 * @author vallee
 */
public class BGProcessJobRegistry {
  
  private final Map<String, BGProcessService> registeredServices;
  private final Map<Long, IBGProcessJob>      registeredJobs;
  
  /**
   * Default constructor
   */
  public BGProcessJobRegistry()
  {
    registeredServices = new ConcurrentHashMap<>();
    registeredJobs = new ConcurrentHashMap<>();
  }
  
  /**
   * Check if the service is not already existing and register it if not
   *
   * @param serviceName
   *          the service name
   * @return the corresponding BGP service
   */
  private BGProcessService registerService(String serviceName) throws CSInitializationException
  {
    if (!registeredServices.containsKey(serviceName)) {
      registeredServices.put(serviceName, new BGProcessService(serviceName));
    }
    else { // replace with the new set of properties
      registeredServices.replace(serviceName, new BGProcessService(serviceName));
    }
    return registeredServices.get(serviceName);
  }
  
  /**
   * Register an active task for first time
   *
   * @param job
   */
  public synchronized void registerNewJob(IBGProcessJob job)
  {
    if (!registeredJobs.containsKey(job.getIID())) {
      registeredJobs.put(job.getIID(), job);
    }
  }
  
  /**
   * Register or update an active task
   *
   * @param job
   */
  public synchronized void registerOrUpdateJob(IBGProcessJob job)
  {
    if (registeredJobs.containsKey(job.getIID())) {
      registeredJobs.replace(job.getIID(), job);
    }
    else {
      registeredJobs.put(job.getIID(), job);
    }
  }
  
  /**
   * Register a service and its corresponding task from task data
   *
   * @param bgpProcess task data
   * @param defaultUserSession the root user session of BGP
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void resgisterJob(IBGProcessDTO bgpProcess, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        BGProcessService bgpService;
        try {
          bgpService = registerService(bgpProcess.getService());
          registerNewJob(bgpService.newJobFromBGProcessData(bgpProcess, defaultUserSession));
        }
        catch (CSInitializationException e) {
          RDBMSLogger.instance().exception(e);
        }
      }
    });
  }
  
  /**
   * Remove an ended task and decrement internal counter
   *
   * @param job
   *          the ended job
   */
  public synchronized void unregisterJob(IBGProcessJob job)
  {
    registeredJobs.remove(job.getIID());
  }
  
  /**
   * @param job
   * @return true when the task is already registered
   */
  public boolean isJobRegistered(IBGProcessJob job)
  {
    return registeredJobs.containsKey(job.getIID());
  }
  
  /**
   * Reload registered services from properties
   *
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  public void updateRegisteredServices() throws CSInitializationException
  {
    String services = CSProperties.instance().getString("bgp.scheduler.services");
    String[] serviceSymbols = services.split(",");
    for (String serviceSymbol : serviceSymbols) {
      String serviceName = serviceSymbol.trim();
      if ( serviceName.isEmpty() )
        continue;
      BGProcessService service = new BGProcessService(serviceName);
      if (registeredServices.containsKey(service.getService()))
        registeredServices.replace(service.getService(), service);
      else
        registeredServices.put(service.getService(), service);
    }
  }
  
  /**
   * @param service
   * @return the number of running jobs per service
   */
  public long getNbStartedJobs(String service)
  {
    return getRegisteredJobs().stream().filter(job -> {
          return (job.getService().equals(service) && job.isStarted());
        })
        .count();
  }
  
  /**
   * Wait for all submitted tasks to terminate
   */
  public void waitRunningJobs()
  {
    registeredJobs.values()
        .forEach(task -> {
          task.getResult();
        });
  }
  
  /**
   * @param jobIID
   * @return the job identified by its IID or null
   */
  public IBGProcessJob getResgisteredJob(long jobIID)
  {
    return registeredJobs.get(jobIID);
  }
  
  /**
   * @return the set of active jobs
   */
  public Collection<IBGProcessJob> getRegisteredJobs()
  {
    return registeredJobs.values();
  }
  
  /**
   * @return the set of registered services
   */
  public Collection<BGProcessService> getRegisteredServices()
  {
    return registeredServices.values();
  }
}
