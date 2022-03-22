package com.cs.core.bgprocess.services.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cs.core.bgprocess.dto.UpdateInstancesOnKPIDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IUpdateInstancesOnKPIDeleteDTO;
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

public class UpdateInstancesOnKPIDelete  extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int                                    batchSize;
  private int                                    currentBatchNo                = 0;
  protected int                                  numberOfBatches               = 0;
  
  protected final IUpdateInstancesOnKPIDeleteDTO updateInstancesOnKPIDeleteDTO = new UpdateInstancesOnKPIDeleteDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    //Getting data from BGP table
    updateInstancesOnKPIDeleteDTO.fromJSON(jobData.getEntryData().toString());
    
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
    
    numberOfBatches = updateInstancesOnKPIDeleteDTO.getRuleCodes().size() / batchSize;
    if ( numberOfBatches == 0 || updateInstancesOnKPIDeleteDTO.getRuleCodes().size() % batchSize > 0 )
      numberOfBatches++; // add 1 more run for remaining entities 
    
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
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
            numberOfBatches);
    
    List<String> deletedRuleCodes = updateInstancesOnKPIDeleteDTO.getRuleCodes();
    List<String> batchRuleCodes = new ArrayList<>();
    Iterator<String> ruleCodesIterator = deletedRuleCodes.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!ruleCodesIterator.hasNext())
        break;
      batchRuleCodes.add(ruleCodesIterator.next());
    }
    
    this.runBatchToUpdateInstances(batchRuleCodes);
    //delete processed codes
    deletedRuleCodes.removeAll(batchRuleCodes);
    
    RDBMSLogger.instance()
        .info("Batch %d: processed Rule Codes %s", currentBatchNo,
            batchRuleCodes.toString());
    // Update progress data
    jobData.getProgress()
        .setPercentageCompletion(currentBatchNo * 100 / numberOfBatches);
    // Keep continuing as soon as the final number of batches has not been
    // reached or the last batch was not empty
    if (currentBatchNo < numberOfBatches && !batchRuleCodes.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary()
        .getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  protected void runBatchToUpdateInstances(List<String> ruleCodes)
      throws RDBMSException, CSFormatException
  {
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
          currentConn.getProcedure( "sp_updateinstancesonkpidelete")
              .setInput(ParameterType.STRING_ARRAY, ruleCodes)
              .execute();
        });
  }
  
}
