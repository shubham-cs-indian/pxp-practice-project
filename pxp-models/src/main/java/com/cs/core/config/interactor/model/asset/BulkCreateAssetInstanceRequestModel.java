package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BulkCreateAssetInstanceRequestModel implements IBulkCreateAssetInstanceRequestModel {
  
  private static final long     serialVersionUID    = 1L;
  private List<IAssetKeysModel> assetKeysModelList  = new ArrayList<>();
  private Map<String, Boolean>  isHashDuplicatedMap = new HashMap<>();
  private List<String>          collectionIds       = new ArrayList<>();
  
  @Override
  public List<IAssetKeysModel> getAssetKeysModelList()
  {
    return assetKeysModelList;
  }
  
  @Override
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModelList)
  {
    this.assetKeysModelList = assetKeysModelList;
  }
  
  @Override
  public Map<String, Boolean> getIsHashDuplicatedMap()
  {
    return isHashDuplicatedMap;
  }
  
  @Override
  public void setIsHashDuplicatedMap(Map<String, Boolean> isHashDuplicatedMap)
  {
    this.isHashDuplicatedMap = isHashDuplicatedMap;
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
  
}
