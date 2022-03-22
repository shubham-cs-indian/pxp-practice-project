package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IApplyEffectModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String TYPES_TO_APPLY      = "typesToApply";
  public static final String TAXONOMIES_TO_APPLY = "taxonomiesToApply";
  public static final String BASE_TYPE           = "baseType";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getTypesToApply();
  
  public void setTypesToApply(List<String> typesToApply);
  
  public List<String> getTaxonomiesToApply();
  
  public void setTaxonomiesToApply(List<String> taxonomiesToApply);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
