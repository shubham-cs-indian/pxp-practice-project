package com.cs.core.config.interactor.model.variantcontext;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;

public interface IGetConfigDetailsForAutoCreateTIVResponseModel extends IModel{
  
  public static final String CONFIG_DETAILS_MAP = "configDetailsMap";
  
  public Map<String, IGetConfigDetailsForCreateVariantModel> getConfigDetailsMap();
  public void setConfigDetailsMap(
      Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap);
  
}
