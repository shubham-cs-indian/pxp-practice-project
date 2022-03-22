package com.cs.core.bgprocess.services.config;

import com.cs.core.bgprocess.dto.TagValueDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.ITagValueDeleteDTO;
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
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TagValueDelete extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String        NB_BATCHES                = "nbBatches";
  private static final String        BATCH_SIZE                = "batchSize";
  private static final String        PROCESSED_TAG_VALUES_CODE = "processedTagValueCode";
  
  private int                        batchSize;
  private int                        currentBatchNo            = 0;
  protected int                      nbBatches                 = 0;
  
  protected final ITagValueDeleteDTO tagValueDelete            = new TagValueDeleteDTO();
  protected Set<String>              processedTagValueCodes    = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    //Get entry data
    tagValueDelete.fromJSON(jobData.getEntryData()
        .toString());
    //
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
    
    nbBatches = tagValueDelete.getTagValues().size() / batchSize;
    if ( nbBatches == 0 || tagValueDelete.getTagValues().size() % batchSize > 0 )
      nbBatches++; // add 1 more run for remaining entities 
    
    RDBMSLogger.instance().info("Task %s / iid=%d initialization batchSize=%d ", getService(), getIID(), batchSize);
    
    if (jobData.getRuntimeData().isEmpty()) {
      jobData.getRuntimeData().setField(NB_BATCHES, nbBatches);
      jobData.getRuntimeData().setField(BATCH_SIZE, batchSize);
      jobData.getRuntimeData().setStringArrayField(PROCESSED_TAG_VALUES_CODE, processedTagValueCodes);
    }
    else {
      IJSONContent runtimeData = jobData.getRuntimeData();
      batchSize = runtimeData.getInitField(BATCH_SIZE, batchSize);
      nbBatches = runtimeData.getInitField(NB_BATCHES, 1);
      processedTagValueCodes.addAll(runtimeData.getArrayField(PROCESSED_TAG_VALUES_CODE, String.class));
    }
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    if (jobData.getProgress()
        .getPercentageCompletion() == 100) {
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      return jobData.getStatus();
    }
    
    currentBatchNo = this.getCurrentBatchNo() + 1;
    
    RDBMSLogger.instance()
        .info("Starting %s (job %d), batch %d / %d", getService(), getIID(), currentBatchNo,
            nbBatches);
    
    Set<String> remTagValues = new HashSet<>();
    remTagValues.addAll(tagValueDelete.getTagValues());
    remTagValues.removeAll(processedTagValueCodes);
    Set<String> batchTagValueCodes = new HashSet<>();
    Iterator<String> remTagValuesItr = remTagValues.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remTagValuesItr.hasNext())
        break;
      batchTagValueCodes.add(remTagValuesItr.next());
    }
    
    this.runTagValueDeleteBatch(batchTagValueCodes);
    
    processedTagValueCodes.addAll(batchTagValueCodes);
    jobData.getRuntimeData()
        .setStringArrayField(PROCESSED_TAG_VALUES_CODE, processedTagValueCodes);
    RDBMSLogger.instance()
        .info("Batch %d: processed tag value codes %s", currentBatchNo,
            batchTagValueCodes.toString());
    // Update progress data
    jobData.getProgress()
        .setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    // Keep continuing as soon as the final number of batches has not been
    // reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchTagValueCodes.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary()
        .getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  protected void runTagValueDeleteBatch(Set<String> tagValueCodes)
      throws RDBMSException, CSFormatException
  {
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
          currentConn.getProcedure( "sp_removetagvaluecode")
              .setInput(ParameterType.STRING_ARRAY, tagValueCodes)
              .execute();
        });
  }
  
}
