package com.cs.core.config.interactor.model.asset;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUploadAssetModel extends IModel {
  
  public static final String MODE                      = "mode";
  public static final String MULTI_PART_FILE_INFO_LIST = "multiPartFileInfoList";
  public static final String COLLECTION_IDS            = "collectionIds";
  public static final String KLASS_ID                  = "klassId";
  public static final String ARTICLE_INSTANCE_ID       = "articleInstanceId";
  public static final String RELATIONSHIP_ID           = "relationshipId";
  public static final String ADDITIONAL_PARAMETR_MAP   = "additionalParameterMap";
 
  public String getMode();
  
  public void setMode(String mode);
  
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList();
  
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList);
  
  public Boolean getIsUploadedFromInstance();
  
  public void setIsUploadedFromInstance(Boolean isUploadedFromInstance);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public List<String> getCollectionIds();
 
  public void setCollectionIds(List<String> collectionIds);
  
  public Map<String, String[]> getAdditionalParameterMap();
 
  public void setAdditionalParameterMap(Map<String, String[]> parameterMap);
  
  
}
