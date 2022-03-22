package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITypesAndCoverFlowModel extends IModel {
  
  public static final String COVER_FLOW_MODEL = "coverFlowModel";
  public static final String TYPES            = "types";
  public static final String ASSET_ID         = "assetId";
  
  public ICoverFlowModel getCoverFlowModel();
  
  public void setCoverFlowModel(ICoverFlowModel coverFlowModel);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public String getAssetId();
  
  public void setAssetId(String assetId);
}
