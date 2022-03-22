package com.cs.core.runtime.variant.assetinstance;

import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.exception.assetinstance.AssetInstanceDeleteFailedException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.variant.AbstractDeleteInstanceVariantsService;
import com.cs.dam.downloadtracker.IResetDownloadCountService;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetWithoutTIVsFromSharedSwiftServerService;
import com.cs.utils.dam.AssetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DeleteAssetInstanceVariantsService extends AbstractDeleteInstanceVariantsService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel>
    implements IDeleteAssetInstanceVariantsService {
  
  @Resource(name = "assetException")
  private Properties                                            assetException;
  
  @Autowired
  protected IDeleteAssetWithoutTIVsFromSharedSwiftServerService deleteAssetWithoutTIVsFromSharedSwiftServerService;
  
  @Autowired
  protected IResetDownloadCountService                          resetDownloadCountService;
  
  @Autowired
  protected TransactionThreadData                               transactionThreadData;
  
  @Autowired
  RDBMSComponentUtils                                           rdbmsComponentUtils;
  
  @Override
  protected IBulkDeleteVariantsReturnModel executeInternal(IDeleteVariantModel model) throws Exception
  {
    try {
      for (String id : model.getIds()) {
        IIdParameterModel dataModel = new IdParameterModel(id);
        deleteAssetWithoutTIVsFromSharedSwiftServerService.execute(dataModel);
        resetDownloadCountService.execute(dataModel);
      }
      return super.executeInternal(model);
    }
    catch (PluginException e) {
      ExceptionUtil.replaceExceptionKeyWithSpecificExceptionKey(assetException, e.getExceptionDetails(), e.getDevExceptionDetails());
      throw new AssetInstanceDeleteFailedException(e.getExceptionDetails(), e.getDevExceptionDetails());
    }
  }
  
  @Override
  protected void preProcessData(IBaseEntityDAO baseEntityDAO) throws Exception
  {
    Map<String, Object> transactionDataMap = new HashMap<>();
    TransactionData transactionData = transactionThreadData.getTransactionData();
    transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
    transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
    transactionDataMap.put(ITransactionData.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
    
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    String hash = baseEntityDTO.getHashCode();
    long baseEntityIId = baseEntityDTO.getBaseEntityIID();
    
    // Mark isDuplicate column of entity to false if it is no longer duplicate.
    long duplicateAssetIId = AssetUtils.handleDuplicate(hash, baseEntityDAO, transactionDataMap, baseEntityIId);
    if (duplicateAssetIId != 0 && duplicateAssetIId != 1) {
      Set<Long> baseEntityIIds = new HashSet<>();
      baseEntityIIds.add(duplicateAssetIId);
      baseEntityDAO.markAssetsDuplicateByIIds(baseEntityIIds, false);
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(duplicateAssetIId, IEventDTO.EventType.ELASTIC_UPDATE);
    }
  }
}
