package com.cs.core.runtime.interactor.model.variants.detail;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IVariantDetailRequestModel extends IModel {
  
  public static final String KLASS_INSTANCES = "klassInstances";
  
  public static final String VARIANT_DETAILS = "variantDetails";
  
  public List<IVariantDetailBaseRequestModel> getKlassInstances();
  
  public void setKlassInstances(List<IVariantDetailBaseRequestModel> klassInstances);
  
  public Map<String, Object> getVariantDetails();
  
  public void setVariantDetails(Map<String, Object> variantDetails);
}
