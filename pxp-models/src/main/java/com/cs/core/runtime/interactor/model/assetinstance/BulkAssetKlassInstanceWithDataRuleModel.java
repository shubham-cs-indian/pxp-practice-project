package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BulkAssetKlassInstanceWithDataRuleModel
    implements IBulkAssetKlassInstanceWithDataRuleModel {
  
  private static final long                                 serialVersionUID = 1L;
  
  protected List<IAssetKlassInstanceWithDataRuleModel>      assetInstances   = new ArrayList<>();
  protected List<String>                                    collectionIds    = new ArrayList<>();
  protected String                                          label;
  protected Map<String, IGetConfigDetailsForCustomTabModel> configDetails;
  
  @Override
  public List<IAssetKlassInstanceWithDataRuleModel> getAssetInstances()
  {
    return assetInstances;
  }
  
  @Override
  public void setAssetInstances(List<IAssetKlassInstanceWithDataRuleModel> assetInstances)
  {
    this.assetInstances = assetInstances;
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Map<String, IGetConfigDetailsForCustomTabModel> getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForCustomTabModel.class)
  public void setConfigDetails(Map<String, IGetConfigDetailsForCustomTabModel> configDetails)
  {
    this.configDetails = configDetails;
  }
}
