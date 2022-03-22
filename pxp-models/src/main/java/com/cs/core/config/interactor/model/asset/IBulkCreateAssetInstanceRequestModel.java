package com.cs.core.config.interactor.model.asset;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IBulkCreateAssetInstanceRequestModel extends IModel {
  
  public static final String ASSET_KEYS_MODEL_LIST  = "assetKeysModelList";
  public static final String IS_HASH_DUPLICATED_MAP = "isHashDuplicatedMap";
  public static final String COLLECTION_IDS         = "collectionIds";
  
  public List<IAssetKeysModel> getAssetKeysModelList();
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModelList);
  
  public Map<String, Boolean> getIsHashDuplicatedMap();
  public void setIsHashDuplicatedMap(Map<String, Boolean> isHashDuplicatedMap);
  
  public List<String> getCollectionIds();
  public void setCollectionIds(List<String> collectionIds);
}
