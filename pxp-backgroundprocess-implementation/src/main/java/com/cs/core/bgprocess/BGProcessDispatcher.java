package com.cs.core.bgprocess;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.BGProcessJobRegistry;
import com.cs.core.bgprocess.services.BGProcessService;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.eventqueue.impl.CustomExecutorService;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * The background process dispatcher reads the BGP services from the property
 * file and it decides to run them according to the available resources It picks
 * the service definitions from properties and decide to run them according to
 * the conditions to start a new job (cf. limited by nbThread property). 
 * It then follows their execution to ensure their correct
 * termination in case of shutdown
 *
 * @author vallee
 */
public class BGProcessDispatcher {
  
  // singleton implementation
  private static BGProcessDispatcher INSTANCE = null;
  
  public static BGProcessDispatcher instance() throws CSInitializationException, RDBMSException, CSFormatException
  {
    if (INSTANCE == null)
      INSTANCE = new BGProcessDispatcher();
    return INSTANCE;
  }
  
  private final BGProcessJobRegistry jobRegistry;
  private IUserSessionDTO            rootUserSession;
  private final ExecutorService      executor;
  
  /**
   * Constructor with real/test mode specification
   */
  private BGProcessDispatcher() throws CSInitializationException, RDBMSException, CSFormatException
  {
    jobRegistry = new BGProcessJobRegistry();
    IRDBMSAppDriver driver = RDBMSAppDriverManager.getDriver();
    
    RDBMSAbstractDriver txDriver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(txDriver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          rootUserSession = driver.newUserSessionDAO().openSession(CSProperties.instance().getString("bgpUserName"),
              IUserSessionDTO.createUniqueSessionID("BGP"));
        }
        catch (RDBMSException | CSInitializationException e) {
          RDBMSLogger.instance().exception(e);
        }
      }
    });
    
    // the cached thread pool mode reuses ended threads for new tasks
    BlockingQueue<Runnable> threadQueue = new SynchronousQueue<>();
    int maxThreads = CSProperties.instance().getInt("bgp.scheduler.maxThread");
    maxThreads = ( maxThreads > 10 ? maxThreads : 10); // assume minimum of 10
    int minThreads = CSProperties.instance().getInt("bgp.scheduler.minThread");
    minThreads = ( minThreads <= maxThreads ? minThreads : maxThreads );
    executor = new CustomExecutorService( minThreads, maxThreads, 500L, TimeUnit.MILLISECONDS, threadQueue);
    // at construction, the list of existing tasks is pulled from the database
    reloadInterruptedJobs();
  }
  
  /**
   * Rebuild the list of interrupted and running jobs from database information
   * and register the corresponding task
   */
  private void reloadInterruptedJobs() throws RDBMSException, CSInitializationException, CSFormatException
  {
    // Pull from the database the list of RUNNING or INTERRUPTED jobs
    List<IBGProcessDTO> bgpJobs = (new BGProcessDAS()).getInterruptedJobs();
    for (IBGProcessDTO taskData : bgpJobs) {
      RDBMSLogger.instance().debug( 
              "Found interrupted or running task iid=%d, service=%s", taskData.getJobIID(), taskData.getService());
      jobRegistry.resgisterJob(taskData, rootUserSession); // create and register the task from its data
    }
  }
  
  /**
   * Start a job by registering it properly in activeTasks and executor
   *
   * @param newJob
   */
  private void startJob(IBGProcessJob newJob)
  {
    try {
      Future<BGPStatus> taskHandler = executor.submit(newJob);
      newJob.registerSchedulerHandler(taskHandler);
    }
    catch (RejectedExecutionException ex) {
      RDBMSLogger.instance().warn("New task rejected: " + newJob.getIID());
      RDBMSLogger.instance().exception(ex);
    }
  }
  
  /**
   * Check active jobs remove when ended or failed, restart when ready for a new
   * batch
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void manageRegisteredJobs() throws RDBMSException
  {
    jobRegistry.getRegisteredJobs().forEach((IBGProcessJob job) -> {
      if (job.isTerminated()) {
        RDBMSLogger.instance().debug(
                "Detecting terminated job iid=%d, service=%s, status=%s", job.getIID(), job.getService(), job.getStatus());
        jobRegistry.unregisterJob(job);
      }
      else if (!job.isSubmitted() || job.isWaiting()) {
        RDBMSLogger.instance().debug(
                "Allocating thread to job iid=%d, service=%s, status=%s", job.getIID(), job.getService(), job.getStatus());
        startJob(job);
      }
      else if (job.isReadyToNewBatch()) { // should never happen
        RDBMSLogger.instance().warn(
            "Reactivating idle job iid=%d, service=%s, status=%s", job.getIID(), job.getService(), job.getStatus());
        startJob(job);
      }
    });
  }
  
  /**
   * Running the dispatcher consists in - start new registered service according
   * to their starting conditions
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void detectAndRegisterPendingJobs()
      throws RDBMSException, CSInitializationException, CSFormatException
  {
    jobRegistry.updateRegisteredServices(); // update services from property file
    for (BGProcessService bgpService : jobRegistry.getRegisteredServices()) {
      String service = bgpService.getService();
      
      long serviceJobNb = jobRegistry.getNbStartedJobs(service);
      while (bgpService.canStartNewJob(serviceJobNb)) {
        RDBMSLogger.instance().debug( 
                "Service %s ready for a new job (currently %d tasks started out of %d)", service, serviceJobNb,  bgpService.getNbThread());
        IBGProcessJob job = bgpService.pollNewStartingJob(rootUserSession);
        if (job != null && !jobRegistry.isJobRegistered(job)) {
          RDBMSLogger.instance().debug(
                  "Detecting new starting job iid=%d, service=%s, status=%s", job.getIID(), job.getService(), job.getStatus());
          jobRegistry.registerOrUpdateJob(job); // update job information into the registery
          serviceJobNb = jobRegistry.getNbStartedJobs(service);
        }
        else {
          break; // no new task is pending here, so no need to keep monitoring new tasks for this service
        }
      }
    }
  }
  
  private static final int SHUTDOWN_TIME_SEC = 30; // wait 30 sec. between 2 attempts to shutdown
  private static final int MAX_SHUTDOWN_TRY  = 10; // wait at most 5 mn to shutdown
  
  /**
   * send an interrupt signal to active tasks and check to shutdown the executor
   */
  void shutdown()
  {
    // send an interrupt message to active jobs
    RDBMSLogger.instance().info("Engaging all jobs shutdown");
    RDBMSLogger.instance().debug("Shutdown with %d active jobs", jobRegistry.getRegisteredJobs().size());
    executor.shutdownNow();
    boolean ended = false;
    int tryNb = 0;
    do {
      tryNb++;
      RDBMSLogger.instance().debug("Attempt %d / %d to shutdown...", tryNb, MAX_SHUTDOWN_TRY);
      try { // Wait 30 sec before reevaluating
        ended = executor.awaitTermination(SHUTDOWN_TIME_SEC, TimeUnit.SECONDS);
      }
      catch (InterruptedException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
    while (!ended && tryNb < MAX_SHUTDOWN_TRY);
    if (!ended) {
      jobRegistry.getRegisteredJobs().stream().filter(job -> {
          return job.isRunning();
        })
        .forEach(job -> {
          RDBMSLogger.instance() .warn("Abrupt termination of " + job.getIID());
          job.updateInterruptedProcessData();
          RDBMSLogger.instance().debug(
            "Remaining task iid=%d, service=%s, status=%s", job.getIID(), job.getService(), job.getStatus());
        });
      RDBMSLogger.instance().warn("Abrupt shutdown");
    }
  }
  
  /**
   * Manage to shutdown a particular job with a new stopped status
   *
   * @param jobIID
   * @param bgpStatus
   */
  public void manageJobWithStopStatus(long jobIID, BGPStatus bgpStatus)
  {
    IBGProcessJob job = jobRegistry.getResgisteredJob(jobIID);
    if (job == null || !bgpStatus.isStopped())
      return; // The job is not registered as current job or this is not a stop
    RDBMSLogger.instance().warn("Received order to stop job %d with status %s", jobIID, bgpStatus.toString());
    job.stopWithStatus(bgpStatus);
  }
  
  /**
   * Run tasks for testing purpose
   *
   * @param nbBatches the maximum number of task to run
   */
  void runJobSamples(int nbBatches)
      throws RDBMSException, CSInitializationException, CSFormatException, Exception
  {
    for (IBGProcessJob job : jobRegistry.getRegisteredJobs()) {
      for (int i = 0; i < nbBatches; i++) {
        RDBMSLogger.instance().debug(
                "BEGIN SAMPLE %d on service %s / job iid=%d", i, job.getService(), job.getIID());
        BGPStatus status = job.call();
        RDBMSLogger.instance().debug(
                "END SAMPLE %s / job iid=%d status=%s", job.getService(), job.getIID(), status.toString());
        if (job.isTerminated())
          break;
      }
    }
  }
}
