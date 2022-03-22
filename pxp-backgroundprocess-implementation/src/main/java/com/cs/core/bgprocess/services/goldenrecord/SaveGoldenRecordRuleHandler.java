package com.cs.core.bgprocess.services.goldenrecord;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.GoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class SaveGoldenRecordRuleHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  protected int            nbBatches       = 1;
  protected int            batchSize;
  protected int            currentBatchNo  = 0;
  private IGoldenRecordDTO goldenRecordDTO = new GoldenRecordDTO();
  ILocaleCatalogDAO        localeCatalogDAO;
  GoldenRecordUtils        goldenRecordUtils;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    goldenRecordDTO.fromJSON(jobData.getEntryData().toString());
    localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(goldenRecordDTO.getLocaleID(), goldenRecordDTO.getCatalogCode(), ""));
    goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    deleteAndEvaluateGoldenRecordBuckets(goldenRecordDTO.getRuleId());
    
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
  
  private void deleteAndEvaluateGoldenRecordBuckets(String ruleId) throws Exception
  {
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    goldenRecordBucketDAO.deleteGoldenRecordBucketsByRuleId(ruleId);
    for (Long baseEntityIid : goldenRecordDTO.getLinkedBaseEntities()) {
      IBaseEntityDTO baseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntityIid);
      goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDTO);
    }
  }
  
}
