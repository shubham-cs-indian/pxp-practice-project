package com.cs.core.runtime.interactor.model.variants.detail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class VariantDetailRequestModel implements IVariantDetailRequestModel {
  
  private static final long                      serialVersionUID = 1L;
  protected List<IVariantDetailBaseRequestModel> klassInstances;
  protected Map<String, Object>                  variantDetails;
  
  @Override
  public List<IVariantDetailBaseRequestModel> getKlassInstances()
  {
    return klassInstances;
  }
  
  @JsonDeserialize(contentAs = VariantDetailBaseRequestModel.class)
  @Override
  public void setKlassInstances(List<IVariantDetailBaseRequestModel> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public Map<String, Object> getVariantDetails()
  {
    return variantDetails;
  }
  
  @Override
  public void setVariantDetails(Map<String, Object> variantDetails)
  {
    this.variantDetails = variantDetails;
  }
}
