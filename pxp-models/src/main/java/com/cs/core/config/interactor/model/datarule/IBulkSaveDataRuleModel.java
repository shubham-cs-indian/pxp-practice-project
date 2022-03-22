package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkSaveDataRuleModel extends IModel {
  
  public static final String ID                    = "id";
  public static final String LABEL                 = "label";
  public static final String CODE                  = "code";
  public static final String TYPE                  = "type";
  public static final String IS_STANDARD           = "isStandard";
  public static final String IS_LANGUAGE_DEPENDENT = "isLanguageDependent";
  public static final String LANGUAGES             = "languages";
  public static final String PHYSICAL_CATALOG_IDS  = "physicalCatalogIds";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public Boolean getIsLanguageDependent();
  
  public void setIsLanguageDependent(Boolean isLanguageDependent);
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
}
