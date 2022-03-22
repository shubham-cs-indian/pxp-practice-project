package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.configdetails.CoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.ITypesAndCoverFlowModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TypesAndCoverFlowModel implements ITypesAndCoverFlowModel {
  
  private static final long serialVersionUID = 1L;
  protected ICoverFlowModel coverFlowModel;
  protected List<String>    types;
  protected String          assetId;
  
  public ICoverFlowModel getCoverFlowModel()
  {
    return coverFlowModel;
  }
  
  @Override
  @JsonDeserialize(as = CoverFlowModel.class)
  public void setCoverFlowModel(ICoverFlowModel coverFlowModel)
  {
    this.coverFlowModel = coverFlowModel;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public String getAssetId()
  {
    return assetId;
  }
  
  @Override
  public void setAssetId(String assetId)
  {
    this.assetId = assetId;
  }
}
