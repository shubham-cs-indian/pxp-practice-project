package com.cs.core.bgprocess.services.goldenrecord;

import com.cs.core.bgprocess.dto.DeleteGoldenRecordBucketDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IDeleteGoldenRecordBucketDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class DeleteGoldenRecordBuckets extends AbstractBGProcessJob implements IBGProcessJob {
  
  protected int                        nbBatches                    = 1;
  protected int                        batchSize;
  protected int                        currentBatchNo               = 0;
  private IDeleteGoldenRecordBucketDTO deleteGoldenRecordsBucketDTO = new DeleteGoldenRecordBucketDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    deleteGoldenRecordsBucketDTO.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    deleteGoldenRecordBuckets(deleteGoldenRecordsBucketDTO.getRuleId());
    
    setCurrentBatchNo(++currentBatchNo);
    
    IBGProcessDTO.BGPStatus status = null;
    
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    return status;
  }
  
  private void deleteGoldenRecordBuckets(String ruleId) throws RDBMSException
  {
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    goldenRecordBucketDAO.deleteGoldenRecordBucketsByRuleId(ruleId);
  }
  
}
