package com.cs.core.bgprocess.services.uniquenessViolation;

import java.util.List;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.UniquenessDTO;
import com.cs.core.rdbms.entity.idto.IKpiUniqunessDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.rdbms.process.dao.RuleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class UniquenessViolation extends AbstractBGProcessJob implements IBGProcessJob {
  
  UniquenessDTO uniquenessDTO = new UniquenessDTO();
  
  int currentBatchNo = 0;
  
  protected int                      nbBatches                 = 1;
  
  private int                        batchSize;

  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    
    List<IUniquenessViolationDTO> sourceViolationDTO = uniquenessDTO.getUniquenessViolationDTOs();
    
    if(!sourceViolationDTO.isEmpty()) {
      
      long targetIID = sourceViolationDTO.get(0).getTargetIID();
      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, targetIID);
      IUniquenessViolationDAO uniquenessViolationDAO = catalogDao.openUniquenessDAO();
      uniquenessViolationDAO.insertViolatedEntity(sourceViolationDTO);
    }

    /* IKpiUniqunessDTO kpiUniqunessDTOs = uniquenessDTO.getKpiUniqunessDTOs();
    
    if(kpiUniqunessDTOs != null && !kpiUniqunessDTOs.getBaseEntityIID().isEmpty()) {
      ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession, kpiUniqunessDTOs.getSourceIID());
      RuleCatalogDAO ruleCatalogDAO = new RuleCatalogDAO(catalogDao);
      List<Long> targetEntityIIDs = kpiUniqunessDTOs.getBaseEntityIID();
      
      ruleCatalogDAO.updateTargetBaseEntityforUniquenessBlock(targetEntityIIDs, kpiUniqunessDTOs.getRuleExpressionIID(),(double) 0);
      ruleCatalogDAO.insertKpiUniqunessViolationForSourceEntities(kpiUniqunessDTOs.getBaseEntityIID(), kpiUniqunessDTOs.getSourceIID(), kpiUniqunessDTOs.getRuleExpressionIID());
    }*/
    
    setCurrentBatchNo(++currentBatchNo);
    IBGProcessDTO.BGPStatus status = null;
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    if (jobData.getProgress().getPercentageCompletion() == 100)
    {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    
    return status;
  }
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    uniquenessDTO.fromJSON(jobData.getEntryData().toString());
    
    batchSize = CSProperties.instance().getInt(propName("batchSize"));

    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
}
