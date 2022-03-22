package com.cs.core.bgprocess;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.postgres.RDBMSDriver;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Entry point for business process executor
 *
 * @author vallee
 */
public class BGProcessApplication {
  
  private BGProcessDispatcher   jobDispatcher;
  private int                   pollInterval       = 0;
  private long                  loopTime           = 0L; // measured time to run a loop
  public static IRDBMSAppDriver driver             = null;
  private static final String       PXP_BGP_HOME       = System.getenv("PXP_BGP_HOME");
  private static ApplicationContext applicationContext = null;
  
  private static BGProcessApplication bgpApplication;
  
  // Termination management on interrupt signal
  private static class InterruptSignal {
    
    private boolean flag = false;
    
    /**
     * @return true whenever the process has received a termination signal
     */
    public boolean isRaised()
    {
      return flag;
    }
    
    /**
     * Raise the interruption flag
     */
    public synchronized void raise()
    {
      flag = true;
    }
  }
  
  private static final InterruptSignal INTERRUPT = new InterruptSignal();
  
  static {
    Runtime.getRuntime().addShutdownHook(new Thread()
        {
          @Override
          public void run()
          {
            INTERRUPT.raise();
          }
        });
  }
  
  /**
   * Make public constructor available
   */
  public BGProcessApplication()
  {
  }
  
  /**
   * Initialize RDBMS connector
   *
   * @throws RDBMSException
   * @throws CSInitializationException
   */
  private static void initRDBMS() throws RDBMSException, CSInitializationException
  {
    if (RDBMSDriverManager.getDriver() != null)
      return; // already initialized
    // initialize the DB connection according to the property db vendor
    
    String dbVendor = CSProperties.instance().getString("dbVendor");
    try {
      switch (RDBMSDriver.DBVendor.valueOf(dbVendor.toUpperCase())) {
        case POSTGRES:
          RDBMSDriver driver = (RDBMSDriver) applicationContext.getBean("driver");
          Class.forName("com.cs.core.rdbms.postgres.RDBMSDriver");
          RDBMSDriverManager.registerDriver(driver);
          break;
        case ORACLE:
          Class.forName("com.cs.core.rdbms.oracle.RDBMSDriver");
          RDBMSDriverManager.registerDriver(new com.cs.core.rdbms.oracle.RDBMSDriver());
          break;
        default:
          throw new CSInitializationException("Unknown dbVendor: " + dbVendor);
      }
    }
    catch (ClassNotFoundException ex) {
      throw new CSInitializationException("Configuration error", ex);
    }
    driver = new RDBMSAppDriver();
    RDBMSAppDriverManager.registerDriver(driver);
  }
  
  /**
   * Default initialization
   * Initialize the bgp ExecutorService and reload the Interrupted Jobs 
   * 
   * The method runs in transaction boundary (Annotation Driven transaction) 
   * The transaction is added here to provide a connection for initializing the BGP service
   * and loading default initialization of user session
   * @throws CSInitializationException
   * @throws RDBMSException
   */
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    // initialize the job dispatcher in real mode
    try {
      jobDispatcher = BGProcessDispatcher.instance();
    } catch ( CSInitializationException | RDBMSException | CSFormatException ex) {
      RDBMSLogger.instance().exception( ex);
      throw ex;
    }
  }

  /**
   * Default initialization of properties and load context file
   * @throws CSInitializationException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public static void initialize() throws CSInitializationException, RDBMSException, CSFormatException
  {
    // first manage properties and initialize the logger
    if ( PXP_BGP_HOME.isEmpty() ) {
      throw new CSInitializationException( "missing PXP_BGP_HOME environment variable");
    }
    CSProperties.init( PXP_BGP_HOME + "/bgprocess-props/bgprocess.properties");
    RDBMSLogger.instance().info("Starting BGP application");
    
    // initialize applicationContext
    applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    bgpApplication = (BGProcessApplication) applicationContext.getBean("bgpApplication");
    initRDBMS();
  }
  
  public static ApplicationContext getApplicationContext()
  {
    return applicationContext;    
  }
  
  /**
   * Initialization with a specific property file (used for tests)
   *
   * @param propertyFilePath specify the BGP property file that should be used
   * @throws CSInitializationException
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void init(String propertyFilePath)
      throws CSInitializationException, RDBMSException, CSFormatException
  {
    // first manage properties and initialize the logger
    CSProperties.init(propertyFilePath);
    RDBMSLogger.instance().info("Starting BGP application in TEST MODE");
    initRDBMS();
    // initialize the task scheduler in test mode
    try {
      jobDispatcher = BGProcessDispatcher.instance();
    } catch ( CSInitializationException | RDBMSException | CSFormatException ex) {
      RDBMSLogger.instance().exception( ex);
      throw ex;
    }
  }
  
  /**
   * @return the default callback URL Template
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  public String getTestCallbackTemplateURL() throws CSInitializationException
  {
    return CSProperties.instance().getString("testCallbackURL");
  }
  
  /**
   * run the BGP scheduler that manages the BGP services to start (made public
   * for test purpose)
   * 
   * @throws com.cs.core.technical.exception.CSInitializationException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void runDispatcher() throws CSInitializationException, RDBMSException, CSFormatException
  {
    CSProperties.instance().reload();// Reload the scheduling properties
    pollInterval = CSProperties.instance().getInt("bgp.scheduler.pollInterval");
    long startLoopTime = System.currentTimeMillis();
    jobDispatcher.detectAndRegisterPendingJobs();
    jobDispatcher.manageRegisteredJobs();
    loopTime = System.currentTimeMillis() - startLoopTime;
  }
  
  /**
   * run a single batch of any pending/waiting tasks without schedule control
   * This service is only aimed for test purpose in order to check the behavior
   * of new BGP tasks
   *
   * @param nbBatches
   *          the number of batches to be run
   * @throws CSInitializationException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public void runJobSample(int nbBatches)
      throws CSInitializationException, RDBMSException, CSFormatException, Exception
  {
    jobDispatcher.detectAndRegisterPendingJobs();
    jobDispatcher.runJobSamples(nbBatches);
  }
  
  /**
   * mark a pause given by the poll interval time minus the last time to loop
   * over tasks
   */
  public void pause()
  {
    try {
      long waitingMs = TimeUnit.SECONDS.toMillis(pollInterval) - loopTime;
      Thread.sleep(waitingMs > 0 ? waitingMs : 0);
    }
    catch (InterruptedException ex) {
      RDBMSLogger.instance().exception(ex);
    }
  }
  
  /**
   * Wait all the current job to be finished and stop the process
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void shutdown() throws RDBMSException
  {
    // Wait all dispathed services to be ended correctly...
    jobDispatcher.shutdown();
    // Close properly the DB connections opened from this hostname
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          RDBMSAppDriverManager.getDriver().newUserSessionDAO().shutdownSessions();
        }
        catch (RDBMSException e) {
         RDBMSLogger.instance().exception(e);
        }
      }
    });
    RDBMSLogger.instance().info("BGP Application correctly stopped.");
  }
  
  /**
   * The main loop consists in polling pending BGP tasks at regular interval The
   * dispatcher considers the pending and paused jobs from the RDBMS and starts
   * them according to available resources and priorities. When the main loop is
   * interrupted (SIGINT from OS), then all activities are shutdown properly
   * through the dispatcher. According to the running tasks, the shutdown
   * procedure may take many minutes.
   *
   * @param args
   */
  public static void main(String[] args)
  {
    try {
      //initialize the default property 
      initialize();
      bgpApplication.init();
      // start the HTTP Server for REST services
      BGProcessRESTServer.start();
      int counter = 1;
      UUID id = null;
      do {
        if (counter == 10) {
          id = UUID.randomUUID();
          RDBMSLogger.instance().debug("\t->BGP dispatcher started of id =" + id);
        }
        bgpApplication.runDispatcher();
        if (counter == 10) {
          RDBMSLogger.instance().debug("\t->BGP dispatcher ended successfully of id=" + id);
          counter = 1;
        }
        bgpApplication.pause();
        counter++;
      }
      while (!INTERRUPT.isRaised());
      bgpApplication.shutdown();
    }
    catch (Throwable ex) {
      RDBMSLogger.instance().exception(ex);
      ex.printStackTrace();
      RDBMSLogger.instance().warn("BGP application terminated on exception");
      System.exit(1);
    }
    RDBMSLogger.instance().warn("BGP application safely shutdown");
    System.exit(0);
  }
}
