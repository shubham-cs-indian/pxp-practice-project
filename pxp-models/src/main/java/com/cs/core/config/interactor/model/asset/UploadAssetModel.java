package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * mode - tells wether asset is uploaded in 'config' or 'runtime'
 *
 * <p>
 * multiPartFileInfoList - a list which contains data of multiple assets
 */
public class UploadAssetModel implements IUploadAssetModel {
  
  private static final long               serialVersionUID       = 1L;
  
  protected String                        mode;
  protected List<IMultiPartFileInfoModel> multiPartFileInfoList;
  protected Boolean                       isUploadedFromInstance = false;
  protected String                        klassId;
  protected List<String>                  collectionIds = new ArrayList<>();
  Map<String, String[]>                   additionalParameterMap           = new HashMap<>();
  
  @Override
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList()
  {
    return multiPartFileInfoList;
  }
  
  @Override
  @JsonDeserialize(contentAs = MultiPartFileInfoModel.class)
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList)
  {
    this.multiPartFileInfoList = multiPartFileInfoList;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
  @Override
  public Boolean getIsUploadedFromInstance()
  {
    return isUploadedFromInstance;
  }
  
  @Override
  public void setIsUploadedFromInstance(Boolean isUploadedFromInstance)
  {
    this.isUploadedFromInstance = isUploadedFromInstance;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }

  @Override
  public List<String> getCollectionIds()
  {
    return collectionIds;
  }

  @Override
  public void setCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }

  @Override
  public Map<String, String[]> getAdditionalParameterMap()
  {
    return additionalParameterMap;
  }

  @Override
  public void setAdditionalParameterMap(Map<String, String[]> additionalParameterMap)
  {
    this.additionalParameterMap = additionalParameterMap;
  }
}
