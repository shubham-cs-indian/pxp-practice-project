package com.cs.core.bgprocess.services.purging;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

/**
 * Task in charge of removing old events and old bgProcess from the database
 * This is a very simple process in one single part and multiple batches
 *
 * @author vallee
 */
public class QueueMaintenance extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                 maxDeletionLines;
  private int                 maxHours;
  private int                 nbBatches;
  private long                deadLine;
  private int                 currentBatchNo      = 1;
  
  private static final String Q_EVENT_QUEUE_COUNT = "select count(*) as eventCount from pxp.eventqueue where posted < ?";
  
  /**
   * @return the number of events to be deleted
   * @throws RDBMSException
   */
  private int countNumberOfOldLines() throws RDBMSException
  {
    deadLine = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(maxHours);
    int[] nbOfLines = { 0 };
    RDBMSConnectionManager.instance().runTransaction(( currConnection) -> {
          PreparedStatement query = currConnection.prepareStatement(Q_EVENT_QUEUE_COUNT);
          query.setLong(1, deadLine);
          ResultSet rs = query.executeQuery();
          if (rs.next())
            nbOfLines[0] = rs.getInt("eventCount");
        });
    return nbOfLines[0];
  }
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    // Get the parameters from the properties file
    setCurrentBatchNo(currentBatchNo);
    maxDeletionLines = CSProperties.instance().getInt(propName("maxDeletionLines"));
    maxHours = CSProperties.instance().getInt(propName("maxHours"));
    int numberOfEventsToDelete = countNumberOfOldLines();
    nbBatches = numberOfEventsToDelete / maxDeletionLines + 1;
    RDBMSLogger.instance().debug("\t->task %s / iid=%d end of initialization maxDeletionLines=%d, maxHours=%d", getService(),
            getIID(), maxDeletionLines, maxDeletionLines);
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    // Check the max number of batches has not been overtaken
    if (currentBatchNo <= nbBatches) {
      RDBMSLogger.instance()
          .debug("\t->task %s / iid=%d purging batch %d on %d lines", getService(), getIID(),
              currentBatchNo, maxDeletionLines);
      purgeEventQueue();
      jobData.getProgress()
          .setPercentageCompletion((currentBatchNo * 100) / nbBatches);
      RDBMSLogger.instance()
          .info("\t->task %s / iid=%d purging batch %d on complete %d percentage", getService(),
              getIID(), currentBatchNo, jobData.getProgress().getPercentageCompletion());
      setCurrentBatchNo(++currentBatchNo);
      // updateProcessData(); // periodically report progress information in
      // RDBMS => not required
      // here since it is automatically updated at the begining of the run
    }
    // Keep continuing as soon as the final number of batches has not been
    // reached
    return (currentBatchNo > nbBatches ? BGPStatus.ENDED_SUCCESS : BGPStatus.RUNNING);
  }
  
  /**
   * delete the line of records from event queue
   *
   * @throws RDBMSException
   */
  public synchronized void purgeEventQueue() throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          currentConn.getProcedure( "pxp.sp_purgeEventQueueByLines")
              .setInput(ParameterType.LONG, deadLine)
              .setInput(ParameterType.INT, maxDeletionLines)
              .execute();
        });
  }
}
