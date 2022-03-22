package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceWithDataRuleModel;

import java.util.Map;

public class AssetKlassInstanceWithDataRuleModel extends KlassInstanceWithDataRuleModel
    implements IAssetKlassInstanceWithDataRuleModel {
  
  protected Map<String, Object> metadata;
  
  @Override
  public Map<String, Object> getMetadata()
  {
    return metadata;
  }
  
  @Override
  public void setMetadata(Map<String, Object> metadata)
  {
    this.metadata = metadata;
  }
}
