package com.cs.core.bgprocess.services.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Generic processing of a list of base entity IIDs
 *
 * @author vallee
 */
public abstract class AbstractBaseEntityProcessing extends AbstractBGProcessJob
    implements IBGProcessJob {
  
  private static final String       NB_BATCHES           = "nbBatches";
  private static final String       BATCH_SIZE           = "batchSize";
  private static final String       PROCESSED_IIDS       = "processedIIDs";
  protected int                     nbBatches            = 0;
  protected int                     batchSize;
  protected final BaseEntityPlanDTO processPlan          = new BaseEntityPlanDTO();
  protected Set<Long>               passedBaseEntityIIDs = new HashSet<>();
  
  /**
   * Implement the process on a batch of base entities
   *
   * @param batchIIDs
   *          the set of base entity IIDs to be processed
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws CSInitializationException 
   * @throws PluginException
   * @throws JsonProcessingException
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   * @throws Exception
   * @throws PluginException
   * @throws Exception
   */
  protected abstract void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException, CSFormatException, CSInitializationException, PluginException, JsonProcessingException, JsonParseException, JsonMappingException, IOException, Exception;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    // Initialize entry data
    processPlan.fromJSON(jobData.getEntryData().toString());
    // Get the parameters from the processedProperties file vs. runtime data when this has been interrupted
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
    nbBatches = processPlan.getBaseEntityIIDs().size() / batchSize;
    if ( nbBatches == 0 || processPlan.getBaseEntityIIDs().size() % batchSize > 0 )
      nbBatches++; // add 1 more run for remaining entities 
    if (jobData.getRuntimeData().isEmpty()) {
      jobData.getRuntimeData().setField(NB_BATCHES, nbBatches);
      jobData.getRuntimeData().setField(BATCH_SIZE, batchSize);
      jobData.getRuntimeData().setLongArrayField(PROCESSED_IIDS, passedBaseEntityIIDs);
    }
    else {
      IJSONContent runtimeData = jobData.getRuntimeData();
      batchSize = runtimeData.getInitField(BATCH_SIZE, batchSize);
      nbBatches = runtimeData.getInitField(NB_BATCHES, 1);
      passedBaseEntityIIDs.addAll(runtimeData.getArrayField(PROCESSED_IIDS, Long.class));
    }
    RDBMSLogger.instance().info("Starting %s (job %d), batch %d / %d of %d base entity(ies)",
        getService(), getIID(), getCurrentBatchNo(), nbBatches, batchSize);
  }
  
  @Override
  public IBGProcessDTO.BGPStatus runBatch()
      throws JsonProcessingException, Exception
  {
    // process next batch and increment for progress
    int currentBatchNo = getCurrentBatchNo() + 1;
    // Identify the entities that must be processed and build the next batch
    Set<Long> remEntityIIDs = new HashSet<>();
    remEntityIIDs.addAll(processPlan.getBaseEntityIIDs());
    remEntityIIDs.removeAll(passedBaseEntityIIDs);
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = remEntityIIDs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }
    // Process with the batch of base entity IIDs
    runBaseEntityBatch(batchEntityIIDs);
    // Once processed, register entities as processed
    passedBaseEntityIIDs.addAll(batchEntityIIDs);
    jobData.getRuntimeData().setLongArrayField(PROCESSED_IIDS, passedBaseEntityIIDs);
    jobData.getSummary().setTotalCount(passedBaseEntityIIDs.size());
    RDBMSLogger.instance().info(
            "Batch %d: processed base entity IIDs %s", getCurrentBatchNo(), batchEntityIIDs.toString());
    // Update progress data
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
}
