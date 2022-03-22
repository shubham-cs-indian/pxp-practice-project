package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IInstanceMigrationRequestModel extends IModel {
  
  String DEPENDENT_ATTRIBUTE_CODES   = "dependentAttributeCodes";
  String CREATION_lANGUAGE           = "creationLanguage";
  String TECHNICAL_VARIANT_KLASS_IDS = "technicalVariantKlassIds";
  
  public List<String> getDependentAttributeCodes();
  
  public void setDependentAttributeCodes(List<String> list);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public List<String> getTechnicalVariantKlassIds();
  
  public void setTechnicalVariantKlassIds(List<String> technicalVariantKlassIds);
}
