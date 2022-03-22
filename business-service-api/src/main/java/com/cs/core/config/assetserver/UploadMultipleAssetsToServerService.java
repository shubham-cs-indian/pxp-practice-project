package com.cs.core.config.assetserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.dto.AssetFileDTO;
import com.cs.core.bgprocess.dto.BulkAssetUploadDTO;
import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBulkAssetUploadDTO;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithGlobalPermissionStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.assetupload.IUploadAssetResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.dam.runtime.usecase.upload.IUploadAssetsService;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;

@Service("uploadMultipleAssetsToServerService")
public class UploadMultipleAssetsToServerService extends AbstractSaveConfigService<IUploadAssetModel, IBulkUploadResponseAssetModel>
    implements IUploadMultipleAssetsToServerService {
  
  @Autowired
  protected PermissionUtils                       permissionUtils;
  
  @Autowired
  protected IGetAssetWithGlobalPermissionStrategy getAssetWithGlobalPermissionStrategy;
  
  @Autowired
  protected IFetchAssetConfigurationDetails       fetchAssetConfigurationDetails;
  
  @Autowired
  protected IUploadAssetsService                  uploadAssetsService;
  
  @Override
  public IBulkUploadResponseAssetModel executeInternal(IUploadAssetModel model) throws Exception
  {
    IBulkUploadResponseAssetModel responseModel = new BulkUploadResponseAssetModel();
    
    // Get fileModelList & asset configuration
    IUploadAssetResponseModel uploadResponseModel = uploadAssetsService.execute(model);
    
    IAssetConfigurationDetailsResponseModel assetConfigModel = uploadResponseModel.getAssetConfigModel();
    initiateUpload(model, uploadResponseModel.getFileModelList(), responseModel, assetConfigModel.getDetectDuplicate());

    responseModel.setFailure(uploadResponseModel.getFailure());
    return responseModel;
  }

  protected void initiateUpload(IUploadAssetModel model, List<IAssetFileModel> fileModelList,
      IBulkUploadResponseAssetModel responseModel, Boolean detectDuplicate) throws Exception
  {
    if (!fileModelList.isEmpty()) {
      responseModel.setIsAssetUploadSubmitedToBGP(true);
      IBulkAssetUploadDTO bulkAssetUploadDTO = prepareBulkAssetUploadDTO(fileModelList, model, detectDuplicate);
      workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.ASSET_UPLOAD, bulkAssetUploadDTO.toJSON(), BGPPriority.MEDIUM);
    }
    else {
      responseModel.setIsAssetUploadSubmitedToBGP(false);
    }
  }
  
  protected IBulkAssetUploadDTO prepareBulkAssetUploadDTO(List<IAssetFileModel> fileModelList,
      IUploadAssetModel model, Boolean detectDuplicate)
  {
    boolean shouldCheckForRedundancy = false;
    TransactionData transactionData = transactionThread.getTransactionData();
    IBulkAssetUploadDTO entryData = new BulkAssetUploadDTO();
    entryData.setLocaleID(transactionData.getDataLanguage());
    entryData.setCatalogCode(transactionData.getPhysicalCatalogId());
    entryData.setUserId(transactionData.getUserId());
    entryData.setUserName(transactionData.getUserName());
    entryData.setOrganizationCode(transactionData.getOrganizationId());
    entryData.setEndpointId(transactionData.getEndpointId());
    entryData.setCatalogCode(transactionData.getPhysicalCatalogId());
    entryData.setPortalId(transactionData.getPortalId());
    entryData.setParentTransactionId(transactionData.getParentTransactionId());
    entryData.setSystemId(transactionData.getSystemId());
    entryData.setDetectDuplicate(detectDuplicate);
    
    // fill relationship data if uploaded from relationship tab
    if (DAMConstants.MODE_RELATIONSHIP_BULK_UPLOAD.equals(model.getMode())) {
      fillRelationshipData(entryData, model);
    }
    
    List<IAssetFileDTO> assetFileDTOs = new ArrayList<>();
    for (IAssetFileModel file : fileModelList) {
      shouldCheckForRedundancy = file.getShouldCheckForRedundancy();
      IAssetFileDTO assetFile = new AssetFileDTO();
      assetFile.setFilePath(file.getPath());
      assetFile.setKlassID(file.getKlassId());
      assetFile.setExtensionType(file.getExtensionType());
      assetFile.setIsExtracted(file.getIsExtracted());
      IAssetExtensionConfiguration extensionConfiguration = file.getExtensionConfiguration();
      Map<String, Object> extensionConfigurationMap = new HashMap<>();
      if (extensionConfiguration != null) {
        extensionConfigurationMap.put("extractMetadata", extensionConfiguration.getExtractMetadata());
        extensionConfigurationMap.put("extractRendition", extensionConfiguration.getExtractRendition());
      }
      assetFile.setExtensionConfiguration(extensionConfigurationMap);
      assetFile.setIds(file.getIds());
      assetFile.setIsInDesignServerEnabled(file.isInDesignServerEnabled());
      assetFileDTOs.add(assetFile);
    }
    entryData.setAssets(assetFileDTOs);
    entryData.setShouldCheckForRedundancy(shouldCheckForRedundancy);
    entryData.setCollectionIds(model.getCollectionIds());
    return entryData;
  }

  /** Fill relationship data for relationship Tab Bulk upload
   * @param entryData
   * @param model
   */
  @SuppressWarnings("unchecked")
  private void fillRelationshipData(IBulkAssetUploadDTO entryData, IUploadAssetModel model)
  {
    Map<String, String[]> parameterMap = model.getAdditionalParameterMap();
    String modifiedRelationships = parameterMap.get("modifiedRelationship")[0];
    Map<String, String> modifiedRelationshipMap = null;
    try {
      modifiedRelationshipMap = ObjectMapperUtil.readValue(modifiedRelationships, Map.class);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    } 
    
    entryData.setSide1InstanceId(parameterMap.get(BulkAssetUploadDTO.SIDE_1_INSTANCE_ID)[0]);
    entryData.setTabType(parameterMap.get(BulkAssetUploadDTO.TAB_TYPE)[0]);
    entryData.setSide1BaseType(parameterMap.get(BulkAssetUploadDTO.SIDE_1_BASE_TYPE)[0]);
    entryData.setTabId(parameterMap.get(BulkAssetUploadDTO.TAB_ID)[0]); 
    entryData.setRelationshipEntityId(modifiedRelationshipMap.get(BulkAssetUploadDTO.RELATIONSHIP_ENTITY_ID));
    entryData.setRelationshipId(modifiedRelationshipMap.get(BulkAssetUploadDTO.RELATIONSHIP_ID));
    entryData.setSideId(modifiedRelationshipMap.get(BulkAssetUploadDTO.SIDE_ID));
    entryData.setMode(model.getMode());
  }
}
