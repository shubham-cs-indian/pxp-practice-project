package com.cs.core.config.iconlibrary;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.dto.AssetFileDTO;
import com.cs.core.bgprocess.dto.BulkUploadIconsDTO;
import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBulkUploadIconsDTO;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import com.cs.utils.dam.AssetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer implementation for bulk icon upload
 * 
 * @author pranav.huchche
 *
 */
@Service
public class UploadIconsToServerService extends AbstractSaveConfigService<IUploadMultipleIconsRequestModel, IBulkUploadResponseAssetModel>
    implements IUploadIconsToServerService {
  
  @Autowired
  TransactionThreadData           transactionThread;
  
  @Autowired
  IFetchAssetConfigurationDetails fetchAssetConfigurationDetails;
  
  @Override
  protected IBulkUploadResponseAssetModel executeInternal(IUploadMultipleIconsRequestModel model) throws Exception
  {
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = model.getMultiPartFileInfoList();
    Map<String, Object> fileKeyVsCodeMap = model.getFileKeyVsCodeMap();
    Map<String, Object> fileKeyVsNameMap = model.getFileKeyVsNameMap();
    IBulkUploadResponseAssetModel responseModel = new BulkUploadResponseAssetModel();
    IExceptionModel failure = new ExceptionModel();
    List<IAssetFileModel> fileModelList = new ArrayList<>();
    
    IIdParameterModel idParameterModel = new IdParameterModel();
    idParameterModel.setType(DAMConstants.ASSET_KLASS);
    IAssetConfigurationDetailsResponseModel assetModel = fetchAssetConfigurationDetails.execute(idParameterModel);
    
    if (assetModel.getNatureType() == null) {
      assetModel.setNatureType(VertexLabelConstants.ENTITY_TYPE_ICON);
    }
    
    if (!multiPartFileInfoModelList.isEmpty()) {
      for (IMultiPartFileInfoModel multiPartFileInfoModel : multiPartFileInfoModelList) {
        String iconCode = (String) fileKeyVsCodeMap.get(multiPartFileInfoModel.getKey());
        String iconName = (String) fileKeyVsNameMap.get(multiPartFileInfoModel.getKey());
        
        IAssetFileModel assetFileModel = AssetUtils.getFileModel(multiPartFileInfoModel, DAMConstants.MODE_CONFIG, fileModelList,
            assetModel);
        assetFileModel.setKey(multiPartFileInfoModel.getKey());
        
        if (StringUtils.isEmpty(iconCode)) {
          iconCode = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ICON.getPrefix());
        }
        
        assetFileModel.setCode(iconCode);
        assetFileModel.setName(iconName);
        fileModelList.add(assetFileModel);
      }
    }
    
    if (!fileModelList.isEmpty()) {
      IBulkUploadIconsDTO iconsUploadDTO = prepareBulkIconsUploadDTO(fileModelList);
      workflowUtils.executeApplicationEvent(IApplicationTriggerModel.ApplicationActionType.ICON_UPLOAD, iconsUploadDTO.toJSON(),
          BGPPriority.MEDIUM);
    }
    
    responseModel.setFailure(failure);
    return responseModel;
  }
  
  /**
   * This method prepares icon upload DTO for BGP call.
   * 
   * @param fileModelList
   * @return
   * @throws CSInitializationException
   */
  private IBulkUploadIconsDTO prepareBulkIconsUploadDTO(List<IAssetFileModel> fileModelList) throws CSInitializationException
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    IBulkUploadIconsDTO entryData = new BulkUploadIconsDTO();
    entryData.setLocaleId(transactionData.getUiLanguage());
    entryData.setUserId(transactionData.getUserId());
    
    List<IAssetFileDTO> assetFileDTOs = new ArrayList<>();
    for (IAssetFileModel file : fileModelList) {
      IAssetFileDTO assetFile = new AssetFileDTO();
      assetFile.setFilePath(file.getPath());
      assetFile.setKlassID(file.getKlassId());
      assetFile.setExtensionType(file.getExtensionType());
      assetFile.setIsExtracted(file.getIsExtracted());
      assetFile.setCode(file.getCode());
      assetFile.setName(file.getName());
      IAssetExtensionConfiguration extensionConfiguration = file.getExtensionConfiguration();
      Map<String, Object> extensionConfigurationMap = new HashMap<>();
      if (extensionConfiguration != null) {
        extensionConfigurationMap.put(IAssetExtensionConfiguration.EXTRACT_METADATA, extensionConfiguration.getExtractMetadata());
        extensionConfigurationMap.put(IAssetExtensionConfiguration.EXTRACT_RENDITION, extensionConfiguration.getExtractRendition());
      }
      assetFile.setExtensionConfiguration(extensionConfigurationMap);
      assetFileDTOs.add(assetFile);
    }
    entryData.setAssets(assetFileDTOs);
    
    return entryData;
  }
}
