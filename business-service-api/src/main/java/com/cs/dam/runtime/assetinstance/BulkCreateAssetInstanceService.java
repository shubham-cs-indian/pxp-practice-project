package com.cs.dam.runtime.assetinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.Constants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.BulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.CreateAssetInstanceAfterUploadRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceRequestModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.ICreateAssetInstanceAfterUploadRequestModel;
import com.cs.core.config.interactor.model.variantcontext.GetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForAutoCreateTIVStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;

/***
 * This service bulk create the asset instances for the uploaded assets
 * @author vannya.kalani
 *
 */
@Service
public class BulkCreateAssetInstanceService 
    extends AbstractRuntimeService<IBulkCreateAssetInstanceRequestModel, IBulkCreateAssetInstanceResponseModel>
    implements IBulkCreateAssetInstanceService {
  
  @Autowired
  RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  IFetchAssetConfigurationDetails             fetchAssetConfigurationDetails;
  
  @Autowired
  IGetConfigDetailsForAutoCreateTIVStrategy   getConfigDetailsForAutoCreateTIVStrategy;
  
  @Autowired
  ICreateAssetInstanceAfterUploadService      createAssetInstanceAfterUploadService;
  
  @Override
  protected IBulkCreateAssetInstanceResponseModel executeInternal(IBulkCreateAssetInstanceRequestModel model) throws Exception
  {
    IBulkCreateAssetInstanceResponseModel responseModel = new BulkCreateAssetInstanceResponseModel();
    Map<String, IGetConfigDetailsForCustomTabModel> typeIdConfigDetailsMapping = new HashMap<>();
    Map<String, IGetConfigDetailsForCreateVariantModel> tivConfigDetailsMap = new HashMap<>();
    List<String> contextIds = new ArrayList<>();
    
    Map<String, Boolean> isHashDuplicatedMap = model.getIsHashDuplicatedMap();
    List<IAssetKeysModel> assetInstances = model.getAssetKeysModelList();
    fillAssetInstanceConfigDetails(typeIdConfigDetailsMapping, assetInstances, contextIds);
    
    if (!contextIds.isEmpty()) {
      tivConfigDetailsMap = getConfigDetailsForTIVCreation(contextIds);
    }
    
    //Get configured extensions list from orient.
    IAssetConfigurationDetailsResponseModel assetConfigurationDetailsResponseModel = fetchAssetConfigurationDetails
        .execute(new IdParameterModel());
    
    for (IAssetKeysModel assetKeyModel : model.getAssetKeysModelList()) {
      String klassType = assetKeyModel.getKlassId();
      boolean isDuplicate = false;
      if (isHashDuplicatedMap != null) {
        isDuplicate = isHashDuplicatedMap.get(assetKeyModel.getHash()) != null && isHashDuplicatedMap.get(assetKeyModel.getHash());
      }
      
      ICreateAssetInstanceAfterUploadRequestModel reqModel = prepareRequestModelForCreateInstanceAfterUpload(tivConfigDetailsMap,
          assetConfigurationDetailsResponseModel, assetKeyModel, isDuplicate, typeIdConfigDetailsMapping.get(klassType));
      IBulkCreateAssetInstanceResponseModel returnModel = createAssetInstanceAfterUploadService.execute(reqModel);
      
      fillResponseModel(responseModel, returnModel);
    }
    
    List<String> collectionIds = model.getCollectionIds();
    if (!collectionIds.isEmpty()) {
      addInstancesToCollections(responseModel.getSuccessInstanceIIds(), collectionIds);
    }
    
    return responseModel;
  }

  /***
   * Prepare and returns ICreateAssetInstanceAfterUploadRequestModel model.
   * @param tivConfigDetailsMap
   * @param assetConfigurationDetailsResponseModel
   * @param assetKeyModel
   * @param isDuplicate
   * @param configDetails
   * @return
   */
  private ICreateAssetInstanceAfterUploadRequestModel prepareRequestModelForCreateInstanceAfterUpload(
      Map<String, IGetConfigDetailsForCreateVariantModel> tivConfigDetailsMap,
      IAssetConfigurationDetailsResponseModel assetConfigurationDetailsResponseModel,
      IAssetKeysModel assetKeyModel, boolean isDuplicate, IGetConfigDetailsForCustomTabModel configDetails)
  {
    ICreateAssetInstanceAfterUploadRequestModel reqModel = new CreateAssetInstanceAfterUploadRequestModel();
    reqModel.setAssetKeysModel(assetKeyModel);
    reqModel.setConfigDetails(configDetails);
    reqModel.setAssetConfigDetails(assetConfigurationDetailsResponseModel);
    reqModel.setTivConfigDetails(tivConfigDetailsMap);
    reqModel.setIsDuplicate(isDuplicate);
    return reqModel;
  }

  /***
   * Fills the passed response model.
   * @param responseModel
   * @param returnModel
   */
  private void fillResponseModel(IBulkCreateAssetInstanceResponseModel responseModel, IBulkCreateAssetInstanceResponseModel returnModel)
  {
    responseModel.getSuccess().addAll(returnModel.getSuccess());
    responseModel.getSuccessInstanceIIds().addAll(returnModel.getSuccessInstanceIIds());
    responseModel.getFailure().addAll(returnModel.getFailure());
    responseModel.getTivFailure().addAll(returnModel.getTivFailure());
    responseModel.getTivSuccess().addAll(returnModel.getTivSuccess());
    responseModel.getTivWarning().addAll(returnModel.getTivWarning());
    responseModel.getDuplicateIIdSet().addAll(returnModel.getDuplicateIIdSet());
    responseModel.getTivDuplicateDetectionInfoMap().putAll(returnModel.getTivDuplicateDetectionInfoMap());
  }
  
  /***
   * Get the config details for passed assetInstances and fill the typeIdConfigDetailsMapping and contextIds
   * @param typeIdConfigDetailsMapping klassId and configDetails map
   * @param assetInstances list of IAssetKeysModel for which config details need to be fetched.
   * @param contextIds list in which auto create tiv klass context ids needs to be added.
   * @throws Exception
   */
  private void fillAssetInstanceConfigDetails(Map<String, IGetConfigDetailsForCustomTabModel> typeIdConfigDetailsMapping,
      List<IAssetKeysModel> assetInstances, List<String> contextIds) throws Exception
  {
    for (IAssetKeysModel assetInstance : assetInstances) {
      String type = assetInstance.getKlassId();
      
      if (typeIdConfigDetailsMapping.get(type) != null) {
        continue;
      }
      
      IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
      fillMultiClassificationDetails(type, multiclassificationRequestModel);
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
      List<ITechnicalImageVariantWithAutoCreateEnableModel> autoCreateConfigInfoList = configDetails
          .getTechnicalImageVariantContextWithAutoCreateEnable();
      autoCreateConfigInfoList.forEach(autoCreateConfigInfo -> contextIds.add(autoCreateConfigInfo.getId()));
      typeIdConfigDetailsMapping.put(type, configDetails);
    }
  }
  
  /***
   * Fills the passed MulticlassificationRequestModel.
   * @param type
   * @param multiclassificationRequestModel
   */
  private void fillMultiClassificationDetails(String type, IMulticlassificationRequestModel multiclassificationRequestModel)
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    multiclassificationRequestModel.setKlassIds(Arrays.asList(type));
  }
  
  /***
   * Get the config details of auto-create TIVs for passed context Ids.
   * @param contextIds list of context id for which config details needs to be fetched
   * @return
   * @throws Exception
   */
  private Map<String, IGetConfigDetailsForCreateVariantModel> getConfigDetailsForTIVCreation(List<String> contextIds)
      throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    IGetConfigDetailsForAutoCreateTIVRequestModel getConfigDetailsForAutoCreateTIVRequestMap = new GetConfigDetailsForAutoCreateTIVRequestModel();
    getConfigDetailsForAutoCreateTIVRequestMap.setContextIds(contextIds);
    getConfigDetailsForAutoCreateTIVRequestMap.setOrganizationId(transactionData.getOrganizationId());
    getConfigDetailsForAutoCreateTIVRequestMap.setEndpointId(transactionData.getEndpointId());
    getConfigDetailsForAutoCreateTIVRequestMap.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    getConfigDetailsForAutoCreateTIVRequestMap.setPortalId(transactionData.getPortalId());
    getConfigDetailsForAutoCreateTIVRequestMap.setBaseType(Constants.ASSET_INSTANCE_BASE_TYPE);
    
    IGetConfigDetailsForAutoCreateTIVResponseModel configDetailsForAutoCreateTIVResponseModel = getConfigDetailsForAutoCreateTIVStrategy
        .execute(getConfigDetailsForAutoCreateTIVRequestMap);
    return configDetailsForAutoCreateTIVResponseModel.getConfigDetailsMap();
  }
  
  /***
   * Add the passed instanceIIds in collections of passed collectionIds
   * @param instanceIdsForCollection
   * @param collectionIds
   * @throws Exception
   */
  private void addInstancesToCollections(List<Long> instanceIdsForCollection, List<String> collectionIds) throws Exception
  {
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCollection();
    for (String collectionId : collectionIds) {
      long collectionIId = Long.parseLong(collectionId);
      ICollectionDTO collectionDTO = collectionDAO.getCollection(collectionIId, CollectionType.staticCollection);
      collectionDAO.updateCollectionRecords(collectionIId, collectionDTO, instanceIdsForCollection, new ArrayList<>());
    }
  }
}
