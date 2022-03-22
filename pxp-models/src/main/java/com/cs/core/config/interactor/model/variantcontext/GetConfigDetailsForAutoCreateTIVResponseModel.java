package com.cs.core.config.interactor.model.variantcontext;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class GetConfigDetailsForAutoCreateTIVResponseModel
    implements IGetConfigDetailsForAutoCreateTIVResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap = new HashMap<String, IGetConfigDetailsForCreateVariantModel>();

  @Override
  public Map<String, IGetConfigDetailsForCreateVariantModel> getConfigDetailsMap()
  {
    return configDetailsMap;
  }

  @Override
  @JsonDeserialize(contentAs = GetConfigDetailsForCreateVariantModel.class)
  public void setConfigDetailsMap(
      Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap)
  {
    this.configDetailsMap = configDetailsMap;
  }
  
  
}
