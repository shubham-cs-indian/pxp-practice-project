package com.cs.core.config.iconlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.constants.DAMConstants;
import com.cs.core.bgprocess.dto.AssetFileDTO;
import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.config.interactor.model.asset.AssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.AssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.configdetails.BulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.services.CSConfigServer;
import com.cs.utils.dam.AssetUtils;

@Service
public class UploadStandardIconToServerService
    extends AbstractSaveConfigService<IUploadMultipleIconsRequestModel, IBulkUploadResponseAssetModel>
    implements IUploadStandardIconsToServerService {
  
  private static final String ORIENT_PLUGIN_FOT_SAVE_ICONS = "BulkCreateAndSaveIcons";
  
  private static final String ORIENT_PLUGIN_FOR_GET_ICONS  = "GetIconsCode";
  
  @Override
  protected IBulkUploadResponseAssetModel executeInternal(IUploadMultipleIconsRequestModel model) throws Exception
  {
    List<IMultiPartFileInfoModel> multiPartFileInfoModelList = model.getMultiPartFileInfoList();
    Map<String, Object> fileKeyVsCodeMap = model.getFileKeyVsCodeMap();
    Map<String, Object> fileKeyVsNameMap = model.getFileKeyVsNameMap();
    IBulkUploadResponseAssetModel responseModel = new BulkUploadResponseAssetModel();
    IExceptionModel failure = new ExceptionModel();
    List<IAssetFileModel> fileModelList = new ArrayList<>();
    
    IAssetConfigurationDetailsResponseModel assetModel = prepareAssetConfigInfo();
    
    Map<String, Object> requestMapIcon = new HashMap<>();
    JSONObject result = CSConfigServer.instance().request(requestMapIcon, ORIENT_PLUGIN_FOR_GET_ICONS,
        LocaleCatalogDAO.getDefaultRootLocaleCatalog().getLocaleID());
    List<String> iconList = (List<String>) result.get("code");
    
    List<Map<String, Object>> iconsList = new ArrayList<Map<String, Object>>();
    
    if (!multiPartFileInfoModelList.isEmpty()) {
      for (IMultiPartFileInfoModel multiPartFileInfoModel : multiPartFileInfoModelList) {
        String iconCode = (String) fileKeyVsCodeMap.get(multiPartFileInfoModel.getKey());
        String iconName = (String) fileKeyVsNameMap.get(multiPartFileInfoModel.getKey());
        if(iconList.contains(iconCode)) {
          continue;
        }
        IAssetFileModel assetFileModel = AssetUtils.getFileModelForDataInitialization(multiPartFileInfoModel, DAMConstants.MODE_CONFIG, fileModelList,
            assetModel);
        assetFileModel.setKey(multiPartFileInfoModel.getKey());
        assetFileModel.setCode(iconCode);
        assetFileModel.setName(iconName);
        
        IAssetFileDTO assetFile = new AssetFileDTO();
        assetFile.setFilePath(assetFileModel.getPath());
        assetFile.setKlassID(assetFileModel.getKlassId());
        assetFile.setExtensionType(assetFileModel.getExtensionType());
        assetFile.setIsExtracted(assetFileModel.getIsExtracted());
        
        IAssetExtensionConfiguration extensionConfiguration = assetFileModel.getExtensionConfiguration();
        Map<String, Object> extensionConfigurationMap = new HashMap<>();
        if (extensionConfiguration != null) {
          extensionConfigurationMap.put(IAssetExtensionConfiguration.EXTRACT_METADATA, extensionConfiguration.getExtractMetadata());
          extensionConfigurationMap.put(IAssetExtensionConfiguration.EXTRACT_RENDITION, extensionConfiguration.getExtractRendition());
        }
        assetFile.setExtensionConfiguration(extensionConfigurationMap);
        
          Map<String, Object> uploadedIconAsset = null;
          try {
            uploadedIconAsset = createFileModelAndUploadAsset(assetFile);
          }
          catch (Exception e) {
            // jobData.getLog().error("ICON UPLOAD ERROR" + e);
          }
          
          // Prepare data for icon entity to store in Orient
          if (uploadedIconAsset != null) {
            Map<String, Object> iconDataMap = new HashMap<>();
            iconDataMap.put(IIconModel.LABEL, null);
            iconDataMap.put(IIconModel.CODE, iconCode);
            iconDataMap.put(IIconModel.ICON_KEY, uploadedIconAsset.get(DAMConstants.ASSET_OBJECT_KEY));
            iconsList.add(iconDataMap);
          }
      }
    }
    
    if (!iconsList.isEmpty()) {
      // save in orintDB
      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("icons", iconsList);
      CSConfigServer.instance().request(requestMap, ORIENT_PLUGIN_FOT_SAVE_ICONS,
          LocaleCatalogDAO.getDefaultRootLocaleCatalog().getLocaleID());
    }
    
    responseModel.setFailure(failure);
    return responseModel;
  }
  
  private IAssetConfigurationDetailsResponseModel prepareAssetConfigInfo()
  {
    IAssetConfigurationDetailsResponseModel assetModel = new AssetConfigurationDetailsResponseModel();
    Map<String, List<IAssetExtensionConfigurationModel>> assetReq = new HashMap<>();
    List<IAssetExtensionConfigurationModel> list = new ArrayList<>();
    IAssetExtensionConfigurationModel assetExtensionModel = new AssetExtensionConfigurationModel();
    assetExtensionModel.setExtension("png");
    list.add(assetExtensionModel);
    IAssetExtensionConfigurationModel svgExtensionModel = new AssetExtensionConfigurationModel();
    svgExtensionModel.setExtension("svg");
    list.add(svgExtensionModel);
    assetReq.put("imageAsset", list);
    assetModel.setNatureType(VertexLabelConstants.ENTITY_TYPE_ICON);
    assetModel.setExtensionConfiguration(assetReq);
    return assetModel;
  }
  
  private Map<String, Object> createFileModelAndUploadAsset(IAssetFileDTO asset) throws Exception
  {
    String fileSourcePath = asset.getFilePath();
    Map<String, Object> fileModelMap = new HashMap<>();
    fileModelMap.put(DAMConstants.FILE_MODEL_PATH, fileSourcePath);
    fileModelMap.put(DAMConstants.FILE_MODEL_IS_EXTRACTED, asset.getIsExtracted());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_TYPE, asset.getExtensionType());
    fileModelMap.put(DAMConstants.FILE_MODEL_KLASS_ID, asset.getKlassID());
    fileModelMap.put(DAMConstants.FILE_MODEL_EXT_CONFIG, asset.getExtensionConfiguration());
    fileModelMap.put(DAMConstants.FILE_MODEL_PHYSICAL_CATALOG_ID, null);
    fileModelMap.put(DAMConstants.FILE_MODEL_ORGANIZATION_ID, null);
    fileModelMap.put(DAMConstants.FILE_MODEL_ENDPOINT_ID, null);
    fileModelMap.put(DAMConstants.FILE_MODEL_CONTAINER, DAMConstants.SWIFT_CONTAINER_ICONS);
    return AssetUtils.handleFile(fileModelMap);
  }
  
}
