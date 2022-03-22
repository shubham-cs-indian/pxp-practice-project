package com.cs.core.bgprocess.services.datarules;

import com.cs.core.bgprocess.dto.DataRuleDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.dao.RuleCatalogDAO;
import com.cs.core.rdbms.process.dao.RuleCatalogDAS;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Set;

public class DataRuleDelete extends AbstractBGProcessJob implements IBGProcessJob {
  
  private int        currentBatchNo    = 0;
  private int        nbBatches         = 1;
  IDataRuleDeleteDTO dataRuleDeleteDTO = new DataRuleDeleteDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    dataRuleDeleteDTO.fromJSON(jobData.getEntryData().toString());
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    this.deleteDataRules(dataRuleDeleteDTO.getRuleCodes());
    
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
  
  private void deleteDataRules(Set<String> dataRuleCodes) throws Exception
  {
    updateElasticDocument(dataRuleCodes);
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      RuleCatalogDAS ruleDAS = new RuleCatalogDAS(currentConn);
      
      ruleDAS.deleteDataRulesAndViolations(dataRuleCodes);
    });
  }

  private void updateElasticDocument(Set<String> dataRuleCodes)
      throws CSFormatException, RDBMSException
  {
    ILocaleCatalogDAO localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(dataRuleDeleteDTO.getLocaleID(), dataRuleDeleteDTO.getCatalogCode(), ""));
    
    IRuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(localeCatalogDAO);
    List<Long> baseEntityIIDs = ruleCatalogDAO.getViolatedEntitiesOfRuleCodes(dataRuleCodes);
    
    for (Long iid : baseEntityIIDs) {
      localeCatalogDAO.postUsecaseUpdate(iid, EventType.ELASTIC_UPDATE);
    }
  }
  
}