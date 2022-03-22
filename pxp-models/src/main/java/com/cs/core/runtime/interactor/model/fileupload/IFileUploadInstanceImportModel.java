package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.config.interactor.model.articleimportcomponent.IComponentParameterModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFileUploadInstanceImportModel extends IContentIdentifierModel {
  
  public static final String MAPPINGS                  = "mappings";
  public static final String TAG_VALUE_MAPPING         = "tagValuesMapping";
  public static final String ITEM_MAP                  = "itemMap";
  public static final String COMPONENT_PARAMETER_MODEL = "componentParameterModel";
  public static final String USER_ID                   = "userId";
  public static final String IMPORTED_IDS              = "importedIds";
  public static final String IMPORTED_IDS_TO_TRANSFER  = "importedIdsToTransfer";
  public static final String FAILED_IDS                = "failedIds";
  public static final String IN_PROGRESS_IDS           = "inProgressIds";
  public static final String COMPONENT_ID              = "componentId";
  public static final String DATE_ATTRIBUTE_IDS        = "dateAttributeIds";
  public static final String PROPERY_MAPPING           = "propertyMapping";
  public static final String ENDPOINT_ID               = "endpointId";
  public static final String SYSTEM_ID                 = "systemId";
  public static final String PROCESS_INSTANCE_ID       = "processInstanceId";
  public static final String KLASS_INSTANCE_ID         = "klassInstanceId";
  public static final String TYPE                      = "type";
  public static final String ASSET_KEYS_MODEL          = "assetKeysModel";
  public static final String FILE_PATH                 = "filePath";
  public static final String ORIGINAL_INSTANCE_ID      = "originalInstanceId";
  public static final String UI_LANGUAGE               = "uiLanguage";
  public static final String DATA_LANGUAGE             = "dataLanguage";
  
  public IGetMappingForImportResponseModel getMappings();
  
  public void setMappings(IGetMappingForImportResponseModel mappings);
  
  public HashMap<String, HashMap<String, Object>> getTagValuesMapping();
  
  public void setTagValuesMapping(HashMap<String, HashMap<String, Object>> tagValuesMapping);
  
  public Map<String, Object> getItemMap();
  
  public void setItemMap(Map<String, Object> itemMap);
  
  public IComponentParameterModel getComponentParameterModel();
  
  public void setComponentParameterModel(IComponentParameterModel componentParameterModel);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Set<String> getImportedIds();
  
  public void setImportedIds(Set<String> importedIds);
  
  public Set<String> getImportedIdsToTransfer();
  
  public void setImportedIdsToTransfer(Set<String> importedIdsToTransfer);
  
  public Set<String> getFailedIds();
  
  public void setFailedIds(Set<String> failedIds);
  
  public Map<String, String> getInProgressIds();
  
  public void setInProgressIds(Map<String, String> inProgressIds);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public List<String> getDateAttributeIds();
  
  public void setDateAttributeIds(List<String> dateAttributeIds);
  
  public IMappingModel getPropertyMapping();
  
  public void setPropertyMapping(IMappingModel propertyMapping);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getType();
  
  public void setType(String type);
  
  public IAssetKeysModel getAssetKeysModel();
  
  public void setAssetKeysModel(IAssetKeysModel assetKeysModel);
  
  public String getFilePath();
  
  public void setFilePath(String filePath);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
}
