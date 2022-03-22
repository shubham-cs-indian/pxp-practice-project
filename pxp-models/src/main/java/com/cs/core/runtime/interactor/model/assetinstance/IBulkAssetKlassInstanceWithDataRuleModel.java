package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;
import java.util.Map;

public interface IBulkAssetKlassInstanceWithDataRuleModel extends IModel {
  
  public static final String ASSET_INSTANCES = "assetInstances";
  public static final String COLLECTION_IDS  = "collectionIds";
  public static final String label           = "label";
  public static final String CONFIG_DETAILS  = "configDetails";
  
  public List<IAssetKlassInstanceWithDataRuleModel> getAssetInstances();
  
  public void setAssetInstances(List<IAssetKlassInstanceWithDataRuleModel> assetInstances);
  
  public List<String> getCollectionIds();
  
  public void setCollectionIds(List<String> collectionIds);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, IGetConfigDetailsForCustomTabModel> getConfigDetails();
  
  public void setConfigDetails(Map<String, IGetConfigDetailsForCustomTabModel> configDetails);
}
