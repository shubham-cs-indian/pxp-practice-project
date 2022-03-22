package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceWithDataRuleModel;

import java.util.Map;

public interface IAssetKlassInstanceWithDataRuleModel extends IKlassInstanceWithDataRuleModel {
  
  public static final String METADATA = "metadata";
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
}
