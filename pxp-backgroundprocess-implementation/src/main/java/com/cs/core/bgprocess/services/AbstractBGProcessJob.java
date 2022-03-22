package com.cs.core.bgprocess.services;

import static com.cs.core.bgprocess.idto.IBGProcessDTO.JOB_PLACEHOLDER;
import static com.cs.core.bgprocess.idto.IBGProcessDTO.STATUS_PLACEHOLDER;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.bgprocess.dto.BGPProgressDTO;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.data.TextArchive;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.google.gson.Gson;

/**
 * Common implementation for BGProcess tasks By construction, this class has
 * subclasses are only instantiated by default constructor
 *
 * @author vallee
 */
public abstract class AbstractBGProcessJob implements IBGProcessJob {

  private static final String   TIMESTAMP_FORMAT     = "dd-MM-yyyy HH:mm:ssZ";
  public static final String    SUCCESS_IIDS         = "SUCCESS_IIDS";
  public static final String    FAILED_IIDS          = "FAILED_IIDS";

  private Future<BGPStatus>     taskHandler          = null;
  protected final BGProcessDAS  bgpDAS               = new BGProcessDAS();
  protected IRDBMSAppDriver     driver               = RDBMSAppDriverManager.getDriver();
  private boolean               isDefautlUserSession = true;
  protected IUserSessionDTO     userSession          = null;
  protected BGProcessDTO        jobData              = null;
  private BGPStatus             userRequiredStatus   = BGPStatus.UNDEFINED;
  private TransactionThreadData transactionThread    = null;
  private static boolean        callbackEnabled      = true;
  private Boolean               shouldPostProcess    = false;
  private IModel                dataForPostProcessing;
  private String                postProcessingUrl;
  private String                jSessionID;
  protected RDBMSComponentUtils rdbmsComponentUtils;
  private static final String SOURCE_CATALOG         = "sourceCatalog";

  /**
   * Use progress data to store the current batch no between 2 executions
   *
   * @return the current batch no
   */
  protected final int getCurrentBatchNo()
  {
    return jobData.getProgress().getBatchNo();
  }

  protected final void setCurrentBatchNo(int no) throws CSFormatException
  {
    ((BGPProgressDTO) jobData.getProgress()).setBatchNo(no);
  }

  @Override
  public final synchronized void stopWithStatus(BGPStatus bgpStatus)
  {
    userRequiredStatus = bgpStatus;
  }

  @Override
  public void initBeforeStart(IBGProcessDTO taskData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    jobData = (BGProcessDTO) taskData;

    // defines what user to use and pass the correct user session
    userSession = defaultUserSession;
    if (!taskData.getUser().isNull()) {
      userSession = RDBMSAppDriverManager.getDriver().newUserSessionDAO().openSession(taskData.getUser().getUserName(),
          IUserSessionDTO.createUniqueSessionID("BGP"));
      isDefautlUserSession = false;
    }
    jobData.initStartTimeNow();
    writeLogHeader();
    updateProcessData(BGPStatus.RUNNING);
    RDBMSLogger.instance().info("Prepared before starting: BGP %s / iid=%d / new status = %s", getService(), getIID(), jobData.getStatus());
  }

  private void initTransactionData(BGProcessDTO jobData) throws CSFormatException
  {
    JSONContentParser entryData = new JSONContentParser(jobData.getEntryData().toString());

    String localeID = entryData.getString(Constants.LOCALE_ID);
    String catalogCode = entryData.getString(Constants.CATALOG_CODE)
        .isEmpty() ? entryData.getString(SOURCE_CATALOG)
            : entryData.getString(Constants.CATALOG_CODE);
    String organizationCode = entryData.getString(Constants.ORGANIZATION_CODE);
    String userId = entryData.getString(Constants.USER_ID);
    String userName = entryData.getString(Constants.USER_NAME);
    String endpointID = entryData.getString(ITransactionData.ENDPOINT_ID);

    transactionThread = BGProcessApplication.getApplicationContext().getBean(TransactionThreadData.class);
    TransactionData transactionData = transactionThread.getTransactionData();
    transactionData.setOrganizationId(organizationCode);
    transactionData.setDataLanguage(localeID);
    transactionData.setPhysicalCatalogId(catalogCode);
    transactionData.setUserId(userId);
    transactionData.setEndpointId(endpointID);
    try {
      userSession.setTransactionId(UUID.randomUUID().toString());
      SessionContextCustomProxy context = BGProcessApplication.getApplicationContext().getBean(SessionContextCustomProxy.class);
      context.setUserSessionDTOInThreadLocal(userSession);

      if (StringUtils.isEmpty(userName)) {
        userName = userSession.getUserName();
      }
      transactionData.setUserName(userName);

      rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }

  }

  /**
   * @param characteristic queried in the properties file
   * @return the full property name
   */
  protected final String propName(String characteristic)
  {
    return String.format("bgp.%s.%s", getService(), characteristic);
  }

  @Override
  public final boolean isSubmitted()
  {
    return this.taskHandler != null;
  }

  @Override
  public final BGPStatus getResult()
  {
    if (this.taskHandler != null)
      try {
        return taskHandler.get();
      }
      catch (InterruptedException | ExecutionException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    return BGPStatus.UNDEFINED;
  }

  @Override
  public final synchronized void registerSchedulerHandler(Future<BGPStatus> taskHandler)
  {
    this.taskHandler = taskHandler;
  }

  @Override
  public final boolean isReadyToNewBatch()
  {
    return (!isInterrupted() && isRunning() && (taskHandler == null || taskHandler.isDone()));
  }

  /**
   * @return true when an interruption is required by the task scheduler
   */
  protected final boolean isInterrupted()
  {
    return isSubmitted() && taskHandler.isCancelled();
  }

  /**
   * @return true when pause or cancellation is required by user services
   */
  protected final boolean isStoppedByUser()
  {
    return userRequiredStatus.isStopped();
  }

  @Override
  public final IBGProcessDTO.BGPStatus getStatus()
  {
    if (taskHandler != null) { // Make task termination more robust by calling
                               // Future<>::get()
      try {
        taskHandler.get(1, TimeUnit.MILLISECONDS);
      }
      catch (TimeoutException ex) {
        // Don't mention it
      }
      catch (InterruptedException | ExecutionException exc) {
        RDBMSLogger.instance().exception(exc);
      }
      catch (Exception appEx) { // Other exception case must be handled here
        RDBMSLogger.instance().exception(appEx);
        if (jobData.getStatus() != BGPStatus.ENDED_EXCEPTION) {
          jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
          jobData.getLog().exception(appEx);
        }
      }
    }
    return jobData.getStatus();
  }

  @Override
  public final String getService()
  {
    return jobData.getService();
  }

  @Override
  public final long getIID()
  {
    return jobData.getIID();
  }

  @Override
  public int compareTo(Object t)
  {
    IBGProcessJob that = (IBGProcessJob) t;
    int serviceComparison = this.getService().compareTo(that.getService());
    return serviceComparison != 0 ? serviceComparison : new Long(this.getIID()).compareTo(that.getIID());
  }

  @Override
  public final synchronized void updateProcessData() throws RDBMSException, CSFormatException
  {
    
    jobData.initStartTimeNow(); // start time is set to current system time when
                                // not yet defined
    bgpDAS.updateBGProcessData(jobData.getJobIID(), getStatus().ordinal(), jobData.getRuntimeData().toString(),
        jobData.getProgress().toJSON(), jobData.getSummary().toJSON(), TextArchive.zip(jobData.getLog().toBytes()),
        jobData.getRunningTime(), jobData.getStartTime(), jobData.getEndTime());
    
  }

  /**
   * update the process data with changing the status
   *
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void updateProcessData(BGPStatus newStatus) throws RDBMSException, CSFormatException
  {
    jobData.setStatus(newStatus);
    if (newStatus.isEnded())
      jobData.setEndTimeNow();
    updateProcessData();
  }

  @Override
  public final synchronized void updateInterruptedProcessData()
  {
    jobData.getLog().progress("Process interrupted by user or shutdown");
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          updateProcessData(BGPStatus.INTERRUPTED);
        }
        catch (RDBMSException | CSFormatException ex) {
          RDBMSLogger.instance().warn("Cannot update process id=%d status to interrupted", getIID());
          RDBMSLogger.instance().exception(ex);
        }
      }
    });
  }

  @Override
  public final synchronized void updateAbortedProcessData(Exception ex)
  {
    jobData.getLog().progress("Process interrupted by exception");
    jobData.getLog().exception(ex);
    try {
      updateProcessData(BGPStatus.INTERRUPTED);
    }
    catch (RDBMSException | CSFormatException ex2) {
      RDBMSLogger.instance().warn("Cannot update process id=%d status to interrupted", getIID());
      RDBMSLogger.instance().exception(ex2);
    }
  }

  protected void writeLogHeader()
  {
    jobData.getLog().info("BGP %s / Job iid %d started %s", getService(), getIID(),
        (new SimpleDateFormat(TIMESTAMP_FORMAT)).format(new Date()));
    jobData.getLog().info("User %s / session ID %s", userSession.getUserName(), userSession.getSessionID());
    jobData.getLog().progress("Starting from status %s\n", jobData.getStatus());
  }

  /**
   * Open an user session to execute any RDBMS service
   *
   * @return the user session interface
   * @throws RDBMSException
   */
  protected final IUserSessionDAO openUserSession() throws RDBMSException
  {
    return driver.newUserSessionDAO();
  }

  /**
   * Open a locale catalog
   *
   * @param catalog the catalog to be opened
   * @return the catalog interface
   * @throws RDBMSException
   */
  protected final ILocaleCatalogDAO openLocaleCatalog(ILocaleCatalogDTO catalog) throws RDBMSException
  {
    IUserSessionDAO session = openUserSession();
    return session.openLocaleCatalog(userSession, catalog);
  }

  /**
   * Disable callback returns - firstly aimed for test cases
   */
  public static void disableCallback()
  {
    callbackEnabled = false;
  }

  private final void sendCallback()
  {
    if (!callbackEnabled) {
      return;
    }
    String callbackStatus;
    switch (getStatus()) {
      case ENDED_SUCCESS:
        callbackStatus = "SUCCESS";
        break;
      case ENDED_ERRORS:
        callbackStatus = "ERROR";
        break;
      case ENDED_EXCEPTION:
        callbackStatus = "EXCEPTION";
        break;
      default:
        return; // if not endeded, no callback signal is sent
    }
    String callBackURL = jobData.getCallbackURLTemplate().trim();
    if (!callBackURL.isEmpty()) {
      callBackURL = callBackURL.replaceAll(JOB_PLACEHOLDER, Long.toString(getIID()));
      callBackURL = callBackURL.replaceAll(STATUS_PLACEHOLDER, callbackStatus);
      WebTarget pxpServer = ClientBuilder.newClient().target(callBackURL);
      RDBMSLogger.instance().info("Job iid = %d, Callback PUT %s", getIID(), callBackURL);
      try {
        pxpServer.request().put(Entity.entity(getCallbackData(), MediaType.APPLICATION_JSON));
      }
      catch (Throwable ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
    else {
      RDBMSLogger.instance().warn("Job iid = %d, Empty Callback", getIID());
    }
  }

  /**
   * The method runs in transaction boundary (Programmatic transaction) Starting
   * point of all BGP task. If an exception is thrown while executing, it
   * rollbacks all the SQL dataBase operation in a given process
   */
  @Override
  public final BGPStatus call()
  {
    RDBMSAbstractDriver driver = null;
    TransactionTemplate transactionTemplate = null;
    BGPStatus bgpStatus [] = {BGPStatus.RUNNING};
    
    try {
      driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
      transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    }
    catch (Throwable ex) {
      RDBMSLogger.instance().warn("Unable to start BGP job of IID %d / %s failed with Exception", getIID(), getService());
      RDBMSLogger.instance().exception(ex);
    }
    
   updateProcessData(transactionTemplate, bgpStatus[0]);
    
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {

      protected void doInTransactionWithoutResult(TransactionStatus status)
      {

        final Thread currentThread = Thread.currentThread();
        final String oldName = currentThread.getName();
        String id = getService() + "-" + getIID();
        currentThread.setName(id); // Manage thread name for debugging
                                   // capabilities
        RDBMSLogger.instance().debug("\t->Started new job of ID= " + id);
        try {
          initTransactionData(jobData);
          int loopNb = 1; // Keep track of loop number
          do {
            RDBMSLogger.instance().debug("\t->run batch of job iid=%d / service=%s (submission loop=%d)", getIID(), getService(), loopNb);
            long startBatchTime = System.currentTimeMillis();
            resetPostProcessingVariables();
            bgpStatus[0] = runBatch();
            bgpStatus[0] = postProcess(bgpStatus[0]);
            long batchDuration = System.currentTimeMillis() - startBatchTime;
            RDBMSLogger.instance().debug("\t->end run batch of task iid=%d / service=%s returned status=%s [duration=%d.%d sec.]", getIID(),
                getService(), bgpStatus[0].toString(), batchDuration / 1000, batchDuration % 10000);
            jobData.addRunningTime(batchDuration);
            jobData.incrBatchNo();
            if (batchDuration > TimeUnit.SECONDS.toMillis(MAX_BATCH_DURATION)) {
              RDBMSLogger.instance().warn("Job iid=%d / service=%s batch execution in more than %d sec.", getIID(), getService(),
                  MAX_BATCH_DURATION);
            }
            if (++loopNb >= NB_MAX_BATCH) { // Protection against infinite loop
              throw new CSInitializationException("Maximum number of batches has been reached");
            }
            
            if (isInterrupted() && bgpStatus[0] == BGPStatus.RUNNING) { // manage
                                                                       // here
                                                                       // the
                                                                       // shutdown
              RDBMSLogger.instance().debug("\t->Job iid=%d / service=%s is interrupted", getIID(), getService());
              bgpStatus[0] = BGPStatus.INTERRUPTED;
              
              jobData.setStatus(bgpStatus[0]);
              jobData.getLog().progress("Shutdown by server");
              break;
            }
            if (isStoppedByUser() && !bgpStatus[0].isEnded()) {
              RDBMSLogger.instance().debug("\t->Job iid=%d / service=%s is stopped by user, status=%s", getIID(), getService(),
                  userRequiredStatus.toString());
              jobData.getLog().progress("Interrupted by user with status = %s", userRequiredStatus.toString());
              bgpStatus[0] =userRequiredStatus;
              jobData.setStatus(bgpStatus[0]);
              break;
            }
            // update process data in other case of loop continuation
            jobData.setStatus(bgpStatus[0]);
          }
          while (isRunning());
          if (!isDefautlUserSession)
            openUserSession().closeSession(userSession.getSessionID(), IUserSessionDTO.LogoutType.NORMAL, "end of BGP job");
        }
        catch (Throwable ex) { // Must be a catch all at this level
          RDBMSLogger.instance().exception(ex);
          RDBMSLogger.instance().warn("BGP Task %d / %s failed on exception", getIID(), getService());
          jobData.getLog().exception(ex); // report exception in the log
          bgpStatus[0] = BGPStatus.ENDED_EXCEPTION;
          jobData.setStatus(bgpStatus[0]);
          
        }
        finally {
          currentThread.setName(oldName);
          RDBMSLogger.instance().debug("\t->Ended job successfully  of ID=" + id);
        }
      }
    });
    
    updateProcessData(transactionTemplate, bgpStatus[0]);
    // Return the status by callback
    sendCallback();
    return getStatus();
  }


  /**
   * update BGP status 
   * @param transactionTemplate
   * @param bgpStatus
   */
  private void updateProcessData(TransactionTemplate transactionTemplate, BGPStatus bgpStatus)
  {
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          updateProcessData(bgpStatus);
        }
        catch (RDBMSException | CSFormatException e) {
          RDBMSLogger.instance().warn("Unable to update status %s of job IID  %d / %s !", bgpStatus.toString(), getIID(), getService());
          RDBMSLogger.instance().exception(e);
        }
      }
    });
  }
  

  /**
   *
   * @return this method will return time, BGP service, Job iid, Session Id and
   *         user name
   */
  protected String log()
  {
    return (new SimpleDateFormat(TIMESTAMP_FORMAT)).format(new Date()).toString() + "|" + jobData.getService() + "|" + jobData.getJobIID()
        + "|" + userSession.getSessionID() + "|" + userSession.getUserName() + "|";

  }

  public void setShouldPostProcess(Boolean shouldPostProcess)
  {
    this.shouldPostProcess = shouldPostProcess;
  }

  public void setDataForPostProcessing(IModel dataForPostProcessing)
  {
    this.dataForPostProcessing = dataForPostProcessing;
  }

  public void setPostProcessingUrl(String postProcessingUrl)
  {
    this.postProcessingUrl = postProcessingUrl;
  }

  public void setJSessionID(String jSessionID)
  {
    this.jSessionID = jSessionID;
  }

  private BGPStatus postProcess(BGPStatus status)
  {
    if (status.equals(BGPStatus.ENDED_SUCCESS) && shouldPostProcess) {

      WebTarget pxpServer = ClientBuilder.newClient().target(postProcessingUrl);
      RDBMSLogger.instance().info("Job iid = %d, POST PROCESSING %s", getIID(), postProcessingUrl);
      try {
        String s = new Gson().toJson(dataForPostProcessing);
        Response post = pxpServer.request().cookie("JSESSIONID", URLEncoder.encode(jSessionID, "UTF-8"))
            .post(Entity.entity(s, MediaType.APPLICATION_JSON));

        if (post.getStatus() != 200) {
          return BGPStatus.ENDED_EXCEPTION;
        }
        String response = post.readEntity(String.class);
        JSONObject jsonObject = new JSONContent(response).toJSONObject();
        if (jsonObject.get("failure") != null) {
          return BGPStatus.ENDED_EXCEPTION;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        RDBMSLogger.instance().exception(ex);
        return BGPStatus.ENDED_EXCEPTION;
      }
    }
    return status;
  }

  private void resetPostProcessingVariables()
  {
    shouldPostProcess = false;
    dataForPostProcessing = null;
    postProcessingUrl = "";
  }

  /**
   * Get the required data for the call back.
   *
   * @return
   */
  protected String getCallbackData()
  {
    return "{}";
  }

  /**
   * @param <T>
   * @param className class object of which bean needs to return
   * @return the bean of required class object
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  protected <T> T getBean(Class<T> className) throws Exception
  {
    return (T) BGProcessApplication.getApplicationContext().getBean(Class.forName(className.getName()));
  }

}
