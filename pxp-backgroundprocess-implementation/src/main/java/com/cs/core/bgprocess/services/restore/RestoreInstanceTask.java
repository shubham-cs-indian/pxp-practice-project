package com.cs.core.bgprocess.services.restore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IBaseEntityPlanDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.restore.RollbackAndRestoreUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;

public class RestoreInstanceTask extends AbstractBGProcessJob implements IBGProcessJob {

  IBaseEntityPlanDTO                backgroundData      = new BaseEntityPlanDTO();
  
  private Long                      baseEntityIID;
  
  protected int                     currentBatchNo = 0;
  
  protected int                     nbBatches      = 1;
  
  @Autowired
  protected RollbackAndRestoreUtils rollbackAndRestoreUtils;
  
  @Override
  public void initBeforeStart(IBGProcessDTO taskData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(taskData, defaultUserSession);
    backgroundData.fromJSON(jobData.getEntryData().toString());
    baseEntityIID = backgroundData.getBaseEntityIIDs().iterator().next();
    rollbackAndRestoreUtils = BGProcessApplication.getApplicationContext().getBean(RollbackAndRestoreUtils.class);
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException,
      PluginException, Exception
  {
    IBaseEntityDAO baseEntityDAO = rollbackAndRestoreUtils.handleBaseEntityRestore(Arrays.asList(Long.toString(baseEntityIID)));
    
    handlePostRestoreEntitySpecificUseCase(baseEntityDAO);
    
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

  /**
   * Entity Specific handling after restore
   * @param baseEntityDAO
   * @throws RDBMSException
   * @throws Exception
   */
  private void handlePostRestoreEntitySpecificUseCase(IBaseEntityDAO baseEntityDAO) throws RDBMSException, Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    BaseType baseType = baseEntityDTO.getBaseType();
    // Entity specific handling for Asset type
    if (BaseType.ASSET.equals(baseType)) {
      handleDuplicateDetection(baseEntityDAO);
    }
    
  }
  
  /**
   * Handle duplicate detection (Asset Specific).
   * @param baseEntityDAO
   * @throws Exception
   * @throws RDBMSException
   */
  private void handleDuplicateDetection(IBaseEntityDAO baseEntityDAO) throws Exception, RDBMSException
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Map<String, Object> transactionDataMap = new HashMap<String, Object>();
    long assetInstanceIID = baseEntityDTO.getBaseEntityIID();
    long duplicateId = 0;
    transactionDataMap.put(ITransactionData.ENDPOINT_ID, baseEntityDTO.getEndpointCode());
    transactionDataMap.put(ITransactionData.ORGANIZATION_ID, backgroundData.getOrganizationCode());
    transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, backgroundData.getCatalogCode());
    
    duplicateId = AssetUtils.handleDuplicate(baseEntityDTO.getHashCode(),
        baseEntityDAO, transactionDataMap, assetInstanceIID);
    AssetUtils.updateDuplicateStatus(duplicateId, baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
  }

}