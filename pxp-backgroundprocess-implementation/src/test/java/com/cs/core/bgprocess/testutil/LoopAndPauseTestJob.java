package com.cs.core.bgprocess.testutil;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * A Demonstrator of BGP Job This a very simple task that contains a defined
 * number of batches which consist in running a loop with pause
 *
 * @author vallee
 */
public class LoopAndPauseTestJob extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String NB_BATCHES = "nbBatches";
  private static final String PAUSE_MS   = "pauseInMilliSec";
  private static final String NB_PAUSE   = "nbPausesPerBatch";
  private int                 nbBatches;
  private int                 pauseInMilliSec;
  private int                 nbPausesPerBatch;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    // Get the parameters from the properties file
    nbBatches = CSProperties.instance().getInt(propName("nbBatches"));
    pauseInMilliSec = CSProperties.instance().getInt(propName("pauseInMilliSec"));
    nbPausesPerBatch = CSProperties.instance().getInt(propName("nbPausesPerBatch"));
    // example of how to initialize runtime data
    jobData.getRuntimeData().setField(NB_BATCHES, nbBatches);
    jobData.getRuntimeData().setField(PAUSE_MS, pauseInMilliSec);
    jobData.getRuntimeData().setField(NB_PAUSE, nbPausesPerBatch);
    RDBMSLogger.instance().debug("\t->Job %s / iid=%d end of initialization nbBatches=%d, pause=%d ms, nbPauses=%d",
            getService(), getIID(), nbBatches, pauseInMilliSec, nbPausesPerBatch);
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    // increment the next number of batches
    int currentBatchNo = getCurrentBatchNo();
    int loopNb = 0;
    while (loopNb++ < nbPausesPerBatch) {
      jobData.getProgress().setPercentageCompletion(loopNb * 100 / nbPausesPerBatch);
      String logLine = String.format(
          "Job %s / iid=%d Batch simulation, loop %d / %d on batch %d, progression %d",
          getService(), getIID(), loopNb, nbPausesPerBatch, currentBatchNo, jobData.getProgress().getPercentageCompletion());
      RDBMSLogger.instance().debug("\t->" + logLine);
      jobData.getLog().info(logLine);
      updateProcessData(); // periodically report progress information in RDBMS
      try {
        Thread.sleep(pauseInMilliSec);
      }
      catch (InterruptedException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
    // Keep continuing as soon as the final number of batches has not been reached
    return (getCurrentBatchNo() < nbBatches ? BGPStatus.RUNNING : BGPStatus.ENDED_SUCCESS);
  }
}
